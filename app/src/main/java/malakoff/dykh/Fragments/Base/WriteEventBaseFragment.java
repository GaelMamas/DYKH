package malakoff.dykh.Fragments.Base;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
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

import malakoff.dykh.AppApplication.Constants;
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

    protected String selectedTheme, selectedTodayLocation, selectedBCAD, inputYear;
    protected int monthIndex = 0;
    protected List<EventDate> eventDates = new ArrayList<>();

    protected BetterSpinner themeSpinner, todayLocationSpinner, mBCADSpinner, mDatedTypeEventSpinner, monthSpinner, daySpinner;
    protected AppCompatEditText titleEditText, historicLocationEditText, storyEditText, yearEditText;
    protected ImageView mCancelEventDateButton;
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

        mCancelEventDateButton = view.findViewById(R.id.button_event_cancel_date);

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
        monthsList.add(getString(R.string.event_creation_unknown_item_date));
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

        mCancelEventDateButton.setOnClickListener(this);
        publishButton.setOnClickListener(this);

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
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {
            case R.id.button_event_cancel_date:

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle);

                builder.setTitle(R.string.event_creation_cancel_warning_title)
                        .setMessage(R.string.event_creation_cancel_warning_msg)
                        .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                mDatedTypeEventSpinner.resetPlaceHolderText();
                                mBCADSpinner.resetPlaceHolderText();
                                yearEditText.setText("");

                                monthSpinner.resetPlaceHolderText();
                                daySpinner.resetPlaceHolderText();

                                mBCADSpinner.setVisibility(View.GONE);
                                dateSetterLayout.setVisibility(View.GONE);
                                datePicker.setVisibility(View.GONE);

                                eventDates.clear();

                                mCancelEventDateButton.setVisibility(View.INVISIBLE);

                            }
                        })
                        .setNegativeButton(R.string.no_button, null)
                        .create().show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch ((int) id) {

            case R.id.spinner_event_theme:

                selectedTheme = (String) parent.getItemAtPosition(position);

                break;

            case R.id.spinner_event_today_location:


                selectedTodayLocation = (String) parent.getItemAtPosition(position);

                if (!TextUtils.isEmpty(selectedTodayLocation))
                    Toast.makeText(getContext(), "Set precisely the location", Toast.LENGTH_SHORT).show();

                break;


            case R.id.spinner_event_single_date:


                mDatedTypeEventSpinner.setError(null);


                if (position == 0) {

                    if (eventDates.isEmpty()) {


                        mBCADSpinner.setVisibility(View.VISIBLE);


                    } else if (eventDates.size() > 0) {


                        playbackAnEventDate(0);


                    }


                } else {


                    if (eventDates.isEmpty()) {

                        mDatedTypeEventSpinner.setError("Define first the the first date");
                        mDatedTypeEventSpinner.resetPlaceHolderText();
                        mBCADSpinner.setVisibility(View.GONE);
                        dateSetterLayout.setVisibility(View.GONE);
                        datePicker.setVisibility(View.GONE);

                    } else if (eventDates.size() > 1) {


                        playbackAnEventDate(1);


                    } else {

                        mBCADSpinner.resetPlaceHolderText();
                        mBCADSpinner.setVisibility(View.VISIBLE);
                        dateSetterLayout.setVisibility(View.GONE);
                        datePicker.setVisibility(View.GONE);


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

                        selectedBCAD = (String) parent.getItemAtPosition(position);
                        setLayoutsAfterBCAD(position);

                    }

                } else if (mDatedTypeEventSpinner.getSelectedIndex() == 1) {


                    checkBCADInputs((String) parent.getItemAtPosition(position), position, false);


                } else if (eventDates.size() > 1) {


                    checkBCADInputs((String) parent.getItemAtPosition(position), position, true);


                } else if (eventDates.size() == 1) {//Kidding back and forth with the inputs


                    eventDates.get(0).setmBCAD(selectedBCAD = (String) parent.getItemAtPosition(position));


                }


                break;


            case R.id.spinner_event_month_setter:


                monthSpinner.setError(null);


                monthIndex = position;


                if (setMonthDays() != null) {

                    String month = (String) parent.getItemAtPosition(position);

                    switch (month) {
                        case Constants.DYKH_ITEM_DATE_UNKNOWN:


                            if (eventDates.isEmpty()) {


                                addAnEventDate(selectedBCAD, inputYear, null, null);


                            } else if (mDatedTypeEventSpinner.getSelectedIndex() == 1) {

                                if (eventDates.size() == 2) {

                                    eventDates.set(1, new EventDate(selectedBCAD, inputYear, null, null));

                                } else {

                                    addAnEventDate(selectedBCAD, inputYear, null, null);

                                }

                            } else {

                                eventDates.set(0, new EventDate(selectedBCAD, inputYear, null, null));


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
                    case Constants.DYKH_ITEM_DATE_UNKNOWN:

                        if (eventDates.isEmpty()) {

                            addAnEventDate(selectedBCAD, inputYear, String.valueOf(monthIndex), null);


                        } else if (mDatedTypeEventSpinner.getSelectedIndex() == 1) {

                            if (eventDates.size() == 2) {

                                eventDates.set(1, new EventDate(selectedBCAD, inputYear, String.valueOf(monthIndex), null));

                            } else {

                                addAnEventDate(selectedBCAD, inputYear, String.valueOf(monthIndex), null);

                            }

                        } else if (eventDates.size() > 1) {

                            if (areNot2DatesChronological(DAY_STEP, day, true)) {

                                daySpinner.setError(getString(R.string.event_chronological_alert_message));

                            } else {

                                eventDates.set(0, new EventDate(selectedBCAD, inputYear, String.valueOf(monthIndex), null));

                            }


                        }


                    default:

                        if (eventDates.isEmpty()) {

                            addAnEventDate(selectedBCAD, inputYear, String.valueOf(monthIndex), String.valueOf(position));

                        } else if (mDatedTypeEventSpinner.getSelectedIndex() == 1) {

                            if (eventDates.size() == 2) {

                                eventDates.set(1, new EventDate(selectedBCAD, inputYear, String.valueOf(monthIndex), day));

                            } else {

                                addAnEventDate(selectedBCAD, inputYear, String.valueOf(monthIndex), day);

                            }

                        } else if (eventDates.size() > 1) {

                            if (areNot2DatesChronological(DAY_STEP, day, true)) {

                                daySpinner.setError(getString(R.string.event_chronological_alert_message));

                            } else {

                                eventDates.set(0, new EventDate(selectedBCAD, inputYear, String.valueOf(monthIndex), day));

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


                if (TextUtils.isEmpty(v.getText().toString())
                        && v.getText().toString().matches("[-+]?\\d*\\.?\\d+")) {

                    yearEditText.setError(getString(R.string.event_correct_date_reminder_message));
                    return false;

                }


                if (eventDates.isEmpty()) {


                    setYearInput(Integer.parseInt(inputYear = v.getText().toString()), 0, 1);


                } else if (mDatedTypeEventSpinner.getSelectedIndex() == 1) {


                    if (areNot2DatesChronological(YEAR_STEP, v.getText().toString(), false)) {

                        yearEditText.setError(getString(R.string.event_chronological_alert_message));
                        return false;

                    } else if (eventDates.size() == 1) {

                        EventDate eventDate = eventDates.get(0);

                        setYearInput(Integer.parseInt(inputYear = v.getText().toString()),
                                UsefulGenericMethods.isANumeric(eventDate.getMonth()) ? Integer.parseInt(eventDate.getMonth()) : 0,
                                UsefulGenericMethods.isANumeric(eventDate.getDay()) ? Integer.parseInt(eventDate.getDay()) : 1);


                    } else {

                        playbackYear(1);

                    }


                } else {

                    playbackYear(0);

                }


        }


        UsefulGenericMethods.hideKeyboard(getContext(), yearEditText);

        return true;
    }

    private List<String> setMonthDays() {

        int number = setMonthDaysNumber();

        if (number == 0) return null;

        List<String> monthDay = new ArrayList<>();

        monthDay.add(getString(R.string.event_creation_unknown_item_date));


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


    protected String makeUpEventDateFormat() {

        // beginning year BC/AD, {start date BC/AD - end date BC/AD}


        switch (eventDates.size()) {
            case 2:

                StringBuilder finalEventDate = makeUpFirstPartEventDate(eventDates.get(0));


                if (finalEventDate.toString().contentEquals(makeUpFirstPartEventDate(eventDates.get(1)))) {

                    finalEventDate = makeUpFirstPartEventDate(eventDates.get(0))
                            .append("}");


                    Log.i("DYKH Date Format", "Same Date " + finalEventDate.toString());

                } else {

                    finalEventDate.append(" - ")
                            .append(eventDates.get(0).getYear())
                            .append(!TextUtils.isEmpty(eventDates.get(0).getMonth()) ? eventDates.get(0).getMonth() : "")
                            .append(!TextUtils.isEmpty(eventDates.get(0).getDay()) ? eventDates.get(0).getDay() : "")
                            .append(eventDates.get(0).getmBCAD())
                            .append("}");
                }

                return finalEventDate.toString();
            case 1:

                finalEventDate = makeUpFirstPartEventDate(eventDates.get(0))
                        .append("}");


                Log.i("DYKH Date Format", finalEventDate.toString());

                return finalEventDate.toString();
            default:
                return null;

        }

    }

    private StringBuilder makeUpFirstPartEventDate(EventDate eventDate) {

        return new StringBuilder().append(eventDate.getYear())
                .append(eventDate.getmBCAD())
                .append(", {")
                .append(eventDate.getYear())
                .append(!TextUtils.isEmpty(eventDate.getMonth()) ? eventDate.getMonth() : "")
                .append(!TextUtils.isEmpty(eventDate.getDay()) ? eventDate.getDay() : "")
                .append(eventDate.getmBCAD());

    }


    private void checkBCADInputs(String value, int position, boolean backwardTime) {

        if (areNot2DatesChronological(BCAD_STEP, value, backwardTime)) {


            mBCADSpinner.setError(getString(R.string.event_chronological_alert_message));


        } else {

            selectedBCAD = value;
            setLayoutsAfterBCAD(position);

        }

    }


    private void setLayoutsAfterBCAD(int position) {

        switch (position) {

            case 1:

                if (eventDates.size() == 1) {

                    datePicker.setVisibility(View.GONE);
                    dateSetterLayout.setVisibility(View.VISIBLE);
                    monthSpinner.setVisibility(View.GONE);
                    daySpinner.setVisibility(View.GONE);

                    yearEditText.setText("");
                    monthSpinner.resetPlaceHolderText();
                    daySpinner.resetPlaceHolderText();

                } else if (eventDates.size() > 1) {

                    playbackYear(1);


                }

            case 0:
            default:

                if (eventDates.size() > 0 && position == 0
                        && mDatedTypeEventSpinner.getSelectedIndex() == 0) {

                    playbackYear(0);

                } else {

                    datePicker.setVisibility(View.GONE);
                    dateSetterLayout.setVisibility(View.VISIBLE);
                    monthSpinner.setVisibility(View.GONE);
                    daySpinner.setVisibility(View.GONE);

                    yearEditText.setText("");
                    monthSpinner.resetPlaceHolderText();
                    daySpinner.resetPlaceHolderText();

                }

        }

    }

    private void playbackYear(int position) {

        EventDate eventDate = eventDates.get(position);
        String year = eventDate.getYear();

        if (TextUtils.isEmpty(year)
                && !year.matches("[-+]?\\d*\\.?\\d+")) return;


        int yearNumber = Integer.parseInt(year);


        if (getResources().getStringArray(R.array.event_bc_or_ad)[1]
                .contentEquals(selectedBCAD)
                && yearNumber >= 1900) {

            datePicker.setVisibility(View.VISIBLE);
            dateSetterLayout.setVisibility(View.GONE);

            setEventDatePicker(yearNumber,
                    eventDate.getMonth() == null ? 0 :
                            Integer.parseInt(eventDate.getMonth()),
                    eventDate.getDay() == null ? 1 :
                            Integer.parseInt(eventDate.getDay()));

        } else {

            datePicker.setVisibility(View.GONE);
            dateSetterLayout.setVisibility(View.VISIBLE);

            yearEditText.setText(year);


            monthSpinner.setVisibility(View.VISIBLE);
            daySpinner.setVisibility(View.VISIBLE);

            monthSpinner.setSelection(0);
            daySpinner.setSelection(0);

            if (!TextUtils.isEmpty(eventDate.getMonth())) {

                String month = eventDate.getMonth();

                monthIndex = month.matches("[-+]?\\d*\\.?\\d+") ?
                        Integer.parseInt(month) : 0;


                monthSpinner.setAdapter(new ArrayAdapter<>(getContext(),
                        R.layout.cell_text_dark,
                        setMonthDays()));

                monthSpinner.setSelection(monthIndex);


                String day = eventDates.get(position).getDay();

                if (monthIndex > 0 && !TextUtils.isEmpty(day)) {

                    daySpinner.setAdapter(new ArrayAdapter<>(getContext(),
                            R.layout.cell_text_dark,
                            setMonthDays()));


                    daySpinner.setSelection(day.matches("[-+]?\\d*\\.?\\d+") ?
                            Integer.parseInt(day) : 0);

                }

            }

        }

    }


    private boolean areNot2DatesChronological(String step, String value, boolean backwardTime) {

        switch (step) {

            case BCAD_STEP:

                if (backwardTime) {

                    return getResources().getStringArray(R.array.event_bc_or_ad)[0]
                            .contentEquals(eventDates.get(1).getmBCAD())

                            && getResources().getStringArray(R.array.event_bc_or_ad)[1]
                            .contentEquals(value);

                } else {

                    return getResources().getStringArray(R.array.event_bc_or_ad)[1]
                            .contentEquals(eventDates.get(0).getmBCAD())

                            && getResources().getStringArray(R.array.event_bc_or_ad)[0]
                            .contentEquals(value);

                }


            case YEAR_STEP:

                if (eventDates.get(backwardTime ? 1 : 0).getmBCAD().contentEquals(selectedBCAD)) {

                    if (getResources().getStringArray(R.array.event_bc_or_ad)[0]
                            .contentEquals(eventDates.get(0).getmBCAD())) {


                        return backwardTime ?
                                Integer.parseInt(value) < Integer.parseInt(eventDates.get(1).getYear()) :
                                Integer.parseInt(value) > Integer.parseInt(eventDates.get(0).getYear());

                    } else if (getResources().getStringArray(R.array.event_bc_or_ad)[1]
                            .contentEquals(eventDates.get(0).getmBCAD())) {

                        return backwardTime ?
                                Integer.parseInt(value) > Integer.parseInt(eventDates.get(1).getYear()) :
                                Integer.parseInt(value) < Integer.parseInt(eventDates.get(0).getYear());

                    }

                }


            case MONTH_STEP:

                if (!TextUtils.isEmpty(value)
                        && eventDates.get(backwardTime ? 1 : 0).getYear().contentEquals(value)) {


                    return backwardTime ?
                            Integer.parseInt(eventDates.get(1).getMonth()) < Integer.parseInt(value) :
                            Integer.parseInt(eventDates.get(0).getMonth()) > Integer.parseInt(value);


                }


            case DAY_STEP:

                if (!TextUtils.isEmpty(value)
                        && eventDates.get(backwardTime ? 1 : 0).getYear().contentEquals(value)
                        && eventDates.get(backwardTime ? 1 : 0).getMonth().contentEquals(value)) {


                    return backwardTime ?
                            Integer.parseInt(eventDates.get(1).getMonth()) < Integer.parseInt(value) :
                            Integer.parseInt(eventDates.get(0).getMonth()) > Integer.parseInt(value);


                }


        }

        return false;
    }

    private void playbackAnEventDate(int position) {


        String mDateBCAD = eventDates.get(position).getmBCAD();
        String mDateYear = eventDates.get(position).getYear();


        mBCADSpinner.setVisibility(View.VISIBLE);

        if (TextUtils.isEmpty(mDateBCAD)) {

            mBCADSpinner.resetPlaceHolderText();
            return;

        }


        boolean isBCorAD = mDateBCAD
                .contentEquals(getResources().getStringArray(R.array.event_bc_or_ad)[0]);

        try {


            mBCADSpinner.setSelection(isBCorAD ? 0 : 1);


        } catch (IndexOutOfBoundsException e) {

            mBCADSpinner.setAdapter(new ArrayAdapter<>(getContext(),
                    R.layout.cell_text_dark,
                    Arrays.asList(getResources().getStringArray(R.array.event_bc_or_ad))));

            mBCADSpinner.setSelection(isBCorAD ? 0 : 1);

        }


        if (!TextUtils.isEmpty(mDateYear)) {

            playbackYear(position);

        } else {

            dateSetterLayout.setVisibility(View.VISIBLE);

        }


    }


    private void setYearInput(int year, int monthOfYear, int dayOfMonth) {


        if (selectedBCAD.contentEquals(getResources()
                .getStringArray(R.array.event_bc_or_ad)[0])) {


            monthSpinner.setVisibility(View.VISIBLE);


        } else if (selectedBCAD.contentEquals(getResources()
                .getStringArray(R.array.event_bc_or_ad)[1])) {


            if (year >= 1900 && year <= Calendar.getInstance().get(Calendar.YEAR)) {


                dateSetterLayout.setVisibility(View.GONE);
                datePicker.setVisibility(View.VISIBLE);


                setEventDatePicker(year, monthOfYear, dayOfMonth);


            } else if (year < 1900) {


                monthSpinner.setVisibility(View.VISIBLE);


            }

        }


        UsefulGenericMethods.hideKeyboard(getContext(), yearEditText);


    }

    private void setEventDatePicker(int year, int monthOfYear, int dayOfMonth) {

        saveEventYearFromDatePicker(year, monthOfYear, dayOfMonth);

        datePicker.init(year, monthOfYear, dayOfMonth == 0 ? 1 : dayOfMonth, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


                if ((year < Calendar.getInstance().get(Calendar.YEAR)) ||
                        (monthOfYear <= Calendar.getInstance().get(Calendar.MONTH)
                                && dayOfMonth <= Calendar.getInstance().get(Calendar.DAY_OF_MONTH))) {


                    saveEventYearFromDatePicker(year, monthOfYear, dayOfMonth);


                } else {


                    Toast.makeText(getContext(),
                            R.string.event_correct_date_reminder_message,
                            Toast.LENGTH_SHORT).show();


                }
            }
        });


    }

    private void saveEventYearFromDatePicker(int year, int monthOfYear, int dayOfMonth) {

        if (mDatedTypeEventSpinner.getSelectedIndex() == 1) {


            switch (eventDates.size()) {

                case 2:

                    eventDates.set(1, new EventDate(selectedBCAD,
                            String.valueOf(year),
                            String.valueOf(monthOfYear),
                            String.valueOf(dayOfMonth)));

                case 1:

                    addAnEventDate(selectedBCAD,
                            String.valueOf(year),
                            String.valueOf(monthOfYear),
                            String.valueOf(dayOfMonth));


            }

        } else {

            switch (eventDates.size()) {

                case 2:
                case 1:

                    eventDates.set(0, new EventDate(selectedBCAD,
                            String.valueOf(year),
                            String.valueOf(monthOfYear),
                            String.valueOf(dayOfMonth)));

                case 0:

                    addAnEventDate(selectedBCAD,
                            String.valueOf(year),
                            String.valueOf(monthOfYear),
                            String.valueOf(dayOfMonth));

            }

        }

    }


    private void addAnEventDate(String selectedBCAD, String inputYear, String month, String day) {

        eventDates.add(new EventDate(selectedBCAD, inputYear, month, day));

        if (mCancelEventDateButton.getVisibility() == View.INVISIBLE) {
            mCancelEventDateButton.setVisibility(View.VISIBLE);
        }

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
