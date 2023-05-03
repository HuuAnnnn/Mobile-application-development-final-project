package tdtu.edu.vn.finalproject_suppermarket;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import tdtu.edu.vn.finalproject_suppermarket.Products.DisplayMainProduct;

public class Home extends AppCompatActivity {
    BottomNavigationView navbar;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        navbar = findViewById(R.id.navBar);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        DisplayMainProduct home = new DisplayMainProduct();
        fragmentTransaction.add(R.id.mainDisplay, home);
        fragmentTransaction.commit();

        navbar.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.userAccount:
                        addNewFragment(new UserInformation());
                        break;
                    case R.id.homepage:
                        addNewFragment(new DisplayMainProduct());
                        break;
                }
                return true;
            }
        });
    }

    private void addNewFragment(Fragment fragment) {
        fragmentManager.popBackStack();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainDisplay, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}