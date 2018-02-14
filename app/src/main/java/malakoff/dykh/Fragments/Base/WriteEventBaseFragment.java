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
import malakoff.dykh.DesignWidget.EventDateDeclarationCardView;
import malakoff.dykh.Interfaces.ResetInputsListener;
import malakoff.dykh.ModelBase.Base.EventDate;
import malakoff.dykh.Network.MySingleton;
import malakoff.dykh.R;
import malakoff.dykh.Utils.UsefulGenericMethods;

/**
 * Created by VM32776N on 03/03/2017.
 */

public class WriteEventBaseFragment extends InstanceBaseFragement implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    protected String selectedTheme, selectedTodayLocation;
    protected List<EventDate> eventDates = new ArrayList<>();

    protected BetterSpinner themeSpinner, todayLocationSpinner;
    protected AppCompatEditText titleEditText, historicLocationEditText, storyEditText;
    protected EventDateDeclarationCardView startingDateCardView, endingDateCardView;
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
            startingDateCardView.getmBCADSpinner().resetPlaceHolderText();

            endingDateCardView.getmBCADSpinner().resetPlaceHolderText();

            startingDateCardView.getDatePicker().setVisibility(View.GONE);
            endingDateCardView.getDatePicker().setVisibility(View.GONE);

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


        titleEditText = view.findViewById(R.id.edittext_event_title);
        historicLocationEditText = view.findViewById(R.id.edittext_event_location);
        storyEditText = view.findViewById(R.id.edittext_event_story);

        startingDateCardView = view.findViewById(R.id.component_event_starting_date);
        endingDateCardView = view.findViewById(R.id.component_event_ending_date);

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

        themeSpinner.setAdapter(themeAdapter);
        themeSpinner.setOnItemSelectedListener(this);

        todayLocationSpinner.setAdapter(todayLocationAdapter);
        todayLocationSpinner.setOnItemSelectedListener(this);

        publishButton.setOnClickListener(this);

        startingDateCardView.setSwitchable(new EventDateDeclarationCardView.EventDateSwitchable() {
            @Override
            public void onSwitch(boolean isOn) {

                endingDateCardView.setValues(new EventDate("AD", "2018", "1", "14"), true);
            }
        });

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


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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





    public void runANewRequest(Request gsonRequest) {

        // Run a new request into the RequestQueue.

        MySingleton.getInstance(getContext()).addToRequestQueue(gsonRequest);

    }
}
