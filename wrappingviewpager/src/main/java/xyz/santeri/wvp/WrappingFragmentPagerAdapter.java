package xyz.santeri.wvp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * FragmentPagerAdapter that automatically works with {@link WrappingViewPager}, if you don't want to
 * implement the necessary logic in the adapter yourself.
 * <p>
 * Make sure you only use this adapter with a {@link WrappingViewPager}, otherwise your app
 * will crash!
 *
 * @author Santeri Elo
 * @since 14-06-2016
 */
public abstract class WrappingFragmentPagerAdapter extends FragmentPagerAdapter {
    private int mCurrentPosition = -1;

    public WrappingFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * @param container View container (instance of {@link WrappingViewPager}))
     * @param position  Item position
     * @param object    {@link Fragment}
     */
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);

        if (!(container instanceof WrappingViewPager)) {
            throw new UnsupportedOperationException("ViewPager is not a WrappingViewPager");
        }


        Fragment fragment = (Fragment) object;
        WrappingViewPager pager = (WrappingViewPager) container;
        if (fragment != null && fragment.getView() != null) {
            if (position != mCurrentPosition) {
                mCurrentPosition = position;
            }
            pager.onPageChanged(fragment.getView());
        }
    }
}
