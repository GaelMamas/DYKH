package malakoff.dykh.DesignWidget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import malakoff.dykh.AppApplication.Constants;
import malakoff.dykh.ModelBase.Base.EventDate;
import malakoff.dykh.R;
import malakoff.dykh.Utils.UsefulGenericMethods;

/**
 * Created by gael on 14/02/2018.
 */

public class EventDateDeclarationCardView extends CardView implements View.OnClickListener, AdapterView.OnItemSelectedListener, TextView.OnEditorActionListener {

    private final static String BCAD_STEP = "bc ad", YEAR_STEP = "year", MONTH_STEP = "month", DAY_STEP = "day";

    protected String selectedBCAD, inputYear;
    protected int monthIndex = 0;
    protected EventDate eventDateInside, eventDateOutside;


    protected BetterSpinner mBCADSpinner, monthSpinner, daySpinner;
    protected AppCompatEditText yearEditText;
    protected ImageView mCancelEventDateButton;
    protected View dateSetterLayout;
    protected DatePicker datePicker;

    protected SwitchCompat switchCompat;

    private EventDateListener switchable;

    protected View rootView;

    public EventDateDeclarationCardView(@NonNull Context context) {
        this(context, null);
    }

    public EventDateDeclarationCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EventDateDeclarationCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        rootView = inflate(context, R.layout.layout_event_date_declaration, this);

        mBCADSpinner = rootView.findViewById(R.id.spinner_event_bc_or_ad);

        mCancelEventDateButton = rootView.findViewById(R.id.button_event_cancel_date);

        dateSetterLayout = rootView.findViewById(R.id.event_date_setter_layout);

        yearEditText = rootView.findViewById(R.id.edittext_event_year_setter);
        monthSpinner = rootView.findViewById(R.id.spinner_event_month_setter);
        daySpinner = rootView.findViewById(R.id.spinner_event_day_setter);

        datePicker = rootView.findViewById(R.id.datePicker);

