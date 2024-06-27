package com.cy.recyclerviewadapter.activity.vr;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.HRVBean;
import com.cy.recyclerviewadapter.bean.VRMultiBean;
import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.MultiAdapter;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;
import com.cy.rvadapterniubility.recyclerview.HorizontalRecyclerView;
import com.cy.rvadapterniubility.recyclerview.LinearItemDecoration;
import com.cy.rvadapterniubility.recyclerview.OnLinearLoadMoreListener;
import com.cy.rvadapterniubility.recyclerview.VerticalRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class VRMultiActivity extends BaseActivity {

    private SimpleAdapter<VRMultiBean> rvAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vrmulti);
        final List<VRMultiBean> list = new ArrayList<>();

        list.add(new VRMultiBean("忒囧途押金我积极偶就开个会积极自己交给鸡攻击huiyhuhuio8u9ehjkgh会祸害过",
                new int[]{R.drawable.pic1, R.drawable.pic2, R.drawable.pic3}, 1));
        list.add(new VRMultiBean("hi偶尔几个技能奇偶及会计进口国家囧囧而考虑过就没看了交集高科技奇偶及会计胡歌奇偶及会计",
                new int[]{R.drawable.pic5, R.drawable.pic4, R.drawable.pic3}, 2));
        list.add(new VRMultiBean("货物挤公交我黑狗竟然换个我哦我合计好几个我囧囧积极 囧囧囧囧囧窘境及囧窘境囧囧健康人格和基金囧囧花给你们",
                new int[]{R.drawable.pic4, R.drawable.pic5, R.drawable.pic3}, 3));
        list.add(new VRMultiBean("货物挤公交我黑狗竟然换个我哦我合计好几个我囧囧积极 囧囧囧囧囧窘境及囧窘境囧囧健康人格和基金囧囧花给你们",
                new int[]{R.drawable.pic4, R.drawable.pic5, R.drawable.pic3}, 4));


        list.add(new VRMultiBean("忒囧途押金我积极偶就开个会积极自己交给鸡攻击huiyhuhuio8u9ehjkgh会祸害过",
                new int[]{R.drawable.pic4, R.drawable.pic2, R.drawable.pic1}, 1));
        list.add(new VRMultiBean("货物挤公交我黑狗乐观我乳胶管我哦惹急hi偶然和基金法科技馆一积极几颗 自己进欧冠竟然换个我哦健康人格和基金囧囧花给你们",
                new int[]{R.drawable.pic4, R.drawable.pic5, R.drawable.pic3}, 3));
        list.add(new VRMultiBean("忒囧途押金我积极偶就开个会积极自己交给鸡攻击huiyhuhuio8u9ehjkgh会祸害过",
                new int[]{R.drawable.pic1, R.drawable.pic2, R.drawable.pic3}, 1));

        list.add(new VRMultiBean("忒囧途押金我积极偶就开个会积极自己交给鸡攻击huiyhuhuio8u9ehjkgh会祸害过",
                new int[]{R.drawable.pic4, R.drawable.pic2, R.drawable.pic1}, 2));
        list.add(new VRMultiBean("货物挤公交我黑狗乐观我乳胶管我哦惹急hi偶然和基金法科技馆一积极几颗 自己进欧冠竟然换个我哦健康人格和基金囧囧花给你们",
                new int[]{R.drawable.pic4, R.drawable.pic5, R.drawable.pic3}, 3));
        list.add(new VRMultiBean("忒囧途押金我积极偶就开个会积极自己交给鸡攻击huiyhuhuio8u9ehjkgh会祸害过",
                new int[]{R.drawable.pic1, R.drawable.pic2, R.drawable.pic3}, 1));

        list.add(new VRMultiBean("忒囧途押金我积极偶就开个会积极自己交给鸡攻击huiyhuhuio8u9ehjkgh会祸害过",
                new int[]{R.drawable.pic4, R.drawable.pic2, R.drawable.pic1}, 2));
        list.add(new VRMultiBean("货物挤公交我黑狗乐观我乳胶管我哦惹急hi偶然和基金法科技馆一积极几颗 自己进欧冠竟然换个我哦健康人格和基金囧囧花给你们",
                new int[]{R.drawable.pic4, R.drawable.pic5, R.drawable.pic3}, 2));
        list.add(new VRMultiBean("忒囧途押金我积极偶就开个会积极自己交给鸡攻击huiyhuhuio8u9ehjkgh会祸害过",
                new int[]{R.drawable.pic1, R.drawable.pic2, R.drawable.pic3}, 1));

        list.add(new VRMultiBean("忒囧途押金我积极偶就开个会积极自己交给鸡攻击huiyhuhuio8u9ehjkgh会祸害过",
                new int[]{R.drawable.pic4, R.drawable.pic2, R.drawable.pic1}, 2));
        list.add(new VRMultiBean("货物挤公交我黑狗乐观我乳胶管我哦惹急hi偶然和基金法科技馆一积极几颗 自己进欧冠竟然换个我哦健康人格和基金囧囧花给你们",
                new int[]{R.drawable.pic4, R.drawable.pic5, R.drawable.pic3}, 3));
        list.add(new VRMultiBean("忒囧途押金我积极偶就开个会积极自己交给鸡攻击huiyhuhuio8u9ehjkgh会祸害过",
                new int[]{R.drawable.pic1, R.drawable.pic2, R.drawable.pic3}, 1));
        rvAdapter = new SimpleAdapter<VRMultiBean>() {
            @Override
            public void bindDataToView(BaseViewHolder holder, int position, VRMultiBean bean) {
                switch (bean.getView_type()) {
                    case 1:
                        holder.setText(R.id.tv, bean.getTitle());
                        holder.setImageResource(R.id.iv, bean.getResID()[0]);
                        holder.setOnClickListener(R.id.tv, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showToast("点击文字");
                            }
                        });
                        break;

                    case 2:
                        holder.setText(R.id.tv, bean.getTitle());
                        holder.setImageResource(R.id.iv_1, bean.getResID()[0]);
                        holder.setImageResource(R.id.iv_2, bean.getResID()[1]);
                        holder.setImageResource(R.id.iv_3, bean.getResID()[2]);
                        holder.setOnClickListener(R.id.tv, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showToast("点击文字");
                            }
                        });
                        break;
                    case 3:
                        holder.setText(R.id.tv, bean.getTitle());
                        holder.setImageResource(R.id.iv, bean.getResID()[0]);
                        holder.setOnClickListener(R.id.tv, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showToast("点击文字");
                            }
                        });
                        break;
                    case 4:
                        HorizontalRecyclerView horizontalRecyclerView = holder.getView(R.id.hrv);
                        List<HRVBean> list = new ArrayList<>();
                        for (int i = 0; i < 10; i++) {
                            if (i % 5 == 0) {
                                list.add(new HRVBean(R.drawable.pic3));
                                continue;

                            }
                            list.add(new HRVBean(R.drawable.pic1));
                        }
                        SimpleAdapter simpleAdapter = new SimpleAdapter<HRVBean>() {
                            @Override
                            public void bindDataToView(BaseViewHolder holder, int position, HRVBean bean) {

                                holder.setImageResource(R.id.iv, bean.getResID());

                            }

                            @Override
                            public int getItemLayoutID(int position, HRVBean bean) {
                                return R.layout.item_hrv;
                            }

                            @Override
                            public void onItemClick(BaseViewHolder holder, int position, HRVBean bean) {
                                showToast("点击" + position);
                            }
                        };
                        horizontalRecyclerView.addItemDecoration(new LinearItemDecoration().setSpace_horizontal(40).setSpace_vertical(40));
                        horizontalRecyclerView.setAdapter(simpleAdapter);
                        simpleAdapter.add(list);
                        break;
                }
            }

            @Override
            public int getItemLayoutID(int position, VRMultiBean bean) {
                switch (bean.getView_type()) {
                    case 1:
                        return R.layout.item_pic_right;
                    case 2:
                        return R.layout.item_pic_bottom;
                    case 3:
                        return R.layout.item_pic_one_bottom;
                    case 4:
                        return R.layout.item_multi_hrv;
                }
                return 1;
            }


            @Override
            public void onItemClick(BaseViewHolder holder, int position, VRMultiBean bean) {

                if (position == 4) return;
                showToast("点击" + position);
            }
        };
        ((VerticalRecyclerView) findViewById(R.id.vr)).setAdapter(rvAdapter);
        rvAdapter.add(list);
    }

    @Override
    public void onClick(View v) {

    }
}
