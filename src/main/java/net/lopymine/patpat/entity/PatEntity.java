package net.lopymine.patpat.entity;

import net.minecraft.entity.*;

import java.util.*;

public class PatEntity {
    private final LivingEntity patEntity;
    private final PatAnimation animation;

    public PatEntity(LivingEntity entity, boolean bl) {
        this.patEntity = entity;
        this.animation = PatAnimation.of(entity, bl);
    }

    public PatAnimation getAnimation() {
        return this.animation;
    }

    public boolean is(LivingEntity entity) {
        return this.is(entity.getUuid());
    }

    private boolean is(UUID uuid) {
        return this.patEntity.getUuid().equals(uuid);
    }

    public LivingEntity getEntity() {
        return this.patEntity;
    }
}
