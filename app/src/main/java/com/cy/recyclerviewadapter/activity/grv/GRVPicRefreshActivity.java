package com.cy.recyclerviewadapter.activity.grv;

import android.app.Service;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.cy.androidview.ScreenUtils;
import com.cy.androidview.selectorview.ImageViewSelector;
import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.GlideUtils;
import com.cy.recyclerviewadapter.LogUtils;
import com.cy.recyclerviewadapter.R;
import com.cy.refreshlayoutniubility.IHeadView;
import com.cy.refreshlayoutniubility.OnSimpleRefreshListener;
import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.DragSelectorAdapter;
import com.cy.rvadapterniubility.adapter.MultiAdapter;
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
        list.add("https://img1.baidu.com/it/u=2603934083,3021636721&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500");
        list.add("https://img1.baidu.com/it/u=858214038,371193906&fm=253&fmt=auto&app=120&f=JPEG?w=1422&h=800");
        list.add("https://img2.baidu.com/it/u=2553853611,1255290940&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=500");
        list.add("https://img0.baidu.com/it/u=1100854359,3557979750&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800");
        list.add("https://img2.baidu.com/it/u=734998948,2733036542&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800");
        list.add("https://img1.baidu.com/it/u=2121836459,4185680900&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=500");
        list.add("https://img2.baidu.com/it/u=587955173,665005673&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800");
        list.add("https://img1.baidu.com/it/u=666159255,3156215465&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500");
        list.add("https://img0.baidu.com/it/u=1035818712,4106770352&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800");
        list.add("https://img0.baidu.com/it/u=2596934837,1532569852&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500");
        list.add("https://img2.baidu.com/it/u=3161270167,1354297607&fm=253&fmt=auto&app=138&f=JPEG?w=801&h=500");
        list.add("https://img1.baidu.com/it/u=2603934083,3021636721&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500");
        list.add("https://img1.baidu.com/it/u=858214038,371193906&fm=253&fmt=auto&app=120&f=JPEG?w=1422&h=800");
        list.add("https://img2.baidu.com/it/u=2553853611,1255290940&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=500");
        list.add("https://img0.baidu.com/it/u=1100854359,3557979750&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800");
        list.add("https://img2.baidu.com/it/u=734998948,2733036542&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800");
        list.add("https://img1.baidu.com/it/u=2121836459,4185680900&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=500");
        list.add("https://img2.baidu.com/it/u=587955173,665005673&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800");
        list.add("https://img1.baidu.com/it/u=666159255,3156215465&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500");
        list.add("https://img0.baidu.com/it/u=1035818712,4106770352&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800");
        list.add("https://img0.baidu.com/it/u=2596934837,1532569852&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500");
        list.add("https://img2.baidu.com/it/u=3161270167,1354297607&fm=253&fmt=auto&app=138&f=JPEG?w=801&h=500");
        list.add("https://img1.baidu.com/it/u=2603934083,3021636721&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500");
        list.add("https://img1.baidu.com/it/u=858214038,371193906&fm=253&fmt=auto&app=120&f=JPEG?w=1422&h=800");
        list.add("https://img2.baidu.com/it/u=2553853611,1255290940&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=500");
        list.add("https://img0.baidu.com/it/u=1100854359,3557979750&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800");
        list.add("https://img2.baidu.com/it/u=734998948,2733036542&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800");
        list.add("https://img1.baidu.com/it/u=2121836459,4185680900&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=500");
        list.add("https://img2.baidu.com/it/u=587955173,665005673&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800");
        list.add("https://img1.baidu.com/it/u=666159255,3156215465&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500");
        list.add("https://img0.baidu.com/it/u=1035818712,4106770352&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800");
        list.add("https://img0.baidu.com/it/u=2596934837,1532569852&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500");
        list.add("https://img2.baidu.com/it/u=3161270167,1354297607&fm=253&fmt=auto&app=138&f=JPEG?w=801&h=500");
        list.add("https://img1.baidu.com/it/u=2603934083,3021636721&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500");
        list.add("https://img1.baidu.com/it/u=858214038,371193906&fm=253&fmt=auto&app=120&f=JPEG?w=1422&h=800");
        list.add("https://img2.baidu.com/it/u=2553853611,1255290940&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=500");

        layout_menu = findViewById(R.id.layout_menu);
        VerticalGridRecyclerView verticalGridRecyclerView = findViewById(R.id.VerticalGridRecyclerView);
        ImageViewSelector imageViewSelector = findViewById(R.id.ivs);
        TextView tv_count = findViewById(R.id.tv_count);
        imageViewSelector.setOnCheckedChangeListener(new ImageViewSelector.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ImageViewSelector iv, boolean isChecked) {
                LogUtils.log("onCheckedChangedALL", isChecked);
                dragSelectorAdapter.selectAll(isChecked);
            }
        });

        dragSelectorAdapter = new DragSelectorAdapter<String>() {
            @Override
            public boolean areItemsTheSameToItemDecoration(String beanOld, String beanNew) {
                return beanOld.equals(beanNew);
            }

            @Override
            public boolean areItemsTheSame(String beanOld, String beanNew) {
                return super.areItemsTheSame(beanOld, beanNew);
            }

            @Override
            public boolean areContentsTheSame(String beanOld, String beanNew) {
                return super.areContentsTheSame(beanOld, beanNew);
            }
            //            @Override
//            public Object getChangePayload(int oldItemPosition, int newItemPosition, String beanOld, String beanNew) {
//                LogUtils.log("getChangePayload",oldItemPosition);
//                LogUtils.log("getChangePayload newItemPosition",newItemPosition);
//                return new String("数据变化"+oldItemPosition+":"+newItemPosition);
//            }

            @Override
            public void onSelectCountChanged(boolean isAllSelected, int count_selected) {
                imageViewSelector.setChecked(isAllSelected);
//                tv_count.setText("已选择"+getSelectedSize()+"项");
                //或者
                tv_count.setText("已选择" + count_selected + "项");

                // 降序遍历，如果要删除，从后往前删除
                for (int index : getSetSelector().getTreeSet().descendingSet()) {
                    LogUtils.log("onSelectCountChanged", index);
                }
            }
            @Override
            public void bindDataToView(BaseViewHolder holder, int position, String bean, boolean isSelected, @NonNull List<Object> payloads) {
                LogUtils.log("bindDataToView", position + ":" + isSelected + ":" + (!payloads.isEmpty() ? payloads.get(0) : ""));
                GlideUtils.load(GRVPicRefreshActivity.this, bean, R.drawable.default_pic, holder.getView(R.id.iv));
                holder.setVisibility(R.id.ivs, isUsingSelector() ? View.VISIBLE : View.GONE);
                ImageViewSelector imageViewSelector = holder.getView(R.id.ivs);
                imageViewSelector.setOnCheckedChangeListener(new ImageViewSelector.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(ImageViewSelector iv, boolean isChecked) {
                        LogUtils.log("onCheckedChanged", position);
                        select(position, isChecked);
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
            public void onItemLongClick__(BaseViewHolder holder, int position, String bean) {
                super.onItemLongClick(holder, position, bean);
                LogUtils.log("dispatchTouchEvent  onItemLongClick");
                Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
                if (vibrator != null) vibrator.vibrate(50);
                layout_menu.setVisibility(View.VISIBLE);
                startDragSelect(position);
            }
        };
        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_menu.setVisibility(View.GONE);
                dragSelectorAdapter.stopDragSelect();
            }
        });
        gridRefreshLayout.getRecyclerView().setSpanCount(3)
                .dragSelector(dragSelectorAdapter)
                .addItemDecoration(new GridItemDecoration(ScreenUtils.dpAdapt(this, 6)));
        multiAdapter = new MultiAdapter().addAdapter(dragSelectorAdapter);
        gridRefreshLayout.setAdapter(multiAdapter, new OnSimpleRefreshListener() {
            @Override
            public void onRefreshStart(IHeadView headView) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        list.set(3, "https://img2.baidu.com/it/u=2886261971,4176733325&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=667");
                        list.set(2, "https://img2.baidu.com/it/u=2017833233,3595322493&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=750");
                        if (list.size() == dragSelectorAdapter.getList_bean().size()) {
                            dragSelectorAdapter.dispatchUpdatesToItemDecoration(new ArrayList<>(list));
                            //ListAdapter好用但不如直接使用diffResult靠谱，ListAdapter下拉刷新后会导致列表顶上去
//                            dragSelectorAdapter.submitList(dragSelectorAdapter.getList_bean());
                        } else {
                            //如果数量变了，必须notifyDataSetChanged，否则错乱，尤其是间隔错乱
                            dragSelectorAdapter.clearAdd(list);
                        }
                        gridRefreshLayout.closeRefreshDelay("有8条更新", 2000);
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

                        dragSelectorAdapter.addNoNotify("https://img1.baidu.com/it/u=2683593527,2630972789&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=667");
                        dragSelectorAdapter.addNoNotify("https://img2.baidu.com/it/u=2236665998,2672651341&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500");
                        dragSelectorAdapter.addNoNotify("https://img1.baidu.com/it/u=3543009939,2144310597&fm=253&fmt=auto&app=138&f=JPEG?w=704&h=500");
                        dragSelectorAdapter.addNoNotify("https://img0.baidu.com/it/u=971759419,1542527601&fm=253&fmt=auto&app=120&f=JPEG?w=1422&h=800");
                        dragSelectorAdapter.addNoNotify("https://img0.baidu.com/it/u=1100854359,3557979750&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800");
                        dragSelectorAdapter.addNoNotify("https://img2.baidu.com/it/u=734998948,2733036542&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800");
                        dragSelectorAdapter.addNoNotify("https://img1.baidu.com/it/u=2121836459,4185680900&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=500");
                        dragSelectorAdapter.addNoNotify("https://img2.baidu.com/it/u=587955173,665005673&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800");

                        closeLoadMoreDelay("有8条更多", 1000, new Callback() {
                            @Override
                            public void onClosed() {
                                LogUtils.log("onClosed");
                                /**
                                 * 体现了MergeAdapter的强大所在，代码解耦合，position操作和单个Adapter一样，
                                 */
//                                dragSelectorAdapter.notifyItemRangeInserted(multiAdapter.getAdapter(0).getItemCount() - 8, 8);
                                dragSelectorAdapter.notifyBehindInserted(8);
                            }
                        });
                    }
                }, 2000);
            }
        });
        dragSelectorAdapter.add(list);
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
