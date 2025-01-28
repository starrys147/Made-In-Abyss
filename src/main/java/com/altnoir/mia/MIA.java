package com.altnoir.mia;

import com.altnoir.mia.register.*;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

// 此处的值应与 META-INF/mods.toml 文件中的条目匹配
@Mod(MIA.MOD_ID)
public class MIA {
    // 在所有地方定义一个通用的 mod id
    public static final String MOD_ID = "mia";
    // 直接引用一个 slf4j 日志记录器
    private static final Logger LOGGER = LogUtils.getLogger();

    public MIA(IEventBus modEventBus, ModContainer modContainer)
    {
        // 注册 commonSetup 方法以进行模组加载
        modEventBus.addListener(this::commonSetup);
        MIATabs.register(modEventBus);
        MIABlocks.register(modEventBus);
        MIAItems.register(modEventBus);
        MIAEffects.register(modEventBus);
        MIACarvers.register(modEventBus);

        // 注册我们感兴趣的服务器和其他游戏事件
        NeoForge.EVENT_BUS.register(this);

        // 注册我们的 ForgeConfigSpec，以便 Forge 可以为我们创建和加载配置文件
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        //MinecraftForge.EVENT_BUS.register(new HoleGenerator());
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // 一些通用设置代码
        LOGGER.info("来自通用设置的问候");

        if (Config.logDirtBlock)
            LOGGER.info("泥土方块 >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("物品 >> {}", item.toString()));
    }

    // 使用 SubscribeEvent 并让事件总线发现带有 @SubscribeEvent 注解的方法来调用
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // 当服务器启动时执行某些操作
        LOGGER.info("来自服务器启动的问候");
    }

    // 使用 EventBusSubscriber 自动注册类中所有带有 @SubscribeEvent 注解的静态方法
    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // 一些客户端设置代码
            LOGGER.info("来自客户端设置的问候");
            LOGGER.info("MINECRAFT 名称 >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
