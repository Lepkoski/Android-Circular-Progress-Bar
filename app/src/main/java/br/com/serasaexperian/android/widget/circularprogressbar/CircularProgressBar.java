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
    private float strokeWidth;
    private float left;
    private float top;
    private float right;
    private float bottom;
    private float animationProgress = 0.5f;
    private float drawableWidth;
    private float drawableHeight;
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
            strokeColor = a.getInt(R.styleable.CircularProgressBar_strokeColor, Color.BLACK);
        } finally {
            a.recycle();
        }
    }

    @NonNull
    private RectF createRectF() {
        drawableWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        drawableHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        if (drawableWidth < drawableHeight) {
            shapeSize = drawableWidth;
        } else {
            shapeSize = drawableHeight;
        }

        final float strokeMargin = strokeWidth/2f;

        left = getPaddingLeft() + strokeMargin;
        top = getPaddingTop() + strokeMargin;
        right = left + shapeSize - strokeWidth;
        bottom = top + shapeSize - strokeWidth;

        if (shapeSize < drawableWidth) {
            switch (horizontalAlignment) {
                case LEFT:
                    // do nothing
                    break;
                case RIGHT:
                    right = getWidth() - getPaddingRight() - strokeMargin;
                    left = right - shapeSize + strokeMargin;
                    break;
                default:
                    left = drawableWidth / 2f - shapeSize / 2f + strokeMargin + getPaddingLeft();
                    right = left + shapeSize - strokeWidth;
            }
        }

        if (shapeSize < drawableHeight) {
            switch (verticalAlignment) {
                case TOP:
                    // do nothing
                    break;
                case BOTTOM:
                    bottom = getHeight() - getPaddingBottom() - strokeMargin;
                    top = bottom - shapeSize + strokeMargin;
                    break;
                default:
                    top = drawableHeight / 2f - shapeSize / 2f + strokeMargin + getPaddingTop();
                    bottom = top + shapeSize - strokeWidth;
            }
        }

        return new RectF(left, top, right, bottom);
    }

    private void drawStartPoint(Canvas canvas) {
        final Paint startPaint = new Paint();
        startPaint.setAntiAlias(true);
        startPaint.setStyle(Paint.Style.FILL);
        startPaint.setStrokeWidth(strokeWidth);
        startPaint.setColor(Color.GREEN);

        PointF point = getPointForAngle(0, shapeSize/2.0);

        float left = point.x;
        float top = point.y;
        float right = left + strokeWidth;
        float bottom = top + strokeWidth;

        float dotRadius =  strokeWidth/2f * (1f - animationProgress);

        canvas.drawRoundRect(new RectF(left, top, right, bottom), dotRadius, dotRadius, startPaint);
    }

    @NonNull
    private Paint createPaint() {
        final Paint arcPaint = new Paint();
        arcPaint.setAntiAlias(true);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(strokeWidth);
        arcPaint.setColor(strokeColor);
        arcPaint.setStrokeCap(Paint.Cap.ROUND);
        return arcPaint;
    }

    private double calculateArcPerimeter(double angle, double radius) {
        return Math.toRadians(angle) * radius;
    }

    private double calculateArcAngle(double perimeter, double radius) {
        return Math.toDegrees(perimeter/radius);
    }

    private PointF getPointForAngle(double angle, double radius) {
        final float strokeMargin = strokeWidth/2f;
        double x = drawableWidth/2.0 + getPaddingLeft() - strokeMargin + (radius - strokeMargin) * Math.cos(Math.toRadians(angle));
        double y = drawableHeight/2.0 + getPaddingTop() - strokeMargin + (radius - strokeMargin) * Math.sin(Math.toRadians(angle));

        return new PointF((float)x, (float)y);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO redraw only if configuration has changed
        final RectF arcRect = createRectF();

        final Paint arcPaint = createPaint();

        canvas.drawArc(arcRect, startAngle, endAngle, false, arcPaint);

        drawStartPoint(canvas);
    }
}