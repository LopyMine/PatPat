package net.lopymine.patpat.client.resourcepack;

import java.util.concurrent.*;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.*;
import net.minecraft.util.Unit;
import net.minecraft.util.profiling.*;


public abstract class AbstractResourceReloadListener implements PreparableReloadListener {


	public Identifier getId() {
		String modId = this.getModId();
		String path = "%s-reload-listener".formatted(modId);
		return Identifier.fromNamespaceAndPath(modId, path);
	}

	public abstract String getModId();

	@Override
	public CompletableFuture<Void> reload(SharedState store, Executor prepareExecutor, PreparationBarrier synchronizer, Executor applyExecutor) {
		return synchronizer.wait(Unit.INSTANCE).thenRunAsync(() -> {
			ProfilerFiller profiler = Profiler.get();
			profiler.push("%s-reload-listener".formatted(this.getModId()));
			this.reloadStuff(synchronizer, store.resourceManager(), prepareExecutor, applyExecutor);
			profiler.pop();
		}, applyExecutor);
	}

	protected abstract void reloadStuff(PreparationBarrier synchronizer, ResourceManager manager, Executor prepareExecutor, Executor applyExecutor);
}
