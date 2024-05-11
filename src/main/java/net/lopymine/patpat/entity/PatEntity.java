package net.lopymine.patpat.entity;

import lombok.Getter;
import net.minecraft.entity.LivingEntity;

import java.util.Objects;
import java.util.UUID;

@Getter
public class PatEntity {
	private final LivingEntity entity;
	private final PatAnimation animation;

	public PatEntity(LivingEntity entity, boolean bl) {
		this.entity = entity;
		this.animation = PatAnimation.of(entity, bl);
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
		return Objects.hash(entity.getUuid());
	}
}
