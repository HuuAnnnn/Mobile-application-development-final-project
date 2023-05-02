package tdtu.edu.vn.finalproject_suppermarket;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class ViewPagerAuthAdapter extends FragmentPagerAdapter {

    @StringRes
    private final Context mContext;
    private ArrayList<Fragment> fragmentArrayList = new ArrayList<Fragment>();

    public ViewPagerAuthAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
       return fragmentArrayList.get(position);
    }

    public void addFragment(Fragment fragment){
        fragmentArrayList.add(fragment);
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        if (position==0){
            title = "Đăng nhập";
        }

        if (position == 1){
            title = "Đăng ký";
        }
        return title;
    }

    public void addTab(Fragment fragment){
        fragmentArrayList.add(fragment);
    }

    public void removeTab(){
        fragmentArrayList.remove(1);
    }
}
