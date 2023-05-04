package tdtu.edu.vn.finalproject_suppermarket.Notification;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tdtu.edu.vn.finalproject_suppermarket.R;

public class NotificationAdapter<E> extends RecyclerView.Adapter<NotificationViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<E> data;

    public NotificationAdapter(Context context, ArrayList<E> data) {
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    public void updateData(ArrayList<E> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View productCard = inflater.inflate(R.layout.notification_component, parent, false);
        NotificationViewHolder productViewHolder = new NotificationViewHolder(productCard);
        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = (Notification) data.get(position);
        holder.displayNotificationTitle.setText(notification.getTitle());
        holder.displayNotificationContent.setText(notification.getContent());
        holder.displayNotificationDateCreate.setText(notification.getDateCreate());

        byte[] decodedString = Base64.decode(notification.getImage(), Base64.DEFAULT);
        Bitmap imageProduct = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.displayNotificationImage.setImageBitmap(imageProduct);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}