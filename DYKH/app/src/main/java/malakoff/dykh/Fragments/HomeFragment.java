package malakoff.dykh.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import malakoff.dykh.AppApplication.AppApplication;
import malakoff.dykh.AppApplication.Constants;
import malakoff.dykh.CellHolder.TitleCellHolder;
import malakoff.dykh.DesignWidget.ModelBase.TimeClipperButton;
import malakoff.dykh.Event.Event;
import malakoff.dykh.Fragments.Base.InstanceBaseFragement;
import malakoff.dykh.ModelBase.EventsPartition;
import malakoff.dykh.R;
import malakoff.dykh.Recycler.AnimatedReAdapter;
import malakoff.dykh.Recycler.ReAdapterViewProvider;
import malakoff.dykh.Recycler.ReCellHolder;
import malakoff.dykh.Utils.UsefulGenericMethods;

/**
 * Created by user on 27/06/2016.
 */
public class HomeFragment extends InstanceBaseFragement implements View.OnTouchListener {

    private static final int TYPE_FOCUSED_EVENT = 0, TYPE_PREVIOUS_EVENT = 1;
    private View mapLayout;
    private RecyclerView recyclerView;
    private TimeClipperButton timeClipperButton;

    private Event previousAssociateEvent;

    private int currentEraIndex = 0;

    private List<Event> modelData = new ArrayList<>();
    private AnimatedReAdapter<String> adapter;

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

        mapLayout = view.findViewById(R.id.map_layout);
        recyclerView = (RecyclerView) view.findViewById(android.R.id.list);
        timeClipperButton = (TimeClipperButton) view.findViewById(R.id.time_clipper);
    }

    @Override
    protected void populateViews(Bundle savedInstanceState) {
        super.populateViews(savedInstanceState);

        timeClipperButton.setOnTouchListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new AnimatedReAdapter<>();

        adapter.setListCellBuilder(new ReAdapterViewProvider<String>() {
            @Override
            public int getLayoutId(int holderType) {
                switch (holderType) {
                    case TYPE_FOCUSED_EVENT:
                        return R.layout.cell_focused_event;
                    case TYPE_PREVIOUS_EVENT:
                    default:
                        return R.layout.cell_event_title;
                }
            }

            @Override
            public ReCellHolder<String> getHolder(int holderType, View view) {
                switch (holderType) {
                    case TYPE_FOCUSED_EVENT:
                        return new TitleCellHolder(view);
                    case TYPE_PREVIOUS_EVENT:
                    default:
                        return new TitleCellHolder(view);

                }
            }

            @Override
            public void onItemClicked(int position, String data) {

            }

            @Override
            public int getHolderType(int position, String data) {
                return position == 0 ? TYPE_FOCUSED_EVENT : TYPE_PREVIOUS_EVENT;
            }
        });

        adapter.addDatas(Arrays.asList(new String[3]));

        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {

        }
    }

    private void loadData() {
        if (mEventManager.getEvents() == null || mEventManager.getEvents().isEmpty()) {

            EventsPartition bcEventsPartition = UsefulGenericMethods.setDefaultModelData(getActivity().getResources().openRawResource(R.raw.bc_events));
            EventsPartition adEventsPartition = UsefulGenericMethods.setDefaultModelData(getActivity().getResources().openRawResource(R.raw.ad_events));

            modelData.clear();

            if (bcEventsPartition != null && bcEventsPartition.getEvents() != null) {
                modelData.addAll(bcEventsPartition.getEvents());
            }

            if (adEventsPartition != null && adEventsPartition.getEvents() != null) {
                modelData.addAll(adEventsPartition.getEvents());
            }

            mEventManager.loadEvents(modelData);
        }

        mEventManager.readEvents(AppApplication.getUserInfo().getUserId());
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (view.getId()) {
            case R.id.time_clipper:
                timeClipperButton.onTouchEvent(motionEvent);

                currentEraIndex = timeClipperButton.getCurrentEraIndex();

                if(timeClipperButton.isBrutalChangedEra()){
                    previousAssociateEvent = mEventManager.passYearAfterBrutalChange(timeClipperButton.getTextToDisplay());
                }else{
                    previousAssociateEvent = mEventManager.passYear(timeClipperButton.getTextToDisplay(), previousAssociateEvent);
                }

                try {
                    if (previousAssociateEvent != null
                            && !TextUtils.isEmpty(previousAssociateEvent.getTitle())
                            && !previousAssociateEvent.getTitle().contentEquals(adapter.getItem(0))) {
                        adapter.setData(previousAssociateEvent.getTitle());
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                    adapter.setData(previousAssociateEvent.getTitle());
                }

                break;
        }
        return true;
    }

}
