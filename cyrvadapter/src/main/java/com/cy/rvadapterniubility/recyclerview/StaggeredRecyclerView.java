package com.cy.rvadapterniubility.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cy.BaseAdapter.R;
import com.cy.rvadapterniubility.LogUtils;
import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by cy on 2018/4/8.
 */

public class StaggeredRecyclerView extends ScrollView {
    private int orientation = RecyclerView.VERTICAL;
    private int spanCount = 2;
    private List<ILinearRecyclerView> listRV;
    private SimpleAdapter simpleAdapter0;
    private StaggeredAdapter<Object> staggeredAdapter;
    //    private SimpleAdapter simpleAdapter;
    private LinearLayout contentView;

    public StaggeredRecyclerView(Context context) {
        this(context, null);
    }

    public StaggeredRecyclerView(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        listRV = new ArrayList<ILinearRecyclerView>();

        contentView = new LinearLayout(context);
        contentView.setOrientation(LinearLayout.VERTICAL);

        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        for (int i = 0; i < spanCount; i++) {
            final ILinearRecyclerView linearRecyclerView;
            if (orientation == RecyclerView.VERTICAL) {
                linearRecyclerView = new StaggeredVerticalRecyclerView(getContext());
            } else {
                linearRecyclerView = new StaggeredHorizontalRecyclerView(getContext());
            }

            linearRecyclerView.getRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    for (int i = 0; i < spanCount; i++) {
                        int index=listRV.indexOf(linearRecyclerView);
                        ILinearRecyclerView linearR=listRV.get(i);
                        if(i!=index){
//                            ((StaggeredVerticalRecyclerView)linearR.getRecyclerView()).setOffsetX();
//                            ((StaggeredVerticalRecyclerView)linearR.getRecyclerView()).setOffsetX(((StaggeredVerticalRecyclerView)listRV.get(index).getRecyclerView()).getOffsetX());
//                            ((StaggeredVerticalRecyclerView)linearR.getRecyclerView()).setOffsetY(((StaggeredVerticalRecyclerView)listRV.get(index).getRecyclerView()).getOffsetY());
                            ((StaggeredVerticalRecyclerView)linearR.getRecyclerView())
                                    .scrollTo(((StaggeredVerticalRecyclerView)listRV.get(index).getRecyclerView()).getOffsetX(),
                                            ((StaggeredVerticalRecyclerView)listRV.get(index).getRecyclerView()).getOffsetY());
                        }
                    }
                }
            });
            listRV.add(linearRecyclerView);

            ll.addView(listRV.get(i).getRecyclerView(), new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));
        }
//        simpleAdapter = new SimpleAdapter<String>() {
//            @Override
//            public void bindDataToView(BaseViewHolder holder, int position, String bean, boolean isSelected) {
//                LogUtils.log("bindDataToView");
//                contentView = (LinearLayout) holder.itemView;
//                if (contentView.getChildCount() > 0) return;
//                LogUtils.log("bindDataToView222");

