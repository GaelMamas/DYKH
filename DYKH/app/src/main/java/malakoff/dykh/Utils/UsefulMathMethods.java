package malakoff.dykh.Utils;

/**
 * Created by user on 26/07/2016.
 */

public class UsefulMathMethods {

    public static float computeDistanceBetwen2Points(float x1, float y1, float x2, float y2) {

        return (float) Math.sqrt((x1 - x2) * (x1 - x2)
                + (y1 - y2) * (y1 - y2));
    }


    public static float getResusltAngle(float y, float centerY, float cosinus) {

        float gapAngle = (float) (Math.acos(cosinus) * 180f / Math.PI);

        if (!Float.isNaN(gapAngle)) {
            if (y < centerY) {
                gapAngle = 360 - gapAngle;
            }

        } else {
            return Float.NaN;
        }

        return gapAngle;
    }


    public static float cosThruScalProductBasedOnCenter(float x, float y,
                                                        float centerX, float centerY,
                                                        float xStarter, float yStarter,
                                                        float outerCircleRadius, float norm) {

        return ((xStarter - centerX) * (x - centerX)
                + (yStarter - centerY) * (y - centerY))
                / (outerCircleRadius * norm);
    }

    public static float updateCircleCounter(float previousAngle, float newAngle, float cosine) {
        if(previousAngle >270){
            if(cosine > 0){
                if(newAngle < 90){
                    return newAngle - previousAngle + 360;
                }
            }
        }
        else if(previousAngle < 90){
            if(cosine > 0){
                if(newAngle > 270){
                    return newAngle - previousAngle - 360;
                }
            }
        }

        return newAngle - previousAngle;

    }
}
