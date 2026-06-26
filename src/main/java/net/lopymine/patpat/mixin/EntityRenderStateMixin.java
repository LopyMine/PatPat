package net.lopymine.patpat.mixin;

import net.lopymine.patpat.utils.mixin.EntityRenderStateWithParent;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.*;

@Mixin(EntityRenderState.class)
public class EntityRenderStateMixin implements EntityRenderStateWithParent {

	@Unique
	private Entity patPat$entity;

	@Unique
	private float patPat$tickDelta;

	@Override
	public void patPat$setEntity(Entity entity) {
		this.patPat$entity = entity;
	}

	@Override
	public Entity patPat$getEntity() {
		return this.patPat$entity;
	}

	@Override
	public void patPat$setTickDelta(float tickDelta) {
		this.patPat$tickDelta = tickDelta;
	}

	@Override
	public float patPat$getTickDelta() {
		return this.patPat$tickDelta;
	}
}