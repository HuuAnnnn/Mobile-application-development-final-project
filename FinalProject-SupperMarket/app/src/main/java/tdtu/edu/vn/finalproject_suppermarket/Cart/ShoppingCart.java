package tdtu.edu.vn.finalproject_suppermarket.Cart;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import tdtu.edu.vn.finalproject_suppermarket.R;

public class ShoppingCart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DisplayProductCart cart = new DisplayProductCart();
        fragmentTransaction.add(R.id.shoppingCartDisplay, cart);
        fragmentTransaction.commit();
    }
}