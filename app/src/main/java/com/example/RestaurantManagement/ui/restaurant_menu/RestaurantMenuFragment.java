package com.example.RestaurantManagement.ui.restaurant_menu;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.RestaurantManagement.R;

public class RestaurantMenuFragment extends Fragment {

    private RestaurantMenuViewModel restaurantMenuViewModel;
    private RecyclerView recyclerView;
    private TextView textViewComment;
    private TextView textViewTurial;
    private OnSelectedListener mCallback;
    public interface OnSelectedListener{
        public void onCreateAddItemActivity();
    }

    public RestaurantMenuFragment() {
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_restaurantmenu, container, false);
        textViewComment = root.findViewById(R.id.tvCommentMenuIsEmty);
        textViewTurial = root.findViewById(R.id.tvTurialMenuIsEmty);
        onObserve();
        textViewTurial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onCreateAddItemActivity();
            }
        });
        recyclerView = root.findViewById(R.id.rvResMenu);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        onCheckItemInRecycleView();
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

    private void onCheckItemInRecycleView()
    {
        restaurantMenuViewModel =
                new ViewModelProvider(this).get(RestaurantMenuViewModel.class);
        if (recyclerView.getItemDecorationCount() == 0)
        {
            recyclerView.setVisibility(View.INVISIBLE);
            //recyclerView.getLayoutParams().height = 100;
        }
        else
        {
            recyclerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        }
    }

    private void onObserve()
    {
        restaurantMenuViewModel =
                new ViewModelProvider(this).get(RestaurantMenuViewModel.class);
        restaurantMenuViewModel.getTextComment().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textViewComment.setText(s);
            }
        });
        restaurantMenuViewModel.getTextTurial().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                textViewTurial.setText(s);
            }
        });
    }

}