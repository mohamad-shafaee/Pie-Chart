package com.pink.gir.pielibrary.handler;


import android.graphics.Matrix;
import android.graphics.RectF;
import android.view.View;

import com.pink.gir.pielibrary.utils.FPoint;
import com.pink.gir.pielibrary.utils.Utils;


public class ViewPortHandler {

    /**
     * matrix used for touch events
     */
    protected final Matrix mMatrixTouch = new Matrix();

    /**
     * this rectangle defines the area in which graph values can be drawn
     */
    protected RectF mContentRect = new RectF();

    // mChartWidth = offsetLeft + mContentRect.width + offsetRight
    // mChartHeight = offsetTop + mContentRect.height + offsetBottom
    protected float mChartWidth = 0f;
    protected float mChartHeight = 0f;

    protected float offsetLeft = 0f;
    protected float offsetTop = 0f;
    protected float offsetRight = 0f;
    protected float offsetBottom = 0f;

    /**
     * minimum scale value on the y-axis
     */
    private float mMinScaleY = 1f;

    /**
     * maximum scale value on the y-axis
     */
    private float mMaxScaleY = Float.MAX_VALUE;

    /**
     * minimum scale value on the x-axis
     */
    private float mMinScaleX = 1f;

    /**
     * maximum scale value on the x-axis
     */
    private float mMaxScaleX = Float.MAX_VALUE;

    /**
     * contains the current scale factor of the x-axis
     */
    private float mScaleX = 1f;

    /**
     * contains the current scale factor of the y-axis
     */
    private float mScaleY = 1f;

    /**
     * current translation (drag distance) on the x-axis
     */
    private float mTransX = 0f;

    /**
     * current translation (drag distance) on the y-axis
     */
    private float mTransY = 0f;

    /**
     * offset that allows the chart to be dragged over its bounds on the x-axis
     */
    private float mTransOffsetX = 0f;

    /**
     * offset that allows the chart to be dragged over its bounds on the x-axis
     */
    private float mTransOffsetY = 0f;

    /**
     * Constructor - don't forget calling setOffsets() and setChartDimens(...)
     */
    public ViewPortHandler() {

    }

    /**
     * Sets the offsets of the chart. The arguments must be set deliberately! that is small proper values
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setOffsets(float left, float top, float right, float bottom){
        this.offsetLeft = left;
        this.offsetTop = top;
        this.offsetRight = right;
        this.offsetBottom = bottom;
    }


    /**
     * Sets the width and height of the chart.
     *
     * @param width
     * @param height
     */

    public void setChartDimens(float width, float height){

        mChartWidth = width;
        mChartHeight = height;

        mContentRect.set(
                offsetLeft, offsetTop,
                mChartWidth - offsetRight, mChartHeight - offsetBottom);

    }

    /*public float offsetLeft(){
        return mContentRect.left;
    }
    public float offsetRight(){
        return mChartWidth - mContentRect.right;
    }

    public float offsetTop(){
        return mContentRect.top;
    }

    public float offsetBottom(){
        return mChartHeight - mContentRect.bottom;
    }*/

    public boolean hasChartDimens() {
        if (mChartHeight > 0 && mChartWidth > 0)
            return true;
        else
            return false;
    }

    public float contentTop() {
        return mContentRect.top;
    }

    public float contentLeft() {
        return mContentRect.left;
    }

    public float contentRight() {
        return mContentRect.right;
    }

    public float contentBottom() {
        return mContentRect.bottom;
    }

    public float contentWidth() {
        return mContentRect.width();
    }

    public float contentHeight() {
        return mContentRect.height();
    }

    public RectF getContentRect() {
        return mContentRect;
    }

    public FPoint getContentCenter() {
        return FPoint.getInstance(mContentRect.centerX(), mContentRect.centerY());
    }

    /*
     * related setter is setChartDimens() defined above
     */
    public float getChartHeight() {
        return mChartHeight;
    }

    public float getChartWidth() {
        return mChartWidth;
    }

    /**
     * Returns the smallest extension of the content rect (width or height).
     *
     * @return
     */
    public float getSmallestContentExtension() {
        return Math.min(mContentRect.width(), mContentRect.height());
    }


    /**
     * ################ ################ ################ ################
     */
    /** CODE BELOW THIS RELATED TO SCALING AND GESTURES */

    /**
     * Zooms in by 1.4f, x and y are the coordinates (in pixels) of the zoom
     * center.
     *
     * @param x
     * @param y
     */
    public Matrix zoomIn(float x, float y){
        Matrix save = new Matrix();
        zoomIn(x, y, save);
        return save;
    }

