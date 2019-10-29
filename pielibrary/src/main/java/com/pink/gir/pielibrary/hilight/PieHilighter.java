package com.pink.gir.pielibrary.hilight;


import com.pink.gir.pielibrary.chart.PieChart;
import com.pink.gir.pielibrary.chart.PieDataSet;
import com.pink.gir.pielibrary.chart.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class PieHilighter {

    protected PieChart mChart;
    /**
     * buffer for storing previously highlighted values
     */
    protected List<Hilight> mHighlightBuffer = new ArrayList<Hilight>();

    public PieHilighter(PieChart chart){
        this.mChart = chart;
    }

    public Hilight getHilight(float x, float y){

        float touchDistanceToCenter = mChart.distanceToCenter(x, y);

        //check if a slice is touched
        if(touchDistanceToCenter > mChart.getRadius()){

            // if no slice was touched, highlight nothing
            return null;
        }else{

            float angle = mChart.getAngleForPoint(x,y);

            angle /= mChart.getAnimator().getPhaseY();
            int index = mChart.getIndexForAngle(angle);

            // check if the index could be found
            if(index < 0 || index >= mChart.getDataSet().getEntryCount()){

                return null;
            }else{

                return getHilight(index);
            }
        }
    }

    /**
     * Returns the closest Highlight object of the given objects based on the touch position inside the chart.
     *
     * @param index
     * @return
     */
    public Hilight getHilight(int index){

        PieDataSet dataSet = mChart.getDataSet();
        PieEntry entry = dataSet.getEntryForIndex(index);

        return new Hilight(index, entry.getY());
    }
}

