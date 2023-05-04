package tdtu.edu.vn.finalproject_suppermarket.Notification;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import tdtu.edu.vn.finalproject_suppermarket.R;

public class NotificationViewHolder extends RecyclerView.ViewHolder {
    TextView displayNotificationTitle;
    TextView displayNotificationDateCreate;
    TextView displayNotificationContent;
    ImageView displayNotificationImage;

    public NotificationViewHolder(@NonNull View itemView) {
        super(itemView);
        displayNotificationTitle = itemView.findViewById(R.id.displayNotificationTitle);
        displayNotificationDateCreate = itemView.findViewById(R.id.displayNotificationDateCreate);
        displayNotificationContent = itemView.findViewById(R.id.displayNotificationContent);
        displayNotificationImage = itemView.findViewById(R.id.displayNotificationImage);
    }
}