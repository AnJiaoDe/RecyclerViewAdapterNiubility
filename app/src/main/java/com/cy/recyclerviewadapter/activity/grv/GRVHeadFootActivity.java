package com.cy.recyclerviewadapter.activity.grv;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import com.cy.androidview.BitmapUtils;
import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.R;
import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;
import com.cy.rvadapterniubility.recyclerview.GridItemDecoration;
import com.cy.rvadapterniubility.recyclerview.VerticalGridRecyclerView;

import java.util.List;

public class GRVHeadFootActivity extends BaseActivity {
    private SimpleAdapter<Boolean> simpleAdapter;

    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grvhead_foot);
        new Thread(new Runnable() {
            @Override
            public void run() {
                bitmap = getBlurBitmap(BitmapUtils.decodeResource(GRVHeadFootActivity.this, R.drawable.add, 100 * 100),
                        0, 0, Color.BLACK, 4, BlurMaskFilter.Blur.SOLID);
            }
        }).start();

        simpleAdapter = new SimpleAdapter<Boolean>() {
            @Override
            public void bindDataToView(@NonNull BaseViewHolder holder, int position, Boolean bean, @NonNull List<Object> payloads) {
                if (bean) {
                    holder.setText(R.id.tv, "indexFullSpan:" +position);
                    return;
                }
                holder.setImageResource(R.id.iv, R.drawable.pic3);
//                if (position % 2 == 0) {
//                    holder.setVisible(R.id.ivs);
//                    holder.setGone(R.id.ivs2);
//                } else {
//                    holder.setVisible(R.id.ivs2);
//                    holder.setGone(R.id.ivs);
//                }
//                holder.setImageBitmap(R.id.iv_shadow, bitmap);
            }

            @Override
            public int getItemLayoutID(int position, Boolean bean) {
                if (bean) return R.layout.item_head_00;
                return R.layout.item_grv;
            }

            @Override
            public void onItemClick(@NonNull BaseViewHolder holder, int position, Boolean bean) {
                showToast("点击" + position);
            }

            @Override
            public boolean isFullSpan(int itemLayoutID) {
                if (itemLayoutID == R.layout.item_head_00) return true;
                return super.isFullSpan(itemLayoutID);
            }

            @Override
            public void onViewAttachedToWindow(BaseViewHolder holder) {
                super.onViewAttachedToWindow(holder);
//                startDefaultAttachedAnim(holder);
            }
        };

        VerticalGridRecyclerView verticalGridRecyclerView = findViewById(R.id.grv);
        verticalGridRecyclerView.setSpanCount(4)
                .addItemDecoration(new GridItemDecoration(dpAdapt(10)))
                .setAdapter(simpleAdapter);

        for (int i = 0; i < 1000; i++) {
            simpleAdapter.addNoNotify(i % 15 == 0);
        }
        simpleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * --------------------------------------------------------------------------------
     */
    public int dpAdapt(float dp) {
        return dpAdapt(dp, 360);
    }

    @Nullable
    @WorkerThread
    public static Bitmap getBlurBitmap(Bitmap bitmap, int shadow_limit_x, int shadow_limit_y, int color_shadow, float radius, BlurMaskFilter.Blur style) {
        if (bitmap == null || bitmap.isRecycled() || bitmap.getWidth() <= 0 || bitmap.getHeight() <= 0)
            return null;
        Bitmap bitmapR = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Bitmap bitmapAlpha = bitmap.extractAlpha();
        if (bitmapAlpha == null) return bitmapR;

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Paint paintShadow = new Paint();
        paintShadow.setAntiAlias(true);
        paintShadow.setColor(color_shadow);
        paintShadow.setMaskFilter(new BlurMaskFilter(radius, style));

        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Canvas canvas = new Canvas(bitmapR);
        canvas.save();
        canvas.translate(shadow_limit_x, shadow_limit_y);
        canvas.drawBitmap(bitmapAlpha, null, rect, paintShadow);
        canvas.restore();
        canvas.drawBitmap(bitmap, null, rect, paint);

        return bitmapR;
    }

    public int dpAdapt(float dp, float widthDpBase) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int heightPixels = dm.heightPixels;//高的像素
        int widthPixels = dm.widthPixels;//宽的像素
        float density = dm.density;//density=dpi/160,密度比
        float heightDP = heightPixels / density;//高度的dp
        float widthDP = widthPixels / density;//宽度的dp
        float w = widthDP > heightDP ? heightDP : widthDP;
        return (int) (dp * w / widthDpBase * density + 0.5f);
    }
}
