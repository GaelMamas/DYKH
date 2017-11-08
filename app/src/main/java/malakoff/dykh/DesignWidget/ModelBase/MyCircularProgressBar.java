package malakoff.dykh.DesignWidget.ModelBase;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import malakoff.dykh.DesignWidget.Utils;
import malakoff.dykh.R;


/**
 * Created by user on 12/01/2016.
 */
public class MyCircularProgressBar extends View {

    private static final String INSTANCE_PROGRESS_ANGLE = "saved_progressangle";
    private final float default_progress_angle = 10;
    private final int default_progress_color = Color.BLACK;
    RectF progressRectF = new RectF();
    private float progressAngle;
    private float progressStroke;
    private int progressColor, progressShadowColor;
    private Paint progressPaint;
    private Paint progressShadowPaint;
    private int viewMinimumSize;

    public MyCircularProgressBar(Context context) {
        this(context, null);
    }

    public MyCircularProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCircularProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        progressShadowColor = ContextCompat.getColor(context, R.color.progress_shadow_color);

        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TimeClipper, defStyleAttr, 0);

        progressStroke = attributes.getFloat(R.styleable.TimeClipper_progress_stroke, Utils.convertDpToPixel(getResources(), 5));
        progressAngle = attributes.getFloat(R.styleable.TimeClipper_progress_angle, default_progress_angle);
        progressColor = attributes.getColor(R.styleable.TimeClipper_progress_color, default_progress_color);
        viewMinimumSize = attributes.getInt(R.styleable.TimeClipper_view_minimum_size, (int) Utils.convertDpToPixel(getResources(), 200));

        initPaint();
    }

    private void initPaint() {
        progressPaint = new Paint();
        progressPaint.setColor(progressColor);
        progressPaint.setStrokeWidth(progressStroke);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setAntiAlias(true);

        progressShadowPaint = new Paint();
        progressShadowPaint.setColor(progressShadowColor);
        progressShadowPaint.setStrokeWidth(progressStroke);
        progressShadowPaint.setStyle(Paint.Style.STROKE);
        progressShadowPaint.setAntiAlias(true);
    }

    @Override
    public void invalidate() {
        initPaint();
        super.invalidate();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec));
    }

    private int measure(int measureSpec) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = viewMinimumSize;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        progressRectF.set(progressStroke,
                progressStroke,
                getWidth() - progressStroke,
                getHeight() - progressStroke);

        canvas.drawArc(progressRectF, 0, progressAngle, false, progressPaint);

        canvas.drawArc(progressRectF, progressAngle, 360 - progressAngle, false, progressShadowPaint);
    }

    public float getProgressAngle() {
        return progressAngle;
    }

    public void setProgressAngle(float progressAngle) {
        this.progressAngle = progressAngle;

        if (this.progressAngle > 360) {
            this.progressAngle %= 360;
        }

        //Log.d("setProgressAngle","progressAngle = " + progressAngle);

        invalidate();
    }
}
