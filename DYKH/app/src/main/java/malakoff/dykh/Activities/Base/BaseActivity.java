package malakoff.dykh.Activities.Base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import malakoff.dykh.AppApplication.Constants;
import malakoff.dykh.Fragments.Base.BaseFragment;
import malakoff.dykh.Fragments.HomeFragment;
import malakoff.dykh.R;

/**
 * Created by user on 26/06/2016.
 */

public abstract class BaseActivity extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        beginTransaction(savedInstanceState);
    }

    protected abstract void beginTransaction(Bundle savedInstanceState);


    protected void init(){
        setContentView(getContentView());
    }

    public abstract int getContentView();
}
