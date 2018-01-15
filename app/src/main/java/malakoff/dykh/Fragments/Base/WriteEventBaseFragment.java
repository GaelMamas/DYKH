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
import java.util.Arrays;
import java.util.Calendar;
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

    protected String selectedTheme, selectedTodayLocaction, selectedBCAD;
    protected boolean isDatedSingleEvent;
    protected int monthIndex = 0, dayIndex = 0;
    protected String[] newEventFinalDate;
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

        ArrayAdapter<String> mMonthsAdapter = new ArrayAdapter<>(getContext(),
                R.layout.cell_text_dark,
                DateFormatSymbols.getInstance(Locale.getDefault()).getMonths());

        String[] monthDay = new String[setMondaysNumber()];

        for (int i = 0; i < monthDay.length; i++) {

            monthDay[i] = String.valueOf(i + 1);

        }

        ArrayAdapter<String> mDaysAdapter = new ArrayAdapter<>(getContext(),
                R.layout.cell_text_dark,
                monthDay);

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

    private int setMondaysNumber() {
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
            default:

                return 30;
        }
    }

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

                isDatedSingleEvent = position == 0;

                break;


            case R.id.spinner_event_bc_or_ad:


            datePicker.setVisibility(View.GONE);
            dateSetterLayout.setVisibility(View.GONE);
            monthSpinner.setVisibility(View.GONE);
            daySpinner.setVisibility(View.GONE);

            yearEditText.setText("");
            monthSpinner.setText("");
            daySpinner.setText("");

            selectedBCAD = (String) parent.getItemAtPosition(position);

            if (!TextUtils.isEmpty(selectedBCAD)) dateSetterLayout.setVisibility(View.VISIBLE);


            break;



            case R.id.spinner_event_month_setter:

                monthIndex = position;

                return;


            case R.id.spinner_event_day_setter:

                dayIndex = position;

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
                String inputYear = v.getText().toString();
                if (inputYear.isEmpty()) {
                    newEventFinalDate[0] = "";
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

    public void runANewRequest(Request gsonRequest) {

        // Run a new request into the RequestQueue.

        MySingleton.getInstance(getContext()).addToRequestQueue(gsonRequest);

    }
}
