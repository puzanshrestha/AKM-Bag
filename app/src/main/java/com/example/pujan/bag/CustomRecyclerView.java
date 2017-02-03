package com.example.pujan.bag;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Pujan on 03-Feb-17.
 */
public class CustomRecyclerView extends RecyclerView {

    Context context;
    public CustomRecyclerView(Context context) {
        super(context);
        this.context = context;
    }

    public CustomRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public boolean fling(int velocityX, int velocityY) {
        velocityY*=1;
        return super.fling(velocityX, velocityY);

    }


}
