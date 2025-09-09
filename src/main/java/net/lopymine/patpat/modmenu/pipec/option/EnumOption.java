package net.lopymine.patpat.modmenu.pipec.option;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import net.minecraft.network.chat.Component;

import java.util.function.Function;

@Getter
@SuperBuilder
public class EnumOption<T extends Enum<T>> extends PatOption<T> {

	private final Class<T> enumClass;
	private final Function<T, Component> enumNameProvider;

}
