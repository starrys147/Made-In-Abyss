package com.altnoir.mia.content.effect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class PerspectiveEffect extends MIABaseEffect{
    public PerspectiveEffect() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier){
        super.applyEffectTick(entity, amplifier);

    }
}

