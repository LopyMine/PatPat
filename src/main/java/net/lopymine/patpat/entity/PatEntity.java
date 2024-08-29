package net.lopymine.patpat.entity;

import lombok.*;
import net.minecraft.entity.LivingEntity;

import net.lopymine.patpat.config.resourcepack.*;

import java.util.*;

@Getter
public class PatEntity {

	private final LivingEntity entity;
	private final CustomAnimationSettingsConfig animation;
	@Setter
	private int currentFrame;
	private long timeOfStart;

	public PatEntity(LivingEntity entity, PlayerConfig whoPatted) {
		this.entity       = entity;
		this.animation    = CustomAnimationSettingsConfig.of(entity, whoPatted);
		this.timeOfStart  = System.currentTimeMillis();
		this.currentFrame = 0;
	}

	public void resetAnimation() {
		this.timeOfStart  = System.currentTimeMillis();
		this.currentFrame = 0;
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
