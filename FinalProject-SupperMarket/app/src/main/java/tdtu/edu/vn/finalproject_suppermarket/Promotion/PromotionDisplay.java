package tdtu.edu.vn.finalproject_suppermarket.Promotion;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import tdtu.edu.vn.finalproject_suppermarket.R;

public class PromotionDisplay extends AppCompatActivity {
    ImageButton btnBack;
    TextView promotionTitle;
    TextView promotionCode;
    TextView promotionDateExpired;
    ImageView qrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_display);
        btnBack = findViewById(R.id.btnPromotionBack);
        promotionTitle = findViewById(R.id.displayPromotionTitle);
        promotionCode = findViewById(R.id.displayPromotionCode);
        promotionDateExpired = findViewById(R.id.promotionExpiredDate);
        qrCode = findViewById(R.id.qrCode);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        promotionTitle.setText(intent.getStringExtra("title"));
        promotionCode.setText(intent.getStringExtra("code"));
        promotionDateExpired.setText(intent.getStringExtra("dateExpired"));
        QRGEncoder qrgEncoder = new QRGEncoder(intent.getStringExtra("code"), null, QRGContents.Type.TEXT, 512);
        qrgEncoder.setColorBlack(Color.WHITE);
        qrgEncoder.setColorWhite(Color.BLACK);
        Bitmap bitmap = qrgEncoder.getBitmap();
        qrCode.setImageBitmap(bitmap);
    }
}