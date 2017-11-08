package malakoff.dykh.Recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import malakoff.dykh.R;

/**
 * Created by user on 06/09/2016.
 */
public class TripleEventTitleAdapter extends RecyclerView.Adapter<TripleEventTitleAdapter.ViewHolder> {

    private List<String> mDataset;

    public TripleEventTitleAdapter(List<String> tripletEventTitles) {
        this.mDataset = tripletEventTitles;

    }

    @Override
    public TripleEventTitleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_event_title, parent, false);
        return new ViewHolder((TextView) v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }

    }

}
