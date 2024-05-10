package net.lopymine.patpat.entity;

import net.minecraft.entity.Entity;

import java.util.*;

public class PatEntity {
    private final Entity patEntity;
    private final PatAnimation animation;

    public PatEntity(Entity entity) {
        this.patEntity = entity;
        this.animation = PatAnimation.of(entity);
    }

    public PatAnimation getAnimation() {
        return this.animation;
    }

    public boolean is(Entity entity) {
        return this.is(entity.getUuid());
    }

    private boolean is(UUID uuid) {
        return this.patEntity.getUuid().equals(uuid);
    }

    public Entity getEntity() {
        return this.patEntity;
    }
}
