package com.github.donmor.killerbunnytweaks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.donmor.killerbunnytweaks.fabric.events.EntityTickCallback;

import net.minecraft.world.entity.Entity;

@Mixin(Entity.class)
public abstract class MixinEntity {
    @Inject(method = "tick()V", at = @At("RETURN"))
    private void atTick(CallbackInfo info) {
        EntityTickCallback.EVENT.invoker().interact((Entity) (Object) this);
    }
}
