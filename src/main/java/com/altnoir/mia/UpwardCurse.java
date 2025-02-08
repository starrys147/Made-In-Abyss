package com.altnoir.mia;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

public class UpwardCurse {
    private double lastY;
    private double accumulatedRise;

    public double getLastY() { return lastY; }
    public void setLastY(double lastY) { this.lastY = lastY; }
    public double getAccumulatedRise() { return accumulatedRise; }
    public void setAccumulatedRise(double accumulatedRise) { this.accumulatedRise = accumulatedRise; }

    public static class Provider implements ICapabilitySerializable<CompoundTag> {
        public static final Capability<UpwardCurse> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

        private final UpwardCurse data = new UpwardCurse();
        private final LazyOptional<UpwardCurse> optional = LazyOptional.of(() -> data);

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
            return CAPABILITY.orEmpty(cap, optional);
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putDouble("lastY", data.lastY);
            tag.putDouble("accumulatedRise", data.accumulatedRise);
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            data.lastY = nbt.getDouble("lastY");
            data.accumulatedRise = nbt.getDouble("accumulatedRise");
        }
    }

    @Mod.EventBusSubscriber(modid = MIA.MOD_ID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof Player) {
                event.addCapability(
                        new ResourceLocation(MIA.MOD_ID, "rise_data"),
                        new Provider()
                );
            }
        }

        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
            if (event.phase == TickEvent.Phase.START) return;
            Player player = event.player;

            if (player.level().isClientSide) return;

            player.getCapability(Provider.CAPABILITY).ifPresent(data -> {
                double currentY = player.getY();

                if (data.getLastY() == 0) {
                    data.setLastY(currentY);
                    return;
                }

                double deltaY = currentY - data.getLastY();
                if (deltaY > 0) {
                    data.setAccumulatedRise(data.getAccumulatedRise() + deltaY);
                }

                if (data.getAccumulatedRise() >= 10.0) {
                    player.addEffect(new MobEffectInstance(
                            MobEffects.CONFUSION,
                            600,
                            0,
                            false,
                            true
                    ));
                    data.setAccumulatedRise(data.getAccumulatedRise() - 10.0);
                }

                data.setLastY(currentY);
            });
        }
    }

    @Mod.EventBusSubscriber(modid = MIA.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class CapabilityRegistrar {
        @SubscribeEvent
        public static void registerCapabilities(RegisterCapabilitiesEvent event) {
            event.register(UpwardCurse.class);
        }
    }
}