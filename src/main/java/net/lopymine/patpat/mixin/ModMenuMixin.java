package net.lopymine.patpat.mixin;

import com.terraformersmc.modmenu.util.mod.fabric.FabricMod;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.modmenu.ModMenuTranslators;

import java.util.*;

@Mixin(value = FabricMod.class, remap = false)
public class ModMenuMixin {

	@Unique
	private static final ModContainer MOD_CONTAINER = FabricLoader.getInstance().getModContainer(PatPat.MOD_ID).orElse(null);

	@Shadow(remap = false)
	@Final
	protected ModContainer container;

	@Inject(at = @At("RETURN"), method = "getContributors")
	private void getContributorsInject(CallbackInfoReturnable<Map<String, Collection<String>>> cir) {
		if(!MOD_CONTAINER.equals(container)){
			return;
		}
		cir.getReturnValue().putAll(ModMenuTranslators.getInstance().getTranslators());
	}
}
