package com.github.donmor.killerbunnytweaks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutableTriple;

import dev.architectury.event.events.common.EntityEvent;
import net.minecraft.world.entity.EntityType;

public final class KillerBunnyTweaksMod {
        public static final String MOD_ID = "killer_rabbit_transformation";

        public static enum ModLoader {
                ML_FORGE,
                ML_FABRIC;
        }

        public static KBTConfigIF options;

        static Map<ModdedEntityCategory, HashMap<EntityType<?>, Class<?>>> moddedEntityClasses = Map
                        .ofEntries(Map.entry(ModdedEntityCategory.VILLAGER_LIKE,
                                        new HashMap<EntityType<?>, Class<?>>()),
                                        Map.entry(ModdedEntityCategory.PLAYER_LIKE,
                                                        new HashMap<EntityType<?>, Class<?>>()));
        static Map<ModdedEntityCategory, ArrayList<EntityType<?>>> moddedEntities = null;

        public static final Map<String, ImmutableTriple<String, String, Map<String, ImmutableTriple<String, String, ?>>>> CONF_SPEC = Map
                        .of(
                                        "General", ImmutableTriple.of(
                                                        "General Settings",
                                                        "config.killer_rabbit_transformation.general",
                                                        Map.of(
                                                                        "TransformingChance", ImmutableTriple.of(
                                                                                        "killer Bunny spawning rate (x out of 10000), also affects potion transformation (x out of 10000 per tick)",
                                                                                        "config.killer_rabbit_transformation.spawning_rate",
                                                                                        4),
                                                                        "CanWeaknessTransform", ImmutableTriple.of(
                                                                                        "Can regular rabbits transformed by Weakness effect",
                                                                                        "config.killer_rabbit_transformation.can_weakness_transform",
                                                                                        true))),
                                        "BunnyTargeting", ImmutableTriple.of(
                                                        "Bunny Targeting",
                                                        "config.killer_rabbit_transformation.bunny_targeting",
                                                        Map.of(
                                                                        "BunnyAttacksVillagerLikes", ImmutableTriple.of(
                                                                                        "Killer Bunnies attack Villagers/Illagers/Witches",
                                                                                        "config.killer_rabbit_transformation.bunny_attacks_villager_likes",
                                                                                        true),
                                                                        "BunnyAttacksPlayerLikes", ImmutableTriple.of(
                                                                                        "Killer Bunnies attack Zombies/Skeletons/Wither Skeletons; Players are always being attacked though",
                                                                                        "config.killer_rabbit_transformation.bunny_attacks_player_likes",
                                                                                        true),
                                                                        "BunnyAttacksCreepers", ImmutableTriple.of(
                                                                                        "Killer Bunnies attack Creepers",
                                                                                        "config.killer_rabbit_transformation.bunny_attacks_creepers",
                                                                                        true),
                                                                        "BunnyAttacksPiglins", ImmutableTriple.of(
                                                                                        "Killer Bunnies attack Piglins/Piglin Brutes",
                                                                                        "config.killer_rabbit_transformation.bunny_attacks_piglins",
                                                                                        true))),
                                        "BunnyHeadRipping", ImmutableTriple.of(
                                                        "Bunny Head Ripping",
                                                        "config.killer_rabbit_transformation.bunny_head_ripping",
                                                        Map.of(
                                                                        "ZombieHeadDropChance", ImmutableTriple.of(
                                                                                        "Zombies slain by Killer Bunnies drop their heads (x% chance)",
                                                                                        "config.killer_rabbit_transformation.zombie_head_drop_chance",
                                                                                        50),
                                                                        "SkeletonHeadDropChance", ImmutableTriple.of(
                                                                                        "Skeletons slain by Killer Bunnies drop their skulls (x% chance)",
                                                                                        "config.killer_rabbit_transformation.skeleton_head_drop_chance",
                                                                                        50),
                                                                        "WitherSkeletonHeadDropChance",
                                                                        ImmutableTriple.of(
                                                                                        "Wither Skeletons slain by Killer Bunnies drop their skulls (x% chance)",
                                                                                        "config.killer_rabbit_transformation.wither_skeleton_head_drop_chance",
                                                                                        50),
                                                                        "CreeperHeadDropChance", ImmutableTriple.of(
                                                                                        "Creepers slain by Killer Bunnies drop their heads (x% chance)",
                                                                                        "config.killer_rabbit_transformation.creeper_head_drop_chance",
                                                                                        50),
                                                                        "PiglinHeadDropChance", ImmutableTriple.of(
                                                                                        "Piglins slain by Killer Bunnies drop their heads (x% chance)",
                                                                                        "config.killer_rabbit_transformation.piglin_head_drop_chance",
                                                                                        50),
                                                                        "DragonHeadDropChance", ImmutableTriple.of(
                                                                                        "The Ender Dragon drop its head if killed by a rabbit (x% chance). Impossible unless tame the rabbit using other mods",
                                                                                        "config.killer_rabbit_transformation.dragon_head_drop_chance",
                                                                                        100),
                                                                        "PlayerHeadDropChance", ImmutableTriple.of(
                                                                                        "Players slain by Killer Bunnies drop their heads (x% chance)",
                                                                                        "config.killer_rabbit_transformation.player_head_drop_chance",
                                                                                        100))));

