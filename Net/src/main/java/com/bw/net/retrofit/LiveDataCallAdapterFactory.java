package com.bw.net.retrofit;

import android.util.Log;

import androidx.lifecycle.LiveData;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.Completable;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

/**
 * Description :
 *
 * @author wangjiaxing
 * @date 2021/8/18
 */
public class LiveDataCallAdapterFactory  extends CallAdapter.Factory {
    public static LiveDataCallAdapterFactory create() {
        return new LiveDataCallAdapterFactory();
    }
    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            String typeName = returnType.getTypeName();
            Log.i("123", "LiveDataCallAdapterFactory get  typeName is "+typeName);
        }

        if (!(returnType instanceof ParameterizedType)) {
            throw new IllegalArgumentException("this returnType is don't can 序列化的, beacuse 要求返回值必须是可参数化的（支持泛型");
        }

        Class<?> rawType = CallAdapter.Factory.getRawType(returnType);
        if (rawType!= Call.class && rawType!= LiveData.class){
            throw new IllegalArgumentException("you result type is don't in  Call or LiveData(返回值类型必须是LiveData或者是Call)");
        }
        Type type = CallAdapter.Factory.getParameterUpperBound(0, (ParameterizedType) returnType);
        if (rawType==Call.class){
            return new DefaultCallAdapter<>(type);
        }else if (rawType==LiveData.class){
            return new LiveDataCallAdapter(type);
        }
        return new DefaultCallAdapter(type);
    }
}