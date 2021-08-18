package com.example.mvvm.viewmodel;

import android.view.View;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import com.example.mvvm.repository.BaseRepository;

/**
 * @ClassName BaseViewModel
 * @Description TODO
 * @Author 崔俊杰
 * @Date 2021/8/18 0018 09:31
 * @Version 1.0
 */
public abstract class BaseViewModel<Repo extends BaseRepository> extends ViewModel implements LifecycleObserver {
    protected Repo mRepository;
    /**
     * Lifecycle声明周期
     */
    private LifecycleOwner mOwner;
    public BaseViewModel(LifecycleOwner owner){
        mRepository=createRepository();
        mOwner=owner;
        mOwner.getLifecycle().addObserver(this);
    }

    /**
     * 创建并初始化Repository获取model的数据
     * @return
     */
    protected abstract Repo createRepository();

    /**
     * 生命周期开始处时执行
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void uiConnection(){
        initResource();
    }

    /**
     * 生命周期停止处时执行
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void disConnection(){
        releaseResource();
        //移除Lifecycle
        mOwner.getLifecycle().removeObserver(this);
    }

    /**
     * 释放资源
     * @param
     * @return
     * @author zhangyue
     * @time 2021/8/17 9:16
     */
    protected void releaseResource(){

    }

    /**
     * 资源的初始化
     * @param
     * @return
     * @author zhangyue
     * @time 2021/8/17 9:16
     */
    protected void initResource(){

    }
}
