package tdtu.edu.vn.finalproject_suppermarket.Promotion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import tdtu.edu.vn.finalproject_suppermarket.R;

public class PromotionAdapter extends BaseAdapter {
    private ArrayList<Promotion> data;
    private LayoutInflater inflater;
    private Context context;

    public PromotionAdapter(Context context, ArrayList<Promotion> data) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void updateData(ArrayList<Promotion> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return this.data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView = inflater.inflate(R.layout.promotion_component, null);
        Promotion promotion = (Promotion) getItem(i);
        TextView promotionTitle = itemView.findViewById(R.id.promotionTitle);
        TextView promotionExpired = itemView.findViewById(R.id.promotionExpired);

        promotionTitle.setText(promotion.getTitle());
        promotionExpired.setText(promotion.getDateExpired());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PromotionDisplay.class);
                intent.putExtra("title", promotion.getTitle());
                intent.putExtra("dateExpired", promotion.getDateExpired());
                intent.putExtra("code", promotion.getCode());

                ((Activity) context).startActivity(intent);
            }
        });
        return itemView;
    }
}
