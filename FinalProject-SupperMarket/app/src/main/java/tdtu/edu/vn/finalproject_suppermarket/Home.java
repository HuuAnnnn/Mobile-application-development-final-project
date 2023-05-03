package tdtu.edu.vn.finalproject_suppermarket;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import tdtu.edu.vn.finalproject_suppermarket.Products.DisplayMainProduct;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DisplayMainProduct home = new DisplayMainProduct();
        fragmentTransaction.add(R.id.mainDisplay, home);

        // Commit the transaction
        fragmentTransaction.commit();
    }
}