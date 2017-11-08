package malakoff.dykh.ModelBase.Base;

/**
 * Created by user on 16/11/2015.
 */
public class Time {

    protected float beginnig;
    protected float duration;

    public Time(float beginnig, float duration) {
        this.beginnig = beginnig;
        this.duration = duration;
    }

    public float getBeginnig() {
        return beginnig;
    }

    public void setBeginnig(float beginnig) {
        this.beginnig = beginnig;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }
}
