package net.lopymine.patpat.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.terraformersmc.modmenu.util.mod.fabric.FabricMod;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.modmenu.ModMenuTranslators;

import org.jetbrains.annotations.NotNull;

/*? >=1.20.4 {*/
import java.util.Collection;
import java.util.Map;
/*?} else {*/
/*import java.util.List;
*//*?}*/

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

	/*? >=1.20.4 {*/
	@ModifyReturnValue(at = @At("RETURN"), method = "getContributors()Ljava/util/Map;", remap = false, require = 0)
	private Map<String, Collection<String>> getContributorsInject(@NotNull Map<String, Collection<String>> original) {
		if (MOD_CONTAINER.equals(container)) {
			original.putAll(ModMenuTranslators.getInstance().getTranslators());
		}
	/*?} else {*/
	/*@ModifyReturnValue(at = @At("RETURN"), method = "getContributors()Ljava/util/List;", remap = false, require = 0)
	private List<String> getContributorsInject(@NotNull List<String> original) {
		if (MOD_CONTAINER.equals(container)) {
			original.addAll(ModMenuTranslators.getInstance().getTranslators());
		}
	*//*?}*/
		return original;
	}
}

