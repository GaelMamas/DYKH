package malakoff.dykh.DesignWidget.ModelBase;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

import malakoff.dykh.AppApplication.Constants;
import malakoff.dykh.DesignWidget.Utils;
import malakoff.dykh.Interfaces.OnClickDetectArcListener;
import malakoff.dykh.R;
import malakoff.dykh.Utils.TimeClipperDecoder;
import malakoff.dykh.Utils.UsefulGenericMethods;
import malakoff.dykh.Utils.UsefulMathMethods;

/**
 * Created by user on 22/12/2015.
 */
public class TimeClipperButton extends View implements OnClickDetectArcListener {

    private static final String INSTANCE_STATE = "saved_instance";
    private static final String INSTANCE_VIEW_MIN_SIZE = "saved_instance_view_min_size";
    private static final String INSTANCE_OUTER_STROKE = "saved_outerstroke";
    private static final String INSTANCE_INNER_STROKE = "saved_innerstroke";
    private static final String INSTANCE_PROGRESS_ANGLE = "saved_progressangle";
    private static final String INSANCE_TEXT_TO_DISPALAY = "saved_texttodisplay";
    private static final String INSTANCE_ERA_TITLES = "saved_eratitles";

    private static final float DEFAULT_RETRO_POSITION = 5f;
    private final float default_progress_angle = 10;
    private final int default_inner_arc_number = 4;
    private final int default_circle_color = Color.WHITE;
    private final int default_progress_color = Color.BLUE;
    RectF outerRectF = new RectF();
    RectF innerRectF = new RectF();
    RectF progressRectF = new RectF();
    Random rnd;
    private String[] eraTitles;
    private String textToDisplay;
    private float progressAngle;
    private float innerCircleRadius;
    private float outerStroke, innerStroke, progressStroke;
    private int progressColor, circleColor, progressShadowColor;
    private int innerArcNumber, viewMinimumSize;
    private ArcDrawer[] innerArcDrawers;
    private Paint progressPaint;
    private Paint circlePaint;
    private Paint progressShadowPaint;
    private TextPaint textPaint;
    private TextPaint middleTextPaint;

    private TimeClipperDecoder clipperDecoder;
    private boolean shouldDraw = false, isBrutalChangedEra = false;
    private float mTimeCounter = 0;
    private int currentEraIndex = 0;

    public TimeClipperButton(Context context) {
        this(context, null);
    }

