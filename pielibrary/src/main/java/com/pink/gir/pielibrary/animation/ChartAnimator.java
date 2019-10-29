package com.pink.gir.pielibrary.animation;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;

public class ChartAnimator {

    /** object that is updated upon animation update */
    private AnimatorUpdateListener mListener;

    private float mPhaseY;
    private ValueAnimator mValueAnimator;

    public ChartAnimator() { }

    //@RequiresApi(11)
    public ChartAnimator(AnimatorUpdateListener listener) {

        mValueAnimator = ValueAnimator.ofFloat(0f, 1f);
        mListener = listener;

        mValueAnimator.addUpdateListener(mListener);
    }

    public void setPhaseY(float phase){
        this.mPhaseY = phase;
    }

    public float getPhaseY(){
        return mPhaseY;
    }

    public void start(){

        mValueAnimator.setDuration(1000);
        mValueAnimator.start();
    }
}
