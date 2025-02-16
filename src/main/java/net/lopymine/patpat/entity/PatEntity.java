package net.lopymine.patpat.entity;

import lombok.*;
import net.minecraft.entity.LivingEntity;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.config.resourcepack.*;

import java.util.*;

//? >1.20.2 {
import net.minecraft.world.tick.TickManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
//?}

@Getter
public class PatEntity {

	private final LivingEntity entity;
	private final CustomAnimationSettingsConfig animation;
	@Setter
	private int currentFrame;

	private int tickProgress;

	public PatEntity(LivingEntity entity, PlayerConfig whoPatted) {
		this.entity       = entity;
		this.animation    = CustomAnimationSettingsConfig.of(entity, whoPatted);
		this.currentFrame = 0;
		this.tickProgress = -1;
	}

	public void resetAnimation() {
		this.currentFrame = 0;
		this.tickProgress = -1;
	}

	public void tick() {
		this.tickProgress += 1;
		if (tickProgress == Integer.MAX_VALUE) {
			tickProgress = 0;
		}
	}

	public float getProgress(float tickDelta) {
		//? >1.20.2 {
		ClientWorld world = MinecraftClient.getInstance().world;
		if (world == null) {
			return 0;
		}

		TickManager tickManager = world.getTickManager();
		float tickMillis = tickManager.getTickRate() > 20 ? (2500F / tickManager.getMillisPerTick()) : 50F;
		float v = Math.max(this.tickProgress, 0) * tickMillis;
		float v1 = tickManager.isFrozen() ? 0 : tickDelta * tickMillis;
		//?} else {
		/*float v = Math.max(this.tickProgress, 0) * 50F;
		float v1 = tickDelta * 50F;
		*///?}
		return v + v1;
	}

	public boolean is(LivingEntity entity) {
		return this.is(entity.getUuid());
	}

	private boolean is(UUID uuid) {
		return this.entity.getUuid().equals(uuid);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PatEntity patEntity = (PatEntity) o;
		return this.is(patEntity.getEntity());
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.entity.getUuid());
	}
}
