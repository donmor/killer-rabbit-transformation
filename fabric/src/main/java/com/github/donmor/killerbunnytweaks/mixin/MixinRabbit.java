package com.github.donmor.killerbunnytweaks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.donmor.killerbunnytweaks.KBTEvents;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.animal.Rabbit;

@Mixin(Rabbit.class)
public abstract class MixinRabbit {
    @Inject(method = "readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V", at = @At("RETURN"))
    private void atReadAdditionalSaveData(CompoundTag compoundTag, CallbackInfo info) {
        Rabbit e = (Rabbit) (Object) this;
        KBTEvents.OnEntityAdd(e, e.getLevel());
    }
}
