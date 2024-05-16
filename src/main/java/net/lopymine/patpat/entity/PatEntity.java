package net.lopymine.patpat.entity;

import lombok.Getter;
import lombok.Setter;
import net.lopymine.patpat.config.AnimationConfig;
import net.minecraft.entity.LivingEntity;

import java.util.Objects;
import java.util.UUID;

@Getter
public class PatEntity {
	private final LivingEntity entity;
	private final AnimationConfig animation;
	@Setter
	private int frame;
	private long timeOfStart;

	public PatEntity(LivingEntity entity, boolean isAuthor) {
		this.entity = entity;
		this.animation = PatAnimation.of(entity, isAuthor);
		this.resetAnimation();
	}

	public void resetAnimation() {
		this.timeOfStart = System.currentTimeMillis();
		this.frame = 0;
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
