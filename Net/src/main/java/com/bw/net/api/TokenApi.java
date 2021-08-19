package com.bw.net.api;

import com.bw.net.protocol.TokenRespEntity;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Description :
 *
 * @author wangjiaxing
 * @date 2021/8/18
 */
public interface TokenApi {
    @FormUrlEncoded
    @POST("token")
    Call<TokenRespEntity> getToken(
            @Field("grant_type") String grant_type,
            @Field("username") String username,
            @Field("password") String password);
}
