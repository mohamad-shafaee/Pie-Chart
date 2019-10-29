package com.pink.gir.pielibrary.renderer;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.StaticLayout;
import android.text.TextPaint;


import com.pink.gir.pielibrary.animation.ChartAnimator;
import com.pink.gir.pielibrary.chart.PieChart;
import com.pink.gir.pielibrary.chart.PieDataSet;
import com.pink.gir.pielibrary.chart.PieEntry;
import com.pink.gir.pielibrary.handler.ViewPortHandler;
import com.pink.gir.pielibrary.utils.FPoint;
import com.pink.gir.pielibrary.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.List;

public class PieChartRenderer extends Renderer {

    protected PieChart mChart;

    /**
     * paint for the hole in the center of the pie chart and the transparent
     * circle
     */
    protected Paint mHolePaint;
    protected Paint mTransparentCirclePaint;
    protected Paint mValueLinePaint;

    /**
     * paint object for the text that can be displayed in the center of the
     * chart
     */
    private TextPaint mCenterTextPaint;

    /**
     * paint object used for drwing the slice-text
     */
    private Paint mEntryLabelsPaint;

    private StaticLayout mCenterTextLayout;
    private CharSequence mCenterTextLastValue;
    private RectF mCenterTextLastBounds = new RectF();
    private RectF[] mRectBuffer = {new RectF(), new RectF(), new RectF()};

    /**
     * Bitmap for drawing the center hole. We create a Bitmap in drawData() function.
     * this Bitmap is connected to mBitmapCanvas and the below weak reference.
     * So it does not garbage collected until mBitmapCanvas exists in memory.
     * It will be garbage collected after mBitmapCanvas be cleared.
     */

    protected WeakReference<Bitmap> mDrawBitmap;

    protected Canvas mBitmapCanvas;



    /**
     * the animator object used to perform animations on the chart data
     */
    protected ChartAnimator mAnimator;

    /**
     * main paint object used for rendering
     */
    protected Paint mRenderPaint;

    /**
     * paint used for highlighting values
     */
    protected Paint mHighlightPaint;

    protected Paint mDrawPaint;

    /**
     * paint object for drawing values (text representing values of chart
     * entries)
     */
    protected Paint mValuePaint;

    protected Paint mBackgroundPaint;

    protected RectF circleBox;


    public PieChartRenderer(PieChart chart, ChartAnimator animator,
                            ViewPortHandler viewPortHandler) {
        this(animator, viewPortHandler);
        mChart = chart;

        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(Color.WHITE);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setAlpha(30);

        mHolePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHolePaint.setColor(Color.WHITE);
        mHolePaint.setStyle(Paint.Style.FILL);

        mTransparentCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTransparentCirclePaint.setColor(Color.WHITE);
        mTransparentCirclePaint.setStyle(Paint.Style.FILL);
        mTransparentCirclePaint.setAlpha(105);

        mCenterTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mCenterTextPaint.setColor(Color.BLACK);
        mCenterTextPaint.setTextSize(Utils.convertDpToPixel(12f));

        mValuePaint.setTextSize(Utils.convertDpToPixel(13f));
        mValuePaint.setColor(Color.WHITE);
        mValuePaint.setTextAlign(Paint.Align.CENTER);

        mEntryLabelsPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mEntryLabelsPaint.setColor(Color.WHITE);
        mEntryLabelsPaint.setTextAlign(Paint.Align.CENTER);
        mEntryLabelsPaint.setTextSize(Utils.convertDpToPixel(13f));

        mValueLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mValueLinePaint.setStyle(Paint.Style.STROKE);
    }


    public PieChartRenderer(ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(viewPortHandler);
        this.mAnimator = animator;

        mRenderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRenderPaint.setStyle(Paint.Style.FILL);

        mDrawPaint = new Paint(Paint.DITHER_FLAG);

        mValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mValuePaint.setColor(Color.rgb(63, 63, 63));
        mValuePaint.setTextAlign(Paint.Align.CENTER);
        mValuePaint.setTextSize(Utils.convertDpToPixel(9f));

        mHighlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHighlightPaint.setStyle(Paint.Style.STROKE);
        mHighlightPaint.setStrokeWidth(2f);
        mHighlightPaint.setColor(Color.rgb(255, 187, 115));
    }

    /*protected boolean isDrawingValuesAllowed(ChartInterface chart) {
        return chart.getData().getEntryCount() < chart.getMaxVisibleCount()
                * mViewPortHandler.getScaleX();
    }*/

    /**
     * Returns the Paint object this renderer uses for drawing the values
     * (value-text).
     *
     * @return
     */
    public Paint getPaintValues() {
        return mValuePaint;
    }

