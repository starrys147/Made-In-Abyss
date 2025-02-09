package com.altnoir.mia;

import com.altnoir.mia.register.MIAEffects;
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
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.effect.MobEffect;

import javax.annotation.Nonnull;

import java.util.List;

import static com.mojang.text2speech.Narrator.LOGGER;

public class UpwardCurse {
    private double lowY;      // 记录最低Y坐标
    private double checkY;       // 用于检查的高度

    public double getLowY() { return lowY; }
    public void setLowY(double y) { this.lowY = y; }
    public double getCheckY() { return checkY; }
    public void setCheckY(double y) { this.checkY = y; }


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
            tag.putDouble("lowestY", data.lowY);
            tag.putDouble("checkY", data.checkY);
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            data.lowY = nbt.getDouble("lowestY");
            data.checkY = nbt.getDouble("checkY");
        }
    }

    @Mod.EventBusSubscriber(modid = MIA.MOD_ID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof Player) {
                event.addCapability(
                        new ResourceLocation(MIA.MOD_ID, "upward_curse"),
                        new Provider()
                );
            }
        }
        @SubscribeEvent
        public static void onDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
            Player player = event.getEntity();
            player.getCapability(Provider.CAPABILITY).ifPresent(data -> {
                // 切换维度重置高度
                double currentY = player.getY();
                data.setCheckY(currentY);
                data.setLowY(currentY);
            });
        }
        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
            if (event.phase == TickEvent.Phase.START) return;
            Player player = event.player;

            if (player.tickCount % 60 != 0) return;
            if (player.level().isClientSide) return;

            player.getCapability(Provider.CAPABILITY).ifPresent(data -> {
                double nowY = player.getY();
                if (data.getCheckY() == 0) {
                    data.setCheckY(nowY);
                    data.setLowY(nowY);
                    return;
                }

                if (nowY < data.getLowY()) {
                    data.setLowY(nowY);
                }

                double relativeHeight = nowY - data.getLowY();
                LOGGER.info("Height: " + relativeHeight);

                if (relativeHeight >= 10.0) {
                    ResourceLocation dimensionId = player.level().dimension().location();
                    List<EffectConfigManager.EffectConfig> effects = EffectConfigManager.getEffects(dimensionId);

                    if (!effects.isEmpty()) {
                        for (EffectConfigManager.EffectConfig config : effects) {
                            MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(config.id));
                            if (effect != null && !player.hasEffect(effect)) { // 防止重复添加
                                player.addEffect(new MobEffectInstance(
                                        effect,
                                        config.duration,
                                        config.amplifier,
                                        false,
                                        true
                                ));
                            } else {
                                LOGGER.warn("无效的药水效果ID: {}", config.id);
                            }
                        }
                        data.setCheckY(nowY);
                        data.setLowY(nowY);
                    }
                }
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