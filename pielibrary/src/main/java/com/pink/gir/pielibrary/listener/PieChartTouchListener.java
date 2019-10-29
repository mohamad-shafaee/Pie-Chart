package com.pink.gir.pielibrary.listener;


import android.view.GestureDetector;
import android.view.MotionEvent;

import com.pink.gir.pielibrary.chart.PieChart;
import com.pink.gir.pielibrary.hilight.Hilight;


public class PieChartTouchListener extends GestureDetector.SimpleOnGestureListener {

    // in PieChart class we must override onTouchEvent() and use mGestureDetector.onTouchEvent()


    /**
     * the chart the listener represents
     */
    protected PieChart mChart;
    /**
     * the gesturedetector used for detecting taps and longpresses, ...
     */
    protected GestureDetector mGestureDetector;
    /**
     * the last highlighted object (via touch)
     */
    protected Hilight mLastHilighted;

    public PieChartTouchListener(PieChart chart){
        this.mChart = chart;
        mGestureDetector = new GestureDetector(chart.getContext(), this);
    }

    @Override
    public boolean onSingleTapUp(MotionEvent me){

        Hilight hilight = mChart.getHilightByTouchPoint(me.getX(), me.getY());
        performHilight(hilight);

        mChart.invalidate();
        return true;
    }

    public void performHilight(Hilight h){

        if(h == null || h.equalTo(mLastHilighted)){
            mChart.hilightValue(null);
            mLastHilighted = null;
        }else{
            mChart.hilightValue(h);
            mLastHilighted = h;
        }
    }


    /**
     * Sets the last value that was highlighted via touch.
     *
     * @param high
     */
    public void setLastHilighted(Hilight high) {
        mLastHilighted = high;
    }


    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distX, float distY){

        float x2 = e2.getX();
        float y2 = e2.getY();

        //Now I do not implement onScroll()
        // I wanted it to rotate the piechart when scroll the finger on it.
        // I will do it later.

        float length = (float) Math.sqrt(distX * distX + distY * distY);



        mChart.invalidate();

        return true;
    }

    public boolean onTouch(MotionEvent me){

        /*if(mGestureDetector.onTouchEvent(me))
            return true;

        return false;*/
        return mGestureDetector.onTouchEvent(me);
    }


}

