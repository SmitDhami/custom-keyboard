package com.keyboardstyle.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MultiColorPicker extends View {
    private float[] adjacentHue = new float[5];
    private Path arrowPointerPath;
    private int arrowPointerSize;
    private float[] colorHSV = {0.0f, 0.0f, 1.0f};
    private RectF colorPointerCoords;
    private Paint colorPointerPaint;
    private Paint colorViewPaint;
    private Path colorViewPath;
    private Bitmap colorWheelBitmap;
    private Paint colorWheelPaint;
    private int colorWheelRadius;
    private Matrix gradientRotationMatrix;
    private int innerPadding;
    private int innerWheelRadius;
    private RectF innerWheelRect;
    private int outerPadding;
    private int outerWheelRadius;
    private RectF outerWheelRect;
    private final int paramArrowPointerSize = 4;
    private final int paramColorCount = 5;
    private final float paramHueSpreadAngle = 30.0f;
    private final int paramInnerPadding = 5;
    private final int paramOuterPadding = 2;
    private final int paramValueSliderWidth = 10;
    private Paint valuePointerArrowPaint;
    private Paint valuePointerPaint;
    private Paint valueSliderPaint;
    private Path valueSliderPath;
    private int valueSliderWidth;

    public MultiColorPicker(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public MultiColorPicker(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public MultiColorPicker(Context context) {
        super(context);
        init();
    }

    private void init() {
        this.colorPointerPaint = new Paint();
        this.colorPointerPaint.setStyle(Style.STROKE);
        this.colorPointerPaint.setStrokeWidth(2.0f);
        this.colorPointerPaint.setARGB(128, 0, 0, 0);
        this.valuePointerPaint = new Paint();
        this.valuePointerPaint.setStyle(Style.STROKE);
        this.valuePointerPaint.setStrokeWidth(2.0f);
        this.valuePointerArrowPaint = new Paint();
        this.colorWheelPaint = new Paint();
        this.colorWheelPaint.setAntiAlias(true);
        this.colorWheelPaint.setDither(true);
        this.valueSliderPaint = new Paint();
        this.valueSliderPaint.setAntiAlias(true);
        this.valueSliderPaint.setDither(true);
        this.colorViewPaint = new Paint();
        this.colorViewPaint.setAntiAlias(true);
        this.colorViewPath = new Path();
        this.valueSliderPath = new Path();
        this.arrowPointerPath = new Path();
        this.outerWheelRect = new RectF();
        this.innerWheelRect = new RectF();
        this.colorPointerCoords = new RectF();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int min = Math.min(MeasureSpec.getSize(i), MeasureSpec.getSize(i2));
        setMeasuredDimension(min, min);
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"DrawAllocation"})
    public void onDraw(Canvas canvas) {
        int width = getWidth() / 2;
        int height = getHeight() / 2;
        canvas.drawBitmap(this.colorWheelBitmap, (float) (width - this.colorWheelRadius), (float) (height - this.colorWheelRadius), null);
        int[] colors = getColors();
        for (int i = 0; i < 5; i++) {
            this.colorViewPath.reset();
            this.colorViewPath.arcTo(this.outerWheelRect, 270.0f - (((float) i) * 36.0f), -36.0f);
            this.colorViewPath.arcTo(this.innerWheelRect, (((float) ((5 - i) - 1)) * 36.0f) + 90.0f, 36.0f);
            this.colorViewPaint.setColor(colors[i]);
            canvas.drawPath(this.colorViewPath, this.colorViewPaint);
        }
        float f = (float) width;
        float f2 = (float) height;
        SweepGradient sweepGradient = new SweepGradient(f, f2, new int[]{-16777216, Color.HSVToColor(new float[]{this.colorHSV[0], this.colorHSV[1], 1.0f}), -1}, null);
        sweepGradient.setLocalMatrix(this.gradientRotationMatrix);
        this.valueSliderPaint.setShader(sweepGradient);
        canvas.drawPath(this.valueSliderPath, this.valueSliderPaint);
        for (int i2 = 0; i2 < 5; i2++) {
            drawColorWheelPointer(canvas, (float) Math.toRadians((double) this.adjacentHue[i2]));
        }
        this.valuePointerPaint.setColor(Color.HSVToColor(new float[]{0.0f, 0.0f, 1.0f - this.colorHSV[2]}));
        double d = ((double) (this.colorHSV[2] - 0.5f)) * 3.141592653589793d;
        float cos = (float) Math.cos(d);
        float sin = (float) Math.sin(d);
        canvas.drawLine((((float) this.innerWheelRadius) * cos) + f, (((float) this.innerWheelRadius) * sin) + f2, (cos * ((float) this.outerWheelRadius)) + f, (sin * ((float) this.outerWheelRadius)) + f2, this.valuePointerPaint);
        if (this.arrowPointerSize > 0) {
            drawPointerArrow(canvas);
        }
    }

    private void drawColorWheelPointer(Canvas canvas, float f) {
        double d = (double) f;
        int width = ((int) ((-Math.cos(d)) * ((double) this.colorHSV[1]) * ((double) this.colorWheelRadius))) + (getWidth() / 2);
        int height = ((int) ((-Math.sin(d)) * ((double) this.colorHSV[1]) * ((double) this.colorWheelRadius))) + (getHeight() / 2);
        float f2 = ((float) this.colorWheelRadius) * 0.075f;
        float f3 = f2 / 2.0f;
        float f4 = (float) ((int) (((float) width) - f3));
        float f5 = (float) ((int) (((float) height) - f3));
        this.colorPointerCoords.set(f4, f5, f4 + f2, f2 + f5);
        canvas.drawOval(this.colorPointerCoords, this.colorPointerPaint);
    }

    private void drawPointerArrow(Canvas canvas) {
        Canvas canvas2 = canvas;
        int width = getWidth() / 2;
        double d = ((double) (this.colorHSV[2] - 0.5f)) * 3.141592653589793d;
        double d2 = d + 0.032724923474893676d;
        double d3 = d - 0.032724923474893676d;
        double cos = Math.cos(d) * ((double) this.outerWheelRadius);
        double sin = Math.sin(d) * ((double) this.outerWheelRadius);
        int height = getHeight() / 2;
        double cos2 = Math.cos(d2) * ((double) (this.outerWheelRadius + this.arrowPointerSize));
        double sin2 = Math.sin(d2) * ((double) (this.outerWheelRadius + this.arrowPointerSize));
        double cos3 = Math.cos(d3) * ((double) (this.outerWheelRadius + this.arrowPointerSize));
        double sin3 = Math.sin(d3) * ((double) (this.outerWheelRadius + this.arrowPointerSize));
        this.arrowPointerPath.reset();
        float f = (float) width;
        float f2 = ((float) cos) + f;
        float f3 = (float) height;
        float f4 = ((float) sin) + f3;
        this.arrowPointerPath.moveTo(f2, f4);
        this.arrowPointerPath.lineTo(((float) cos2) + f, ((float) sin2) + f3);
        this.arrowPointerPath.lineTo(((float) cos3) + f, ((float) sin3) + f3);
        this.arrowPointerPath.lineTo(f2, f4);
        this.valuePointerArrowPaint.setColor(Color.HSVToColor(this.colorHSV));
        this.valuePointerArrowPaint.setStyle(Style.FILL);
        Canvas canvas3 = canvas;
        canvas3.drawPath(this.arrowPointerPath, this.valuePointerArrowPaint);
        this.valuePointerArrowPaint.setStyle(Style.STROKE);
        this.valuePointerArrowPaint.setStrokeJoin(Join.ROUND);
        this.valuePointerArrowPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas3.drawPath(this.arrowPointerPath, this.valuePointerArrowPaint);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        int i5 = i / 2;
        int i6 = i2 / 2;
        this.innerPadding = (i * 5) / 100;
        this.outerPadding = (i * 2) / 100;
        this.arrowPointerSize = (i * 4) / 100;
        this.valueSliderWidth = (i * 10) / 100;
        this.outerWheelRadius = (i5 - this.outerPadding) - this.arrowPointerSize;
        this.innerWheelRadius = this.outerWheelRadius - this.valueSliderWidth;
        this.colorWheelRadius = this.innerWheelRadius - this.innerPadding;
        this.outerWheelRect.set((float) (i5 - this.outerWheelRadius), (float) (i6 - this.outerWheelRadius), (float) (this.outerWheelRadius + i5), (float) (this.outerWheelRadius + i6));
        this.innerWheelRect.set((float) (i5 - this.innerWheelRadius), (float) (i6 - this.innerWheelRadius), (float) (this.innerWheelRadius + i5), (float) (this.innerWheelRadius + i6));
        this.colorWheelBitmap = createColorWheelBitmap(this.colorWheelRadius * 2, this.colorWheelRadius * 2);
        this.gradientRotationMatrix = new Matrix();
        this.gradientRotationMatrix.preRotate(270.0f, (float) i5, (float) i6);
        this.valueSliderPath.arcTo(this.outerWheelRect, 270.0f, 180.0f);
        this.valueSliderPath.arcTo(this.innerWheelRect, 90.0f, -180.0f);
    }

    private Bitmap createColorWheelBitmap(int i, int i2) {
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
        int[] iArr = new int[13];
        float[] fArr = {0.0f, 1.0f, 1.0f};
        for (int i3 = 0; i3 < iArr.length; i3++) {
            fArr[0] = (float) (((i3 * 30) + 180) % 360);
            iArr[i3] = Color.HSVToColor(fArr);
        }
        iArr[12] = iArr[0];
        float f = (float) (i / 2);
        float f2 = (float) (i2 / 2);
        SweepGradient sweepGradient = new SweepGradient(f, f2, iArr, null);
        RadialGradient radialGradient = new RadialGradient(f, f2, (float) this.colorWheelRadius, -1, ViewCompat.MEASURED_SIZE_MASK, TileMode.CLAMP);
        this.colorWheelPaint.setShader(new ComposeShader(sweepGradient, radialGradient, Mode.SRC_OVER));
        new Canvas(createBitmap).drawCircle(f, f2, (float) this.colorWheelRadius, this.colorWheelPaint);
        return createBitmap;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action != 0 && action != 2) {
            return super.onTouchEvent(motionEvent);
        }
        int x = (int) motionEvent.getX();
        int width = x - (getWidth() / 2);
        int y = ((int) motionEvent.getY()) - (getHeight() / 2);
        double sqrt = Math.sqrt((double) ((width * width) + (y * y)));
        if (sqrt <= ((double) this.colorWheelRadius)) {
            this.colorHSV[0] = (float) (Math.toDegrees(Math.atan2((double) y, (double) width)) + 180.0d);
            this.colorHSV[1] = Math.max(0.0f, Math.min(1.0f, (float) (sqrt / ((double) this.colorWheelRadius))));
            updateAdjacentHue();
            invalidate();
        } else if (x >= getWidth() / 2 && sqrt >= ((double) this.innerWheelRadius)) {
            this.colorHSV[2] = (float) Math.max(0.0d, Math.min(1.0d, (Math.atan2((double) y, (double) width) / 3.141592653589793d) + 0.5d));
            updateAdjacentHue();
            invalidate();
        }
        return true;
    }

    private void updateAdjacentHue() {
        int i = 0;
        while (i < 5) {
            this.adjacentHue[i] = (this.colorHSV[0] - (((float) (2 - i)) * 30.0f)) % 360.0f;
            this.adjacentHue[i] = this.adjacentHue[i] < 0.0f ? this.adjacentHue[i] + 360.0f : this.adjacentHue[i];
            i++;
        }
        this.adjacentHue[2] = this.colorHSV[0];
    }

    public void setColor(int i) {
        Color.colorToHSV(i, this.colorHSV);
        updateAdjacentHue();
    }

    public int getColor() {
        return Color.HSVToColor(this.colorHSV);
    }

    public int[] getColors() {
        int[] iArr = new int[5];
        float[] fArr = new float[3];
        for (int i = 0; i < 5; i++) {
            fArr[0] = this.adjacentHue[i];
            fArr[1] = this.colorHSV[1];
            fArr[2] = this.colorHSV[2];
            iArr[i] = Color.HSVToColor(fArr);
        }
        return iArr;
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putFloatArray("color", this.colorHSV);
        bundle.putParcelable("super", super.onSaveInstanceState());
        return bundle;
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            this.colorHSV = bundle.getFloatArray("color");
            updateAdjacentHue();
            super.onRestoreInstanceState(bundle.getParcelable("super"));
            return;
        }
        super.onRestoreInstanceState(parcelable);
    }
}
