package net.lopymine.patpat.mixin;

import net.lopymine.patpat.utils.mixin.MarkedEntity;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.*;

@Mixin(Entity.class)
public class EntityMixin implements MarkedEntity {

	@Unique
	private boolean patPat$marked;

	@Override
	public void patPat$mark(boolean marked) {
		this.patPat$marked = marked;
	}

	@Override
	public boolean patPat$isMarked() {
		return this.patPat$marked;
	}
}
