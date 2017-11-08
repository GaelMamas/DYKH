package malakoff.dykh.Fragments;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import malakoff.dykh.Activities.Base.BaseDawerActivity;
import malakoff.dykh.Activities.MainActivity;
import malakoff.dykh.Activities.MySpaceActivity;
import malakoff.dykh.Fragments.Base.BaseFragment;
import malakoff.dykh.R;

/**
 * Created by user on 27/06/2016.
 */
public class NavigationFragment extends BaseFragment implements View.OnClickListener {

    private TextView homeButton, mySpaceButton;

    @Override
    protected void populateViews(Bundle savedInstanceState) {
        homeButton.setOnClickListener(this);
        mySpaceButton.setOnClickListener(this);
    }

    @Override
    protected void assignViews(View view) {
        homeButton = (TextView)view.findViewById(R.id.navigation_home_button);
        mySpaceButton = (TextView)view.findViewById(R.id.navigation_myspace_button);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_navigation;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.navigation_home_button:
                closeDrawer();
                MainActivity.open(getActivity());
                break;

            case R.id.navigation_myspace_button:
                closeDrawer();
                MySpaceActivity.open(getActivity());
                break;
        }
    }

    private void closeDrawer() {
        if(getActivity() instanceof BaseDawerActivity){
            ((BaseDawerActivity) getActivity()).toggleDrawer(false, Gravity.START);
        }
    }


}
