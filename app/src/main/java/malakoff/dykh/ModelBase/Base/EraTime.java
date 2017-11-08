package malakoff.dykh.ModelBase.Base;

/**
 * Created by user on 01/01/2016.
 */
public class EraTime extends Time {

    protected String title;

    public EraTime(float beginnig, float duration, String title) {
        super(beginnig, duration);

        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getClosingTime(){
        return this.beginnig + this.duration;
    }
}
