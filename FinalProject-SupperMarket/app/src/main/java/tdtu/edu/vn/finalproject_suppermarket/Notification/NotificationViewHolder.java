package tdtu.edu.vn.finalproject_suppermarket.Notification;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import tdtu.edu.vn.finalproject_suppermarket.DisplayNotificationDetail;
import tdtu.edu.vn.finalproject_suppermarket.R;

public class NotificationViewHolder extends RecyclerView.ViewHolder {
    TextView displayNotificationTitle;
    TextView displayNotificationDateCreate;
    ImageView displayNotificationImage;
    LinearLayout container;
    String id;

    public NotificationViewHolder(@NonNull View itemView) {
        super(itemView);
        container = itemView.findViewById(R.id.container);
        displayNotificationTitle = itemView.findViewById(R.id.displayNotificationTitle);
        displayNotificationDateCreate = itemView.findViewById(R.id.displayNotificationDateCreate);
        displayNotificationImage = itemView.findViewById(R.id.displayNotificationImage);

        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(itemView.getContext(), DisplayNotificationDetail.class);
                intent.putExtra("notificationID", id);
                itemView.getContext().startActivity(intent);
            }
        });
    }
}