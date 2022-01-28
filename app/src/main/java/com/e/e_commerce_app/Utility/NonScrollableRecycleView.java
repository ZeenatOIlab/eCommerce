package com.e.e_commerce_app.Utility;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by server3 on 9/8/2017.
 */

public class NonScrollableRecycleView extends RecyclerView {

    public NonScrollableRecycleView(Context context) {
        super(context);
    }
    public NonScrollableRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public NonScrollableRecycleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMeasureSpec_custom = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec_custom);
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();
    }
}
