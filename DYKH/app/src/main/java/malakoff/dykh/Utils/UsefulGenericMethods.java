package malakoff.dykh.Utils;

import android.content.Context;
import android.support.annotation.RawRes;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import malakoff.dykh.AppApplication.AppApplication;
import malakoff.dykh.AppApplication.Constants;
import malakoff.dykh.Event.Event;
import malakoff.dykh.ModelBase.Base.EraTime;
import malakoff.dykh.ModelBase.EventsPartition;
import malakoff.dykh.ModelBase.TimeLine;

/**
 * Created by user on 28/07/2016.
 */

public class UsefulGenericMethods {

    public static TimeLine createTimeLine(String[] eraTitles, Random rnd) {

        List<EraTime> eraTimes = new ArrayList<>();

        for (int j = 0; j < eraTitles.length; j++) {
            EraTime era = j > 0 ? new EraTime(eraTimes.get(j - 1).getClosingTime(),
                    rnd.nextInt((1000 - 200) + 1) + 200, eraTitles[j])

                    : new EraTime(0, rnd.nextInt((1000 - 200) + 1) + 200, eraTitles[j]);

            eraTimes.add(era);
        }

        return new TimeLine(eraTimes);
    }


    public static TimeLine getDefaultTimeLine(String[] eraTitles) {

        List<EraTime> eraTimes = new ArrayList<>();

        eraTimes.add(new EraTime(0, Constants.BENCH_TIME_ORIGINE - Constants.ANTIQUITY_GO - 1, eraTitles[0]));
        eraTimes.add(new EraTime(eraTimes.get(0).getClosingTime(), Constants.ANTIQUITY_GO + Constants.MIDDLE_AGES_GO, eraTitles[1]));
        eraTimes.add(new EraTime(eraTimes.get(1).getClosingTime(), Constants.MODERN_TIMES_GO - Constants.MIDDLE_AGES_GO, eraTitles[2]));
        eraTimes.add(new EraTime(eraTimes.get(2).getClosingTime(), Constants.CONTEMPORARY_TIMES_GO - Constants.MODERN_TIMES_GO, eraTitles[3]));
        eraTimes.add(new EraTime(eraTimes.get(3).getClosingTime(), Constants.ARBITRARY_END_OF_TIMES - Constants.CONTEMPORARY_TIMES_GO, eraTitles[4]));

        return new TimeLine(eraTimes);
    }

    public static EventsPartition setDefaultModelData(InputStream inputStream) {
        try {
            int size = inputStream.available();

            byte[] buffer = new byte[size];

            inputStream.read(buffer);

            inputStream.close();

            return new Gson().fromJson(new String(buffer, Constants.DYKH_UTF8_ENCODING_STANDARD),
                    new TypeToken<EventsPartition>() {}.getType());

        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
