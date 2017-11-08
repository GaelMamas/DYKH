package malakoff.dykh.Recycler;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import malakoff.dykh.Event.Event;

/**
 * Created by user on 24/06/2016.
 */

public class AnimatedReAdapter<T> extends RecyclerView.Adapter<ReCellHolder<T>> implements View.OnClickListener, View.OnLongClickListener{

    private static final String LOG_TAG = AnimatedReAdapter.class.getSimpleName();

    private ReAdapterViewProvider<T> cellBuilder;

    private List<T> objects;

    public AnimatedReAdapter(){
        this(null);
    }

    public AnimatedReAdapter(List<T> objects) {

        if(objects == null){
            this.objects = new ArrayList<>();
        }else {
            this.objects = new ArrayList<>(objects);
        }
    }

    @Override
    public ReCellHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = cellBuilder.getLayoutId(viewType);

        View view = LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);

        view.setOnClickListener(this);
        view.setOnLongClickListener(this);

        ReCellHolder<T> holder = cellBuilder.getHolder(viewType, view);
        view.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(ReCellHolder<T> holder, int position) {
        T data = getItem(position);
        holder.initData(data, position);
        holder.fillCell();
    }

    @Override
    public int getItemViewType(int position) {
        return cellBuilder.getHolderType(position, getItem(position));
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }
    public T getItem(int position) {
        return objects.get(position);
    }

    public void setListCellBuilder(ReAdapterViewProvider<T> adapterCellBuilder) {
        this.cellBuilder = adapterCellBuilder;
    }

    @Override
    public void onClick(View view) {
        Object tag = view.getTag();
        if (tag instanceof ReCellHolder) {
            ReCellHolder<T> holder = (ReCellHolder) tag;
            cellBuilder.onItemClicked(holder.position, holder.data);
        } else {
            Log.w(LOG_TAG, "onClick on cell, but it has no CellHolder as tag: " + tag);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        Object tag = view.getTag();
        if(tag instanceof ReCellHolder){
            ReCellHolder<T> holder = (ReCellHolder) tag;
            return cellBuilder.onItemLongClicked(holder.position, holder.data);
        }else{
            Log.w(LOG_TAG, "onLongClick on cell, but it has no CellHolder as tag: " + tag);
        }
        return false;
    }


    public void addDatas(List<T> modelData) {
        int newPosition= objects.size();

        objects.addAll(modelData);
        notifyItemRangeInserted(newPosition, modelData.size());
    }

    public void setData(T data){

        objects.set(2, objects.get(1));
        objects.set(1, objects.get(0));
        objects.set(0, data);

        notifyDataSetChanged();
    }

    public void setFirstData(T data) {
        objects.set(0,data);
        notifyDataSetChanged();
    }

    public void setUpNewData(List<T> newData){
        objects.clear();
        objects.addAll(newData);
        notifyDataSetChanged();
    }
}
