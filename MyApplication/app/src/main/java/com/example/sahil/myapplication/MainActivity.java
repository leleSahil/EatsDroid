package com.example.sahil.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.sahil.myapplication.Utils.CalendarUtils;

import java.util.Calendar;

import butterknife.Bind;
import feast.FeastAPI;


public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    @Bind(R.id.view_pager) ViewPager mViewPager;


    public static int day_x=-1;
    public static int month_x=-1;
    public static int year_x=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        if(year_x==-1 || month_x==-1 || day_x==-1) {
            year_x = CalendarUtils.getYear();
            month_x = CalendarUtils.getMonth();
            day_x = CalendarUtils.getDay();
        }

////        Date date = new Date();
//        Calendar calendar.getInstance
//        RestaurantFragment.setDates(date.getYear(), date.getMonth(), date.getDay());
        RestaurantFragment.setDates(year_x, month_x, day_x);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        Log.w("Sahil", "OnCreate MainActivity");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // TODO hide the favorites button if the person isn't logged in
        if(!FeastAPI.sharedAPI.isUserAuthorized()) {
            MenuItem item = menu.findItem(R.id.action_settings);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
//            getFragmentManager().beginTransaction()
//                    .replace(android.R.id.content, new SettingsFragment())
//                    .commit();
            Intent intent = new Intent(getApplicationContext(), FavoriteActivity.class);
            startActivity(intent);
        } else if(id == R.id.action_calendar) {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getFragmentManager(), "MyDialog");
        }

        return super.onOptionsItemSelected(item);
    }

    public class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
//            DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
//            pickerDialog.getDatePicker().setMaxDate(maxDate);
//            pickerDialog.getDatePicker().setMinDate(minDate); // System.currentTimeMillis() - 1000
//            return pickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user

            year_x = year;
            month_x = month + 1;
            day_x = day;

            // TODO alert the Restaurant Fragment that the date has changed

            RestaurantFragment.setDates(year_x, month_x, day_x);

            Log.w("Sahil", year + "-" + month + "-" + day);

            String name = getFragmentTag(mViewPager.getId(), mViewPager.getCurrentItem());
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(name);
            ((RestaurantFragment)fragment).refreshMenus();
        }

        private String getFragmentTag(int viewPagerId, int fragmentPosition)
        {
            return "android:switcher:" + viewPagerId + ":" + fragmentPosition;
        }
    }

    public static class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            RestaurantFragment restaurantFragment = new RestaurantFragment();
            Bundle args = new Bundle();
//
//            // may want to put something about what the date is
//
//            restaurantFragment.setArguments(args);
//            Log.w("Sahil", "Log tags working");
//            return PlaceholderFragment.newInstance(position + 1);

            // TODO put into the bundle the id (the position) of the restaurant that we want
            args.putInt("DiningHallID", position);
            restaurantFragment.setArguments(args);

            return restaurantFragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {



            // TODO change this to get the actual names
            switch(position) {
                case 0:
                    return "EVK";
                case 1:
                    return "Parkside";
                case 2:
                    return "Cafe 84";
                default:
                    return "";
            }
        }
    }
}
