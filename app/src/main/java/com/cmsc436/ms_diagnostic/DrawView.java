package com.cmsc436.ms_diagnostic;

import android.content.Context;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created By Lauren and Shubham P.
 * This Class implements a Drawable View to be used for the Tracing Activity
 */

public class DrawView extends View {
    public DrawView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        float X = getX();
        float Y = getY();

        Path drawPath = new Path();

        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(X,Y);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.moveTo(X,Y);
                break;
            case MotionEvent.ACTION_UP:
                drawPath.moveTo(X,Y);
                break;
            default: return false;
        }
        invalidate();
        return true;
    }
}
