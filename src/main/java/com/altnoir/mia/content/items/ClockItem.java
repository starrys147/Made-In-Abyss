package com.altnoir.mia.content.items;

import com.altnoir.mia.content.ability.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ClockItem extends Item {
    public ClockItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level p_41432_, Player p_41433_, @NotNull InteractionHand p_41434_) {
        p_41433_.setItemInHand(p_41434_, p_41433_.getItemInHand(p_41434_));
//        if (!TimeStop.get()) {
//            Minecraft.getInstance().gameRenderer.loadEffect(new ResourceLocation("shaders/post/the_world.json"));
//        }
        if (p_41433_.level().isClientSide)
            TimeStop.abs(p_41433_);
        p_41433_.getCooldowns().addCooldown(this, 10); //物品冷却时间
        return super.use(p_41432_, p_41433_, p_41434_);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack p_41454_) {
        return 10;
    }
}