    /**
     * Returns the Paint object this renderer uses for drawing highlight
     * indicators.
     *
     * @return
     */
    public Paint getPaintHighlight() {
        return mHighlightPaint;
    }

    /**
     * Returns the Paint object used for rendering.
     *
     * @return
     */
    public Paint getPaintRender() {
        return mRenderPaint;
    }

    /**
     * Applies the required styling (provided by the DataSet) to the value-paint
     * object.
     *
     *
     */
    /*protected void applyValueTextStyle(IDataSet set) {

        mValuePaint.setTypeface(set.getValueTypeface());
        mValuePaint.setTextSize(set.getValueTextSize());
    }*/

    public Paint getPaintHole() {
        return mHolePaint;
    }

    public Paint getPaintTransparentCircle() {
        return mTransparentCirclePaint;
    }

    public TextPaint getPaintCenterText() {
        return mCenterTextPaint;
    }

    public Paint getPaintEntryLabels() {
        return mEntryLabelsPaint;
    }

    public void drawData(Canvas c){

        int width = (int) viewPortHandler.getChartWidth();
        int height = (int) viewPortHandler.getChartHeight();

        Bitmap drawBitmap = mDrawBitmap == null ? null : mDrawBitmap.get();

        if(drawBitmap == null || (drawBitmap.getWidth()) != width || (drawBitmap.getHeight()) != height){

            if(width > 0 && height > 0){
                drawBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
                mDrawBitmap = new WeakReference<>(drawBitmap);
                mBitmapCanvas = new Canvas(drawBitmap);
            }else{
                return;
            }
        }

        drawBitmap.eraseColor(Color.TRANSPARENT);

        PieDataSet pieDataSet = mChart.getDataSet();

        if (pieDataSet.isVisible() && pieDataSet.getEntryCount() > 0)
            drawDataSet(c, pieDataSet);
    }



    public void drawDataSet(Canvas c, PieDataSet dataSet){

        float startAngle = 0f;
        float sweepAngle;
        float phaseY = mAnimator.getPhaseY();

        RectF contBox = viewPortHandler.getContentRect();

        circleBox = mChart.getCircleBox();
        final float[] drawAngles = mChart.getDrawAngles();

        final int entryCount = dataSet.getEntryCount();

        List<Integer> colors = dataSet.getColors();
        int colorCount = colors.size();

        mBitmapCanvas.drawRect(contBox, mBackgroundPaint);

        //mBitmapCanvas.drawArc(circleBox, 0, drawAngles[0], true, mDrawPaint );

        for(int i = 0; i < entryCount; i++){

            startAngle *= phaseY;
            sweepAngle = drawAngles[i] * phaseY;
            mDrawPaint.setColor(colors.get(i%colorCount));
            mBitmapCanvas.drawArc(circleBox, (int) startAngle, (int) sweepAngle, true, mDrawPaint );
            startAngle += drawAngles[i];

        }

        float holeRadius = mChart.getHoleRadius();
        float transparentHoleRadius = mChart.getTransparentHoleRadius();
        FPoint circleBoxCenter = mChart.getCenterOfCircleBox();
        RectF holeRect = new RectF();
        /*holeRect.set(circleBoxCenter.x - holeRadius, circleBoxCenter.y - holeRadius,
                circleBoxCenter.x + holeRadius, circleBoxCenter.y + holeRadius);
        mBitmapCanvas.drawRect(holeRect, mValuePaint);*/
        mValuePaint.setAlpha(100);
        if(mChart.getDrawHole())
            mBitmapCanvas.drawCircle(circleBoxCenter.x, circleBoxCenter.y, holeRadius, mValuePaint);
        mValuePaint.setAlpha(255);
        mBitmapCanvas.drawCircle(circleBoxCenter.x, circleBoxCenter.y, transparentHoleRadius, mValuePaint);

        Path cntrTxtPath = new Path();
        cntrTxtPath.moveTo((float) circleBoxCenter.x + holeRadius, (float) circleBoxCenter.y);
        //cntrTxtPath.lineTo((float) circleBoxCenter.x + holeRadius, (float) circleBoxCenter.y);
        //cntrTxtPath.lineTo((float) circleBoxCenter.x, (float) circleBoxCenter.y +holeRadius);
        cntrTxtPath.addCircle((float) circleBoxCenter.x, (float) circleBoxCenter.y,
                (float) holeRadius-50, Path.Direction.CW);

        cntrTxtPath.close();
        //cntrTxtPath.addArc(holeRect, 0, 110);

        /*mBitmapCanvas.drawTextOnPath("Mohammad Sh. N.",
                cntrTxtPath, 10, 10, mCenterTextPaint);*/
        //mBitmapCanvas.drawText("Mohammad Sh. N.", circleBoxCenter.x, circleBoxCenter.y, mCenterTextPaint);




        //mDrawBitmap.get() returns drawBitmap created in drawData()
        c.drawBitmap(mDrawBitmap.get(), 0, 0, mDrawPaint);





      /*  for(int i = 0; i < entryCount; i++){

            sweepAngle = drawAngles[i] * phaseY;

            mRenderPaint.setColor(dataSet.getColor(i));

            mBitmapCanvas.drawArc(contBox, startAngle, sweepAngle, true, mRenderPaint);

            startAngle += sweepAngle;

        }*/

    }

