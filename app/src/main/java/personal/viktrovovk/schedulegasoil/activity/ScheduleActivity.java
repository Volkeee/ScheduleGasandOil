package personal.viktrovovk.schedulegasoil.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;

import personal.viktrovovk.schedulegasoil.R;
import personal.viktrovovk.schedulegasoil.adapter.ScheduleRecyclerAdapter;
import personal.viktrovovk.schedulegasoil.model.ScheduleItem;
import personal.viktrovovk.schedulegasoil.model.SelectorItem;
import personal.viktrovovk.schedulegasoil.service.ConnectionManager;
import personal.viktrovovk.schedulegasoil.tools.RecycleTouchListener;

public class ScheduleActivity extends AppCompatActivity {
    public static ArrayList<ScheduleItem> mScheduleList;
    public ConnectionManager mConnectionManager;
    public Integer mCurrentFragmentIndex = 0;
    public static Integer mSelectedWeek = 1;
    private ScheduleReceiver mReceiver;
    public TabLayout mParityTabs;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        mScheduleList = new ArrayList<>();
        mConnectionManager = new ConnectionManager(this);
        mReceiver = new ScheduleReceiver();

        if (getIntent().getSerializableExtra("group") != null) {
            Intent receivedIntent = getIntent();
            SelectorItem faculty = (SelectorItem) receivedIntent.getSerializableExtra("faculty");
            SelectorItem group = (SelectorItem) receivedIntent.getSerializableExtra("group");

//            Log.d("HERE!", "Received items are:\n" + faculty.toString() + "\n" + group.toString());

            mConnectionManager.requestSchedulePage(faculty, group);

            IntentFilter scheduleFilter = new IntentFilter(ConnectionManager.ACTION_RETURN_SCHEDULE);
            scheduleFilter.addCategory(Intent.CATEGORY_DEFAULT);
            registerReceiver(mReceiver, scheduleFilter);
        } else {
            SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.application_preference_key), MODE_PRIVATE);
            Gson gson = new Gson();
            String jsonFaculty = sharedPref.getString(getString(R.string.preference_key_users_faculty), null);
            String jsonGroup = sharedPref.getString(getString(R.string.preference_key_users_group), null);
            SelectorItem faculty = gson.fromJson(jsonFaculty, SelectorItem.class);
            SelectorItem group = gson.fromJson(jsonGroup, SelectorItem.class);

            Log.d("Saved info loaded", "Saved faculty is \"" + faculty.getName() + "with id " + faculty.getId().toString()
                    + "\" and saved group is \"" + group.getName() + "\" with id " + group.getId().toString());

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

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mParityTabs = (TabLayout) findViewById(R.id.tabs_parity);
        mParityTabs.setTabGravity(TabLayout.GRAVITY_FILL);
        mParityTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mSelectedWeek = tab.getPosition();
                PlaceholderFragment currentFragment = mSectionsPagerAdapter.getCreatedFragment(mCurrentFragmentIndex);
                ((ScheduleRecyclerAdapter) currentFragment
                        .mRecyclerView.getAdapter())
                        .applyFilter(mScheduleList, currentFragment.mFragmentsDay, mSelectedWeek);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }


            @Override
            public void onPageSelected(int position) {
                mCurrentFragmentIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private ScheduleRecyclerAdapter mAdapterFragment;
        private ArrayList<ScheduleItem> mFragmentsList;
        private RecyclerView mRecyclerView;
        private Integer mFragmentsDay;
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
            mFragmentsList = new ArrayList<>();
            mAdapterFragment = new ScheduleRecyclerAdapter(mFragmentsList);
            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.schedule_recyclerview);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
            mRecyclerView.setAdapter(mAdapterFragment);
            mRecyclerView.addOnItemTouchListener(new RecycleTouchListener(getContext(), mRecyclerView, (view, position) -> {
                Intent intent = new Intent(this.getContext(), TaskListActivity.class);
                intent.putExtra("scheduleItem", ((ScheduleRecyclerAdapter)mRecyclerView.getAdapter()).getItem(position));

                startActivity(intent);
            }));

            mFragmentsDay = getArguments().getInt(ARG_SECTIONS_DAY);

            if (!mScheduleList.isEmpty()) {
                swapListContentforDay(mScheduleList);
            }
            return rootView;
        }

        public void swapListContentforDay(ArrayList<ScheduleItem> list) {
            ((ScheduleRecyclerAdapter) mRecyclerView.getAdapter()).applyFilter(list, mFragmentsDay);
        }
    }

    /**
     * A {@link FragmentStatePagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<PlaceholderFragment> mCreatedFragments;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            mCreatedFragments = new ArrayList<>();
        }

        @Override
        public void notifyDataSetChanged() {
            mCreatedFragments.clear();
            super.notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            PlaceholderFragment fragment = PlaceholderFragment.newInstance(position);
            mCreatedFragments.add(fragment);

            return fragment;
        }

        public PlaceholderFragment getCreatedFragment(int position) {
            return mCreatedFragments.get(position);
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
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
        @SuppressWarnings("unchecked")
        public void onReceive(Context context, Intent intent) {
            mScheduleList = (ArrayList<ScheduleItem>) intent.getSerializableExtra("schedule");

            mSectionsPagerAdapter.notifyDataSetChanged();
            Log.i("Receiver", "Response received");
            try {
                unregisterReceiver(mReceiver);
            } catch (IllegalArgumentException e) {
                Log.i("Unregister", e.toString());
            }
        }
    }
}
