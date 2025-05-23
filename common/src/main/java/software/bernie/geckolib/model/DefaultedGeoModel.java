package software.bernie.geckolib.model;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.base.GeoRenderState;

/**
 * Defaulted model class for GeckoLib models
 * <p>
 * This class allows for minimal boilerplate when implementing basic models, and saves on new classes
 * <p>
 * Additionally, it encourages consistency and sorting of asset paths.
 */
public abstract class DefaultedGeoModel<T extends GeoAnimatable> extends GeoModel<T> {
	private ResourceLocation modelPath;
	private ResourceLocation texturePath;
	private ResourceLocation animationsPath;

	/**
	 * Create a new instance of this model class
	 * <p>
	 * The asset path should be the truncated relative path from the base folder
	 * <p>
	 * E.G.
	 * <pre>{@code new ResourceLocation("myMod", "animals/red_fish")}</pre>
	 */
	public DefaultedGeoModel(ResourceLocation assetSubpath) {
		this.modelPath = buildFormattedModelPath(assetSubpath);
		this.texturePath = buildFormattedTexturePath(assetSubpath);
		this.animationsPath = buildFormattedAnimationPath(assetSubpath);
	}

	/**
	 * Changes the constructor-defined model path for this model to an alternate
	 * <p>
	 * This is useful if your animatable shares a model path with another animatable that differs in path to the texture and animations for this model
	 */
	public DefaultedGeoModel<T> withAltModel(ResourceLocation altPath) {
		this.modelPath = buildFormattedModelPath(altPath);

		return this;
	}

	/**
	 * Changes the constructor-defined animations path for this model to an alternate
	 * <p>
	 * This is useful if your animatable shares an animations path with another animatable that differs in path to the model and texture for this model
	 */
	public DefaultedGeoModel<T> withAltAnimations(ResourceLocation altPath) {
		this.animationsPath = buildFormattedAnimationPath(altPath);

		return this;
	}

	/**
	 * Changes the constructor-defined texture path for this model to an alternate
	 * <p>
	 * This is useful if your animatable shares a texture path with another animatable that differs in path to the model and animations for this model
	 */
	public DefaultedGeoModel<T> withAltTexture(ResourceLocation altPath) {
		this.texturePath = buildFormattedTexturePath(altPath);

		return this;
	}

	/**
	 * Constructs a defaulted resource path for a geo.json file based on the input namespace and subpath, automatically using the {@link DefaultedGeoModel#subtype() subtype}
	 *
	 * @param basePath The base path of your resource. E.G. <pre>{@code new ResourceLocation(MyMod.MOD_ID, "animal/goat")}</pre>
	 * @return The formatted model resource path based on recommended defaults. E.G. <pre>{@code "mymod:entity/animal/goat"}</pre>
	 */
	public ResourceLocation buildFormattedModelPath(ResourceLocation basePath) {
		return basePath.withPrefix(subtype() + "/");
	}

	/**
	 * Constructs a defaulted resource path for a animation.json file based on the input namespace and subpath, automatically using the {@link DefaultedGeoModel#subtype() subtype}
	 *
	 * @param basePath The base path of your resource. E.G. <pre>{@code new ResourceLocation(MyMod.MOD_ID, "animal/goat")}</pre>
	 * @return The formatted animation resource path based on recommended defaults. E.G. <pre>{@code "mymod:entity/animal/goat"}</pre>
	 */
	public ResourceLocation buildFormattedAnimationPath(ResourceLocation basePath) {
		return basePath.withPrefix(subtype() + "/");
	}

	/**
	 * Constructs a defaulted resource path for a geo.json file based on the input namespace and subpath, automatically using the {@link DefaultedGeoModel#subtype() subtype}
	 *
	 * @param basePath The base path of your resource. E.G. <pre>{@code new ResourceLocation(MyMod.MOD_ID, "animal/goat")}</pre>
	 * @return The formatted texture resource path based on recommended defaults. E.G. <pre>{@code "mymod:textures/entity/animal/goat.png"}</pre>
	 */
	public ResourceLocation buildFormattedTexturePath(ResourceLocation basePath) {
		return basePath.withPath("textures/" + subtype() + "/" + basePath.getPath() + ".png");
	}

	/**
	 * Returns the subtype string for this type of model
	 * <p>
	 * This allows for sorting of asset files into neat subdirectories for clean management
	 * <p>
	 * Examples:
	 * <ul>
	 *     <li>"entity"</li>
	 *     <li>"block"</li>
	 *     <li>"item"</li>
	 * </ul>
	 */
	protected abstract String subtype();

	@Override
	public ResourceLocation getModelResource(GeoRenderState renderState) {
		return this.modelPath;
	}

	@Override
	public ResourceLocation getTextureResource(GeoRenderState renderState) {
		return this.texturePath;
	}

	@Override
	public ResourceLocation getAnimationResource(T animatable) {
		return this.animationsPath;
	}
}
