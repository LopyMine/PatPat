package net.lopymine.patpat.mixin;

//? >=1.21.2 {
import net.lopymine.patpat.utils.mixin.EntityRenderStateWithParent;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityRenderState.class)
public class EntityRenderStateMixin implements EntityRenderStateWithParent {

	@Unique
	private Entity entity;

	@Unique
	private float tickDelta;

	@Override
	public void patPat$setEntity(Entity entity) {
		this.entity = entity;
	}

	@Override
	public Entity patPat$getEntity() {
		return this.entity;
	}

	@Override
	public void patPat$setTickDelta(float tickDelta) {
		this.tickDelta = tickDelta;
	}

	@Override
	public float patPat$getTickDelta() {
		return this.tickDelta;
	}
}
//?}