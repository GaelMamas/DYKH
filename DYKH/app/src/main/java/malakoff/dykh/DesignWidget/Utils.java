package malakoff.dykh.DesignWidget;

import android.content.res.Resources;

/**
 * Created by user on 19/12/2015.
 */
public class Utils {
    public static float convertDpToPixel(Resources resources, float dp){
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float convertSpToPixel(Resources resources, float sp){
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public static float dpFromPx(Resources resources, final float px) {
        return px / resources.getDisplayMetrics().density;
    }

    public static float interpretAngleInTrigoCircle(float angle) {

        double angleInRadian = angle;

        if(angleInRadian  < 180 ){
            angleInRadian = - angleInRadian * Math.PI/180f;
        }
        else if(angleInRadian == 180){
            angleInRadian = Math.PI;
        }
        else if(angleInRadian > 180){
            angleInRadian = (360 - angleInRadian) * Math.PI/180f;
        }
        else{
            return 0;
        }

        return (float)angleInRadian;
    }

}
