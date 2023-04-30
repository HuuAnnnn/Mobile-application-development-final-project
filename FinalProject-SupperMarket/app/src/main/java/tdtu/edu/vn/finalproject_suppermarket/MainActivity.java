package tdtu.edu.vn.finalproject_suppermarket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        //tabLayout = findViewById(R.id.tabLayout);
        //viewPager = findViewById(R.id.viewPager);

        //ViewPagerAuthAdapter viewPagerAuthAdapter = new ViewPagerAuthAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        //viewPager.setAdapter(viewPagerAuthAdapter);
        //tabLayout.setupWithViewPager(viewPager);
    }
}