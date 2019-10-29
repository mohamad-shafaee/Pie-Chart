package com.pink.gir.pielibrary.chart;


import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;


import com.pink.gir.pielibrary.color.ColorTemplate;
import com.pink.gir.pielibrary.utils.FPoint;
import com.pink.gir.pielibrary.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class PieDataSet<T extends PieEntry> {

    protected List<T> mYVals = null;

    private float mSliceSpace = 0f;
    private boolean mAutomaticallyDisableSliceSpacing;

    /**
     * indicates the selection distance of a pie slice
     */
    private float mShift = 18f;

    private ValuePosition mXValuePosition = ValuePosition.INSIDE_SLICE;
    private ValuePosition mYValuePosition = ValuePosition.INSIDE_SLICE;
    private boolean mUsingSliceColorAsValueLineColor = false;
    private int mValueLineColor = 0xff000000;
    private float mValueLineWidth = 1.0f;
    private float mValueLinePart1OffsetPercentage = 75.f;
    private float mValueLinePart1Length = 0.3f;
    private float mValueLinePart2Length = 0.4f;
    private boolean mValueLineVariableLength = true;


    /**
     * List representing all colors that are used for this DataSet
     */
    protected List<Integer> mColors = null;

    //protected GradientColor mGradientColor = null;

    //protected List<GradientColor> mGradientColors = null;

    /**
     * List representing all colors that are used for drawing the actual values for this DataSet
     */
    protected List<Integer> mValueColors = null;

    /**
     * label that describes the DataSet or the data the DataSet represents
     */
    private String mLabel = "DataSet";

    /**
     * if true, value highlightning is enabled
     */
    protected boolean mHighlightEnabled = true;
    /**
     * custom formatter that is used instead of the auto-formatter if set
     */
    //protected transient ValueFormatter mValueFormatter;

    /**
     * the typeface used for the value text
     */
    protected Typeface mValueTypeface;

    //private Legend.LegendForm mForm = Legend.LegendForm.DEFAULT;
    private float mFormSize = Float.NaN;
    private float mFormLineWidth = Float.NaN;
    private DashPathEffect mFormLineDashEffect = null;


    /**
     * if true, y-values are drawn on the chart
     */
    protected boolean mDrawValues = true;

    /**
     * if true, y-icons are drawn on the chart
     */
    protected boolean mDrawIcons = true;

    /**
     * the offset for drawing icons (in dp)
     */
    protected FPoint mIconsOffset;

    /**
     * the size of the value-text labels
     */
    protected float mValueTextSize = 17f;

    /**
     * flag that indicates if the DataSet is visible or not
     */
    protected boolean mVisible = true;


    /**
     * Constructor with label.
     *
     * @param label
     */
    public PieDataSet(String label) {
        this(null, label);

    }

    /**
     * Default constructor.
     */
    public PieDataSet(List<T> yVals, String label) {

        mColors = new ArrayList<Integer>();
        mValueColors = new ArrayList<Integer>();
        // default color
        mColors.add(Color.rgb(255, 255, 0));
        mColors.add(Color.rgb(100, 114, 255));
        mColors.add(Color.rgb(255, 0, 0));
        mColors.add(Color.rgb(2, 255, 0));
        mColors.add(Color.rgb(255, 0, 255));
        mValueColors.add(Color.BLACK);

        //I think un-necessary
        if (mYVals == null)
            mYVals = new ArrayList<T>();

       if( yVals != null)
        this.mYVals = yVals;

        this.mLabel = label;

        // mShift = Utils.convertDpToPixel(12f);

    }

    public PieDataSet<T> copy(){
        List<T> entries = new ArrayList<>();
        for(int i = 0; i < mYVals.size(); i++){
            // entries.add((T) mYVals.get(i).copy());
            entries.add(mYVals.get(i));
        }
        PieDataSet copied = new PieDataSet(entries, getLabel());
        return copied;
    }

    public int getSumOfEntries(){

        int sum = 0;
        int count = mYVals.size();
        for(int i = 0; i < count; i++){
            sum += mYVals.get(i).getY();
        }

        return  sum;
    }

    public void setSliceSpace(float spaceDp) {

        if (spaceDp > 20)
            spaceDp = 20f;
        if (spaceDp < 0)
            spaceDp = 0f;

        mSliceSpace = Utils.convertDpToPixel(spaceDp);
    }

    public float getSliceSpace() {
        return mSliceSpace;
    }

    public void setAutomaticallyDisableSliceSpacing(boolean autoDisable) {
        mAutomaticallyDisableSliceSpacing = autoDisable;
    }

    public boolean isAutomaticallyDisableSliceSpacingEnabled() {
        return mAutomaticallyDisableSliceSpacing;
    }

    public void setSelectionShift(float shift) {
        mShift = Utils.convertDpToPixel(shift);
    }

    // @Override
    public float getSelectionShift() {
        return mShift;
    }

    // @Override
    public ValuePosition getXValuePosition() {
        return mXValuePosition;
    }

    public void setXValuePosition(ValuePosition xValuePosition) {
        this.mXValuePosition = xValuePosition;
    }

    // @Override
    public ValuePosition getYValuePosition() {
        return mYValuePosition;
    }

    public void setYValuePosition(ValuePosition yValuePosition) {
        this.mYValuePosition = yValuePosition;
    }

    /**
     * When valuePosition is OutsideSlice, use slice colors as line color if true
     */
    //@Override
    public boolean isUsingSliceColorAsValueLineColor() {
        return mUsingSliceColorAsValueLineColor;
    }

    public void setUsingSliceColorAsValueLineColor(boolean usingSliceColorAsValueLineColor) {
        this.mUsingSliceColorAsValueLineColor = usingSliceColorAsValueLineColor;
    }

    /**
     * When valuePosition is OutsideSlice, indicates line color
     */
    //@Override
    public int getValueLineColor() {
        return mValueLineColor;
    }

    public void setValueLineColor(int valueLineColor) {
        this.mValueLineColor = valueLineColor;
    }

    /**
     * When valuePosition is OutsideSlice, indicates line width
     */
    //@Override
    public float getValueLineWidth() {
        return mValueLineWidth;
    }

    public void setValueLineWidth(float valueLineWidth) {
        this.mValueLineWidth = valueLineWidth;
    }

    /**
     * When valuePosition is OutsideSlice, indicates offset as percentage out of the slice size
     */
    //@Override
    public float getValueLinePart1OffsetPercentage() {
        return mValueLinePart1OffsetPercentage;
    }

    public void setValueLinePart1OffsetPercentage(float valueLinePart1OffsetPercentage) {
        this.mValueLinePart1OffsetPercentage = valueLinePart1OffsetPercentage;
    }

    /**
     * When valuePosition is OutsideSlice, indicates length of first half of the line
     */
    //@Override
    public float getValueLinePart1Length() {
        return mValueLinePart1Length;
    }

    public void setValueLinePart1Length(float valueLinePart1Length) {
        this.mValueLinePart1Length = valueLinePart1Length;
    }

    /**
     * When valuePosition is OutsideSlice, indicates length of second half of the line
     */
    //@Override
    public float getValueLinePart2Length() {
        return mValueLinePart2Length;
    }

    public void setValueLinePart2Length(float valueLinePart2Length) {
        this.mValueLinePart2Length = valueLinePart2Length;
    }

    /**
     * When valuePosition is OutsideSlice, this allows variable line length
     */
    //@Override
    public boolean isValueLineVariableLength() {
        return mValueLineVariableLength;
    }

    public void setValueLineVariableLength(boolean valueLineVariableLength) {

        this.mValueLineVariableLength = valueLineVariableLength;
    }

    /**
     * Returns the array of entries that this DataSet represents.
     *
     * @return
     */
    public List<T> getValues() {
        return mYVals;
    }

    /**
     * Sets the array of entries that this DataSet represents, and calls notifyDataSetChanged()
     *
     * @return
     */
    public void setValues(List<T> values){
        this.mYVals = values;
        //notifyDataSetChanged();
    }

    public String toSimpleString(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("Data Set, Label: " + (getLabel() == null ? "" : getLabel()) +
                ", Entries Number: " + mYVals.size() + "\n");
        return buffer.toString();
    }

    public String toString(){
        StringBuffer buffer = new StringBuffer();
        buffer.append(toSimpleString());
        for(int i = 0; i < mYVals.size(); i++){
            buffer.append(mYVals.get(i).toString() + "\n");
        }
        return buffer.toString();
    }

    public void clear() {
        mYVals.clear();
    }

    public boolean addEntry(T e){

        if (e == null)
            return false;
        if(mYVals == null){
            mYVals = new ArrayList<T>();
        }
        boolean add = mYVals.add(e);
        return add;
    }

    public boolean removeEntry(T e){
        if(e == null)
            return false;

        if(mYVals == null)
            return false;

        boolean removed = mYVals.remove(e);
        return removed;
    }

    //@Override
    public T getEntryForIndex(int index) {
        return mYVals.get(index);
    }

    public int getEntryIndex(T e){
        return mYVals.indexOf(e);
    }

    //@Override
    public int getEntryCount() {
        return mYVals.size();
    }

    //@Override
    public int getIndexForLabel(String label) {

        for (int i = 0; i < getEntryCount(); i++) {
            if (label.equals(getEntryForIndex(i).getLabel()))
                return i;
        }

        return -1;
    }

    //@Override
    public boolean contains(T e) {

        for (int i = 0; i < getEntryCount(); i++) {
            if (getEntryForIndex(i).equalTo(e))
                return true;
        }

        return false;
    }

    /**
     * Use this method to tell the data set that the underlying data has changed.
     */
    /*public void notifyDataSetChanged() {
        //calcMinMax();
    }*/

    /**
     * ###### ###### COLOR GETTING RELATED METHODS ##### ######
     */

    //@Override
    public List<Integer> getColors() {
        return mColors;
    }

    public List<Integer> getValueColors() {
        return mValueColors;
    }

    //@Override
    public int getColor() {
        return mColors.get(0);
    }

    //@Override
    public int getColor(int index) {
        return mColors.get(index % mColors.size());
    }



    /**
     * ###### ###### COLOR SETTING RELATED METHODS ##### ######
     */

    /**
     * Sets the colors that should be used fore this DataSet. Colors are reused
     * as soon as the number of Entries the DataSet represents is higher than
     * the size of the colors array. If you are using colors from the resources,
     * make sure that the colors are already prepared (by calling
     * getResources().getColor(...)) before adding them to the DataSet.
     *
     * @param colors
     */
    public void setColors(List<Integer> colors) {
        this.mColors = colors;
    }

    /**
     * Sets the colors that should be used fore this DataSet. Colors are reused
     * as soon as the number of Entries the DataSet represents is higher than
     * the size of the colors array. If you are using colors from the resources,
     * make sure that the colors are already prepared (by calling
     * getResources().getColor(...)) before adding them to the DataSet.
     *
     * @param colors
     */
    public void setColors(int... colors) {
        this.mColors = ColorTemplate.createColors(colors);
    }

    /**
     * Sets the colors that should be used fore this DataSet. Colors are reused
     * as soon as the number of Entries the DataSet represents is higher than
     * the size of the colors array. You can use
     * "new int[] { R.color.red, R.color.green, ... }" to provide colors for
     * this method. Internally, the colors are resolved using
     * getResources().getColor(...)
     *
     * @param colors
     */
    public void setColors(int[] colors, Context c) {

        if (mColors == null) {
            mColors = new ArrayList<>();
        }

        mColors.clear();

        for (int color : colors) {
            mColors.add(c.getResources().getColor(color));
        }
    }

    /**
     * Adds a new color to the colors array of the DataSet.
     *
     * @param color
     */
    public void addColor(int color) {
        if (mColors == null)
            mColors = new ArrayList<Integer>();
        mColors.add(color);
    }

    /**
     * Sets the one and ONLY color that should be used for this DataSet.
     * Internally, this recreates the colors array and adds the specified color.
     *
     * @param color
     */
    public void setColor(int color) {
        resetColors();
        mColors.add(color);
    }

    /**
     * Sets a color with a specific alpha value.
     *
     * @param color
     * @param alpha from 0-255
     */
    public void setColor(int color, int alpha) {
        setColor(Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color)));
    }

    /**
     * Sets colors with a specific alpha value.
     *
     * @param colors
     * @param alpha
     */
    public void setColors(int[] colors, int alpha) {
        resetColors();
        for (int color : colors) {
            addColor(Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color)));
        }
    }

    /**
     * Resets all colors of this DataSet and recreates the colors array.
     */
    public void resetColors() {
        if (mColors == null) {
            mColors = new ArrayList<Integer>();
        }
        mColors.clear();
    }

    /**
     * ###### ###### OTHER STYLING RELATED METHODS ##### ######
     */

    //@Override
    public void setLabel(String label) {
        mLabel = label;
    }

    //@Override
    public String getLabel() {
        return mLabel;
    }

    //@Override
    public void setHighlightEnabled(boolean enabled) {
        mHighlightEnabled = enabled;
    }

    //@Override
    public boolean isHighlightEnabled() {
        return mHighlightEnabled;
    }

    //@Override
    /*public void setValueFormatter(ValueFormatter f) {

        if (f == null)
            return;
        else
            mValueFormatter = f;
    }*/

    //@Override
    /*public ValueFormatter getValueFormatter() {
        if (needsFormatter())
            return Utils.getDefaultValueFormatter();
        return mValueFormatter;
    }*/

    //@Override
    /*public boolean needsFormatter() {
        return mValueFormatter == null;
    }*/

    //@Override
    public void setValueTextColor(int color) {
        mValueColors.clear();
        mValueColors.add(color);
    }

    //@Override
    public void setValueTextColors(List<Integer> colors) {
        mValueColors = colors;
    }

    //@Override
    public void setValueTypeface(Typeface tf) {
        mValueTypeface = tf;
    }

    //@Override
    public void setValueTextSize(float size) {
        mValueTextSize = Utils.convertDpToPixel(size);
    }

    //@Override
    public int getValueTextColor() {
        return mValueColors.get(0);
    }

    //@Override
    public int getValueTextColor(int index) {
        return mValueColors.get(index % mValueColors.size());
    }

    //@Override
    public Typeface getValueTypeface() {
        return mValueTypeface;
    }

    //@Override
    public float getValueTextSize() {
        return mValueTextSize;
    }

    /*public void setForm(Legend.LegendForm form) {
        mForm = form;
    }*/

    //@Override
    /*public Legend.LegendForm getForm() {
        return mForm;
    }*/

    public void setFormSize(float formSize) {
        mFormSize = formSize;
    }

    //@Override
    public float getFormSize() {
        return mFormSize;
    }

    public void setFormLineWidth(float formLineWidth) {
        mFormLineWidth = formLineWidth;
    }

    //@Override
    public float getFormLineWidth() {
        return mFormLineWidth;
    }

    public void setFormLineDashEffect(DashPathEffect dashPathEffect) {
        mFormLineDashEffect = dashPathEffect;
    }

    //@Override
    public DashPathEffect getFormLineDashEffect() {
        return mFormLineDashEffect;
    }

    //@Override
    public void setDrawValues(boolean enabled) {
        this.mDrawValues = enabled;
    }

    //@Override
    public boolean isDrawValuesEnabled() {
        return mDrawValues;
    }

    // @Override
    public void setDrawIcons(boolean enabled) {
        mDrawIcons = enabled;
    }

    // @Override
    public boolean isDrawIconsEnabled() {
        return mDrawIcons;
    }

    //@Override
    public void setIconsOffset(FPoint offsetDp) {

        mIconsOffset.x = offsetDp.x;
        mIconsOffset.y = offsetDp.y;
    }

    //@Override
    public FPoint getIconsOffset() {
        return mIconsOffset;
    }

    //@Override
    public void setVisible(boolean visible) {
        mVisible = visible;
    }

    //@Override
    public boolean isVisible() {
        return mVisible;
    }

    public enum ValuePosition {
        INSIDE_SLICE,
        OUTSIDE_SLICE
    }
}

