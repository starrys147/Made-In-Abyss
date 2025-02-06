package com.altnoir.mia;

import com.altnoir.mia.register.*;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

// 此处的值应与 META-INF/mods.toml 文件中的条目匹配
@Mod(MIA.MOD_ID)
public class MIA {
    // 在所有地方定义一个通用的 mod id
    public static final String MOD_ID = "mia";
    // 直接引用一个 slf4j 日志记录器
    private static final Logger LOGGER = LogUtils.getLogger();

    public MIA() {
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // 注册 commonSetup 方法以进行模组加载
        modEventBus.addListener(this::commonSetup);
        MIATabs.register(modEventBus);
        MIABlocks.register(modEventBus);
        MIAItems.register(modEventBus);
        MIAEffects.register(modEventBus);
        MIACarvers.register(modEventBus);
        MIABiomeModifierConfig.register(modEventBus);

        // 注册我们感兴趣的服务器和其他游戏事件
        MinecraftForge.EVENT_BUS.register(this);

        // 注册我们的 ForgeConfigSpec，以便 Forge 可以为我们创建和加载配置文件
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        // MinecraftForge.EVENT_BUS.register(new HoleGenerator());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // 一些通用设置代码
        LOGGER.info("来自通用设置的问候");

        if (Config.logDirtBlock)
            LOGGER.info("泥土方块 >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("物品 >> {}", item.toString()));
    }

    // 使用 SubscribeEvent 并让事件总线发现带有 @SubscribeEvent 注解的方法来调用
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // 当服务器启动时执行某些操作
        LOGGER.info("来自服务器启动的问候");
    }

    // 使用 EventBusSubscriber 自动注册类中所有带有 @SubscribeEvent 注解的静态方法
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // 一些客户端设置代码
            LOGGER.info("来自客户端设置的问候");
            LOGGER.info("MINECRAFT 名称 >> {}", Minecraft.getInstance().getUser().getName());
        }
    }

    // 上升诅咒！
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class PlayerMovementEvents {
        private static final ResourceLocation ALLURING_FOREST_DIMENSION =
                new ResourceLocation("mia", "alluring_forest"); // 维度ID
        private static final int Y_THRESHOLD = 10; // 诅咒触发高度
        private static final MobEffectInstance UPWARD_CURSE =
                new MobEffectInstance(MobEffects.CONFUSION, 200, 0); // 诅咒效果
        private static int tickCounter = 0; // 添加计数器
        private static int lowestY = Integer.MAX_VALUE; // 添加最低Y轴位置记录

        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                tickCounter++; // 每tick增加计数器

                if (tickCounter >= 60) { // 每3秒执行一次
                    Player player = event.player; // 初始化player变量
                    ResourceLocation dimensionId = player.level().dimension().location();
                    // LOGGER.info("玩家在 {} dimension", dimensionId);
                    if (dimensionId.equals(ALLURING_FOREST_DIMENSION)) {
                        int currentY = (int) player.getY();
                        int previousY = player.getPersistentData().getInt("lastY");

                        if (currentY < lowestY) {
                            lowestY = currentY;
                        }

                        if (currentY - previousY >= Y_THRESHOLD) {
                            player.addEffect(UPWARD_CURSE); // 施加诅咒效果
                            player.getPersistentData().putInt("lastY", currentY);
                            lowestY = currentY; // 更新Y轴位置
                        }
                    }
                    tickCounter = 0; // 重置计数器
                }
            }
        }
    }
}