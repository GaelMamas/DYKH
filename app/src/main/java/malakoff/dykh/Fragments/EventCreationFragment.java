package malakoff.dykh.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import malakoff.dykh.Activities.Base.BaseDawerActivity;
import malakoff.dykh.AppApplication.Constants;
import malakoff.dykh.Event.Event;
import malakoff.dykh.Fragments.Base.WriteEventBaseFragment;
import malakoff.dykh.Network.MySingleton;
import malakoff.dykh.R;

/**
 * Created by user on 21/06/2016.
 */

public class EventCreationFragment extends WriteEventBaseFragment {

    RequestQueue queue;

    public static EventCreationFragment newInstance() {
        return new EventCreationFragment();
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

                /*JSONArray jsonArray = new JSONArray();
                JSONObject params;

                for (Event event : mEventManager.getEvents()) {

                    try {
                        params = new JSONObject();

                        params.put("_id", "Admin");
                        params.put("sliceTime", event.getSliceTime());
                        params.put("location", event.getLocation());
                        params.put("title", event.getTitle());
                        params.put("story", event.getStory());
                        params.put("theme", event.getTheme());
                        params.put("isValidate", String.valueOf(event.getIsValidate()));
                        params.put("locationModernCalling", event.getLocationModernCalling());
                        params.put("longitude", String.valueOf(event.getLongitude()));
                        params.put("latitude", String.valueOf(event.getLatitude()));

                        jsonArray.put(params);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                JsonArrayRequest jsonObjectGsonRequest = new JsonArrayRequest(
                        Request.Method.POST,
                        Constants.SERVER_URL_ROOT + "/posttestarray",
                        jsonArray,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                if (getView() != null) {
                                    Snackbar.make(getView(), "Response is: " + response.length(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getContext(), "Response is: " + response.length(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "That didn't work!", Toast.LENGTH_SHORT).show();
                    }
                });*/

                /*JsonObjectRequest jsonObjectGsonRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        Constants.SERVER_URL_ROOT + "/posttest",
                        new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if(getView() != null) {
                                    Snackbar.make(getView(), "Response is: " + response.toString(), Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(getContext(), "Response is: " + response.toString(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),"That didn't work!", Toast.LENGTH_SHORT).show();
                    }
                });*/

                /*Map<String, String> deleteParams = new HashMap<>();

                deleteParams.put("_id", "59808d2939f5b8144041deda");

                JsonObjectRequest jsonObjectGsonRequest = new JsonObjectRequest(
                        Request.Method.DELETE,
                        Constants.SERVER_URL_ROOT + "/deletetest",
                        new JSONObject(deleteParams),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(getContext(), "Response is: " + response.toString(), Toast.LENGTH_LONG).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "That didn't work!", Toast.LENGTH_SHORT).show();
                    }
                });*/

                /*GsonArrayRequest jsonObjectGsonRequest = new GsonArrayRequest(
                        Request.Method.GET,
                        Constants.SERVER_URL_ROOT + "/getEvents",
                        JSONArray.class,
                        params,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    Toast.makeText(getContext(),"Response is: "+ response.get(0).toString(), Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),"That didn't work!", Toast.LENGTH_SHORT).show();
                    }
                });*/

                // Add the request to the RequestQueue.

                //MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectGsonRequest);

                /*String title = titleEditText.getText().toString(),
                        historicLocation = historicLocationEditText.getText().toString(),
                        story = storyEditText.getText().toString();


                if (TextUtils.isEmpty(title)
                        || TextUtils.isEmpty(historicLocation)
                        || TextUtils.isEmpty(story)) {
                    titleEditText.setError("Need to be filled");
                    historicLocationEditText.setError("Need to be filled");
                    storyEditText.setError("Need to be filled");

                } else if (TextUtils.isEmpty(selectedTheme)
                        || TextUtils.isEmpty(selectedTodayLocaction)) {

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

                } else if (getView() != null) {*/



                    /*Snackbar.make(getView(),
                            mEventManager.fillForm2CreateEvent(
                                    title,
                                    selectedTheme,
                                    newEventFinalDate[0] + "-" + newEventFinalDate[1] + "-" + newEventFinalDate[2] + " " + selectedBCAD,
                                    12,
                                    selectedTodayLocaction,
                                    historicLocation,
                                    story) ?
                                    "Published with succes" : "Not have been published", Snackbar.LENGTH_SHORT).show();*/
                //}
                break;
        }
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
