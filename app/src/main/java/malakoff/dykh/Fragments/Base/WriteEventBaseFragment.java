package malakoff.dykh.Fragments.Base;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import malakoff.dykh.DesignWidget.BetterSpinner;
import malakoff.dykh.Interfaces.ResetInputsListener;
import malakoff.dykh.Network.MySingleton;
import malakoff.dykh.R;
import malakoff.dykh.Utils.UsefulGenericMethods;

/**
 * Created by VM32776N on 03/03/2017.
 */

public class WriteEventBaseFragment extends InstanceBaseFragement implements View.OnClickListener, AdapterView.OnItemSelectedListener, TextView.OnEditorActionListener {

    private final static String BCAD_STEP = "bc ad", YEAR_STEP = "year", MONTH_STEP = "month", DAY_STEP = "day";

    protected String selectedTheme, selectedTodayLocaction, selectedBCAD, dykhEventDateFormat;
    protected int monthIndex = 0;
    protected String[] newEventFinalDate;
    protected List<EventDate> eventDates = new ArrayList<>();

    protected BetterSpinner themeSpinner, todayLocationSpinner, mBCADSpinner, mDatedTypeEventSpinner, monthSpinner, daySpinner;
    protected AppCompatEditText titleEditText, historicLocationEditText, storyEditText, yearEditText;
    protected View dateSetterLayout;
    protected DatePicker datePicker;
    protected Button publishButton;
    protected ProgressBar mEventUpdatingProgressBar;


