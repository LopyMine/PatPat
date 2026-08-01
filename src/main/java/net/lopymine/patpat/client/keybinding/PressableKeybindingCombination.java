package net.lopymine.patpat.client.keybinding;

import com.mojang.blaze3d.platform.InputConstants.Key;
import lombok.*;
import net.lopymine.patpat.mixin.KeyMappingAccessor;
import net.minecraft.client.Minecraft;

@Getter(AccessLevel.PRIVATE)
public class PressableKeybindingCombination extends KeybindingCombination {

	// Shouldn't be used outside this class, use methods below
	private boolean keyPressed;
	private boolean attributeKeyPressed;

	public void setKeyPressed(boolean keyPressed) {
		if (this.getKey() == null) {
			return;
		}
		this.keyPressed = keyPressed;
	}

	public void setAttributeKeyPressed(boolean attributeKeyPressed) {
		if (this.getAttributeKey() == null) {
			return;
		}
		this.attributeKeyPressed = attributeKeyPressed;
	}

	public void setAll(boolean pressed) {
		this.setKeyPressed(pressed);
		this.setAttributeKeyPressed(pressed);
	}

	public void set(Key key, boolean pressed) {
		if (key.equals(this.getKey())) {
			this.setKeyPressed(pressed);
		}
		if (key.equals(this.getAttributeKey())) {
			this.setAttributeKeyPressed(pressed);
		}
	}

	public boolean contains(Key key) {
		return key.equals(this.getKey()) || key.equals(this.getAttributeKey());
	}

	public boolean onlyOneKey() {
		return (this.getKey() == null && this.getAttributeKey() != null) || (this.getKey() != null && this.getAttributeKey() == null);
	}

	public boolean allPressed() {
		if (this.getKey() != null && this.getAttributeKey() != null) {
			return this.isKeyPressed() && this.isSecondKeyPressed();
		}
		if (this.getKey() != null) {
			return this.isKeyPressed();
		}
		if (this.getAttributeKey() != null) {
			return this.isSecondKeyPressed();
		}
		return false;
	}

	private boolean isSecondKeyPressed() {
		if (Minecraft.getInstance().options.toggleCrouch().get()
				&& Minecraft.getInstance().options.keyShift.isDown()
				&& ((KeyMappingAccessor) (Minecraft.getInstance().options.keyShift)).getKeyMappingKey().equals(this.getAttributeKey())
		) {
			return Minecraft.getInstance().options.keyShift.isDown();
		}
		return this.isAttributeKeyPressed();
	}

	public boolean anyPressed() {
		boolean bl = false;
		if (this.getKey() != null) {
			bl |= this.isKeyPressed();
		}
		if (this.getAttributeKey() != null) {
			bl |= this.isSecondKeyPressed();
		}
		return bl;
	}
}
