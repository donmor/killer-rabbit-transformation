package com.github.donmor.killerbunnytweaks;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import dev.architectury.event.EventResult;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class KBTEvents {

    public static EventResult OnEntityAdd(Entity entity, Level world) {
        // Logically SSO
        if (!(world instanceof ServerLevel)) {
            return EventResult.pass();
        }

        if (entity instanceof Rabbit rabbit) {
            // Rabbit transforming under certain circumstances
            if (rabbit.getVariant() != Rabbit.Variant.EVIL
                    && Math.random() * 10000.0 < KillerBunnyTweaksMod.options.TransformingChance()) {
                rabbit.setVariant(Rabbit.Variant.EVIL);
            }
            // Modify existing killer bunnies (and transformed as well)
            if (rabbit.getVariant() == Rabbit.Variant.EVIL) {
                makeRabbitEviler(rabbit, world);
            }
        }

        return EventResult.pass();

    }

    static EventResult OnEntityDeath(LivingEntity entity, DamageSource source) {
        // Logically SSO
        Level world = entity.level();
        if (!(world instanceof ServerLevel))
            return EventResult.pass();

        // Holy Hand Grenade advancement
        if (entity instanceof Rabbit rabbit && rabbit.getVariant() == Rabbit.Variant.EVIL
                && (source.is(DamageTypes.EXPLOSION) || source.is(DamageTypes.PLAYER_EXPLOSION)
                        || source.is(DamageTypes.FIREWORKS))) {
            double x = entity.getX(), y = entity.getY(), z = entity.getZ();
            if (source.getEntity() instanceof ServerPlayer player)
                giveAdvancementToPlayer(player,
                        new ResourceLocation("killer_rabbit_transformation:as_what_happened_in"));
            else if (((Entity) world
                    .getEntitiesOfClass(Player.class, AABB.ofSize(new Vec3(x, y, z), 15, 15, 15), e -> true)
                    .stream().sorted(new Object() {
                        Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                            return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
                        }
                    }.compareDistOf(x, y, z)).findFirst().orElse(null)) instanceof ServerPlayer _player) {
                giveAdvancementToPlayer(_player,
                        new ResourceLocation("killer_rabbit_transformation:as_what_happened_in"));
            }
        }

        // Bunny head ripping
        if (source.getEntity() instanceof Rabbit rabbit && rabbit.getVariant() == Rabbit.Variant.EVIL) {
            if (entity.getClass() == Zombie.class
                    && Math.random() * 100 < KillerBunnyTweaksMod.options.ZombieHeadDropChance())
                entity.spawnAtLocation(Items.ZOMBIE_HEAD);
            else if (entity.getClass() == Skeleton.class
                    && Math.random() * 100 < KillerBunnyTweaksMod.options.SkeletonHeadDropChance())
                entity.spawnAtLocation(Items.SKELETON_SKULL);
            else if (entity.getClass() == WitherSkeleton.class
                    && Math.random() * 100 < KillerBunnyTweaksMod.options.WitherSkeletonHeadDropChance())
                entity.spawnAtLocation(Items.WITHER_SKELETON_SKULL);
            else if (entity.getClass() == Creeper.class
                    && Math.random() * 100 < KillerBunnyTweaksMod.options.CreeperHeadDropChance())
                entity.spawnAtLocation(Items.CREEPER_HEAD);
            else if (entity.getClass() == Piglin.class
                    && Math.random() * 100 < KillerBunnyTweaksMod.options.PiglinHeadDropChance())
                entity.spawnAtLocation(Items.PIGLIN_HEAD);
            else if (entity.getClass() == EnderDragon.class
                    && Math.random() * 100 < KillerBunnyTweaksMod.options.DragonHeadDropChance())
                entity.spawnAtLocation(Items.DRAGON_HEAD);
            else if (entity instanceof ServerPlayer player
                    && Math.random() * 100 < KillerBunnyTweaksMod.options.PlayerHeadDropChance()) {
                ItemStack headStack;
                if ((headStack = getPlayerHeadStack(player)) != null)
                    entity.spawnAtLocation(headStack);
            }
        }

        return EventResult.pass();
    }

    static EventResult OnEntityHurt(LivingEntity entity, DamageSource source, float dmg) {
        // Bite by a bunny, survived or not, then get an advancement
        if (source.getEntity() instanceof Rabbit rabbit && rabbit.getVariant() == Rabbit.Variant.EVIL
                && entity instanceof ServerPlayer player) {
            giveAdvancementToPlayer(player,
                    new ResourceLocation("killer_rabbit_transformation:we_need_holy_hand_grenade"));
        }
        return EventResult.pass();
    }

    public static void OnEntityTick(Entity entity) {
        if (!KillerBunnyTweaksMod.options.CanWeaknessTransform())
            return;
        Level world = entity.level();
        // Try to mske a weakened rabbit evil, 20 times per sec
        if (world instanceof ServerLevel && entity instanceof Rabbit rabbit
                && rabbit.getVariant() != Rabbit.Variant.EVIL
                && rabbit.hasEffect(MobEffects.WEAKNESS)
                && Math.random() * 10000.0 < KillerBunnyTweaksMod.options.TransformingChance()) {
            rabbit.setVariant(Rabbit.Variant.EVIL);
            makeRabbitEviler(rabbit, world);
        }
    }

    /**
     * Give an advancement to a player
     * 
     * @param player The target
     * @param v      The advancement
     */
    private static void giveAdvancementToPlayer(ServerPlayer player, ResourceLocation v) {
        Advancement _adv = player.server.getAdvancements()
                .getAdvancement(v);
        AdvancementProgress _ap = player.getAdvancements().getOrStartProgress(_adv);
        if (!_ap.isDone()) {
            Iterator<String> _iterator = _ap.getRemainingCriteria().iterator();
            while (_iterator.hasNext())
                player.getAdvancements().award(_adv, (String) _iterator.next());
        }
    }

    /**
     * Generate an itemstack of a player's head
     * 
     * @param player The victim
     * @return The itemstack
     */
    private static ItemStack getPlayerHeadStack(ServerPlayer player) {
        GameProfile gameProfile = player.getGameProfile();
        if (gameProfile == null)
            return null;
        ItemStack headStack = new ItemStack(Items.PLAYER_HEAD, 1);
        CompoundTag headCompoundTag = new CompoundTag();
        headCompoundTag.putString("SkullOwner", gameProfile.getName());
        headStack.setTag(headCompoundTag);
        return headStack;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static void makeRabbitEviler(Rabbit rabbit, Level world) {
        // Vanilla entities to be attacked
        try {
            if (KillerBunnyTweaksMod.options.BunnyAttacksPlayerLikes()) { // Player-like
                rabbit.targetSelector.addGoal(2,
                        new NearestAttackableTargetGoal<>(rabbit, Zombie.class, false));
                rabbit.targetSelector.addGoal(2,
                        new NearestAttackableTargetGoal<>(rabbit, AbstractSkeleton.class, false));
            }
            if (KillerBunnyTweaksMod.options.BunnyAttacksVillagerLikes()) { // Villager-like
                rabbit.targetSelector.addGoal(2,
                        new NearestAttackableTargetGoal<>(rabbit, AbstractVillager.class, false));
                rabbit.targetSelector.addGoal(2,
                        new NearestAttackableTargetGoal<>(rabbit, AbstractIllager.class, false));
                rabbit.targetSelector.addGoal(2,
                        new NearestAttackableTargetGoal<>(rabbit, Witch.class, false));
            }
            if (KillerBunnyTweaksMod.options.BunnyAttacksCreepers()) // Creeper
                rabbit.targetSelector.addGoal(2,
                        new NearestAttackableTargetGoal<>(rabbit, Creeper.class, false, false));
            if (KillerBunnyTweaksMod.options.BunnyAttacksPiglins()) // Piglin
                rabbit.targetSelector.addGoal(2,
                        new NearestAttackableTargetGoal<>(rabbit, AbstractPiglin.class, false, false));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Entities from other mods
        if (KillerBunnyTweaksMod.moddedEntities == null)
            return;
        try {
            // Iterate each category in moddedEntities
            for (KillerBunnyTweaksMod.ModdedEntityCategory category : KillerBunnyTweaksMod.moddedEntities.keySet())
                if (category.valid())
                    for (EntityType<?> entityType : KillerBunnyTweaksMod.moddedEntities.get(category)) {
                        if (!KillerBunnyTweaksMod.moddedEntityClasses.get(category).containsKey(entityType)) {
                            // Class not yet cached, fetching (by creating one and remove)
                            Entity e = entityType.create(world);
                            if (e instanceof Mob m)
                                KillerBunnyTweaksMod.moddedEntityClasses.get(category).put(entityType, m.getClass());
                            e.discard();
                            ;
                        }
                        rabbit.targetSelector.addGoal(2, new NearestAttackableTargetGoal(rabbit,
                                KillerBunnyTweaksMod.moddedEntityClasses.get(category).get(entityType), false));
                    }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void onResourceManagerReload(ResourceManager resourceManager) {
        // Re-initialize map
        KillerBunnyTweaksMod.moddedEntities = Map.ofEntries(
                Map.entry(KillerBunnyTweaksMod.ModdedEntityCategory.VILLAGER_LIKE,
                        new ArrayList<EntityType<?>>()),
                Map.entry(KillerBunnyTweaksMod.ModdedEntityCategory.PLAYER_LIKE,
                        new ArrayList<EntityType<?>>()),
                Map.entry(KillerBunnyTweaksMod.ModdedEntityCategory.CREEPER_LIKE,
                        new ArrayList<EntityType<?>>()));
        // Get EntityTypes from data
        for (KillerBunnyTweaksMod.ModdedEntityCategory category : KillerBunnyTweaksMod.moddedEntities
                .keySet())
            try (BufferedReader reader = resourceManager
                    .getResource(new ResourceLocation(KillerBunnyTweaksMod.MOD_ID,
                            "tags/entity_types/" + category.name().toLowerCase() + ".json"))
                    .get().openAsReader()) {
                JsonParser.parseReader(reader).getAsJsonObject().getAsJsonArray("values").forEach(t -> {
                    try {
                        Optional<EntityType<?>> eOptional;
                        if ((eOptional = EntityType.byString(t.getAsString())).isPresent())
                            KillerBunnyTweaksMod.moddedEntities.get(category)
                                    .add(eOptional.get());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
