package malakoff.dykh.Utils;


import android.util.Log;

import malakoff.dykh.AppApplication.Constants;
import malakoff.dykh.Interfaces.DetectCounterChangleListener;
import malakoff.dykh.ModelBase.Base.EraTime;
import malakoff.dykh.ModelBase.TimeLine;

/**
 * Created by user on 28/07/2016.
 */

public class TimeClipperDecoder implements DetectCounterChangleListener {

    private int eraNumber;
    private int currentEra = 0;
    private float offsetTimeSinceThen = 0;

    private float eraIncrement;
    private String decodedTime;

    private TimeLine timeLine;

    private float circleSliceLength;


    public TimeClipperDecoder(TimeLine timeLine) {

        this.eraIncrement = 0;
        this.decodedTime = Constants.BEGINNING_OF_MANTIME;
        this.timeLine = timeLine;
        this.eraNumber = timeLine.getEraTimeList().size();
        this.circleSliceLength = 360/eraNumber;
    }

    public int getEraNumber() {
        return eraNumber;
    }

    public synchronized  int getCurrentEra() {
        return currentEra;
    }

    public void setEraNumber(int eraNumber) {
        this.eraNumber = eraNumber;
    }

    public synchronized String getDecodedTime() {
        return decodedTime;
    }

    public synchronized void setDecodedTime(String decodedTime) {
        this.decodedTime = decodedTime;
    }

    public synchronized void setDecodedTime2(float counter) {
        this.decodedTime = decodeTimeToString(counter);
    }

    public synchronized float getEraIncrement() {
        return eraIncrement;
    }

    public synchronized void setEraIncrement(float counter) {
        try {
            currentEra = getPreciseEraIndex(counter);
            this.eraIncrement = (currentEra > 0? counter - offsetTimeSinceThen:counter) * circleSliceLength/timeLine.getEraTimeList().get(currentEra).getDuration();
        } catch (IndexOutOfBoundsException e) {
            Log.e("Time Computation", e.getMessage());
        }

    }

    private int getPreciseEraIndex(float counter) {

        float sum = 1;

        for (int j = 0; j < timeLine.getEraTimeList().size(); j++) {
            EraTime era = timeLine.getEraTimeList().get(j);
            sum += era.getDuration();

            if (counter < sum) {
                offsetTimeSinceThen = sum - era.getDuration();
                return j;
            }
        }

        return currentEra;
    }

    private float offsetSinceThen(float counter) throws IndexOutOfBoundsException {

        float sum = 1;

        for (int j = 0; j < timeLine.getEraTimeList().size(); j++) {
            EraTime era = timeLine.getEraTimeList().get(j);
            sum += era.getDuration();

            if (counter < sum) {
                return sum - era.getDuration();
            }
        }

        throw new IndexOutOfBoundsException("Counter Is Out of Time");
    }

    private float offsetSinceThen(int seletedEra) throws IndexOutOfBoundsException {

        float sum = 1;

        for (int j = 0; j < timeLine.getEraTimeList().size(); j++) {
            if(seletedEra <= j) {
                return sum;
            }
            sum += timeLine.getEraTimeList().get(j).getDuration();
        }

        return  sum;
    }

    private String decodeTimeToString(float counter) {

        if (counter - Constants.BENCH_TIME_ORIGINE < 0) {
            return decodeTime(counter) + " BC";
        }
        else if(counter == Constants.BENCH_TIME_ORIGINE){
            return "0";
        } else {
            return decodeTime(counter) + " AD";
        }
    }

    private int decodeTime(float counter) {

        if (counter <= Constants.BENCH_TIME_ORIGINE) {
            return Math.round(Constants.BENCH_TIME_ORIGINE - counter);
        } else if (counter == 0) {
            return (int) Constants.BENCH_TIME_ORIGINE;
        }else if (counter > Constants.BENCH_TIME_ORIGINE + Constants.ARBITRARY_END_OF_TIMES){
            return (int)Constants.ARBITRARY_END_OF_TIMES;
        }else {
            return Math.round(counter - Constants.BENCH_TIME_ORIGINE);
        }

    }

    @Override
    public void onDetectCounterChange(float counter) {
        setEraIncrement(counter);
        setDecodedTime2(counter);
    }

    public float shiftTime(int selectedEra) {
        currentEra = selectedEra;
        float mTimeCounter = offsetSinceThen(selectedEra);
        setDecodedTime2(mTimeCounter);
        return  mTimeCounter;
    }
}
