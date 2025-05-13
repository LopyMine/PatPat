package net.lopymine.patpat.modmenu.yacl;

import net.minecraft.client.gui.screen.Screen;
//? >=1.20.1 {
import dev.isxander.yacl3.api.*;
import lombok.experimental.ExtensionMethod;

import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.client.config.sub.*;
import net.lopymine.patpat.modmenu.yacl.custom.base.*;
import net.lopymine.patpat.modmenu.yacl.custom.extension.SimpleOptionExtension;
import net.lopymine.patpat.modmenu.yacl.custom.screen.SimpleYACLScreen;
import net.lopymine.patpat.modmenu.yacl.custom.utils.*;

@ExtensionMethod(SimpleOptionExtension.class)
public class YACLConfigurationScreen {

	private YACLConfigurationScreen() {
		throw new IllegalStateException("Screen class");
	}

	public static Screen createScreen(Screen parent) {
		PatPatClientConfig defConfig = PatPatClientConfig.getNewInstance();
		PatPatClientConfig config = PatPatClientConfig.getInstance();

		return SimpleYACLScreen.startBuilder(parent, config::saveAsync)
				.categories(getGeneralCategory(defConfig, config))
				.build();
	}

	private static ConfigCategory getGeneralCategory(PatPatClientConfig defConfig, PatPatClientConfig config) {
		return SimpleCategory.startBuilder("general")
				.groups(getMainGroup(defConfig.getMainConfig(), config.getMainConfig()))
				.groups(getResourcePacksGroup(defConfig.getResourcePacksConfig(), config.getResourcePacksConfig()))
				.groups(getSoundGroup(defConfig.getSoundsConfig(), config.getSoundsConfig()))
				.groups(getVisualGroup(defConfig.getVisualConfig(), config.getVisualConfig()))
				.groups(getServerGroup(defConfig.getServerConfig(), config.getServerConfig()))
				.build();
	}

	private static OptionGroup getMainGroup(PatPatClientMainConfig defConfig, PatPatClientMainConfig config) {
		return SimpleGroup.startBuilder("main").options(
				SimpleOption.<Boolean>startBuilder("enable_mod")
						.withBinding(defConfig.isModEnabled(), config::isModEnabled, config::setModEnabled, false)
						.withController()
						.withDescription(SimpleContent.NONE)
						.build(),
				SimpleOption.<Boolean>startBuilder("debug_log_enabled")
						.withBinding(defConfig.isDebugLogEnabled(), config::isDebugLogEnabled, config::setDebugLogEnabled, false)
						.withController()
						.withDescription(SimpleContent.NONE)
						.build()
		).build();
	}

	private static OptionGroup getResourcePacksGroup(PatPatClientResourcePacksConfig defConfig, PatPatClientResourcePacksConfig config) {
		return SimpleGroup.startBuilder("resource_packs").options(
				SimpleOption.<Boolean>startBuilder("skip_outdated_animations_enabled")
						.withBinding(defConfig.isSkipOldAnimationsEnabled(), config::isSkipOldAnimationsEnabled, config::setSkipOldAnimationsEnabled, false)
						.withController()
						.withDescription(SimpleContent.NONE)
						.build()
		).build();
	}

	private static OptionGroup getSoundGroup(PatPatClientSoundsConfig defConfig, PatPatClientSoundsConfig config) {
		return SimpleGroup.startBuilder("sound").options(
				SimpleOption.<Boolean>startBuilder("enable_sounds")
						.withBinding(defConfig.isSoundsEnabled(), config::isSoundsEnabled, config::setSoundsEnabled, false)
						.withController()
						.withDescription(SimpleContent.NONE)
						.build(),
				SimpleOption.<Float>startBuilder("sounds_volume")
						.withBinding(defConfig.getSoundsVolume(), config::getSoundsVolume, config::setSoundsVolume, false)
						.withController(0.0F, 2.0F, 0.01F)
						.withDescription(SimpleContent.NONE)
						.build()
		).build();
	}

