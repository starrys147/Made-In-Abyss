package com.altnoir.mia;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

// 此处的值应与 META-INF/mods.toml 文件中的条目匹配
@Mod(MIA.MOD_ID)
public class MIA
{
    // 在所有地方定义一个通用的 mod id
    public static final String MOD_ID = "mia";
    // 直接引用一个 slf4j 日志记录器
    private static final Logger LOGGER = LogUtils.getLogger();
    // 创建一个 Deferred Register 用于注册 Block，这些 Block 将在 "mia" 命名空间下注册
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    // 创建一个 Deferred Register 用于注册 Item，这些 Item 将在 "mia" 命名空间下注册
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    // 创建一个 Deferred Register 用于注册 CreativeModeTab，这些 Tab 将在 "mia" 命名空间下注册
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    // 创建一个新的 Block，其 ID 为 "mia:example_block"，结合命名空间和路径
    public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));
    // 创建一个新的 BlockItem，其 ID 为 "mia:example_block"，结合命名空间和路径
    public static final RegistryObject<Item> EXAMPLE_BLOCK_ITEM = ITEMS.register("example_block", () -> new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties()));

    // 创建一个新的食物物品，其 ID 为 "mia:example_item"，营养值为 1，饱腹感为 2
    public static final RegistryObject<Item> EXAMPLE_ITEM = ITEMS.register("example_item", () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEat().nutrition(1).saturationMod(2f).build())));

    // 创建一个新的创意模式标签，其 ID 为 "mia:example_tab"，用于示例物品，并放置在战斗标签之后
    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(EXAMPLE_ITEM.get()); // 将示例物品添加到标签中。对于自己的标签，此方法优于事件
            }).build());

    public MIA()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // 注册 commonSetup 方法以进行模组加载
        modEventBus.addListener(this::commonSetup);

        // 注册 Deferred Register 到模组事件总线，以便注册 Block
        BLOCKS.register(modEventBus);
        // 注册 Deferred Register 到模组事件总线，以便注册 Item
        ITEMS.register(modEventBus);
        // 注册 Deferred Register 到模组事件总线，以便注册标签
        CREATIVE_MODE_TABS.register(modEventBus);

        // 注册我们感兴趣的服务器和其他游戏事件
        MinecraftForge.EVENT_BUS.register(this);

        // 注册物品到创意模式标签
        modEventBus.addListener(this::addCreative);

        // 注册我们的 ForgeConfigSpec，以便 Forge 可以为我们创建和加载配置文件
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // 一些通用设置代码
        LOGGER.info("来自通用设置的问候");

        if (Config.logDirtBlock)
            LOGGER.info("泥土方块 >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("物品 >> {}", item.toString()));
    }

    // 将示例方块物品添加到建筑方块标签
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
            event.accept(EXAMPLE_BLOCK_ITEM);
    }

    // 使用 SubscribeEvent 并让事件总线发现带有 @SubscribeEvent 注解的方法来调用
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // 当服务器启动时执行某些操作
        LOGGER.info("来自服务器启动的问候");
    }

    // 使用 EventBusSubscriber 自动注册类中所有带有 @SubscribeEvent 注解的静态方法
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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
