package net.lopymine.patpat.modmenu.screen.yacl.simple;

//? >=1.20 {
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.OptionGroup.Builder;
import net.minecraft.text.Text;

import net.lopymine.patpat.utils.ModMenuUtils;

import java.util.function.Function;

public class SimpleGroupOptionBuilder {
	private final Builder groupBuilder;
	private final String groupId;

	public SimpleGroupOptionBuilder(String groupId) {
		String groupKey = ModMenuUtils.getGroupTitleKey(groupId);
		this.groupId = groupId;
		this.groupBuilder = OptionGroup.createBuilder()
				.name(Text.translatable(groupKey))
				.description(OptionDescription.of(Text.translatable(ModMenuUtils.getDescriptionKey(groupKey))));
	}

	public static SimpleGroupOptionBuilder createBuilder(String groupId) {
		return new SimpleGroupOptionBuilder(groupId);
	}

	public SimpleGroupOptionBuilder options(Function<SimpleOptionsCollector, Option<?>[]> function) {
		SimpleOptionsCollector collector = SimpleOptionsCollector.createBuilder(this.groupId);
		Option<?>[] options = function.apply(collector);
		for (Option<?> option : options) {
			if (option == null) {
				continue;
			}
			this.groupBuilder.option(option);
		}
		return this;
	}

	public OptionGroup build() {
		return this.groupBuilder.build();
	}
}
//?}


