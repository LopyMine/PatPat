package net.lopymine.patpat.modmenu.screen.yacl;

import dev.isxander.yacl3.api.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.config.client.PatPatClientConfig;
import net.lopymine.patpat.modmenu.screen.yacl.simple.SimpleGroupOptionBuilder;

import java.util.function.Function;

public class YACLConfigurationScreen {

	private static final Function<Boolean, Text> ENABLED_OR_DISABLE_FORMATTER = state -> Text.translatable("patpat.modmenu.formatter.enable_or_disable." + state);

	private YACLConfigurationScreen() {
		throw new IllegalStateException("Screen class");
	}

	public static Screen createScreen(Screen parent) {
		PatPatClientConfig defConfig = PatPatClientConfig.DEFAULT;
		PatPatClientConfig config = PatPatClient.getConfig();

		return YetAnotherConfigLib.createBuilder()
				.title(Text.translatable("patpat.modmenu.title"))
				.category(ConfigCategory.createBuilder()
						.name(Text.translatable("patpat.modmenu.title"))
						.group(getMainGroup(defConfig, config))
						.group(getSoundGroup(defConfig, config))
						.group(getVisualGroup(defConfig, config))
						.group(getServerGroup(defConfig, config))
						.build())
				.save(config::save)
				.build()
				.generateScreen(parent);
	}

	private static OptionGroup getMainGroup(PatPatClientConfig defConfig, PatPatClientConfig config) {
		return SimpleGroupOptionBuilder.createBuilder("main").options(collector -> collector.collect(
				collector.addBooleanOption("enable_mod", defConfig.isModEnabled(), config::isModEnabled, config::setModEnabled, ENABLED_OR_DISABLE_FORMATTER::apply)
		)).build();
	}

	private static OptionGroup getSoundGroup(PatPatClientConfig defConfig, PatPatClientConfig config) {
		return SimpleGroupOptionBuilder.createBuilder("sound").options(collector -> collector.collect(
				collector.addBooleanOption("enable_sounds", defConfig.isSoundsEnabled(), config::isSoundsEnabled, config::setSoundsEnabled, ENABLED_OR_DISABLE_FORMATTER::apply),
				collector.addDoubleOptionAsSlider("sounds_volume", 0D, 1D, 0.05D, defConfig.getSoundsVolume(), config::getSoundsVolume, config::setSoundsVolume)
		)).build();
	}

	private static OptionGroup getVisualGroup(PatPatClientConfig defConfig, PatPatClientConfig config) {
		return SimpleGroupOptionBuilder.createBuilder("visual").options(collector -> collector.collect(
				collector.addBooleanOption("lowering_animation_enabled", defConfig.isLoweringAnimationEnabled(), config::isLoweringAnimationEnabled, config::setLoweringAnimationEnabled, ENABLED_OR_DISABLE_FORMATTER::apply),
				collector.addBooleanOption("hiding_nickname_enabled", defConfig.isNicknameHidingEnabled(), config::isNicknameHidingEnabled, config::setNicknameHidingEnabled, ENABLED_OR_DISABLE_FORMATTER::apply),
				collector.addBooleanOption("swing_hand_enabled", defConfig.isSwingHandEnabled(), config::isSwingHandEnabled, config::setSwingHandEnabled, ENABLED_OR_DISABLE_FORMATTER::apply),
				collector.addDoubleOptionAsSlider("hand_offset_x", -5D, 5D, 0.01D, defConfig.getAnimationOffsets().getOffsetX(), defConfig.getAnimationOffsets()::getOffsetX, defConfig.getAnimationOffsets()::setOffsetX),
				collector.addDoubleOptionAsSlider("hand_offset_y", -5D, 5D, 0.01D, defConfig.getAnimationOffsets().getOffsetY(), defConfig.getAnimationOffsets()::getOffsetY, defConfig.getAnimationOffsets()::setOffsetY),
				collector.addDoubleOptionAsSlider("hand_offset_z", -5D, 5D, 0.01D, defConfig.getAnimationOffsets().getOffsetZ(), defConfig.getAnimationOffsets()::getOffsetZ, defConfig.getAnimationOffsets()::setOffsetZ)
		)).build();
	}

	private static OptionGroup getServerGroup(PatPatClientConfig defConfig, PatPatClientConfig config) {
		return SimpleGroupOptionBuilder.createBuilder("server").options(collector -> collector.collect(
				collector.addBooleanOption("pat_me_enabled", defConfig.isPatMeEnabled(), config::isPatMeEnabled, config::setPatMeEnabled, ENABLED_OR_DISABLE_FORMATTER::apply),
				collector.addBooleanOption("bypass_server_resource_pack_priority_enabled", defConfig.isBypassServerResourcePackPriorityEnabled(), config::isBypassServerResourcePackPriorityEnabled, config::setBypassServerResourcePackPriorityEnabled, ENABLED_OR_DISABLE_FORMATTER::apply)
		)).build();
	}
}


