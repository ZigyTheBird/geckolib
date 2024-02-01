package software.bernie.geckolib.services;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.network.GeckoLibNetwork;
import software.bernie.geckolib.network.SerializableDataTicket;
import software.bernie.geckolib.network.packet.BlockEntityAnimDataSyncPacket;
import software.bernie.geckolib.network.packet.BlockEntityAnimTriggerPacket;
import software.bernie.geckolib.network.packet.EntityAnimDataSyncPacket;
import software.bernie.geckolib.network.packet.EntityAnimTriggerPacket;

public class ForgeGeckoLibNetworking implements GeckoLibNetworking {

    @Override
    public <D> void syncBlockEntityAnimData(BlockPos pos, SerializableDataTicket<D> dataTicket, D data, Level serverLevel){
        GeckoLibNetwork.send(new BlockEntityAnimDataSyncPacket<>(pos, dataTicket, data),
                PacketDistributor.TRACKING_CHUNK.with(serverLevel.getChunkAt(pos)));
    }

    @Override
    public void blockEntityAnimTrigger(BlockPos pos, @Nullable String controllerName, String animName, Level serverLevel){
        GeckoLibNetwork.send(new BlockEntityAnimTriggerPacket<>(pos, controllerName, animName),
                PacketDistributor.TRACKING_CHUNK.with(serverLevel.getChunkAt(pos)));
    }

    @Override
    public <D> void syncEntityAnimData(Entity entity, SerializableDataTicket<D> dataTicket, D data){
        GeckoLibNetwork.send(new EntityAnimDataSyncPacket<>(entity.getId(), dataTicket, data), PacketDistributor.TRACKING_ENTITY_AND_SELF.with(entity));
    }

    @Override
    public void entityAnimTrigger(Entity entity, @Nullable String controllerName, String animName){
        GeckoLibNetwork.send(new EntityAnimTriggerPacket<>(entity.getId(), controllerName, animName), PacketDistributor.TRACKING_ENTITY_AND_SELF.with(entity));
    }
}
