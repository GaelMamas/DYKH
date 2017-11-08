package malakoff.dykh.Activities.Base;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import malakoff.dykh.AppApplication.Constants;
import malakoff.dykh.Fragments.Base.BaseFragment;
import malakoff.dykh.R;

/**
 * Created by user on 26/06/2016.
 */

public abstract class BaseToolbarActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {


    protected Toolbar toolbar;

    @Override
    protected void init() {
        super.init();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(this);

        //TODO no collapse_toolbar id
        CollapsingToolbarLayout collapseToolbar = null;//(CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(true);

            int titleResId = getTitleResId();
            String title = getResources().getString(titleResId > 0 ? titleResId : R.string.global_back);

            getSupportActionBar().setTitle(title);

            if (collapseToolbar != null) {
                collapseToolbar.setTitle(title);
            }
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    protected abstract int getTitleResId();

    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public abstract boolean onMenuItemClick(MenuItem item);

    @Override
    protected void beginTransaction(Bundle savedInstanceState) {

        BaseFragment fragment;

        if (savedInstanceState != null) {

            fragment = assignFragment(savedInstanceState.getInt(Constants.DYKH_FRAGMENT_SELECTION));

        } else {
            fragment = assignFragment(0);
        }

        fragment.setArguments(getIntent().getExtras());

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
    }


    protected abstract BaseFragment assignFragment(int selection);

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(Constants.DYKH_FRAGMENT_SELECTION, 0);
    }
}
