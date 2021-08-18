package com.example.mvvm.repository;

import com.example.mvvm.model.IModel;

/**
 * @ClassName BaseRepository
 * @Description TODO
 * @Author 崔俊杰
 * @Date 2021/8/18 0018 09:28
 * @Version 1.0
 */
public abstract class BaseRepository<M extends IModel> {
    protected M mModel;

    public BaseRepository(){
        mModel=createRepostory();
    }

    protected abstract M createRepostory();

}
