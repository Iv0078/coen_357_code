package com.example.coen357_final_sound;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class ScreenVisualization extends View {

    private static final int MAX_AMPLITUDE = 32767;
    private DotsPointArray vectors;
    private  DotsPointArray amplitudes;
    private ColorScheme colorScheme;
    private int widht, height;
    public ScreenVisualization(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        colorScheme = new ColorScheme();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.widht = w;
        this.height = h;
        this.amplitudes = new DotsPointArray(this.widht/2,1);
        this.vectors = new DotsPointArray(this.widht/2,2);
    }

    public void addAmplitude(int ampitude){
        invalidate();
        float scaledHeight = ((float) ampitude/MAX_AMPLITUDE)*(height-1);
        amplitudes.add(0,height-scaledHeight);
        vectors.add(0,height,0,height-scaledHeight);
    }

    @Override
    protected  void onDraw(Canvas canvas){
        colorScheme.shuffle();
        canvas.drawPaint(colorScheme.CanvasPaint);
        canvas.drawLines(vectors.getIndexedArray(0),colorScheme.LinePaint);
        canvas.drawLine(widht/2,height,widht/2,0,colorScheme.LinePaint);
        canvas.drawPoints(amplitudes.getIndexedArray(this.widht/2),colorScheme.CirclePaint);

    }
}