    protected ResetInputsListener resetInputsListener = new ResetInputsListener() {
        @Override
        public void succeed() {

            titleEditText.setText("");
            storyEditText.setText("");
            historicLocationEditText.setText("");

            themeSpinner.resetPlaceHolderText();
            todayLocationSpinner.resetPlaceHolderText();
            mBCADSpinner.resetPlaceHolderText();

            dateSetterLayout.setVisibility(View.GONE);

            datePicker.setVisibility(View.GONE);

            mEventUpdatingProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void fail() {

            mEventUpdatingProgressBar.setVisibility(View.GONE);

        }

        @Override
        public boolean canProcess() {
            return mEventUpdatingProgressBar.getVisibility() == View.GONE;
        }
    };


    @Override
    protected void assignViews(View view) {
        themeSpinner = view.findViewById(R.id.spinner_event_theme);
        todayLocationSpinner = view.findViewById(R.id.spinner_event_today_location);
        mBCADSpinner = view.findViewById(R.id.spinner_event_bc_or_ad);
        mDatedTypeEventSpinner = view.findViewById(R.id.spinner_event_single_date);

        titleEditText = view.findViewById(R.id.edittext_event_title);
        historicLocationEditText = view.findViewById(R.id.edittext_event_location);
        storyEditText = view.findViewById(R.id.edittext_event_story);

        dateSetterLayout = view.findViewById(R.id.event_date_setter_layout);

        yearEditText = view.findViewById(R.id.edittext_event_year_setter);
        monthSpinner = view.findViewById(R.id.spinner_event_month_setter);
        daySpinner = view.findViewById(R.id.spinner_event_day_setter);

        datePicker = view.findViewById(R.id.datePicker);

        publishButton = view.findViewById(R.id.button_event_publish);

        mEventUpdatingProgressBar = view.findViewById(R.id.progressbar_event_updating);
    }


    @Override
    protected void populateViews(Bundle savedInstanceState) {
        super.populateViews(savedInstanceState);

        ArrayAdapter<String> themeAdapter = new ArrayAdapter<>(getContext(),
                R.layout.cell_text_dark,
                Arrays.asList(getResources().getStringArray(R.array.event_themes)));

        themeAdapter.setDropDownViewResource(R.layout.cell_text_darker);

        ArrayAdapter<String> todayLocationAdapter = new ArrayAdapter<>(getContext(),
                R.layout.cell_text_dark,
                UsefulGenericMethods.getWorldCountriesList());

        todayLocationAdapter.setDropDownViewResource(R.layout.cell_text_darker);

        ArrayAdapter<String> mBCADAdapter = new ArrayAdapter<>(getContext(),
                R.layout.cell_text_dark,
                Arrays.asList(getResources().getStringArray(R.array.event_bc_or_ad)));

        mBCADAdapter.setDropDownViewResource(R.layout.cell_text_darker);

        ArrayAdapter<String> mDatedTypeAdapter = new ArrayAdapter<>(getContext(),
                R.layout.cell_text_dark,
                Arrays.asList(getResources().getStringArray(R.array.event_single_couple_dated)));


        List<String> monthsList = new ArrayList<>();
        monthsList.add("Unknown");
        monthsList.addAll(Arrays.asList(DateFormatSymbols.getInstance(Locale.getDefault()).getMonths()));
        ArrayAdapter<String> mMonthsAdapter = new ArrayAdapter<>(getContext(),
                R.layout.cell_text_dark,
                monthsList);


        ArrayAdapter<String> mDaysAdapter = new ArrayAdapter<>(getContext(),
                R.layout.cell_text_dark,
                setMonthDays());

        themeSpinner.setAdapter(themeAdapter);
        themeSpinner.setOnItemSelectedListener(this);

        todayLocationSpinner.setAdapter(todayLocationAdapter);
        todayLocationSpinner.setOnItemSelectedListener(this);


        mBCADSpinner.setAdapter(mBCADAdapter);
        mBCADSpinner.setOnItemSelectedListener(this);

        mDatedTypeEventSpinner.setAdapter(mDatedTypeAdapter);
        mDatedTypeEventSpinner.setOnItemSelectedListener(this);

        yearEditText.setOnEditorActionListener(this);

        monthSpinner.setAdapter(mMonthsAdapter);
        monthSpinner.setOnItemSelectedListener(this);

        daySpinner.setAdapter(mDaysAdapter);
        daySpinner.setOnItemSelectedListener(this);

        publishButton.setOnClickListener(this);


        newEventFinalDate = new String[3];

        for (int i = 0; i < newEventFinalDate.length; i++) {
            newEventFinalDate[i] = "";
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        UsefulGenericMethods.hideKeyboard(getContext(), getView());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_event_creation;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch ((int) id) {

            case R.id.spinner_event_theme:

                selectedTheme = (String) parent.getItemAtPosition(position);

                break;

            case R.id.spinner_event_today_location:


                selectedTodayLocaction = (String) parent.getItemAtPosition(position);

                if (!TextUtils.isEmpty(selectedTodayLocaction))
                    Toast.makeText(getContext(), "Set precisely the location", Toast.LENGTH_SHORT).show();

                break;


            case R.id.spinner_event_single_date:


                mDatedTypeEventSpinner.setError(null);


                if (position == 0) {

                    if (eventDates.isEmpty()) {


                        mBCADSpinner.setVisibility(View.VISIBLE);


                    }else if(eventDates.size() > 0){

                        String firstDateBCAD = eventDates.get(position).getmBCAD();
                        String firstDateYear = eventDates.get(position).getYear();

                        if(!TextUtils.isEmpty(firstDateBCAD)){

                            mBCADSpinner.setVisibility(View.VISIBLE);
                            mBCADSpinner.setSelection(firstDateBCAD
                                    .contentEquals(getResources().getStringArray(R.array.event_bc_or_ad)[0])?0:1);

                        }

                        if(!TextUtils.isEmpty(firstDateYear)){

                            dateSetterLayout.setVisibility(View.VISIBLE);
                            yearEditText.setText(firstDateYear);

                            String month = eventDates.get(position).getMonth();

                            if(!TextUtils.isEmpty(month)){

                                monthSpinner.setVisibility(View.VISIBLE);
                                monthSpinner.setSelection(month.matches("[-+]?\\d*\\.?\\d+") ?
                                        Integer.parseInt(month):0);


                                if(monthSpinner.getSelectedIndex() > 0){

                                    String day = eventDates.get(position).getDay();

                                    if(!TextUtils.isEmpty(day)){


                                        daySpinner.setVisibility(View.VISIBLE);
                                        daySpinner.setSelection(day.matches("[-+]?\\d*\\.?\\d+") ?
                                                Integer.parseInt(day):0);


                                    }else {

                                        daySpinner.setVisibility(View.GONE);

                                    }

                                }

                            }else{

                                monthSpinner.setVisibility(View.GONE);

                            }

                        }else{

                            dateSetterLayout.setVisibility(View.GONE);

                        }


                    }


                } else {


                    if (eventDates.isEmpty()) {

                        mDatedTypeEventSpinner.setError("Define first the the first date");
                        mDatedTypeEventSpinner.resetPlaceHolderText();
                        mBCADSpinner.setVisibility(View.GONE);
                        dateSetterLayout.setVisibility(View.GONE);

                    } else {

                        mBCADSpinner.resetPlaceHolderText();
                        mBCADSpinner.setVisibility(View.VISIBLE);
                        dateSetterLayout.setVisibility(View.GONE);

                    }

                }


                break;


            case R.id.spinner_event_bc_or_ad:


                mBCADSpinner.setError(null);


                if (eventDates.isEmpty()) {

                    if (mDatedTypeEventSpinner.getSelectedIndex() == 1) {


                        if (areNot2DatesChronological(BCAD_STEP, (String) parent.getItemAtPosition(position), false)) {


                            mBCADSpinner.setError(getString(R.string.event_chronological_alert_message));


                        }

                    } else {

                        setLayoutsAfterBCAD((String) parent.getItemAtPosition(position), position);

                    }

                } else if (mDatedTypeEventSpinner.getSelectedIndex() == 1) {


                    checkBCADInputs((String) parent.getItemAtPosition(position), position, false);


                } else if (eventDates.size() > 1) {


                    checkBCADInputs((String) parent.getItemAtPosition(position), position, true);


                }


                break;


            case R.id.spinner_event_month_setter:


                monthSpinner.setError(null);


                monthIndex = position;



                if (setMonthDays() != null) {

                    String month = (String) parent.getItemAtPosition(position);

                    switch (month) {
                        case "Unknown":


                            if (eventDates.isEmpty()) {


                                eventDates.add(new EventDate(selectedBCAD, yearEditText.getText().toString(), null, null));


                                mDatedTypeEventSpinner.requestFocus();



                            } else if (mDatedTypeEventSpinner.getSelectedIndex() == 1) {

                                if (eventDates.size() == 2) {

                                    eventDates.set(1, new EventDate(selectedBCAD, yearEditText.getText().toString(), null, null));

                                } else {

                                    eventDates.add(new EventDate(selectedBCAD, yearEditText.getText().toString(), null, null));

                                }

                            } else {

                                eventDates.set(0, new EventDate(selectedBCAD, yearEditText.getText().toString(), null, null));


                            }


                            break;
                        default:

                            if (eventDates.isEmpty()) {


                                daySpinner.setVisibility(View.VISIBLE);
                                daySpinner.setAdapter(new ArrayAdapter<>(getContext(),
                                        R.layout.cell_text_dark,
                                        setMonthDays()));


                            } else if (mDatedTypeEventSpinner.getSelectedIndex() == 1) {


                                if (areNot2DatesChronological(MONTH_STEP, month, false)) {


                                    monthSpinner.setError(getString(R.string.event_chronological_alert_message));


                                } else {

                                    daySpinner.setVisibility(View.VISIBLE);
                                    daySpinner.setAdapter(new ArrayAdapter<>(getContext(),
                                            R.layout.cell_text_dark,
                                            setMonthDays()));

                                }


                            } else if (eventDates.size() > 1) {


                                if (areNot2DatesChronological(MONTH_STEP, month, true)) {


                                    monthSpinner.setError(getString(R.string.event_chronological_alert_message));


                                } else {


                                    daySpinner.setVisibility(View.VISIBLE);
                                    daySpinner.setAdapter(new ArrayAdapter<>(getContext(),
                                            R.layout.cell_text_dark,
                                            setMonthDays()));


                                }


                            }

                    }


                }


            case R.id.spinner_event_day_setter:


                daySpinner.setError(null);


                String day = (String) parent.getItemAtPosition(position);



                switch (day) {
                    case "Unknown":

                        if (eventDates.isEmpty()) {

                            eventDates.add(new EventDate(selectedBCAD, yearEditText.getText().toString(), String.valueOf(monthIndex), null));

                            mDatedTypeEventSpinner.requestFocus();

                        } else if (mDatedTypeEventSpinner.getSelectedIndex() == 1) {

                            if (eventDates.size() == 2) {

                                eventDates.set(1, new EventDate(selectedBCAD, yearEditText.getText().toString(), String.valueOf(monthIndex), null));

                            } else {

                                eventDates.add(new EventDate(selectedBCAD, yearEditText.getText().toString(), String.valueOf(monthIndex), null));

                            }

                        } else if (eventDates.size() > 1) {

                            if (areNot2DatesChronological(DAY_STEP, day, true)) {

                                daySpinner.setError(getString(R.string.event_chronological_alert_message));

                            } else {

                                eventDates.set(0, new EventDate(selectedBCAD, yearEditText.getText().toString(), String.valueOf(monthIndex), null));

                            }


                        }


                    default:

                        if (eventDates.isEmpty()) {

                            eventDates.add(new EventDate(selectedBCAD, yearEditText.getText().toString(), String.valueOf(monthIndex), String.valueOf(position)));

                            mDatedTypeEventSpinner.requestFocus();

                        } else if (mDatedTypeEventSpinner.getSelectedIndex() == 1) {

                            if (eventDates.size() == 2) {

                                eventDates.set(1, new EventDate(selectedBCAD, yearEditText.getText().toString(), String.valueOf(monthIndex), day));

                            } else {

                                eventDates.add(new EventDate(selectedBCAD, yearEditText.getText().toString(), String.valueOf(monthIndex), day));

                            }

                        } else if (eventDates.size() > 1) {

                            if (areNot2DatesChronological(DAY_STEP, day, true)) {

                                daySpinner.setError(getString(R.string.event_chronological_alert_message));

                            } else {

                                eventDates.set(0, new EventDate(selectedBCAD, yearEditText.getText().toString(), String.valueOf(monthIndex), day));

                            }


                        }
                }


                break;

        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        switch (v.getId()) {
            case R.id.edittext_event_year_setter:

                yearEditText.setError(null);

                if (event != null && event.getKeyCode() == KeyEvent.FLAG_EDITOR_ACTION) {

                    UsefulGenericMethods.hideKeyboard(getContext(), yearEditText);

                }


                String inputYear = v.getText().toString();


                if (TextUtils.isEmpty(inputYear)
                        && inputYear.matches("[-+]?\\d*\\.?\\d+")) {


                    newEventFinalDate[0] = "";
                    return false;


                } else if (mDatedTypeEventSpinner.getSelectedIndex() == 1
                        && areNot2DatesChronological(YEAR_STEP, inputYear, false)) {

                    yearEditText.setError(getString(R.string.event_chronological_alert_message));
                    return false;

                }

                int year = Integer.parseInt(inputYear);

                if (selectedBCAD.contentEquals(getResources().getStringArray(R.array.event_bc_or_ad)[0])) {
                    newEventFinalDate[0] = String.valueOf(year);
                    monthSpinner.setVisibility(View.VISIBLE);
                } else if (selectedBCAD.contentEquals(getResources().getStringArray(R.array.event_bc_or_ad)[1])) {
                    if (year >= 1900 && year <= Calendar.getInstance().get(Calendar.YEAR)) {
                        dateSetterLayout.setVisibility(View.GONE);
                        datePicker.setVisibility(View.VISIBLE);
                        datePicker.init(year, 0, 1, new DatePicker.OnDateChangedListener() {
                            @Override
                            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                if (monthOfYear <= Calendar.getInstance().get(Calendar.MONTH)
                                        && dayOfMonth <= Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {

                                    newEventFinalDate[0] = String.valueOf(year);
                                    newEventFinalDate[1] = String.valueOf(monthOfYear);
                                    newEventFinalDate[2] = String.valueOf(dayOfMonth);

                                    Toast.makeText(getContext(), newEventFinalDate[0] + "-" + newEventFinalDate[1] + "-" + newEventFinalDate[2], Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(getContext(), "Please enter correct date", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else if (year <= Calendar.getInstance().get(Calendar.YEAR)) {
                        monthSpinner.setVisibility(View.VISIBLE);
                        newEventFinalDate[0] = inputYear;
                    }
                }

                return true;
        }


        return false;
    }

    private List<String> setMonthDays() {

        int number = setMonthDaysNumber();

        if (number == 0) return null;

        List<String> monthDay = new ArrayList<>();

        monthDay.add("Unknown");


        for (int i = 0; i < number; i++) {

            monthDay.add(String.valueOf(i + 1));

        }


        return monthDay;
    }

    private int setMonthDaysNumber() {
        switch (monthIndex) {
            case 1:

                return 29;
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:

                return 31;

            case 3:
            case 5:
            case 8:
            case 10:

                return 30;

            default:
                return 0;
        }
    }


    protected String checkEventDateFormat() {

        // beginning year BC/AD, {start date BC/AD - end date BC/AD}

        if (!TextUtils.isEmpty(yearEditText.getText())) {

            if (monthIndex >= 0 && monthIndex <= 11) {

                if (daySpinner.getVisibility() == View.VISIBLE) {


                }


            } else if (monthIndex == 12) {

                return newEventFinalDate[0] + " " + selectedBCAD;

            }


        }

        return "";

    }

    private void checkBCADInputs(String value, int position, boolean backwardTime){

        if (areNot2DatesChronological(BCAD_STEP, value, backwardTime)) {


            mBCADSpinner.setError(getString(R.string.event_chronological_alert_message));


        } else {

            setLayoutsAfterBCAD(value, position);

        }

    }


    private void setLayoutsAfterBCAD(String value, int position){

        selectedBCAD = value;

        if(eventDates.size() > 0){


            switch (position){
                case 0:

                    eventDates.get(0).setmBCAD(value);

                case 1:

                if(eventDates.size() > 1){

                    eventDates.get(1).setmBCAD(value);

                }
            }


        }

        datePicker.setVisibility(View.GONE);
        dateSetterLayout.setVisibility(View.VISIBLE);
        monthSpinner.setVisibility(View.GONE);
        daySpinner.setVisibility(View.GONE);

        yearEditText.setText("");
        monthSpinner.resetPlaceHolderText();
        daySpinner.resetPlaceHolderText();

    }


    private boolean areNot2DatesChronological(String step, String value, boolean backwardTime) {

        switch (step) {

            case BCAD_STEP:

                return getResources().getStringArray(R.array.event_bc_or_ad)[1]
                        .contentEquals(eventDates.get(0).getmBCAD())
                        && getResources().getStringArray(R.array.event_bc_or_ad)[0]
                        .contentEquals(value);

            case YEAR_STEP:

                if (eventDates.get(backwardTime ? 1 : 0).getmBCAD().contentEquals(selectedBCAD)) {

                    if (getResources().getStringArray(R.array.event_bc_or_ad)[0]
                            .contentEquals(eventDates.get(0).getmBCAD())) {

                        return Integer.parseInt(value) <= Integer.parseInt(eventDates.get(backwardTime ? 1 : 0).getYear());

                    } else if (getResources().getStringArray(R.array.event_bc_or_ad)[1]
                            .contentEquals(eventDates.get(0).getmBCAD())) {

                        return Integer.parseInt(value) >= Integer.parseInt(eventDates.get(backwardTime ? 1 : 0).getYear());

                    }

                    return true;

                }

                return false;

            case MONTH_STEP:

                if (!TextUtils.isEmpty(value)
                        && eventDates.get(backwardTime ? 1 : 0).getYear().contentEquals(value)) {


                    return backwardTime ?
                            Integer.parseInt(eventDates.get(1).getMonth()) < Integer.parseInt(value) :
                            Integer.parseInt(eventDates.get(0).getMonth()) > Integer.parseInt(value);


                }

                return false;


            case DAY_STEP:

                if (!TextUtils.isEmpty(value)
                        && eventDates.get(backwardTime ? 1 : 0).getYear().contentEquals(value)
                        && eventDates.get(backwardTime ? 1 : 0).getMonth().contentEquals(value)) {


                    return backwardTime ?
                            Integer.parseInt(eventDates.get(1).getMonth()) < Integer.parseInt(value) :
                            Integer.parseInt(eventDates.get(0).getMonth()) > Integer.parseInt(value);


                }

                return false;

        }

        return false;
    }

    public void runANewRequest(Request gsonRequest) {

        // Run a new request into the RequestQueue.

        MySingleton.getInstance(getContext()).addToRequestQueue(gsonRequest);

    }

    class EventDate {

        private String day;
        private String month;
        private String year;
        private String mBCAD;

        EventDate(String mBCAD, String year, String month, String day) {

            this.mBCAD = mBCAD;
            this.year = year;
            this.month = month;
            this.day = day;

        }

        String getDay() {
            return day;
        }

        String getMonth() {
            return month;
        }

        String getYear() {
            return year;
        }

        String getmBCAD() {
            return mBCAD;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public void setmBCAD(String mBCAD) {
            this.mBCAD = mBCAD;
        }
    }
}
