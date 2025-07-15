package com.cy.recyclerviewadapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * @Description:
 * @Author: cy
 * @CreateDate: 2020/9/24 22:12
 * @UpdateUser:
 * @UpdateDate: 2020/9/24 22:12
 * @UpdateRemark:
 * @Version: 1.0
 */
public class BaseFragment extends Fragment {
    protected ComponentActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (ComponentActivity) context;
    }

    public void startActivity(Class<?> cls) {
        startActivity(createIntent(cls));
    }

    public Intent createIntent(Class<?> cls) {
        return new Intent(getContext(), cls);
    }

    public void showToast(String str) {
        ToastUtils.showToast(activity, str);
    }

    public void showToast(@StringRes int id) {
        ToastUtils.showToast(activity, getResources().getString(id));
    }
}
