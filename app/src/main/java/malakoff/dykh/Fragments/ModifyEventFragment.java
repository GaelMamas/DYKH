package malakoff.dykh.Fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import malakoff.dykh.Activities.Base.BaseDawerActivity;
import malakoff.dykh.AppApplication.AppApplication;
import malakoff.dykh.AppApplication.Constants;
import malakoff.dykh.Event.Event;
import malakoff.dykh.Fragments.Base.WriteEventBaseFragment;
import malakoff.dykh.ModelBase.Base.EventDate;
import malakoff.dykh.Network.RequestsFactory;
import malakoff.dykh.R;
import malakoff.dykh.Utils.UsefulGenericMethods;

/**
 * Created by user on 12/07/2016.
 */
public class ModifyEventFragment extends WriteEventBaseFragment {

    private Event currentEvent;


    @Override
    protected void populateViews(Bundle savedInstanceState) {
        super.populateViews(savedInstanceState);

        currentEvent = mEventManager.readSpecificEvent(getArguments().getString(Constants.DYKH_FRAGMENT_SELECTION));

        if (getActivity() instanceof BaseDawerActivity) {
            ((BaseDawerActivity) getActivity()).setDefaultDrawable();
            ((BaseDawerActivity) getActivity()).changeToolbarConfig(R.drawable.ic_arrow_back_24dp,
                    currentEvent != null && !TextUtils.isEmpty(currentEvent.getTitle()) ?
                            currentEvent.getTitle() : getString(R.string.app_name));
        }

        if (currentEvent != null) {
            titleEditText.setText(currentEvent.getTitle());
            List<String> themes = Arrays.asList(getResources().getStringArray(R.array.event_themes));
            if (themes.contains(currentEvent.getTheme())) {
                themeSpinner.setSelection(themes.indexOf(currentEvent.getTheme()));
            }

            storyEditText.setText(currentEvent.getStory());

            List<String> todayLocations = UsefulGenericMethods.getWorldCountriesList();

            if (todayLocations.contains(!TextUtils.isEmpty(currentEvent.getLocationModernCalling()) ?
                    currentEvent.getLocationModernCalling() : currentEvent.getLocation())) {
                try {
                    todayLocationSpinner.setSelection(themes.indexOf(currentEvent.getLocationModernCalling()));
                } catch (IndexOutOfBoundsException e) {
                    Log.d(ModifyEventFragment.class.getSimpleName(), e.getLocalizedMessage());
                }
            }
            historicLocationEditText.setText(currentEvent.getLocation());

            //currentEvent.setSliceTime("1899 AD,1899-12-31-AD");
            currentEvent.setSliceTime("2018 AD,2018-03-25-AD");
            //currentEvent.setSliceTime("2015 AD,2015-10-19-AD,2018-01-24-AD");
            //currentEvent.setSliceTime("1899 AD,1899-12-30-AD,1899-12-31-AD");
            //currentEvent.setSliceTime("1899 AD");

            if (!TextUtils.isEmpty(currentEvent.getSliceTime())) {

                EventDate startEventDate;

                if (currentEvent.getSliceTime().contains(",")) {

                    String[] eventDates = currentEvent.getSliceTime().split(",");

                    if (eventDates == null || eventDates.length == 0) return;


                    if (!TextUtils.isEmpty(eventDates[0]) && eventDates.length == 1) {

                        startEventDate = retrieveYearAndBCAD(currentEvent.getSliceTime());

                        if (startEventDate != null) {

                            this.eventDates.add(startEventDate);

                        }

                        firstRecorderView.fillEventDateBlock(this.eventDates.get(0), true, false);

                    } else {

                        for (int i = 1; i < eventDates.length; i++) {

                            if (!TextUtils.isEmpty(eventDates[i])) {

                                EventDate eventDate = retrieveEventDate(eventDates[i]);

                                switch (i) {
                                    case 1:
                                        if (eventDate != null) {

                                            firstRecorderView.fillEventDateBlock(eventDate, true, eventDates.length > 2);

                                            secondRecorderView.setVisibility(View.GONE);
                                        }

                                        break;

                                    case 2:

                                        if (eventDate != null) {

                                            firstRecorderView.putOnSwitch(true);
                                            secondRecorderView.setVisibility(View.VISIBLE);
                                            secondRecorderView.fillEventDateBlock(eventDate, false, false);
                                        }

                                        break;
                                }

                            }

                        }


                    }


                } else {

                    startEventDate = retrieveYearAndBCAD(currentEvent.getSliceTime());

                    if (startEventDate != null) {

                        this.eventDates.add(startEventDate);

                        firstRecorderView.fillEventDateBlock(eventDates.get(0), true, false);

                    }


                }

            }


            publishButton.setText(R.string.event_modify_apply_button);

            publishButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    if (getActivity() == null) return false;

                    PopupMenu popup = new PopupMenu(getActivity(), publishButton, Gravity.TOP);

                    popup.getMenuInflater().inflate(R.menu.dykh_webservices_test, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @SuppressLint("SimpleDateFormat")
                        public boolean onMenuItemClick(MenuItem item) {

                            final JSONArray jsonArray;

                            switch (item.getItemId()) {


                                case R.id.event_putter:

                                    try {
                                        runANewRequest(RequestsFactory.putAnEvent(getContext(), UsefulGenericMethods.getEventJSONObject(currentEvent), resetInputsListener));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    break;

                                case R.id.events_putter:

                                    if (getContext() != null
                                            && mEventManager.getEvents() != null
                                            && mEventManager.getEvents().size() > 0) {

                                        jsonArray = new JSONArray();

                                        for (int i = 0; i < 10; i++) {

                                            try {

                                                jsonArray.put(UsefulGenericMethods.getEventJSONObject(mEventManager.getEvents().get(i)));

                                            } catch (NullPointerException e) {
                                                Log.e(this.getClass().getSimpleName(), e.getMessage());
                                            }

                                        }

                                        runANewRequest(RequestsFactory.putManyEvents(getContext(), jsonArray, resetInputsListener));

                                    }

                                    break;

                                case R.id.event_deleter:

                                    try {
                                        runANewRequest(RequestsFactory.deleteAnEvent(getContext(), "5a5124ebde9a37001e49bb9f", resetInputsListener));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    break;

                                case R.id.events_deleter:

                                    mEventManager.readEvents(AppApplication.getUserInfo().getUserId());

                                    if (getContext() != null
                                            && mEventManager.getEvents() != null
                                            && mEventManager.getEvents().size() > 0) {

                                        jsonArray = new JSONArray();

                                        for (Event event : mEventManager.getEvents()) {

                                            try {

                                                jsonArray.put(new JSONObject()
                                                        .put("_id",
                                                                event.getEventId()));

                                            } catch (NullPointerException e) {
                                                Log.e(this.getClass().getSimpleName(), e.getMessage());
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }

                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle);

                                        builder.setTitle("Deleting Warning")
                                                .setMessage("Are you sure to delete many events at once")
                                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                        runANewRequest(RequestsFactory.deleteManyEvents(getContext(), jsonArray, resetInputsListener));

                                                    }
                                                })
                                                .setNegativeButton("No", null)
                                                .create().show();

                                    }


                                    break;

                            }

                            return true;
                        }
                    });

