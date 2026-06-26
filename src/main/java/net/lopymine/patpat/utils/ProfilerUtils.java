package net.lopymine.patpat.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ProfilerUtils {

	public static void push(String id) {
		net.minecraft.util.profiling.Profiler.get().push(id);
	}

	public static void pop() {
		net.minecraft.util.profiling.Profiler.get().pop();
	}

}
