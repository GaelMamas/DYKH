package malakoff.dykh.DesignWidget.ModelBase;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

/**
 * Created by user on 23/12/2015.
 */
public class ArcDrawer {

    private final Paint paint;
    private final float startAngle;
    private final float sweepAngle;
    private int ownColor;
    private String title;
    private Path path;

    public ArcDrawer(Paint paint, int ownColor, float startAngle, float sweepAngle, String title){
        this.paint = paint;
        this.ownColor = ownColor;
        this.startAngle = startAngle;
        this.sweepAngle = sweepAngle;
        this.title = title;

        path = new Path();
    }

    public Paint getPaint() {
        return paint;
    }

    public int getOwnColor() {
        return ownColor;
    }

    public float getStartAngle() {
        return startAngle;
    }

    public float getSweepAngle() {
        return sweepAngle;
    }

    public float getFromZeroToSweepAngle(){return startAngle + sweepAngle;}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPath(RectF rectF) {
        path.addArc(rectF, startAngle, sweepAngle);
    }

    public Path getPath() {
        return path;
    }
}
