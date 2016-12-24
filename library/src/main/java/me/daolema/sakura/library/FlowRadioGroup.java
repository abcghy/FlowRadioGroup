package me.daolema.sakura.library;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sakura on 2016/12/24.
 * A flow radio group, which will turn to second line when it hit the border.
 * Not like the original radio button, just hit the border. And you won't see it.
 */

public class FlowRadioGroup extends RadioGroup {

    public FlowRadioGroup(Context context) {
        this(context, null);
    }

    public FlowRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new RadioGroup.LayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        heightList.clear();
        childPosition.clear();

        int mWidth = 0;
        int mHeight = 0;

        int lineWidth = 0;
        int lineHeight = 0;

        int childCount = getChildCount();

        int currentLine = 1;

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            RadioGroup.LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            int childHeight = child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;

            if (lineWidth + childWidth > widthSize) {
                // 必须得换行了,将之前的lineHeight加上
                heightList.add(lineHeight);
                currentLine++;

                mHeight += lineHeight;
                lineHeight = childHeight;
                mWidth = Math.max(mWidth, lineWidth);
                lineWidth = childWidth;

                int top = childPosition.get(childPosition.size() - 1).top + heightList.get(currentLine - 2);
                childPosition.add(new Rect(0, top, childWidth, top + childHeight));
            } else {
                lineHeight = Math.max(childHeight, lineHeight);
                // 无需换行,该行宽度增加
                lineWidth += childWidth;
                if (i == 0) {
                    childPosition.add(new Rect(0, 0, childWidth, childHeight));
                } else {
                    int left = childPosition.get(childPosition.size() - 1).right;
                    int right = left + childWidth;
                    int top = childPosition.get(childPosition.size() - 1).top;
                    int bottom = top + childHeight;
                    childPosition.add(new Rect(left, top, right, bottom));
                }
            }

            if (i == childCount - 1) {
                heightList.add(lineHeight);
                mWidth = Math.max(lineWidth, mWidth);
                mHeight += lineHeight;
            }
        }

        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? widthSize : mWidth,
                (heightMode == MeasureSpec.EXACTLY) ? heightSize : mHeight);
    }

    private List<Integer> heightList = new ArrayList<>();
    private List<Rect> childPosition = new ArrayList<>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int visibleCount = childPosition.size();
        int childCount = getChildCount();
        for (int i = 0, j = 0; i < childCount; i++, j++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                j--;
                continue;
            }
            Rect rect = childPosition.get(j);
            RadioGroup.LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            child.layout(rect.left + layoutParams.leftMargin, rect.top + layoutParams.topMargin,
                    rect.right - layoutParams.rightMargin, rect.bottom - layoutParams.bottomMargin);
        }
    }

}
