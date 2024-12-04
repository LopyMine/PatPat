package net.lopymine.patpat.renderer;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

import net.lopymine.patpat.entity.PatEntity;
import net.lopymine.patpat.manager.client.PatPatClientManager;

public class PatAnimationRenderer {

	public static void scaleEntityIfPatted(LivingEntity livingEntity, MatrixStack matrixStack){
		PatEntity patEntity = PatPatClientManager.getPatEntity(livingEntity);
		if (patEntity == null) {
			return;
		}

		if (PatPatClientManager.expired(patEntity)) {
			PatPatClientManager.removePatEntity(patEntity);
			return;
		}

		matrixStack.scale(1F, PatPatClientManager.getAnimationProgress(patEntity), 1F);
	}
}
