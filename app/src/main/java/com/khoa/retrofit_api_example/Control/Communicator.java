package com.khoa.retrofit_api_example.Control;

import android.util.Log;

import com.khoa.retrofit_api_example.Event.EventFail;
import com.khoa.retrofit_api_example.Event.EventGetLastTimePingSuccess;
import com.khoa.retrofit_api_example.Event.EventLoadUrlAvatarSuccess;
import com.khoa.retrofit_api_example.Event.EventLoginSuccess;
import com.khoa.retrofit_api_example.Event.EventSavePingSuccess;
import com.khoa.retrofit_api_example.Event.EventSignupSuccess;
import com.khoa.retrofit_api_example.Model.AvatarImage;
import com.khoa.retrofit_api_example.Model.SimplePing;
import com.khoa.retrofit_api_example.Model.User;
import com.khoa.retrofit_api_example.Network.RetrofitClient;
import com.khoa.retrofit_api_example.Network.RetrofitInterface;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Communicator {

    private String SEVER_URL = "http://192.168.0.108";
    private RetrofitInterface retrofitInterface = null;

    public Communicator() {
        retrofitInterface = RetrofitClient.getClient(SEVER_URL).create(RetrofitInterface.class);
    }

    public void login(String username, String password) {
        Call<User> loginCall = retrofitInterface.login(username, password);
        loginCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                EventBus.getDefault().post(new EventLoginSuccess(response.body()));
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                EventBus.getDefault().post(new EventFail(t.getMessage()));
            }
        });
    }

    public void signup(String username, String password, String email){
        Call<User> signupCall = retrofitInterface.signup(username, password, email);
        signupCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                EventBus.getDefault().post(new EventSignupSuccess(response.body()));
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                EventBus.getDefault().post(new EventFail(t.getMessage()));
            }
        });
    }

    public void getAvatarImage(String token, int id){
        Call<AvatarImage> call = retrofitInterface.getAvatarImage(token, id);
        call.enqueue(new Callback<AvatarImage>() {
            @Override
            public void onResponse(Call<AvatarImage> call, Response<AvatarImage> response) {
                EventBus.getDefault().post(new EventLoadUrlAvatarSuccess(response.body()));
            }

            @Override
            public void onFailure(Call<AvatarImage> call, Throwable t) {
                EventBus.getDefault().post(new EventFail(t.getMessage()));
            }
        });
    }

    public void savePing(String token, int count, String pings){
        Call<Integer> call = retrofitInterface.savePing(token, count,pings);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Log.e("Loi", response.body()+"");
                EventBus.getDefault().post(new EventSavePingSuccess(response.body()));
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                EventBus.getDefault().post(new EventFail(t.getMessage()));
            }
        });
    }

    public void getLastTimePing(){
        Call<String> call = retrofitInterface.getLastTimePing();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                EventBus.getDefault().post(new EventGetLastTimePingSuccess(response.body()));
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                EventBus.getDefault().post(new EventFail(t.getMessage()));
            }
        });
    }

}
