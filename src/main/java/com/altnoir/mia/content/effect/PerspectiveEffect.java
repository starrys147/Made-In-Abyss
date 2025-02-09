package com.altnoir.mia.content.effect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class ExampleEffect extends MIABaseEffect{
    public ExampleEffect() {
        super(MobEffectCategory.BENEFICIAL, 0XFF00);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier){
        super.applyEffectTick(entity, amplifier);

    }
}

