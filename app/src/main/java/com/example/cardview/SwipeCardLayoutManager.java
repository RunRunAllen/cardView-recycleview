package com.example.cardview;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

/**
 * creation date: 2020-03-12 20:58
 * description ：
 */
class SwipeCardLayoutManager extends RecyclerView.LayoutManager {


    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        // super.onLayoutChildren(recycler, state);
        //回收
        detachAndScrapAttachedViews(recycler);
        int itemCount = getItemCount();
        int bottomPosition;
        if (itemCount < CardConfig.MAX_SHOW_COUNT) {
            bottomPosition = 0;

        } else {
            bottomPosition = itemCount - CardConfig.MAX_SHOW_COUNT;
        }

        for (int i = bottomPosition; i < itemCount; i++) {
            View child = recycler.getViewForPosition(i);
            addView(child);
            Log.i("haha", "====child==" + child);
            measureChildWithMargins(child, 0, 0);
            int widSpace = getWidth() - getDecoratedMeasuredWidth(child);
            int heiSpace = getHeight() - getDecoratedMeasuredWidth(child);
            //
            layoutDecoratedWithMargins(child, widSpace / 2, heiSpace / 2, widSpace / 2 + getDecoratedMeasuredWidth(child)
                    , heiSpace / 2 + getDecoratedMeasuredHeight(child));


            int level = itemCount - i - 1;

            if (level > 0) {
                if (level < CardConfig.MAX_SHOW_COUNT - 1) {
                    child.setTranslationY(CardConfig.TRANS_Y_GAP * level);
                    child.setScaleX(1 - CardConfig.SCALE_GAP * level);
                    child.setScaleY(1 - CardConfig.SCALE_GAP * level);
                } else {
                    child.setTranslationY(CardConfig.TRANS_Y_GAP * (level - 1));
                    child.setScaleX(1 - CardConfig.SCALE_GAP * (level - 1));
                    child.setScaleY(1 - CardConfig.SCALE_GAP * (level - 1));
                }

            }

        }


    }

}
