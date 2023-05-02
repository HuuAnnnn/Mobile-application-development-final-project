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

public class ProvinceSpinnerAdapter extends ArrayAdapter<Province> {
    public ProvinceSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<Province> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address_selected, parent, false);
        TextView tvProvinceSelected = convertView.findViewById(R.id.tv_province_selected);

        Province province = this.getItem(position);
        if(province!=null){
            tvProvinceSelected.setText(province.getName_with_type());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, parent, false);
        TextView tvProvince = convertView.findViewById(R.id.tvProvince);

        Province province = this.getItem(position);
        if(province!=null){
            tvProvince.setText(province.getName_with_type());
        }
        return convertView;
    }
}
