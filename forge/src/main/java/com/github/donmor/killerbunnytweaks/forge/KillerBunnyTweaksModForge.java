package com.github.donmor.killerbunnytweaks.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.github.donmor.killerbunnytweaks.KillerBunnyTweaksMod;
import com.github.donmor.killerbunnytweaks.KillerBunnyTweaksMod.KBTConfigIF;

@Mod(KillerBunnyTweaksMod.MOD_ID)
public final class KillerBunnyTweaksModForge {
    public KillerBunnyTweaksModForge() {
        // Submit our event bus to let Architectury API register our content on the
        // right time.
        EventBuses.registerModEventBus(KillerBunnyTweaksMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

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
                }).getRight();
        ModLoadingContext.get().registerConfig(Type.COMMON, config);

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
        KillerBunnyTweaksMod.init();
    }
}
