package com.ohrats.bbb.ohrats;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * controls adding fragments to an adapter for the tabbed view
 * Created by Matt on 10/6/2017.
 */

class SectionsPageAdapter extends FragmentPagerAdapter{

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    SectionsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * adds fragments to section
     * @param fragment to be added
     * @param title of the section
     */
    void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    /**
     * gets the title of the fragment at a specific position
     * @param position of page
     * @return title of page
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    /**
     * gets the fragment at a specific position
     * @param position of page
     * @return fragment
     */
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    /**
     * gets size of section page adapter
     * @return size
     */
    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
