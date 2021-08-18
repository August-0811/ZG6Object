package com.example.mvvm.view;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.example.mvvm.common.MVVMModelException;
import com.example.mvvm.viewmodel.BaseViewModel;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName BaseMVVMActivity
 * @Description TODO
 * @Author 崔俊杰
 * @Date 2021/8/18 0018 09:39
 * @Version 1.0
 */
public abstract class BaseMVVMActivity<VM extends BaseViewModel,Binding extends ViewDataBinding> extends AppCompatActivity {
    protected Binding mBinding;
    protected VM mViewModel;
    private HashMap<Integer,Object> mMap=new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(this,getLayoutId());
        //设置生命周期的所有者，如果不设置使用LiveData页面将无法获取到数据的更新
        mBinding.setLifecycleOwner(this);
        mViewModel=createViewModel();
        prepareSetVars(mMap);
        setVars(mBinding,mMap);

        loadData();
        initEvent();
    }

    /**
     * 初始化事件
     */
    protected abstract void initEvent();

    /**
     * 初始化数据意思为获取数据
     */
    protected abstract void loadData();

    /**
     * 设置变量
     * @param mBinding
     * @param mMap
     */
    protected void setVars(Binding mBinding, HashMap<Integer, Object> mMap){
        //判断是否有变量 如果没有就抛出异常
        if (mMap.size()==0){
            throw new MVVMModelException("please set variable..");
        }
        for (Map.Entry<Integer,Object> entry:mMap.entrySet()) {
           //setVariable用来设置属于用户的变量,第一个变量类似于findViewbyId
            //例如 xml中Text的id为name
            // 这里要写BR.name,第二个参数为设置的数据
            mBinding.setVariable(entry.getKey(),entry.getValue());
        }
    }


    /**
     * 准备设置数据源
     * @param mMap
     */
    protected abstract void prepareSetVars(HashMap<Integer, Object> mMap);

    /**
     * 创建ViewModel实例
     * @param
     * @return
     * @time 2021/8/17 8:59
     */
    protected abstract VM createViewModel();

    /**
     * 让子类去重写获取布局资源id
     * @return
     */
    protected abstract int getLayoutId();
}
