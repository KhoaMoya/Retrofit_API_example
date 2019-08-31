package com.khoa.retrofit_api_example.Network;

import com.khoa.retrofit_api_example.Model.AvatarImage;
import com.khoa.retrofit_api_example.Model.User;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @FormUrlEncoded
    @POST("/application_login/login.php")
    Call<User> login(@Field("username") String username,
                     @Field("password") String password);

    @FormUrlEncoded
    @POST("/application_login/register.php")
    Call<User> signup(
            @Field("username") String username
            , @Field("password") String password
            , @Field("email") String email);

    @FormUrlEncoded
    @POST("/application_login/avatar_image.php")
    Call<AvatarImage> getAvatarImage(
            @Field("token") String token,
            @Field("id") Integer id);

    @FormUrlEncoded
    @POST("/application_login/ping.php")
    Call<Integer> savePing(
            @Field("token") String token,
//            @Field("value_ping") Float value_ping,
////            @Field("address") String address);
            @Field("count") int count,
            @Field("pings") String jsonPings);

    @GET("/application_login/getLastTimePing.php")
    Call<String> getLastTimePing();
}
