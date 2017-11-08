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
 * Created by user on 12/07/2016.
 */
public class ModifyEventFragment extends InstanceBaseFragement{

    private Event currentEvent;

    private TextView titleView, themeView, sliceTimeView, locationView, storyView;

    @Override
    protected void assignViews(View view) {
        super.assignViews(view);

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

        if(getActivity() instanceof BaseDawerActivity){
            ((BaseDawerActivity) getActivity()).changeToolbarConfig(android.R.drawable.ic_input_get,
                    currentEvent.getTitle());
        }

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
        return R.layout.fragment_modify_event;
    }

    public static ModifyEventFragment newInstance(long eventId) {

        Bundle args = new Bundle();

        ModifyEventFragment fragment = new ModifyEventFragment();
        args.putLong(Constants.DYKH_FRAGMENT_SELECTION, eventId);
        fragment.setArguments(args);
        return fragment;
    }
}
