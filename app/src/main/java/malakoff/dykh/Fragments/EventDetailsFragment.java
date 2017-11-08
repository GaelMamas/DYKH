package malakoff.dykh.Fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import malakoff.dykh.Activities.Base.BaseDawerActivity;
import malakoff.dykh.AppApplication.Constants;
import malakoff.dykh.Event.Event;
import malakoff.dykh.Fragments.Base.InstanceBaseFragement;
import malakoff.dykh.R;
import malakoff.dykh.Utils.UsefulGenericMethods;

/**
 * Created by user on 24/06/2016.
 */

public class EventDetailsFragment extends InstanceBaseFragement {

    private Event currentEvent;

    private TextView themeView, sliceTimeView, locationView, storyView;

    public static EventDetailsFragment newInstance(String selection) {

        Bundle args = new Bundle();

        EventDetailsFragment fragment = new EventDetailsFragment();

        args.putString(Constants.DYKH_FRAGMENT_SELECTION, selection);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void assignViews(View view) {
        themeView = (TextView) view.findViewById(R.id.event_theme);
        sliceTimeView = (TextView) view.findViewById(R.id.event_slice_time);
        locationView = (TextView) view.findViewById(R.id.event_location);
        storyView = (TextView) view.findViewById(R.id.event_story);
    }

    @Override
    protected void populateViews(Bundle savedInstanceState) {
        super.populateViews(savedInstanceState);

        currentEvent = mEventManager.readSpecificEvent(getArguments().getString(Constants.DYKH_FRAGMENT_SELECTION));

        if (currentEvent != null) {
            themeView.setText(currentEvent.getTheme());
            sliceTimeView.setText(currentEvent.getSliceTime());
            locationView.setText(TextUtils.isEmpty(currentEvent.getLocation()) ? getString(R.string.event_fill_blank_location) : currentEvent.getLocation());
            storyView.setText(currentEvent.getStory());

            UsefulGenericMethods.getMyColorFromTheme(getContext(), currentEvent.getTheme(), themeView, sliceTimeView);

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
            ((BaseDawerActivity) getActivity()).setDefaultDrawable();
            ((BaseDawerActivity) getActivity()).changeToolbarConfig(R.drawable.ic_arrow_back_24dp,
                    currentEvent != null && !TextUtils.isEmpty(currentEvent.getTitle()) ?
                            currentEvent.getTitle() : getString(R.string.app_name));
        }
    }
}
