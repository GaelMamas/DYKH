package malakoff.dykh.Recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by user on 26/06/2016.
 */
public abstract class ReCellHolder<T> extends RecyclerView.ViewHolder{

    protected Context context;
    protected T data;
    protected int position;
    private Object tag;

    protected OnItemChildClickListener onItemChildClickListener = null;

    public abstract class OnItemChildClickListener {
        public abstract void onItemChildClicked(int position, T data, View view);
    }

    public void setOnItemChildClickListener(OnItemChildClickListener listener) {
        onItemChildClickListener = listener;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public ReCellHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        buildHolder(itemView);
    }

    public void initData(T data, int position) {
        this.data = data;
        this.position = position;
    }

    protected abstract void buildHolder(View view);

    protected abstract void fillCell();
}
