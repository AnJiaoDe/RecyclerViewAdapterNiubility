package com.cy.recyclerviewadapter.activity.tabrv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.cy.cyrvadapter.adapter.BaseViewHolder;
import com.cy.cyrvadapter.adapter.LinearItemDecoration;
import com.cy.cyrvadapter.adapter.SimpleAdapter;
import com.cy.cyrvadapter.refreshlayout.LogUtils;
import com.cy.cyrvadapter.tablayout.TabLayoutSimple;
import com.cy.recyclerviewadapter.R;

import static androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_DRAGGING;
import static androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_IDLE;
import static androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_SETTLING;

public class TabLayoutRVActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private TabLayoutSimple cyTabLayout;
    private FragmentPageAdapter fragmentPageAdapter;
    private SimpleAdapter<String> simpleAdapter_tab;

    private int position_scroll_last = 0;
    private int diff = 0;
    private int diff_click = 0;
    private int toScroll = 0;

    private int offsetX_last = 0;
    private int offsetX_last_click = 0;
    private int offsetX_touch = 0;
    private boolean rvScrolledByVp = false;
    private boolean rvScrolledByTouch = false;
    private boolean scrolledByClick = false;

    private int position_selected_last = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout_r_v);
        viewPager2 = findViewById(R.id.view_pager);
        cyTabLayout = findViewById(R.id.tablayout);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                LogUtils.log("onPageSelected", position);
                simpleAdapter_tab.setPositionSelected(viewPager2.getCurrentItem());
            }

            /**
             * @param position
             * @param positionOffset
             * @param positionOffsetPixels
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                LogUtils.log("onPageScrolled", position);
                LogUtils.log("_positionOffset", positionOffset);
                //注意：滑动很快的时候，即使到了另外的page,positionOffsetPixels不一定会出现0
                LogUtils.log("positionOffsetPixels", positionOffsetPixels);
                LogUtils.log("getOffsetX", cyTabLayout.getHorizontalRecyclerView().getOffsetX());

                if (cyTabLayout.getHorizontalRecyclerView().getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {

                    if (rvScrolledByTouch && offsetX_touch != 0) {
                        LogUtils.log("offsetX_touch00000", offsetX_touch);
                        cyTabLayout.getHorizontalRecyclerView().scrollBy(offsetX_touch, 0);
                        RecyclerView.ViewHolder viewHolder = cyTabLayout.getHorizontalRecyclerView().findViewHolderForAdapterPosition(viewPager2.getCurrentItem());
                        if (viewHolder != null) {
                            LogUtils.log("onScrolledviewHolder != null");
                            cyTabLayout.setWidth_indicator(cyTabLayout.getWidth_indicator_selected())
                                    .setProgress((int) (viewHolder.itemView.getLeft()
                                            + viewHolder.itemView.getWidth() * 1f / 2
                                            - cyTabLayout.getWidth_indicator() / 2));
                        }else {
                            cyTabLayout.setWidth_indicator(0).invalidate();
                        }
                        rvScrolledByTouch = false;
                        offsetX_touch = 0;
                    } else

                        //onPageSelected先回调，然后还会继续回调onPageScrolled，直到onPageScrolled=position_selected，从page index 滑动到 page index+1，

                        //说明是点击item进行滑动
                        if (scrolledByClick && viewPager2.getScrollState() == SCROLL_STATE_SETTLING) {
                            LogUtils.log("scrolledByClick");
                            if (position == viewPager2.getCurrentItem() - 1 || position == viewPager2.getCurrentItem()) {
                                RecyclerView.ViewHolder viewHolder = cyTabLayout.getHorizontalRecyclerView().findViewHolderForAdapterPosition(viewPager2.getCurrentItem());
                                if (viewHolder != null) {
                                    if (diff_click == 0)
                                        diff_click = (int) (viewHolder.itemView.getLeft() + viewHolder.itemView.getWidth() * 1f / 2 - cyTabLayout.getWidth() / 2);
                                    if (offsetX_last_click == 0)
                                        offsetX_last_click = cyTabLayout.getHorizontalRecyclerView().getOffsetX();
                                    LogUtils.log("diff_click", diff_click);
                                    LogUtils.log("diff_clickoffsetX_touch", offsetX_touch);

                                    int width = viewHolder.itemView.getWidth();
                                    if (positionOffset != 0) {
                                        LogUtils.log("AAAscrollTo", cyTabLayout.getHorizontalRecyclerView().getOffsetX());
                                        //scrollBy调用一次，onScrolled回调一次
                                        rvScrolledByVp = true;

                                        if (position_selected_last < viewPager2.getCurrentItem()) {
                                            cyTabLayout.getHorizontalRecyclerView().scrollTo((int) (offsetX_last_click - (diff_click * positionOffset)), 0);
                                        } else {
                                            cyTabLayout.getHorizontalRecyclerView().scrollTo((int) (offsetX_last_click - (diff_click * (1 - positionOffset))), 0);
                                        }

                                        rvScrolledByVp = false;
                                        LogUtils.log("AAAoffsetX_restore_before");
                                        LogUtils.log("AAAoffsetX_restore_after");
                                    }
                                    cyTabLayout.setWidth_indicator(Math.max(cyTabLayout.getWidth_indicator_selected(), (int) (cyTabLayout.getWidth_indicator_selected() +
                                            (positionOffset == 0 ? 0 : cyTabLayout.getWidth_indicator_max() * (0.5 - Math.abs(0.5 - positionOffset))))))
                                            .setProgress((int) (viewHolder.itemView.getLeft()
                                                    + width * 1f / 2
                                                    - cyTabLayout.getWidth_indicator() / 2));
                                }else{
                                    cyTabLayout.setWidth_indicator(0).invalidate();
                                }
                            }
                        } else {
                            if (position == 0) {
                                diff = 0;
                                offsetX_last = 0;
                                RecyclerView.ViewHolder viewHolder_behind = cyTabLayout.getHorizontalRecyclerView().findViewHolderForAdapterPosition(position + 1);
                                if (viewHolder_behind != null) {
                                    RecyclerView.ViewHolder viewHolder = cyTabLayout.getHorizontalRecyclerView().findViewHolderForAdapterPosition(position);
                                    if (viewHolder != null)
                                        toScroll = (int) (viewHolder.itemView.getWidth() * 1f / 2
                                                + cyTabLayout.getHorizontalRecyclerView().getItemDecoration().getSpace_horizontal()
                                                + viewHolder_behind.itemView.getWidth() * 1f / 2);
                                    LogUtils.log("toScroll", toScroll);
                                }
                            } else if (position_scroll_last < position) {
                                RecyclerView.ViewHolder viewHolder_behind = cyTabLayout.getHorizontalRecyclerView().findViewHolderForAdapterPosition(position + 1);
                                if (viewHolder_behind != null) {
                                    diff = (int) (viewHolder_behind.itemView.getLeft() + viewHolder_behind.itemView.getWidth() * 1f / 2 - cyTabLayout.getWidth() / 2);
                                    if (diff < 0) diff = 0;
                                    offsetX_last = cyTabLayout.getHorizontalRecyclerView().getOffsetX();
                                    LogUtils.log("diff<", diff);

                                    RecyclerView.ViewHolder viewHolder = cyTabLayout.getHorizontalRecyclerView().findViewHolderForAdapterPosition(position);
                                    if (viewHolder != null)
                                        toScroll = (int) (viewHolder.itemView.getWidth() * 1f / 2
                                                + cyTabLayout.getHorizontalRecyclerView().getItemDecoration().getSpace_horizontal()
                                                + viewHolder_behind.itemView.getWidth() * 1f / 2);
                                    LogUtils.log("toScroll", toScroll);
                                }

                                //避免出现负数导致recyclerView抖动
//                    if (d < 0) d = 0;
                                //说明从page index 滑动到 page index-1,即往回滑动
                            } else if (position_scroll_last > position) {

                                //避免出现正数导致recyclerView抖动
                                RecyclerView.ViewHolder viewHolder = cyTabLayout.getHorizontalRecyclerView().findViewHolderForAdapterPosition(position);
                                if (viewHolder != null) {
                                    diff = (int) (viewHolder.itemView.getLeft() + viewHolder.itemView.getWidth() * 1f / 2 - cyTabLayout.getWidth() / 2);
                                    if (diff > 0) diff = 0;

                                    offsetX_last = cyTabLayout.getHorizontalRecyclerView().getOffsetX();
                                    LogUtils.log("diff>", diff);

                                    RecyclerView.ViewHolder viewHolder_behind = cyTabLayout.getHorizontalRecyclerView().findViewHolderForAdapterPosition(position + 1);
                                    if (viewHolder_behind != null)
                                        toScroll = (int) (viewHolder.itemView.getWidth() * 1f / 2
                                                + cyTabLayout.getHorizontalRecyclerView().getItemDecoration().getSpace_horizontal()
                                                + viewHolder_behind.itemView.getWidth() * 1f / 2);

                                    LogUtils.log("toScroll", toScroll);

                                }
                            }

                            LogUtils.log("0000000000000000000000diff", diff);
                            LogUtils.log("0000000000000000000000position_scroll_last", position_scroll_last);
                            LogUtils.log("0000000000000000000000position", position);
                            RecyclerView.ViewHolder viewHolder = cyTabLayout.getHorizontalRecyclerView().findViewHolderForAdapterPosition(position);
                            if (viewHolder != null) {
                                int width = viewHolder.itemView.getWidth();
                                if (diff != 0 && positionOffset != 0 && position_scroll_last == position) {
                                    LogUtils.log("AAAscrollTo00000000", cyTabLayout.getHorizontalRecyclerView().getOffsetX());
                                    LogUtils.log("AAAscrollTo00000000offsetX_last", offsetX_last);
                                    LogUtils.log("AAAscrollTo00000000diff", diff);
                                    //scrollBy调用一次，onScrolled回调一次
                                    rvScrolledByVp = true;

                                    if (diff > 0) {
                                        cyTabLayout.getHorizontalRecyclerView().scrollTo((int) (offsetX_last - (diff * positionOffset)), 0);
                                    } else {
                                        cyTabLayout.getHorizontalRecyclerView().scrollTo((int) (offsetX_last - (diff * (1 - positionOffset))), 0);
                                    }

                                    rvScrolledByVp = false;
                                    LogUtils.log("AAAoffsetX_restore_before");
                                    LogUtils.log("AAAoffsetX_restore_after");
                                }
                                cyTabLayout.setWidth_indicator(Math.max(cyTabLayout.getWidth_indicator_selected(), (int) (cyTabLayout.getWidth_indicator_selected() +
                                        (positionOffset == 0 ? 0 : cyTabLayout.getWidth_indicator_max() * (0.5 - Math.abs(0.5 - positionOffset))))))
                                        .setProgress((int) (viewHolder.itemView.getLeft()
                                                + width * 1f / 2
                                                - cyTabLayout.getWidth_indicator() / 2
                                                + (toScroll * positionOffset)));
                            }else {
                                cyTabLayout.setWidth_indicator(0).invalidate();
                            }
                        }
                }
                position_scroll_last = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                switch (state) {
                    case SCROLL_STATE_DRAGGING:
                        LogUtils.log("onPageScrollStateChangedSCROLL_STATE_DRAGGING");
                        break;
                    case SCROLL_STATE_SETTLING:

                        LogUtils.log("onPageScrollStateChangedSCROLL_STATE_SETTLING");
                        break;
                    case SCROLL_STATE_IDLE:
                        LogUtils.log("onPageScrollStateChangedSCROLL_STATE_IDLE");

//                        if (scrolledByClick) {
//                            RecyclerView.ViewHolder viewHolder_behind = cyTabLayout.getHorizontalRecyclerView().findViewHolderForAdapterPosition(viewPager2.getCurrentItem() + 1);
//                            if (viewHolder_behind != null) {
//                                diff = (int) (viewHolder_behind.itemView.getLeft() + viewHolder_behind.itemView.getWidth() * 1f / 2 - cyTabLayout.getWidth() / 2);
//                                offsetX_last = cyTabLayout.getHorizontalRecyclerView().getOffsetX();
//                            }
//                        }

                        scrolledByClick = false;
//                        offsetX_touch = 0;
                        diff_click = 0;
                        offsetX_last_click = 0;
                        break;
                }
            }
        });

        fragmentPageAdapter = new FragmentPageAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return FragmentTab2.newInstance(FragmentTab2.TAB_NAME2, getList_bean().get(position));
            }
        };
        simpleAdapter_tab = new SimpleAdapter<String>() {
            @Override
            public void bindDataToView(BaseViewHolder holder, int position, String bean, boolean isSelected) {
                TextView textView = holder.getView(R.id.tv);
                if (isSelected) {
                    textView.setTextColor(0xffe45540);
                    textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                } else {
                    textView.setTextColor(0xff444444);
                    textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                }
                textView.setText(bean);
            }

            @Override
            public int getItemLayoutID(int position, String bean) {
                return R.layout.item_tab;
            }

            /**
             * @param holder
             * @param position
             * @param bean
             */
            @Override
            public void onItemClick(BaseViewHolder holder, int position, String bean) {
                //会先回调onPageSelected,然后回调onPageScrolled
                rvScrolledByTouch = false;
                scrolledByClick = true;
                position_selected_last = viewPager2.getCurrentItem();
                viewPager2.setCurrentItem(position);
            }
        };
        cyTabLayout.getHorizontalRecyclerView()
                .addItemDecoration(new LinearItemDecoration(simpleAdapter_tab)
                        .setSpace_horizontal(dpAdapt(20)).setSpace_vertical(dpAdapt(8)));
        cyTabLayout.getHorizontalRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LogUtils.log("AAAonScrolleddx", dx);
                if (!rvScrolledByVp) {
                    offsetX_touch -= dx;
                    LogUtils.log("offsetX_touch!rvScrolledByVp", offsetX_touch);
                    cyTabLayout.getHorizontalRecyclerView().setOffsetX(cyTabLayout.getHorizontalRecyclerView().getOffsetX() - dx);
                    rvScrolledByTouch = true;
                }
                RecyclerView.ViewHolder viewHolder = cyTabLayout.getHorizontalRecyclerView().findViewHolderForAdapterPosition(viewPager2.getCurrentItem());
                if (viewHolder != null) {
                    LogUtils.log("onScrolledviewHolder != null");
                    cyTabLayout.setWidth_indicator(cyTabLayout.getWidth_indicator_selected())
                            .setProgress((int) (viewHolder.itemView.getLeft()
                                    + viewHolder.itemView.getWidth() * 1f / 2
                                    - cyTabLayout.getWidth_indicator() / 2));
                } else {
                    cyTabLayout.setWidth_indicator(0).invalidate();
                }
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        RecyclerView.ViewHolder viewHolder = cyTabLayout.getHorizontalRecyclerView().findViewHolderForAdapterPosition(viewPager2.getCurrentItem());
                        if (viewHolder != null){
                            cyTabLayout.setWidth_indicator(cyTabLayout.getWidth_indicator_selected())
                                    .setProgress((int) (viewHolder.itemView.getLeft()
                                            + viewHolder.itemView.getWidth() * 1f / 2
                                            - cyTabLayout.getWidth_indicator() / 2));
                        }else {
                            cyTabLayout.setWidth_indicator(0).invalidate();
                        }

                        break;
                }
            }
        });
        cyTabLayout.getHorizontalRecyclerView().setAdapter(simpleAdapter_tab);

        viewPager2.setAdapter(fragmentPageAdapter);

        fragmentPageAdapter.add("关注");
        fragmentPageAdapter.add("推荐");
        fragmentPageAdapter.add("视频");
        fragmentPageAdapter.add("抗疫");
        fragmentPageAdapter.add("深圳");
        fragmentPageAdapter.add("热榜");
        fragmentPageAdapter.add("小视频");
        fragmentPageAdapter.add("软件");
        fragmentPageAdapter.add("探索");
        fragmentPageAdapter.add("在家上课");
        fragmentPageAdapter.add("手机");
        fragmentPageAdapter.add("动漫");
        fragmentPageAdapter.add("通信");
        fragmentPageAdapter.add("影视");
        fragmentPageAdapter.add("互联网");
        fragmentPageAdapter.add("设计");
        fragmentPageAdapter.add("家电");
        fragmentPageAdapter.add("平板");
        fragmentPageAdapter.add("网球");
        fragmentPageAdapter.add("军事");
        fragmentPageAdapter.add("羽毛球");
        fragmentPageAdapter.add("奢侈品");
        fragmentPageAdapter.add("美食");
        fragmentPageAdapter.add("瘦身");
        fragmentPageAdapter.add("幸福里");
        fragmentPageAdapter.add("棋牌");
        fragmentPageAdapter.add("奇闻");
        fragmentPageAdapter.add("艺术");
        fragmentPageAdapter.add("减肥");
        fragmentPageAdapter.add("电玩");
        fragmentPageAdapter.add("台球");
        fragmentPageAdapter.add("八卦");
        fragmentPageAdapter.add("酷玩");
        fragmentPageAdapter.add("彩票");
        fragmentPageAdapter.add("漫画");
        fragmentPageAdapter.notifyDataSetChanged();

        simpleAdapter_tab.add("关注");
        simpleAdapter_tab.add("推荐");
        simpleAdapter_tab.add("视频");
        simpleAdapter_tab.add("抗疫");
        simpleAdapter_tab.add("深圳");
        simpleAdapter_tab.add("热榜");
        simpleAdapter_tab.add("小视频");
        simpleAdapter_tab.add("软件");
        simpleAdapter_tab.add("探索");
        simpleAdapter_tab.add("在家上课");
        simpleAdapter_tab.add("手机");
        simpleAdapter_tab.add("动漫");
        simpleAdapter_tab.add("通信");
        simpleAdapter_tab.add("影视");
        simpleAdapter_tab.add("互联网");
        simpleAdapter_tab.add("设计");
        simpleAdapter_tab.add("家电");
        simpleAdapter_tab.add("平板");
        simpleAdapter_tab.add("网球");
        simpleAdapter_tab.add("军事");
        simpleAdapter_tab.add("羽毛球");
        simpleAdapter_tab.add("奢侈品");
        simpleAdapter_tab.add("美食");
        simpleAdapter_tab.add("瘦身");
        simpleAdapter_tab.add("幸福里");
        simpleAdapter_tab.add("棋牌");
        simpleAdapter_tab.add("奇闻");
        simpleAdapter_tab.add("艺术");
        simpleAdapter_tab.add("减肥");
        simpleAdapter_tab.add("电玩");
        simpleAdapter_tab.add("台球");
        simpleAdapter_tab.add("八卦");
        simpleAdapter_tab.add("酷玩");
        simpleAdapter_tab.add("彩票");
        simpleAdapter_tab.add("漫画");
        simpleAdapter_tab.notifyDataSetChanged();

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
