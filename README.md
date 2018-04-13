https://blog.csdn.net/confusing_awakening/article/details/78332215

**使用方法**

将libray模块复制到项目中,或者直接在build.gradle中依赖:

```
dependencies {
	        compile 'com.github.437042721:RecyclerViewAdapter:1.1.1'
	}
```

在没有万能RV适配器的时候是这样写代码的

```
    private class CurrHolder extends RecyclerView.ViewHolder implements
            OnClickListener, ItemSelectListener {
        ImageView album, menu;
        TextView title, artist;
        View splite, state;
        Music music;

        public CurrHolder(View view) {
            super(view);
            album = (ImageView) view.findViewById(R.id.music_item_album);
            menu = (ImageView) view.findViewById(R.id.music_item_menu);
            title = (TextView) view.findViewById(R.id.music_item_title);
            artist = (TextView) view.findViewById(R.id.music_item_artist);
            splite = view.findViewById(R.id.music_item_splite);
            state = view.findViewById(R.id.music_item_state);
            menu.setImageResource(mTheme.isColorTheme() ? R.drawable.ic_item_menu
                    : R.drawable.ic_item_menu_w);
            if (splite != null) {
                splite.setBackgroundColor(mTheme.getDividerColor());
            }
            state.setBackgroundColor(mTheme.color);
            itemView.setOnClickListener(this);
            ViewUtil.setViewBg(itemView, mTheme.getItemSelector());
        }
```
 构造方法能写到怀疑人生
 
 有万能适配的时候是这样写代码的

  

```
    List<VRBean> list = new ArrayList<>();
        for (int i=0;i<100;i++){
            list.add(new VRBean("内容"+i));
        }
        rvAdapter = new RVAdapter<VRBean>(list) {
            @Override
            public void bindDataToView(MyViewHolder holder, int position, VRBean bean, boolean isSelected) {
                holder.setText(R.id.tv, bean.getStr());
            }

            @Override
            public int getItemLayoutID(int position, VRBean bean) {
                return R.layout.item_rv;
            }



            @Override
            public void onItemClick(int position, VRBean bean) {
                showToast("点击" + position);

            }
        };
        ((VerticalRecyclerView) findViewById(R.id.vr)).setAdapter(rvAdapter);
```
  
 
 流水线式编程，各种复制粘贴

