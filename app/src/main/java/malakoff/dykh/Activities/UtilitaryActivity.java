package malakoff.dykh.Activities;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import malakoff.dykh.Activities.Base.BaseActivity;
import malakoff.dykh.AppApplication.Constants;
import malakoff.dykh.Fragments.Base.BaseFragment;
import malakoff.dykh.Fragments.EventDetailsFragment;
import malakoff.dykh.Fragments.ModifyEventFragment;
import malakoff.dykh.ModelBase.MapRequestObject;
import malakoff.dykh.R;
import malakoff.dykh.Utils.UsefulGenericMethods;

/**
 * Created by vm32776n on 30/11/2016.
 */
public class UtilitaryActivity extends BaseActivity implements OnMapReadyCallback {

    private final static String BUNDLE_KEY_MAP_STATE = "mapData";

    private MapView mapLayout;
    private FloatingActionButton myFAB;
    private GoogleMap map;

    private Boolean fragmentChangeTag = Boolean.FALSE;

    private String currentEventIndex;

    public static void open(Activity activity, String location, String eventId, String title, String theme) {
        if(TextUtils.isEmpty(eventId)) {
            Log.e(UtilitaryActivity.class.getSimpleName(), "eventId = " + eventId);
        }else{

            Intent intent = new Intent(activity, UtilitaryActivity.class);
            intent.putExtra(Constants.DYKH_ID_VALUE, location);
            intent.putExtra(Constants.DYKH_TITLE_EXTRA, title);
            intent.putExtra("THEME_TEST", theme); //TODO TEST
            intent.putExtra(Constants.DYKH_FRAGMENT_SELECTION, eventId);
            activity.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapLayout.onCreate(savedInstanceState != null ? savedInstanceState.getBundle(BUNDLE_KEY_MAP_STATE) : null);
    }

    @Override
    protected void init() {
        super.init();

        mapLayout = (MapView) findViewById(R.id.map_layout);
        myFAB = (FloatingActionButton) findViewById(R.id.fab_editor);

        UsefulGenericMethods.getMyColorFromTheme(this,
                getIntent().getStringExtra("THEME_TEST"), null,
                findViewById(R.id.view_theme_edging));

        MapsInitializer.initialize(this);
        mapLayout.getMapAsync(this);

        ((Toolbar) findViewById(R.id.main_toolbar)).setTitle(getIntent().getStringExtra(Constants.DYKH_TITLE_EXTRA));


        myFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fragmentChangeTag) {
                    onOpenNewFragment(ModifyEventFragment.newInstance(currentEventIndex));
                    myFAB.setImageResource(R.drawable.ic_done_24dp);
                    fragmentChangeTag = Boolean.TRUE;
                } else {
                    onBackPressed();
                }
            }
        });
    }

    private void onOpenNewFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName());

        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void beginTransaction(Bundle savedInstanceState) {
        BaseFragment fragment;

        if (savedInstanceState != null) {

            currentEventIndex = savedInstanceState.getString(Constants.DYKH_FRAGMENT_SELECTION);
            fragment = EventDetailsFragment.newInstance(currentEventIndex);

        } else {
            currentEventIndex =  getIntent().getStringExtra(Constants.DYKH_FRAGMENT_SELECTION);
            fragment = EventDetailsFragment.newInstance(currentEventIndex);
        }

        fragment.setArguments(getIntent().getExtras());

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_coordinator_layout;
    }

    @Override
    protected void resumeHomeFragments() {
        if (fragmentChangeTag) {
            myFAB.setImageResource(R.drawable.ic_open_editor_black_24dp);
            fragmentChangeTag = Boolean.FALSE;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapLayout.onResume();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Bundle mapState = new Bundle();
        mapLayout.onSaveInstanceState(mapState);
        outState.putBundle(BUNDLE_KEY_MAP_STATE, mapState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapLayout.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapLayout.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapLayout.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        setMarkerOnMap();
    }

    private void setMarkerOnMap() {
        final String locationName = getIntent().getStringExtra(Constants.DYKH_ID_VALUE);

        if (Geocoder.isPresent() && !TextUtils.isEmpty(locationName)) {

            new AsyncTask<MapRequestObject, MapRequestObject, MapRequestObject>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                    //TODO SET PROGRESS BAR

                }

                @Override
                protected MapRequestObject doInBackground(MapRequestObject... params) {

                    try {
                        final Geocoder geocoder = new Geocoder(UtilitaryActivity.this);
                        final List<Address> addresses = geocoder.getFromLocationName(locationName, 5);

                        for (final Address a : addresses) {
                            if (a.hasLatitude() && a.hasLongitude()) {

                                return new MapRequestObject(new MarkerOptions()
                                        .position(new LatLng(a.getLatitude(), a.getLongitude())), null, true);
                            }
                        }

                    } catch (final IOException e) {
                        return new MapRequestObject(null, e.getLocalizedMessage(), false);
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(final MapRequestObject mapRequestObject) {
                    super.onPostExecute(mapRequestObject);

                    if (mapRequestObject == null) return;

                    if (mapRequestObject.isRequestSucceeded() && mapRequestObject.getMarkerOptions() != null) {
                        map.addMarker(mapRequestObject.getMarkerOptions());
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(mapRequestObject.getMarkerOptions().getPosition(), 2));
                    } else if (!TextUtils.isEmpty(mapRequestObject.getErrorContent())) {
                        Toast.makeText(getApplicationContext(), mapRequestObject.getErrorContent(), Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute();

        }
    }

}
