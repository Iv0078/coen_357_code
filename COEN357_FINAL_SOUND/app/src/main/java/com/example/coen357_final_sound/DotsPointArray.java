package com.example.coen357_final_sound;

public class DotsPointArray {
    final float[] bufferArray;

    final int bufferSize, numValuesPerPoint,numPointPerElement,numElements;
    int currPos;

    DotsPointArray(int numElements, int numPointPerElement){
        this(numElements,numPointPerElement,2);
    }


    public DotsPointArray(int numElements, int numPointPerElement, int numValuesPerPoint) {
        this.currPos = 0;
        this.numElements = numElements;
        this.numPointPerElement = numPointPerElement;
        this.numValuesPerPoint = numValuesPerPoint;
        this.bufferSize = numElements*numPointPerElement*numValuesPerPoint;
        this.bufferArray = new float[this.bufferSize];
    }

    public boolean add(float... args){
        int numInputval = args.length;
        if(numInputval!=numPointPerElement*numValuesPerPoint){
            return false;
        }
        for(int i=0;i<numInputval;i++){
            bufferArray[(currPos+i)%bufferSize] = args[i];
        }
        currPos= (currPos+numInputval)%bufferSize;
        return true;
    }

    public float[] getArray(){
        return this.getIndexedArray(0);
    }
    public float[] getIndexedArray(int startIndex){
        float[] opbuffer = new float[this.bufferSize];
        int actualPosition =0;
        for(int i =0;i<numElements;i++){
            for(int j=0;j<numPointPerElement;j++){
                actualPosition = i*numPointPerElement*numValuesPerPoint+j*numPointPerElement;
                opbuffer[actualPosition]=i+startIndex;
                for(int k=1; k <numValuesPerPoint;k++){
                    actualPosition++;
                    opbuffer[actualPosition]=bufferArray[(currPos+actualPosition)%bufferSize];
                }
            }
        }
        return opbuffer;
    }




}
