package com.github.donmor.killerbunnytweaks.forge.events;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraft.world.entity.Entity;

import com.github.donmor.killerbunnytweaks.KBTEvents;

@Mod.EventBusSubscriber
public class EntityTickCallback {
    @SubscribeEvent
    public static void onEntityTick(LivingTickEvent event) {
        Entity entity = event.getEntity();
        if (entity == null)
            return;
        KBTEvents.OnEntityTick(entity);
    }
}