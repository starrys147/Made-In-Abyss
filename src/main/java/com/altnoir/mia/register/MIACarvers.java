package com.altnoir.mia.register;

import com.altnoir.mia.MIA;
import com.altnoir.mia.content.worldgen.carver.CentralHoleCarver;
import com.altnoir.mia.content.worldgen.carver.CentralHoleCarverConfiguration;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.carver.CarverConfiguration;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MIACarvers {
    public static final DeferredRegister<WorldCarver<?>> CARVERS = DeferredRegister.create(BuiltInRegistries.CARVER, MIA.MOD_ID);

    public static final DeferredHolder<WorldCarver<?>, CentralHoleCarver> CENTRAL_HOLE_CARVER = register("abyss_central_hole",
            () -> new CentralHoleCarver(CentralHoleCarverConfiguration.CODEC));

    private static <C extends CarverConfiguration, F extends WorldCarver<C>> DeferredHolder<WorldCarver<?>,F> register(String pKey, final Supplier<F> pCarver) {
        return CARVERS.register(pKey, pCarver);
    }
    public static void register(IEventBus modEventBus){
        CARVERS.register(modEventBus);
    }
}
