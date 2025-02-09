package com.altnoir.mia.register;

import com.altnoir.mia.MIA;
import com.altnoir.mia.content.effect.ExampleEffect;
import com.altnoir.mia.content.effect.MIABaseEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
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


    // 确保效果已正确注册：
    public static final RegistryObject<MobEffect> VISUAL_DISTORTION = REGISTER.register(
            "visual_distortion",
            () -> new VisualDistortionEffect()
    );

    // 自定义效果类需要继承MobEffect
    public static class VisualDistortionEffect extends MobEffect {
        public VisualDistortionEffect() {
            super(MobEffectCategory.HARMFUL, 0x00FF00);
        }

        @Override
        public boolean isDurationEffectTick(int duration, int amplifier) {
            return true;
        }
    }
}
