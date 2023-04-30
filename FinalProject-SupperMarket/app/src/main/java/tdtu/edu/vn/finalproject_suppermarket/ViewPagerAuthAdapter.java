package tdtu.edu.vn.finalproject_suppermarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAuthAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAuthAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position==0){
            return new LoginFragment();
        }

        if (position == 1){
            return new RegisterFragment();
        }

        return new LoginFragment();
    }

    @Override
    public int getCount() {
        return 2;
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
}
