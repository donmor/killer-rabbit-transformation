package com.github.donmor.killerbunnytweaks.forge.events;

import javax.annotation.Nonnull;

import com.github.donmor.killerbunnytweaks.KBTEvents;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ReloadCallback {
    @SubscribeEvent
    public static void onAddReloadListener(AddReloadListenerEvent event) {
        event.addListener(new ResourceManagerReloadListener() {

            @Override
            public void onResourceManagerReload(@Nonnull ResourceManager arg) {
                KBTEvents.onResourceManagerReload(arg);
            }
        });
    }
}
