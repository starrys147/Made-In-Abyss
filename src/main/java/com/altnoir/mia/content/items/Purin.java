package com.altnoir.mia.content.items;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class Purin extends MIABaseItem {
    public Purin() {
        super(new Item.Properties().food(new FoodProperties.Builder()
                .fast().nutrition(2).saturationMod(2f).build()));
    }
}
