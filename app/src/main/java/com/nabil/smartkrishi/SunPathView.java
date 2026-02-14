package com.nabil.smartkrishi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.content.ContextCompat;

public class SunPathView extends View {
    private Paint arcPaint;
    private Path arcPath;
    private RectF arcRect;
    private float progress = 0.0f;
    private Drawable sunIcon;
    private float[] pos = new float[2]; // Stores [x, y] coordinates
    private PathMeasure pathMeasure;

    public SunPathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(4f);
        arcPaint.setColor(0xFFCCCCCC); // Light gray for the path
        // Creates the dotted line effect: 10px dash, 10px gap
        arcPaint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));

        arcPath = new Path();
        arcRect = new RectF();
        pathMeasure = new PathMeasure();

        // Use a sun icon from your drawables
        sunIcon = ContextCompat.getDrawable(context, R.drawable.ic_sun);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float w = getWidth();
        float h = getHeight();

        // 1. Draw the Arc
        arcPath.reset();
        // Adjust these values to fit your card's height/padding
        arcRect.set(100, 50, w - 100, h * 2 - 50);
        arcPath.addArc(arcRect, 180, 180);
        canvas.drawPath(arcPath, arcPaint);

        // 2. Calculate Sun Position
        pathMeasure.setPath(arcPath, false);
        float distance = pathMeasure.getLength() * progress;
        pathMeasure.getPosTan(distance, pos, null);

        // 3. Draw Sun Icon at calculated x, y
        if (sunIcon != null) {
            int size = 80; // Size of sun in pixels
            sunIcon.setBounds((int)pos[0] - size/2, (int)pos[1] - size/2,
                    (int)pos[0] + size/2, (int)pos[1] + size/2);
            sunIcon.draw(canvas);
        }
    }

    public void setProgress(float p) {
        this.progress = p;
        invalidate(); // Refreshes the view
    }
}