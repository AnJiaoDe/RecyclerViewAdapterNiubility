package com.cy.recyclerviewadapter.activity.tabrv;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.cy.cyrvadapter.adapter.BaseViewHolder;
import com.cy.cyrvadapter.adapter.SimpleAdapter;
import com.cy.cyrvadapter.tablayout.TabLayoutSimple;
import com.cy.recyclerviewadapter.R;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentTab1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTab1 extends Fragment {
    private ViewPager2 viewPager2;
    private TabLayoutSimple cyTabLayout;
    private FragmentPageAdapter fragmentPageAdapter;
    public static final String TAB_NAME1 = "TAB_NAME1";
    private View view;
    public String tab_name1 = "";
    private SimpleAdapter<String> simpleAdapter_tab;

    public FragmentTab1() {
    }

    public static FragmentTab1 newInstance(String key, String value) {
        FragmentTab1 fragment = new FragmentTab1();
        Bundle args = new Bundle();
        args.putString(key, value);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab1, container, false);
        viewPager2 = view.findViewById(R.id.view_pager);
        cyTabLayout = view.findViewById(R.id.tablayout);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });

        fragmentPageAdapter = new FragmentPageAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return FragmentTab2.newInstance(FragmentTab2.TAB_NAME2, getList_bean().get(position));
            }
        };
        viewPager2.setAdapter(fragmentPageAdapter);



        simpleAdapter_tab=new SimpleAdapter<String>() {
            @Override
            public void bindDataToView(BaseViewHolder holder, int position, String bean,boolean isSelected) {
                holder.setText(R.id.tv,bean);
            }

            @Override
            public int getItemLayoutID(int position, String bean) {
                return R.layout.item_tab;
            }

            @Override
            public void onItemClick(BaseViewHolder holder, int position, String bean) {

            }
        };
        cyTabLayout.getHorizontalRecyclerView().setAdapter(simpleAdapter_tab);



        if (getArguments() != null) {
            tab_name1 = getArguments().getString(TAB_NAME1);
            fragmentPageAdapter.add(tab_name1+"0");
            fragmentPageAdapter.add(tab_name1+"1");
            fragmentPageAdapter.add(tab_name1+"2");
            fragmentPageAdapter.add(tab_name1+"3");
            fragmentPageAdapter.add(tab_name1+"4");
            fragmentPageAdapter.add(tab_name1+"5");
            fragmentPageAdapter.add(tab_name1+"6");
            fragmentPageAdapter.add(tab_name1+"7");
            fragmentPageAdapter.add(tab_name1+"8");
            fragmentPageAdapter.add(tab_name1+"9");
            fragmentPageAdapter.add(tab_name1+"10");
            fragmentPageAdapter.notifyDataSetChanged();

            simpleAdapter_tab.add(tab_name1+"0");
            simpleAdapter_tab.add(tab_name1+"1");
            simpleAdapter_tab.add(tab_name1+"2");
            simpleAdapter_tab.add(tab_name1+"3");
            simpleAdapter_tab.add(tab_name1+"4");
            simpleAdapter_tab.add(tab_name1+"5");
            simpleAdapter_tab.add(tab_name1+"6");
            simpleAdapter_tab.add(tab_name1+"7");
            simpleAdapter_tab.add(tab_name1+"8");
            simpleAdapter_tab.add(tab_name1+"9");
            simpleAdapter_tab.add(tab_name1+"10");
            simpleAdapter_tab.notifyDataSetChanged();
        }




        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