    public void drawValues(Canvas c){

        float radius = mChart.getRadius();
        float holeRadius = mChart.getHoleRadius();
        float midRadius = (radius + holeRadius)/2f;

        final float[] drawAngles = mChart.getDrawAngles();
        // final float[] percentVals = new float[drawAngles.length];

        float value;

        float startAngle = 0f;
        float sweepAngle;

        for(int i = 0; i < drawAngles.length; i++){

            if(i == 0){
                startAngle = 0f;
                sweepAngle = drawAngles[0];
            }else{
                startAngle += drawAngles[i-1];
                sweepAngle = drawAngles[i];
            }

            value = sweepAngle / 3.6f;
            if(value > 3){
                drawValuesInside(c, startAngle, sweepAngle, midRadius);
            }
        }


    }

    public void drawValuesInside(Canvas c, float startAngle, float sweepAngle, float radius){

        float value = sweepAngle / 3.6f;
        String val = String.format("%.2f", value);
        //String val = "%" + value;

        Path path = insidePath(startAngle, sweepAngle, radius);

        mBitmapCanvas.drawTextOnPath(val,
                path, 1, 1, mCenterTextPaint);

        c.drawBitmap(mDrawBitmap.get(), 0, 0, mDrawPaint);


    }

    public Path insidePath(float startAngle, float sweepAngle,  float radius){

        // still without memory management
        Path path = new Path();
        //FPoint startPoint = sliceStartPoit(startAngle, radius);
        //path.moveTo(startPoint.x, startPoint.y);

        FPoint center = mChart.getCenterOfCircleBox();
        RectF rect = new RectF();
        rect.set(center.x - radius, center.y - radius, center.x + radius, center.y + radius);
        path.arcTo(rect, startAngle, sweepAngle);

        path.close();



        return path;
    }

    public FPoint angleRadiusPoint(float angle, float radius){

        FPoint center = mChart.getCenterOfCircleBox();

        center.x += radius * Math.cos(Utils.FDEG2RAD * angle);
        center.y += radius * Math.sin(Utils.FDEG2RAD * angle);

        return center;
    }

    public void drawLabels(Canvas c){

        PieDataSet<PieEntry> dataSet = mChart.getDataSet();
        float radius = mChart.getHoleRadius();

        final float[] drawAngles = mChart.getDrawAngles();
        // final float[] percentVals = new float[drawAngles.length];

        float value;

        float startAngle = 0f;
        float sweepAngle;

        for(int i = 0; i < dataSet.getEntryCount(); i++){

            String label = mChart.getDataSet().getEntryForIndex(i).getLabel();

            if(i == 0){
                startAngle = 0f;
                sweepAngle = drawAngles[0];
            }else{
                startAngle += drawAngles[i-1];
                sweepAngle = drawAngles[i];
            }

            float length = Utils.calcTextWidth(mCenterTextPaint, label);

            Path labPath = labelPath(startAngle, sweepAngle, radius, length);

            mBitmapCanvas.drawTextOnPath(label,
                    labPath, 10, 10, mCenterTextPaint);
            //mBitmapCanvas.drawText("Mohammad Sh. N.", circleBoxCenter.x, circleBoxCenter.y, mCenterTextPaint);
        }

        //mDrawBitmap.get() returns drawBitmap created in drawData()
        c.drawBitmap(mDrawBitmap.get(), 0, 0, mDrawPaint);

    }

    public Path labelPath(float startAngle, float sweepAngle,  float radius, float length){

        float midAngle = startAngle + sweepAngle / 2f;

        // still without memory management
        Path path = new Path();
        FPoint startPoint = angleRadiusPoint(midAngle, radius);
        float leftLength = radius - length > 0 ? (radius - length) : 0;
        FPoint endPoint = angleRadiusPoint(midAngle, leftLength - 50);

        path.moveTo(startPoint.x, startPoint.y);
        path.lineTo(endPoint.x, endPoint.y);

        path.close();

        return path;
    }


}

