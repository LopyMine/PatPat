package net.lopymine.patpat.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.Util;

public class PatAnimation {
    private final long timeOfStart;
    private final int duration;
    private int frame;

    public PatAnimation(long timeOfStart, int duration) {
        this.timeOfStart = timeOfStart;
        this.duration = duration;
        this.frame = 0;
    }

    public static PatAnimation of(Entity entity) { // TODO
        return new PatAnimation(Util.getMeasuringTimeMs(), 320);
    }

    public long getTimeOfStart() {
        return this.timeOfStart;
    }

    public int getDuration() {
        return this.duration;
    }

    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }
}
