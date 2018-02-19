package malakoff.dykh.DesignWidget.ModelBase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

    private View rootview;
    private BetterSpinner mBCADSpinner, monthSpinner, daySpinner;
    private AppCompatEditText yearEditText;
    private View dateSetterLayout, switchLayout;
    private DatePicker datePicker;


    private int monthIndex;
    private String selectedBCAD, inputYear;


    private EventDateRecordable dateRecordable;

    private EventDate mRecordedEventDate;

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


    private void init(){


        mBCADSpinner = rootview.findViewById(R.id.spinner_event_bc_or_ad);
        yearEditText = rootview.findViewById(R.id.edittext_event_year_setter);

        monthSpinner = rootview.findViewById(R.id.spinner_event_month_setter);
        daySpinner = rootview.findViewById(R.id.spinner_event_day_setter);

        dateSetterLayout = rootview.findViewById(R.id.event_date_setter_layout);

        datePicker = rootview.findViewById(R.id.datePicker);

        switchLayout = rootview.findViewById(R.id.layout_event_date_switch);

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

                selectedBCAD = (String) adapterView.getItemAtPosition(position);

                yearEditText.setVisibility(VISIBLE);
                monthSpinner.setVisibility(INVISIBLE);
                daySpinner.setVisibility(INVISIBLE);
                dateSetterLayout.setVisibility(VISIBLE);
                datePicker.setVisibility(GONE);
                switchLayout.setVisibility(GONE);

                if (mRecordedEventDate != null && dateRecordable != null) {

                    dateRecordable.onSwitch(false);
                    dateRecordable.isCompleteDateAvailable(null);

                }

                break;

            case R.id.spinner_event_month_setter:

                daySpinner.setVisibility(position > 0 ? VISIBLE : INVISIBLE);

                monthSpinner.setError(null);

                monthIndex = position;

                Toast.makeText(getContext(), "Month " + position, Toast.LENGTH_SHORT).show();

                if (position == 0) {

                    recordThisDate(inputYear, String.valueOf(monthIndex), null);

                    if (dateRecordable != null) {
                        dateRecordable.isCompleteDateAvailable(mRecordedEventDate);
                        dateRecordable.onSwitch(true);
                    }

                }

                break;


            case R.id.spinner_event_day_setter:

                daySpinner.setError(null);

                Toast.makeText(getContext(), "Day " + position, Toast.LENGTH_SHORT).show();


                recordThisDate(inputYear, String.valueOf(monthIndex), String.valueOf(position));

                if (dateRecordable != null) {
                    dateRecordable.isCompleteDateAvailable(mRecordedEventDate);
                    dateRecordable.onSwitch(true);
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


                setYearInput(Integer.parseInt(inputYear = v.getText().toString()), 0, 1);


        }


        UsefulGenericMethods.hideKeyboard(getContext(), yearEditText);

        return true;
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

        recordThisDate(year, monthOfYear, dayOfMonth);


        datePicker.init(year, monthOfYear, dayOfMonth == 0 ? 1 : dayOfMonth, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


                if ((year < Calendar.getInstance().get(Calendar.YEAR)) ||
                        (monthOfYear <= Calendar.getInstance().get(Calendar.MONTH)
                                && dayOfMonth <= Calendar.getInstance().get(Calendar.DAY_OF_MONTH))) {

                    recordThisDate(year, monthOfYear, dayOfMonth);

                    if (dateRecordable != null) {

                        dateRecordable.isCompleteDateAvailable(mRecordedEventDate);
                        dateRecordable.onSwitch(true);

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

    private void recordThisDate(int year, int monthOfYear, int dayOfMonth) {

        recordThisDate(String.valueOf(year),
                String.valueOf(monthOfYear),
                String.valueOf(dayOfMonth));

    }

    private void recordThisDate(String year, String monthOfYear, String dayOfMonth) {

        mRecordedEventDate = new EventDate(selectedBCAD,
                String.valueOf(year),
                String.valueOf(monthOfYear),
                String.valueOf(dayOfMonth));

        Toast.makeText(getContext(), "Year " + year + " Month " + monthOfYear + " Day " + dayOfMonth, Toast.LENGTH_SHORT).show();

        switchLayout.setVisibility(VISIBLE);

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

    public void setDefaultValues(EventDate eventDate) {
        if (eventDate == null || TextUtils.isEmpty(eventDate.getmBCAD())) return;

        setVisibility(VISIBLE);

        init();

        mBCADSpinner.setSelection(eventDate.getmBCAD().contentEquals(getResources()
                .getStringArray(R.array.event_bc_or_ad)[0]) ? 0 : 1);

        if(TextUtils.isEmpty(eventDate.getYear())
                && eventDate.getYear().matches("[-+]?\\d*\\.?\\d+")) return;

        setYearInput(Integer.parseInt(eventDate.getYear()),
                Integer.parseInt(eventDate.getMonth()),
                        TextUtils.isEmpty(eventDate.getDay())?0:Integer.parseInt(eventDate.getDay()));

    }

    public void setDateRecordable(EventDateRecordable dateRecordable) {
        this.dateRecordable = dateRecordable;
    }

    public interface EventDateRecordable {

        void onSwitch(boolean isOn);

        void isCompleteDateAvailable(EventDate eventDate);
    }
}
