package com.chensi.slidemenu.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.chensi.slidemenu.R;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by chensi on 2016/2/21.
 */
public class SlidingMenu extends HorizontalScrollView {

    private LinearLayout wrapper;
    private ViewGroup menu;
    private ViewGroup content;
    private int screenWidth;

    private int menuWidth;
    private int menuRightPadding = 50;
    private boolean once;

    public SlidingMenu(Context context) {
        super(context);
    }

    /**
     * 未使用自定义属性时，调用
     *
     * @param context
     * @param attrs
     */
    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 当使用了自定义属性时，会调用此构造方法
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 获取我们定义的属性
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SlidingMenu, defStyleAttr, 0);
        for (int i = 0; i < array.getIndexCount(); i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.SlidingMenu_rightPadding:
                    menuRightPadding = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics()));
            }
        }
        array.recycle();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
    }

    //设置子View的宽和高，自己的宽和高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!once) {
            wrapper = (LinearLayout) getChildAt(0);
            menu = (ViewGroup) wrapper.getChildAt(0);
            content = (ViewGroup) wrapper.getChildAt(1);
            menuWidth = menu.getLayoutParams().width = screenWidth - menuRightPadding;
            content.getLayoutParams().width = screenWidth;
            once = true;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //通过设置偏移量，将menu隐藏
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            this.scrollTo(menuWidth, 0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                if (scrollX >= (menuWidth / 2)) {
                    this.smoothScrollTo(menuWidth, 0);
                } else {
                    this.smoothScrollTo(0, 0);
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 如果不加此监听事件则整个为普通侧滑
     * @param l 此参数相当于izontalScrollView getScrollX
     * @param t
     * @param oldl
     * @param oldt
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        float scale = l * 1.0f / menuWidth; //1~0
        //抽屉式菜单一
//        ViewHelper.setTranslationX(menu, menuWidth*scale);

        //抽屉式菜单二(仿QQ)
        /**
         * 区别1：内容区域1.0~0.7 缩放的效果 scale : 1.0~0.0 0.7 + 0.3 * scale
         *
         * 区别2：菜单的偏移量需要修改
         *
         * 区别3：菜单的显示时有缩放以及透明度变化 缩放：0.7 ~1.0 1.0 - scale * 0.3 透明度 0.6 ~ 1.0
         * 0.6+ 0.4 * (1- scale) ;
         *
         */
//        float rightScale = 0.7f + 0.3f * scale;
//        float leftScale = 1.0f - scale * 0.3f;
//        float leftAlpha = 0.6f + 0.4f * (1 - scale);
//
//        // 调用属性动画，设置TranslationX
//        ViewHelper.setTranslationX(menu, menuWidth * scale * 0.8f);
//
//        ViewHelper.setScaleX(menu, leftScale);
//        ViewHelper.setScaleY(menu, leftScale);
//        ViewHelper.setAlpha(menu, leftAlpha);
//        // 设置content的缩放的中心点
//        ViewHelper.setPivotX(content, 0);
//        ViewHelper.setPivotY(content, content.getHeight() / 2);
//        ViewHelper.setScaleX(content, rightScale);
//        ViewHelper.setScaleY(content, rightScale);

        //抽屉式菜单三，仿QQ6.0
        ViewHelper.setTranslationX(menu, menuWidth*scale*0.8f);

    }
}