                    popup.show();

                    return true;
                }
            });
        }
    }


    private EventDate retrieveEventDate(String eventDateFormat) {

        String[] skinOff;

        if (TextUtils.isEmpty(eventDateFormat)) return null;

        skinOff = eventDateFormat.split("-");

        if (skinOff == null || skinOff.length == 0) return null;

        return new EventDate(skinOff[3], skinOff[0], skinOff[1], skinOff[2]);

    }

    private EventDate retrieveYearAndBCAD(String sliceTime) {

        if (TextUtils.isEmpty(sliceTime)) return null;

        if (sliceTime.contains("BC")
                || sliceTime.contains("AD")) {

            String[] yearBCADArray = sliceTime.trim().split(" ");
            String mYear = null;

            if (yearBCADArray == null || yearBCADArray.length == 0) return null;


            if (!TextUtils.isEmpty(yearBCADArray[0])
                    && yearBCADArray[0].matches("[-+]?\\d*\\.?\\d+")) {

                mYear = yearBCADArray[0];

            }

            return new EventDate(yearBCADArray[1], mYear, null, null);

        }


        return null;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_event_publish:

                String title = titleEditText.getText().toString(),
                        historicLocation = historicLocationEditText.getText().toString(),
                        story = storyEditText.getText().toString();


                if (!isTitleOK) {
                    titleEditText.setError("Need to be filled");
                    onScrollToChild(titleEditText.getScrollX(), titleEditText.getScrollY());

                } else if (!isThemeOK) {

                    themeSpinner.setError("Need to be filled");
                    onScrollToChild(themeSpinner.getScrollX(), themeSpinner.getScrollY());

                } else if (!isStoryOK) {

                    storyEditText.setError("Need to be filled");
                    onScrollToChild(storyEditText.getScrollX(), storyEditText.getScrollY());

                } else if (!isTodayLocationOK) {

                    todayLocationSpinner.setError("Need to be filled");
                    onScrollToChild(todayLocationSpinner.getScrollX(), todayLocationSpinner.getScrollY());

                } else if (!isHistoricLocationOK) {

                    historicLocationEditText.setError("Need to be filled");
                    onScrollToChild(historicLocationEditText.getScrollX(),
                            historicLocationEditText.getScrollY());

                } else if (!isEventDateOK) {

                    if (secondRecorderView.getVisibility() == View.VISIBLE) {

                        firstRecorderView.getHeaderTitleTextView().setError(null);
                        secondRecorderView.getHeaderTitleTextView().setError("Need to be filled");

                    } else {

                        firstRecorderView.getHeaderTitleTextView().setError("Need to be filled");

                        onScrollToChild(firstRecorderView.getHeaderTitleTextView().getScrollX(),
                                firstRecorderView.getHeaderTitleTextView().getScrollY());

                    }

                } else if (resetInputsListener.canProcess()) {

                    try {

                        mEventUpdatingProgressBar.setVisibility(View.VISIBLE);

                        runANewRequest(RequestsFactory.putAnEvent(getContext(), UsefulGenericMethods.getEventJSONObject(
                                new Event(
                                        currentEvent.getEventId(),
                                        makeUpEventDateFormat(),
                                        historicLocation,
                                        selectedTodayLocation,
                                        AppApplication.getUserInfo().getUserId(),
                                        title,
                                        story,
                                        selectedTheme
                                )), resetInputsListener));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public static ModifyEventFragment newInstance(String eventId) {

        Bundle args = new Bundle();

        ModifyEventFragment fragment = new ModifyEventFragment();
        args.putString(Constants.DYKH_FRAGMENT_SELECTION, eventId);
        fragment.setArguments(args);
        return fragment;
    }
}
