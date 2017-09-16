package freestar.rvtest;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.orhanobut.logger.Logger;

/**
 * 描述：
 * 作者：一颗浪星
 * 日期：2017/9/13
 * github：
 */

public class TopLayoutManager extends LinearLayoutManager {

    public int getOffset() {
        return mOffset;
    }

    private int mOffset;

    public void setOffset(int offset) {
        this.mOffset = offset;
    }

    public TopLayoutManager(Context context) {
        super(context);
    }

    public TopLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public TopLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

//    @Override
//    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
//        super.smoothScrollToPosition(recyclerView, state, position);
//    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        TopSmoothScroller smoothScroller = new TopSmoothScroller(recyclerView.getContext());
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }

    private class TopSmoothScroller extends LinearSmoothScroller {

        public TopSmoothScroller(Context context) {
            super(context);
        }

        // -1   boxStart - viewStart  1  boxEnd - viewEnd   0
        @Override
        public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
            Logger.e(snapPreference + "-mOffset:" + (boxStart - viewStart) + "-boxStart-" + boxStart + "-viewStart-" + viewStart + "-viewEnd-" + viewEnd + "-boxEnd-" + boxEnd + "-mOffset-" + mOffset);
//            return boxStart - viewStart;// 这里是关键，得到的就是置顶的偏移量
            return super.calculateDtToFit(viewStart, viewEnd, boxStart, boxEnd - mOffset, 1);
        }
    }
}
