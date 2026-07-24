package net.lopymine.patpat.compat.iris;

import java.lang.reflect.Method;
import java.nio.file.*;
import java.util.stream.Stream;
import net.lopymine.patpat.compat.LoadedMods;

public class IrisShaderController {

	private static final String[] IRIS_CLASSES = {"net.irisshaders.iris.Iris", "net.coderbot.iris.Iris"};

	private IrisShaderController() {
		throw new IllegalStateException("Controller class");
	}

	public static String enableFirstShaderPack() {
		if (!LoadedMods.IRIS_LOADED) {
			return "none";
		}
		Class<?> irisClass = findIrisClass();
		if (irisClass == null) {
			return "unsupported";
		}
		try {
			Path directory = (Path) irisClass.getMethod("getShaderpacksDirectory").invoke(null);
			Method isValid = irisClass.getMethod("isValidShaderpack", Path.class);

			String pack;
			try (Stream<Path> stream = Files.list(directory)) {
				pack = stream.filter(path -> isValidShaderpack(isValid, path)).map(path -> path.getFileName().toString()).findFirst().orElse(null);
			}
			if (pack == null) {
				return "no-packs";
			}

			boolean bl = testGetAPI();

			Method getConfig = irisClass.getMethod("getIrisConfig");
			Object config = getConfig.invoke(null);
			Class<?> configClass = getConfig.getReturnType();
			configClass.getMethod("setShaderPackName", String.class).invoke(config, pack);
			if (!bl) {
				configClass.getMethod("setShadersEnabled", boolean.class).invoke(config, true);
			}
			configClass.getMethod("save").invoke(config);
			if (!bl) {
				irisClass.getMethod("reload").invoke(null);
			}

			if (bl) {
				setShadersEnabled();
			}
			return pack;
		} catch (Exception e) {
			throw new IllegalStateException("Failed to enable a shader pack", e);
		}
	}

	private static Class<?> findIrisClass() {
		for (String name : IRIS_CLASSES) {
			try {
				return Class.forName(name);
			} catch (ClassNotFoundException ignored) {
				// try the next known package
			}
		}
		return null;
	}

	private static boolean isValidShaderpack(Method isValid, Path path) {
		try {
			return (boolean) isValid.invoke(null, path);
		} catch (Exception e) {
			return false;
		}
	}

	private static void setShadersEnabled() throws Exception {
		Class<?> apiClass = Class.forName("net.irisshaders.iris.api.v0.IrisApi");
		Object api = apiClass.getMethod("getInstance").invoke(null);
		Object config = apiClass.getMethod("getConfig").invoke(api);
		Class.forName("net.irisshaders.iris.api.v0.IrisApiConfig").getMethod("setShadersEnabledAndApply", boolean.class).invoke(config, true);
	}

	private static boolean testGetAPI() {
		try {
			Class.forName("net.irisshaders.iris.api.v0.IrisApi");
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
