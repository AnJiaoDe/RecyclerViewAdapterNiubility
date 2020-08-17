//package com.cy.cyrvadapter.adapter;
//
//import android.util.SparseArray;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.recyclerview.widget.RecyclerView.Adapter;
//
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//
//public class MergeAdapter___ extends RecyclerView.Adapter {
//    private List<Adapter> list_adapter;
//    private SparseArray<Adapter> viewTypeAdapter;
//    public MergeAdapter___() {
//        list_adapter = new ArrayList<>();
//        viewTypeAdapter=new SparseArray<>();
//    }
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return null;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        AdapterWrapper adapterWrapper = getAdapterWrapper(position);
//        adapterWrapper.getAdapter().onBindViewHolder(holder, adapterWrapper.getPosition());
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        AdapterWrapper adapterWrapper = getAdapterWrapper(position);
//        int viewType= adapterWrapper.getAdapter().getItemViewType(adapterWrapper.getPosition());
//        return viewType;
//    }
//
//    @Override
//    public int getItemCount() {
//        int count = 0;
//        for (Adapter adapter : list_adapter) {
//            count += adapter.getItemCount();
//        }
//        return count;
//    }
//
//    private AdapterWrapper getAdapterWrapper(int position) {
//        AdapterWrapper adapterWrapper = new AdapterWrapper();
//        int count = 0;
//        for (Adapter adapter : list_adapter) {
//            if (position < adapter.getItemCount()) {
//                adapterWrapper.setAdapter(adapter);
//                adapterWrapper.setPosition(position - count);
//                return adapterWrapper;
//            } else {
//                count += adapter.getItemCount();
//            }
//        }
//        return adapterWrapper;
//    }
//
//    private static class AdapterWrapper {
//        private Adapter adapter;
//        private int position;
//        public Adapter getAdapter() {
//            return adapter;
//        }
//
//        public void setAdapter(Adapter adapter) {
//            this.adapter = adapter;
//        }
//
//        public int getPosition() {
//            return position;
//        }
//
//        public void setPosition(int position) {
//            this.position = position;
//        }
//    }
//}