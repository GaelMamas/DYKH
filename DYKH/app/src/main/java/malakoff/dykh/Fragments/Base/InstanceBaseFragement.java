package malakoff.dykh.Fragments.Base;

import android.os.Bundle;
import android.view.View;

import malakoff.dykh.Activities.Base.BaseDawerActivity;
import malakoff.dykh.Event.EventManager;
import malakoff.dykh.Factory.FactoryManagers;
import malakoff.dykh.R;

/**
 * Created by user on 29/06/2016.
 */

public class InstanceBaseFragement extends BaseFragment implements View.OnClickListener {

    protected EventManager mEventManager;

    @Override
    protected void populateViews(Bundle savedInstanceState) {
        mEventManager = FactoryManagers.getmEventManagerInstance();
        reinitToolbar();
    }

    @Override
    protected void assignViews(View view) {
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }


    protected void reinitToolbar(){
        if (getActivity() instanceof BaseDawerActivity) {
            ((BaseDawerActivity) getActivity()).setCurrentFargmentLayoutId(getLayoutId());
            ((BaseDawerActivity) getActivity()).getToolbar().setTitle(R.string.app_name);
            getActivity().supportInvalidateOptionsMenu();
        }
    }

    @Override
    public void onClick(View view) {
    }

    public interface MyUILauncherCallback {
        void onOpenFragment(InstanceBaseFragement fragement);
    }


}
