package malakoff.dykh.Utils;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import malakoff.dykh.Activities.UtilitaryActivity;
import malakoff.dykh.Event.Event;
import malakoff.dykh.ModelBase.MapRequestObject;


/**
 * Created by VM32776N on 27/01/2017.
 */

public class DykhGoogleMapManager {

    private Context context;
    private GoogleMap map;
    private TextView notifyer;
    private Handler handler;


    public DykhGoogleMapManager(Context context, GoogleMap map, TextView notifyer) {
        this.context = context;
        this.map = map;
        this.notifyer = notifyer;

        handler = new Handler();
    }


    public void markLocation(final Event previousAssociateEvent) {

        if (previousAssociateEvent == null) return;

        /*String eventLocation = !TextUtils.isEmpty(previousAssociateEvent.getLocationModernCalling()) ?
                    previousAssociateEvent.getLocationModernCalling() :
                    !TextUtils.isEmpty(previousAssociateEvent.getLocation()) ?
                            previousAssociateEvent.getLocation() :
                            "";*/

        new AsyncTask<MapRequestObject, MapRequestObject, MapRequestObject>() {
            @Override
            protected MapRequestObject doInBackground(MapRequestObject... params) {

                String eventLocation = previousAssociateEvent.getLocation();

                if (Geocoder.isPresent() && !TextUtils.isEmpty(eventLocation)) {
                    try {
                        final Geocoder geocoder = new Geocoder(context);
                        List<Address> addresses = geocoder.getFromLocationName(eventLocation, 5);

                        for (Address a : addresses) {
                            if (a.hasLatitude() && a.hasLongitude()) {
                                LatLng location = new LatLng(a.getLatitude(), a.getLongitude());
                                JSONObject jo = new JSONObject();
                                try {
                                    jo.put("eventId", previousAssociateEvent.getEventId())
                                            .put("title", previousAssociateEvent.getTitle())
                                            .put("theme", previousAssociateEvent.getTheme())
                                            .put("location", previousAssociateEvent.getLocation());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                return new MapRequestObject(new MarkerOptions().position(location)
                                        .snippet(jo.toString()), null, true);

                            }
                        }

                    } catch (IOException e) {
                        return new MapRequestObject(null, e.getLocalizedMessage(), false);
                    }
                }

                return null;
            }

            @Override
            protected void onPostExecute(MapRequestObject mapRequestObject) {
                super.onPostExecute(mapRequestObject);

                interactWithMap(mapRequestObject);
            }
        }.execute();

    }

    private void interactWithMap(MapRequestObject mapRequestObject) {
        if (mapRequestObject == null) return;

        if (mapRequestObject.isRequestSucceeded() && mapRequestObject.getMarkerOptions() != null) {
            map.addMarker(mapRequestObject.getMarkerOptions());

            map.moveCamera(CameraUpdateFactory.newLatLng(mapRequestObject.getMarkerOptions().getPosition()));

            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    try {
                        JSONObject json = new JSONObject(marker.getSnippet());

                        String title = json.getString("title");

                        if (!TextUtils.isEmpty(title)) {
                            UtilitaryActivity.open((Activity) context, json.getString("location"),
                                    json.getString("eventId"),
                                    title,
                                    json.getString("theme"));

                            return true;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return false;
                }
            });
        } else if (!TextUtils.isEmpty(mapRequestObject.getErrorContent())) {
            notifyer.setVisibility(View.VISIBLE);
            notifyer.setText(mapRequestObject.getErrorContent());

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    notifyer.setVisibility(View.GONE);
                }
            }, 2000);
        }
    }
}
