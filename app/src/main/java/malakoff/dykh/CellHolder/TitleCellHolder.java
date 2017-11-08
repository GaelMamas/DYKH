package malakoff.dykh.CellHolder;

import android.view.View;
import android.widget.TextView;

import malakoff.dykh.Event.Event;
import malakoff.dykh.R;
import malakoff.dykh.Recycler.ReCellHolder;

/**
 * Created by user on 15/09/2016.
 */

public class TitleCellHolder extends ReCellHolder<Event> {

    private TextView titleView;

    public TitleCellHolder(View view) {
        super(view);
    }

    @Override
    protected void buildHolder(View view) {
        titleView = (TextView) view.findViewById(R.id.event_title);
    }

    @Override
    protected void fillCell() {
        if (data == null) return;

        titleView.setText(data.getSliceTime() + " : " + data.getTitle());

    }

}
