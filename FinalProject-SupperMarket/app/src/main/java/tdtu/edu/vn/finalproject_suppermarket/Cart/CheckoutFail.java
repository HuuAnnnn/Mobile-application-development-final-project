package tdtu.edu.vn.finalproject_suppermarket.Cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import tdtu.edu.vn.finalproject_suppermarket.R;

public class CheckoutFail extends AppCompatActivity {
    Button btnBackToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_fail);
        btnBackToHome = findViewById(R.id.btnBackToHome);
        btnBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CheckoutFail.this, ShoppingCart.class);
                startActivity(intent);
                finish();
            }
        });
    }
}