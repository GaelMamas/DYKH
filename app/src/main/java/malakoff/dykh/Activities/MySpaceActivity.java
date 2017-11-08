package malakoff.dykh.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.view.MenuItem;

import malakoff.dykh.Activities.Base.BaseDawerActivity;
import malakoff.dykh.Fragments.Base.BaseFragment;
import malakoff.dykh.Fragments.EventCreationFragment;
import malakoff.dykh.Fragments.EventDetailsFragment;
import malakoff.dykh.Fragments.EventsFeedBaseFragment;
import malakoff.dykh.Fragments.ModifyEventFragment;
import malakoff.dykh.R;
import malakoff.dykh.Utils.UsefulGenericMethods;

/**
 * Created by user on 27/06/2016.
 */

public class MySpaceActivity extends BaseDawerActivity{

    public static void open(Activity activity){
        Intent intent = new Intent(activity, MySpaceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
    }

    @Override
    protected int getTitleResId() {
        return R.string.app_name;
    }


    @Override
    public int getContentView() {
        return R.layout.activity_my_space;
    }

    @Override
    protected BaseFragment assignFragment(int myOwnFragmentIndex, int selection) {

        switch (myOwnFragmentIndex){
            /*case 3:
                return ModifyEventFragment.newInstance(selection);
            case 2:
                return EventCreationFragment.newInstance();
            case 1:
                return EventDetailsFragment.newInstance(selection);
            case 0:*/
                default:
                return EventsFeedBaseFragment.newInstance(selection);
        }
    }
}
