package malakoff.dykh.Fragments.Base;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import malakoff.dykh.DesignWidget.BetterSpinner;
import malakoff.dykh.DesignWidget.ModelBase.DateRecorderView;
import malakoff.dykh.Interfaces.ResetInputsListener;
import malakoff.dykh.ModelBase.Base.EventDate;
import malakoff.dykh.Network.MySingleton;
import malakoff.dykh.R;
import malakoff.dykh.Utils.UsefulGenericMethods;

/**
 * Created by VM32776N on 03/03/2017.
 */

public class WriteEventBaseFragment extends InstanceBaseFragement implements View.OnClickListener, AdapterView.OnItemSelectedListener, TextView.OnEditorActionListener {

    protected String selectedTheme, selectedTodayLocation;
    protected List<EventDate> eventDates = new ArrayList<>();

    protected ScrollView containerScrollView;
    protected BetterSpinner themeSpinner, todayLocationSpinner;
    protected AppCompatEditText titleEditText, historicLocationEditText, storyEditText;
    protected Button publishButton;
    protected ProgressBar mEventUpdatingProgressBar;

    protected DateRecorderView firstRecorderView, secondRecorderView;

    protected boolean isThemeOK, isTodayLocationOK, isTitleOK, isHistoricLocationOK, isStoryOK, isEventDateOK;


