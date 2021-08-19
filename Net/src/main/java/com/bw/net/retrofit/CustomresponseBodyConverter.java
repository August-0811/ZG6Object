package com.bw.net.retrofit;

import android.util.Log;

import com.bw.net.protocol.BaseRespEntity;
import com.bw.net.protocol.TokenRespEntity;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Description :
 *
 * @author wangjiaxing
 * @date 2021/8/18
 */
public class CustomresponseBodyConverter<T> implements Converter<ResponseBody, T> {

    @Override
    public T convert(ResponseBody value) throws IOException {
        String string = value.string();
        Log.i("123", "CustomresponseBodyConverter string is "+string);
        if (string.contains("access_")){
            return (T) new Gson().fromJson(string, TokenRespEntity.class);
        }
        return (T) new Gson().fromJson(string, BaseRespEntity.class);
    }
}
