package malakoff.dykh.Activities;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;

import malakoff.dykh.Activities.Base.BaseDawerActivity;
import malakoff.dykh.Fragments.Base.BaseFragment;
import malakoff.dykh.Fragments.Base.InstanceBaseFragement;
import malakoff.dykh.Fragments.HomeFragment;
import malakoff.dykh.R;

public class MainActivity extends BaseDawerActivity{


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
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected BaseFragment assignFragment(int selection) {

        switch (selection) {
            case 0:
            default:
                return HomeFragment.newInstance(selection);
        }

    }
}
