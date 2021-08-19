package com.bw.net.retrofit;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;

/**
 * Description :
 *
 * @author wangjiaxing
 * @date 2021/8/18
 */
public class DefaultCallAdapter<T> implements CallAdapter<T,Object> {
    Type type=null;

    public DefaultCallAdapter(Type type) {
        this.type = type;
    }

    @Override
    public Type responseType() {
        return type;
    }

    @Override
    public Object adapt(Call<T> call) {
        return call;
    }
}
