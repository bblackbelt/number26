package number26.de.bitcoins;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import number26.de.bitcoins.model.Point;

/**
 * Created by emanuele on 02.06.16.
 */
public class GraphView extends View {

    private static final String LOG_TAG = GraphView.class.getSimpleName();
    private final int mMinWidth;
    private final int mMinHeight;

    private final List<Point> mGraphPoints = new ArrayList<>();

    private final Paint mTextPaint;
    private final Paint mGraphPaint;
    private final Paint mAxisPaint;

    private float mMinX = Integer.MAX_VALUE;
    private float mMaxX = Integer.MIN_VALUE;

    private float mMinY = Integer.MAX_VALUE;
    private float mMaxY = Integer.MIN_VALUE;

    private final SimpleDateFormat mYearSimpleDateFormat = new SimpleDateFormat("MMM yy");
    private final SimpleDateFormat mDaysSimpleDateFormat = new SimpleDateFormat("d M");
    private final Calendar mCalendar = Calendar.getInstance();

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mMinWidth = (int) Utils.pxToDp(context, 400);
        mMinHeight = (int) Utils.pxToDp(context, 400);

        mGraphPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mGraphPaint.setStyle(Paint.Style.STROKE);
        mGraphPaint.setColor(Color.RED);
        mGraphPaint.setStrokeWidth(Utils.pxToDp(context, 2));

