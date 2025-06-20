package com.cy.recyclerviewadapter.activity.grv;

import static com.cy.recyclerviewadapter.ToastUtils.showToast;

import android.app.Service;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;


import com.cy.androidview.ScreenUtils;
import com.cy.androidview.selectorview.ImageViewSelector;
import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.GlideUtils;
import com.cy.recyclerviewadapter.LogUtils;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.HRVBean;
import com.cy.refreshlayoutniubility.IHeadView;
import com.cy.refreshlayoutniubility.OnSimpleRefreshListener;
import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.CallbackTag;
import com.cy.rvadapterniubility.adapter.DragSelectorAdapter;
import com.cy.rvadapterniubility.adapter.MultiAdapter;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;
import com.cy.rvadapterniubility.recyclerview.GridItemDecoration;
import com.cy.rvadapterniubility.recyclerview.OnGridLoadMoreListener;
import com.cy.rvadapterniubility.recyclerview.VerticalGridRecyclerView;
import com.cy.rvadapterniubility.refreshrv.GridRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class GRVPicRefreshActivity extends BaseActivity {
    private GridRefreshLayout gridRefreshLayout;
    private MultiAdapter multiAdapter;
    private DragSelectorAdapter<String> dragSelectorAdapter;
    private View layout_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grvload_pic);
        gridRefreshLayout = (GridRefreshLayout) findViewById(R.id.grl);
        List<String> list = new ArrayList<>();
        list.add("https://img0.baidu.com/it/u=1064515679,2989524044&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=725");
        list.add("https://img1.baidu.com/it/u=924730145,3669789419&fm=253&fmt=auto&app=120&f=JPEG?w=763&h=500");
        list.add("https://img2.baidu.com/it/u=2886261971,4176733325&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=667");
        list.add("https://img2.baidu.com/it/u=2017833233,3595322493&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=750");
        list.add("https://img1.baidu.com/it/u=2683593527,2630972789&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=667");
        list.add("https://img2.baidu.com/it/u=2236665998,2672651341&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500");
        list.add("https://img1.baidu.com/it/u=3543009939,2144310597&fm=253&fmt=auto&app=138&f=JPEG?w=704&h=500");
        list.add("https://img0.baidu.com/it/u=971759419,1542527601&fm=253&fmt=auto&app=120&f=JPEG?w=1422&h=800");
        list.add("https://img0.baidu.com/it/u=1100854359,3557979750&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800");
        list.add("https://img2.baidu.com/it/u=734998948,2733036542&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800");
        list.add("https://img1.baidu.com/it/u=2121836459,4185680900&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=500");
        list.add("https://img2.baidu.com/it/u=587955173,665005673&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800");
        list.add("https://img1.baidu.com/it/u=666159255,3156215465&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500");
        list.add("https://img0.baidu.com/it/u=1035818712,4106770352&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800");
        list.add("https://img0.baidu.com/it/u=2596934837,1532569852&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500");
        list.add("https://img2.baidu.com/it/u=3161270167,1354297607&fm=253&fmt=auto&app=138&f=JPEG?w=801&h=500");

        layout_menu = findViewById(R.id.layout_menu);
        VerticalGridRecyclerView verticalGridRecyclerView = (VerticalGridRecyclerView) findViewById(R.id.VerticalGridRecyclerView);
        ImageViewSelector imageViewSelector = (ImageViewSelector) findViewById(R.id.ivs);
        TextView tv_count = (TextView) findViewById(R.id.tv_count);
        imageViewSelector.setOnCheckedChangeListener(new ImageViewSelector.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ImageViewSelector iv, boolean isChecked) {
                LogUtils.log("onCheckedChangedALL", isChecked);
                dragSelectorAdapter.selectAll(isChecked);
            }
        });

        dragSelectorAdapter = new DragSelectorAdapter<String>() {
            @Override
            public void onAllSelectChanged(boolean selectedAll) {
                LogUtils.log("onCheckedChanged isAllSelected", selectedAll);
                imageViewSelector.setChecked(selectedAll);
            }

            @Override
            public void bindDataToView(BaseViewHolder holder, final int position, String bean, boolean isSelected) {
                LogUtils.log("bindDataToView", position + ":" + isSelected);
                holder.setImageViewTag(R.id.iv, R.drawable.default_pic, bean);
                holder.isEqualsViewTag(R.id.iv, bean, new CallbackTag() {
                    @Override
                    public void onTagEquls(Object tag) {
                        GlideUtils.load(GRVPicRefreshActivity.this, bean, R.drawable.default_pic, holder.getView(R.id.iv));
                    }
                });
                holder.setVisibility(R.id.ivs, isUsingSelector() ? View.VISIBLE : View.GONE);
//                holder.setImageResource(R.id.ivs, isSelected ? R.drawable.cb_selected_rect_blue : R.drawable.cb_unselected_rect_white);
                ImageViewSelector imageViewSelector = holder.getView(R.id.ivs);
//                LogUtils.log("getTag",position+":::::"+imageViewSelector.getTag());
//                imageViewSelector.setTag(position);
                imageViewSelector.setOnCheckedChangeListener(new ImageViewSelector.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(ImageViewSelector iv, boolean isChecked) {
                        LogUtils.log("onCheckedChanged", position);
                        select(position, isChecked);
                        tv_count.setText("已选择" + getSelectedSize() + "项");
                    }
                });
                //注意：setChecked必须在setOnCheckedChangeListener之后，否则VIEW复用导致position选择错乱
                imageViewSelector.setChecked(isSelected);
            }

            @Override
            public int getItemLayoutID(int position, String bean) {
                return R.layout.item_grv_drag_selector;
            }


            @Override
            public void onItemClick(BaseViewHolder holder, int position, String bean) {
                showToast("点击" + position);
            }

            @Override
            public void onItemLongClick(BaseViewHolder holder, int position, String bean) {
                super.onItemLongClick(holder, position, bean);
                LogUtils.log("dispatchTouchEvent  onItemLongClick");
                Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
                if (vibrator != null) vibrator.vibrate(50);
                layout_menu.setVisibility(View.VISIBLE);
                startDragSelect(position);
            }
        };
        gridRefreshLayout.getRecyclerView().setSpanCount(3)
                .addItemDecoration(new GridItemDecoration(ScreenUtils.dpAdapt(this, 6)));
        gridRefreshLayout.getRecyclerView().dragSelector(dragSelectorAdapter);
        dragSelectorAdapter.getAdapter().add(list);

        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_menu.setVisibility(View.GONE);
                dragSelectorAdapter.stopDragSelect();
            }
        });
        multiAdapter = new MultiAdapter().addAdapter(dragSelectorAdapter.getAdapter());
        gridRefreshLayout.getRecyclerView().addItemDecoration(new GridItemDecoration(dpAdapt(10)));
        gridRefreshLayout.setAdapter(multiAdapter, new OnSimpleRefreshListener() {
            @Override
            public void onRefreshStart(IHeadView headView) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dragSelectorAdapter.getAdapter().clearAdd(list);
                        gridRefreshLayout.finishRefresh();
//                        for (int i = 0; i < 8; i++) {
//                            rvAdapter.addToTopNoNotify(new HRVBean(R.drawable.pic3));
//                        }
//                        rvAdapter.notifyDataSetChanged();
//                        gridRefreshLayout.closeRefreshDelay("有8条更新",2000);
                    }
                }, 1000);
            }
        }, new OnGridLoadMoreListener(multiAdapter, 10) {
            @Override
            public void onLoadMoreStart() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /**
                         * 模拟没有更多的场景
                         */
                        if (multiAdapter.getMergeAdapter().getItemCount() > 120) {
                            closeLoadMoreDelay("没有更多了哦~", 1000, new Callback() {
                                @Override
                                public void onClosed() {

                                }
                            });
                            return;
                        }
//                        for (int i = 0; i < 8; i++) {
//                            rvAdapter.addNoNotify(new HRVBean(R.drawable.pic1));
//                        }
                        closeLoadMoreDelay("有8条更多", 1000, new Callback() {
                            @Override
                            public void onClosed() {
                                LogUtils.log("onClosed");
//                                rvAdapter.notifyDataSetChanged();
                                /**
                                 * 体现了MergeAdapter的强大所在，代码解耦合，position操作和单个Adapter一样，
                                 */
                                dragSelectorAdapter.getAdapter().notifyItemRangeInserted(multiAdapter.getAdapter(0).getItemCount() - 8, 8);
                            }
                        });
                    }
                }, 2000);
            }
        });
        dragSelectorAdapter.getAdapter().add(list);
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
