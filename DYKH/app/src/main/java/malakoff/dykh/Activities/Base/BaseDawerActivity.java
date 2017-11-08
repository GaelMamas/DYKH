package malakoff.dykh.Activities.Base;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import malakoff.dykh.Activities.MainActivity;
import malakoff.dykh.Activities.MySpaceActivity;
import malakoff.dykh.AppApplication.Constants;
import malakoff.dykh.Fragments.Base.BaseFragment;
import malakoff.dykh.Fragments.Base.InstanceBaseFragement;
import malakoff.dykh.Fragments.EventDetailsFragment;
import malakoff.dykh.Fragments.ModifyEventFragment;
import malakoff.dykh.R;

/**
 * Created by user on 26/06/2016.
 */

public class BaseDawerActivity extends BaseToolbarActivity implements InstanceBaseFragement.MyUILauncherCallback{

    protected DrawerLayout mDrawerLayout;
    protected NavigationView mNavigationView;
    protected ActionBarDrawerToggle mActionBarDrawerToggle;
    private Drawable defaultDrawable;
    private View.OnClickListener defaultToolbarOnClickListener,
            customToolbarOnclickListener;

    protected int currentFargmentLayoutId = 0;

    /**
     * Bind, create and set up the resources
     */
    @Override
    protected void init() {
        super.init();

        // Navigation Drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mNavigationView = (NavigationView)findViewById(R.id.navigation_view);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if(item.isChecked()){
                    item.setChecked(false);
                }else{
                    item.setChecked(true);
                }

                mDrawerLayout.closeDrawers();

                switch(item.getItemId()){
                    case R.id.home:
                        MainActivity.open(BaseDawerActivity.this);
                        return true;

                    case R.id.my_space:
                        MySpaceActivity.open(BaseDawerActivity.this);
                        return true;
                }


                return false;
            }
        });

        mActionBarDrawerToggle = new ActionBarDrawerToggle
                (
                        this,
                        mDrawerLayout,
                        toolbarOpensDrawer() ? toolbar : null,
                        R.string.navigation_drawer_opened,
                        R.string.navigation_drawer_closed
                ) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // Disables the burger/arrow animation by default
                super.onDrawerSlide(drawerView, 0);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(drawerView.getWindowToken(), 0);

            }
        };

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        mActionBarDrawerToggle.syncState();
    }

    public void toggleDrawer(boolean open, int gravity) {
        if (open) {
            mDrawerLayout.openDrawer(gravity);
        } else {
            mDrawerLayout.closeDrawer(gravity);
        }
    }

    protected boolean toolbarOpensDrawer() {
        return true;
    }

    public void setDefaultDrawable() {
        this.defaultDrawable = getToolbar().getNavigationIcon();

        customToolbarOnclickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseDawerActivity.super.onBackPressed();
            }
        };
    }

    public void setNavigationBarIcon(){
        if(defaultDrawable != null) {
            if (!defaultDrawable.equals(toolbar.getNavigationIcon())) {
                toolbar.setNavigationIcon(defaultDrawable);
            }
        }
    }

    public void enableDrawerOpening(boolean yes){
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(yes);
        if(yes) {
            mActionBarDrawerToggle.setToolbarNavigationClickListener(defaultToolbarOnClickListener);
        }else{
            defaultToolbarOnClickListener = mActionBarDrawerToggle
                    .getToolbarNavigationClickListener();
            mActionBarDrawerToggle
                    .setToolbarNavigationClickListener(customToolbarOnclickListener);
        }
    }

    public void changeToolbarConfig(@DrawableRes int drawable, @StringRes int title){
        enableDrawerOpening(false);
        toolbar.setNavigationIcon(drawable);
        toolbar.setTitle(title);
        supportInvalidateOptionsMenu();
    }

    public void changeToolbarConfig(@DrawableRes int drawable, CharSequence title){
        enableDrawerOpening(false);
        toolbar.setNavigationIcon(drawable);
        toolbar.setTitle(title);
        supportInvalidateOptionsMenu();
    }

    @Override
    protected int getTitleResId() {
        return 0;
    }


    @Override
    public int getContentView() {
        return 0;
    }

    @Override
    protected BaseFragment assignFragment(int selection) {
        return null;
    }

    @Override
    public void onOpenFragment(InstanceBaseFragement fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName());

        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void setCurrentFargmentLayoutId(int currentFargmentLayoutId) {
        this.currentFargmentLayoutId = currentFargmentLayoutId;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        switch (getTypeToolbar()){
            case Constants.TOOLBAR_CREATIVE_MENU:
                getMenuInflater().inflate(R.menu.menu_creative, menu);
                return true;
            case Constants.TOOLBAR_READY_MENU:
                getMenuInflater().inflate(R.menu.menu_ready, menu);
                return true;
            case Constants.TOOLBAR_UPDATELIKED_MENU:
                getMenuInflater().inflate(R.menu.menu_update, menu);
                return true;
            default:
                getMenuInflater().inflate(R.menu.menu_main, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        getFunctionTypeToolbar();
        return false;
    }


    private int getTypeToolbar() {
        switch (currentFargmentLayoutId){
            case R.layout.fragment_home:

            return Constants.TOOLBAR_CREATIVE_MENU;

            case R.layout.fragment_event_details:

            return Constants.TOOLBAR_UPDATELIKED_MENU;

            case R.layout.fragment_events_feed:
            default:

            return Constants.TOOLBAR_READY_MENU;
        }
    }

    private void getFunctionTypeToolbar(){
        switch (currentFargmentLayoutId){
            case R.layout.fragment_home:
                break;

            case R.layout.fragment_event_details:
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

                if(currentFragment.getTag().contentEquals(EventDetailsFragment.class.getSimpleName())) {
                    onOpenFragment(ModifyEventFragment
                            .newInstance(currentFragment.getArguments()
                                    .getLong(Constants.DYKH_FRAGMENT_SELECTION)));
                }

                break;

            case R.layout.fragment_events_feed:
            default:

                break;
        }
    }
}