        public static void init(ModLoader l) {
                // Write common init code here.
                EntityEvent.ADD.register(KBTEvents::OnEntityAdd);
                EntityEvent.LIVING_DEATH.register(KBTEvents::OnEntityDeath);
                EntityEvent.LIVING_HURT.register(KBTEvents::OnEntityHurt);
        }

        public interface KBTConfigIF {

                /**
                 * Killer Bunny spawning rate (x out of 10000), also affects potion
                 * transformation (x out of 10000 per tick)
                 * 
                 * @return int x out of 10000
                 */
                int TransformingChance();

                /**
                 * Can regular rabbits transformed by Weakness effect
                 * 
                 * @return boolean value
                 */
                boolean CanWeaknessTransform();

                /**
                 * Killer Bunnies attack Villagers/Illagers/Witches
                 * 
                 * @return boolean value
                 */
                boolean BunnyAttacksVillagerLikes();

                /**
                 * Killer Bunnies attack Zombies/Skeletons/Wither Skeletons; Players are always
                 * being attacked though
                 * 
                 * @return boolean value
                 */
                boolean BunnyAttacksPlayerLikes();

                /**
                 * Killer Bunnies attack Creepers
                 * 
                 * @return boolean value
                 */
                boolean BunnyAttacksCreepers();

                /**
                 * Killer Bunnies attack Piglins/Piglin Brutes
                 * 
                 * @return boolean value
                 */
                boolean BunnyAttacksPiglins();

                /**
                 * Zombies slain by Killer Bunnies drop their heads (x% chance)
                 * 
                 * @return int x out of 100
                 */
                int ZombieHeadDropChance();

                /**
                 * Skeletons slain by Killer Bunnies drop their skulls (x% chance)
                 * 
                 * @return int x out of 100
                 */
                int SkeletonHeadDropChance();

                /**
                 * Wither Skeletons slain by Killer Bunnies drop their skulls (x% chance)
                 * 
                 * @return int x out of 100
                 */
                int WitherSkeletonHeadDropChance();

                /**
                 * Creepers slain by Killer Bunnies drop their heads (x% chance)
                 * 
                 * @return int x out of 100
                 */
                int CreeperHeadDropChance();

                /**
                 * Piglins slain by Killer Bunnies drop their heads (x% chance)
                 * 
                 * @return int x out of 100
                 */
                int PiglinHeadDropChance();

                /**
                 * The Ender Dragon drop its head if killed by a rabbit (x% chance)
                 * Impossible unless tame the rabbit using other mods
                 * 
                 * @return int x out of 100
                 */
                int DragonHeadDropChance();

                /**
                 * Players slain by Killer Bunnies drop their heads (x% chance)
                 * 
                 * @return int x out of 100
                 */
                int PlayerHeadDropChance();
        }

        enum ModdedEntityCategory {
                VILLAGER_LIKE,
                PLAYER_LIKE;

                boolean valid() {
                        switch (this) {
                                case VILLAGER_LIKE:
                                        return options.BunnyAttacksVillagerLikes();
                                case PLAYER_LIKE:
                                        return options.BunnyAttacksPlayerLikes();
                                default:
                                        return false;
                        }
                }
        }
}
