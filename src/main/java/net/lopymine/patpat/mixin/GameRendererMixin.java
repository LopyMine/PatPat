package net.lopymine.patpat.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import lombok.experimental.ExtensionMethod;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.*;
import net.minecraft.world.phys.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.lopymine.patpat.extension.EntityExtension;

import java.util.function.Predicate;
import org.jetbrains.annotations.Nullable;

@ExtensionMethod(EntityExtension.class)
@Mixin(GameRenderer.class)
public class GameRendererMixin {

	//? if >=1.20.5 {
	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/ProjectileUtil;getEntityHitResult(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;D)Lnet/minecraft/world/phys/EntityHitResult;"), method = "pick(Lnet/minecraft/world/entity/Entity;DDF)Lnet/minecraft/world/phys/HitResult;")
	//?} else {
	/*@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/ProjectileUtil;getEntityHitResult(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;D)Lnet/minecraft/world/phys/EntityHitResult;"), method = "pick")
	*///?}
	private @Nullable EntityHitResult makeAllMobsPattable(Entity entity, Vec3 vec3, Vec3 vec32, AABB aABB, Predicate<Entity> predicate, double d, Operation<EntityHitResult> original) {
		if (!entity.isMarked()) {
			return original.call(entity, vec3, vec32, aABB, predicate, d);
		}
		return original.call(entity, vec3, vec32, aABB, EntitySelector.NO_SPECTATORS, d);
	}
}
