package com.cy.rvadapterniubility.recyclerview;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @Description:
 * @Author: cy
 * @CreateDate: 2020/8/20 18:52
 * @UpdateUser:
 * @UpdateDate: 2020/8/20 18:52
 * @UpdateRemark:
 * @Version:
 */
public interface IGridItemDecoration {

    public int getSpace();

    public <T extends RecyclerView.ItemDecoration> T getGridItemDecoration();

}
