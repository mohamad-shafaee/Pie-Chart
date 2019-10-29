package com.pink.gir.pielibrary.hilight;


public class Hilight {


    /**
     * the index of the hilighted entry
     */
    private int mEntryIndex = -1;

    /**
     * the y value of the entry
     */
    private int mEntryY = 0;



    public Hilight(int index, int yEntry) {

        this.mEntryIndex = index;
        this.mEntryY = yEntry;
    }


    /**
     * the index of Entry
     *
     * @return
     */
    public int getEntryIndex() {
        return mEntryIndex;
    }

    public boolean equalTo(Hilight h){

        if(h == null){
            return false;
        }else{
            if(h.mEntryIndex == this.mEntryIndex){
                return true;
            }else {
                return false;
            }
        }
    }



}

