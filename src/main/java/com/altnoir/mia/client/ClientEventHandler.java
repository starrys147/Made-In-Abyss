package com.altnoir.mia.client;

import com.altnoir.mia.MIA;
import com.altnoir.mia.register.MIAEffects;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MIA.MOD_ID, value = Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void onRenderLivingPre(RenderLivingEvent.Pre<?, ?> event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.player.hasEffect(MIAEffects.PERSPECTIVE.get())) {
            Entity entity = event.getEntity();
            double scale = RenderUtils.calculateScale(entity);

            PoseStack poseStack = event.getPoseStack();
            poseStack.pushPose();
            poseStack.scale((float) scale, (float) scale, (float) scale);
        }
    }
    @SubscribeEvent
    public static void onRenderLivingPost(RenderLivingEvent.Post<?, ?> event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.player.hasEffect(MIAEffects.PERSPECTIVE.get())) {
            event.getPoseStack().popPose();
        }
    }
}
