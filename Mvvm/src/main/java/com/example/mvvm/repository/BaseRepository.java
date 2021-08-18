package com.example.mvvm.repository;

import com.example.mvvm.common.MVVMModelException;
import com.example.mvvm.model.IModel;
import com.example.mvvm.model.Model;

import java.lang.reflect.Field;

/**
 * @ClassName BaseRepository
 * @Description TODO
 * @Author 崔俊杰
 * @Date 2021/8/18 0018 09:28
 * @Version 1.0
 */
public abstract class BaseRepository {
    //普通mvvm只能对一个model
//    protected M mModel;
//
//    public BaseRepository(){
//        mModel=createRepostory();
//    }
//
//    protected abstract M createRepostory();

    public BaseRepository(){
        injectModel();
    }

    /**
     * 反射设置Model的实例
     */
    private void injectModel(){
        //获取这个类的Class对象
        Class<? extends BaseRepository> clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();
        if (fields==null||fields.length==0){
            throw new MVVMModelException("no have any fields info...");
        }
        boolean flage=false;
        for (Field field:fields){
            Model annotation = field.getAnnotation(Model.class);
            if (null!=annotation){
                flage=true;
                field.setAccessible(true);
                String fieldClassName = field.getType().getName();
                try {
                    //通过反射动态创建实例
                    Class<?> fieldClazz = Class.forName(fieldClassName);
                    Object fieldInstance =fieldClazz.newInstance();
                    field.set(this,fieldInstance);
                } catch (ClassNotFoundException e) {
                    throw new MVVMModelException(e.getMessage());
                } catch (IllegalAccessException e) {
                    throw new MVVMModelException(e.getMessage());
                } catch (InstantiationException e) {
                    throw new MVVMModelException(e.getMessage());
                }
            }
        }
        if (!flage){
            throw new MVVMModelException("no set any model...");
        }
    }
}
