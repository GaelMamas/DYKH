package malakoff.dykh.DesignWidget.ModelBase;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import malakoff.dykh.DesignWidget.Utils;
import malakoff.dykh.R;


/**
 * Created by user on 19/12/2015.
 */
public class ArcButton extends View {

    private static final String INSTANCE_SAVE = "saved_instance";
    private static final String INSTANCE_ANGLE = "currentAngle";
    private static final String INSTANCE_TEXT_SIZE = "text_size";
    private static final String INSTANCE_TEXT_COLOR = "text_color";
    private static final String INSTANCE_ARC_COLOR = "arc_color";
    private final String default_arcTitle = "era";
    private final int default_textColor = Color.BLACK;
    private final int default_arcColor = Color.YELLOW;
    private RectF arcRect = new RectF();
    private float littleRadius, greatRadius;
    private float currentAngle;
    private Paint textPaint, arcPaint;
    private float textSize;
    private int textColor;
    private int arcColor;
    private String arcTitle;
    private float default_littleRadius;
    private float default_greatRadius;
    private float default_angle;
    private float default_previous;
    private float default_textSize;
    private float previousAngle;

    public ArcButton(Context context) {
        this(context, null, 0);
    }

    public ArcButton(Context context, AttributeSet attrs, int arcNumerous) {
        this(context, attrs, 0, 3, 360 * arcNumerous / 3);
    }

    public ArcButton(Context context, AttributeSet attrs, int defStyleAttr, int arcNumber, float previousAngle) {
        super(context, attrs, defStyleAttr);
        this.default_littleRadius = Utils.convertDpToPixel(getResources(), 20);
        this.default_greatRadius = Utils.convertDpToPixel(getResources(), 40);
        this.default_angle = 360 / arcNumber;
        this.default_textSize = Utils.convertSpToPixel(getResources(), 14);
        this.default_previous = previousAngle;

        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcButton, defStyleAttr, 0);
        initByAttributes(attributes);

        attributes.recycle();

        initPainters();
    }


    protected void initPainters() {
        textPaint = new TextPaint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);

        arcPaint = new Paint();
        arcPaint.setColor(arcColor);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setAntiAlias(true);
        arcPaint.setStrokeWidth(Math.abs(greatRadius - littleRadius));
    }

    protected void initByAttributes(TypedArray attributes) {
        currentAngle = attributes.getFloat(R.styleable.ArcButton_arcbutton_angle, default_angle);
        previousAngle = attributes.getFloat(R.styleable.ArcButton_arcbutton_previous, default_previous);
        littleRadius = attributes.getFloat(R.styleable.ArcButton_arcbutton_little_radius, default_littleRadius);
        greatRadius = attributes.getFloat(R.styleable.ArcButton_arcbutton_great_radius, default_greatRadius);

        textColor = attributes.getColor(R.styleable.ArcButton_arcbutton_text_color, default_textColor);
        textSize = attributes.getDimension(R.styleable.ArcButton_arcbutton_text_size, default_textSize);
        arcColor = attributes.getColor(R.styleable.ArcButton_arcbutton_arc_color, default_arcColor);

        if (attributes.getString(R.styleable.ArcButton_arcbutton_title) != null) {
            arcTitle = attributes.getString(R.styleable.ArcButton_arcbutton_title);
        }
    }

    @Override
    public void invalidate() {
        initPainters();
        super.invalidate();
    }

    public float getLittleRadius() {
        return littleRadius;
    }

    public void setLittleRadius(float littleRadius) {
        this.littleRadius = littleRadius;
    }

    public float getGreatRadius() {
        return greatRadius;
    }

    public void setGreatRadius(float greatRadius) {
        this.greatRadius = greatRadius;
    }

    public float getCurrentAngle() {
        return currentAngle;
    }

    public void setCurrentAngle(float currentAngle) {
        this.currentAngle = currentAngle;
    }

    public String getArcTitle() {
        return arcTitle;
    }

    public void setArcTitle(String arcTitle) {
        this.arcTitle = arcTitle;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        arcRect.set(10, 10, 200, 200);

        canvas.drawArc(arcRect, previousAngle, currentAngle, false, arcPaint);
        if (!TextUtils.isEmpty(arcTitle)) {
            canvas.drawText(arcTitle, arcRect.centerX(), arcRect.centerY(), textPaint);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();

        bundle.putParcelable(INSTANCE_SAVE, super.onSaveInstanceState());
        bundle.putFloat(INSTANCE_ANGLE, currentAngle);
        bundle.putInt(INSTANCE_ARC_COLOR, arcColor);
        bundle.putInt(INSTANCE_TEXT_COLOR, textColor);
        bundle.putFloat(INSTANCE_TEXT_SIZE, textSize);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            currentAngle = bundle.getFloat(INSTANCE_ANGLE);
            arcColor = bundle.getInt(INSTANCE_ARC_COLOR);
            textColor = bundle.getInt(INSTANCE_TEXT_COLOR);
            textSize = bundle.getFloat(INSTANCE_TEXT_SIZE);
        }

        super.onRestoreInstanceState(state);
    }
}
