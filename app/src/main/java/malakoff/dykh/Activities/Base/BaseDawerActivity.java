package malakoff.dykh.Activities.Base;

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
import malakoff.dykh.Fragments.EventCreationFragment;
import malakoff.dykh.Fragments.EventDetailsFragment;
import malakoff.dykh.Fragments.EventsFeedBaseFragment;
import malakoff.dykh.Fragments.HomeFragment;
import malakoff.dykh.Fragments.ModifyEventFragment;
import malakoff.dykh.R;

/**
 * Created by user on 26/06/2016.
 */

public class BaseDawerActivity extends BaseToolbarActivity implements InstanceBaseFragement.MyUILauncherCallback {

    protected DrawerLayout mDrawerLayout;
    protected NavigationView mNavigationView;
    protected ActionBarDrawerToggle mActionBarDrawerToggle;
    protected int currentFragmentLayoutId = 0;
    private Drawable defaultDrawable, myBurger;
    private View.OnClickListener defaultToolbarOnClickListener,
            customToolbarOnclickListener;

    private View backupContainer, furtherContainer;

    /**
     * Bind, create and set up the resources
     */
    @Override
    protected void init() {
        super.init();

        // Navigation Drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);

        backupContainer = findViewById(R.id.fragment_container_backup);
        furtherContainer = findViewById(R.id.fragment_container);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }

                mDrawerLayout.closeDrawers();

                switch (item.getItemId()) {
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

    public void setNavigationBarIcon() {
        if (defaultDrawable != null) {
            if (!defaultDrawable.equals(toolbar.getNavigationIcon())) {
                toolbar.setNavigationIcon(defaultDrawable);
            }
        }
    }

    public void enableDrawerOpening(boolean yes) {
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(yes);
        if (yes) {
            mActionBarDrawerToggle.setToolbarNavigationClickListener(defaultToolbarOnClickListener);
        } else {
            defaultToolbarOnClickListener = mActionBarDrawerToggle
                    .getToolbarNavigationClickListener();
            mActionBarDrawerToggle
                    .setToolbarNavigationClickListener(customToolbarOnclickListener);
        }
    }

    public void changeToolbarConfig(@DrawableRes int drawable, @StringRes int title) {
        enableDrawerOpening(false);
        toolbar.setNavigationIcon(drawable);
        toolbar.setTitle(title);
        supportInvalidateOptionsMenu();
    }

    public void changeToolbarConfig(@DrawableRes int drawable, CharSequence title) {
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
    protected void resumeHomeFragments() {

        if (backupContainer.getVisibility() == View.GONE
                && getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null
                && getSupportFragmentManager().findFragmentById(R.id.fragment_container_backup) != null) {
            backupContainer.setVisibility(View.VISIBLE);
            furtherContainer.setVisibility(View.GONE);

            if (this instanceof MainActivity) {
                getSupportFragmentManager().beginTransaction().show(getSupportFragmentManager().findFragmentByTag(HomeFragment.class.getName())).commit();
                setCurrentFragmentLayoutId(R.layout.fragment_home);
            } else if (this instanceof MySpaceActivity) {
                getSupportFragmentManager().beginTransaction().show(getSupportFragmentManager().findFragmentByTag(EventsFeedBaseFragment.class.getName())).commit();
                setCurrentFragmentLayoutId(R.layout.fragment_events_feed);
            }

            toolbar.setNavigationIcon(myBurger);
            enableDrawerOpening(true);
            getToolbar().setTitle(R.string.app_name);
            supportInvalidateOptionsMenu();
        }
    }

    @Override
    protected BaseFragment assignFragment(int myOwnFragmentIndex, int selection) {
        return null;
    }

    @Override
    public void onOpenFragment(InstanceBaseFragement fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (backupContainer.getVisibility() == View.VISIBLE) {
            backupContainer.setVisibility(View.GONE);
            furtherContainer.setVisibility(View.VISIBLE);

            myBurger = getToolbar().getNavigationIcon(); //TODO how to back up default berger icon

            if (this instanceof MainActivity) {
                transaction.hide(getSupportFragmentManager().findFragmentByTag(HomeFragment.class.getName()));
            } else if (this instanceof MySpaceActivity) {
                transaction.hide(getSupportFragmentManager().findFragmentByTag(EventsFeedBaseFragment.class.getName()));
            }
        }

        transaction.replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName());
        transaction.addToBackStack(null);

        transaction.commit();
    }

    public void setCurrentFragmentLayoutId(int currentFragmentLayoutId) {
        this.currentFragmentLayoutId = currentFragmentLayoutId;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        switch (getTypeToolbar()) {
            case Constants.TOOLBAR_MAIN_MENU:
                getMenuInflater().inflate(R.menu.menu_creative, menu);
                return true;
            case Constants.TOOLBAR_READY_MENU:
                getMenuInflater().inflate(R.menu.menu_ready, menu);
                return true;
            case Constants.TOOLBAR_UPDATELIKED_MENU:
                getMenuInflater().inflate(R.menu.menu_update, menu);
                return true;

            case Constants.TOOLBAR_CREATIVE_MENU:
            default:
                getMenuInflater().inflate(R.menu.menu_main, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return getFunctionTypeToolbar(item);
    }


    private int getTypeToolbar() {
        switch (currentFragmentLayoutId) {
            case R.layout.fragment_home:

                return Constants.TOOLBAR_MAIN_MENU;

            case R.layout.fragment_event_details:

                return Constants.TOOLBAR_UPDATELIKED_MENU;

            case R.layout.fragment_event_creation:

                return Constants.TOOLBAR_CREATIVE_MENU;

            case R.layout.fragment_events_feed:
            default:

                return Constants.TOOLBAR_READY_MENU;
        }
    }

    private boolean getFunctionTypeToolbar(MenuItem item) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(backupContainer.getVisibility() == View.VISIBLE ? R.id.fragment_container_backup : R.id.fragment_container);

        switch (currentFragmentLayoutId) {
            case R.layout.fragment_home:
                if (currentFragment instanceof HomeFragment) {

                    if (item.getItemId() == R.id.action_create) {
                        onOpenFragment(EventCreationFragment
                                .newInstance());
                        return true;
                    } else if (item.getItemId() == R.id.action_search) {
                        onOpenFragment(EventsFeedBaseFragment
                                .newInstance(-1));
                        return true;
                    }
                }
                break;

            case R.layout.fragment_event_details:
                if (currentFragment instanceof EventDetailsFragment) {
                    onOpenFragment(ModifyEventFragment
                            .newInstance(currentFragment.getArguments()
                                    .getString(Constants.DYKH_FRAGMENT_SELECTION)));
                    return true;
                }

                break;

            case R.layout.fragment_events_feed:
                if (currentFragment instanceof EventsFeedBaseFragment) {
                    if (item.getItemId() == R.id.action_create) {
                        onOpenFragment(EventCreationFragment
                                .newInstance());
                        return true;
                    }
                }
            default:

                break;
        }

        return false;
    }

    /*public void setUserProfile(String userName, @DrawableRes int drawable){
        if(mNavigationView != null) {
            ((TextView) mNavigationView.findViewById(R.id.text_user_name)).setText(userName);
            ((ImageView) mNavigationView.findViewById(R.id.image_user_profile)).setImageResource(drawable);
        }
    }*/
}
