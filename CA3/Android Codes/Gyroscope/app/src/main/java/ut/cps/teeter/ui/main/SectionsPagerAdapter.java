package ut.cps.teeter.ui.main;

import android.content.Context;
import android.hardware.Sensor;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import ut.cps.teeter.R;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.gyroscope, R.string.gravity};
    private Context context;
    private Fragment gyroscopeFrag = new GameFragment(Sensor.TYPE_GYROSCOPE);
    private Fragment gravityFrag = new GameFragment(Sensor.TYPE_GRAVITY);

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return gyroscopeFrag;
            case 1:
            default:
                return gravityFrag;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return context.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 2;
    }
}