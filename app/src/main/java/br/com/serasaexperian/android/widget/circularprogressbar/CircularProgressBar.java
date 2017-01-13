package br.com.serasaexperian.android.widget.circularprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import br.com.serasaexperian.circularprogressbar.R;

/**
 * Created by lepkoski on 1/12/17.
 */

public class CircularProgressBar extends View {

    private HorizontalAlignment horizontalAlignment;
    private VerticalAlignment verticalAlignment;
    private float startAngle;
    private float endAngle;
    private int strokeColor;
    private int progressBarColor;
    private int shapeBackgroundColor;
    private float strokeWidth;
    private float progressBarWidth;
    private float left;
    private float top;
    private float right;
    private float bottom;
    private float animationProgress = 0.5f;
    private float shapeSize;

    public CircularProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CircularProgressBar,
                0, 0);

        try {
            int horizontalAlignmentInt = a.getInt(R.styleable.CircularProgressBar_horizontalAlignment, HorizontalAlignment.CENTER.getId());
            horizontalAlignment = HorizontalAlignment.fromId(horizontalAlignmentInt);

            int verticalAlignmentInt = a.getInt(R.styleable.CircularProgressBar_verticalAlignment, VerticalAlignment.CENTER.getId());
            verticalAlignment = VerticalAlignment.fromId(verticalAlignmentInt);

            startAngle = a.getFloat(R.styleable.CircularProgressBar_startAngle, 0);
            endAngle = a.getFloat(R.styleable.CircularProgressBar_endAngle, 0);
            strokeWidth = a.getFloat(R.styleable.CircularProgressBar_strokeWidth, 1);
            progressBarWidth = a.getFloat(R.styleable.CircularProgressBar_progressBarWidth, 2);
            strokeColor = a.getInt(R.styleable.CircularProgressBar_strokeColor, Color.BLACK);
            progressBarColor = a.getInt(R.styleable.CircularProgressBar_progressBarColor, Color.RED);
            shapeBackgroundColor = a.getInt(R.styleable.CircularProgressBar_shapeBackgroundColor, Color.TRANSPARENT);
        } finally {
            a.recycle();
        }
    }

    @NonNull
    private RectF createRectF() {
        float drawableWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        float drawableHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        if (drawableWidth < drawableHeight) {
            shapeSize = drawableWidth;
        } else {
            shapeSize = drawableHeight;
        }

        final float progressBarMargin = progressBarWidth/2f;

        left = getPaddingLeft() + progressBarMargin;
        top = getPaddingTop() + progressBarMargin;
        right = left + shapeSize - progressBarWidth;
        bottom = top + shapeSize - progressBarWidth;

        if (shapeSize < drawableWidth) {
            switch (horizontalAlignment) {
                case LEFT:
                    // do nothing
                    break;
                case RIGHT:
                    right = getWidth() - getPaddingRight() - progressBarMargin;
                    left = right - shapeSize + progressBarMargin;
                    break;
                default:
                    left = drawableWidth / 2f - shapeSize / 2f + progressBarMargin + getPaddingLeft();
                    right = left + shapeSize - progressBarWidth;
            }
        }

        if (shapeSize < drawableHeight) {
            switch (verticalAlignment) {
                case TOP:
                    // do nothing
                    break;
                case BOTTOM:
                    bottom = getHeight() - getPaddingBottom() - progressBarMargin;
                    top = bottom - shapeSize + progressBarMargin;
                    break;
                default:
                    top = drawableHeight / 2f - shapeSize / 2f + progressBarMargin + getPaddingTop();
                    bottom = top + shapeSize - progressBarWidth;
            }
        }

        return new RectF(left, top, right, bottom);
    }

    private void drawStartPoint(Canvas canvas) {
        final Paint startPaint = new Paint();
        startPaint.setAntiAlias(true);
        startPaint.setStyle(Paint.Style.FILL);
        startPaint.setStrokeWidth(progressBarWidth);
        startPaint.setColor(Color.GREEN);

        PointF point = getPointForAngle(startAngle, shapeSize/2.0);

        float left = point.x;
        float top = point.y;
        float right = left + progressBarWidth;
        float bottom = top + progressBarWidth;

        float dotRadius =  progressBarWidth/2f * (1f - animationProgress);

        canvas.drawRoundRect(new RectF(left, top, right, bottom), dotRadius, dotRadius, startPaint);
    }

    @NonNull
    private Paint createStrokePaint() {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(strokeColor);
        paint.setStrokeCap(Paint.Cap.ROUND);
        return paint;
    }

    @NonNull
    private Paint createArcPaint() {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(progressBarWidth);
        paint.setColor(progressBarColor);
        paint.setStrokeCap(Paint.Cap.ROUND);
        return paint;
    }

    @NonNull
    private Paint createCirclePaint() {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(progressBarWidth);
        paint.setColor(shapeBackgroundColor);
        return paint;
    }

    private double calculateArcPerimeter(double angle, double radius) {
        return Math.toRadians(angle) * radius;
    }

    private double calculateArcAngle(double perimeter, double radius) {
        return Math.toDegrees(perimeter/radius);
    }

    private PointF getPointForAngle(double angle, double radius) {
        final float progressBarMargin = progressBarWidth/2f;
        double x = shapeSize/2.0 + left - progressBarWidth + (radius - progressBarMargin) * Math.cos(Math.toRadians(angle));
        double y = shapeSize/2.0 + top - progressBarWidth + (radius - progressBarMargin) * Math.sin(Math.toRadians(angle));

        return new PointF((float)x, (float)y);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO redraw only if configuration has changed
        final RectF arcRect = createRectF();

        final Paint circlePaint = createCirclePaint();
        canvas.drawArc(arcRect, 0, 360, false, circlePaint);

        final Paint strokePaint = createStrokePaint();
        canvas.drawArc(arcRect, 0, 360, false, strokePaint);

        final Paint arcPaint = createArcPaint();
        canvas.drawArc(arcRect, startAngle, endAngle, false, arcPaint);

        drawStartPoint(canvas);
    }
}