    public void zoomIn(float x, float y, Matrix outputMatrix){
        outputMatrix.reset();
        outputMatrix.set(mMatrixTouch);
        outputMatrix.postScale(1.4f, 1.4f, x, y);
    }

    /**
     * Zooms out by 0.7f, x and y are the coordinates (in pixels) of the zoom
     * center.
     */

    public Matrix zoomOut(float x, float y){
        Matrix save = new Matrix();
        zoomOut(x, y, save);
        return save;
    }

    public void zoomOut(float x, float y, Matrix outputMatrix){
        outputMatrix.reset();
        outputMatrix.set(mMatrixTouch);
        outputMatrix.postScale(0.7f, 0.7f, x, y);
    }

    /**
     * Zooms out to original size.
     * @param outputMatrix
     */
    public void resetZoom(Matrix outputMatrix){
        outputMatrix.reset();
        outputMatrix.set(mMatrixTouch);
        outputMatrix.postScale(1f, 1f, 0,0);
    }

    /**
     * Post-scales by the specified scale factors.
     *
     * @param scaleX
     * @param scaleY
     * @return
     */
    public Matrix zoom(float scaleX, float scaleY){
        Matrix save = new Matrix();
        zoom(scaleX, scaleY, save);
        return save;
    }

    public void zoom(float scaleX, float scaleY, Matrix outputMatrix){
        outputMatrix.reset();
        outputMatrix.set(mMatrixTouch);
        outputMatrix.postScale(scaleX, scaleY);
    }

    /**
     * Post-scales by the specified scale factors. x and y is pivot.
     *
     * @param scaleX
     * @param scaleY
     * @param x
     * @param y
     * @return
     */
    public Matrix zoom(float scaleX, float scaleY, float x, float y){
        Matrix save = new Matrix();
        zoom(scaleX, scaleY, x, y, save);
        return save;
    }

    public void zoom(float scaleX, float scaleY, float x, float y, Matrix outputMatrix){
        outputMatrix.reset();
        outputMatrix.set(mMatrixTouch);
        outputMatrix.postScale(scaleX, scaleY, x, y);
    }

    /**
     * Sets the scale factor to the specified values.
     *
     * @param scaleX
     * @param scaleY
     * @return
     */
    public Matrix setZoom(float scaleX, float scaleY) {

        Matrix save = new Matrix();
        setZoom(scaleX, scaleY, save);
        return save;
    }

    public void setZoom(float scaleX, float scaleY, Matrix outputMatrix) {
        outputMatrix.reset();
        outputMatrix.set(mMatrixTouch);
        outputMatrix.setScale(scaleX, scaleY);
    }

    /**
     * Sets the scale factor to the specified values. x and y is pivot.
     *
     * @param scaleX
     * @param scaleY
     * @param x
     * @param y
     * @return
     */
    public Matrix setZoom(float scaleX, float scaleY, float x, float y) {

        Matrix save = new Matrix();
        save.set(mMatrixTouch);

        save.setScale(scaleX, scaleY, x, y);

        return save;
    }

    protected float[] valsBufferForFitScreen = new float[9];

    /**
     * Resets all zooming and dragging and makes the chart fit exactly it's
     * bounds.
     */
    public Matrix fitScreen() {

        Matrix save = new Matrix();
        fitScreen(save);
        return save;
    }

    /**
     * Resets all zooming and dragging and makes the chart fit exactly it's
     * bounds.  Output Matrix is available for those who wish to cache the object.
     */
    public void fitScreen(Matrix outputMatrix) {
        mMinScaleX = 1f;
        mMinScaleY = 1f;

        outputMatrix.set(mMatrixTouch);

        float[] vals = valsBufferForFitScreen;
        for (int i = 0; i < 9; i++) {
            vals[i] = 0;
        }

        outputMatrix.getValues(vals);

        // reset all translations and scaling
        vals[Matrix.MTRANS_X] = 0f;
        vals[Matrix.MTRANS_Y] = 0f;
        vals[Matrix.MSCALE_X] = 1f;
        vals[Matrix.MSCALE_Y] = 1f;

        outputMatrix.setValues(vals);
    }

    /**
     * Post-translates to the specified points.  Less Performant.
     *
     * @param transformedPts
     * @return
     */
    public Matrix translate(final float[] transformedPts){
        Matrix save = new Matrix();
        translate(transformedPts, save);
        return save;
    }

    /**
     * Post-translates to the specified points.  Output matrix allows for caching objects.
     *
     * @param transformedPts
     * @return
     */
    public void translate(final float[] transformedPts, Matrix outputMatrix){
        outputMatrix.reset();
        float x = transformedPts[0];
        float y = transformedPts[1];
        outputMatrix.setTranslate(-x, -y);
    }

