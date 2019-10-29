package com.pink.gir.pielibrary.renderer;


import com.pink.gir.pielibrary.handler.ViewPortHandler;

public abstract class Renderer {

    /**
     * the component that handles the drawing area of the chart and it's offsets
     */
    protected ViewPortHandler viewPortHandler;

    public Renderer(ViewPortHandler vph){
        this.viewPortHandler = vph;
    }
}
