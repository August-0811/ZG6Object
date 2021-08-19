package com.bw.net.retrofit;

import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bw.net.R;
import com.bw.net.protocol.BaseRespEntity;
import com.bw.net.protocol.TokenRespEntity;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Description :
 *
 * @author wangjiaxing
 * @date 2021/8/18
 */
public class LiveDataCallAdapter<T> implements CallAdapter<T, LiveData<BaseRespEntity<R>>> {
    public Type type;

    public LiveDataCallAdapter(Type type) {
        this.type = type;
    }

    @Override
    public Type responseType() {
        return type;
    }

    @Override
    public LiveData<BaseRespEntity<R>> adapt(Call<T> call) {
        MutableLiveData<BaseRespEntity<R>> liveData=new MutableLiveData<>();
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (Looper.getMainLooper().getThread()==Thread.currentThread()){
                    liveData.setValue((BaseRespEntity<R>) response.body());
                }else {
                    liveData.postValue((BaseRespEntity<R>) response.body());
                }
            }
            @Override
            public void onFailure(Call<T> call, Throwable t) {
                BaseRespEntity entity=new BaseRespEntity();
                entity.setCode(-11);
                entity.setMsg(t.getMessage());
                if (Looper.getMainLooper().getThread()==Thread.currentThread()){
                    liveData.setValue(entity);
                }else {
                    liveData.postValue(entity);
                }
            }
        });
        return liveData;
    }
}
