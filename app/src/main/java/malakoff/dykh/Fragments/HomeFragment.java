package malakoff.dykh.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import malakoff.dykh.Activities.MainActivity;
import malakoff.dykh.Activities.UtilitaryActivity;
import malakoff.dykh.AppApplication.AppApplication;
import malakoff.dykh.AppApplication.Constants;
import malakoff.dykh.CellHolder.TitleCellHolder;
import malakoff.dykh.DesignWidget.ModelBase.TimeClipperButton;
import malakoff.dykh.Event.Event;
import malakoff.dykh.Fragments.Base.InstanceBaseFragement;
import malakoff.dykh.Network.GsonArrayRequest;
import malakoff.dykh.Network.MySingleton;
import malakoff.dykh.R;
import malakoff.dykh.Recycler.AnimatedReAdapter;
import malakoff.dykh.Recycler.ReAdapterViewProvider;
import malakoff.dykh.Recycler.ReCellHolder;
import malakoff.dykh.Utils.DykhGoogleMapManager;

/**
 * Created by user on 27/06/2016.
 */
public class HomeFragment extends InstanceBaseFragement implements View.OnTouchListener, OnMapReadyCallback {

    private static final int CURRENT_EVENT = 0, LATTERLY_PAST_EVENT = 1, PAST_EVENT = 2;

    private DykhGoogleMapManager mapManager;
    private GoogleMap map;

    private MapView mapLayout;
    private RecyclerView recyclerView;
    private TimeClipperButton timeClipperButton;

    private ThreadPoolExecutor poolExecutor;

    private Event previousAssociateEvent;

    private int currentEraIndex = 0;

    private List<Event> modelData = new ArrayList<>();
    private AnimatedReAdapter<Event> adapter;

