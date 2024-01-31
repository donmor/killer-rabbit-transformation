package net.mcreator.killerrabbittransformation.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class OnEntitySpawnProcedure {
	@SubscribeEvent
	public static void onEntityJoin(EntityJoinWorldEvent event) {
		execute(event, event.getEntity());
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof Rabbit) {
			if (Math.random() >= 0.9998) {
				((Rabbit) entity).setRabbitType(Rabbit.TYPE_EVIL);
			}
		}
		if (entity instanceof Rabbit && ((Rabbit) entity).getRabbitType() == 99) {
			if (ModList.get().isLoaded("guardvillagers")) {
				try {
					((Mob) entity).targetSelector.addGoal(2, new NearestAttackableTargetGoal((Mob) entity, Class.forName("tallestegg.guardvillagers.GuardVillagers"), false, false));
				} catch (Exception e) {
				}
			}
			((Mob) entity).targetSelector.addGoal(2, new NearestAttackableTargetGoal((Mob) entity, AbstractVillager.class, false, false));
			((Mob) entity).targetSelector.addGoal(2, new NearestAttackableTargetGoal((Mob) entity, AbstractIllager.class, false, false));
			((Mob) entity).targetSelector.addGoal(2, new NearestAttackableTargetGoal((Mob) entity, Witch.class, false, false));
			((Mob) entity).targetSelector.addGoal(2, new NearestAttackableTargetGoal((Mob) entity, Zombie.class, false, false));
			((Mob) entity).targetSelector.addGoal(2, new NearestAttackableTargetGoal((Mob) entity, Creeper.class, false, false));
			((Mob) entity).targetSelector.addGoal(2, new NearestAttackableTargetGoal((Mob) entity, Skeleton.class, false, false));
			((Mob) entity).targetSelector.addGoal(2, new NearestAttackableTargetGoal((Mob) entity, WitherSkeleton.class, false, false));
		}
	}
}