    public TimeClipperButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeClipperButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        progressShadowColor = ContextCompat.getColor(context, R.color.progress_shadow_color);

        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TimeClipper, defStyleAttr, 0);

        initByAttributes(attributes);

        attributes.recycle();

        initOtherAttributes();

        initPaint();
    }


    private void initByAttributes(TypedArray attributes) {
        outerStroke = attributes.getFloat(R.styleable.TimeClipper_outer_stroke, Utils.convertDpToPixel(getResources(), 20));
        innerStroke = attributes.getFloat(R.styleable.TimeClipper_inner_stroke, Utils.convertDpToPixel(getResources(), 15));
        progressStroke = attributes.getFloat(R.styleable.TimeClipper_progress_stroke, Utils.convertDpToPixel(getResources(), 5));
        progressAngle = attributes.getFloat(R.styleable.TimeClipper_progress_angle, default_progress_angle);
        innerArcNumber = attributes.getInt(R.styleable.TimeClipper_inner_arc_number, default_inner_arc_number);
        viewMinimumSize = attributes.getInt(R.styleable.TimeClipper_view_minimum_size, (int) Utils.convertDpToPixel(getResources(), 200));

        progressColor = attributes.getColor(R.styleable.TimeClipper_progress_color, default_progress_color);
        circleColor = attributes.getColor(R.styleable.TimeClipper_circle_color, default_circle_color);

        if (attributes.getResourceId(R.styleable.TimeClipper_era_titles, 0) != 0) {
            eraTitles = getResources().getStringArray(R.array.era_titles);
        }

    }


    private void initOtherAttributes() {
        if (eraTitles != null && eraTitles.length > 0) {
            innerArcNumber = eraTitles.length;
        }

        innerArcDrawers = new ArcDrawer[innerArcNumber];
    }

    private void initPaint() {

        rnd = new Random();

        for (int j = 0; j < innerArcNumber; j++) {

            Paint innerPaint = new Paint();

            innerPaint.setColor(progressShadowColor);
            innerPaint.setStrokeWidth(innerStroke);
            innerPaint.setStyle(Paint.Style.STROKE);
            innerPaint.setAntiAlias(true);


            innerArcDrawers[j] = new ArcDrawer(innerPaint,
                    j==0? -2141408028:j==1? -2136976647:j==2? -2132745752:j==3? -2143460918:-2137885921,
                    //Color.argb(128, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)),
                    360 * j / innerArcNumber,
                    360 / innerArcNumber,
                    (eraTitles != null && eraTitles.length > 0) ? eraTitles[j] : "Era " + j);

        }

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

        circlePaint = new Paint();
        circlePaint.setColor(circleColor);
        circlePaint.setAntiAlias(true);

        middleTextPaint = new TextPaint();
        middleTextPaint.setColor(Color.BLACK);
        middleTextPaint.setTextSize(Utils.convertSpToPixel(getResources(), 10f));
        middleTextPaint.setAntiAlias(true);


        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setTextSize(Utils.convertSpToPixel(getResources(), 10f));


        if(clipperDecoder == null) {
            clipperDecoder = new TimeClipperDecoder(UsefulGenericMethods.getDefaultTimeLine(eraTitles));
            textToDisplay = clipperDecoder.getDecodedTime();
        }

    }

    @Override
    public void invalidate() {
        initOtherAttributes();
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

        currentEraIndex = clipperDecoder.getCurrentEra();

        drawViewBases(canvas);

        drawEraArcs(canvas, currentEraIndex);

        drawInsideViewText(canvas);

        drawCurrentEraArcAnimation(canvas, currentEraIndex);

        drawPerpheralCircleAnimation(canvas);
    }

    private void drawPerpheralCircleAnimation(Canvas canvas) {
        canvas.drawArc(progressRectF, 0, progressAngle, false, progressPaint);
        canvas.drawArc(progressRectF, progressAngle, 360 - progressAngle, false, progressShadowPaint);
    }


    private void drawCurrentEraArcAnimation(Canvas canvas, int currentEra) {
        paintCurrentEraArc(currentEra);
        canvas.drawArc(innerRectF, innerArcDrawers[currentEra].getStartAngle(), clipperDecoder.getEraIncrement(), false, innerArcDrawers[currentEra].getPaint());
    }

    private void drawInsideViewText(Canvas canvas) {
        if (!TextUtils.isEmpty(textToDisplay)) {
            float textHeight = middleTextPaint.descent() + middleTextPaint.ascent();
            canvas.drawText(textToDisplay, (getWidth() - textPaint.measureText(textToDisplay)) / 2.0f, (getWidth() - textHeight) / 2.0f, textPaint);
        }
    }

    private void drawEraArcs(Canvas canvas, int currentEra) {
        for (int j = 0; j < innerArcNumber; j++) {

            if (currentEra > j || mTimeCounter == Constants.BENCH_TIME_ORIGINE + Constants.ARBITRARY_END_OF_TIMES) {
                innerArcDrawers[j].getPaint().setColor(innerArcDrawers[j].getOwnColor());
            } else {
                innerArcDrawers[j].getPaint().setColor(progressShadowColor);
            }

            canvas.drawArc(innerRectF,
                    innerArcDrawers[j].getStartAngle(),
                    innerArcDrawers[j].getSweepAngle(),
                    false, innerArcDrawers[j].getPaint());

            innerArcDrawers[j].setPath(innerRectF);

            CharSequence myBShorthenTitle = TextUtils.ellipsize(innerArcDrawers[j].getTitle(), textPaint, innerArcDrawers[j].getSweepAngle(), TextUtils.TruncateAt.END);

            float textWidth = textPaint.measureText(myBShorthenTitle, 0, myBShorthenTitle.length());

            canvas.drawTextOnPath(myBShorthenTitle.toString(), innerArcDrawers[j].getPath(), innerArcDrawers[j].getSweepAngle() - textWidth / 2f, innerStroke / 4f, textPaint);
        }
    }

    private void drawViewBases(Canvas canvas) {

        innerCircleRadius = getHeight() == getWidth() ? getWidth() / 2 - (innerStroke + progressStroke) : Float.NaN;

        outerRectF.set(outerStroke,
                outerStroke,
                getWidth() - outerStroke,
                getHeight() - outerStroke);

        innerRectF.set(innerStroke + progressStroke,
                innerStroke + progressStroke,
                getWidth() - (innerStroke + progressStroke),
                getHeight() - (innerStroke + progressStroke));

        progressRectF.set(progressStroke,
                progressStroke,
                getWidth() - progressStroke,
                getHeight() - progressStroke);

        canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, innerCircleRadius, circlePaint);

    }

    private void paintCurrentEraArc(int currentEra) {
        switch (currentEra) {
            case 4:
                innerArcDrawers[4].getPaint().setColor(innerArcDrawers[4].getOwnColor());
                break;
            case 3:
                innerArcDrawers[3].getPaint().setColor(innerArcDrawers[3].getOwnColor());
                break;
            case 2:
                innerArcDrawers[2].getPaint().setColor(innerArcDrawers[2].getOwnColor());
                break;
            case 1:
                innerArcDrawers[1].getPaint().setColor(innerArcDrawers[1].getOwnColor());
                break;
            case 0:
                innerArcDrawers[0].getPaint().setColor(innerArcDrawers[0].getOwnColor());
                break;
            default:
        }
    }

    public synchronized float getProgressAngle() {
        return progressAngle;
    }

    public void setProgressAngle(float progressAngle) {

        this.progressAngle = progressAngle;

        if (this.progressAngle > 360) {
            this.progressAngle %= 360;
        }

        //Log.d("setProgressAngle", "progressAngle = " + progressAngle);

        invalidate();
    }

    public float getOuterStroke() {
        return outerStroke;
    }

    public void setOuterStroke(float outerStroke) {
        this.outerStroke = outerStroke;
    }

    public float getInnerStroke() {
        return innerStroke;
    }

    public void setInnerStroke(float innerStroke) {
        this.innerStroke = innerStroke;
    }

    public float getProgressStroke() {
        return progressStroke;
    }

    public void setProgressStroke(float progressStroke) {
        this.progressStroke = progressStroke;
    }

    public int getViewMinimumSize() {
        return viewMinimumSize;
    }

    public float getInnerCircleRadius() throws ArithmeticException {
        if (Float.isNaN(innerCircleRadius))
            new ArithmeticException("This geometric figure is not a square");

        return innerCircleRadius;
    }

    public float getMidCircleRadius() throws ArithmeticException {
        if (Float.isNaN(innerCircleRadius))
            new ArithmeticException("This geometric figure is not a square");

        return innerCircleRadius + innerStroke;
    }

    public float getOuterCircleRadius() {
        return getHeight() == getWidth() ? getWidth() / 2f : Float.NaN;
    }

    public float getCenterX() {
        return getHeight() == getWidth() ? getWidth() / 2f : Float.NaN;
    }

    public float getCenterY() {
        return getHeight() == getWidth() ? getWidth() / 2f : Float.NaN;
    }

    public float getXstarter() {
        return getHeight() == getWidth() ? getWidth() : Float.NaN;
    }

    public float getYstarter() {
        return getHeight() == getWidth() ? getWidth() / 2f : Float.NaN;
    }

    public float getXcursor() throws ArithmeticException {
        if (Float.isNaN(getCenterX())) {
            throw new ArithmeticException("This geometric figure is not a square");
        }

        return getCenterX() + getOuterCircleRadius() * (float) Math.cos(interpretProgressAngleInTrigoCircle(progressAngle));
    }

    public float getYcursor() throws ArithmeticException {
        if (Float.isNaN(getCenterY())) {
            throw new ArithmeticException("This geometric figure is not a square");
        }

        return getCenterY() + getOuterCircleRadius() * (float) Math.sin(interpretProgressAngleInTrigoCircle(progressAngle));
    }

    public float getXPreviousCursorPosition() {
        if (Float.isNaN(getCenterX())) {
            throw new ArithmeticException("This geometric figure is not a square");
        }

        return getCenterX() + getOuterCircleRadius()
                * (float) Math.cos(interpretProgressAngleInTrigoCircle(progressAngle >= 5 ? progressAngle - DEFAULT_RETRO_POSITION : 355));
    }

    public float getYPreviousCursorPosition() {
        if (Float.isNaN(getCenterY())) {
            throw new ArithmeticException("This geometric figure is not a square");
        }

        return getCenterY() + getOuterCircleRadius()
                * (float) Math.sin(interpretProgressAngleInTrigoCircle(progressAngle >= 5 ? progressAngle - DEFAULT_RETRO_POSITION : 355));
    }

    public String[] getEraTitles() {
        return eraTitles;
    }

    public int getCurrentEraIndex() {
        return currentEraIndex;
    }

    public String getTextToDisplay() {
        return textToDisplay;
    }

    public void setTextToDisplay(String textToDisplay) {
        this.textToDisplay = textToDisplay;
        invalidate();
    }

    public boolean isBrutalChangedEra() {
        return isBrutalChangedEra;
    }

    /**
     * Compute the angle according the trigonomitric way
     *
     * @return
     */

    public float interpretProgressAngleInTrigoCircle(float angle) {

        double angleInRadian = angle;

        if (angleInRadian < 180) {
            angleInRadian = -angleInRadian * Math.PI / 180f;
        } else if (angleInRadian == 180) {
            angleInRadian = Math.PI;
        } else if (angleInRadian > 180) {
            angleInRadian = (360 - angleInRadian) * Math.PI / 180f;
        } else {
            this.progressAngle = 0;
            return 0;
        }

        return (float) angleInRadian;
    }

    /**
     * @param eventX
     * @param eventY
     * @return
     */

    @Override
    public int onMyTouchEvent(float eventX, float eventY, float angle) {

        if (Float.isNaN(eventX) || Float.isNaN(eventY)) return -1;

        if (isInEraCrowned(eventX, eventY)) {

            for (int j = 0; j < innerArcNumber; j++) {

                if (isInThisEraSlice(j, angle)) {

                    return j;
                }
            }

        }

        return -1;
    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);

        float outerCircleRadius = innerCircleRadius + innerStroke;

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float recentX = motionEvent.getX();
                float recentY = motionEvent.getY();
                float norm = UsefulMathMethods.
                        computeDistanceBetwen2Points(getCenterX(), getCenterY(),
                                recentX, recentY);

                float cosine = UsefulMathMethods.cosThruScalProductBasedOnCenter(recentX, recentY, getCenterX(), getCenterY(),
                        getXstarter(), getYstarter(), getYstarter(), norm);

                try {
                    float gapAngle = UsefulMathMethods.getResusltAngle(recentY,
                            getCenterY(), cosine);

                    if (norm > outerCircleRadius) {
                        shouldDraw = true;
                    } else if (norm > innerCircleRadius) {
                        try {
                            int selectedEra = onMyTouchEvent(motionEvent.getX(), motionEvent.getY(), gapAngle);
                            String squareTitle = innerArcDrawers[selectedEra].getTitle();
                            if (!TextUtils.isEmpty(squareTitle)) {
                                isBrutalChangedEra = true;
                            }

                            mTimeCounter = clipperDecoder.shiftTime(selectedEra);
                            clipperDecoder.onDetectCounterChange(mTimeCounter);
                            this.textToDisplay = clipperDecoder.getDecodedTime();
                            setProgressAngle(Constants.ANGLE_ZERO);

                        }catch (IndexOutOfBoundsException e){
                            e.getMessage();
                        }
                    }
                } catch (ArithmeticException e) {
                    e.printStackTrace();
                }

                break;
            case MotionEvent.ACTION_UP:
                shouldDraw = false;

                break;

            case MotionEvent.ACTION_MOVE:
                if (shouldDraw) {
                    float currentX = motionEvent.getX(),
                            currentY = motionEvent.getY();

                    float dynNorm = UsefulMathMethods.
                            computeDistanceBetwen2Points(getCenterX(), getCenterY(),
                                    currentX, currentY);

                    float dynCosine = UsefulMathMethods.cosThruScalProductBasedOnCenter(motionEvent.getX(), motionEvent.getY(), getCenterX(), getCenterY(),
                            getXstarter(), getYstarter(), getYstarter(), dynNorm);

                    try {
                        float newAngle = UsefulMathMethods.getResusltAngle(currentY,
                                getCenterY(), dynCosine);

                        isBrutalChangedEra = false;

                        mTimeCounter += UsefulMathMethods.updateCircleCounter(progressAngle, newAngle, dynCosine);

                        Log.d("updateCounter", "Counter = " + mTimeCounter);

                        if (mTimeCounter < 0) {

                            mTimeCounter = 0;
                            this.progressAngle = Constants.ANGLE_ZERO;
                            this.textToDisplay = Constants.BEGINNING_OF_MANTIME;

                        } else if (mTimeCounter > Constants.ARBITRARY_END_OF_TIMES + Constants.BENCH_TIME_ORIGINE) {

                            mTimeCounter = Constants.ARBITRARY_END_OF_TIMES + Constants.BENCH_TIME_ORIGINE;
                            this.progressAngle = newAngle;
                            this.textToDisplay = Constants.ARBITRARY_END_OF_MANTIME;

                        } else {

                            setProgressAngle(newAngle);
                            clipperDecoder.onDetectCounterChange(mTimeCounter);
                            this.textToDisplay = clipperDecoder.getDecodedTime();
                        }


                    } catch (ArithmeticException e) {
                        e.printStackTrace();
                    }

                }

                break;
        }

        return true;
    }


    /**
     * @param x
     * @param y
     * @return
     * @throws ArithmeticException
     */


    private boolean isInPeriodCrowned(float x, float y) throws ArithmeticException {

        if (Float.isNaN(getCenterX()) || Float.isNaN(getCenterY())) {
            throw new ArithmeticException("This geometric figure is not a square");
        }

        float distanceFromCenter = (float) Math.sqrt((x - getCenterX()) * (x - getCenterX()) + (y - getCenterY()) * (y - getCenterY()));


        return distanceFromCenter > getMidCircleRadius() && distanceFromCenter < getOuterCircleRadius();
    }


    /**
     * @param x
     * @param y
     * @return
     * @throws ArithmeticException
     */

    private boolean isInEraCrowned(float x, float y) throws ArithmeticException {

        if (Float.isNaN(getCenterX()) || Float.isNaN(getCenterY())) {
            throw new ArithmeticException("This geometric figure is not a square");
        }

        float distanceFromCenter = (float) Math.sqrt((x - getCenterX()) * (x - getCenterX()) + (y - getCenterY()) * (y - getCenterY()));

        return distanceFromCenter > getInnerCircleRadius() && distanceFromCenter < getMidCircleRadius();
    }

    /**
     * @param j
     * @return
     * @throws IllegalArgumentException
     * @throws ArithmeticException
     */

    private boolean isInThisEraSlice(int j, float angle) throws IllegalArgumentException, ArithmeticException {
        if (j < 0 || j > innerArcNumber) throw new IllegalArgumentException(j + " Out of Bound");

        if (Float.isNaN(angle)) throw new ArithmeticException("Unknown Angle");

        return angle >= innerArcDrawers[j].getStartAngle() && angle <= innerArcDrawers[j].getFromZeroToSweepAngle();
    }

    /**
     * @return
     */

    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();

        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putInt(INSTANCE_VIEW_MIN_SIZE, getViewMinimumSize());
        bundle.putFloat(INSTANCE_OUTER_STROKE, getOuterStroke());
        bundle.putFloat(INSTANCE_INNER_STROKE, getInnerStroke());
        bundle.putFloat(INSTANCE_PROGRESS_ANGLE, getProgressAngle());
        bundle.putString(INSANCE_TEXT_TO_DISPALAY, getTextToDisplay());
        bundle.putStringArray(INSTANCE_ERA_TITLES, getEraTitles());

        SavedState savedState = new SavedState(bundle);

        return savedState;
    }

    /**
     * @param state
     */

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {

            final Bundle bundle = (Bundle) ((SavedState) state).getSuperState();

            viewMinimumSize = bundle.getInt(INSTANCE_VIEW_MIN_SIZE);
            outerStroke = bundle.getFloat(INSTANCE_OUTER_STROKE);
            innerStroke = bundle.getFloat(INSTANCE_INNER_STROKE);
            progressAngle = bundle.getFloat(INSTANCE_PROGRESS_ANGLE);
            textToDisplay = bundle.getString(INSANCE_TEXT_TO_DISPALAY);
            eraTitles = bundle.getStringArray(INSTANCE_ERA_TITLES);

            initOtherAttributes();
            initPaint();

            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
        }

        super.onRestoreInstanceState(state);
    }

    static class SavedState extends BaseSavedState {
        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
        int stateToSave;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.stateToSave = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.stateToSave);
        }
    }
}
