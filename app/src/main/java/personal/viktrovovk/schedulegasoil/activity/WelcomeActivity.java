package personal.viktrovovk.schedulegasoil.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import personal.viktrovovk.schedulegasoil.R;
import personal.viktrovovk.schedulegasoil.adapter.SelectorsRecyclerAdapter;
import personal.viktrovovk.schedulegasoil.model.SelectorItem;
import personal.viktrovovk.schedulegasoil.service.ConnectionManager;
import personal.viktrovovk.schedulegasoil.tools.ClickListener;
import personal.viktrovovk.schedulegasoil.tools.RecycleTouchListener;
import personal.viktrovovk.schedulegasoil.views.NonSwipeableViewPager;

public class WelcomeActivity extends AppCompatActivity {
    public static SelectorItem mSelectedGroup;
    public Toolbar mToolbar;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private static SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link NonSwipeableViewPager} that will host the section contents.
     */
    public static NonSwipeableViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the NonSwipeableViewPager with the sections adapter.
        mViewPager = (NonSwipeableViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 1) {
            mViewPager.setCurrentItem(0);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        public SelectorsRecyclerAdapter mSelectorsRecyclerAdapter;
        public ConnectionManager mConnectionManager;
        public ProgressBar mFacultiesProgressBar;
        public SelectorsItemsReceiver mReceiver;
        public ArrayList<SelectorItem> mList;
        public RecyclerView mRecyclerView;
        public Integer currentSection;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);
            mList = new ArrayList<>();
            mReceiver = new SelectorsItemsReceiver();
            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.selector_recycler_view);
            mFacultiesProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
            mSelectorsRecyclerAdapter = new SelectorsRecyclerAdapter(mList);
            mConnectionManager = new ConnectionManager(getContext());
            currentSection = getArguments().getInt(ARG_SECTION_NUMBER);

            mFacultiesProgressBar.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);

            mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.setAdapter(mSelectorsRecyclerAdapter);

            //Set the toolbar title for corresponding page
            switch (currentSection) {
                case 0:
                    mConnectionManager.requestFacultiesSelector();
                    IntentFilter selectorFacultiesFilter = new IntentFilter(ConnectionManager.ACTION_RETURN_FACULTIES);
                    selectorFacultiesFilter.addCategory(Intent.CATEGORY_DEFAULT);

                    getActivity().registerReceiver(mReceiver, selectorFacultiesFilter);

                    mRecyclerView.addOnItemTouchListener(new RecycleTouchListener(getContext(), mRecyclerView, new ClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            mSelectedGroup = mSelectorsRecyclerAdapter.getItem(position);
                            mSectionsPagerAdapter.notifyDataSetChanged();
                            mViewPager.setCurrentItem(1);
                        }
                    }));
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_chooseFaculty));
                    break;
                case 1:
                    if (mSelectedGroup != null) {
                        mConnectionManager.requestGroupsSelector(mSelectedGroup);
                        IntentFilter selectorGroupsFilter = new IntentFilter(ConnectionManager.ACTION_RETURN_GROUPS);
                        selectorGroupsFilter.addCategory(Intent.CATEGORY_DEFAULT);

                        getActivity().registerReceiver(mReceiver, selectorGroupsFilter);
                        mRecyclerView.addOnItemTouchListener(new RecycleTouchListener(getContext(), mRecyclerView, (view, position) -> {
                            SelectorItem selectedGroup = ((SelectorsRecyclerAdapter) mRecyclerView.getAdapter()).getItem(position);

                            SharedPreferences mPrefs = getActivity().getSharedPreferences(getString(R.string.application_preference_key), MODE_PRIVATE);
                            SharedPreferences.Editor prefsEditor = mPrefs.edit();

                            Gson gson  = new Gson();
                            String jsonFaculty = gson.toJson(mSelectedGroup);
                            String jsonGroup = gson.toJson(selectedGroup);

                            prefsEditor.putString(getString(R.string.preference_key_users_faculty), jsonFaculty);
                            prefsEditor.putString(getString(R.string.preference_key_users_group), jsonGroup);

                            prefsEditor.commit();


                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.putExtra("faculty", mSelectedGroup);
                            intent.putExtra("group", selectedGroup);

                            startActivity(intent);
                            getActivity().finish();
                        }));
                    } else
                        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_chooseGroup));
                    break;
            }
            return rootView;
        }

        private class SelectorsItemsReceiver extends BroadcastReceiver {

            @Override
            @SuppressWarnings("unchecked")
            public void onReceive(Context context, Intent intent) {
                String receivedAction = intent.getAction();

                if (receivedAction.equals(ConnectionManager.ACTION_RETURN_FACULTIES)) {
                    ArrayList<SelectorItem> faculties = (ArrayList<SelectorItem>) intent.getSerializableExtra("faculties");
                    mSelectorsRecyclerAdapter.swap(faculties);
                    mFacultiesProgressBar.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                } else if (receivedAction.equals(ConnectionManager.ACTION_RETURN_GROUPS)) {
                    ArrayList<SelectorItem> groups = (ArrayList<SelectorItem>) intent.getSerializableExtra("groups");
                    mSelectorsRecyclerAdapter.swap(groups);
                    mFacultiesProgressBar.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }

                try {
                    getActivity().unregisterReceiver(mReceiver);
                } catch (Exception e) {
                    Log.i("Unregister", e.toString());
                }
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<PlaceholderFragment> fragments;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            PlaceholderFragment fragment;
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0 && fragments.size() != 0) {
                fragment = fragments.get(0);
                return fragment;
            } else {
                fragment = PlaceholderFragment.newInstance(position);
                if (fragments.size() > 1)
                    fragments.remove(1);
                fragments.add(fragment);
                return fragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public int getItemPosition(Object object) {
            if (fragments.indexOf(object) == 0)
                return POSITION_UNCHANGED;
            else if (fragments.indexOf(object) == 1)
                return POSITION_NONE;
            else return POSITION_UNCHANGED;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.title_chooseFaculty);
                case 1:
                    return getString(R.string.title_chooseGroup);
            }
            return null;
        }
    }
}
