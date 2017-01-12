package br.com.serasaexperian.android.widget.circularprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import br.com.serasaexperian.circularprogressbar.R;

/**
 * Created by lepkoski on 1/12/17.
 */

public class CircularProgressBar extends View {

    private int color;
    private HorizontalAlignment horizontalAlignment;
    private VerticalAlignment verticalAlignment;
    private float startAngle = 0;
    private float endAngle = 350;
    float strokeWidth = 20;

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
        } finally {
            a.recycle();
        }

        color = Color.RED;
    }

    private RectF createRectF() {
        final float drawableWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        final float drawableHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        final float shapeSize;
        if (drawableWidth < drawableHeight) {
            shapeSize = drawableWidth;
        } else {
            shapeSize = drawableHeight;
        }

        final float strokeMargin = strokeWidth/2f;

        float left = getPaddingLeft() + strokeMargin;
        float top = getPaddingTop() + strokeMargin;
        float right = left + shapeSize - strokeWidth;
        float bottom = top + shapeSize - strokeWidth;

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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        RectF arcRect = createRectF();

        final Paint arcPaint = new Paint();
        arcPaint.setAntiAlias(true);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(strokeWidth);
        arcPaint.setColor(color);

        canvas.drawArc(arcRect, startAngle, endAngle, false, arcPaint);
    }
}