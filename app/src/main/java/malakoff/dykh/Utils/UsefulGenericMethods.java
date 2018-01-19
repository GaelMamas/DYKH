package malakoff.dykh.Utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import malakoff.dykh.AppApplication.AppApplication;
import malakoff.dykh.AppApplication.Constants;
import malakoff.dykh.Event.Event;
import malakoff.dykh.ModelBase.Base.EraTime;
import malakoff.dykh.ModelBase.EventsPartition;
import malakoff.dykh.ModelBase.TimeLine;
import malakoff.dykh.R;

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
                    new TypeToken<EventsPartition>() {
                    }.getType());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void getMyColorFromTheme(Context context, String theme, TextView themeView, View view) {
        if (themeView == null && view == null) return;

        switch (theme) {
            case Constants.EVENT_THEME_SCIENCE:
            case Constants.EVENT_THEME_INVENTION:
                if (themeView != null)
                    themeView.setTextColor(ContextCompat.getColor(context, R.color.color_event_theme_invention));
                if (view != null)
                    view.setBackgroundColor(ContextCompat.getColor(context, R.color.color_event_theme_invention));
                break;

            case Constants.EVENT_THEME_WAR_EMPIRE:
            case Constants.EVENT_THEME_WAR:
                if (themeView != null)
                    themeView.setTextColor(ContextCompat.getColor(context, R.color.color_event_theme_war));
                if (view != null)
                    view.setBackgroundColor(ContextCompat.getColor(context, R.color.color_event_theme_war));
                break;

            case Constants.EVENT_THEME_DISCOVERY:
                if (themeView != null)
                    themeView.setTextColor(ContextCompat.getColor(context, R.color.color_event_theme_discovery));
                if (view != null)
                    view.setBackgroundColor(ContextCompat.getColor(context, R.color.color_event_theme_discovery));
                break;

            case Constants.EVENT_THEME_PEACE_TREATY:
                if (themeView != null)
                    themeView.setTextColor(ContextCompat.getColor(context, R.color.color_event_theme_peace_treaty));
                if (view != null)
                    view.setBackgroundColor(ContextCompat.getColor(context, R.color.color_event_theme_peace_treaty));
                break;

            default:
                if (themeView != null)
                    themeView.setTextColor(ContextCompat.getColor(context, R.color.color_event_theme_general));
                if (view != null)
                    view.setBackgroundColor(ContextCompat.getColor(context, R.color.color_event_theme_general));
        }
    }

    public static void hideKeyboard(Context context, View mainView) {
        InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (mainView != null) {
            mgr.hideSoftInputFromWindow(mainView.getWindowToken(), 0);
        }
    }

    public static void showKeyboard(Context context, EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }


    public static List<String> getWorldCountriesList() {
        List<String> list = new ArrayList<>();

        String[] isoCountryCodes = Locale.getISOCountries();
        for (String countryCode : isoCountryCodes) {
            Locale locale = new Locale("", countryCode);
            //TODO set DisplayCountry without parameters to let the device give the spelling
            list.add(locale.getDisplayCountry(Locale.US));
        }

        return list;
    }

    public static JSONObject getEventJSONObject(Event event) throws NullPointerException{

        JSONObject params = null;

        try {
            params = new JSONObject();

            params.put("_id", event.getEventId());
            params.put("sliceTime", event.getSliceTime());
            params.put("location", event.getLocation());
            params.put("title", event.getTitle());
            params.put("story", event.getStory());
            params.put("theme", event.getTheme());
            params.put("isValidate", String.valueOf(event.getIsValidate()));
            params.put("locationModernCalling", event.getLocationModernCalling());
            params.put("longitude", String.valueOf(event.getLongitude()));
            params.put("userId", event.getUserId());
            params.put("latitude", String.valueOf(event.getLatitude()));

        } catch (JSONException e) {
            e.printStackTrace();

            new NullPointerException(e.getMessage());
        }

        return params;
    }

    public static boolean isANumeric(String number){

        return !TextUtils.isEmpty(number) && number.matches("[-+]?\\d*\\.?\\d+");

    }
}
