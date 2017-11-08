package malakoff.dykh.Fragments;

import android.os.Bundle;
import android.view.View;

import malakoff.dykh.AppApplication.AppApplication;
import malakoff.dykh.Event.EventManager;
import malakoff.dykh.Factory.FactoryManagers;
import malakoff.dykh.Fragments.Base.BaseFragment;
import malakoff.dykh.Fragments.Base.InstanceBaseFragement;

/**
 * Created by user on 21/06/2016.
 */

public class EventCreationFragment extends InstanceBaseFragement implements View.OnClickListener {



    @Override
    protected void populateViews(Bundle savedInstanceState) {
    }

    @Override
    protected void assignViews(View view) {
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case android.R.id.button1:

                mEventManager.fillForm2CreateEvent(
                        view.findViewById(android.R.id.text1).toString(),
                        view.findViewById(android.R.id.text1).toString(),
                        view.findViewById(android.R.id.text1).toString(),
                        AppApplication.getUserInfo().getUserId(),
                        view.findViewById(android.R.id.text1).toString(),
                        view.findViewById(android.R.id.text1).toString()
                );
                break;
        }
    }
}
