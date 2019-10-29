package com.pink.gir.pielibrary.chart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.pink.gir.pielibrary.animation.ChartAnimator;
import com.pink.gir.pielibrary.handler.ViewPortHandler;
import com.pink.gir.pielibrary.hilight.Hilight;
import com.pink.gir.pielibrary.hilight.PieHilighter;
import com.pink.gir.pielibrary.listener.PieChartTouchListener;
import com.pink.gir.pielibrary.renderer.PieChartRenderer;
import com.pink.gir.pielibrary.utils.FPoint;
import com.pink.gir.pielibrary.utils.Utils;

public class PieChart extends View {

    /*
     * width and height of the whole view
     */

    int viewWidth;
    int viewHeight;



    /**
     * rect object that represents the bounds of the piechart, needed for
     * drawing the circle
     */
    //private RectF mCircleBox = new RectF();
    private RectF mCircleBox;

    private PieDataSet mDataSet;

    private ChartAnimator mAnimator;

    /**
     * holds the normalized version of the current rotation angle of the chart
     */
    private float mRotationAngle = 0f;

    /**
     * holds the raw version of the current rotation angle of the chart
     */
    private float mRawRotationAngle = 0f;

    /**
     * flag that indicates if rotation is enabled or not
     */
    protected boolean mRotateEnabled = false;

    /**
     * flag indicating if entry labels should be drawn or not
     */
    private boolean mDrawEntryLabels = true;

    /**
     * array that holds the width of each pie-slice in degrees
     */
    private float[] mDrawAngles = new float[1];

    /**
     * array that holds the cumulative angle in degrees of slices
     */
    private float[] mAbsoluteAngles = new float[1];

    /**
     * if true, the white hole inside the chart will be drawn
     */
    private boolean mDrawHole = true;

    public void setDrawHole(boolean b){
        mDrawHole = b;
    }

    public boolean getDrawHole(){
        return mDrawHole;
    }

    /**
     * variable for the text that is drawn in the center of the pie-chart
     */
    private CharSequence mCenterText = "";

    private FPoint mCenterTextOffset = FPoint.getInstance(0, 0);

    /**
     * indicates the size of the hole in the center of the piechart, default:
     * radius / 2
     */
    private float mHoleRadiusPercent = 65f;

    public void setHoleRadiusPercent(float percent){
        mHoleRadiusPercent = percent;
    }

    public float getHoleRadiusPercent(){
        return mHoleRadiusPercent;
    }


    private float radiusOfTransparentHole;


    public float getTransparentHoleRadius(){

        if(mCircleBox == null){
            return 0;
        }else{
            return radiusOfTransparentHole;
        }
    }


    /**
     * the radius of the transparent circle next to the chart-hole in the center
     */
    protected float mTransparentHoleRadiusPercent = 55f;

    /**
     * if enabled, centertext is drawn
     */
    private boolean mDrawCenterText = true;

    /**
     * Minimum angle to draw slices, this only works if there is enough room for all slices to have
     * the minimum angle, default 0f.
     */
    private float mMinAngleForSlices = 0f;

    public PieChart(Context context) {
        super(context);
        init(context, null);
    }

    public PieChart(Context contex, AttributeSet attrs){
        super(contex, attrs);
        init(contex, attrs);
    }

   /* public PieChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PieChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes){
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }
*/

    public PieDataSet getDataSet(){
        return mDataSet;
    }

