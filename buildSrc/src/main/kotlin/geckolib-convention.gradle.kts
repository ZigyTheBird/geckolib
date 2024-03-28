plugins {
    `java`
    `maven-publish`
    `idea`
    `eclipse`
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(17)

    withSourcesJar()
    withJavadocJar()
}

val libs = project.versionCatalogs.find("libs")

val modId: String by project
val modDisplayName: String by project
val modAuthors: String by project
val modLicense: String by project
val modDescription: String by project
val modVersion = libs.get().findVersion("geckolib").get()
val mcVersion = libs.get().findVersion("minecraft").get()
val forgeVersion = libs.get().findVersion("forge").get()
val forgeVersionRange = libs.get().findVersion("forge.range").get()
val fmlVersionRange = libs.get().findVersion("forge.fml.range").get()
val mcVersionRange = libs.get().findVersion("minecraft.range").get()
val fapiVersion = libs.get().findVersion("fabric.api").get()
val fabricVersion = libs.get().findVersion("fabric").get()
val neoforgeVersion = libs.get().findVersion("neoforge").get()
val neoforgeLoaderVersionRange = libs.get().findVersion("neoforge.loader.range").get()

tasks.withType<Jar>().configureEach {
    from(rootProject.file("LICENSE")) {
        rename { "${it}_${modDisplayName}" }
    }

    manifest {
        attributes(mapOf(
                "Specification-Title"     to modDisplayName,
                "Specification-Vendor"    to modAuthors,
                "Specification-Version"   to archiveVersion,
                "Implementation-Title"    to modDisplayName,
                "Implementation-Version"  to archiveVersion,
                "Implementation-Vendor"   to modAuthors,
                "Built-On-Minecraft"      to mcVersion
        ))
    }
}

tasks.withType<JavaCompile>().configureEach {
    this.options.encoding = "UTF-8"
    this.options.getRelease().set(17)
}

tasks.withType<ProcessResources>().configureEach {
    val expandProps = mapOf(
            "version" to modVersion,
            "group" to project.group, // Else we target the task's group.
            "minecraft_version" to mcVersion,
            "forge_version" to forgeVersion,
            "forge_loader_range" to fmlVersionRange,
            "forge_version_range" to forgeVersionRange,
            "minecraft_version_range" to mcVersionRange,
            "fabric_api_version" to fapiVersion,
            "fabric_loader_version" to fabricVersion,
            "mod_display_name" to modDisplayName,
            "mod_authors" to modAuthors,
            "mod_id" to modId,
            "mod_license" to modLicense,
            "mod_description" to modDescription,
            "neoforge_version_range" to neoforgeVersion,
            "neoforge_loader_range" to neoforgeLoaderVersionRange
    )

    filesMatching(listOf("pack.mcmeta", "fabric.mod.json", "META-INF/mods.toml", "*.mixins.json")) {
        expand(expandProps)
    }

    inputs.properties(expandProps)
}

// Disables Gradle's custom module metadata from being published to maven. The
// metadata includes mapped dependencies which are not reasonably consumable by
// other mod developers.
tasks.withType<GenerateModuleMetadata>().configureEach {
    enabled = false
}

publishing {
    repositories {
        if (System.getenv("cloudUsername") == null && System.getenv("cloudPassword") == null) {
            mavenLocal()
        }
        else maven {
            name = "cloudsmith"
            url = uri("https://maven.cloudsmith.io/geckolib3/geckolib/")

            credentials {
                username = System.getenv("cloudUsername")
                password = System.getenv("cloudPassword")
            }
        }
    }
}