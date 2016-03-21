package org.bissoft.yean.votes;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.bissoft.yean.votes.util.NonSwipeableViewPager;

/**
 * A fragment representing a single bill screen
 * when the head is logged in.
 * @author      Nataliia Makarenko
 */
public class BillInfoHeadFragment extends Fragment implements View.OnClickListener{

    /** Current position in pager adapter. */
    int currentFragmPos = 0;
    /** An adapter for ViewPager. */
    private PagerAdapter mPagerAdapter;
    /** Layout manager that allows the user to flip left and right
     * through pages of data. */
    private NonSwipeableViewPager mViewPager;

    /** Button to go to the info about the bill. */
    LinearLayout llInfoToMain;
    /** Button to go to the discussion of the bill. */
    LinearLayout llInfoToDisc;
    /** Button to go to the voting for the bill. */
    LinearLayout llInfoToVot;

    /** Arguments of the fragment. */
    Bundle args;

    /**
     * Constructs a new instance of {@code BillInfoHeadFragment}.
     */
    public BillInfoHeadFragment() {
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater the LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     *                  The fragment should not add the view itself,
     *                  but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     *
     * @return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bill_info_head, container, false);

        args = getArguments();

        mPagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager(), 3);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (NonSwipeableViewPager) rootView.findViewById(R.id.pager);
        mViewPager.setAdapter(mPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setMenuIcons(position);
                //  mViewPager.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        llInfoToMain = (LinearLayout) rootView.findViewById(R.id.llInfoToMain);
        llInfoToDisc = (LinearLayout) rootView.findViewById(R.id.llInfoToDisc);
        llInfoToVot = (LinearLayout) rootView.findViewById(R.id.llInfoToVot);

        llInfoToMain.setOnClickListener(this);
        llInfoToDisc.setOnClickListener(this);
        llInfoToVot.setOnClickListener(this);

        return rootView;
    }

    /**
     * A callback to be invoked when a view is clicked.
     *
     * @param v View that has been clicked.
     */
    @Override
    public void onClick(View v) {
        if (!HeadPollFragment.isVoting
                && !BillInfoDiscussFragment.isSpeaking) {
            switch (v.getId()) {
                case R.id.llInfoToMain:
                    setMenuIcons(0);
                    mViewPager.setCurrentItem(0);
                    break;
                case R.id.llInfoToDisc:
                    setMenuIcons(1);
                    mViewPager.setCurrentItem(1);
                    break;
                case R.id.llInfoToVot:
                    setMenuIcons(2);
                    mViewPager.setCurrentItem(2);
                    break;
            }
        }
    }

    /**
     * Sets the menu icons.
     *
     * @param currentPage the current page in ViewPager.
     */
    private void setMenuIcons (int currentPage) {
        switch (currentPage) {
            case 0:
                llInfoToMain.setBackgroundColor(Color.parseColor("#cccccc"));
                llInfoToVot.setBackgroundColor(Color.parseColor("#ffffff"));
                llInfoToDisc.setBackgroundColor(Color.parseColor("#ffffff"));
                break;
            case 1:
                llInfoToMain.setBackgroundColor(Color.parseColor("#ffffff"));
                llInfoToVot.setBackgroundColor(Color.parseColor("#ffffff"));
                llInfoToDisc.setBackgroundColor(Color.parseColor("#cccccc"));
                break;
            case 2:
                llInfoToMain.setBackgroundColor(Color.parseColor("#ffffff"));
                llInfoToVot.setBackgroundColor(Color.parseColor("#cccccc"));
                llInfoToDisc.setBackgroundColor(Color.parseColor("#ffffff"));
                break;
        }
    }

    /**
     * Implementation of {@link android.support.v4.view.PagerAdapter} that
     * uses a {@link Fragment} to manage each page. This class also handles
     * saving and restoring of fragment's state.
     */
    class PagerAdapter extends FragmentStatePagerAdapter {
        /** Number of pages in ViewPager.*/
        int mNumOfTabs;

        /**
         * Constructs a new instance of {@code PagerAdapter}.
         *
         * @param fm the fragment manager of this {@code PagerAdapter}.
         * @param NumOfTabs number of pages in this {@code PagerAdapter}.
         */
        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        /**
         * Gets item of ViewPager on the specified position.
         *
         * @param position position of the item.
         *
         * @return item of the ViewPager.
         */
        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    BillInfoMainFragment tab1 = new BillInfoMainFragment();
                    tab1.setArguments(args);
                    return tab1;
                case 1:
                    BillInfoDiscussFragment tab2 = new BillInfoDiscussFragment();
                    tab2.setArguments(args);
                    return tab2;
                case 2:
                    HeadPollFragment tab3 = new HeadPollFragment();
                    tab3.setArguments(args);
                    return tab3;
                default:
                    return null;
            }
        }

        /**
         * Gets number of elements in ViewPager.
         *
         * @return number of elements in ViewPager.
         */
        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}

