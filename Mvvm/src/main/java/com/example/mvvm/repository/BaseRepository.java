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
        //获取这个类
        Class<? extends BaseRepository> clazz = this.getClass();
        //获取这个类里的字段
        Field[] fields = clazz.getDeclaredFields();
        //判断是否为空不为空执行下边，不为空抛异常
        if (fields==null||fields.length==0){
            throw new MVVMModelException("no have any fields info...");
        }
        boolean flage=false;
        for (Field field:fields){
            //获取带注解的文件
            Model annotation = field.getAnnotation(Model.class);
            //判断是否为空
            if (null!=annotation){
                flage=true;
                //把安全监测关闭
                field.setAccessible(true);
                //
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
