package com.pink.gir.pielibrary.utils;


public class ObjectPool <T extends ObjectPool.Poolable> {

    private static int ids = 0;
    private int poolId;
    private int capacity;
    private Object[] objects; // private Poolable[] objects;
    private int objectsPointer;
    private Poolable modelObject;
    private float replenishPercentage;

    /**
     * Returns the id of the given pool instance.
     *
     * @return an integer ID belonging to this pool instance.
     */
    public int getPoolId(){
        return poolId;
    }

    public static synchronized <T extends Poolable> ObjectPool create(int capacity, T model){
        ObjectPool result = new ObjectPool(capacity, model);
        result.poolId = ids;
        ids++;

        return result;
    }

    private <T extends Poolable> ObjectPool(int capacity, T model){
        if(capacity < 0){

        }
        this.capacity = capacity;
        this.objects = new Object[capacity];
        this.objectsPointer = 0;
        this.modelObject = model;
        this.replenishPercentage = 1f;
        this.refillPool();
    }

    private void refillPool(){
        this.refillPool(this.replenishPercentage);
    }

    private void refillPool(float p){


        int realCapacity  = (int) (capacity * p);
       /* if(realCapacity < 1){
            realCapacity = 1;
        }else if(realCapacity > capacity){
            realCapacity = capacity;
        }*/

        for(int i = 0; i < realCapacity; i++){
            T modelObj = (T) modelObject.instantiate();
            modelObj.currentOwnerId = ids;
            this.objects[i] = modelObj;
            //this.objects[i] = modelObject.instantiate();
        }

        objectsPointer = realCapacity - 1;
    }

    public void setRelenshipPercent(float percentage){
        float p = percentage;
        if(p < 0f){
            p = 0f;
        }else if(p > 1f){
            p = 1f;
        }
        this.replenishPercentage = p;
    }

    public float getReplenishPercentage(){
        return replenishPercentage;
    }

    public synchronized T get(){
        if(objectsPointer == -1 && replenishPercentage > 0f){
            refillPool();
        }
        T result = (T) objects[objectsPointer];
        objectsPointer--;
        result.currentOwnerId = Poolable.NO_OWNER;
        return result;
    }

    /*
     * Recycle an instance of Poolable that this pool is capable of generating.
     * The T instance passed must not already exist inside this or any other ObjectPool instance.
     *
     * @param an object of type T to recycle
     */
    public synchronized void recycle(T object){

        if(object.currentOwnerId != Poolable.NO_OWNER){
            if(object.currentOwnerId == poolId){
                throw new IllegalArgumentException("The object passed is already stored in this pool!");
            }else{
                throw new IllegalArgumentException("The object to recycle already belongs to poolId "
                        + object.currentOwnerId + ".  Object cannot belong to two different" +
                        " pool instances simultaneously!");

            }
        }

        this.objectsPointer++;
        if(this.objectsPointer >= objects.length){
            this.resizePool();
        }

        object.currentOwnerId = this.poolId;
        objects[this.objectsPointer] = object;

    }

    private void resizePool(){
        final int oldCapacity = this.capacity;
        this.capacity *= 2;
        Object[] temp = new Object[this.capacity];
        for(int i=0; i<oldCapacity; i++){
            temp[i] = this.objects[i];
        }
        this.objects = temp;
    }

    public int getPoolCapacity(){
        return this.objects.length;
    }
    public int getPoolCount(){
        return this.objectsPointer + 1;
    }







    public static abstract class Poolable{

        public static int NO_OWNER = -1;
        public int currentOwnerId = NO_OWNER;

        protected abstract Poolable instantiate();

    }
}

