package tdtu.edu.vn.finalproject_suppermarket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class DistrictsAdapter extends ArrayAdapter<Districts> {
    public DistrictsAdapter(@NonNull Context context, int resource, @NonNull List<Districts> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address_selected, parent, false);
        TextView tvProvinceSelected = convertView.findViewById(R.id.tv_province_selected);

        Districts districts = this.getItem(position);
        if(districts!=null){
            tvProvinceSelected.setText(districts.getNameWithType());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, parent, false);
        TextView tvProvince = convertView.findViewById(R.id.tvProvince);

        Districts districts = this.getItem(position);
        if(districts!=null){
            tvProvince.setText(districts.getNameWithType());
        }
        return convertView;
    }
}