    protected ResetInputsListener resetInputsListener = new ResetInputsListener() {
        @Override
        public void succeed() {

            titleEditText.setText("");
            storyEditText.setText("");
            historicLocationEditText.setText("");

            themeSpinner.resetPlaceHolderText();
            todayLocationSpinner.resetPlaceHolderText();

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

        containerScrollView = view.findViewById(R.id.container_event_date);

        themeSpinner = view.findViewById(R.id.spinner_event_theme);
        todayLocationSpinner = view.findViewById(R.id.spinner_event_today_location);


        titleEditText = view.findViewById(R.id.edittext_event_title);
        historicLocationEditText = view.findViewById(R.id.edittext_event_location);
        storyEditText = view.findViewById(R.id.edittext_event_story);

        firstRecorderView = view.findViewById(R.id.recorder_first_event);
        secondRecorderView = view.findViewById(R.id.recorder_second_event);

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

        titleEditText.setOnEditorActionListener(this);
        storyEditText.setOnEditorActionListener(this);
        historicLocationEditText.setOnEditorActionListener(this);

        themeSpinner.setAdapter(themeAdapter);
        themeSpinner.setOnItemSelectedListener(this);

        todayLocationSpinner.setAdapter(todayLocationAdapter);
        todayLocationSpinner.setOnItemSelectedListener(this);


        titleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validateTitle(editable);
            }
        });
        storyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validateStory(editable);
            }
        });
        historicLocationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validateHistory(editable);
            }
        });


        firstRecorderView.setDateRecordable(new DateRecorderView.EventDateRecordable() {
            @Override
            public void onSwitch(boolean isOn, EventDate eventDate) {
                secondRecorderView.setVisibility(isOn ? View.VISIBLE : View.GONE);
                secondRecorderView.setTwinDefaultValues(eventDate);
                isEventDateOK = !isOn && eventDate != null;
                enablePublishButton();
            }

            @Override
            public void isCompleteDateAvailable(EventDate eventDate) {
                if (eventDate == null) {

                    secondRecorderView.setVisibility(View.GONE);

                    isEventDateOK = false;

                    return;
                }
                secondRecorderView.setTwinDefaultValues(eventDate);

                isEventDateOK = true;

                enablePublishButton();
            }
        });

        secondRecorderView.setDateRecordable(new DateRecorderView.EventDateRecordable() {
            @Override
            public void onSwitch(boolean isOn, EventDate eventDate) {

            }

            @Override
            public void isCompleteDateAvailable(EventDate eventDate) {
                isEventDateOK = eventDate != null;
                enablePublishButton();
            }
        });


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
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch ((int) id) {

            case R.id.spinner_event_theme:

                themeSpinner.setError(null);

                selectedTheme = (String) parent.getItemAtPosition(position);

                isThemeOK = !TextUtils.isEmpty(selectedTheme);

                enablePublishButton();

                break;

            case R.id.spinner_event_today_location:

                todayLocationSpinner.setError(null);

                selectedTodayLocation = (String) parent.getItemAtPosition(position);

                isTodayLocationOK = !TextUtils.isEmpty(selectedTodayLocation);

                enablePublishButton();

                if (!isTodayLocationOK)
                    Toast.makeText(getContext(), "Set precisely the location", Toast.LENGTH_SHORT).show();

                break;


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {


        switch (textView.getId()) {

            case R.id.edittext_event_title:

                validateTitle(textView.getText());

                break;

            case R.id.edittext_event_story:

                validateStory(textView.getText());

                break;

            case R.id.edittext_event_location:

                validateHistory(textView.getText());

                break;

        }


        return false;
    }


    protected String makeUpEventDateFormat() {

        // only one year: year BC/AD, date:YYYY-MM-DD-BC/AD
        // start year: year BC/AD,start date:YYYY-MM-DD-BC/AD, end date :YYYY-MM-DD-BC/AD
        eventDates.add(firstRecorderView.getmRecordedEventDate());
        if (secondRecorderView.getVisibility() == View.VISIBLE)
            eventDates.add(secondRecorderView.getmRecordedEventDate());


        switch (eventDates.size()) {
            case 2:

                StringBuilder finalEventDate = makeUpFirstPartEventDate(eventDates.get(0));


                if (finalEventDate.toString().contentEquals(makeUpFirstPartEventDate(eventDates.get(1)))) {

                    finalEventDate = makeUpFirstPartEventDate(eventDates.get(0));


                    Log.i("DYKH Date Format", "Same Date " + finalEventDate.toString());

                } else {

                    finalEventDate.append(",")
                            .append(eventDates.get(1).getYear())
                            .append("-")
                            .append(!TextUtils.isEmpty(eventDates.get(1).getMonth()) ? eventDates.get(1).getMonth() : "")
                            .append("-")
                            .append(!TextUtils.isEmpty(eventDates.get(1).getDay()) ? eventDates.get(1).getDay() : "")
                            .append("-")
                            .append(eventDates.get(1).getmBCAD());
                }

                return finalEventDate.toString();
            case 1:

                finalEventDate = makeUpFirstPartEventDate(eventDates.get(0));


                Log.i("DYKH Date Format", finalEventDate.toString());

                return finalEventDate.toString();
            default:
                return null;

        }

    }

    private StringBuilder makeUpFirstPartEventDate(EventDate eventDate) {

        return new StringBuilder().append(eventDate.getYear())
                .append(" ")
                .append(eventDate.getmBCAD())
                .append(",")
                .append(eventDate.getYear())
                .append("-")
                .append(!TextUtils.isEmpty(eventDate.getMonth()) ? eventDate.getMonth() : "")
                .append("-")
                .append(!TextUtils.isEmpty(eventDate.getDay()) ? eventDate.getDay() : "")
                .append("-")
                .append(eventDate.getmBCAD());

    }

    protected void enablePublishButton() {

        publishButton.setSelected(isTitleOK && isThemeOK && isStoryOK
                && isTodayLocationOK && isHistoricLocationOK && isEventDateOK);

    }


    public void runANewRequest(Request gsonRequest) {

        // Run a new request into the RequestQueue.

        MySingleton.getInstance(getContext()).addToRequestQueue(gsonRequest);

    }


    protected void onScrollToChild(final int x, final int y) {

        new Handler().post(new Runnable() {
            @Override
            public void run() {

                containerScrollView.scrollTo(x, y);

            }
        });

    }

    private void validateTitle(CharSequence editable) {

        titleEditText.setError(null);

        isTitleOK = !TextUtils.isEmpty(editable);

        enablePublishButton();

    }

    private void validateStory(CharSequence editable) {

        storyEditText.setError(null);

        isStoryOK = !TextUtils.isEmpty(editable);

        enablePublishButton();

    }

    private void validateHistory(CharSequence editable) {

        historicLocationEditText.setError(null);

        isHistoricLocationOK = !TextUtils.isEmpty(editable);

        enablePublishButton();

    }
}