    public static HomeFragment newInstance(long selection) {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        args.putLong(Constants.DYKH_FRAGMENT_SELECTION, selection);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    protected void assignViews(View view) {
        super.assignViews(view);

        mapLayout = (MapView) view.findViewById(R.id.map_layout);
        recyclerView = (RecyclerView) view.findViewById(android.R.id.list);
        timeClipperButton = (TimeClipperButton) view.findViewById(R.id.time_clipper);

        serverBugNotifyer = (TextView) view.findViewById(R.id.text_server_bugs_notifyer);
    }

    @Override
    protected void populateViews(Bundle savedInstanceState) {
        super.populateViews(savedInstanceState);

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setNavigationBarIcon();
            ((MainActivity) getActivity()).enableDrawerOpening(true);
        }

        MapsInitializer.initialize(getContext());
        mapLayout.getMapAsync(this);


        timeClipperButton.setOnTouchListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new AnimatedReAdapter<>();

        adapter.setListCellBuilder(getListCellBuilder());

        adapter.addDatas(Arrays.asList(new Event[3]));

        recyclerView.setAdapter(adapter);

        loadData();

        int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
        poolExecutor = new ThreadPoolExecutor(
                NUMBER_OF_CORES * 2,
                NUMBER_OF_CORES * 2,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>()
        );

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mapLayout.onCreate(savedInstanceState);

        for (int i = 0; i < modelData.size(); i++) {
            adapter.setData(modelData.get(i));
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mapLayout.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();

        mapLayout.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapLayout.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapLayout.onStop();
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
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
    }

    private void loadData() {
        if (mEventManager.getEvents() == null || mEventManager.getEvents().isEmpty()) {

            MySingleton
                    .getInstance(getContext())
                    .addToRequestQueue(
                            new GsonArrayRequest(
                                    Request.Method.GET,
                                    Constants.SERVER_URL_ROOT + Constants.SERVER_URL_EVENT_ROUT + "/getEvents",
                                    JSONArray.class,
                                    new HashMap<String, String>(),
                                    new Response.Listener<JSONArray>() {
                                        @Override
                                        public void onResponse(JSONArray response) {
                                            modelData.clear();
                                            List<Event> events = new Gson().fromJson(String.valueOf(response),
                                                    new TypeToken<List<Event>>() {
                                                    }.getType());

                                            if (events != null && events.size() > 0) {
                                                //modelData.addAll(events);

                                                mEventManager.loadEvents(events);

                                                timeClipperButton.setDisplay(true);
                                            } else {
                                                timeClipperButton.setDisplay(false);
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    serverBugNotifyer.setVisibility(View.VISIBLE);
                                    serverBugNotifyer.setText(error.getLocalizedMessage());

                                    timeClipperButton.setVisibility(View.INVISIBLE);

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            serverBugNotifyer.setVisibility(View.GONE);
                                        }
                                    }, 2000);
                                }
                            })
                    );
        }
    }


    @Override
    public boolean onTouch(View view, final MotionEvent motionEvent) {

        switch (view.getId()) {
            case R.id.time_clipper:

                timeClipperButton.onTouchEvent(motionEvent);
                currentEraIndex = timeClipperButton.getCurrentEraIndex();

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        poolExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                setPreviousAssociateEvent(processEventPulling(timeClipperButton.isBrutalChangedEra(), timeClipperButton.getTextToDisplay()));
                            }
                        });

                        processEventDisplay(getPreviousAssociateEvent());

                        break;


                }

                break;
        }

        return true;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        mapManager = new DykhGoogleMapManager(getContext(), map, serverBugNotifyer);
    }

    private ReAdapterViewProvider<Event> getListCellBuilder() {

        return new ReAdapterViewProvider<Event>() {
            @Override
            public int getLayoutId(int holderType) {
                switch (holderType) {
                    case CURRENT_EVENT:
                        return R.layout.cell_event_few_details_current;
                    case LATTERLY_PAST_EVENT:
                        return R.layout.cell_event_few_details_latterly;
                    case PAST_EVENT:
                    default:
                        return R.layout.cell_event_few_details_past;
                }
            }

            @Override
            public ReCellHolder<Event> getHolder(int holderType, View view) {
                switch (holderType) {
                    case CURRENT_EVENT:
                        return new TitleCellHolder(view);
                    case LATTERLY_PAST_EVENT:
                        return new TitleCellHolder(view);
                    case PAST_EVENT:
                    default:
                        return new TitleCellHolder(view);

                }
            }

            @Override
            public void onItemClicked(int position, Event data) {
                if (position == 0 && data != null && !TextUtils.isEmpty(data.getTitle())) {
                    map.setMinZoomPreference(5.0f);
                    map.setMaxZoomPreference(14.0f);
                    map.moveCamera(CameraUpdateFactory.zoomIn());
                    map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                }
            }

            @Override
            public boolean onItemLongClicked(int position, final Event data) {
                if (position == 0 && !TextUtils.isEmpty(data.getTitle())) {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);

                    alertDialog.setTitle(data.getTitle())
                            .setMessage(!TextUtils.isEmpty(data.getStory()) ? data.getStory() : data.getTitle())
                            .setNeutralButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setPositiveButton(R.string.more_button, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    UtilitaryActivity.open(getActivity(), data.getLocation(), data.getEventId(),
                                            data.getTitle(), data.getTheme(), AppApplication.getUserInfo().getUserId()
                                                .contentEquals(data.getUserId()));
                                }
                            });

                    alertDialog.show();

                    return true;
                }

                return false;
            }

            @Override
            public int getHolderType(int position, Event data) {
                switch (position) {
                    case 0:
                        return CURRENT_EVENT;

                    case 1:
                        return LATTERLY_PAST_EVENT;

                    case 2:
                    default:
                        return PAST_EVENT;
                }
            }
        };
    }

    private Event processEventPulling(boolean isbrutalChangedEra, String textToDisplay) {

        if (isbrutalChangedEra) {
            return mEventManager.passYearAfterBrutalChange(textToDisplay);
        }
        return mEventManager.passYear(textToDisplay, getPreviousAssociateEvent());
    }

    private void processEventDisplay(final Event previousAssociateEvent) {
        if (previousAssociateEvent != null
                && !TextUtils.isEmpty(previousAssociateEvent.getTitle())) {

            if (adapter.getItem(0) == null) {

                modelData.add(previousAssociateEvent);
                adapter.setFirstData(previousAssociateEvent);

                mapManager.markLocation(previousAssociateEvent);

            } else if (!adapter.getItem(0).getTitle().contains(previousAssociateEvent.getTitle())) {

                modelData.add(previousAssociateEvent);
                adapter.setData(previousAssociateEvent);

                mapManager.markLocation(previousAssociateEvent);
            }
        }
    }

    public synchronized Event getPreviousAssociateEvent() {
        return previousAssociateEvent;
    }

    public synchronized void setPreviousAssociateEvent(Event previousAssociateEvent) {
        this.previousAssociateEvent = previousAssociateEvent;
    }
}
