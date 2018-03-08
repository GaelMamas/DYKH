package malakoff.dykh.DesignWidget.ModelBase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import malakoff.dykh.DesignWidget.BetterSpinner;
import malakoff.dykh.ModelBase.Base.EventDate;
import malakoff.dykh.R;
import malakoff.dykh.Utils.UsefulGenericMethods;

/**
 * Created by gael on 16/02/2018.
 */

public class DateRecorderView extends CardView implements AdapterView.OnItemSelectedListener, TextView.OnEditorActionListener {

    private final static String BCAD_STEP = "bc ad", YEAR_STEP = "year", MONTH_STEP = "month", DAY_STEP = "day";

    private View rootview;
    private TextView headerTitleTextView;
    private BetterSpinner mBCADSpinner, monthSpinner, daySpinner;
    private AppCompatEditText yearEditText;
    private View dateSetterLayout, switchLayout;
    private DatePicker datePicker;
    private SwitchCompat switchCompat;


    private int monthIndex;
    private String selectedBCAD, inputYear;

    private boolean doesSwitchHaveToAppear = true;


    private EventDateRecordable dateRecordable;

    private EventDate mRecordedEventDate, relatedEventDate;

    public DateRecorderView(@NonNull Context context) {
        this(context, null);
    }

    public DateRecorderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DateRecorderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        rootview = inflate(getContext(), R.layout.layout_event_date_declaration, this);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        init();


