package net.lopymine.patpat.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ProfilerUtils {

	public static void push(String id) {
		/*? >=1.21.2 {*/
		net.minecraft.util.profiling.Profiler.get().push(id);
		/*?} else {*/ /*net.minecraft.client.Minecraft.getInstance().getProfiler().push(id); *//*?}*/
	}

	public static void pop() {
		/*? >=1.21.2 {*/
		net.minecraft.util.profiling.Profiler.get().pop();
		/*?} else {*/ /*net.minecraft.client.Minecraft.getInstance().getProfiler().pop(); *//*?}*/
	}

}