    public void setdataSet(PieDataSet dataSet){
        mDataSet = dataSet;

        if(mDataSet == null){
            return;
        }
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged(){

        calculateAngles();
        mAnimator.start();
    }


    /**
     * object responsible for rendering the data
     */
    protected PieChartRenderer mRenderer;

    protected ViewPortHandler vph = new ViewPortHandler();

    protected PieHilighter mHilighter;

    PieChartTouchListener mChartTouchListener;


    /**
     * initialize all paints and stuff
     */
    //from Chart.java in mpandroid
    protected void init(Context context, AttributeSet attrs){

        vph.setOffsets(5, 5, 5, 5);
        mCircleBox = new RectF();


        mAnimator = new ChartAnimator(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                mAnimator.setPhaseY((float) valueAnimator.getAnimatedValue());
                //alpha = mAnimator.getPhaseY();

                //alpha = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });

        //Utils.init(getContext());
        Utils.init(context);

        mRenderer = new PieChartRenderer(this, mAnimator, vph);

        mChartTouchListener = new PieChartTouchListener(this);

        mHilighter = new PieHilighter(this);

        mInfoPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInfoPaint.setColor(Color.rgb(247, 189, 51)); // orange
        mInfoPaint.setTextAlign(Paint.Align.CENTER);
        mInfoPaint.setTextSize(Utils.convertDpToPixel(12f));

    }



    public float distanceToCenter(float x, float y){

        //FPoint c = getCenterOffset();
        FPoint c = getCenterOfCircleBox();
        return (float) Math.sqrt(Math.pow((x - c.x), 2) + Math.pow((y - c.y), 2));
    }

    public FPoint getCenterOfCircleBox(){
        if(mCircleBox == null){
            return null;
        }else{
            return FPoint.getInstance(mCircleBox.centerX(), mCircleBox.centerY());
        }
    }

    public FPoint getCenterOffset(){
        return vph.getContentCenter();
    }


    public float getRadius(){

        if(mCircleBox == null){
            return 0;
        }else{
            return radiusOfCircleBox;
        }
    }

    public float getHoleRadius(){

        if(mCircleBox == null){
            return 0;
        }else{
            return radiusOfHole;
        }
    }

    public float getAngleForPoint(float x, float y){

        //FPoint c = getCenterOffset();
        FPoint c = getCenterOfCircleBox();
        float dx = x - c.x;
        float dy = y - c.y;
        float teta = 0f;

        if( dx > 0 && dy > 0 ){

            teta = (float) Math.atan(dy/dx);
        }
        else if( dx < 0 ){

            teta = (float) (Math.atan(dy/dx) + Math.PI);
        }
        else if(dx > 0 && dy < 0){

            teta = (float) (Math.atan(dy/dx) + 2 * Math.PI);
        }

        return teta;
    }

    public int getIndexForAngle(float angle){

        // take the current angle of the chart into consideration
        float a = Utils.getNormalizedAngle(angle - getRotationAngle());

        for(int i = 0; i < mAbsoluteAngles.length; i++){

            if(a > mAbsoluteAngles[i])
                return i;
        }

        //if no index found
        return -1;

    }

    /**
     * gets a normalized version of the current rotation angle of the pie chart,
     * which will always be between 0.0 < 360.0
     *
     * @return
     */
    public float getRotationAngle() {
        return mRotationAngle;
    }

    private String mNoDataText = "No chart data available.";
    protected Paint mInfoPaint;


    @Override
    public void onDraw(Canvas canvas){
        //super.onDraw(c);

        if (mDataSet == null){

            boolean hasText = !TextUtils.isEmpty(mNoDataText);
            if(hasText){
                FPoint center = getCenter();
                canvas.drawText(mNoDataText, center.x, center.y, mInfoPaint);
            }

            return;

        }

        mRenderer.drawData(canvas);
        mRenderer.drawValues(canvas);
        if(mDrawEntryLabels)
            mRenderer.drawLabels(canvas);

    }

    /**
     * Returns a recyclable MPPointF instance.
     * Returns the center point of the chart (the whole View) in pixels.
     *
     * @return
     */
    public FPoint getCenter() {
        return FPoint.getInstance(getWidth() / 2f, getHeight() / 2f);
    }

    public ChartAnimator getAnimator(){
        return mAnimator;
    }






    //line 105 from PieChart

    /**
     * returns the circlebox, the boundingbox of the pie-chart slices
     *
     * @return
     */
    public RectF getCircleBox() {
        return mCircleBox;
    }

    /**
     * returns an integer array of all the different angles the chart slices
     * have the angles in the returned array determine how much space (of 360°)
     * each slice takes
     *
     * @return
     */
    public float[] getDrawAngles() {
        return mDrawAngles;
    }

    /**
     * calculates the needed angles for the chart slices
     */
    protected void calculateAngles() {

        if(mDataSet != null){
        int entryCount = mDataSet.getEntryCount();

        if (mDrawAngles.length != entryCount) {
            mDrawAngles = new float[entryCount];
        } else {
            for (int i = 0; i < entryCount; i++) {
                mDrawAngles[i] = 0;
            }
        }

        if (mAbsoluteAngles.length != entryCount) {
            mAbsoluteAngles = new float[entryCount];
        } else {
            for (int i = 0; i < entryCount; i++) {
                mAbsoluteAngles[i] = 0;
            }
        }

        int entryValuesSum = mDataSet.getSumOfEntries();

        for (int i = 0; i < entryCount; i++) {

            mDrawAngles[i] = calcAngle(mDataSet.getEntryForIndex(i).getY(), entryValuesSum);
        }

        for (int i = 0; i < entryCount; i++) {

            if (i == 0) {
                mAbsoluteAngles[i] = mDrawAngles[i];
            } else {
                mAbsoluteAngles[i] = mAbsoluteAngles[i - 1] + mDrawAngles[i];
            }

        }
    }

    }

    protected float calcAngle(int y, int sum){

        return (float) y / sum * 360;

    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int desiredWidth = 1000;
        int desiredHeight = 1500;

        int width;
        int height;

        //width
        if(widthMode == MeasureSpec.EXACTLY){
            //Must be this size
            width = widthSize;

        }else if(widthMode == MeasureSpec.AT_MOST){
            //Can't be bigger than...
            width = Math.min(widthSize, desiredWidth);

        }else{
            //Be whatever you want
            width = desiredWidth;
        }

        //height
        if(heightMode == MeasureSpec.EXACTLY){
            //Must be this size
            height = heightSize;

        }else if(heightMode == MeasureSpec.AT_MOST){
            //Can't be bigger than...
            height = Math.min(heightSize, desiredHeight);

        }else{
            //Be whatever you want
            height = desiredHeight;
        }

        //MUST CALL THIS

        setMeasuredDimension(width, height);

    }

    float radiusOfCircleBox;
    float radiusOfHole;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        viewWidth = w;
        viewHeight = h;

        //vph.setOffsets(); it is called in init()
        vph.setChartDimens(viewWidth, viewHeight);

        int extraOffset = 15;
        int diameter = Math.min(viewWidth, viewHeight);
        radiusOfCircleBox = (float) diameter/2 - extraOffset;
        radiusOfHole = radiusOfCircleBox * mHoleRadiusPercent / 100f;
        radiusOfTransparentHole = radiusOfCircleBox * mTransparentHoleRadiusPercent / 100f;



        mCircleBox.set(vph.contentLeft() + extraOffset, vph.contentTop() + extraOffset,
                diameter - extraOffset, diameter - extraOffset);

    }


    public Hilight getHilightByTouchPoint(float x, float y){
        if(mDataSet == null){
            return null;
        }else{
            return getHilighter().getHilight(x, y);
        }
    }

    public PieHilighter getHilighter(){
        return mHilighter;
    }

    public void setHilighter(PieHilighter ph){
        this.mHilighter = ph;
    }

    protected Hilight[] mIndicesToHilight;
    protected boolean mHighLightPerTapEnabled;
    /**
     * Returns the array of currently highlighted values. This might a null or
     * empty array if nothing is highlighted.
     *
     * @return
     */
    public Hilight[] getHighlighted() {
        return mIndicesToHilight;
    }

    /**
     * Returns true if values can be highlighted via tap gesture, false if not.
     *
     * @return
     */
    public boolean isHighlightPerTapEnabled() {
        return mHighLightPerTapEnabled;
    }

    /**
     * Set this to false to prevent values from being highlighted by tap gesture.
     * Values can still be highlighted via drag or programmatically. Default: true
     *
     * @param enabled
     */
    public void setHighlightPerTapEnabled(boolean enabled) {
        mHighLightPerTapEnabled = enabled;
    }

    /**
     * Returns true if there are values to highlight, false if there are no
     * values to highlight. Checks if the highlight array is null, has a length
     * of zero or if the first object is null.
     *
     * @return
     */
    public boolean valuesToHighlight() {
        return mIndicesToHilight == null || mIndicesToHilight.length <= 0
                || mIndicesToHilight[0] == null ? false
                : true;
    }

    /**
     * Sets the last highlighted value for the touchlistener.
     *
     * @param highs
     */
    protected void setLastHighlighted(Hilight[] highs) {

        if (highs == null || highs.length <= 0 || highs[0] == null) {
            mChartTouchListener.setLastHilighted(null);
        } else {
            mChartTouchListener.setLastHilighted(highs[0]);
        }
    }

    /**
     * Highlights the values at the given indices in the given DataSets. Provide
     * null or an empty array to undo all highlighting. This should be used to
     * programmatically highlight values.
     * This method *will not* call the listener.
     *
     * @param highs
     */
    public void highlightValues(Hilight[] highs) {

        // set the indices to highlight
        mIndicesToHilight = highs;

        setLastHighlighted(highs);

        // redraw the chart
        invalidate();
    }

    public void hilightValue(Hilight hi){

        if (hi == null)
            mIndicesToHilight = null;
        else {
            // set the indices to highlight
            mIndicesToHilight = new Hilight[]{ hi };
        }

        setLastHighlighted(mIndicesToHilight);
    }

    //still without setter and getter
    private boolean mTouchEnabled = true;


    @Override
    public boolean onTouchEvent(MotionEvent me){

        //For testing scrollable
        return super.onTouchEvent(me);
        /*if(mTouchEnabled && (mChartTouchListener != null))
         return mChartTouchListener.onTouch(me);

        return super.onTouchEvent(me);*/
    }







}



