package com.altnoir.mia.core.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.Queue;

@Mod.EventBusSubscriber
public class RenderUtils {

    static Map<ParticleRenderType, Queue<Particle>> ps = null;

    private static final double BASE_DISTANCE = 5.0;

    public static double calculateScale(Entity entity) {
        Minecraft mc = Minecraft.getInstance();
        Vec3 cameraPos = mc.gameRenderer.getMainCamera().getPosition();
        Vec3 entityPos = entity.position();
        double distance = cameraPos.distanceTo(entityPos);
        double scale = distance / BASE_DISTANCE;
        return Math.max(scale, 1);
    }
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void gui(RenderGuiOverlayEvent event) {
        GuiGraphics guiGraphics = new GuiGraphics(Minecraft.getInstance(), Minecraft.getInstance().renderBuffers().bufferSource());
        PoseStack stack = new PoseStack();
        stack.pushPose();
        stack.scale(0.01f, 0.01f, 0.01f);
        guiGraphics.renderItem(new ItemStack(Items.ENCHANTED_GOLDEN_APPLE), -13, -12);
        stack.popPose();
    }
}