        mAxisPaint = new Paint(mGraphPaint);
        mAxisPaint.setStrokeWidth(Utils.pxToDp(context, 2));
        mAxisPaint.setColor(Color.BLACK);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.GREEN);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        final int width;
        final int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(mMinWidth, widthSize);
        } else {
            width = mMinWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(mMinHeight, heightSize);
        } else {
            height = mMinHeight;
        }
        setMeasuredDimension(width, height);
    }

    public void addPoint(final Point point) {
        synchronized (mGraphPoints) {
            mGraphPoints.add(point);
        }
        updateMinMaxX(point);
        updateMinMaxY(point);
    }

    public void addPoints(final List<? extends Point> points) {
        synchronized (mGraphPoints) {
            mGraphPoints.addAll(points);
        }
        Log.e(LOG_TAG, "size " + mGraphPoints.size());
        calculateMinAndMaxX();
        calculateMinAndMaxY();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int horizontalMargin = 0;
        int verticalMargin = 0;

        final int canvasWidth = getWidth() - getLeft() - getPaddingLeft() - getPaddingRight() - horizontalMargin - getLeft();
        final int canvasHeight = getHeight() - getPaddingTop() - getPaddingBottom() - verticalMargin - getTop();

        for (int i = 0; i < mGraphPoints.size() - 1; i++) {
            Point point = mGraphPoints.get(i);
            Point nextPoint = mGraphPoints.get(i + 1);
            final float startX = getLeft() + normalize(canvasWidth, mMinX, mMaxX, point.getX());
            final float startY = normalize(canvasHeight, mMinY, mMaxY, point.getY());

            final float nextX = getLeft() + normalize(canvasWidth, mMinX, mMaxX, nextPoint.getX());
            final float nextY = normalize(canvasHeight, mMinY, mMaxY, nextPoint.getY());

            canvas.drawLine(startX, canvasHeight
                    - startY, nextX, canvasHeight
                    - nextY, mGraphPaint);
        }

        final float axisX = normalize(canvasHeight, mMinX, mMaxX, mMinX);
        final float axisY = getLeft() + normalize(canvasWidth, mMinY, mMaxY, mMinY);
        canvas.drawLine(getLeft(), canvasHeight - axisX, getWidth(), canvasHeight - axisX, mAxisPaint);
        canvas.drawLine(axisY, 0, axisY, canvasHeight, mAxisPaint);
        writeYLabels(canvas);
    }

    private String getFormattedXValue(long milliSecs) {
        mCalendar.setTimeInMillis(milliSecs);
        if (mGraphPoints.size() < 366) {
            return mDaysSimpleDateFormat.format(mCalendar.getTime());
        }
        return mYearSimpleDateFormat.format(mCalendar.getTime());
    }

    private float normalize(float size, float min, float max, float toNormalize) {
        return (((toNormalize - min) / (max - min)) * size);
    }

    private void updateMinMaxY(Point point) {
        if (mMinY != Integer.MAX_VALUE && mMaxY != Integer.MIN_VALUE) {
            mMinY = Math.min(mMinY, point.getY());
            mMaxY = Math.max(mMaxY, point.getY());
            return;
        }
        calculateMinAndMaxY();
    }

    private void updateMinMaxX(Point point) {
        if (mMinX != Integer.MAX_VALUE && mMaxX != Integer.MIN_VALUE) {
            mMinX = Math.min(mMinX, point.getX());
            mMaxX = Math.max(mMaxX, point.getX());
            return;
        }
        calculateMinAndMaxX();
    }

    private void calculateMinAndMaxX() {
        synchronized (mGraphPoints) {
            for (Point point : mGraphPoints) {
                mMinX = Math.min(mMinX, point.getX());
                mMaxX = Math.max(mMaxX, point.getX());
            }
        }
        mMinX = (((int) mMinX / 10) - 1) * 10;
        mMaxX = (((int) mMaxX / 10) + 1) * 10;
    }


    private void calculateMinAndMaxY() {
        synchronized (mGraphPoints) {
            for (Point point : mGraphPoints) {
                mMinY = Math.min(mMinY, point.getY());
                mMaxY = Math.max(mMaxY, point.getY());
            }
        }
        mMinY = (((int) mMinY / 10) - 1) * 10;
        mMaxY = (((int) mMaxY / 10) + 1) * 10;
    }

    private void writeYLabels(Canvas canvas) {
        if (mGraphPoints.isEmpty()) {
            return;
        }

        int newMinY = ((((int) mMinY / 10) - 1) * 10);
        int newMaxY = ((((int) mMaxY / 10) + 1) * 10);
        Log.e(LOG_TAG, "newMinY " + newMinY + " newMaxY " + newMaxY);
        float step = ((mMaxY - mMinY) / 8);

        int horizontalMargin = 0;
        int verticalMargin = 0;

        final int canvasWidth = getWidth() - getLeft() - getPaddingLeft() - getPaddingRight() - horizontalMargin - getLeft();
        final int canvasHeight = getHeight() - getPaddingTop() - getPaddingBottom() - verticalMargin - getTop();
        mTextPaint.setTextSize(Utils.spToDp(getContext(), 15f));
        canvas.drawText(String.valueOf((int) (mMinY)), 0, (canvasHeight
                - normalize(canvasHeight, mMinY, mMaxY, mMinY)), mTextPaint);
        for (int i = 1; i < 9; i++) {
            float yCoord = canvasHeight
                    - normalize(canvasHeight, mMinY, mMaxY, mMinY + (i * step));
            canvas.drawLine(getLeft(), yCoord, getWidth(), yCoord, mTextPaint);
            canvas.drawText(String.valueOf((int) (mMinY + (i * step))), 0, yCoord, mTextPaint);
        }

        List<String> xLables = getXLabels();
        step = (mMaxX - mMinX) / xLables.size();
        mTextPaint.setTextSize(Utils.spToDp(getContext(), 12f));
        float yCoord = canvasHeight
                - normalize(canvasHeight, mMinY, mMaxY, mMinY);
        for (int i = 1; i < xLables.size() - 1; i++) {
            float xCoord = getLeft() + normalize(canvasWidth, mMinX, mMaxX, mMinX + (i * step));
            canvas.drawLine(xCoord, 0, xCoord, canvasHeight, mTextPaint);
            if (i != 0 && i % 2 == 0) {
                canvas.drawText(xLables.get(i - 1), xCoord, yCoord, mTextPaint);
            }
        }
    }

    private List<String> getXLabels() {
        List<String> list = new ArrayList<>();
        String currentDate = null;
        for (Point point : mGraphPoints) {
            String date = getFormattedXValue((long) (point.getX() * 1000L));
            if (!date.equals(currentDate)) {
                currentDate = date;
                list.add(currentDate);
            }
        }
        return list;
    }

    public void clear() {
        synchronized (mGraphPoints) {
            mGraphPoints.clear();
        }
        mMaxX = mMaxY = Integer.MIN_VALUE;
        mMinX = mMinY = Integer.MAX_VALUE;
        invalidate();
    }
}

