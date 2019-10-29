package com.pink.gir.pielibrary.chart;


import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class PieEntry implements Parcelable {

    /** the y value*/
    private int y;

    /** the x value */
    private String label = "";

    /** optional icon image */
    private Drawable mIcon = null;

    public PieEntry(){}

    public PieEntry(int n, String lab){
        this.y = n;
        this.label = lab;

    }

    public PieEntry(int n, String lab, Drawable icon){
        this.y = n;
        this.label = lab;
        this.mIcon = icon;
    }

    /**
     * Sets the y-value for the Entry.
     *
     * @param n
     */
    public void setY(int n) {
        this.y = n;
    }

    /**
     * Returns the y value of this Entry.
     *
     * @return
     */
    public int getY(){return y;}

    /**
     * Sets the icon drawable
     *
     * @param icon
     */
    public void setIcon(Drawable icon) {
        this.mIcon = icon;
    }

    /**
     * Returns the icon of this Entry.
     *
     * @return
     */
    public Drawable getIcon() {
        return mIcon;
    }

    public void setLabel(String lab){
        this.label = lab;
    }

    public String getLabel(){
        return this.label;
    }

    public boolean equalTo(PieEntry e){
        if(e == null){
            return false;
        }
        if(!(e.getLabel()).equals(this.getLabel())){
            return false;
        }

        if(e.getY() != this.getY()){
            return false;
        }

        return true;
    }

    public PieEntry copy(){
        PieEntry e = new PieEntry(this.getY(), this.getLabel(), this.getIcon());
        return e;
    }

    @Override
    public String toString(){
        return "PieEntry: n = " + this.getY() + " and Label = " + this.getLabel();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeInt(this.y);
        dest.writeString(this.label);

    }

    public static final Parcelable.Creator<PieEntry> CREATOR =
            new Parcelable.Creator<PieEntry>(){

                public PieEntry createFromParcel(Parcel in){
                    return new PieEntry(in);
                }

                public PieEntry[] newArray(int size){
                    return new PieEntry[size];
                }

            };

    private PieEntry(Parcel in){
        this.y = in.readInt();
        this.label = in.readString();
    }

}
