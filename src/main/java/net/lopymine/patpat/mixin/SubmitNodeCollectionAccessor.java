package net.lopymine.patpat.mixin;

import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.feature.phase.TranslucentFeatureRenderPhase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SubmitNodeCollection.class)
public interface SubmitNodeCollectionAccessor {

	@Accessor("translucentModels")
	TranslucentFeatureRenderPhase getTranslucentModels();

}
