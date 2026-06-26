package net.lopymine.patpat.mixin;


import com.llamalad7.mixinextras.injector.wrapoperation.*;
import java.util.function.Predicate;
import lombok.experimental.ExtensionMethod;
import net.lopymine.patpat.extension.EntityExtension;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.component.AttackRange;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@ExtensionMethod(EntityExtension.class)
@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

	@WrapOperation(
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/item/component/AttackRange;getClosesetHit(Lnet/minecraft/world/entity/Entity;FLjava/util/function/Predicate;)Lnet/minecraft/world/phys/HitResult;"
			),
			method = "raycastHitResult"
	)
	private HitResult makeAllMobsPattable(AttackRange instance, Entity entity, float f, Predicate<Entity> predicate, Operation<HitResult> original) {
		if (!entity.isMarked()) {
			return original.call(instance, entity, f, predicate);
		}
		return original.call(instance, entity, f, EntitySelector.NO_SPECTATORS);
	}

}
