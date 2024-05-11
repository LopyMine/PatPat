package net.lopymine.patpat.mixin;

import net.lopymine.patpat.manager.PatPatResourcePackManager;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourceReload;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(ReloadableResourceManagerImpl.class)
public class ReloadableResourceManagerImplMixin {
	@Inject(at = @At("RETURN"), method = "reload")
	private void reload(Executor prepareExecutor, Executor applyExecutor, CompletableFuture<Unit> initialStage, List<ResourcePack> packs, CallbackInfoReturnable<ResourceReload> cir) {
		if (packs.isEmpty()) {
			return;
		}
		try {
			PatPatResourcePackManager.INSTANCE.load(packs); // TODO Отладить ошибку
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
