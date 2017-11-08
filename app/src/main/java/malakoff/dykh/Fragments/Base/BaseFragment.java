package malakoff.dykh.Fragments.Base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by user on 23/06/2016.
 */

public abstract class BaseFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(getLayoutId(), container, false);
        assignViews(view);
        populateViews(savedInstanceState);

        return view;
    }

    protected abstract void assignViews(View view);

    protected abstract void populateViews(Bundle savedInstanceState);

    protected abstract int getLayoutId();


}
