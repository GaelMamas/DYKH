package malakoff.dykh.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import malakoff.dykh.Activities.Base.BaseDawerActivity;
import malakoff.dykh.Activities.MainActivity;
import malakoff.dykh.Activities.MySpaceActivity;
import malakoff.dykh.AppApplication.AppApplication;
import malakoff.dykh.AppApplication.Constants;
import malakoff.dykh.CellHolder.EventCellHolder;
import malakoff.dykh.Event.Event;
import malakoff.dykh.Event.EventManager;
import malakoff.dykh.Factory.FactoryManagers;
import malakoff.dykh.Fragments.Base.RecyclerBaseFragment;
import malakoff.dykh.ModelBase.EventsPartition;
import malakoff.dykh.R;
import malakoff.dykh.Recycler.ReAdapterViewProvider;
import malakoff.dykh.Recycler.ReCellHolder;
import malakoff.dykh.Utils.UsefulGenericMethods;

/**
 * Created by user on 23/06/2016.
 */

public class EventsFeedBaseFragment extends RecyclerBaseFragment<Event> {

    private List<Event> modelData = new ArrayList<>();

    @Override
    protected void assignViews(View view) {
        super.assignViews(view);
    }

    @Override
    protected void populateViews(Bundle savedInstanceState) {
        super.populateViews(savedInstanceState);

        if(getActivity() instanceof BaseDawerActivity){
            ((BaseDawerActivity) getActivity()).setNavigationBarIcon();
            ((BaseDawerActivity) getActivity()).enableDrawerOpening(true);
        }
    }

    @Override
    protected void loadQuery() {
        if (mEventManager.getEvents() == null || mEventManager.getEvents().isEmpty()) {

            EventsPartition bcEventsPartition = UsefulGenericMethods.setDefaultModelData(getActivity().getResources().openRawResource(R.raw.bc_events));
            EventsPartition adEventsPartition = UsefulGenericMethods.setDefaultModelData(getActivity().getResources().openRawResource(R.raw.ad_events));

            modelData.clear();

            if (bcEventsPartition != null && bcEventsPartition.getEvents() != null ) {
                modelData.addAll(bcEventsPartition.getEvents());
            }

            if (adEventsPartition != null && adEventsPartition.getEvents() != null) {
                modelData.addAll(adEventsPartition.getEvents());
            }

            mEventManager.loadEvents(modelData);
        }

        mEventManager.readEvents(AppApplication.getUserInfo().getUserId());
        adapter.addDatas(mEventManager.getEvents());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadQuery();
    }

    @Override
    protected ReAdapterViewProvider<Event> getAdapterCellBuilder() {
        return new ReAdapterViewProvider<Event>() {
            @Override
            public int getLayoutId(int holderType) {
                return R.layout.cell_summary_event;
            }

            @Override
            public ReCellHolder<Event> getHolder(int holderType, View view) {
                return new EventCellHolder(view);
            }

            @Override
            public void onItemClicked(int position, Event data) {
                if(getActivity() instanceof BaseDawerActivity){
                    ((BaseDawerActivity) getActivity()).setDefaultDrawable();
                    ((BaseDawerActivity) getActivity()).onOpenFragment(EventDetailsFragment.newInstance(data.getEventId()));
                }

            }
        };
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_events_feed;
    }

    public static EventsFeedBaseFragment newInstance(long selection) {

        Bundle args = new Bundle();

        EventsFeedBaseFragment fragment = new EventsFeedBaseFragment();
        args.putLong(Constants.DYKH_FRAGMENT_SELECTION, selection);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()){


        }
    }

    private Event createTestEvent(int index) {

        Event event = new Event();
        event.setUserId(AppApplication.getUserInfo().getUserId());
        event.setEventId(index);
        event.setTitle("bonjour " + index);
        event.setTheme("salut");
        event.setLocation("france");
        event.setSliceTime("500-7500");
        event.setStory("Dans le noir de la nuit et le bruit de l'enclune, nous étions dans la veranda");

        return event;

    }




}
