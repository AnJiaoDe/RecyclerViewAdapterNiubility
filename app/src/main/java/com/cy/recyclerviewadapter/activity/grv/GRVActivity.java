package com.cy.recyclerviewadapter.activity.grv;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.cy.androidview.ScreenUtils;
import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.LogUtils;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.HRVBean;
import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.CallbackTag;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;
import com.cy.rvadapterniubility.recyclerview.GridItemDecoration;
import com.cy.rvadapterniubility.recyclerview.VerticalGridRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GRVActivity extends BaseActivity {

    private SimpleAdapter<HRVBean> rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grv);
        List<HRVBean> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            if (i % 5 == 0) {
                list.add(new HRVBean(R.drawable.pic3));
                continue;

            }
            list.add(new HRVBean(R.drawable.pic1));
        }
        rvAdapter = new SimpleAdapter<HRVBean>() {
            // 当然主动持有bitmap显然是不明智的，当view detachwindow之后，bitmap自然就没有可达对象引用它了，会自动被垃圾回收
            //            private Map<Object, Bitmap> mapBitmap = new HashMap<>();
//            @Override
//            public void recycleData(@Nullable Object tag) {
//                Bitmap bitmap = mapBitmap.get(tag);
//                if (bitmap != null && !bitmap.isRecycled()) bitmap.recycle();
//                bitmap = null;
//                mapBitmap.remove(tag);
//            }
//            private void putData(Object tag,@NonNull Bitmap bitmap) {
//                //必须先回收，防止很多异步加载bitmap，put了很多，却没有被回收，GG
//                recycleData(tag);
//                mapBitmap.put(tag,bitmap);
//            }
//            @Nullable
//            @Override
//            public Object setHolderTagPreBindData(BaseViewHolder holder, int position, HRVBean bean) {
//                return bean.getResID();
//            }

            @Override
            public void bindDataToView(BaseViewHolder holder, int position, HRVBean bean, @NonNull List<Object> payloads) {
//                final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), bean.getResID());
//                //假如这里加载bitmap是耗时操作，而且在回调中，就必须判断TAG，否则图片错乱
//                holder.isEqualsHolderTag(bean.getResID(), new CallbackTag() {
//                    @Override
//                    public void onTagEquls(Object tag) {
//                        holder.setImageBitmap(R.id.iv,bitmap);
//                        putData(tag,bitmap);
//                    }
//                });
                holder.setImageResource(R.id.iv, bean.getResID());
            }

            @Override
            public int getItemLayoutID(int position, HRVBean bean) {
                return R.layout.item_grv;
            }


            @Override
            public void onItemClick(BaseViewHolder holder, int position, HRVBean bean) {
                showToast("点击" + position);
            }
        };
        ((VerticalGridRecyclerView) findViewById(R.id.grv))
                .setSpanCount(4)
                .addItemDecoration(new GridItemDecoration(ScreenUtils.dpAdapt(this, 2)));
        ((VerticalGridRecyclerView) findViewById(R.id.grv)).setAdapter(rvAdapter, new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                if (i == 4) return 1;
                return 1;
            }
        });
        rvAdapter.add(list);
    }

    @Override
    public void onClick(View v) {
    }

}
