package com.cy.rvadapterniubility.recyclerview;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @Description:
 * @Author: cy
 * @CreateDate: 2020/8/20 17:22
 * @UpdateUser:
 * @UpdateDate: 2020/8/20 17:22
 * @UpdateRemark:
 * @Version:
 */
public interface ILinearRecyclerView {

    public <T extends RecyclerView> T getRecyclerView();

}
