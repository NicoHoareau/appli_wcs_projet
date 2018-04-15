package fr.nicolashoareau_toulousewcs.appliwcsprojet;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by wilder on 15/04/18.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;

    public PagerAdapter(FragmentManager fm, int NumberOfTabs) {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }
    public final static int TAB_REQUEST = 0;
    public final static int TAB_VALIDATED_REQUEST = 1;
    public final static int TAB_ACTUALITY = 2;


    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case TAB_REQUEST :
                RequestFragment tab1 = new RequestFragment();
                return tab1;
            case TAB_VALIDATED_REQUEST  :
                ValidationRequestFragment tab2 = new ValidationRequestFragment();
                return tab2;
            case TAB_ACTUALITY :
                ActualityFragment tab3 = new ActualityFragment();
                return tab3;

            default :
                return null;
        }


    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}
