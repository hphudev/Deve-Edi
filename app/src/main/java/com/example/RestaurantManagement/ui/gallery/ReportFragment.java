package com.example.RestaurantManagement.ui.gallery;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.RestaurantManagement.R;
import com.example.RestaurantManagement.Restaurant;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.DataInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReportFragment extends Fragment {

//    private ReportViewModel reportViewModel;
//    private RecyclerView rec;
    private Spinner spnCategory;
    private BarChart chart;
//    private boolean showTurial = true;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        reportViewModel =
//                new ViewModelProvider(this).get(ReportViewModel.class);
//        reportViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        View root = inflater.inflate(R.layout.fragment_report, container, false);
        spnCategory = (Spinner) root.findViewById(R.id.spnCategory);

        List<String> list = new ArrayList<>();
        list.add("Báo cáo theo ngày trong tháng");
        list.add("Báo cáo theo tháng");
        list.add("Báo cáo theo năm");
        //list.add("Báo cáo theo món");

        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        spnCategory.setAdapter(adapter);
        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spnCategory.getSelectedItem().toString().equals("Báo cáo theo tháng"))
                {
                    forMonth();
                }
                if (spnCategory.getSelectedItem().toString().equals("Báo cáo theo năm"))
                {
                    forYear();
                }

                if (spnCategory.getSelectedItem().toString().equals("Báo cáo theo ngày trong tháng"))
                {
                    forDay();
                }
               //Toast.makeText(MainActivity.this, spnCategory.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        chart = root.findViewById(R.id.barchart);
        setHasOptionsMenu(false);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void forMonth()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                try {
                    QuerySnapshot querySnapshot =
                            Tasks.await(
                                    db.collection("report")
                                    .whereEqualTo("id_res", Restaurant.getId())
                                    .get());
                    int[] total = new int[13];
                    for (int i = 0; i < 13; i++)
                    {
                        total[i] = 0;
                    }
                    int count = 0;
                    for (DocumentSnapshot document : querySnapshot.getDocuments())
                    {
                        count++;
                        String date = document.get("date").toString();
                        int month = getMonth(date);
                        total[month] += Integer.parseInt(document.get("total").toString());
                    }
                    ArrayList<BarEntry> NoOfEmp = new ArrayList<>();
                    for (int i = 1; i < 13; i++)
                        NoOfEmp.add(new BarEntry(i - 1, Float.parseFloat(String.valueOf(total[i]))));
                    ArrayList<String> month = new ArrayList<>();
                    month.add("T1");
                    month.add("T2");
                    month.add("T3");
                    month.add("T4");
                    month.add("T5");
                    month.add("T6");
                    month.add("T7");
                    month.add("T8");
                    month.add("T9");
                    month.add("T10");
                    month.add("T11");
                    month.add("T12");
                    ((Activity)getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            BarDataSet bardataset = new BarDataSet(NoOfEmp, "Doanh thu theo tháng");
                            BarData data = new BarData(bardataset);
                            bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
                            Description description = new Description();
                            description.setText("Doanh thu");
                            XAxis xAxis = chart.getXAxis();
                            chart.setDescription(description);
                            xAxis.setValueFormatter(new IndexAxisValueFormatter(month));
                            xAxis.setPosition(XAxis.XAxisPosition.TOP);
                            xAxis.setDrawGridLines(false);
                            xAxis.setDrawAxisLine(false);
                            xAxis.setGranularity(1f);
                            xAxis.setLabelCount(month.size());
                            xAxis.setLabelRotationAngle(270f);
                            chart.setData(data);
                            chart.animateY(5000);
                            chart.invalidate();
                        }
                    });

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
//        ArrayList<BarEntry> NoOfEmp = new ArrayList<>();
//        NoOfEmp.add(new BarEntry(1, 1));
//        NoOfEmp.add(new BarEntry(2, 2));
//        NoOfEmp.add(new BarEntry(3, 3));
//        NoOfEmp.add(new BarEntry(4, 4));
//        NoOfEmp.add(new BarEntry(5, 5));
//        NoOfEmp.add(new BarEntry(6, 6));
//        NoOfEmp.add(new BarEntry(7, 7));
//        NoOfEmp.add(new BarEntry(8, 8));
//        NoOfEmp.add(new BarEntry(9, 9));
//        NoOfEmp.add(new BarEntry(10, 10));
//        NoOfEmp.add(new BarEntry(11, 11));
//        NoOfEmp.add(new BarEntry(12, 12));
//
//
//
//        BarDataSet bardataset = new BarDataSet(NoOfEmp, "Doanh thu theo tháng");
//        chart.animateY(5000);
//        BarData data = new BarData(bardataset);
//        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
//        Description description = new Description();
//        description.setText("Doanh thu");
//        chart.setData(data);
//        XAxis xAxis = chart.getXAxis();
//        xAxis.setValueFormatter(new IndexAxisValueFormatter(year));
//        xAxis.setPosition(XAxis.XAxisPosition.TOP);
//        xAxis.setDrawGridLines(false);
//        xAxis.setDrawAxisLine(false);
//        xAxis.setGranularity(1f);
//        xAxis.setLabelCount(year.size());
//        xAxis.setLabelRotationAngle(270f);
//        chart.invalidate();
    }

    private void forDay()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                try {
                    QuerySnapshot querySnapshot =
                            Tasks.await(
                                    db.collection("report")
                                            .whereEqualTo("id_res", Restaurant.getId())
                                            .get());
                    int[] total = new int[32];
                    for (int i = 0; i < 32; i++)
                    {
                        total[i] = 0;
                    }
                    int count = 0;
                    for (DocumentSnapshot document : querySnapshot.getDocuments())
                    {
                        count++;
                        String date = document.get("date").toString();
                        int day = getDay(date);
                        int month = getMonth(date);
                        if (date.equals("8/7/2021"))
                        {
                            int j = 0;
                        }
                        if (month == 7
                            && Calendar.getInstance().get(Calendar.YEAR) == getYear(date))
                        total[day] += Integer.parseInt(document.get("total").toString());
                    }
                    ArrayList<BarEntry> NoOfEmp = new ArrayList<>();
                    NoOfEmp.add(new BarEntry(0, Float.parseFloat(String.valueOf(total[1]))));
                    count = -1;
                    for (int i = 1; i < 32; i++)
                            NoOfEmp.add(new BarEntry(++count, Float.parseFloat(String.valueOf(total[i]))));
                    ArrayList<String> day = new ArrayList<>();
                    for (int i = 1; i < 32; i++)
                    {
                            day.add(String.valueOf(i));
                    }
                    ((Activity)getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            BarDataSet bardataset = new BarDataSet(NoOfEmp, "Doanh thu theo năm");
                            BarData data = new BarData(bardataset);
                            bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
                            Description description = new Description();
                            description.setText("Doanh thu");
                            chart.setDescription(description);
                            XAxis xAxis = chart.getXAxis();
                            chart.setDescription(description);
                            xAxis.setValueFormatter(new IndexAxisValueFormatter(day));
                            xAxis.setPosition(XAxis.XAxisPosition.TOP);
                            xAxis.setDrawGridLines(false);
                            xAxis.setDrawAxisLine(false);
                            xAxis.setGranularity(1f);
                            xAxis.setLabelCount(day.size());
                            xAxis.setLabelRotationAngle(270f);
                            chart.setData(data);
                            chart.animateY(5000);
                            chart.invalidate();
                        }
                    });

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
    }

    private void forYear()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                try {
                    QuerySnapshot querySnapshot =
                            Tasks.await(
                                    db.collection("report")
                                            .whereEqualTo("id_res", Restaurant.getId())
                                            .get());
                    int[] total = new int[10000];
                    for (int i = 0; i < 10000; i++)
                    {
                        total[i] = 0;
                    }
                    int count = 0;
                    for (DocumentSnapshot document : querySnapshot.getDocuments())
                    {
                        count++;
                        String date = document.get("date").toString();
                        int year = getYear(date);
                        total[year] += Integer.parseInt(document.get("total").toString());
                    }
                    ArrayList<BarEntry> NoOfEmp = new ArrayList<>();
                    NoOfEmp.add(new BarEntry(0, Float.parseFloat(String.valueOf(total[2019]))));
                    NoOfEmp.add(new BarEntry(1, Float.parseFloat(String.valueOf(total[2020]))));
                    count = 1;
                    for (int i = 0; i < 10000; i++)
                        if (total[i] > 0)
                            NoOfEmp.add(new BarEntry(++count, Float.parseFloat(String.valueOf(total[i]))));
                    ArrayList<String> year = new ArrayList<>();
                    year.add(String.valueOf(2019));
                    year.add(String.valueOf(2020));
                    for (int i = 0; i < 10000; i++)
                    {
                        if (total[i] > 0)
                            year.add(String.valueOf(i));
                    }
                    ((Activity)getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            BarDataSet bardataset = new BarDataSet(NoOfEmp, "Doanh thu theo năm");
                            BarData data = new BarData(bardataset);
                            bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
                            Description description = new Description();
                            description.setText("Doanh thu");
                            chart.setDescription(description);
                            XAxis xAxis = chart.getXAxis();
                            chart.setDescription(description);
                            xAxis.setValueFormatter(new IndexAxisValueFormatter(year));
                            xAxis.setPosition(XAxis.XAxisPosition.TOP);
                            xAxis.setDrawGridLines(false);
                            xAxis.setDrawAxisLine(false);
                            xAxis.setGranularity(1f);
                            xAxis.setLabelCount(year.size());
                            xAxis.setLabelRotationAngle(270f);
                            chart.setData(data);
                            chart.animateY(5000);
                            chart.invalidate();
                        }
                    });

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
    }

    private void forItem()
    {
        ArrayList<BarEntry> NoOfEmp = new ArrayList<>();

        NoOfEmp.add(new BarEntry(1, 1));
        NoOfEmp.add(new BarEntry(2, 2));
        NoOfEmp.add(new BarEntry(3, 3));
        NoOfEmp.add(new BarEntry(4, 4));
        NoOfEmp.add(new BarEntry(5, 5));
        NoOfEmp.add(new BarEntry(6, 6));
        NoOfEmp.add(new BarEntry(7, 7));
        NoOfEmp.add(new BarEntry(8, 8));
        NoOfEmp.add(new BarEntry(9, 9));
        NoOfEmp.add(new BarEntry(10, 10));
        NoOfEmp.add(new BarEntry(11, 11));
        NoOfEmp.add(new BarEntry(12, 12));

        ArrayList<String> year = new ArrayList<>();

        year.add("2008");
        year.add("2009");
        year.add("2010");
        year.add("2011");
        year.add("2012");
        year.add("2013");
        year.add("2014");
        year.add("2015");
        year.add("2016");
        year.add("2017");
        year.add("2019");
        year.add("2020");

        BarDataSet bardataset = new BarDataSet(NoOfEmp, "Doanh thu theo tháng");
        chart.animateY(5000);
        BarData data = new BarData(bardataset);
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        Description description = new Description();
        description.setText("Doanh thu");
        chart.setData(data);
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(year));
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(year.size());
        xAxis.setLabelRotationAngle(270f);
        chart.invalidate();
    }

    private int getDay(String date) {
        int count = 0;
        StringBuilder day = new StringBuilder();
        for (int i = 0; i < date.length(); i++)
        {
            if (count == 0 && date.charAt(i) != '/')
            {
                day.append(date.charAt(i));
            }
            else if (date.charAt(i) == '/')
                count++;
            if (count == 1)
                break;
        }
        return  Integer.parseInt(day.toString());
    }
    private int getMonth(String date) {
        int count = 0;
        StringBuilder month = new StringBuilder();
        for (int i = 0; i < date.length(); i++)
        {
            if (count == 1 && date.charAt(i) != '/')
            {
                month.append(date.charAt(i));
            }
            else if (date.charAt(i) == '/')
                count++;
            if (count == 2)
                break;
        }
        return  Integer.parseInt(month.toString());
    }

    private int getYear(String date) {
        int count = 0;
        StringBuilder year = new StringBuilder();
        for (int i = 0; i < date.length(); i++)
        {
            if (count == 2 && date.charAt(i) != '/')
            {
                year.append(date.charAt(i));
            }
            else if (date.charAt(i) == '/')
                count++;
        }
        return  Integer.parseInt(year.toString());
    }
}