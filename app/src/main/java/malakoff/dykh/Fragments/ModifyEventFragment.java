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

            if (!TextUtils.isEmpty(currentEvent.getSliceTime())) {

                //dateSetterLayout.setVisibility(View.VISIBLE);

                //mBCADSpinner.setSelection("BC".contains(currentEvent.getSliceTime()) ? 0 : 1);

                if ("-".contains(currentEvent.getSliceTime())) {

                    try {
                        String[] mYMD = TextUtils.split(currentEvent.getSliceTime(), "-");

                        //yearEditText.setText(mYMD[0]);
                        //monthSpinner.setText(mYMD[1]);

                        if ("BC".contains(mYMD[2]) || "AD".contains(mYMD[2])) {

                            //daySpinner.setText(mYMD[2].split(" ")[0]);

                        } else {

                            //daySpinner.setText(mYMD[2]);

                        }


                    } catch (NullPointerException e) {
                        e.getStackTrace();
                    }

                } else {

                    //yearEditText.setText(currentEvent.getSliceTime());

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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_event_publish:

                String title = titleEditText.getText().toString(),
                        historicLocation = historicLocationEditText.getText().toString(),
                        story = storyEditText.getText().toString();


                if (TextUtils.isEmpty(title)) {
                    titleEditText.setError("Need to be filled");
                    titleEditText.scrollTo(titleEditText.getScrollX(), titleEditText.getScrollY());

                } else if (themeSpinner.getSelectedIndex() == -1) {

                    themeSpinner.setError("Need to be filled");
                    themeSpinner.scrollTo(themeSpinner.getScrollX(), themeSpinner.getScrollY());

                } else if (todayLocationSpinner.getSelectedIndex() == -1) {

                    todayLocationSpinner.setError("Need to be filled");
                    todayLocationSpinner.scrollTo(todayLocationSpinner.getScrollX(), todayLocationSpinner.getScrollY());

                } else if (TextUtils.isEmpty(historicLocationEditText.getText())) {

                    historicLocationEditText.setError("Need to be filled");
                    historicLocationEditText.scrollTo(historicLocationEditText.getScrollX(),
                            historicLocationEditText.getScrollY());

                } /*else if (mBCADSpinner.getSelectedIndex() == -1) {

                    mBCADSpinner.setError("Need to be filled");
                    mBCADSpinner.scrollTo(mBCADSpinner.getScrollX(), mBCADSpinner.getScrollY());

                } else if (TextUtils.isEmpty(yearEditText.getText())) {

                    yearEditText.setError("Need to be filled");
                    yearEditText.scrollTo(yearEditText.getScrollX(), yearEditText.getScrollY());

                }*/else if (resetInputsListener.canProcess()) {

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
