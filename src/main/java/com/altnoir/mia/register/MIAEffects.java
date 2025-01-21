package com.altnoir.mia.register;

import com.altnoir.mia.MIA;
import com.altnoir.mia.content.effect.ExampleEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MIAEffects {
    private static final DeferredRegister<MobEffect> REGISTER =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MIA.MOD_ID);

    public static final RegistryObject<MobEffect> EXAMPLE_EFFECT = REGISTER.register("example_effect", ExampleEffect::new);

    public static void register(IEventBus modEventBus){
        REGISTER.register(modEventBus);
    }
}