![这里写图片描述](https://img-blog.csdn.net/20180413112843364?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbmZ1c2luZ19hd2FrZW5pbmc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

**1.VerticalRecyclerView**
**1.1VR**
![这里写图片描述](https://img-blog.csdn.net/20180413113246318?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbmZ1c2luZ19hd2FrZW5pbmc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

```
<?xml version="1.0" encoding="utf-8"?>
<com.cy.cyrvadapter.recyclerview.VerticalRecyclerView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/vr"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />


```

```
package com.cy.recyclerviewadapter.activity.vr;

import android.os.Bundle;
import android.view.View;

import com.cy.cyrvadapter.adapter.RVAdapter;
import com.cy.cyrvadapter.recyclerview.VerticalRecyclerView;
import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.VRBean;

import java.util.ArrayList;
import java.util.List;

public class VRActivity extends BaseActivity {

    private RVAdapter<VRBean> rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vr2);
        List<VRBean> list = new ArrayList<>();
        for (int i=0;i<100;i++){
            list.add(new VRBean("内容"+i));
        }
        rvAdapter = new RVAdapter<VRBean>(list) {
            @Override
            public void bindDataToView(MyViewHolder holder, int position, VRBean bean, boolean isSelected) {
                holder.setText(R.id.tv, bean.getStr());
            }

            @Override
            public int getItemLayoutID(int position, VRBean bean) {
                return R.layout.item_rv;
            }



            @Override
            public void onItemClick(int position, VRBean bean) {
                showToast("点击" + position);

            }
        };
        ((VerticalRecyclerView) findViewById(R.id.vr)).setAdapter(rvAdapter);
    }

    @Override
    public void onClick(View v) {

    }
}

```

**1.2VR+MultiLayout**
![这里写图片描述](https://img-blog.csdn.net/20180413121715601?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbmZ1c2luZ19hd2FrZW5pbmc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

```
<?xml version="1.0" encoding="utf-8"?>
<com.cy.cyrvadapter.recyclerview.VerticalRecyclerView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/vr"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />


```

```
package com.cy.recyclerviewadapter.activity.vr;

import android.os.Bundle;
import android.view.View;

import com.cy.cyrvadapter.adapter.RVAdapter;
import com.cy.cyrvadapter.recyclerview.VerticalRecyclerView;
import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.VRMultiBean;

import java.util.ArrayList;
import java.util.List;

public class VRMultiActivity extends BaseActivity {

    private RVAdapter<VRMultiBean> rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vrmulti);
        final List<VRMultiBean> list = new ArrayList<>();

        list.add(new VRMultiBean("忒囧途押金我积极偶就开个会积极自己交给鸡攻击huiyhuhuio8u9ehjkgh会祸害过",
                new int[]{R.drawable.pic1, R.drawable.pic2, R.drawable.pic3}, 1));
        list.add(new VRMultiBean("hi偶尔几个技能奇偶及会计进口国家囧囧而考虑过就没看了交集高科技奇偶及会计胡歌奇偶及会计",
                new int[]{R.drawable.pic5, R.drawable.pic4, R.drawable.pic3}, 2));
        list.add(new VRMultiBean("货物挤公交我黑狗竟然换个我哦我合计好几个我囧囧积极 囧囧囧囧囧窘境及囧窘境囧囧健康人格和基金囧囧花给你们",
                new int[]{R.drawable.pic4, R.drawable.pic5, R.drawable.pic3}, 3));


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
        rvAdapter = new RVAdapter<VRMultiBean>(list) {
            @Override
            public void bindDataToView(MyViewHolder holder, int position, VRMultiBean bean, boolean isSelected) {
                switch (bean.getView_type()) {
                    case 1:
                        holder.setText(R.id.tv,bean.getTitle());
                        holder.setImageResource(R.id.iv,bean.getResID()[0]);
                        holder.setOnClickListener(R.id.tv, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showToast("点击文字");
                            }
                        });
                        break;

                    case 2:
                        holder.setText(R.id.tv,bean.getTitle());
                        holder.setImageResource(R.id.iv_1,bean.getResID()[0]);
                        holder.setImageResource(R.id.iv_2,bean.getResID()[1]);
                        holder.setImageResource(R.id.iv_3,bean.getResID()[2]);
                        holder.setOnClickListener(R.id.tv, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showToast("点击文字");
                            }
                        });
                        break;
                    case 3:
                        holder.setText(R.id.tv,bean.getTitle());
                        holder.setImageResource(R.id.iv,bean.getResID()[0]);
                        holder.setOnClickListener(R.id.tv, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showToast("点击文字");
                            }
                        });
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
                }
                return 1;
            }


            @Override
            public void onItemClick(int position, VRMultiBean bean) {

                showToast("点击"+position);
            }
        };
        ((VerticalRecyclerView) findViewById(R.id.vr)).setAdapter(rvAdapter);
    }

    @Override
    public void onClick(View v) {

    }
}

```
**1.3VR+head+foot**
![这里写图片描述](https://img-blog.csdn.net/20180413122318313?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbmZ1c2luZ19hd2FrZW5pbmc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

```
<?xml version="1.0" encoding="utf-8"?>
<com.cy.cyrvadapter.recyclerview.VerticalRecyclerView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/vr"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />


```

```
package com.cy.recyclerviewadapter.activity.vr;

import android.os.Bundle;
import android.view.View;

import com.cy.cyrvadapter.adapter.RVAdapter;
import com.cy.cyrvadapter.recyclerview.VerticalRecyclerView;
import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.VRHeadFootBean;

import java.util.ArrayList;
import java.util.List;

public class VRHeadFootActivity extends BaseActivity {

    private RVAdapter<VRHeadFootBean> rvAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vr);

        List<VRHeadFootBean> list = new ArrayList<>();
        for (int i=0;i<100;i++){
            list.add(new VRHeadFootBean("内容"+i));
        }
        rvAdapter=new RVAdapter<VRHeadFootBean>(list,true,true) {
            @Override
            public void bindDataToHeadView(MyViewHolder holder) {
                super.bindDataToHeadView(holder);
            }

            @Override
            public void bindDataToFootView(MyViewHolder holder) {
                super.bindDataToFootView(holder);
            }

            @Override
            public void bindDataToView(MyViewHolder holder, int position, VRHeadFootBean bean, boolean isSelected) {
                holder.setText(R.id.tv, bean.getStr());

            }

            @Override
            public int getItemLayoutID(int position, VRHeadFootBean bean) {
                if (position==0){
                    return R.layout.head;

                }
                if (position==getItemCount()-1){
                    return R.layout.foot;

                }
                return R.layout.item_rv;

            }

            @Override
            public void onItemClick(int position, VRHeadFootBean bean) {

                showToast("点击"+position);
            }

            @Override
            public void onItemHeadClick() {
                super.onItemHeadClick();
                showToast("点击head");

            }

            @Override
            public void onItemFootClick() {
                super.onItemFootClick();
                showToast("点击foot");

            }
        };
        ((VerticalRecyclerView) findViewById(R.id.vr)).setAdapter(rvAdapter);

    }

    @Override
    public void onClick(View v) {

    }
}

```
**1.4VR+Refresh+LoadMore**
![这里写图片描述](https://img-blog.csdn.net/2018041312265859?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbmZ1c2luZ19hd2FrZW5pbmc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

```
<?xml version="1.0" encoding="utf-8"?>
<com.cy.cyrvadapter.refreshrv.VerticalRefreshLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/vrl"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

</com.cy.cyrvadapter.refreshrv.VerticalRefreshLayout>

```

```
package com.cy.recyclerviewadapter.activity.vr;

import android.os.Bundle;
import android.view.View;

import com.cy.cyrvadapter.adapter.RVAdapter;
import com.cy.cyrvadapter.refreshrv.VerticalRefreshLayout;
import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.VRBean;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class VRRefreshLoadMoreActivity extends BaseActivity {

    private RVAdapter<VRBean> rvAdapter;
    private VerticalRefreshLayout verticalRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vrrefresh);

        verticalRefreshLayout= (VerticalRefreshLayout) findViewById(R.id.vrl);
        List<VRBean> list = new ArrayList<>();
        for (int i=0;i<100;i++){
            list.add(new VRBean("内容"+i));
        }
        rvAdapter = new RVAdapter<VRBean>(list) {
            @Override
            public void bindDataToView(MyViewHolder holder, int position, VRBean bean, boolean isSelected) {
                holder.setText(R.id.tv, bean.getStr());
            }

            @Override
            public int getItemLayoutID(int position, VRBean bean) {
                return R.layout.item_rv;

            }


            @Override
            public void onItemClick(int position, VRBean bean) {
                showToast("点击" + position);

            }
        };
        verticalRefreshLayout.setAdapter(rvAdapter,  getResources().getColor(R.color.colorPrimary),
                new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}

```
**1.5VR+Refresh**

![这里写图片描述](https://img-blog.csdn.net/20180413123006536?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbmZ1c2luZ19hd2FrZW5pbmc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

```
<?xml version="1.0" encoding="utf-8"?>
<com.cy.cyrvadapter.refreshrv.VerticalRefreshLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/vrl"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

</com.cy.cyrvadapter.refreshrv.VerticalRefreshLayout>

```

```
package com.cy.recyclerviewadapter.activity.vr;

import android.os.Bundle;
import android.view.View;

import com.cy.cyrvadapter.adapter.RVAdapter;
import com.cy.cyrvadapter.refreshrv.BaseRefreshLayout;
import com.cy.cyrvadapter.refreshrv.VerticalRefreshLayout;
import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.VRBean;

import java.util.ArrayList;
import java.util.List;

public class VRRefreshActivity extends BaseActivity {
    private RVAdapter<VRBean> rvAdapter;
    private VerticalRefreshLayout verticalRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vrrefresh2);

        verticalRefreshLayout= (VerticalRefreshLayout) findViewById(R.id.vrl);
        List<VRBean> list = new ArrayList<>();
        for (int i=0;i<100;i++){
            list.add(new VRBean("内容"+i));
        }
        rvAdapter = new RVAdapter<VRBean>(list) {
            @Override
            public void bindDataToView(MyViewHolder holder, int position, VRBean bean, boolean isSelected) {
                holder.setText(R.id.tv, bean.getStr());
            }

            @Override
            public int getItemLayoutID(int position, VRBean bean) {
                return R.layout.item_rv;

            }

            @Override
            public void onItemClick(int position, VRBean bean) {
                showToast("点击" + position);

            }
        };
        verticalRefreshLayout.setAdapter(rvAdapter,  getResources().getColor(R.color.colorPrimary),
                new BaseRefreshLayout.OnCYRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}


```
**1.6VR+LoadMore**
![这里写图片描述](https://img-blog.csdn.net/2018041312313173?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbmZ1c2luZ19hd2FrZW5pbmc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

```
<?xml version="1.0" encoding="utf-8"?>
<com.cy.cyrvadapter.refreshrv.VerticalRefreshLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/vrl"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

</com.cy.cyrvadapter.refreshrv.VerticalRefreshLayout>

```

```
package com.cy.recyclerviewadapter.activity.vr;

import android.os.Bundle;
import android.view.View;

import com.cy.cyrvadapter.adapter.RVAdapter;
import com.cy.cyrvadapter.refreshrv.BaseRefreshLayout;
import com.cy.cyrvadapter.refreshrv.VerticalRefreshLayout;
import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.VRBean;

import java.util.ArrayList;
import java.util.List;

public class VRLoadMoreActivity extends BaseActivity {
    private RVAdapter<VRBean> rvAdapter;
    private VerticalRefreshLayout verticalRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vrload_more);


        verticalRefreshLayout= (VerticalRefreshLayout) findViewById(R.id.vrl);
        List<VRBean> list = new ArrayList<>();
        for (int i=0;i<100;i++){
            list.add(new VRBean("内容"+i));
        }
        rvAdapter = new RVAdapter<VRBean>(list) {
            @Override
            public void bindDataToView(MyViewHolder holder, int position, VRBean bean, boolean isSelected) {
                holder.setText(R.id.tv, bean.getStr());
            }

            @Override
            public int getItemLayoutID(int position, VRBean bean) {
                return R.layout.item_rv;
            }


            @Override
            public void onItemClick(int position, VRBean bean) {
                showToast("点击" + position);

            }
        };
        verticalRefreshLayout.setAdapter(rvAdapter,  getResources().getColor(R.color.colorPrimary),
                new BaseRefreshLayout.OnCYLoadMoreLister() {
            @Override
            public void onLoadMore() {

            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}


```

**1.7VR+Swipe**
![这里写图片描述](https://img-blog.csdn.net/20180413123457843?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbmZ1c2luZ19hd2FrZW5pbmc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
```
<?xml version="1.0" encoding="utf-8"?>
<com.cy.cyrvadapter.recyclerview.SwipeRecyclerView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/srv"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

</com.cy.cyrvadapter.recyclerview.SwipeRecyclerView>

```

```
package com.cy.recyclerviewadapter.activity.vr;

import android.os.Bundle;
import android.view.View;

import com.cy.cyrvadapter.adapter.SwipeRVAdapter;
import com.cy.cyrvadapter.recyclerview.SwipeRecyclerView;
import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.VRBean;

import java.util.ArrayList;

public class VRSwipeActivity extends BaseActivity {
    private SwipeRVAdapter<VRBean> rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vrswipe);

        final ArrayList<VRBean> list_bean = new ArrayList<VRBean>();
        for (int i = 0; i < 100; i++) {
            list_bean.add(new VRBean("内容"+i));
        }


        rvAdapter = new SwipeRVAdapter<VRBean>(list_bean) {


            @Override
            public void bindSwipeDataToView(MyViewHolder holder, int position, VRBean bean, boolean isSelected) {
                holder.setText(R.id.tv, bean.getStr());


                holder.setOnClickListener(R.id.tv_zhiding, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rvAdapter.closeOpenedSL();
                        showToast("点击置顶");

                    }
                });
                holder.setOnClickListener(R.id.tv_biaoweiweidu, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rvAdapter.closeOpenedSL();

                        showToast("点击标为未读");

                    }
                });
                holder.setOnClickListener(R.id.layout_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rvAdapter.closeOpenedSL();

                        showToast("点击删除");

                    }
                });
            }

            @Override
            public int getItemLayoutID(int position, VRBean bean) {
                return R.layout.item_swipe;

            }


            @Override
            public void onItemClick(int position, VRBean bean) {
                showToast("点击内容");

            }


        };
        ((SwipeRecyclerView)findViewById(R.id.srv)).setAdapter(rvAdapter);
    }

    @Override
    public void onClick(View v) {

    }
}

```
**2.HorizontalRecyclerView**
![这里写图片描述](https://img-blog.csdn.net/20180413123704732?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbmZ1c2luZ19hd2FrZW5pbmc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

```
<?xml version="1.0" encoding="utf-8"?>
<com.cy.cyrvadapter.recyclerview.HorizontalRecyclerView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/hrv"
    android:layout_width="match_parent"
    android:layout_height="300dp">

</com.cy.cyrvadapter.recyclerview.HorizontalRecyclerView>

```

```
package com.cy.recyclerviewadapter.activity.hr;

import android.os.Bundle;
import android.view.View;

import com.cy.cyrvadapter.adapter.RVAdapter;
import com.cy.cyrvadapter.recyclerview.HorizontalRecyclerView;
import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.HRVBean;

import java.util.ArrayList;
import java.util.List;

public class HRVActivity extends BaseActivity {

    private RVAdapter<HRVBean> rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hrv);
        List<HRVBean> list = new ArrayList<>();
        for (int i=0;i<10;i++){
            if (i%5==0){
                list.add(new HRVBean(R.drawable.pic3));
                continue;

            }
            list.add(new HRVBean(R.drawable.pic1));
        }
        rvAdapter = new RVAdapter<HRVBean>(list) {
            @Override
            public void bindDataToView(MyViewHolder holder, int position, HRVBean bean, boolean isSelected) {

                holder.setImageResource(R.id.iv,bean.getResID());

            }

            @Override
            public int getItemLayoutID(int position, HRVBean bean) {
                return R.layout.item_hrv;
            }



            @Override
            public void onItemClick(int position, HRVBean bean) {

            }
        };
        ((HorizontalRecyclerView)findViewById(R.id.hrv)).setAdapter(rvAdapter);
    }

    @Override
    public void onClick(View v) {

    }
}

```
**3.GridRecyclerView**
**3.1GRV**
![这里写图片描述](https://img-blog.csdn.net/20180413124027874?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbmZ1c2luZ19hd2FrZW5pbmc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

```
<?xml version="1.0" encoding="utf-8"?>
<com.cy.cyrvadapter.recyclerview.GridRecyclerView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/grv"
    android:paddingRight="10dp"
    android:paddingBottom="10dp"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

</com.cy.cyrvadapter.recyclerview.GridRecyclerView>

```

```
package com.cy.recyclerviewadapter.activity.grv;

import android.os.Bundle;
import android.view.View;

import com.cy.cyrvadapter.adapter.RVAdapter;
import com.cy.cyrvadapter.recyclerview.GridRecyclerView;
import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.HRVBean;

import java.util.ArrayList;
import java.util.List;

public class GRVActivity extends BaseActivity {

    private RVAdapter<HRVBean> rvAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grv);
        List<HRVBean> list = new ArrayList<>();
        for (int i=0;i<100;i++){
            if (i%5==0){
                list.add(new HRVBean(R.drawable.pic3));
                continue;

            }
            list.add(new HRVBean(R.drawable.pic1));
        }
        rvAdapter=new RVAdapter<HRVBean>(list) {
            @Override
            public void bindDataToView(MyViewHolder holder, int position, HRVBean bean, boolean isSelected) {


                holder.setImageResource(R.id.iv,bean.getResID());
            }

            @Override
            public int getItemLayoutID(int position, HRVBean bean) {
                return R.layout.item_grv;
            }


            @Override
            public void onItemClick(int position, HRVBean bean) {

            }
        };
        ((GridRecyclerView)findViewById(R.id.grv)).setAdapter(rvAdapter,3,false,false);
    }

    @Override
    public void onClick(View v) {

    }
}

```
**3.2GRV+head+foot**
![这里写图片描述](https://img-blog.csdn.net/20180413124242987?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbmZ1c2luZ19hd2FrZW5pbmc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

```
<?xml version="1.0" encoding="utf-8"?>
<com.cy.cyrvadapter.recyclerview.GridRecyclerView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/grv"
    android:paddingRight="10dp"
    android:paddingBottom="10dp"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

</com.cy.cyrvadapter.recyclerview.GridRecyclerView>

```

```
package com.cy.recyclerviewadapter.activity.grv;

import android.os.Bundle;
import android.view.View;

import com.cy.cyrvadapter.adapter.RVAdapter;
import com.cy.cyrvadapter.recyclerview.GridRecyclerView;
import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.HRVBean;

import java.util.ArrayList;
import java.util.List;

public class GRVHeadFootActivity extends BaseActivity {
    private RVAdapter<HRVBean> rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grvhead_foot);

        List<HRVBean> list = new ArrayList<>();
        for (int i = 0; i < 99; i++) {
            if (i % 5 == 0) {
                list.add(new HRVBean(R.drawable.pic3));
                continue;

            }
            list.add(new HRVBean(R.drawable.pic1));
        }
        rvAdapter = new RVAdapter<HRVBean>(list, true, true) {
            @Override
            public void bindDataToHeadView(MyViewHolder holder) {
                super.bindDataToHeadView(holder);
            }
            @Override
            public void bindDataToFootView(MyViewHolder holder) {
                super.bindDataToFootView(holder);
            }

            @Override
            public void bindDataToView(MyViewHolder holder, int position, HRVBean bean, boolean isSelected) {


                holder.setImageResource(R.id.iv, bean.getResID());
            }

            @Override
            public int getItemLayoutID(int position, HRVBean bean) {
                if (position == 0) {
                    return R.layout.head;
                }
                if (position == getItemCount() - 1) {
                    return R.layout.foot;
                }
                return R.layout.item_grv;
            }


            @Override
            public void onItemClick(int position, HRVBean bean) {


            }

            @Override
            public void onItemHeadClick() {
                super.onItemHeadClick();
            }

            @Override
            public void onItemFootClick() {
                super.onItemFootClick();
            }
        };
        ((GridRecyclerView) findViewById(R.id.grv)).setAdapter(rvAdapter, 3,true,true);
    }

    @Override
    public void onClick(View v) {

    }
}

```

**3.3GRV+Refresh+LoadMore**
![这里写图片描述](https://img-blog.csdn.net/20180413130342162?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbmZ1c2luZ19hd2FrZW5pbmc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

```
package com.cy.recyclerviewadapter.activity.grv;

import android.os.Bundle;
import android.view.View;

import com.cy.cyrvadapter.adapter.RVAdapter;
import com.cy.cyrvadapter.refreshrv.GridRefreshLayout;
import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.HRVBean;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class GRVRefreshLoadMoreActivity extends BaseActivity {
    private RVAdapter<HRVBean> rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grvrefresh_load_more);

        List<HRVBean> list = new ArrayList<>();
        for (int i=0;i<100;i++){
            if (i%5==0){
                list.add(new HRVBean(R.drawable.pic3));
                continue;

            }
            list.add(new HRVBean(R.drawable.pic1));
        }

        rvAdapter=new RVAdapter<HRVBean>(list) {
            @Override
            public void bindDataToView(MyViewHolder holder, int position, HRVBean bean, boolean isSelected) {


                holder.setImageResource(R.id.iv,bean.getResID());
            }

            @Override
            public int getItemLayoutID(int position, HRVBean bean) {
                return R.layout.item_grv;
            }


            @Override
            public void onItemClick(int position, HRVBean bean) {

            }
        };

        ((GridRefreshLayout)findViewById(R.id.grl)).setAdapter(rvAdapter, 3, false, false,
                getResources().getColor(R.color.colorPrimary),new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}

```
**4.StaggeredGridRecyclerView**
**4.1SGRV**
![这里写图片描述](https://img-blog.csdn.net/20180413130636826?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbmZ1c2luZ19hd2FrZW5pbmc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

```
<?xml version="1.0" encoding="utf-8"?>
<com.cy.cyrvadapter.recyclerview.StaggeredGridRecyclerView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/grv"
    android:paddingRight="10dp"
    android:paddingBottom="10dp"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

</com.cy.cyrvadapter.recyclerview.StaggeredGridRecyclerView>

```

```
package com.cy.recyclerviewadapter.activity.sgrv;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cy.cyrvadapter.adapter.RVAdapter;
import com.cy.cyrvadapter.recyclerview.StaggeredGridRecyclerView;
import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.SGRVBean;

import java.util.ArrayList;
import java.util.List;

public class SGRVActivity extends BaseActivity {
    private RVAdapter<SGRVBean> rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sgrv);


        List<SGRVBean> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            if (i % 5 == 0) {
                list.add(new SGRVBean("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3367190441,1778923800&fm=27&gp=0.jpg",
                        "接地极给客人就公开房间观看然后呢开飞机后肌肉及推介会IT界hi让他开户及"));
                continue;

            }
            list.add(new SGRVBean("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523372810354&di=f4e6cb5fbef06087acb322973b8cf432&imgtype=0&src=http%3A%2F%2Fimg3.duitang.com%2Fuploads%2Fitem%2F201605%2F11%2F20160511200420_WxrRN.jpeg",
                    "个哥哥"));
        }
        rvAdapter = new RVAdapter<SGRVBean>(list) {
            @Override
            public void bindDataToView(final MyViewHolder holder, int position, SGRVBean bean, boolean isSelected) {

                holder.setText(R.id.tv,bean.getText());

                holder.setImage(R.id.iv,bean.getUrl());


            }

            @Override
            public int getItemLayoutID(int position, SGRVBean bean) {
                return R.layout.item_sgrv;
            }


            @Override
            public void onItemClick(int position, SGRVBean bean) {

                showToast("点击" + position);
            }
        };
        ((StaggeredGridRecyclerView) findViewById(R.id.grv)).setAdapter(rvAdapter, 3, RecyclerView.VERTICAL);
    }

    @Override
    public void onClick(View v) {

    }
}
```
**4.2SGRV+head+foot**
![这里写图片描述](https://img-blog.csdn.net/20180413130909337?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbmZ1c2luZ19hd2FrZW5pbmc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

```
<?xml version="1.0" encoding="utf-8"?>
<com.cy.cyrvadapter.recyclerview.StaggeredGridRecyclerView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/grv"
    android:paddingRight="10dp"
    android:paddingBottom="10dp"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

</com.cy.cyrvadapter.recyclerview.StaggeredGridRecyclerView>

```

```
package com.cy.recyclerviewadapter.activity.sgrv;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cy.cyrvadapter.adapter.RVAdapter;
import com.cy.cyrvadapter.recyclerview.StaggeredGridRecyclerView;
import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.HRVBean;

import java.util.ArrayList;
import java.util.List;

public class SGRVHeadFootActivity extends BaseActivity {
    private RVAdapter<HRVBean> rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sgrvhead_foot);


        List<HRVBean> list = new ArrayList<>();
        for (int i = 0; i < 99; i++) {
            if (i % 5 == 0) {
                list.add(new HRVBean(R.drawable.pic3));
                continue;

            }
            list.add(new HRVBean(R.drawable.pic7));
        }
        rvAdapter = new RVAdapter<HRVBean>(list, true,true,true) {
            @Override
            public void bindDataToHeadView(MyViewHolder holder) {
                super.bindDataToHeadView(holder);
            }
            @Override
            public void bindDataToFootView(MyViewHolder holder) {
                super.bindDataToFootView(holder);
            }

            @Override
            public void bindDataToView(MyViewHolder holder, int position, HRVBean bean, boolean isSelected) {



                holder.setImageResource(R.id.iv, bean.getResID());
            }

            @Override
            public int getItemLayoutID(int position, HRVBean bean) {
                if (position == 0) {
                    return R.layout.head;
                }
                if (position == getItemCount() - 1) {
                    return R.layout.foot;
                }
                return R.layout.item_grv;
            }


            @Override
            public void onItemClick(int position, HRVBean bean) {

                showToast("点击"+position);

            }

            @Override
            public void onItemHeadClick() {
                super.onItemHeadClick();
            }

            @Override
            public void onItemFootClick() {
                super.onItemFootClick();
            }
        };
        ((StaggeredGridRecyclerView) findViewById(R.id.grv)).setAdapter(rvAdapter, 3, RecyclerView.VERTICAL);
    }

    @Override
    public void onClick(View v) {

    }
}


```
**4.3SGRV+Refresh+LoadMore**
![这里写图片描述](https://img-blog.csdn.net/20180413131111106?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbmZ1c2luZ19hd2FrZW5pbmc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

```
<?xml version="1.0" encoding="utf-8"?>
<com.cy.cyrvadapter.refreshrv.StaggeredGridRefreshLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/sgrl"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:layout_marginBottom="10dp"
    android:layout_marginRight="10dp">

</com.cy.cyrvadapter.refreshrv.StaggeredGridRefreshLayout>

```

```
package com.cy.recyclerviewadapter.activity.sgrv;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cy.cyrvadapter.adapter.RVAdapter;
import com.cy.cyrvadapter.refreshrv.StaggeredGridRefreshLayout;
import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.HRVBean;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class SGRVRefreshLoadMoreActivity extends BaseActivity {
    private RVAdapter<HRVBean> rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sgrvrefresh_load_more);
        List<HRVBean> list = new ArrayList<>();
        for (int i=0;i<100;i++){
            if (i%5==0){
                list.add(new HRVBean(R.drawable.pic3));
                continue;

            }
            list.add(new HRVBean(R.drawable.pic7));
        }

        rvAdapter=new RVAdapter<HRVBean>(list) {
            @Override
            public void bindDataToView(MyViewHolder holder, int position, HRVBean bean, boolean isSelected) {


                holder.setImageResource(R.id.iv,bean.getResID());
            }

            @Override
            public int getItemLayoutID(int position, HRVBean bean) {
                return R.layout.item_grv;
            }


            @Override
            public void onItemClick(int position, HRVBean bean) {

            }
        };

        ((StaggeredGridRefreshLayout)findViewById(R.id.sgrl)).setAdapter(rvAdapter, 3, RecyclerView.VERTICAL,
                getResources().getColor(R.color.colorPrimary),new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}


```
 
 
 
 参考：[ListView、RecyclerView、GridView ](https://blog.csdn.net/confusing_awakening/article/category/6968968)
