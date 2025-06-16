package net.lopymine.patpat.mixin;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.*;

import net.lopymine.patpat.utils.mixin.MarkedEntity;

@Mixin(Entity.class)
public class EntityMixin implements MarkedEntity {

	@Unique
	private boolean marked;

	@Override
	public void patPat$mark(boolean marked) {
		this.marked = marked;
	}

	@Override
	public boolean patPat$isMarked() {
		return this.marked;
	}
}
