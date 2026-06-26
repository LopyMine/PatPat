package net.lopymine.patpat.mixin;

//? if fabric {

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.terraformersmc.modmenu.util.mod.fabric.FabricMod;
import java.util.*;
import net.fabricmc.loader.api.*;
import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.modmenu.translation.ModMenuTranslators;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = FabricMod.class, remap = false)
public class ModMenuMixin {

	@Unique
	private static final ModContainer MOD_CONTAINER = FabricLoader
			.getInstance()
			.getModContainer(PatPat.MOD_ID)
			.orElse(null);

	@Shadow(remap = false)
	@Final
	protected ModContainer container;

	@ModifyReturnValue(at = @At("RETURN"), method = "getContributors()Ljava/util/Map;", remap = false, require = 0)
	private Map<String, Collection<String>> getContributorsInject(@NotNull Map<String, Collection<String>> original) {
		if (MOD_CONTAINER.equals(container)) {
			original.putAll(ModMenuTranslators.getInstance().getTranslators());
		}
		return original;
	}
}
//?}

