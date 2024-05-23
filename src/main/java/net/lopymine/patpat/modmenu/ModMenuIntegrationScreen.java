package net.lopymine.patpat.modmenu;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.config.client.PatPatClientConfig;

public class ModMenuIntegrationScreen {
	public static Screen createScreen(Screen parent) {
		PatPatClientConfig config = PatPatClient.getConfig();

		return YetAnotherConfigLib.createBuilder()
				.title(Text.literal("PatPat Client configuration"))
				.category(ConfigCategory.createBuilder()
						.name(Text.literal("PatPat Client configuration"))
						.tooltip(Text.literal("This text will appear as a tooltip when you hover or focus the button with Tab. There is no need to add \n to wrap as YACL will do it for you."))
						.group(OptionGroup.createBuilder()
								.name(Text.literal("Name of the group"))
								.description(OptionDescription.of(Text.literal("This text will appear when you hover over the name or focus on the collapse button with Tab.")))
								.option(Option.<Boolean>createBuilder()
										.name(Text.literal("Boolean Option"))
										.description(OptionDescription.of(Text.literal("This text will appear as a tooltip when you hover over the option.")))
										.binding(true, () -> true, newVal -> System.out.println(newVal))
										.controller(TickBoxControllerBuilder::create)
										.build())
								.build())
						.build())
				.build()
				.generateScreen(parent);
	}
}
