package com.fanqie.appmodel.common.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

/**
 * presenter抽象基类
 *
 * Created by Administrator on 2016/8/9.
 */
public class BasePresenter  {

    //在继承的acvitity里面初始化
    protected BaseActivity rootActivity;

    /**
     * 添加fragment
     */
    public void addFragment(int contentId , Fragment fragment){

        rootActivity.getSupportFragmentManager().beginTransaction()
                .add(contentId , fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();

    }


    /**
     * 添加fragment 加入栈
     */
    public void addFragmentToStack(int contentId , Fragment fragment){

        rootActivity.getSupportFragmentManager().beginTransaction()
                .add(contentId , fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(fragment.getClass().getName())
                .commit();


    }

    /**
     * 替换fragment
     */
    public void replaceFragment(int contentId , Fragment fragment){

        rootActivity.getSupportFragmentManager().beginTransaction()
                .replace(contentId , fragment)
                .commit();

    }

    /**
     * 替换fragment 加入栈
     */
    public void replaceFragmentToStack(int contentId , Fragment fragment){

        rootActivity.getSupportFragmentManager().beginTransaction()
                .replace(contentId , fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();

    }

    /**
     * 销毁fragment
     */
    public void destoryFragmentFromStack(Fragment fragment){
        rootActivity.getSupportFragmentManager().beginTransaction().remove(fragment);
    }

    /**
     * 返回
     */
    public void toBack(){

        int backStackEntryCount = rootActivity.getSupportFragmentManager().getBackStackEntryCount();

        for (int i = 0; i < backStackEntryCount; i++) {
            Log.i("basepresenter" , rootActivity.getSupportFragmentManager().getBackStackEntryAt(i).toString());
        }

        if (backStackEntryCount > 0) {
            rootActivity.getSupportFragmentManager().popBackStack();
        } else {
            rootActivity.finish();
        }

    }


}
