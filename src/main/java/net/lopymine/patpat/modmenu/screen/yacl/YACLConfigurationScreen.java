package net.lopymine.patpat.modmenu.screen.yacl;

import net.minecraft.client.gui.screen.Screen;
//? >=1.20 {
import dev.isxander.yacl3.api.*;
import net.minecraft.text.Text;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.config.client.PatPatClientConfig;
import net.lopymine.patpat.modmenu.screen.yacl.simple.*;

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
						.group(getResourcePacksGroup(defConfig, config))
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
				collector.getBooleanOption("enable_mod", defConfig.isModEnabled(), config::isModEnabled, config::setModEnabled, ENABLED_OR_DISABLE_FORMATTER::apply)
		)).build();
	}

	private static OptionGroup getResourcePacksGroup(PatPatClientConfig defConfig, PatPatClientConfig config) {
		return SimpleGroupOptionBuilder.createBuilder("resource_packs").options(collector -> collector.collect(
				collector.getBooleanOption("skip_outdated_animations_enabled", defConfig.isSkipOldAnimationsEnabled(), config::isSkipOldAnimationsEnabled, config::setSkipOldAnimationsEnabled, ENABLED_OR_DISABLE_FORMATTER::apply)
		)).build();
	}

	private static OptionGroup getSoundGroup(PatPatClientConfig defConfig, PatPatClientConfig config) {
		return SimpleGroupOptionBuilder.createBuilder("sound").options(collector -> collector.collect(
				collector.getBooleanOption("enable_sounds", defConfig.isSoundsEnabled(), config::isSoundsEnabled, config::setSoundsEnabled, ENABLED_OR_DISABLE_FORMATTER::apply),
				collector.getFloatOptionAsSlider("sounds_volume", 0F, 2F, 0.01F, defConfig.getSoundsVolume(), config::getSoundsVolume, config::setSoundsVolume)
		)).build();
	}

	private static OptionGroup getVisualGroup(PatPatClientConfig defConfig, PatPatClientConfig config) {
		return SimpleGroupOptionBuilder.createBuilder("visual").options(collector -> collector.collect(
						//collector.getBooleanOption("lowering_animation_enabled", defConfig.isLoweringAnimationEnabled(), config::isLoweringAnimationEnabled, config::setLoweringAnimationEnabled, ENABLED_OR_DISABLE_FORMATTER::apply),
				collector.getBooleanOption("hiding_nickname_enabled", defConfig.isNicknameHidingEnabled(), config::isNicknameHidingEnabled, config::setNicknameHidingEnabled, ENABLED_OR_DISABLE_FORMATTER::apply),
				collector.getBooleanOption("swing_hand_enabled", defConfig.isSwingHandEnabled(), config::isSwingHandEnabled, config::setSwingHandEnabled, ENABLED_OR_DISABLE_FORMATTER::apply),
				collector.getFloatOptionAsSlider("hand_offset_x", -5F, 5F, 0.01F, defConfig.getAnimationOffsetX(), config::getAnimationOffsetX, config::setAnimationOffsetX, SimpleContent.WEBP),
				collector.getFloatOptionAsSlider("hand_offset_y", -5F, 5F, 0.01F, defConfig.getAnimationOffsetY(), config::getAnimationOffsetY, config::setAnimationOffsetY, SimpleContent.WEBP),
				collector.getFloatOptionAsSlider("hand_offset_z", -5F, 5F, 0.01F, defConfig.getAnimationOffsetZ(), config::getAnimationOffsetZ, config::setAnimationOffsetZ, SimpleContent.WEBP)
		)).build();
	}

	private static OptionGroup getServerGroup(PatPatClientConfig defConfig, PatPatClientConfig config) {
		return SimpleGroupOptionBuilder.createBuilder("server").options(collector -> collector.collect(
				collector.getBooleanOption("pat_me_enabled", defConfig.isPatMeEnabled(), config::isPatMeEnabled, config::setPatMeEnabled, ENABLED_OR_DISABLE_FORMATTER::apply),
				collector.getBooleanOption("bypass_server_resource_pack_priority_enabled", defConfig.isBypassServerResourcePackEnabled(), config::isBypassServerResourcePackEnabled, config::setBypassServerResourcePackEnabled, ENABLED_OR_DISABLE_FORMATTER::apply)
		)).build();
	}
}
//?} else {
/*public class YACLConfigurationScreen {

	private YACLConfigurationScreen() {
		throw new IllegalStateException("Screen class");
	}

	public static Screen createScreen(Screen parent) {
		throw new IllegalStateException("YACL Screen for PatPat mod available only since 1.20!");
	}
}
*///?}


