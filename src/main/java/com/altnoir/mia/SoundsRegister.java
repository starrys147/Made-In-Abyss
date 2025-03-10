package com.altnoir.mia;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundsRegister {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, "mia");
    public static final RegistryObject<SoundEvent> STOP = SOUNDS.register("stop", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("mia", "stop")));
}