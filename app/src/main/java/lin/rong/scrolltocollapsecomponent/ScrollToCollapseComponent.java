package lin.rong.scrolltocollapsecomponent;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

/**
 * Created by dell on 2017/1/3.
 */
public class ScrollToCollapseComponent extends FrameLayout {

    private RelativeLayout top_bar;
    private RelativeLayout top_content;
    private RelativeLayout bottom_box;
    private ScrollView bottom_content;

    private int topBarHeight;
    private int topContentHeight;
    private int scrollOffset;
    private int totalHeight;

    private float lastY;

    private ScrollToCollapseComponentViews scrollToCollapseComponentViews;

    public ScrollToCollapseComponent(Context context) {
        super(context);

        init();
    }

    public void setScrollToCollapseComponentViews(ScrollToCollapseComponentViews views) {
        if (views == null) {
            throw new IllegalArgumentException("must implement interface ScrollToCollapseComponentViews");
        }
        this.top_bar.addView(views.getTopBarView());
        this.top_content.addView(views.getTopContentView());
        this.bottom_content.addView(views.getBottomContentView());
    }

    public ScrollToCollapseComponent(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public ScrollToCollapseComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View rootView = layoutInflater.inflate(R.layout.scroll_to_collapse_component, null);
        top_bar = (RelativeLayout) rootView.findViewById(R.id.top_bar);
        top_content = (RelativeLayout) rootView.findViewById(R.id.top_content);
        bottom_box = (RelativeLayout) rootView.findViewById(R.id.bottom_box);
        bottom_content = (ScrollView) rootView.findViewById(R.id.bottom_content);
        addView(rootView);

        int topBarBg = getResources().getColor(R.color.colorPrimary);
        topBarBg = topBarBg & 0x00ffffff;
        top_bar.setBackgroundColor(topBarBg);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        topBarHeight = top_bar.getMeasuredHeight();
        topContentHeight = top_content.getMeasuredHeight();
        scrollOffset = topContentHeight - topBarHeight;
        totalHeight = getMeasuredHeight();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = ev.getY();
                if (moveY - lastY < 0 && bottom_box.getTop() > topBarHeight) {// 向上滑动
                    bottom_box.layout(bottom_box.getLeft(), (int) (bottom_box.getTop() + (moveY - lastY)), bottom_box.getRight(), totalHeight);
                    bottom_content.layout(bottom_content.getLeft(), bottom_content.getTop(), bottom_content.getRight(), totalHeight);
                    bottom_box.postInvalidate();
                } else if (moveY - lastY > 0 && bottom_box.getTop() < topContentHeight && bottom_content.getScrollY() == 0) {// 向下滑动
                    bottom_box.layout(bottom_box.getLeft(), (int) (bottom_box.getTop() + (moveY - lastY)), bottom_box.getRight(), totalHeight);
                    bottom_content.layout(bottom_content.getLeft(), bottom_content.getTop(), bottom_content.getRight(), totalHeight);
                    bottom_box.postInvalidate();
                }

                if (bottom_box.getTop() < topBarHeight) {// 溢出处理（纠正）
                    bottom_box.layout(bottom_box.getLeft(), topBarHeight, bottom_box.getRight(), totalHeight);
                    bottom_content.layout(bottom_content.getLeft(), bottom_content.getTop(), bottom_content.getRight(), totalHeight);
                    bottom_box.postInvalidate();
                }
                if (bottom_box.getTop() > topContentHeight) {// 溢出处理（纠正）
                    bottom_box.layout(bottom_box.getLeft(), topContentHeight, bottom_box.getRight(), totalHeight);
                    bottom_content.layout(bottom_content.getLeft(), bottom_content.getTop(), bottom_content.getRight(), totalHeight);
                    bottom_box.postInvalidate();
                }

                changeTopBarAlpha();

                lastY = moveY;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void changeTopBarAlpha() {
        int offset = bottom_box.getTop() - topBarHeight;
        int alpha = (int) (255 - offset * 1.0f / scrollOffset * 255f);
        alpha = alpha << 24;
        int topBarBg = getResources().getColor(R.color.colorPrimary);
        topBarBg = topBarBg & 0x00ffffff;
        top_bar.setBackgroundColor(topBarBg | alpha);
    }

    public interface ScrollToCollapseComponentViews {

        public View getTopBarView();

        public View getTopContentView();

        public View getBottomContentView();

    }

}
