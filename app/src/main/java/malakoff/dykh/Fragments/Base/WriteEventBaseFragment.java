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
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Calendar;

import malakoff.dykh.DesignWidget.BetterSpinner;
import malakoff.dykh.R;
import malakoff.dykh.Utils.UsefulGenericMethods;

/**
 * Created by VM32776N on 03/03/2017.
 */

public class WriteEventBaseFragment extends InstanceBaseFragement implements View.OnClickListener, AdapterView.OnItemSelectedListener, TextView.OnEditorActionListener{

    protected String selectedTheme, selectedTodayLocaction, selectedBCAD;
    protected String [] newEventFinalDate;
    protected BetterSpinner themeSprinner, todayLocationSpinner, mBCADSpinner;
    protected AppCompatEditText titleEditText, historicLocationEditText, storyEditText, yearEditText, monthEditText, dayEditText;
    protected View dateSetterLayout;
    protected DatePicker datePicker;
    protected Button publishButton;


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

        themeSprinner.setAdapter(themeAdapter);
        themeSprinner.setOnItemSelectedListener(this);

        todayLocationSpinner.setAdapter(todayLocationAdapter);
        todayLocationSpinner.setOnItemSelectedListener(this);

        mBCADSpinner.setAdapter(mBCADAdapter);
        mBCADSpinner.setOnItemSelectedListener(this);

        yearEditText.setOnEditorActionListener(this);
        monthEditText.setOnEditorActionListener(this);
        dayEditText.setOnEditorActionListener(this);

        publishButton.setOnClickListener(this);

        newEventFinalDate = new String[3];

        for (int i = 0; i < newEventFinalDate.length; i++) {
            newEventFinalDate[i] = "";
        }
    }

    @Override
    protected void assignViews(View view) {
        themeSprinner = (BetterSpinner) view.findViewById(R.id.spinner_event_theme);
        todayLocationSpinner = (BetterSpinner) view.findViewById(R.id.spinner_event_today_location);
        mBCADSpinner = (BetterSpinner) view.findViewById(R.id.spinner_event_bc_or_ad);

        titleEditText = (AppCompatEditText) view.findViewById(R.id.edittext_event_title);
        historicLocationEditText = (AppCompatEditText) view.findViewById(R.id.edittext_event_location);
        storyEditText = (AppCompatEditText) view.findViewById(R.id.edittext_event_story);

        dateSetterLayout = view.findViewById(R.id.event_date_setter_layout);

        yearEditText = (AppCompatEditText) view.findViewById(R.id.edittext_event_year_setter);
        monthEditText = (AppCompatEditText) view.findViewById(R.id.edittext_event_month_setter);
        dayEditText = (AppCompatEditText) view.findViewById(R.id.edittext_event_day_setter);

        datePicker = (DatePicker) view.findViewById(R.id.datePicker);

        publishButton = (Button) view.findViewById(R.id.button_event_publish);
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
            case R.id.spinner_event_bc_or_ad:
                datePicker.setVisibility(View.GONE);
                dateSetterLayout.setVisibility(View.GONE);
                monthEditText.setVisibility(View.GONE);
                dayEditText.setVisibility(View.GONE);

                yearEditText.setText("");
                monthEditText.setText("");
                dayEditText.setText("");

                selectedBCAD = (String) parent.getItemAtPosition(position);

                if (!TextUtils.isEmpty(selectedBCAD)) dateSetterLayout.setVisibility(View.VISIBLE);
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
                    monthEditText.setVisibility(View.VISIBLE);
                } else if (selectedBCAD.contentEquals(getResources().getStringArray(R.array.event_bc_or_ad)[1])) {
                    //TODO DATE HAS TO BE DECLARED IN THE PAST
                    if (year >= 1900 && year <= Calendar.getInstance().get(Calendar.YEAR)) {
                        dateSetterLayout.setVisibility(View.GONE);
                        datePicker.setVisibility(View.VISIBLE);
                        datePicker.init(year, 0, 1, new DatePicker.OnDateChangedListener() {
                            @Override
                            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                newEventFinalDate[0] = String.valueOf(year);
                                newEventFinalDate[1] = String.valueOf(monthOfYear);
                                newEventFinalDate[2] = String.valueOf(dayOfMonth);

                                Toast.makeText(getContext(), newEventFinalDate[0] + "-" + newEventFinalDate[1] + "-" + newEventFinalDate[2], Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (year <= Calendar.getInstance().get(Calendar.YEAR)) {
                        monthEditText.setVisibility(View.VISIBLE);
                        newEventFinalDate[0] = inputYear;
                    }
                }

                return true;
            case R.id.edittext_event_month_setter:
                String inputMonth = v.getText().toString();
                if (inputMonth.isEmpty()) {
                    newEventFinalDate[1] = "0";
                    return false;
                }

                int month = Integer.parseInt(inputMonth);

                if (month > 0 && month < 12) {
                    dayEditText.setVisibility(View.VISIBLE);
                    newEventFinalDate[1] = inputMonth;
                } else {
                    newEventFinalDate[1] = "0";
                }

                return true;
            case R.id.edittext_event_day_setter:
                String inputDay = v.getText().toString();
                if (inputDay.isEmpty()) {
                    newEventFinalDate[2] = "0";
                    return false;
                }

                int day = Integer.parseInt(inputDay);

                month = Integer.parseInt(monthEditText.getText().toString());
                if (day > 0 && day < (month == 2 ? 29 : (month == 4 || month == 6 || month == 9 || month == 11) ? 30 : 31)) {
                    newEventFinalDate[2] = inputDay;
                } else {
                    newEventFinalDate[2] = "0";
                }

                Toast.makeText(getContext(), newEventFinalDate[0] + "-" + newEventFinalDate[1] + "-" + newEventFinalDate[2], Toast.LENGTH_SHORT).show();

                return true;
        }


        return false;
    }
}
