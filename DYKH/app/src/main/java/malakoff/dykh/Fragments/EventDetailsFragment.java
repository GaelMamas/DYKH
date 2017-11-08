package malakoff.dykh.Fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import malakoff.dykh.Activities.Base.BaseDawerActivity;
import malakoff.dykh.AppApplication.Constants;
import malakoff.dykh.Event.Event;
import malakoff.dykh.Fragments.Base.InstanceBaseFragement;
import malakoff.dykh.R;

/**
 * Created by user on 24/06/2016.
 */

public class EventDetailsFragment extends InstanceBaseFragement implements View.OnClickListener {

    private Event currentEvent;

    private TextView titleView, themeView, sliceTimeView, locationView, storyView;

    public static EventDetailsFragment newInstance(long selection) {

        Bundle args = new Bundle();

        EventDetailsFragment fragment = new EventDetailsFragment();
        
        args.putLong(Constants.DYKH_FRAGMENT_SELECTION, selection);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void assignViews(View view) {
        titleView = (TextView) view.findViewById(R.id.event_title);
        themeView = (TextView) view.findViewById(R.id.event_theme);
        sliceTimeView = (TextView) view.findViewById(R.id.event_slice_time);
        locationView = (TextView) view.findViewById(R.id.event_location);
        storyView = (TextView) view.findViewById(R.id.event_story);
    }

    @Override
    protected void populateViews(Bundle savedInstanceState) {
        super.populateViews(savedInstanceState);

        currentEvent = mEventManager.readSpecificEvent(getArguments().getLong(Constants.DYKH_FRAGMENT_SELECTION));

        if (currentEvent != null) {
            titleView.setText(currentEvent.getTitle());
            themeView.setText(currentEvent.getTheme());
            sliceTimeView.setText(currentEvent.getSliceTime());
            locationView.setText(currentEvent.getLocation());
            storyView.setText(currentEvent.getStory());
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_event_details;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case android.R.id.button1:
                break;
        }
    }

    @Override
    protected void reinitToolbar() {
        super.reinitToolbar();
        if (getActivity() instanceof BaseDawerActivity) {
            ((BaseDawerActivity) getActivity()).changeToolbarConfig(android.R.drawable.ic_input_get,
                    R.string.exemple_activity_title);
        }
    }
}
