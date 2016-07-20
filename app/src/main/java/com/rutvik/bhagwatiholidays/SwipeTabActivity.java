package com.rutvik.bhagwatiholidays;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;

import extras.Log;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.FacebookSdk;

import android.widget.TextView;

import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.widget.LikeView;
import com.google.android.gms.plus.PlusOneButton;
import com.nostra13.universalimageloader.core.*;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import bhfragment.FragmentAirTickets;
import bhfragment.FragmentDrawer;
import bhfragment.FragmentHolidayPackages;
import bhfragment.FragmentHotels;
import bhfragment.FragmentVisa;
import extras.CommonUtilities;

public class SwipeTabActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener
{

    private Toolbar mToolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    FragmentStatePagerAdapter mAdapter;
    private FragmentDrawer drawerFragment;

    private static final String TAG = App.APP_TAG + SwipeTabActivity.class.getSimpleName();

    final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

    private App app;

    MenuItem search;

    SearchView searchView;

    public com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    public DisplayImageOptions options;
    PlusOneButton mPlusOneButton;
    private static final int PLUS_ONE_REQUEST_CODE = 0;

    boolean isOnPackagesPage = false;

    SharedPreferences sp;

    boolean isSearchBoxOpen=false;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_tab);

        sp = PreferenceManager.getDefaultSharedPreferences(this);

        mPlusOneButton = (PlusOneButton) findViewById(R.id.plus_one_button);

        app = (App) getApplication();

        app.trackScreenView(SwipeTabActivity.class.getName());

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_logo));
        //mToolbar.setLogo(R.drawable.bh_action_icon);
        getSupportActionBar().setTitle("");
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        //tabLayout.setBackgroundColor(Color.parseColor("#2973bd"));
        //tabLayout.setTabTextColors(Color.parseColor("#a9c7e5"), Color.BLACK);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);


        imageLoader = ImageLoader.getInstance();

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.loading_image)
                .cacheInMemory()
                .cacheOnDisc()
                .displayer(new RoundedBitmapDisplayer(7))
                .build();

        try
        {
            imageLoader.displayImage(app.getUser().getProfilePic(), (android.widget.ImageView) drawerFragment.getView().findViewById(R.id.iv_userImage), options);
            ((TextView) drawerFragment.getView().findViewById(R.id.tv_userName)).setText(app.getUser().getName());
            ((TextView) drawerFragment.getView().findViewById(R.id.tv_userEmail)).setText(app.getUser().getEmail());
        } catch (NullPointerException e)
        {
            e.printStackTrace();
        }


    }

    private void setupTabIcons()
    {

        /**TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
         tabOne.setText("FLIGHTS");
         tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_airticket4, 0, 0);*/
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_airticket4);

        /**TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
         tabTwo.setText("HOTELS");
         tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_hotel2, 0, 0);*/
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_hotel2);

        /**TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
         tabThree.setText("HOLIDAYS");
         tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_holiday2, 0, 0);*/
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_holiday2);

        /**TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
         tabFour.setText("VISA");
         tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_visa2, 0, 0);*/
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_visa2);
    }

    @Override
    public void onBackPressed()
    {
        if (drawerFragment.getDrawerLayout().isDrawerOpen(GravityCompat.START))
        {
            drawerFragment.getDrawerLayout().closeDrawer(GravityCompat.START);
        } else
        {

            CommonUtilities.showSimpleAlertDialog(this,
                    "Alert",
                    "Do you want to exit Bhagwati Holidays?",
                    "Yes", "No", new CommonUtilities.SimpleAlertDialog.OnClickListener()
                    {
                        @Override
                        public void positiveButtonClicked(DialogInterface dialog, int which)
                        {
                            SwipeTabActivity.this.finish();
                        }

                        @Override
                        public void negativeButtonClicked(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    });


        }
    }

    private void setupViewPager(ViewPager viewPager)
    {


        adapter.addFragment(new FragmentAirTickets(), "FLIGHTS");
        adapter.addFragment(new FragmentHotels(), "HOTELS");
        adapter.addFragment(new FragmentHolidayPackages(), "HOLIDAYS");
        adapter.addFragment(new FragmentVisa(), "VISA");
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                View view = SwipeTabActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }

            @Override
            public void onPageSelected(int position)
            {
                if (position == 2)
                {
                    isOnPackagesPage = true;
                    if (isSearchBoxOpen)
                    {
                        Log.i(TAG,"SEARCH BOX IS OPEN SETTING WHITE BACKGROUND");
                        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.white));
                    }
                    else {
                        Log.i(TAG,"SEARCH BOX IS CLOSED SETTING LOGO IN BACKGROUND");
                        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_logo));
                    }
                    search.setVisible(isOnPackagesPage);
                    ((FragmentHolidayPackages) adapter.getItem(position)).loadOffersAsync();
                } else
                {
                    getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_logo));
                    isOnPackagesPage = false;
                    search.setVisible(isOnPackagesPage);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.bh_menu, menu);

        // Associate searchable configuration with the SearchView
        // SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

        searchView.setOnCloseListener(new SearchView.OnCloseListener()
        {
            @Override
            public boolean onClose()
            {
                Log.i(TAG,"SEARCH VIEW ON CLOSED");
                isSearchBoxOpen=false;
                getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_logo));
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.i(TAG,"SEARCH VIEW ON SEARCH CKLICKED");
                isSearchBoxOpen=true;
                getSupportActionBar().setBackgroundDrawable((getResources().getDrawable(R.color.white)));
            }
        });

        search = menu.findItem(R.id.action_search);

        search.setVisible(isOnPackagesPage);
        //searchView.setVisibility(View.GONE);
        // searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {

                ((FragmentHolidayPackages) SwipeTabActivity.this.adapter.getItem(2)).searchForPackage(newText);

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mPlusOneButton.initialize(CommonUtilities.GPLUS_LINK, PLUS_ONE_REQUEST_CODE);
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        AppEventsLogger.activateApp(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_search:
                Log.i(TAG,"ACTION SEARCH CLICKED");
                if (searchView.isActivated())
                {
                    Log.i(TAG,"SEARCH VIEW IS ACTIVE");
                    getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.white));
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bh_menu, menu);
        return true;
    }*/

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

       *//* switch (item.getItemId()) {
            case R.id.menu_offers:
                startActivity(new Intent(SwipeTabActivity.this, OffersActivity.class));
                break;

            case R.id.menu_bus:
                startActivity(new Intent(SwipeTabActivity.this, BusActivity.class));
                break;

            case R.id.menu_settings:
                startActivity(new Intent(SwipeTabActivity.this, SettingsActivity.class));
                break;

            default:
                return super.onOptionsItemSelected(item);
        }*//*
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onDrawerItemSelected(View view, int position)
    {

        //Offers and Promotions
        if (position == 0)
        {
            startActivity(new Intent(this, OffersActivity.class));
        }
        //My Bookings
        else if (position == 1)
        {

            //            long startMillis= Calendar.getInstance().getTimeInMillis();
            //            Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
            //            builder.appendPath("time");
            //            ContentUris.appendId(builder, startMillis);
            //            Intent intent = new Intent(Intent.ACTION_VIEW)
            //                    .setData(builder.build());
            //            startActivity(intent);

            startActivity(new Intent(SwipeTabActivity.this, MyBookingActivity.class));

        }
        //Book Buses Activity
        else if (position == 2)
        {
            startActivity(new Intent(this, BusActivity.class));
        }
        //Disable/Enable Notifications
        else if (position == 5)
        {

            CommonUtilities.showSimpleAlertDialog(this,
                    "Alert",
                    "Are you sure?, You will stop receiving latest offers updates.",
                    "Disable",
                    "Cancel",
                    new CommonUtilities.SimpleAlertDialog.OnClickListener()
                    {
                        @Override
                        public void positiveButtonClicked(DialogInterface dialog, int which)
                        {
                            app.trackEvent(SwipeTabActivity.class.getSimpleName(), "NOTIFICATION TOGGLED", "NAV ACTION");
                            Log.i(TAG, "IS_NOTIFICATION_DISABLED: " + sp.getBoolean("IS_NOTIFICATION_DISABLED", false));
                            if (sp.getBoolean("IS_NOTIFICATION_DISABLED", false))
                            {
                                CommonUtilities.enableNotification(SwipeTabActivity.this, sp, drawerFragment.getAdapter());
                            } else
                            {
                                CommonUtilities.disableNotification(SwipeTabActivity.this, sp, drawerFragment.getAdapter());
                            }
                        }

                        @Override
                        public void negativeButtonClicked(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    });


        }
        //Support
        else if (position == 6)
        {
            app.trackEvent(SwipeTabActivity.class.getSimpleName(), "SUPPORT CLICKED", "NAV ACTION");
            Intent supportIntent = new Intent(Intent.ACTION_DIAL);
            supportIntent.setData(Uri.parse("tel:07940223333"));
            startActivity(supportIntent);
        }
        //Locate Us
        else if (position == 7)
        {
            startActivity(new Intent(SwipeTabActivity.this, LocateUsActivity.class));
        }

        //Send Feedback
        else if (position == 9)
        {
            app.trackEvent(SwipeTabActivity.class.getSimpleName(), "SEND FEEDBACK CLICKED", "NAV ACTION");
            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"info@bhagwatiholidays.com"});
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Feedback");
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        }

    }

    class ViewPagerAdapter extends FragmentPagerAdapter
    {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager)
        {
            super(manager);
        }

        @Override
        public Fragment getItem(int position)
        {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount()
        {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title)
        {
            mFragmentList.add(fragment);
            //mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return null;//mFragmentTitleList.get(position);
        }
    }

}
