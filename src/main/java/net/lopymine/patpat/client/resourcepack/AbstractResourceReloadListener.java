package net.lopymine.patpat.client.resourcepack;

import java.util.concurrent.*;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.*;
import net.minecraft.util.Unit;
import net.minecraft.util.profiling.*;

//? if fabric && <=1.21.8 {
/*import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
 *///?}

public abstract class AbstractResourceReloadListener implements /*? if >=1.21.9 || forge || neoforge {*/ PreparableReloadListener /*?} else {*/ /*IdentifiableResourceReloadListener *//*?}*/ {

	//? if fabric && <=1.21.8 {
	/*@Override
	public Identifier getFabricId() {
		return this.getId();
	}
	*///?}

	public Identifier getId() {
		String modId = this.getModId();
		String path = "%s-reload-listener".formatted(modId);
		//? if >=1.21 {
		return Identifier.fromNamespaceAndPath(modId, path);
		//?} else {
		/*return Identifier.tryBuild(modId, path);
		 *///?}
	}

	public abstract String getModId();

	//? if >=1.21.9 {
	@Override
	public CompletableFuture<Void> reload(SharedState store, Executor prepareExecutor, PreparationBarrier synchronizer, Executor applyExecutor) {
		return synchronizer.wait(Unit.INSTANCE).thenRunAsync(() -> {
			ProfilerFiller profiler = Profiler.get();
			profiler.push("%s-reload-listener".formatted(this.getModId()));
			this.reloadStuff(synchronizer, store.resourceManager(), prepareExecutor, applyExecutor);
			profiler.pop();
		}, applyExecutor);
	}
	//?} else {
	/*@Override
	public CompletableFuture<Void> reload(PreparationBarrier synchronizer, ResourceManager manager, /^? if <=1.21.1 {^/ /^ProfilerFiller profiler, ProfilerFiller applyProfiler, ^//^?}^/ Executor prepareExecutor, Executor applyExecutor) {
		return synchronizer.wait(Unit.INSTANCE).thenRunAsync(() -> {
			//? if >=1.21.2 {
			ProfilerFiller profiler = Profiler.get();
			//?}
			profiler.push("listener");
			this.reloadStuff(synchronizer, manager, prepareExecutor, applyExecutor);
			profiler.pop();
		}, applyExecutor);
	}
	*///?}

	protected abstract void reloadStuff(PreparationBarrier synchronizer, ResourceManager manager, Executor prepareExecutor, Executor applyExecutor);
}
