package tdtu.edu.vn.finalproject_suppermarket;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        ViewPagerAuthAdapter viewPagerAuthAdapter = new ViewPagerAuthAdapter(this, getSupportFragmentManager());
        viewPagerAuthAdapter.addFragment(new LoginFragment());
        viewPagerAuthAdapter.addFragment(PlacholderFragment.newInstance(RegisterFragment.ID));
        viewPager.setAdapter(viewPagerAuthAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}