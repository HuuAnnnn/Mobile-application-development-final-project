package tdtu.edu.vn.finalproject_suppermarket.Cart;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import tdtu.edu.vn.finalproject_suppermarket.Notification.Notification;
import tdtu.edu.vn.finalproject_suppermarket.Notification.NotificationAdapter;
import tdtu.edu.vn.finalproject_suppermarket.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DisplayProductCart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DisplayProductCart extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView displayProductCart;
    private ProductCartAdapter productCartAdapter;
    private ProgressBar spinner;
    public DisplayProductCart() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DisplayProductCart.
     */
    // TODO: Rename and change types and number of parameters
    public static DisplayProductCart newInstance(String param1, String param2) {
        DisplayProductCart fragment = new DisplayProductCart();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display_product_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displayProductCart = view.findViewById(R.id.displayProductCart);
        ArrayList<ProductCart> productCartList = new ArrayList<ProductCart>();
        productCartList.add(new ProductCart("1","2", "3", "3","4","6"));
        productCartList.add(new ProductCart("1","2", "3", "3","4","6"));
        productCartList.add(new ProductCart("1","2", "3", "3","4","6"));
        productCartList.add(new ProductCart("1","2", "3", "3","4","6"));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        displayProductCart.setLayoutManager(layoutManager);
        displayProductCart.setHasFixedSize(true);
        displayProductCart.setAdapter(new ProductCartAdapter(getActivity(), productCartList));
    }
}