//        LinearLayout ll = new LinearLayout(context);
//        ll.setOrientation(LinearLayout.HORIZONTAL);
//        for (int i = 0; i < spanCount; i++) {
//            listRV.get(i).getRecyclerView().setAdapter(staggeredAdapter.getListAdapter().get(i));
//            ll.addView(listRV.get(i).getRecyclerView(), new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));
//        }
        contentView.addView(ll, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//            }
//
//            @Override
//            public int getItemLayoutID(int position, String bean) {
//                return R.layout.cy_staggerd_item;
//            }
//
//            @Override
//            public void onItemClick(BaseViewHolder holder, int position, String bean) {
//
//            }
//        };
//        simpleAdapter.addNoNotify("");

        addView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    LinearLayout getContentView() {
        return contentView;
    }

    public StaggeredRecyclerView setSpanCount(int spanCount) {
        this.spanCount = spanCount;
        return this;
    }

    public StaggeredRecyclerView setOrientation(int orientation) {
        this.orientation = orientation;
        return this;
    }

    public int getOrientation() {
        return orientation;
    }

    public int getSpanCount() {
        return spanCount;
    }

    public <T extends Object> void setAdapter(@Nullable final StaggeredAdapter<T> staggeredAdapter) {
        this.staggeredAdapter = (StaggeredAdapter<Object>) staggeredAdapter;
//        setLayoutManager(new LinearLayoutManager(getContext(), orientation, false));
        for (int i = 0; i < spanCount; i++) {
//            ILinearRecyclerView linearRecyclerView;
//            if (orientation == RecyclerView.VERTICAL) {
//                linearRecyclerView = new StaggeredVerticalRecyclerView(getContext());
//            } else {
//                linearRecyclerView = new StaggeredHorizontalRecyclerView(getContext());
//            }
//            listRV.add(linearRecyclerView);

            if (i == 0) {
                simpleAdapter0 = new SimpleAdapter<T>() {
                    @Override
                    public void bindDataToView(BaseViewHolder holder, int position, T bean, boolean isSelected) {
                        int remain = position % spanCount;
                        if (remain == 0) {
                            staggeredAdapter.bindDataToView(holder, position, getList_bean().get(position), isSelected);
                        } else {
                            staggeredAdapter.getListAdapter().get(remain).notifyItemChanged(position);
                        }
                    }

                    @Override
                    public int getItemLayoutID(int position, T bean) {
                        if (position % spanCount == 0) {
                            return staggeredAdapter.getItemLayoutID(position, getList_bean().get(position));
                        } else {
                            return R.layout.cy_staggerd_item_0;
                        }
                    }

                    @Override
                    public void onItemClick(BaseViewHolder holder, int position, T bean) {
                        staggeredAdapter.onItemClick(holder, position, getList_bean().get(position));
                    }

                    @Override
                    public void onItemLongClick(BaseViewHolder holder, int position, T bean) {
                        super.onItemLongClick(holder, position, bean);
                        staggeredAdapter.onItemLongClick(holder, position, getList_bean().get(position));
                    }

                    @Override
                    public void onViewAttachedToWindow(@NonNull BaseViewHolder holder) {
                        super.onViewAttachedToWindow(holder);
                        if (holder.getBindingAdapterPosition() % spanCount == 0)
                            staggeredAdapter.onViewAttachedToWindow(holder);
                    }
                };
                staggeredAdapter.getListAdapter().add(simpleAdapter0);
            } else {
                staggeredAdapter.getListAdapter().add(new StaggeredInnerAdapter<T>(i) {
                    @Override
                    public void bindDataToView(BaseViewHolder holder, int position, T bean, boolean isSelected) {
                        if (position % spanCount == getIndex())
                            staggeredAdapter.bindDataToView(holder, position, getList_bean().get(position), isSelected);
                    }

                    @Override
                    public int getItemLayoutID(int position, T bean) {
                        if (position % spanCount == getIndex()) {
                            return staggeredAdapter.getItemLayoutID(position, getList_bean().get(position));
                        } else {
                            return R.layout.cy_staggerd_item_0;
                        }
                    }

                    @Override
                    public void onItemClick(BaseViewHolder holder, int position, T bean) {
                        staggeredAdapter.onItemClick(holder, position, getList_bean().get(position));
                    }

                    @Override
                    public void onItemLongClick(BaseViewHolder holder, int position, T bean) {
                        super.onItemLongClick(holder, position, bean);
                        staggeredAdapter.onItemLongClick(holder, position, getList_bean().get(position));
                    }

                    @Override
                    public void onViewAttachedToWindow(@NonNull BaseViewHolder holder) {
                        super.onViewAttachedToWindow(holder);
                        if (holder.getBindingAdapterPosition() % spanCount == getIndex())
                            staggeredAdapter.onViewAttachedToWindow(holder);
                    }
                }.setList_bean(simpleAdapter0.getList_bean()));
            }
            listRV.get(i).getRecyclerView().setAdapter(staggeredAdapter.getListAdapter().get(i));

        }
//        super.setAdapter(simpleAdapter);
    }

    List<ILinearRecyclerView> getListRV() {
        return listRV;
    }
//    @Override
//    public void setAdapter(@Nullable RecyclerView.Adapter adapter) {
//        throw new IllegalArgumentException("You must use " + StaggeredAdapter.class.getName()+" in "+getClass().getName());
//    }
}
