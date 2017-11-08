package malakoff.dykh.CellHolder;

import android.view.View;
import android.widget.TextView;

import malakoff.dykh.Event.Event;
import malakoff.dykh.R;
import malakoff.dykh.Recycler.ReCellHolder;

/**
 * Created by user on 05/07/2016.
 */
public class EventCellHolder extends ReCellHolder<Event> {

    private TextView titleView, themeView, sliceTimeView, locationView, storyView;

    public EventCellHolder(View view) {
        super(view);
    }

    @Override
    protected void buildHolder(View view) {
        titleView = (TextView)view.findViewById(R.id.event_title);
        themeView = (TextView)view.findViewById(R.id.event_theme);
        sliceTimeView = (TextView)view.findViewById(R.id.event_slice_time);
        locationView = (TextView)view.findViewById(R.id.event_location);
        storyView = (TextView)view.findViewById(R.id.event_story);
    }

    @Override
    protected void fillCell() {
        if(data == null)return;

        titleView.setText(data.getTitle());
        themeView.setText(data.getTheme());
        sliceTimeView.setText(data.getSliceTime());
        locationView.setText(data.getLocation());
        storyView.setText(data.getStory());
    }
}
