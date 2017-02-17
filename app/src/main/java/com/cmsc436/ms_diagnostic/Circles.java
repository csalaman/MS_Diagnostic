package com.cmsc436.ms_diagnostic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;




    import android.util.AttributeSet;
    import android.view.View;

    public class Circles extends View {
        int width = this.getWidth();
        int height = this.getHeight();
        Paint paint;
        Path path;
        static final int X_COOR = 400;
        static final int Y_COOR = 500;

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


            //paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(this.getWidth()/2, this.getHeight()/2, 170, paint);
            canvas.drawCircle(this.getWidth()/2, this.getHeight()/2, 270, paint);
            canvas.drawCircle(this.getWidth()/2, this.getHeight()/2, 370, paint);
            //drawCircle(cx, cy, radius, paint)

        }

    }