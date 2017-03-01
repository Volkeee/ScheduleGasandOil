package personal.viktrovovk.schedulegasoil.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import personal.viktrovovk.schedulegasoil.R;
import personal.viktrovovk.schedulegasoil.adapter.ScheduleRecyclerAdapter;
import personal.viktrovovk.schedulegasoil.model.ScheduleItem;
import personal.viktrovovk.schedulegasoil.model.SelectorItem;
import personal.viktrovovk.schedulegasoil.service.ConnectionManager;

public class ScheduleActivity extends AppCompatActivity {
    public ConnectionManager mConnectionManager;
    private ScheduleReceiver mReceiver;
    public TabLayout mParityTabs;
    public PlaceholderFragment currentFragment;


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
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        mConnectionManager = new ConnectionManager(this);
        mReceiver = new ScheduleReceiver();

        if (getIntent() != null) {
            Intent receivedIntent = getIntent();
            SelectorItem faculty = (SelectorItem) receivedIntent.getSerializableExtra("faculty");
            SelectorItem group = (SelectorItem) receivedIntent.getSerializableExtra("group");

//            Log.d("HERE!", "Received items are:\n" + faculty.toString() + "\n" + group.toString());

            mConnectionManager.requestSchedulePage(faculty, group);

            IntentFilter scheduleFilter = new IntentFilter(ConnectionManager.ACTION_RETURN_SCHEDULE);
            scheduleFilter.addCategory(Intent.CATEGORY_DEFAULT);
            registerReceiver(mReceiver, scheduleFilter);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.getCurrentItem();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mParityTabs = (TabLayout) findViewById(R.id.tabs_parity);
        mParityTabs.setTabGravity(TabLayout.GRAVITY_FILL);
        mParityTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("HERE!", Integer.toString(tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar
                .make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_schedule, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        public RecyclerView mRecyclerView;
        public Integer mFragmentsDay;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTIONS_DAY = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTIONS_DAY, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);
            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.schedule_recyclerview);
            mFragmentsDay = getArguments().getInt(ARG_SECTIONS_DAY);

            return rootView;
        }

        public  void swapListContent(ArrayList<ScheduleItem> list) {
            ((ScheduleRecyclerAdapter) mRecyclerView.getAdapter()).swap(list);
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
            PlaceholderFragment fragment = PlaceholderFragment.newInstance(position);
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.tab_title_monday);
                case 1:
                    return getString(R.string.tab_title_tuesday);
                case 2:
                    return getString(R.string.tab_title_wednesday);
                case 3:
                    return getString(R.string.tab_title_thursday);
                case 4:
                    return getString(R.string.tab_title_friday);
            }
            return null;
        }
    }

    private class ScheduleReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            @SuppressWarnings("unchecked")
            ArrayList<ScheduleItem> schedule = (ArrayList<ScheduleItem>) intent.getSerializableExtra("");

            try {
                unregisterReceiver(mReceiver);
            } catch (IllegalArgumentException e) {
                Log.i("Unregister", e.toString());
            }
        }
    }
}
