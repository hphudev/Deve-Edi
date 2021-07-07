package com.example.RestaurantManagement.ui.restaurant_menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.RestaurantManagement.EditItemActivity;
import com.example.RestaurantManagement.Item;
import com.example.RestaurantManagement.R;
import com.example.RestaurantManagement.Restaurant;
import com.example.RestaurantManagement.RestaurantMenuAdapter;
import com.example.RestaurantManagement.RestaurantMenuViewModel;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

import static android.app.Activity.RESULT_OK;

public class RestaurantMenuFragment extends Fragment implements RestaurantMenuAdapter.ResMeAdaInterface {

    private RestaurantMenuViewModel restaurantMenuViewModel;
    private RestaurantMenuAdapter restaurantMenuAdapter;
    private RecyclerView recyclerView;
    private OnSelectedListener mCallback;
    static private int EDIT_ITEM_CODE = 1;

    @Override
    public void onItemResClicked(Item item) {
        Bundle bundle = new Bundle();
        bundle.putString("id", item.getId());
        bundle.putString("id_unit", item.getId_unit());
        bundle.putString("image_url", item.getImage_url());
        bundle.putString("name", item.getName());
        bundle.putInt("price", item.getPrice());
        Intent intent = new Intent(getActivity(), EditItemActivity.class);
        intent.putExtra("data", bundle);
        startActivityForResult(intent, EDIT_ITEM_CODE);
    }

    public interface OnSelectedListener{
        public void onCreateAddItemActivity();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_restaurantmenu, container, false);
        //textViewComment = root.findViewById(R.id.tvCommentMenuIsEmty);
        //textViewTurial = root.findViewById(R.id.tvTurialMenuIsEmty);
        Item.setActivity(getActivity());
        Item.setContext(getContext());
        recyclerView = (RecyclerView)root.findViewById(R.id.rvResMenu);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayout);
        restaurantMenuViewModel =
                new ViewModelProvider(RestaurantMenuFragment.this).get(RestaurantMenuViewModel.class);
        restaurantMenuViewModel.getmListItemLiveData().observe(getViewLifecycleOwner(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                int i = items.size();
                restaurantMenuAdapter = new RestaurantMenuAdapter(items, RestaurantMenuFragment.this);
                recyclerView.setAdapter(restaurantMenuAdapter);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        restaurantMenuViewModel.ClearItems();
        Item.GetAllItemInFirestore(restaurantMenuViewModel);
    }

    @Override
    public void onResume() {
        super.onResume();
        restaurantMenuViewModel.ClearItems();
        Item.GetAllItemInFirestore(restaurantMenuViewModel);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnSelectedListener");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_ITEM_CODE && resultCode == RESULT_OK)
        {
            if (data.getData() != null)
            {

            }
        }
    }
}