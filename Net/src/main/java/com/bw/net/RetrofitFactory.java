package com.bw.net;

import android.text.TextUtils;
import android.util.Log;

import com.bw.net.api.TokenApi;
import com.bw.net.common.Config;
import com.bw.net.protocol.TokenRespEntity;
import com.bw.net.retrofit.CustomGsonConverterFactory;
import com.bw.net.retrofit.LiveDataCallAdapterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Description :
 *
 * @author wangjiaxing
 * @date 2021/8/18
 */
public class RetrofitFactory {
    private Retrofit retrofit;
    private volatile static RetrofitFactory instance=null;
    private RetrofitFactory() {
        retrofit=createRetrofit();
    }
    public static RetrofitFactory getInstance() {
        if (instance==null){
            synchronized (RetrofitFactory.class){
                if (instance==null){
                    instance=new RetrofitFactory();
                }
            }
        }
        return instance;
    }

    private String mToken="";
    private Retrofit createRetrofit() {
        Retrofit retrofit1=new Retrofit.Builder()
                .baseUrl(BuildConfig.BASEURL)
                .addConverterFactory(CustomGsonConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory.create())
                .client(createOkHttpClient())
                .build();
        return retrofit1;
    }

    private OkHttpClient createOkHttpClient() {
        return new OkHttpClient.Builder()
                .readTimeout(Config.TIME, TimeUnit.SECONDS)
                .writeTimeout(Config.TIME, TimeUnit.SECONDS)
                .connectTimeout(Config.TIME, TimeUnit.SECONDS)
                .addInterceptor(createTokenInterceptor())
                .addNetworkInterceptor(createNetInterceptor())
                .build();
    }

    private Interceptor createNetInterceptor() {
        HttpLoggingInterceptor ii=new HttpLoggingInterceptor();
        ii.setLevel(HttpLoggingInterceptor.Level.BODY);
        return ii;
    }

    /**
     *  Token处理的拦截器
     */
    private Interceptor createTokenInterceptor() {
        Interceptor interceptor=new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                String localtoken = mToken;
                //token不为空
                if (!TextUtils.isEmpty(localtoken)){
                   return resetRequest(request,localtoken,chain);
                }
                Response response = chain.proceed(request);

                //如果是401,说明token可能有问题,同步Token然后添加到新请求的Header里,发送新的业务请求
                if (chechHttpCode401(response)){
                    String stoken = requestToken();
                    if (TextUtils.isEmpty(stoken)){
                        return response;
                    }
                    mToken=stoken;
                    return resetRequest(request,stoken,chain);
                }
                return response;
            }
        };
        return interceptor;
    }
    /**
     * 获取Token的同步网络请求
     */
    private String requestToken() {
        TokenApi tokenApi = create(TokenApi.class);
        Call<TokenRespEntity> call = tokenApi.getToken("password", Config.AUTH_CODE, "");
        try {
            retrofit2.Response<TokenRespEntity> execute = call.execute();
            if (execute!=null&&execute.body()!=null){
                return execute.body().getAccess_token();
            }
        } catch (IOException e) {
            Log.e("123", "RetrofitFactory error info: "+e.getMessage());
        }
        return "";
    }
    /**
     * 通过api接口获取到其实例
     */
    public <T> T create(Class<?> tokenApiClass){
        return (T)retrofit.create(tokenApiClass);
    }
    /**
     * 判断HTTP CODE 是否401 —— TOKEN失效
     */
    private boolean chechHttpCode401(Response response) {
        if (response==null){
            return false;
        }
        if (response.code()==401)return true;else return false;
    }
    /**
     * 重置请求
     */
    private Response resetRequest(Request request, String localtoken, Interceptor.Chain chain) {
        Request.Builder newbui = request.newBuilder().addHeader("Authorization", "bearer " + localtoken);
        Request build = newbui.build();
        try {
            return chain.proceed(build);
        } catch (IOException e) {
            Log.e("123", "RetrofitFactory error info: "+e.getMessage());
        }
        return null;
    }
}