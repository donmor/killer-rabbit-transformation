package com.github.donmor.killerbunnytweaks.fabric.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;

/**
 * Callback for entity ticking.
 * Invoked at the end of original procedure.
 * Treats any returned InteractionResult as PASS.
 **/
public interface EntityTickCallback {
    Event<EntityTickCallback> EVENT = EventFactory.createArrayBacked(EntityTickCallback.class, listeners -> entity -> {
        for (EntityTickCallback listener : listeners) {
            InteractionResult result = listener.interact(entity);
            if (result != InteractionResult.PASS)
                return result;
        }
        return InteractionResult.PASS;
    });

    InteractionResult interact(Entity entity);
}