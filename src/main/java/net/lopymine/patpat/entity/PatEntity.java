package net.lopymine.patpat.entity;

import lombok.*;
import net.lopymine.patpat.client.config.resourcepack.*;
import net.minecraft.world.entity.LivingEntity;
import java.util.*;

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
		if (this.tickProgress == Integer.MAX_VALUE) {
			this.tickProgress = 0;
		}
	}

	public float getProgress(float tickDelta) {
		//? >1.20.2 {
		net.minecraft.client.multiplayer.ClientLevel world = net.minecraft.client.Minecraft.getInstance().level;
		if (world == null) {
			return 0;
		}

		net.minecraft.world.TickRateManager tickManager = world.tickRateManager();
		float tickMillis = tickManager.tickrate() > 20 ? (2500F / tickManager.millisecondsPerTick()) : 50F;
		float v = Math.max(this.tickProgress, 0) * tickMillis;
		float v1 = tickManager.isFrozen() ? 0 : tickDelta * tickMillis;
		//?} else {
		/*float v = Math.max(this.tickProgress, 0) * 50F;
		float v1 = tickDelta * 50F;
		*///?}
		return v + v1;
	}

	public boolean is(LivingEntity entity) {
		return this.is(entity.getUUID());
	}

	private boolean is(UUID uuid) {
		return this.entity.getUUID().equals(uuid);
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
		return Objects.hash(this.entity.getUUID());
	}

	@Override
	public String toString() {
		return "PatEntity{" +
				"entity=" + this.entity.toString() +
				", animation=" + this.animation.toString() +
				", currentFrame=" + this.currentFrame +
				", tickProgress=" + this.tickProgress +
				'}';
	}
}
