package com.pink.gir.pielibrary.utils;


public class FPoint extends ObjectPool.Poolable {

    private static ObjectPool<FPoint> pool;

    public float x;
    public float y;

    static{
        pool = ObjectPool.create(10, new FPoint(0,0));
        pool.setRelenshipPercent(0.5f);
    }

    public FPoint(float x, float y){

        this.x = x;
        this.y = y;
    }

    public FPoint(){}

    public static FPoint getInstance(float x, float y){
        FPoint result = pool.get();
        result.x = x;
        result.y = y;
        return result;
    }

    public static FPoint getInstance(){
        return pool.get();
    }

    public static void recycleInstance(FPoint instance){
        pool.recycle(instance);
    }

    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }

    @Override
    protected ObjectPool.Poolable instantiate(){
        return new FPoint(0,0);
    }


}

