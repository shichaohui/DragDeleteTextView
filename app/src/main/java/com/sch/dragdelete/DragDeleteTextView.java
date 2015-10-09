package com.sch.dragdelete;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by shichaohui on 2015/10/9.
 * <br/>
 * 仿QQ消息数拖动删除.
 */
public class DragDeleteTextView extends TextView {

    private Context mContext;
    private ViewGroup decorView;
    private ViewParent parentView;
    private TextView counterfeitView;
    private CircleAndPathView circleView;
    private float pX = 0;
    private float pY = 0;
    private static int connectedColor = Color.RED;

    public DragDeleteTextView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public DragDeleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public DragDeleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DragDeleteTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        init();
    }

    private void init() {
        decorView = (ViewGroup) ((Activity) mContext).getWindow().getDecorView();
        decorView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    @Override
    public boolean onTouchEvent(@NonNull final MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (parentView == null) {
                    parentView = getScrollableParent();
                }
                if (parentView != null) {
                    parentView.requestDisallowInterceptTouchEvent(true);
                }
                pX = event.getX();
                pY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (getVisibility() == VISIBLE) {
                    setVisibility(INVISIBLE);
                    circleView = new CircleAndPathView(mContext);
                    circleView.setStartX(event.getRawX() - pX + getWidth() / 2);
                    circleView.setStartY(event.getRawY() - pY + getHeight() / 2);
                    decorView.addView(circleView);
                    decorView.addView(counterfeitView = cloneView(), getLayoutParams());
                }
                circleView.update(event.getRawX() - pX + getWidth() / 2, event.getRawY() - pY + getHeight() / 2);
                counterfeitView.setX(event.getRawX() - pX);
                counterfeitView.setY(event.getRawY() - pY);
                counterfeitView.invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                parentView.requestDisallowInterceptTouchEvent(false);
            case MotionEvent.ACTION_UP:
                if (circleView != null) {
                    if (circleView.getRadius() > 5) {
                        setVisibility(VISIBLE);
                        if (decorView != null) {
                            decorView.removeView(circleView);
                            if (counterfeitView != null) {
                                decorView.removeView(counterfeitView);
                            }
                        }
                    } else {
                        if (counterfeitView != null) {
                            decorView.removeView(counterfeitView);
                        }
                        final ImageView imageView = new ImageView(getContext());
                        imageView.setImageResource(R.drawable.clean_anim);
                        decorView.addView(imageView, new ViewGroup.LayoutParams(-2, -2));
                        imageView.post(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setX(event.getRawX() - pX + getWidth() / 2 - imageView.getWidth() / 2);
                                imageView.setY(event.getRawY() - pY + getHeight() / 2 - imageView.getHeight() / 2);
                            }
                        });
                        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
                        animationDrawable.start();
                    }
                }
                parentView.requestDisallowInterceptTouchEvent(false);
                break;
        }

        return true;
    }

    protected TextView cloneView() {
        TextView textView = new TextView(getContext());
        textView.setText(getText());
        textView.setTextColor(getTextColors());
        textView.setBackgroundDrawable(getBackground());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize());
        textView.setGravity(getGravity());
        textView.setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
        return textView;
    }

    private ViewGroup getScrollableParent() {
        View target = this;
        while (true) {
            View parent;
            try {
                parent = (View) target.getParent();
            } catch (Exception e) {
                return null;
            }
            if (parent == null)
                return null;
            if (parent instanceof ViewGroup) {
                return (ViewGroup) parent;
            }
            target = parent;
        }

    }

    /**
     * 设置拖动时与原位置连接的线条的颜色
     *
     * @param connectedColor 颜色
     */
    public void setConnectedColor(int connectedColor) {
        DragDeleteTextView.connectedColor = connectedColor;
    }

    static class CircleAndPathView extends View {
        // 默认定点圆半径
        public static final float DEFAULT_RADIUS = 14;
        private Paint paint;
        private float radius = DEFAULT_RADIUS;
        private float startX = 0;
        private float startY = 0;

        private Path path;

        public CircleAndPathView(Context context) {
            super(context);
            init();
        }

        private void init() {
            path = new Path();

            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setStrokeWidth(2);
            paint.setColor(connectedColor);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (radius > 5) {
                canvas.drawPath(path, paint);
                canvas.drawCircle(startX, startY, radius, paint);
            }
        }

        public void update(float x, float y) {
            float distance = (float) Math.sqrt(Math.pow(y - startY, 2) + Math.pow(x - startX, 2));
            radius = -distance / 10 + DEFAULT_RADIUS;

            // 根据角度算出四边形的四个点
            float offsetX = (float) (radius * Math.sin(Math.atan((y - startY) / (x - startX))));
            float offsetY = (float) (radius * Math.cos(Math.atan((y - startY) / (x - startX))));

            float x1 = startX + offsetX;
            float y1 = startY - offsetY;

            float x2 = x + offsetX;
            float y2 = y - offsetY;

            float x3 = x - offsetX;
            float y3 = y + offsetY;

            float x4 = startX - offsetX;
            float y4 = startY + offsetY;

            path.reset();
            path.moveTo(x1, y1);
            path.quadTo((startX + x) / 2, (startY + y) / 2, x2, y2);
            path.lineTo(x3, y3);
            path.quadTo((startX + x) / 2, (startY + y) / 2, x4, y4);
            path.lineTo(x1, y1);

            invalidate();
        }

        public void setStartX(float startX) {
            this.startX = startX;
        }

        public void setStartY(float startY) {
            this.startY = startY;
        }

        public float getRadius() {
            return radius;
        }
    }

}
