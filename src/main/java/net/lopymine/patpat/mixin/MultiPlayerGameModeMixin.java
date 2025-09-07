package net.lopymine.patpat.mixin;

import com.mojang.authlib.GameProfile;
import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.client.config.resourcepack.PlayerConfig;
import net.lopymine.patpat.client.config.sub.PatPatClientFunConfig;
import net.lopymine.patpat.client.config.sub.PatPatClientFunConfig.PvpMode;
import net.lopymine.patpat.client.render.PatPatClientRenderer;
import net.lopymine.patpat.client.render.PatPatClientRenderer.PacketPat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

	@Inject(at = @At("HEAD"), method = "attack")
	private void patOnAttack(Player player, Entity entity, CallbackInfo ci) {
		if (!PatPatClientConfig.getInstance().getMainConfig().isModEnabled()) {
			return;
		}
		PvpMode pvpMode = PatPatClientConfig.getInstance().getFunConfig().getPvpMode();
		if (pvpMode == PvpMode.DISABLED) {
			return;
		}

		LocalPlayer localPlayer = Minecraft.getInstance().player;
		if (localPlayer != player) {
			return;
		}
		if (pvpMode == PvpMode.NOT_EMPTY_HAND && player.getMainHandItem().isEmpty()) {
			return;
		}
		if (pvpMode == PvpMode.EMPTY_HAND && !player.getMainHandItem().isEmpty()) {
			return;
		}

		if (!(entity instanceof LivingEntity pattedEntity)) {
			return;
		}
		GameProfile profile = /*? if >=1.20.2 {*/ Minecraft.getInstance().getGameProfile(); /*?} else {*/ /*Minecraft.getInstance().getUser().getGameProfile(); *//*?}*/

		PlayerConfig whoPatted = PlayerConfig.of(profile.getName(), profile.getId());
		PatPatClientRenderer.registerClientPacket(new PacketPat(pattedEntity, whoPatted, localPlayer, false));
	}

}
