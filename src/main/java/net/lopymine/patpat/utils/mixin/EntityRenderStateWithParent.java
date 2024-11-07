package net.lopymine.patpat.utils.mixin;

import net.minecraft.entity.Entity;

@SuppressWarnings("java:S100")
public interface EntityRenderStateWithParent {

    void patPat$setEntity(Entity entity);

    Entity patPat$getEntity();

    void patPat$setTickDelta(float tickDelta);

    float patPat$getTickDelta();


}
