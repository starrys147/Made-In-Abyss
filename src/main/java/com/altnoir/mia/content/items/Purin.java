package com.altnoir.mia.content.items;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class Purin extends MIABaseItem {
    public Purin() {
        super(new Properties().food(new FoodProperties.Builder()
                .fast().nutrition(2).saturationModifier(2f).build()));
    }
}
