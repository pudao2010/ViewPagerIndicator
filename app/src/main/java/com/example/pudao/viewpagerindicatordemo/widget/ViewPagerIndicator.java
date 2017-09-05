package com.example.pudao.viewpagerindicatordemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pudao.viewpagerindicatordemo.R;

import java.util.List;

/**
 * Created by pucheng on 2017/9/5.
 * ViewPager指示器
 */

public class ViewPagerIndicator extends LinearLayout implements ViewPager.OnPageChangeListener {

    private static final String TAG = "ViewPagerIndicator";

    // 三角形宽度与Tab宽度的比例, 适应各种屏幕
    private static final float RATIO = 1.0f / 6;
    // 制定默认的可见的tab数量
    private static final int DEFAULT_TAB_COUNT = 4;
    // 默认的颜色
    private static final int DEFAULT_COLOR = Color.BLACK;
    // 绘制指示器的画笔
    private Paint mPaint;
    // 用于绘制三角形的路径
    private Path mPath;
    // 三角形宽度
    private int mTriAngleWidth;
    // 三角形高度
    private int mTriAngleHeight;
    // 屏幕上可见的tab数量
    private int visibileTabCount;
    // 三角形的初始化偏移位置
    private int mInitTranslationX;
    // 随着ViewPager滑动时的偏移
    private int mTranslationX;
    // tab标题集合
    private List<String> mTitles;
    // 关联的ViewPager
    private ViewPager mViewPager;
    // 正常文本颜色
    private int normalColor;
    // 选中时的颜色
    private int selectedColor;

    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
    }

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // 获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
        visibileTabCount = typedArray.getInt(R.styleable.ViewPagerIndicator_visibleTabCount, DEFAULT_TAB_COUNT);
        selectedColor = typedArray.getColor(R.styleable.ViewPagerIndicator_selectedColor, 0xFF0000);
        normalColor = typedArray.getColor(R.styleable.ViewPagerIndicator_defaultColor, DEFAULT_COLOR);
        if (visibileTabCount < 0) {
            visibileTabCount = DEFAULT_TAB_COUNT;
        }
        typedArray.recycle();
        initPaint();
    }


    /**
     * Xml文件加载完毕的回调
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int count = getChildCount();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
                layoutParams.width = getScreenWidth() / visibileTabCount;
                child.setLayoutParams(layoutParams);
            }
        }
        setItemClickEvent();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaint = new Paint();
        // 设置抗锯齿
        mPaint.setAntiAlias(true);
        // 设置颜色
        mPaint.setColor(selectedColor);
        // 设置画笔样式
        mPaint.setStyle(Paint.Style.FILL);
        // 设置圆角连接效果
        mPaint.setPathEffect(new CornerPathEffect(4));
    }

    /**
     * 在此回调中设置三角形的大小
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 初始三角形的宽度
        mTriAngleWidth = (int) (w / visibileTabCount * RATIO);
        // 初始化三角形高度, 这里使用等边直角三角形
        mTriAngleHeight = mTriAngleWidth / 2;
        // 初始偏移量, tab的宽度减去三角形宽度的一半
        mInitTranslationX = w / visibileTabCount / 2 - mTriAngleWidth / 2;
        // 初始化三角形
        initTriAngle();
    }

    /**
     * 初始化三角形, 使用Path绘制
     */
    private void initTriAngle() {
        mPath = new Path();
        mPath.moveTo(0, 0);
        mPath.lineTo(mTriAngleWidth, 0);
        mPath.lineTo(mTriAngleWidth / 2, -mTriAngleHeight);
        mPath.close();
    }

    /**
     * 在此回调进行绘制
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        // 保存画板当前状态
        canvas.save();
        // 平移画板
        canvas.translate(mInitTranslationX + mTranslationX, getHeight() - 10);
        // 绘制三角形
        canvas.drawPath(mPath, mPaint);
        // 恢复画板状态
        canvas.restore();
        super.dispatchDraw(canvas);
    }

    /**
     * 指示器跟随ViewPager滚动
     *
     * @param position       ViewPager的当前position
     * @param positionOffset ViewPager的当前positionOffset (小于1的百分数)
     */
    public void scroll(int position, float positionOffset) {
        // tab宽度
        int tabWidth = getWidth() / visibileTabCount;
        // 重新赋值平移量
        mTranslationX = (int) (tabWidth * positionOffset + tabWidth * position);
        // 当tab总数大于visibleTabCount时容器滚动联动
        if (getChildCount() > visibileTabCount &&
                position >= (visibileTabCount - 2) && //当前屏幕倒数第二个tab才开始滚动容器
                position < (getChildCount() - 2) &&   //倒数第二个滚动时不再滚动容器
                positionOffset > 0) {
            // 滚动容器
            if (position != 1) {
                this.scrollTo(
                        (position - (visibileTabCount - 2)) * tabWidth + (int) (tabWidth * positionOffset),
                        0
                );
            } else {
                this.scrollTo(
                        position * tabWidth + (int) (tabWidth * positionOffset),
                        0
                );
            }

        }
        // 重新绘制
        invalidate();
    }

    /**
     * 获取屏幕宽度
     */
    public int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display defaultDisplay = windowManager.getDefaultDisplay();
        defaultDisplay.getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * 设置tab标题
     *
     * @param titles
     */
    public void setTabItemTitles(List<String> titles) {
        if (titles != null && !titles.isEmpty()) {
            // 移除当前所有子控件
            this.removeAllViews();
            mTitles = titles;
            for (String title : mTitles) {
                addView(generateTextView(title));
            }
        }
        setItemClickEvent();
    }

    /**
     * 设置tab的点击事件
     */
    private void setItemClickEvent() {
        for (int i = 0; i < getChildCount(); i++) {
            final int j = i;
            getChildAt(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }

    /**
     * 调用时机要先于 setTabItemTitles
     *
     * @param visibileTabCount
     */
    public void setVisibileTabCount(int visibileTabCount) {
        this.visibileTabCount = visibileTabCount;
    }

    /**
     * 根据title生成TextView
     *
     * @param title
     * @return
     */
    private View generateTextView(String title) {
        TextView textView = new TextView(getContext());
        // 宽高MATCH_PARENT
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -1);
        lp.width = getScreenWidth() / visibileTabCount;
        textView.setText(title);
        textView.setTextColor(DEFAULT_COLOR);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(lp);
        return textView;
    }

    /**
     * 关联ViewPager
     *
     * @param viewpager
     */
    public void setViewPager(ViewPager viewpager, int position) {
        this.mViewPager = viewpager;
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setCurrentItem(position);
        hightLightText(position);
    }

    /**
     * @param position             当前position
     * @param positionOffset       是一个百分数
     * @param positionOffsetPixels 滚动的像素值
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.e(TAG, "onPageScrolled: position=" + position + ", positionOffset=" + positionOffset + ", positionOffsetPixels" + positionOffsetPixels);
        scroll(position, positionOffset);
        if (listener != null) {
            listener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (listener != null) {
            listener.onPageSelected(position);
        }

        hightLightText(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (listener != null) {
            listener.onPageScrollStateChanged(state);
        }
    }

    /**
     * 重置文本颜色
     */
    private void resetTextColor() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof TextView) {
                ((TextView) childAt).setTextColor(DEFAULT_COLOR);
            }
        }
    }

    /**
     * 高亮选中position文本
     *
     * @param position
     */
    public void hightLightText(int position) {
        resetTextColor();
        View childAt = getChildAt(position);
        if (childAt instanceof TextView) {
            ((TextView) childAt).setTextColor(selectedColor);
        }
    }

    private OnPageChangeListener listener;

    public void addOnPageChangeListener(OnPageChangeListener listener) {
        this.listener = listener;
    }

    public interface OnPageChangeListener {

        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onPageSelected(int position);

        void onPageScrollStateChanged(int state);
    }

}