    protected Matrix mCenterViewPortMatrixBuffer = new Matrix();

    /**
     * Centers the viewport around the specified position (x-index and y-value)
     * in the chart. Centering the viewport outside the bounds of the chart is
     * not possible. Makes most sense in combination with the
     * setScaleMinima(...) method.
     *
     * @param transformedPts the position to center view viewport to
     * @param view
     * @return save
     */
    public void centerViewPort(final float[] transformedPts, final View view){
        Matrix save = mCenterViewPortMatrixBuffer;
        save.reset();
        save.set(mMatrixTouch);

        final float x = transformedPts[0];
        final float y = transformedPts[1];
        save.postTranslate(-x, -y);

        refresh(save, view, true);
    }

    /**
     * call this method to refresh the graph with a given matrix
     *
     * @param newMatrix
     * @return
     */
    public Matrix refresh(Matrix newMatrix, View chart, boolean invalidate) {

        mMatrixTouch.set(newMatrix);

        // make sure scale and translation are within their bounds
        limitTransAndScale(mMatrixTouch, mContentRect);

        if (invalidate)
            chart.invalidate();

        newMatrix.set(mMatrixTouch);
        return newMatrix;
    }

    /**
     * buffer for storing the 9 matrix values of a 3x3 matrix
     */
    protected final float[] matrixBuffer = new float[9];


    /**
     * limits the maximum scale and X translation of the given matrix
     *
     * @param matrix
     */
    public void limitTransAndScale(Matrix matrix, RectF content){

        matrix.getValues(matrixBuffer);

        float curTransX = matrixBuffer[Matrix.MTRANS_X];
        float curScaleX = matrixBuffer[Matrix.MSCALE_X];
        float curTransY = matrixBuffer[Matrix.MTRANS_Y];
        float curScaleY = matrixBuffer[Matrix.MSCALE_Y];

        mScaleX = Math.min(Math.max(mMinScaleX, curScaleX), mMaxScaleX);
        mScaleY = Math.min(Math.max(mMinScaleY, curScaleY), mMaxScaleY);

        float width = 0f;
        float height = 0f;

        if (content != null) {
            width = content.width();
            height = content.height();
        }

        float maxTransX = width * (mScaleX - 1f);
        mTransX = Math.min(curTransX, maxTransX + mTransOffsetX);

        float maxTrasY = height * (mScaleY - 1f);
        mTransY = Math.min(curTransY, maxTrasY + mTransOffsetY);

        matrixBuffer[Matrix.MSCALE_X] = mScaleX;
        matrixBuffer[Matrix.MSCALE_Y] = mScaleY;
        matrixBuffer[Matrix.MTRANS_X] = mTransX;
        matrixBuffer[Matrix.MTRANS_Y] = mTransY;

        matrix.setValues(matrixBuffer);
    }

    /**
     * Sets the minimum scale factor for the x-axis
     *
     * @param xScale
     */
    public void setMinimumScaleX(float xScale) {

        if (xScale < 1f)
            xScale = 1f;

        mMinScaleX = xScale;

        limitTransAndScale(mMatrixTouch, mContentRect);
    }

    /**
     * Sets the maximum scale factor for the x-axis
     *
     * @param xScale
     */
    public void setMaximumScaleX(float xScale) {

        if (xScale == 0.f)
            xScale = Float.MAX_VALUE;

        mMaxScaleX = xScale;

        limitTransAndScale(mMatrixTouch, mContentRect);
    }

    /**
     * Sets the minimum and maximum scale factors for the x-axis
     *
     * @param minScaleX
     * @param maxScaleX
     */
    public void setMinMaxScaleX(float minScaleX, float maxScaleX) {

        if (minScaleX < 1f)
            minScaleX = 1f;

        if (maxScaleX == 0.f)
            maxScaleX = Float.MAX_VALUE;

        mMinScaleX = minScaleX;
        mMaxScaleX = maxScaleX;

        limitTransAndScale(mMatrixTouch, mContentRect);
    }

    /**
     * Sets the minimum scale factor for the y-axis
     *
     * @param yScale
     */
    public void setMinimumScaleY(float yScale) {

        if (yScale < 1f)
            yScale = 1f;

        mMinScaleY = yScale;

        limitTransAndScale(mMatrixTouch, mContentRect);
    }

    /**
     * Sets the maximum scale factor for the y-axis
     *
     * @param yScale
     */
    public void setMaximumScaleY(float yScale) {

        if (yScale == 0.f)
            yScale = Float.MAX_VALUE;

        mMaxScaleY = yScale;

        limitTransAndScale(mMatrixTouch, mContentRect);
    }