        super.onLayout(changed, left, top, right, bottom);
    }


    private void init() {

        headerTitleTextView = rootview.findViewById(R.id.text_event_date_header_title);

        mBCADSpinner = rootview.findViewById(R.id.spinner_event_bc_or_ad);
        yearEditText = rootview.findViewById(R.id.edittext_event_year_setter);

        monthSpinner = rootview.findViewById(R.id.spinner_event_month_setter);
        daySpinner = rootview.findViewById(R.id.spinner_event_day_setter);

        dateSetterLayout = rootview.findViewById(R.id.event_date_setter_layout);

        datePicker = rootview.findViewById(R.id.datePicker);

        switchLayout = rootview.findViewById(R.id.layout_event_date_switch);

        switchCompat = rootview.findViewById(R.id.switch_event_ending_date);


        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (dateRecordable != null) {

                    dateRecordable.onSwitch(b, mRecordedEventDate);

                }
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.cell_text_dark,
                Arrays.asList(getResources().getStringArray(R.array.event_bc_or_ad)));

        adapter.setDropDownViewResource(R.layout.cell_text_darker);

        mBCADSpinner.setAdapter(adapter);

        mBCADSpinner.setOnItemSelectedListener(this);


        List<String> monthsList = new ArrayList<>();
        monthsList.add(getResources().getString(R.string.event_creation_unknown_item_date));
        monthsList.addAll(Arrays.asList(DateFormatSymbols.getInstance(Locale.getDefault()).getMonths()));
        ArrayAdapter<String> mMonthsAdapter = new ArrayAdapter<>(getContext(),
                R.layout.cell_text_dark,
                monthsList);


        ArrayAdapter<String> mDaysAdapter = new ArrayAdapter<>(getContext(),
                R.layout.cell_text_dark,
                setMonthDays());


        yearEditText.setOnEditorActionListener(this);

        monthSpinner.setAdapter(mMonthsAdapter);
        monthSpinner.setOnItemSelectedListener(this);

        daySpinner.setAdapter(mDaysAdapter);
        daySpinner.setOnItemSelectedListener(this);


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long viewId) {
        switch ((int) viewId) {
            case R.id.spinner_event_bc_or_ad:

                mBCADSpinner.setError(null);

                if (areNot2DatesChronological(BCAD_STEP, (String) adapterView.getItemAtPosition(position))) {

                    mBCADSpinner.setError(getResources().getString(R.string.event_chronological_alert_message));
                    hideSubsequentComponents();


                } else {

                    selectedBCAD = (String) adapterView.getItemAtPosition(position);

                    hideSubsequentComponents();

                }


                updateTwin(null);

                break;

            case R.id.spinner_event_month_setter:

                monthSpinner.setError(null);

                if (areNot2DatesChronological(MONTH_STEP, String.valueOf(position))) {

                    monthSpinner.setError(getResources().getString(R.string.event_chronological_alert_message));

                    manageDisplayDayComponent(false);

                    updateTwin(null);

                } else {

                    manageDisplayDayComponent(true);

                    monthIndex = position;

                    Toast.makeText(getContext(), "Month " + position, Toast.LENGTH_SHORT).show();

                    if (position == 0) {

                        recordThisDate(inputYear, String.valueOf(monthIndex), null);

                    } else {

                        updateTwin(null);

                    }

                }


                break;


            case R.id.spinner_event_day_setter:

                daySpinner.setError(null);

                if (areNot2DatesChronological(DAY_STEP, String.valueOf(position))) {

                    daySpinner.setError(getResources().getString(R.string.event_chronological_alert_message));

                    updateTwin(null);

                } else {

                    Toast.makeText(getContext(), "Day " + position, Toast.LENGTH_SHORT).show();


                    recordThisDate(inputYear, String.valueOf(monthIndex), String.valueOf(position));


                }


                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onEditorAction(TextView v, int i, KeyEvent keyEvent) {

        switch (v.getId()) {
            case R.id.edittext_event_year_setter:

                yearEditText.setError(null);


                if (TextUtils.isEmpty(v.getText().toString())
                        && v.getText().toString().matches("[-+]?\\d*\\.?\\d+")) {

                    yearEditText.setError(getResources().getString(R.string.event_correct_date_reminder_message));
                    return false;

                }

                if (relatedEventDate == null
                        || !areNot2DatesChronological(YEAR_STEP, v.getText().toString())) {

                    setYearInput(Integer.parseInt(inputYear = v.getText().toString()), 0, 1);


                } else {

                    yearEditText.setError(getResources().getString(R.string.event_chronological_alert_message));

                    hideMonthDayComponents();

                    updateTwin(null);
                }


        }


        UsefulGenericMethods.hideKeyboard(getContext(), yearEditText);

        return true;
    }


    private boolean areNot2DatesChronological(String step, String value) {

        if (relatedEventDate == null) return false;

        switch (step) {

            case BCAD_STEP:


                return getResources().getStringArray(R.array.event_bc_or_ad)[1]
                        .contentEquals(relatedEventDate.getmBCAD())

                        && getResources().getStringArray(R.array.event_bc_or_ad)[0]
                        .contentEquals(value);


            case YEAR_STEP:

                if (TextUtils.isEmpty(relatedEventDate.getYear())) return false;

                if (relatedEventDate.getmBCAD().contentEquals(selectedBCAD)) {

                    if (getResources().getStringArray(R.array.event_bc_or_ad)[0]
                            .contentEquals(relatedEventDate.getmBCAD())) {


                        return Integer.parseInt(value) > Integer.parseInt(relatedEventDate.getYear());

                    } else if (getResources().getStringArray(R.array.event_bc_or_ad)[1]
                            .contentEquals(relatedEventDate.getmBCAD())) {

                        return Integer.parseInt(value) < Integer.parseInt(relatedEventDate.getYear());

                    }

                }


            case MONTH_STEP:

                if (TextUtils.isEmpty(relatedEventDate.getMonth())) return false;

                if (!TextUtils.isEmpty(value)
                        && relatedEventDate.getYear().contentEquals(inputYear)) {


                    return Integer.parseInt(relatedEventDate.getMonth()) > Integer.parseInt(value);


                }


            case DAY_STEP:

                if (TextUtils.isEmpty(relatedEventDate.getDay())) return false;

                if (!TextUtils.isEmpty(value)
                        && relatedEventDate.getYear().contentEquals(inputYear)
                        && relatedEventDate.getMonth().contentEquals(String.valueOf(monthIndex))) {


                    return Integer.parseInt(relatedEventDate.getDay()) > Integer.parseInt(value);


                }


        }

        return false;
    }


    private void setYearInput(int year, int monthOfYear, int dayOfMonth) {


        if (selectedBCAD.contentEquals(getResources()
                .getStringArray(R.array.event_bc_or_ad)[0])) {


            dateSetterLayout.setVisibility(VISIBLE);
            manageDisplayMonthComponent(true);

            daySpinner.setVisibility(INVISIBLE);
            datePicker.setVisibility(View.GONE);

            updateTwin(null);


        } else if (getResources().getStringArray(R.array.event_bc_or_ad)[1]
                .contentEquals(selectedBCAD)) {


            if (year >= 1900 && year <= Calendar.getInstance().get(Calendar.YEAR)) {


                dateSetterLayout.setVisibility(View.GONE);
                datePicker.setVisibility(View.VISIBLE);


                setEventDatePicker(year, monthOfYear, dayOfMonth, true);


            } else if (year < 1900) {

                dateSetterLayout.setVisibility(VISIBLE);
                manageDisplayMonthComponent(true);

                daySpinner.setVisibility(INVISIBLE);
                datePicker.setVisibility(View.GONE);

                updateTwin(null);


            } else {

                yearEditText.setError(getResources().getText(R.string.event_creation_in_futur_year_error));

                updateTwin(null);

            }

        }


        UsefulGenericMethods.hideKeyboard(getContext(), yearEditText);


    }


    private void setEventDatePicker(int year, int monthOfYear, int dayOfMonth, boolean isForSavingPurpose) {

        int reasonableYear = Math.min(year, Calendar.getInstance().get(Calendar.YEAR));

        int reasonableMonthOfYear = reasonableYear < Calendar.getInstance().get(Calendar.YEAR) ?
                monthOfYear : Math.min(monthOfYear, Calendar.getInstance().get(Calendar.MONTH));

        int reasonableDayOfMonth = (reasonableYear < Calendar.getInstance().get(Calendar.YEAR)) ?
                dayOfMonth : reasonableMonthOfYear < Calendar.getInstance().get(Calendar.MONTH) ?
                dayOfMonth : Math.min(dayOfMonth, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        if (isForSavingPurpose) {
            recordThisDate(reasonableYear, reasonableMonthOfYear, reasonableDayOfMonth);
        }


        datePicker.init(reasonableYear, reasonableMonthOfYear, reasonableDayOfMonth == 0 ? 1 : reasonableDayOfMonth, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


                if (UsefulGenericMethods.isTheDateReasonable(year, monthOfYear, dayOfMonth)) {

                    recordThisDate(year, monthOfYear, dayOfMonth);

                    if (dateRecordable != null) {

                        dateRecordable.isCompleteDateAvailable(mRecordedEventDate);

                    }


                } else if (monthOfYear > Calendar.getInstance().get(Calendar.MONTH)) {

                    Toast.makeText(getContext(), R.string.event_creation_in_futur_month_error, Toast.LENGTH_SHORT).show();

                } else if (dayOfMonth > Calendar.getInstance().get(Calendar.MONTH)) {

                    Toast.makeText(getContext(), R.string.event_creation_in_futur_day_error, Toast.LENGTH_SHORT).show();

                } else {


                    Toast.makeText(getContext(),
                            R.string.event_correct_date_reminder_message,
                            Toast.LENGTH_SHORT).show();


                }
            }
        });


    }


    public void updateTwin(EventDate eventDate) {

        if (relatedEventDate == null && dateRecordable != null) {

            switchCompat.setChecked(false);
            dateRecordable.isCompleteDateAvailable(eventDate);

        } else if (dateRecordable != null) {

            dateRecordable.isCompleteDateAvailable(eventDate);

        }

    }

    private void hideSubsequentComponents() {

        yearEditText.setText("");

        hideMonthDayComponents();

        dateSetterLayout.setVisibility(VISIBLE);
        datePicker.setVisibility(GONE);
        switchLayout.setVisibility(GONE);

    }

    private void hideMonthDayComponents() {

        manageDisplayMonthComponent(false);
        manageDisplayDayComponent(false);

    }

    private void manageDisplayMonthComponent(boolean wantToShow) {

        if (wantToShow) {

            monthSpinner.setVisibility(VISIBLE);
            monthSpinner.resetPlaceHolderText();
            monthSpinner.setError(null);

        } else {

            monthSpinner.setVisibility(INVISIBLE);

        }

        monthSpinner.resetPlaceHolderText();

    }

    private void manageDisplayDayComponent(boolean wantToShow) {

        if (wantToShow) {

            daySpinner.setVisibility(VISIBLE);
            daySpinner.resetPlaceHolderText();
            daySpinner.setError(null);

        } else {

            daySpinner.setVisibility(INVISIBLE);

        }

        daySpinner.resetPlaceHolderText();
    }


    private void recordThisDate(int year, int monthOfYear, int dayOfMonth) {

        recordThisDate(String.valueOf(year),
                String.valueOf(monthOfYear),
                String.valueOf(dayOfMonth));

    }

    private void recordThisDate(String year, String monthOfYear, String dayOfMonth) {

        if (mRecordedEventDate == null) {

            mRecordedEventDate = new EventDate(selectedBCAD,
                    year, monthOfYear, dayOfMonth);

        } else {

            mRecordedEventDate.setYear(year);
            mRecordedEventDate.setMonth(monthOfYear);
            mRecordedEventDate.setDay(dayOfMonth);

        }


        updateTwin(mRecordedEventDate);

        Toast.makeText(getContext(), "Year " + year + " Month " + monthOfYear + " Day " + dayOfMonth, Toast.LENGTH_SHORT).show();

        switchLayout.setVisibility(doesSwitchHaveToAppear ? VISIBLE : GONE);


    }


    public EventDate getmRecordedEventDate() {
        return mRecordedEventDate;
    }

    public TextView getHeaderTitleTextView() {
        return headerTitleTextView;
    }

    private List<String> setMonthDays() {

        int number = setMonthDaysNumber();

        List<String> monthDay = new ArrayList<>();

        monthDay.add(getResources().getString(R.string.event_creation_unknown_item_date));

        if (number == 0) return monthDay;


        for (int i = 0; i < number; i++) {

            monthDay.add(String.valueOf(i + 1));

        }


        return monthDay;
    }

    private int setMonthDaysNumber() {
        switch (monthIndex) {
            case 2:

                return 29;
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:

                return 31;

            case 4:
            case 6:
            case 9:
            case 11:

                return 30;

            case 0:
            default:
                return 0;
        }
    }

    public void setTwinDefaultValues(EventDate eventDate) {

        if (eventDate == null
                || TextUtils.isEmpty(eventDate.getmBCAD())) return;

        this.doesSwitchHaveToAppear = false;

        init();


        headerTitleTextView.setText(R.string.event_time_block_title_2);

        if (TextUtils.isEmpty(eventDate.getmBCAD())) {

            mBCADSpinner.resetPlaceHolderText();

            if (TextUtils.isEmpty(eventDate.getYear())
                    && !eventDate.getYear().matches("[-+]?\\d*\\.?\\d+")) return;

            yearEditText.setText(inputYear = eventDate.getYear());

            dateSetterLayout.setVisibility(VISIBLE);
            yearEditText.setVisibility(VISIBLE);

        } else {

            mBCADSpinner.setSelection(eventDate.getmBCAD().contentEquals(getResources()
                    .getStringArray(R.array.event_bc_or_ad)[0]) ? 0 : 1);

            selectedBCAD = eventDate.getmBCAD();

            relatedEventDate = eventDate;

            if (TextUtils.isEmpty(eventDate.getYear())
                    && !eventDate.getYear().matches("[-+]?\\d*\\.?\\d+")) return;


            yearEditText.setText(inputYear = eventDate.getYear());

            monthSpinner.resetPlaceHolderText();

            setYearInput(Integer.parseInt(inputYear),
                    TextUtils.isEmpty(eventDate.getMonth()) ? 0 : Integer.parseInt(eventDate.getMonth()),
                    TextUtils.isEmpty(eventDate.getDay()) ? 0 : Integer.parseInt(eventDate.getDay()));
        }


    }


    public void fillEventDateBlock(EventDate source, boolean potentiallyEnableNextDateLayout, boolean hasNext) {

        if (source == null) return;

        init();

        headerTitleTextView.setText(potentiallyEnableNextDateLayout ?
                R.string.event_time_block_title :
                R.string.event_time_block_title_2);


        if (!TextUtils.isEmpty(source.getmBCAD())) {

            mBCADSpinner.setSelection(source.getmBCAD().contentEquals(getResources()
                    .getStringArray(R.array.event_bc_or_ad)[0]) ? 0 : 1);

            selectedBCAD = source.getmBCAD();


            if (!TextUtils.isEmpty(source.getYear())
                    && source.getYear().matches("[-+]?\\d*\\.?\\d+")) {

                int year = Integer.parseInt(source.getYear());

                if (year > 1899 && mBCADSpinner.getSelectedIndex() == 1) {

                    dateSetterLayout.setVisibility(View.GONE);
                    datePicker.setVisibility(View.VISIBLE);

                    setEventDatePicker(year,
                            TextUtils.isEmpty(source.getMonth()) ? 0 :
                                    Integer.parseInt(source.getMonth().matches("[-+]?\\d*\\.?\\d+") ? source.getMonth() : "0"),
                            TextUtils.isEmpty(source.getDay()) ? 0 :
                                    Integer.parseInt(source.getDay().matches("[-+]?\\d*\\.?\\d+") ? source.getDay() : "0"), false);

                } else {

                    dateSetterLayout.setVisibility(VISIBLE);
                    monthSpinner.setVisibility(VISIBLE);
                    daySpinner.setVisibility(VISIBLE);
                    datePicker.setVisibility(GONE);


                    inputYear = source.getYear();
                    yearEditText.setText(inputYear);


                    monthSpinner.setSelection(
                            (!TextUtils.isEmpty(source.getMonth())
                                    && source.getMonth().matches("[-+]?\\d*\\.?\\d+")) ?
                                    Integer.parseInt(source.getMonth()) : 0);

                    monthIndex = monthSpinner.getSelectedIndex();

                    daySpinner.setAdapter(new ArrayAdapter<>(getContext(),
                            R.layout.cell_text_dark,
                            setMonthDays()));

                    daySpinner.setSelection(
                            (!TextUtils.isEmpty(source.getDay())
                                    && source.getDay().matches("[-+]?\\d*\\.?\\d+")) ?
                                    Integer.parseInt(source.getDay()) : 0);
                }

            }

        }

        switchLayout.setVisibility(potentiallyEnableNextDateLayout ? VISIBLE : GONE);
        switchCompat.setChecked(hasNext);

    }


    public void setDateRecordable(EventDateRecordable dateRecordable) {
        this.dateRecordable = dateRecordable;
    }

    public interface EventDateRecordable {

        void onSwitch(boolean isOn, EventDate eventDate);

        void isCompleteDateAvailable(EventDate eventDate);
    }
}