        switchCompat = rootView.findViewById(R.id.switch_event_ending_date);
    }


    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        ArrayAdapter<String> mBCADAdapter = new ArrayAdapter<>(getContext(),
                R.layout.cell_text_dark,
                Arrays.asList(getResources().getStringArray(R.array.event_bc_or_ad)));

        mBCADAdapter.setDropDownViewResource(R.layout.cell_text_darker);


        List<String> monthsList = new ArrayList<>();
        monthsList.add(getResources().getString(R.string.event_creation_unknown_item_date));
        monthsList.addAll(Arrays.asList(DateFormatSymbols.getInstance(Locale.getDefault()).getMonths()));
        ArrayAdapter<String> mMonthsAdapter = new ArrayAdapter<>(getContext(),
                R.layout.cell_text_dark,
                monthsList);


        ArrayAdapter<String> mDaysAdapter = new ArrayAdapter<>(getContext(),
                R.layout.cell_text_dark,
                setMonthDays());

        mBCADSpinner.setAdapter(mBCADAdapter);
        mBCADSpinner.setOnItemSelectedListener(this);

        yearEditText.setOnEditorActionListener(this);

        monthSpinner.setAdapter(mMonthsAdapter);
        monthSpinner.setOnItemSelectedListener(this);

        daySpinner.setAdapter(mDaysAdapter);
        daySpinner.setOnItemSelectedListener(this);

        mCancelEventDateButton.setOnClickListener(this);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (switchable != null) {

                    switchable.onSwitch(b);

                }
            }
        });

        setValues(null);

        super.onLayout(changed, left, top, right, bottom);
    }


    public void setValues(EventDate outsideEventDate) {

        rootView.findViewById(R.id.layout_event_date_switch).setVisibility(outsideEventDate == null ? GONE : VISIBLE);

        if (outsideEventDate != null) {

            this.eventDateOutside = outsideEventDate;

            mBCADSpinner.setSelection(isBC(outsideEventDate.getmBCAD()) ? 0 : 1);

            ((TextView) rootView.findViewById(R.id.text_event_date_header_title)).setText(R.string.event_time_block_title_2);


        } else {

            mBCADSpinner.resetPlaceHolderText();
            dateSetterLayout.setVisibility(View.GONE);
            datePicker.setVisibility(View.GONE);

        }

    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        switch (v.getId()) {
            case R.id.edittext_event_year_setter:

                yearEditText.setError(null);


                if (TextUtils.isEmpty(v.getText().toString())
                        && v.getText().toString().matches("[-+]?\\d*\\.?\\d+")) {

                    yearEditText.setError(getResources().getString(R.string.event_correct_date_reminder_message));
                    return false;

                }


                if (eventDateOutside == null) {


                    setYearInput(Integer.parseInt(inputYear = v.getText().toString()), 0, 1);


                } else {


                    if (areNot2DatesChronological(YEAR_STEP, v.getText().toString(), false)) {

                        yearEditText.setError(getResources().getString(R.string.event_chronological_alert_message));
                        return false;

                    } else if (!TextUtils.isEmpty(eventDateOutside.getYear())) {


                        setYearInput(Integer.parseInt(inputYear = v.getText().toString()),
                                UsefulGenericMethods.isANumeric(eventDateOutside.getMonth()) ? Integer.parseInt(eventDateOutside.getMonth()) : 0,
                                UsefulGenericMethods.isANumeric(eventDateOutside.getDay()) ? Integer.parseInt(eventDateOutside.getDay()) : 1);


                    } else {

                        playbackYear(1);

                    }


                }


        }


        UsefulGenericMethods.hideKeyboard(getContext(), yearEditText);

        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch ((int) id) {


            case R.id.spinner_event_bc_or_ad:


                mBCADSpinner.setError(null);


                if (eventDateOutside == null) {

                    selectedBCAD = (String) parent.getItemAtPosition(position);
                    setLayoutsAfterBCAD(position);

                } else {

                    checkBCADInputs((String) parent.getItemAtPosition(position), position, false);

                }


                break;


            case R.id.spinner_event_month_setter:


                monthSpinner.setError(null);


                monthIndex = position;


                if (setMonthDays() != null) {

                    String month = (String) parent.getItemAtPosition(position);

                    switch (month) {
                        case Constants.DYKH_ITEM_DATE_UNKNOWN:


                            if (eventDateInside == null) {


                                addAnEventDate(selectedBCAD, inputYear, null, null);


                            } else {

                                eventDateInside = new EventDate(selectedBCAD, inputYear, null, null);

                            }


                            break;
                        default:

                            if (eventDateInside == null) {


                                daySpinner.setVisibility(View.VISIBLE);
                                daySpinner.setAdapter(new ArrayAdapter<>(getContext(),
                                        R.layout.cell_text_dark,
                                        setMonthDays()));


                            } else if (eventDateOutside == null) {


                                if (areNot2DatesChronological(MONTH_STEP, month, false)) {


                                    monthSpinner.setError(getResources().getString(R.string.event_chronological_alert_message));


                                } else {

                                    daySpinner.setVisibility(View.VISIBLE);
                                    daySpinner.setAdapter(new ArrayAdapter<>(getContext(),
                                            R.layout.cell_text_dark,
                                            setMonthDays()));

                                }


                            } else {


                                if (areNot2DatesChronological(MONTH_STEP, month, true)) {


                                    monthSpinner.setError(getResources().getString(R.string.event_chronological_alert_message));


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

                        if (eventDateInside == null) {

                            addAnEventDate(selectedBCAD, inputYear, String.valueOf(monthIndex), null);


                        } else if (eventDateOutside != null) {

                            eventDateOutside = new EventDate(selectedBCAD, inputYear, String.valueOf(monthIndex), null);


                        } else {

                            if (areNot2DatesChronological(DAY_STEP, day, true)) {

                                daySpinner.setError(getResources().getString(R.string.event_chronological_alert_message));

                            } else {

                                eventDateInside = new EventDate(selectedBCAD, inputYear, String.valueOf(monthIndex), null);

                            }


                        }


                    default:

                        if (eventDateInside == null) {

                            addAnEventDate(selectedBCAD, inputYear, String.valueOf(monthIndex), String.valueOf(position));

                        } else if (eventDateOutside == null) {

                            eventDateInside = new EventDate(selectedBCAD, inputYear, String.valueOf(monthIndex), day);

                        } else {

                            if (areNot2DatesChronological(DAY_STEP, day, true)) {

                                daySpinner.setError(getResources().getString(R.string.event_chronological_alert_message));

                            } else {

                                eventDateInside = new EventDate(selectedBCAD, inputYear, String.valueOf(monthIndex), day);

                            }


                        }
                }


                break;

        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void checkBCADInputs(String value, int position, boolean backwardTime) {

        if (areNot2DatesChronological(BCAD_STEP, value, backwardTime)) {


            mBCADSpinner.setError(getResources().getString(R.string.event_chronological_alert_message));


        } else {

            selectedBCAD = value;
            setLayoutsAfterBCAD(position);

        }

    }


    private void setLayoutsAfterBCAD(int position) {

        switch (position) {

            case 1:

                if (eventDateOutside == null) {

                    initDateLayout();

                } else if (eventDateInside != null) {

                    playbackYear(0);

                }

            case 0:
            default:

                if (eventDateOutside != null && position == 0
                        && eventDateInside != null) {

                    playbackYear(0);

                } else {

                    initDateLayout();

                }

        }

    }

    private void initDateLayout() {


        datePicker.setVisibility(View.GONE);
        dateSetterLayout.setVisibility(View.VISIBLE);
        monthSpinner.setVisibility(View.GONE);
        daySpinner.setVisibility(View.GONE);

        yearEditText.setText("");
        monthSpinner.resetPlaceHolderText();
        daySpinner.resetPlaceHolderText();


    }

    private void playbackYear(int position) {

        if (eventDateOutside == null) return;

        String year = eventDateOutside.getYear();

        if (TextUtils.isEmpty(year)
                && !year.matches("[-+]?\\d*\\.?\\d+")) return;


        int yearNumber = Integer.parseInt(year);


        if (getResources().getStringArray(R.array.event_bc_or_ad)[1]
                .contentEquals(selectedBCAD)
                && yearNumber >= 1900) {

            datePicker.setVisibility(View.VISIBLE);
            dateSetterLayout.setVisibility(View.GONE);

            setEventDatePicker(yearNumber,
                    eventDateOutside.getMonth() == null ? 0 :
                            Integer.parseInt(eventDateOutside.getMonth()),
                    eventDateOutside.getDay() == null ? 1 :
                            Integer.parseInt(eventDateOutside.getDay()));

        } else {

            datePicker.setVisibility(View.GONE);
            dateSetterLayout.setVisibility(View.VISIBLE);

            yearEditText.setText(year);


            monthSpinner.setVisibility(View.VISIBLE);
            daySpinner.setVisibility(View.VISIBLE);

            monthSpinner.setSelection(0);
            daySpinner.setSelection(0);

            if (!TextUtils.isEmpty(eventDateOutside.getMonth())) {

                String month = eventDateOutside.getMonth();

                monthIndex = month.matches("[-+]?\\d*\\.?\\d+") ?
                        Integer.parseInt(month) : 0;


                monthSpinner.setAdapter(new ArrayAdapter<>(getContext(),
                        R.layout.cell_text_dark,
                        setMonthDays()));

                monthSpinner.setSelection(monthIndex);


                String day = eventDateOutside.getDay();

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

        if (eventDateOutside == null) return false;

        switch (step) {

            case BCAD_STEP:

                if (backwardTime) {

                    return getResources().getStringArray(R.array.event_bc_or_ad)[0]
                            .contentEquals(eventDateOutside.getmBCAD())

                            && getResources().getStringArray(R.array.event_bc_or_ad)[1]
                            .contentEquals(value);

                } else {

                    return getResources().getStringArray(R.array.event_bc_or_ad)[1]
                            .contentEquals(eventDateOutside.getmBCAD())

                            && getResources().getStringArray(R.array.event_bc_or_ad)[0]
                            .contentEquals(value);

                }


            case YEAR_STEP:

                if (eventDateOutside.getmBCAD().contentEquals(selectedBCAD)) {

                    if (getResources().getStringArray(R.array.event_bc_or_ad)[0]
                            .contentEquals(eventDateOutside.getmBCAD())) {


                        return backwardTime ?
                                Integer.parseInt(value) < Integer.parseInt(eventDateOutside.getYear()) :
                                Integer.parseInt(value) > Integer.parseInt(eventDateOutside.getYear());

                    } else if (getResources().getStringArray(R.array.event_bc_or_ad)[1]
                            .contentEquals(eventDateOutside.getmBCAD())) {

                        return backwardTime ?
                                Integer.parseInt(value) > Integer.parseInt(eventDateOutside.getYear()) :
                                Integer.parseInt(value) < Integer.parseInt(eventDateOutside.getYear());

                    }

                }


            case MONTH_STEP:

                if (!TextUtils.isEmpty(value)
                        && eventDateOutside.getYear().contentEquals(value)) {


                    return backwardTime ?
                            Integer.parseInt(eventDateOutside.getMonth()) < Integer.parseInt(value) :
                            Integer.parseInt(eventDateOutside.getMonth()) > Integer.parseInt(value);


                }


            case DAY_STEP:

                if (!TextUtils.isEmpty(value)
                        && eventDateOutside.getYear().contentEquals(value)
                        && eventDateOutside.getMonth().contentEquals(value)) {


                    return backwardTime ?
                            Integer.parseInt(eventDateOutside.getMonth()) < Integer.parseInt(value) :
                            Integer.parseInt(eventDateOutside.getMonth()) > Integer.parseInt(value);


                }


        }

        return false;
    }

    private void playbackAnEventDate(int position) {

        if (eventDateInside == null) return;

        String mDateBCAD = eventDateInside.getmBCAD();
        String mDateYear = eventDateInside.getYear();


        if (TextUtils.isEmpty(mDateBCAD)) {

            mBCADSpinner.resetPlaceHolderText();
            return;

        }


        boolean isBCorAD = isBC(mDateBCAD);

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

    private boolean isBC(String mDateBCAD) {
        return mDateBCAD
                .contentEquals(getResources()
                        .getStringArray(R.array.event_bc_or_ad)[0]);
    }


    private void setYearInput(int year, int monthOfYear, int dayOfMonth) {


        if (selectedBCAD.contentEquals(getResources()
                .getStringArray(R.array.event_bc_or_ad)[0])) {


            monthSpinner.setVisibility(View.VISIBLE);
            datePicker.setVisibility(View.GONE);


        } else if (getResources().getStringArray(R.array.event_bc_or_ad)[1]
                .contentEquals(selectedBCAD)) {


            if (year >= 1900 && year <= Calendar.getInstance().get(Calendar.YEAR)) {


                dateSetterLayout.setVisibility(View.GONE);
                datePicker.setVisibility(View.VISIBLE);


                setEventDatePicker(year, monthOfYear, dayOfMonth);


            } else if (year < 1900) {


                monthSpinner.setVisibility(View.VISIBLE);
                datePicker.setVisibility(View.GONE);


            } else {

                Toast.makeText(getContext(), R.string.event_creation_in_futur_year_error, Toast.LENGTH_SHORT).show();

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

    private void saveEventYearFromDatePicker(int year, int monthOfYear, int dayOfMonth) {

        if (eventDateOutside == null && eventDateInside != null) {


            addAnEventDate(selectedBCAD,
                    String.valueOf(year),
                    String.valueOf(monthOfYear),
                    String.valueOf(dayOfMonth));


        } else {

            if (eventDateOutside != null && eventDateInside != null) {


                eventDateInside = new EventDate(selectedBCAD,
                        String.valueOf(year),
                        String.valueOf(monthOfYear),
                        String.valueOf(dayOfMonth));

            } else if (eventDateOutside == null && eventDateInside == null)

                addAnEventDate(selectedBCAD,
                        String.valueOf(year),
                        String.valueOf(monthOfYear),
                        String.valueOf(dayOfMonth));


        }

    }


    private void addAnEventDate(String selectedBCAD, String inputYear, String month, String day) {

        eventDateInside = new EventDate(selectedBCAD, inputYear, month, day);

        if (mCancelEventDateButton.getVisibility() == View.INVISIBLE) {
            mCancelEventDateButton.setVisibility(View.VISIBLE);
        }

    }

    private List<String> setMonthDays() {

        int number = setMonthDaysNumber();

        if (number == 0) return null;

        List<String> monthDay = new ArrayList<>();

        monthDay.add(getResources().getString(R.string.event_creation_unknown_item_date));


        for (int i = 0; i < number; i++) {

            monthDay.add(String.valueOf(i + 1));

        }


        return monthDay;
    }


    public BetterSpinner getmBCADSpinner() {
        return mBCADSpinner;
    }

    public BetterSpinner getMonthSpinner() {
        return monthSpinner;
    }

    public BetterSpinner getDaySpinner() {
        return daySpinner;
    }

    public AppCompatEditText getYearEditText() {
        return yearEditText;
    }

    public ImageView getmCancelEventDateButton() {
        return mCancelEventDateButton;
    }

    public View getDateSetterLayout() {
        return dateSetterLayout;
    }

    public DatePicker getDatePicker() {
        return datePicker;
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


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_event_cancel_date:

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle);

                builder.setTitle(R.string.event_creation_cancel_warning_title)
                        .setMessage(R.string.event_creation_cancel_warning_msg)
                        .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                mBCADSpinner.resetPlaceHolderText();
                                yearEditText.setText("");

                                monthSpinner.resetPlaceHolderText();
                                daySpinner.resetPlaceHolderText();

                                dateSetterLayout.setVisibility(View.GONE);
                                datePicker.setVisibility(View.GONE);


                                if(switchable != null){
                                    switchable.onDeleteInfo(eventDateOutside == null);
                                }

                                eventDateOutside = null;
                                eventDateInside = null;

                                mCancelEventDateButton.setVisibility(View.INVISIBLE);

                            }
                        })
                        .setNegativeButton(R.string.no_button, null)
                        .create().show();


        }


    }

    public void setSwitchable(EventDateListener switchable) {
        this.switchable = switchable;
    }

    public interface EventDateListener {

        void onSwitch(boolean isOn);

        void onDeleteInfo(boolean isStartingDate);

    }


}