    public void setMinMaxScaleY(float minScaleY, float maxScaleY) {

        if (minScaleY < 1f)
            minScaleY = 1f;

        if (maxScaleY == 0.f)
            maxScaleY = Float.MAX_VALUE;

        mMinScaleY = minScaleY;
        mMaxScaleY = maxScaleY;

        limitTransAndScale(mMatrixTouch, mContentRect);
    }

    /**
     * Returns the charts-touch matrix used for translation and scale on touch.
     *
     * @return
     */
    public Matrix getMatrixTouch() {
        return mMatrixTouch;
    }

    /**
     * ################ ################ ################ ################
     */
    /**
     * BELOW METHODS FOR BOUNDS CHECK
     */

    public boolean isInBoundsX(float x) {
        return isInBoundsLeft(x) && isInBoundsRight(x);
    }

    public boolean isInBoundsY(float y) {
        return isInBoundsTop(y) && isInBoundsBottom(y);
    }

    public boolean isInBounds(float x, float y) {
        return isInBoundsX(x) && isInBoundsY(y);
    }

    public boolean isInBoundsLeft(float x) {
        return mContentRect.left <= x + 1;
    }

    public boolean isInBoundsRight(float x) {
        x = (float) ((int) (x * 100.f)) / 100.f;
        return mContentRect.right >= x - 1;
    }

    public boolean isInBoundsTop(float y) {
        return mContentRect.top <= y;
    }

    public boolean isInBoundsBottom(float y) {
        y = (float) ((int) (y * 100.f)) / 100.f;
        return mContentRect.bottom >= y;
    }

    /**
     * returns the current x-scale factor
     */
    public float getScaleX() {
        return mScaleX;
    }

    /**
     * returns the current y-scale factor
     */
    public float getScaleY() {
        return mScaleY;
    }

    public float getMinScaleX() {
        return mMinScaleX;
    }

    public float getMaxScaleX() {
        return mMaxScaleX;
    }

    public float getMinScaleY() {
        return mMinScaleY;
    }

    public float getMaxScaleY() {
        return mMaxScaleY;
    }

    /**
     * Returns the translation (drag / pan) distance on the x-axis
     *
     * @return
     */
    public float getTransX() {
        return mTransX;
    }

    /**
     * Returns the translation (drag / pan) distance on the y-axis
     *
     * @return
     */
    public float getTransY() {
        return mTransY;
    }

    /**
     * if the chart is fully zoomed out, return true
     *
     * @return
     */
    public boolean isFullyZoomedOut() {

        return isFullyZoomedOutX() && isFullyZoomedOutY();
    }
    /**
     * Returns true if the chart is fully zoomed out on it's y-axis (vertical).
     *
     * @return
     */
    public boolean isFullyZoomedOutY() {
        return !(mScaleY > mMinScaleY || mMinScaleY > 1f);
    }

    /**
     * Returns true if the chart is fully zoomed out on it's x-axis
     * (horizontal).
     *
     * @return
     */
    public boolean isFullyZoomedOutX() {
        return !(mScaleX > mMinScaleX || mMinScaleX > 1f);
    }

    /**
     * Set an offset in dp that allows the user to drag the chart over it's
     * bounds on the x-axis.
     *
     * @param offset
     */
    public void setDragOffsetX(float offset) {
        mTransOffsetX = Utils.convertDpToPixel(offset);
    }

    /**
     * Set an offset in dp that allows the user to drag the chart over it's
     * bounds on the y-axis.
     *
     * @param offset
     */
    public void setDragOffsetY(float offset) {
        mTransOffsetY = Utils.convertDpToPixel(offset);
    }

    /**
     * Returns true if both drag offsets (x and y) are zero or smaller.
     *
     * @return
     */
    public boolean hasNoDragOffset() {
        return mTransOffsetX <= 0 && mTransOffsetY <= 0;
    }

    /**
     * Returns true if the chart is not yet fully zoomed out on the x-axis
     *
     * @return
     */
    public boolean canZoomOutMoreX() {
        return mScaleX > mMinScaleX;
    }

    /**
     * Returns true if the chart is not yet fully zoomed in on the x-axis
     *
     * @return
     */
    public boolean canZoomInMoreX() {
        return mScaleX < mMaxScaleX;
    }

    /**
     * Returns true if the chart is not yet fully zoomed out on the y-axis
     *
     * @return
     */
    public boolean canZoomOutMoreY() {
        return mScaleY > mMinScaleY;
    }

    /**
     * Returns true if the chart is not yet fully zoomed in on the y-axis
     *
     * @return
     */
    public boolean canZoomInMoreY() {
        return mScaleY < mMaxScaleY;
    }

}

