package com.example.thecoffeeshopmanagement;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link homeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class homeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public homeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment orderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static homeFragment newInstance(String param1, String param2) {
        homeFragment fragment = new homeFragment();
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
        HideActionBar();

    }

    //region Khai báo tablayout và viewpaper
    TabLayout tabLayout;
    ViewPager viewPager;
    //endregion
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_home, container, false);
        //region Tham chiếu đến tabLayout và viewPaper
        tabLayout = (TabLayout) v.findViewById(R.id.tab_layout_home);
        viewPager = (ViewPager) v.findViewById(R.id.viewPaper);
        //endregion
        //region Khai báo arraylist<string> các item để đưa vào tiêu đề tablayout
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Nhà hàng");
        arrayList.add("Tìm việc");
        arrayList.add("Cộng đồng");
        //endregion
        //region Kết nối viewPaper với arrylist
        connectViewPaperToArrayList(viewPager, arrayList);
        //endregion
        //region Kết nối tabLayout với viewPaper
        tabLayout.setupWithViewPager(viewPager);
        //endregion
        return v;

    }

    public void connectViewPaperToArrayList(ViewPager viewPager, ArrayList<String> arrayList) {
        //region Khai báo main Adapter
        MainAdapter adapter = new MainAdapter(getChildFragmentManager());
        Fragment restaurantFragment = new RestaurantFragment();
        for (int i = 0; i < arrayList.size(); i++)
        {
            adapter.addFragment(restaurantFragment, arrayList.get(i));
            restaurantFragment = new RestaurantFragment();
        }
        viewPager.setAdapter(adapter);
        //endregion
    }

    //region Hàm ẩn ActionBar
    private void HideActionBar()
    {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }
    //endregion
    //region Khai báo lớp MainAdapter
    private class MainAdapter extends FragmentPagerAdapter {
        //region Khai báo các ArrayList <fragment>, <string>
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        ArrayList<String> stringArrayList = new ArrayList<>();
        //endregion
        //region Khai báo các ID imange trên tabLayout
        int[] imageList = {R.drawable.ic_restaurant_menu_24, R.drawable.ic_job_24, R.drawable.ic_chat_24};
        //endregion
        //region Khai báo constructor thêm fragment (addFragment) cùng với title (String) tương ứng
        public void addFragment(Fragment fragment, String s)
        {
            fragmentArrayList.add(fragment);
            stringArrayList.add(s);
        }
        //endregion

        @NonNull
        @Override
        public Fragment getItem(int position)
        {
            return fragmentArrayList.get(position);
        }

        @Override
        public int getCount()
        {
            return  fragmentArrayList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position)
        {
            Drawable drawable = ContextCompat.getDrawable(getContext(), imageList[position]);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            SpannableString spannableString = new SpannableString("   " +
                    stringArrayList.get(position));
            ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
            spannableString.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return  spannableString;
        }


        public MainAdapter(FragmentManager childFragmentManager) {
            super(childFragmentManager);
        }
    }
    //endregion
}