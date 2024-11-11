package com.github.donmor.killerbunnytweaks.forge.events;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

import com.github.donmor.killerbunnytweaks.KBTEvents;

@Mod.EventBusSubscriber
public class EntityTickCallback {
    @SubscribeEvent
    public static void onEntityTick(LivingTickEvent event) {
        execute(event, event.getEntity());
    }

    private static void execute(@Nullable Event event, Entity entity) {
        if (entity == null)
            return;
        KBTEvents.OnEntityTick(entity);
    }
}