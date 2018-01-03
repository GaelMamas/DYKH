package malakoff.dykh.Fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import malakoff.dykh.Activities.Base.BaseDawerActivity;
import malakoff.dykh.AppApplication.AppApplication;
import malakoff.dykh.AppApplication.Constants;
import malakoff.dykh.Event.Event;
import malakoff.dykh.Fragments.Base.WriteEventBaseFragment;
import malakoff.dykh.ModelBase.EventsPartition;
import malakoff.dykh.Network.GsonArrayRequest;
import malakoff.dykh.Network.MySingleton;
import malakoff.dykh.Network.RequestsFactory;
import malakoff.dykh.R;
import malakoff.dykh.Utils.UsefulGenericMethods;

import static android.view.View.OnLongClickListener;

/**
 * Created by user on 21/06/2016.
 */

public class EventCreationFragment extends WriteEventBaseFragment {

    RequestQueue queue;
    //TODO Remove this later
    List<Event> eventsUploadingTest = new ArrayList<>();

    public static EventCreationFragment newInstance() {
        return new EventCreationFragment();
    }


    @Override
    protected void populateViews(Bundle savedInstanceState) {
        super.populateViews(savedInstanceState);

        publishButton.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if (getActivity() == null) return false;

                PopupMenu popup = new PopupMenu(getActivity(), publishButton, Gravity.TOP);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.dykh_webservices_test, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("SimpleDateFormat")
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.events_getter:

                                runANewRequest(RequestsFactory.getSpecificEvents(getContext(), AppApplication.getUserInfo().getUserId()));

                                break;
                            case R.id.event_deleter:

                                runANewRequest(RequestsFactory.deleteAnEvent(getContext(), "5a4cf52d5fdf98001e11a74d"));

                                break;
                            case R.id.event_poster:

                                runANewRequest(RequestsFactory.postAnEvent(getContext(), getView(), new Event(
                                        "",
                                        new SimpleDateFormat("yyyy").format(System.currentTimeMillis()) + " AD",
                                        "Clamart",
                                        "Clamart",
                                        AppApplication.getUserInfo().getUserId(),
                                        selectedTodayLocaction,
                                        "Node JS, Block Chain Smart Contract",
                                        "Invention"

                                )));

                                break;
                            case R.id.events_poster:


                                setUploadEventsListAccordingToPeriod(UsefulGenericMethods
                                        .setDefaultModelData(getResources().openRawResource(R.raw.bc_events)));


                                setUploadEventsListAccordingToPeriod(UsefulGenericMethods
                                        .setDefaultModelData(getResources().openRawResource(R.raw.ad_events)));


                                if (getContext() != null) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle);

                                    builder.setTitle("Posting Warning")
                                            .setMessage("Are you sure to post many events at once")
                                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                    runANewRequest(RequestsFactory.postManyEvents(getContext(), getView(), eventsUploadingTest));
                                                }
                                            })
                                            .setNegativeButton("No", null)
                                            .create().show();

                                }


                                break;

                            case R.id.events_deleter:

                                mEventManager.readEvents(AppApplication.getUserInfo().getUserId());

                                if(getContext() != null
                                        && mEventManager.getEvents() != null
                                        && mEventManager.getEvents().size() > 0){

                                    final JSONArray jsonArray = new JSONArray();

                                    for (Event event: mEventManager.getEvents()){

                                            try {
                                                jsonArray.put(event.getEventId());
                                            } catch (NullPointerException e) {
                                                Log.e(this.getClass().getSimpleName(), e.getMessage());
                                            }

                                    }

                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle);

                                    builder.setTitle("Deleting Warning")
                                            .setMessage("Are you sure to delete many events at once")
                                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                    runANewRequest(RequestsFactory.deleteManyEvents(getContext(), jsonArray));

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

                popup.show();//showing popup menu

                return true;
            }
        });
    }


    private void setUploadEventsListAccordingToPeriod(EventsPartition eventsPartition) {

        if (eventsPartition != null
                && eventsPartition.getEvents() != null
                && !eventsPartition.getEvents().isEmpty()
                && AppApplication.getUserInfo() != null) {

            for(Event event:eventsPartition.getEvents()){

                event.setUserId(AppApplication.getUserInfo().getUserId());

            }

            eventsUploadingTest.addAll(eventsPartition.getEvents());


        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        queue = MySingleton.getInstance(getContext().getApplicationContext()).getRequestQueue();
        queue.start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_event_publish:

                String title = titleEditText.getText().toString(),
                        historicLocation = historicLocationEditText.getText().toString(),
                        story = storyEditText.getText().toString();


                if (TextUtils.isEmpty(title)
                        || TextUtils.isEmpty(historicLocation)
                        || TextUtils.isEmpty(story)) {
                    titleEditText.setError("Need to be filled");
                    historicLocationEditText.setError("Need to be filled");
                    storyEditText.setError("Need to be filled");

                } else if (getActivity() != null && (TextUtils.isEmpty(selectedTheme)
                        || TextUtils.isEmpty(selectedTodayLocaction))) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);

                    alertDialog.setTitle(R.string.dykh_warning)
                            .setMessage(R.string.dyky_publishing_msg_error)
                            .setNeutralButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                    alertDialog.show();

                } else if (getView() != null) {

                    runANewRequest(RequestsFactory.postAnEvent(getContext(), getView(), new Event(
                            "",
                            newEventFinalDate[0] + "-" + newEventFinalDate[1] + "-" + newEventFinalDate[2] + " " + selectedBCAD,
                            historicLocation,
                            selectedTodayLocaction,
                            AppApplication.getUserInfo().getUserId(),
                            title,
                            story,
                            selectedTheme

                    )));
                }
                break;
        }
    }


    public void runANewRequest(Request gsonRequest) {

        // Run a new request into the RequestQueue.

        MySingleton.getInstance(getContext()).addToRequestQueue(gsonRequest);

    }

    @Override
    protected void reinitToolbar() {
        super.reinitToolbar();
        if (getActivity() instanceof BaseDawerActivity) {
            ((BaseDawerActivity) getActivity()).setDefaultDrawable();
            ((BaseDawerActivity) getActivity()).changeToolbarConfig(R.drawable.ic_arrow_back_24dp,
                    R.string.dykh_create_event);
        }
    }
}
