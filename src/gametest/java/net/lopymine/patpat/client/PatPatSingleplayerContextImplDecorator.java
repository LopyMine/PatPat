package net.lopymine.patpat.client;

import lombok.SneakyThrows;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.screens.TitleScreen;

import net.fabricmc.fabric.api.client.gametest.v1.context.*;
import net.fabricmc.fabric.api.client.gametest.v1.world.TestWorldSave;
import net.fabricmc.fabric.impl.client.gametest.threading.ThreadingImpl;

import java.io.IOException;
import java.lang.reflect.Field;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings("UnstableApiUsage")
public class PatPatSingleplayerContextImplDecorator implements TestSingleplayerContext {


	private final TestSingleplayerContext testSingleplayerContext;
	private final ClientGameTestContext context;
	private final TestServerContext server;


	public PatPatSingleplayerContextImplDecorator(TestSingleplayerContext testSingleplayerContext){
		try {
			this.testSingleplayerContext = testSingleplayerContext;
			Class<? extends TestSingleplayerContext> testSingleplayerContextClass = testSingleplayerContext.getClass();
			Field contextField = testSingleplayerContextClass.getDeclaredField("context");
			contextField.setAccessible(true);
			this.context = (ClientGameTestContext) contextField.get(testSingleplayerContext);
			this.server = testSingleplayerContext.getServer();
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public @NotNull TestWorldSave getWorldSave() {
		return this.testSingleplayerContext.getWorldSave();
	}

	//? if >=26.1 {
	@Override
	public @NotNull TestClientLevelContext getClientLevel() {
		return this.testSingleplayerContext.getClientLevel();
	}
	//?} else {
	/*@Override
		public @NotNull TestClientWorldContext getClientWorld() {
			return this.testSingleplayerContext.getClientWorld();
		}
	*///?}

	@Override
	public @NotNull TestServerContext getServer() {
		return this.testSingleplayerContext.getServer();
	}

	@Override
	public void close() {
		ThreadingImpl.checkOnGametestThread("close");
		server.runOnServer(minecraftServer -> minecraftServer.halt(false));
		context.waitFor(client -> !ThreadingImpl.isServerRunning && client.level == null, SharedConstants.TICKS_PER_MINUTE);
		context.setScreen(TitleScreen::new);
	}
}
