package com.example.RestaurantManagement.ui.gallery;

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

public class ReportFragment extends Fragment {

    private ReportViewModel reportViewModel;
    private RecyclerView rec;
    private boolean showTurial = true;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        reportViewModel =
                new ViewModelProvider(this).get(ReportViewModel.class);
        View root = inflater.inflate(R.layout.fragment_report, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        rec = root.findViewById(R.id.rec);
        reportViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        onRecycleCreate();
    }

    private void onRecycleCreate()
    {
        if (rec.getItemDecorationCount() != 0)
        {
            rec.setVisibility(View.VISIBLE);
            rec.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            rec.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        }
    }
}