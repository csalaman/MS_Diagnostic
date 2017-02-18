package com.cmsc436.ms_diagnostic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;
import android.util.AttributeSet;

/**
 * Khanh N.
 *
 * This Class Implements a Custom View for Drawing Circles for Balance Activity
 */

public class Circles extends View {
    int width = this.getWidth();
    int height = this.getHeight();
    Paint paint;
    Path path;

    public Circles(Context context) {
        super(context);
        init();
    }

    public Circles(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Circles(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        paint.setStyle(Paint.Style.STROKE);

        canvas.drawCircle(width/2, height/2, width/3, paint);
        canvas.drawCircle(width/2, height/2, width * 2/ 3, paint);
        canvas.drawCircle(width/2, height/2, width, paint);


    }
}
