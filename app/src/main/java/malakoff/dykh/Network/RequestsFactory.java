package malakoff.dykh.Network;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import malakoff.dykh.AppApplication.Constants;
import malakoff.dykh.Event.Event;
import malakoff.dykh.Factory.FactoryManagers;
import malakoff.dykh.Utils.UsefulGenericMethods;

/**
 * Created by gael on 22/12/2017.
 */

public class RequestsFactory {


    public static JsonArrayRequest postManyEvents(final Context context, final View snackBarEnchorView, List<Event> events) {

        JSONArray jsonArray = new JSONArray();

        for (Event event : events) {
            try {
                jsonArray.put(UsefulGenericMethods.getEventJSONObject(event));
            } catch (NullPointerException e) {
                Log.e(context.getClass().getSimpleName(), e.getMessage());
            }

        }

        return new JsonArrayRequest(
                Request.Method.POST,
                Constants.SERVER_URL_ROOT + Constants.SERVER_URL_EVENT_ROUT + "/postEventsArray",
                jsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (snackBarEnchorView != null) {
                            Snackbar.make(snackBarEnchorView, "Response is: " + response.length(), Toast.LENGTH_LONG).show();
                            Log.d(context.getClass().getSimpleName(), "Posts Success " + response.length());
                        } else {
                            Toast.makeText(context, "Response is: " + response.length(), Toast.LENGTH_LONG).show();
                            Log.d(context.getClass().getSimpleName(), "Posts Failure " + response.length());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "That didn't work!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static JsonObjectRequest postAnEvent(final Context context, final View snackBarEnchorView, Event event) {

        return new JsonObjectRequest(
                Request.Method.POST,
                Constants.SERVER_URL_ROOT + Constants.SERVER_URL_EVENT_ROUT + "/postAnEvent",
                UsefulGenericMethods.getEventJSONObject(event),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        Event event = new Gson().fromJson(String.valueOf(response),
                                new TypeToken<Event>() {
                                }.getType());

                        if (event != null && snackBarEnchorView != null) {

                            Snackbar.make(snackBarEnchorView,
                                    FactoryManagers.getmEventManagerInstance().fillForm2CreateEvent(event) ?
                                            "Published with succes" : "Not have been published", Snackbar.LENGTH_SHORT).show();

                            Log.d(context.getClass().getSimpleName(), "Post Success " + response.length());

                        } else {

                            Toast.makeText(context, "Response is: " + response.toString(), Toast.LENGTH_LONG).show();

                            Log.d(context.getClass().getSimpleName(), "Post Failure " + response.length());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "That didn't work!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static GsonArrayRequest getSpecificEvents(final Context context, String userId) {

        Map<String, String> params = new HashMap<>();

        params.put("userId", userId);

        return new GsonArrayRequest(
                Request.Method.GET,
                Constants.SERVER_URL_ROOT + Constants.SERVER_URL_EVENT_ROUT + "/getEvents",
                JSONArray.class,
                params,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Toast.makeText(context, "Response is: " + response.get(0).toString(), Toast.LENGTH_LONG).show();

                            Log.d(context.getClass().getSimpleName(), "Get Event Success " + response.get(0).toString());
                        } catch (JSONException e) {
                            e.printStackTrace();

                            Log.d(context.getClass().getSimpleName(), "Get Event Failure ");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "That didn't work!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static JsonObjectRequest deleteAnEvent(final Context context, String eventId) {


        Map<String, String> deleteParams = new HashMap<>();

        deleteParams.put("_id", eventId);

        return new JsonObjectRequest(
                Request.Method.DELETE,
                Constants.SERVER_URL_ROOT + Constants.SERVER_URL_EVENT_ROUT + "/deleteAnEvent",
                new JSONObject(deleteParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (response != null) {

                            Toast.makeText(context, "Response is: " + response.toString(), Toast.LENGTH_LONG).show();

                            Log.d(context.getClass().getSimpleName(), "Delete Success " + response.toString());

                        } else {

                            Log.d(context.getClass().getSimpleName(), "Delete Failure " + response.toString());

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "That didn't work!", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public static JsonArrayRequest deleteManyEvents(final Context context, JSONArray eventIdsArray) {


        return new JsonArrayRequest(
                Request.Method.DELETE,
                Constants.SERVER_URL_ROOT + Constants.SERVER_URL_EVENT_ROUT + "/deleteManyEvents",
                eventIdsArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response != null) {

                            Toast.makeText(context, "Response is: " + response.toString(), Toast.LENGTH_LONG).show();

                            Log.d(context.getClass().getSimpleName(), "Delete Success " + response.toString());

                        } else {

                            Log.d(context.getClass().getSimpleName(), "Delete Failure " + response.toString());

                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "That didn't work!", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
