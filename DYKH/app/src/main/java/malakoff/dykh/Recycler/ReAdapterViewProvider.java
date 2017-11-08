package malakoff.dykh.Recycler;

import android.view.View;

/**
 * Created by user on 26/06/2016.
 */
public abstract class ReAdapterViewProvider<T> {

    public abstract int getLayoutId(int holderType);

    public abstract ReCellHolder<T> getHolder(int holderType, View view);

    public int getHolderType(int position, T data) {
        return 0;
    }

    public abstract void onItemClicked(int position, T data);

    public void loadNextPage() {
    }
}
