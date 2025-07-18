package net.lopymine.patpat.utils;

import lombok.experimental.UtilityClass;
import net.minecraft.resources.ResourceLocation;

@UtilityClass
public class ResourceLocationUtils {

	public static ResourceLocation parse(String string){
		/*? if >1.20.6 {*/
		return ResourceLocation.parse(string);
		/*?} else {*/
		/*return new ResourceLocation(string);
		*//*?}*/
	}

}
