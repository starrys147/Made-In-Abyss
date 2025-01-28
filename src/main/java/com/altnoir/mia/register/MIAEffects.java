package com.altnoir.mia.register;

import com.altnoir.mia.MIA;
import com.altnoir.mia.content.effect.ExampleEffect;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MIAEffects {
    private static final DeferredRegister<MobEffect> REGISTER =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, MIA.MOD_ID);

    public static final DeferredHolder<MobEffect, ExampleEffect> EXAMPLE_EFFECT = REGISTER.register("example_effect", ExampleEffect::new);

    public static void register(IEventBus modEventBus){
        REGISTER.register(modEventBus);
    }
}
