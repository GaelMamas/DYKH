package malakoff.dykh.Activities;

import android.app.Activity;
import android.content.Intent;

import malakoff.dykh.Activities.Base.BaseDawerActivity;
import malakoff.dykh.Fragments.Base.BaseFragment;
import malakoff.dykh.Fragments.HomeFragment;
import malakoff.dykh.R;

public class MainActivity extends BaseDawerActivity {


    public static void open(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        activity.startActivity(intent);
    }

    @Override
    protected int getTitleResId() {
        return R.string.app_name;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected BaseFragment assignFragment(int myOwnFragmentIndex, int selection) {

        switch (myOwnFragmentIndex) {
            case 0:
            default:
                return HomeFragment.newInstance(selection);
        }

    }
}
