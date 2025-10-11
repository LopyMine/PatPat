package net.lopymine.patpat.compat;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import net.fabricmc.loader.api.FabricLoader;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class PatPatMixinConfigPlugin implements IMixinConfigPlugin {

	private static final String MOD_MENU_MIXIN_CLASS = "net.lopymine.patpat.mixin.ModMenuMixin";

	@Override
	public boolean shouldApplyMixin(String mixinClass, String mixinSource) {
		return !mixinSource.equals(MOD_MENU_MIXIN_CLASS) || FabricLoader.getInstance().isModLoaded("modmenu");
	}

	@Override
	public void onLoad(String s) {
		// NO-OP
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public void acceptTargets(Set<String> set, Set<String> set1) {
		// NO-OP
	}

	@Override
	public List<String> getMixins() {
		return Collections.emptyList();
	}

	@Override
	public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {
		// NO-OP
	}

	@Override
	public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {
		// NO-OP
	}
}