	private static OptionGroup getVisualGroup(PatPatClientVisualConfig defConfig, PatPatClientVisualConfig config) {
		return SimpleGroup.startBuilder("visual").options(
				SimpleOption.<Boolean>startBuilder("hiding_nickname_enabled")
						.withBinding(defConfig.isHidingNicknameEnabled(), config::isHidingNicknameEnabled, config::setHidingNicknameEnabled, false)
						.withController()
						.withDescription(SimpleContent.NONE)
						.build(),
				SimpleOption.<Boolean>startBuilder("swing_hand_enabled")
						.withBinding(defConfig.isSwingHandEnabled(), config::isSwingHandEnabled, config::setSwingHandEnabled, false)
						.withController()
						.withDescription(SimpleContent.NONE)
						.build(),
				SimpleOption.<Float>startBuilder("hand_offset_x")
						.withBinding(defConfig.getAnimationOffsets().getX(), config.getAnimationOffsets()::getX, config.getAnimationOffsets()::setX, false)
						.withController(-5F, 5F, 0.01F)
						.withDescription(SimpleContent.NONE)
						.build(),
				SimpleOption.<Float>startBuilder("hand_offset_y")
						.withBinding(defConfig.getAnimationOffsets().getY(), config.getAnimationOffsets()::getY, config.getAnimationOffsets()::setY, false)
						.withController(-5F, 5F, 0.01F)
						.withDescription(SimpleContent.NONE)
						.build(),
				SimpleOption.<Float>startBuilder("hand_offset_z")
						.withBinding(defConfig.getAnimationOffsets().getZ(), config.getAnimationOffsets()::getZ, config.getAnimationOffsets()::setZ, false)
						.withController(-5F, 5F, 0.01F)
						.withDescription(SimpleContent.NONE)
						.build(),
				SimpleOption.<Boolean>startBuilder("camera_shacking")
						.withBinding(defConfig.isCameraShackingEnabled(), config::isCameraShackingEnabled, config::setCameraShackingEnabled, false)
						.withController()
						.withDescription(SimpleContent.NONE)
						.build(),
				SimpleOption.<Float>startBuilder("pat_weight")
						.withBinding(defConfig.getPatWeight(), config::getPatWeight, config::setPatWeight, false)
						.withController(0.0F, 1.0F, 0.01F)
						.withDescription(SimpleContent.NONE)
						.build()
		).build();
	}

	private static OptionGroup getServerGroup(PatPatClientServerConfig defConfig, PatPatClientServerConfig config) {
		return SimpleGroup.startBuilder("server").options(
				SimpleOption.<Boolean>startBuilder("pat_me_enabled")
						.withBinding(defConfig.isPatMeEnabled(), config::isPatMeEnabled, config::setPatMeEnabled, false)
						.withController()
						.withDescription(SimpleContent.NONE)
						.build(),
				SimpleOption.<Boolean>startBuilder("bypass_server_resource_pack_priority_enabled")
						.withBinding(defConfig.isBypassServerResourcePackPriorityEnabled(), config::isBypassServerResourcePackPriorityEnabled, config::setBypassServerResourcePackEnabled, false)
						.withController()
						.withDescription(SimpleContent.NONE)
						.build(),
				SimpleOption.<Boolean>startBuilder("prox_lib_enabled")
						.withBinding(defConfig.isProxLibEnabled(), config::isProxLibEnabled, config::setProxLibEnabled, false)
						.withController()
						.withDescription(SimpleContent.NONE)
						.build()
		).build();
	}
}
//?} else {
/*public class YACLConfigurationScreen {

	private YACLConfigurationScreen() {
		throw new IllegalStateException("Screen class");
	}

	public static Screen createScreen(Screen parent) {
		throw new IllegalStateException("YACL Screen for PatPat mod available only since 1.20.1!");
	}
}
*///?}


