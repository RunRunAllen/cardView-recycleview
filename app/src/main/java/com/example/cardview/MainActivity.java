package com.example.cardview;

import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cardview.adapter.UniversalAdapter;
import com.example.cardview.adapter.ViewHolder;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<SwipeCardBean> mDatas;
    private UniversalAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rv = findViewById(R.id.rv);
        CardConfig.initConfig(this);
        rv.setLayoutManager(new SwipeCardLayoutManager());

        mDatas = SwipeCardBean.initDatas();
        adapter = new UniversalAdapter<SwipeCardBean>(MainActivity.this, mDatas, R.layout.item_swipe_card) {
            @Override
            public void convert(ViewHolder ViewHolder, SwipeCardBean swipeCardBean) {
                ViewHolder.setText(R.id.tvName, swipeCardBean.getName());
                ViewHolder.setText(R.id.tvPrecent, swipeCardBean.getPostition() + "/" + mDatas.size());
                Glide.with(MainActivity.this)
                        .load(swipeCardBean.getUrl())
                        .into((ImageView) ViewHolder.getView(R.id.iv));
            }
        };
        rv.setAdapter(adapter);
        ItemTouchHelper.Callback callback = new MyCallBack(rv, adapter, mDatas);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rv);


    }
}
