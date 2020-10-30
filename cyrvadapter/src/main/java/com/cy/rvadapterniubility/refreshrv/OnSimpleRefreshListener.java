package com.cy.rvadapterniubility.refreshrv;


import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.cy.BaseAdapter.R;
import com.cy.refreshlayoutniubility.OnPullListener;

public abstract class OnSimpleRefreshListener extends OnRefreshListener<String> {
    @Override
    public void bindDataToRefreshFinishedLayout(View view, String msg) {
        super.bindDataToRefreshFinishedLayout(view, msg);
        TextView textView = view.findViewById(R.id.tv);
        if (textView != null) {
            textView.setTextColor(Color.BLACK);
            textView.setText(msg);
        }
    }
}
