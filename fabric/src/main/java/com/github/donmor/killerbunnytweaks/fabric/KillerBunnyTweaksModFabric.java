package com.github.donmor.killerbunnytweaks.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.config.ModConfig.Type;

import com.github.donmor.killerbunnytweaks.KBTEvents;
import com.github.donmor.killerbunnytweaks.KillerBunnyTweaksMod;
import com.github.donmor.killerbunnytweaks.KillerBunnyTweaksMod.KBTConfigIF;
import com.github.donmor.killerbunnytweaks.fabric.events.EntityTickCallback;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;

public final class KillerBunnyTweaksModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        class KBTModConfig {
        }
        ForgeConfigSpec config = new ForgeConfigSpec.Builder()
                .configure(builder -> {
                    KillerBunnyTweaksMod.CONF_SPEC.forEach((t, u) -> {
                        builder.comment(u.left).translation(u.middle).push(t);
                        u.right.forEach((t1, u1) -> {
                            if (u1.right instanceof Integer v)
                                builder.comment(u1.left).translation(u1.middle).define(t1, v);
                            else if (u1.right instanceof Boolean v)
                                builder.comment(u1.left).translation(u1.middle).define(t1, v);
                        });
                        builder.pop();
                    });
                    return new KBTModConfig();
                }).getValue();
        ForgeConfigRegistry.INSTANCE.register(KillerBunnyTweaksMod.MOD_ID, Type.COMMON, config);

        ResourceManagerHelper.get(PackType.SERVER_DATA)
                .registerReloadListener(new SimpleSynchronousResourceReloadListener() {
                    @Override
                    public ResourceLocation getFabricId() {
                        return new ResourceLocation(KillerBunnyTweaksMod.MOD_ID, "modded_mobs");
                    }

                    @Override
                    public void onResourceManagerReload(ResourceManager resourceManager) {
                        KBTEvents.onResourceManagerReload(resourceManager);
                    }
                });

        KillerBunnyTweaksMod.options = new KBTConfigIF() {

            @Override
            public int TransformingChance() {
                return config.getValues().get("General.TransformingChance") instanceof ConfigValue<?> vw
                        && vw.get() instanceof Integer value ? value : 0;
            }

            @Override
            public boolean CanWeaknessTransform() {
                return config.getValues().get("General.CanWeaknessTransform") instanceof ConfigValue<?> vw
                        && vw.get() instanceof Boolean value ? value : false;
            }

            @Override
            public boolean BunnyAttacksVillagerLikes() {
                return config.getValues().get("BunnyTargeting.BunnyAttacksVillagerLikes") instanceof ConfigValue<?> vw
                        && vw.get() instanceof Boolean value ? value : false;
            }

            @Override
            public boolean BunnyAttacksPlayerLikes() {
                return config.getValues().get("BunnyTargeting.BunnyAttacksPlayerLikes") instanceof ConfigValue<?> vw
                        && vw.get() instanceof Boolean value ? value : false;
            }

            @Override
            public boolean BunnyAttacksCreepers() {
                return config.getValues().get("BunnyTargeting.BunnyAttacksCreepers") instanceof ConfigValue<?> vw
                        && vw.get() instanceof Boolean value ? value : false;
            }

            @Override
            public boolean BunnyAttacksPiglins() {
                return config.getValues().get("BunnyTargeting.BunnyAttacksPiglins") instanceof ConfigValue<?> vw
                        && vw.get() instanceof Boolean value ? value : false;
            }

            @Override
            public int ZombieHeadDropChance() {
                return config.getValues().get("BunnyHeadRipping.ZombieHeadDropChance") instanceof ConfigValue<?> vw
                        && vw.get() instanceof Integer value ? value : 0;
            }

            @Override
            public int SkeletonHeadDropChance() {
                return config.getValues().get("BunnyHeadRipping.SkeletonHeadDropChance") instanceof ConfigValue<?> vw
                        && vw.get() instanceof Integer value ? value : 0;
            }

            @Override
            public int WitherSkeletonHeadDropChance() {
                return config.getValues()
                        .get("BunnyHeadRipping.WitherSkeletonHeadDropChance") instanceof ConfigValue<?> vw
                        && vw.get() instanceof Integer value ? value : 0;
            }

            @Override
            public int CreeperHeadDropChance() {
                return config.getValues().get("BunnyHeadRipping.CreeperHeadDropChance") instanceof ConfigValue<?> vw
                        && vw.get() instanceof Integer value ? value : 0;
            }

            @Override
            public int PiglinHeadDropChance() {
                return config.getValues().get("BunnyHeadRipping.PiglinHeadDropChance") instanceof ConfigValue<?> vw
                        && vw.get() instanceof Integer value ? value : 0;
            }

            @Override
            public int DragonHeadDropChance() {
                return config.getValues().get("BunnyHeadRipping.DragonHeadDropChance") instanceof ConfigValue<?> vw
                        && vw.get() instanceof Integer value ? value : 0;
            }

            @Override
            public int PlayerHeadDropChance() {
                return config.getValues().get("BunnyHeadRipping.PlayerHeadDropChance") instanceof ConfigValue<?> vw
                        && vw.get() instanceof Integer value ? value : 0;
            }
        };
        EntityTickCallback.EVENT.register(entity -> {
            if (entity != null)
                KBTEvents.OnEntityTick(entity);
            return InteractionResult.PASS;
        });
        KillerBunnyTweaksMod.init();
    }
}
