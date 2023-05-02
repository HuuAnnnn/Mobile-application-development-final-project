package tdtu.edu.vn.finalproject_suppermarket;

import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public class ProductViewHolder extends ViewHolder {
        private TextView productTitle;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productTitle = itemView.findViewById(R.id.displayProductName);
        }
    }
    public class ProductAdapter<E> extends RecyclerView.Adapter<ProductViewHolder> {
        private LayoutInflater inflater;
        private ArrayList<E> data;

        public ProductAdapter(Context context, ArrayList<E> data) {
            this.data = data;
            inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View productCard = inflater.inflate(R.layout.product_component, parent, false);
            ProductViewHolder productViewHolder = new ProductViewHolder(productCard);
            return productViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
            holder.productTitle.setText(data.get(position).toString());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private EditText edtPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ArrayList<String> products = new ArrayList<String>();
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        ViewPagerAuthAdapter viewPagerAuthAdapter = new ViewPagerAuthAdapter(this, getSupportFragmentManager());
        viewPagerAuthAdapter.addFragment(new LoginFragment());
        viewPagerAuthAdapter.addFragment(PlacholderFragment.newInstance(RegisterFragment.ID));
        viewPager.setAdapter(viewPagerAuthAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}