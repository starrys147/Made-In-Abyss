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
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

import static com.mojang.text2speech.Narrator.LOGGER;

public class UpwardCurse {
    private double lowestY;      // 记录最低Y坐标
    private double checkY;       // 用于检查的高度
    private int tickCounter;     // 计时器

    public double getLowestY() { return lowestY; }
    public void setLowestY(double y) { this.lowestY = y; }
    public double getCheckY() { return checkY; }
    public void setCheckY(double y) { this.checkY = y; }
    public int getTickCounter() { return tickCounter; }
    public void setTickCounter(int count) { this.tickCounter = count; }

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
            tag.putDouble("lowestY", data.lowestY);
            tag.putDouble("checkY", data.checkY);
            tag.putInt("tickCounter", data.tickCounter);
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            data.lowestY = nbt.getDouble("lowestY");
            data.checkY = nbt.getDouble("checkY");
            data.tickCounter = nbt.getInt("tickCounter");
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
                data.setLowestY(currentY);
                data.setTickCounter(0);
            });
        }
        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
            if (event.phase == TickEvent.Phase.START) return;
            Player player = event.player;

            if (player.level().isClientSide) return;

            player.getCapability(Provider.CAPABILITY).ifPresent(data -> {
                // 计数器递增
                int currentCount = data.getTickCounter() + 1;
                data.setTickCounter(currentCount);

                // 未到3秒时跳过
                if (currentCount < 60) return;

                // 到达3秒后重置并执行检查
                data.setTickCounter(0);
                double currentY = player.getY();

                // 初始化基准高度
                if (data.getCheckY() == 0) {
                    data.setCheckY(currentY);
                    data.setLowestY(currentY);
                    return;
                }

                // 更新最低点
                if (currentY < data.getLowestY()) {
                    data.setLowestY(currentY);
                }

                // 计算相对高度
                double relativeHeight = currentY - data.getLowestY();
                if (relativeHeight >= 10.0) {
                    player.addEffect(new MobEffectInstance(
                            MobEffects.CONFUSION,
                            600,
                            0,
                            false,
                            true
                    ));
                    // 重置基准高度
                    data.setCheckY(currentY);
                    data.setLowestY(currentY);
                }

                LOGGER.info("Height: " + relativeHeight);
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