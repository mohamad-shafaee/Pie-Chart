package com.pink.gir.piechart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.pink.gir.pielibrary.chart.PieChart;
import com.pink.gir.pielibrary.chart.PieDataSet;
import com.pink.gir.pielibrary.chart.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);






        //USAGE: See the below example:

        pieChart = (PieChart) findViewById(R.id.chart_1);

        PieEntry e1 = new PieEntry(10, getResources().getString(R.string.Iran));
        PieEntry e2 = new PieEntry(20, getResources().getString(R.string.Turkey));
        PieEntry e3 = new PieEntry(80, getResources().getString(R.string.Germany));
        PieEntry e4 = new PieEntry(60, getResources().getString(R.string.Thiland));
        PieEntry e5 = new PieEntry(70, getResources().getString(R.string.Japan));
        PieEntry e6 = new PieEntry(12, getResources().getString(R.string.China));
        PieEntry e7 = new PieEntry(12, getResources().getString(R.string.Qatar));


        List<PieEntry> vals = new ArrayList<>();
        vals.add(e1);
        vals.add(e2);
        vals.add(e3);
        vals.add(e4);
        vals.add(e5);
        vals.add(e6);
        vals.add(e7);


        PieDataSet<PieEntry> dataSet = new PieDataSet<>(vals, "First countries data");
        //PieDataSet<PieEntry> dataSet = new PieDataSet<>("First countries data");

        pieChart.setdataSet(dataSet);
       // pieChart.setdataSet(null);*/



    }
}
