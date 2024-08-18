package com.skilrock.retailapp.viewmodels.rms;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.skilrock.retailapp.models.BetDeviceResponseBean;
import com.skilrock.retailapp.models.rms.ConfigurationResponseBean;
import com.skilrock.retailapp.models.rms.HomeDataBean;
import com.skilrock.retailapp.models.rms.LoginBean;
import com.skilrock.retailapp.network.APIClient;
import com.skilrock.retailapp.network.APIConfig;
import com.skilrock.retailapp.utils.AppConstants;
import com.skilrock.retailapp.utils.SharedPrefUtil;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {

    private APIClient apiClient = APIConfig.getInstance().create(APIClient.class);

    private MutableLiveData<ConfigurationResponseBean> liveDataConfig = new MutableLiveData<>();
    private MutableLiveData<LoginBean> liveDataBalance = new MutableLiveData<>();

    public MutableLiveData<ConfigurationResponseBean> getLiveDataConfig() {
        return liveDataConfig;
    }

    private MutableLiveData<HomeDataBean> liveHomeData = new MutableLiveData<>();
    private MutableLiveData<BetDeviceResponseBean> liveDataBetGame = new MutableLiveData<>();


    public MutableLiveData<BetDeviceResponseBean> getLiveDataBetGame() {
        return liveDataBetGame;
    }

    public MutableLiveData<HomeDataBean> getLiveHomeData() {
        return liveHomeData;
    }

    public MutableLiveData<LoginBean> getLiveDataBalance() {
        return liveDataBalance;
    }

    public void getHomeModuleList(String token, String userId, Context context) {

        Call<HomeDataBean> homeModulesBeanCall = apiClient.getMainModules(token, userId, AppConstants.appType, AppConstants.engineCode,
                SharedPrefUtil.getLanguage(context));

        Log.d("log", "HomeModule Request: " + homeModulesBeanCall.request().toString());

        homeModulesBeanCall.enqueue(new Callback<HomeDataBean>() {
            @Override
            public void onResponse(@NonNull Call<HomeDataBean> call, @NonNull Response<HomeDataBean> response) {
                if (response.body() == null || !response.isSuccessful()) {
                    if (response.errorBody() != null) {
                        try {
                            String errorResponse = response.errorBody().string();
                            Log.e("log", "HomeModule API Failed: " + errorResponse);
                            Gson gson = new GsonBuilder().create();
                            HomeDataBean model;
                            model = gson.fromJson(errorResponse, HomeDataBean.class);
                            liveHomeData.postValue(model);
                        } catch (IOException e) {
                            Log.e("log", "HomeModule API Failed: " + e.getMessage());
                            liveHomeData.postValue(null);
                        }
                        return;
                    }
                    liveHomeData.postValue(null);
                    return;
                }

                Log.i("log", "HomeModule Response: " + new Gson().toJson(response.body()));
                liveHomeData.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<HomeDataBean> call, @NonNull Throwable throwable) {
                Log.e("log", "HomeModule Failed: " + throwable.getMessage());
                liveHomeData.postValue(null);
            }
        });
    }

    public void getConfigApi(String url, String authToken, long domain) {
        Call<ConfigurationResponseBean> loginCall = apiClient.getConfig(url, authToken, domain);

        Log.d("log", "Config Request: " + loginCall.request().toString());

        loginCall.enqueue(new Callback<ConfigurationResponseBean>() {
            @Override
            public void onResponse(@NonNull Call<ConfigurationResponseBean> call, @NonNull Response<ConfigurationResponseBean> response) {
                if (response.body() == null || !response.isSuccessful()) {
                    if (response.errorBody() != null) {
                        try {
                            String errorResponse = response.errorBody().string();
                            Log.e("log", "Config API Failed: " + errorResponse);
                            Gson gson = new GsonBuilder().create();
                            ConfigurationResponseBean model;
                            model = gson.fromJson(errorResponse, ConfigurationResponseBean.class);
                            liveDataConfig.postValue(model);
                        } catch (Exception e) {
                            liveDataConfig.postValue(null);
                            e.printStackTrace();
                        }
                        return;
                    }
                    liveDataConfig.postValue(null);
                    return;
                }

                Log.i("log", "Config API Response: " + new Gson().toJson(response.body()));
                liveDataConfig.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ConfigurationResponseBean> call, @NonNull Throwable throwable) {
                Log.e("log", "Config API failed: " + throwable.toString());
                liveDataConfig.postValue(null);
            }
        });
    }

    public void getUpdatedBalance(final String authToken) {
        Call<LoginBean> loginCall = apiClient.getLogin(authToken);

        Log.d("log", "Login Request: " + loginCall.request().toString());

        loginCall.enqueue(new Callback<LoginBean>() {
            @Override
            public void onResponse(@NonNull Call<LoginBean> call, @NonNull Response<LoginBean> response) {
                if (response.body() == null || !response.isSuccessful()) {
                    if (response.errorBody() != null) {
                        try {
                            String errorResponse = response.errorBody().string();
                            Log.e("log", "Login API Failed: " + errorResponse);
                            Gson gson = new GsonBuilder().create();
                            LoginBean model;
                            model = gson.fromJson(errorResponse, LoginBean.class);
                            liveDataBalance.postValue(model);
                        } catch (JsonSyntaxException e) {
                            liveDataBalance.postValue(null);
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                    liveDataBalance.postValue(null);
                    return;
                }

                Log.i("log", "Login API Response: " + new Gson().toJson(response.body()));
                if (response.body().getResponseCode() == 0 && response.body().getResponseData().getStatusCode() == 0)
                    response.body().setToken(authToken);
                liveDataBalance.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<LoginBean> call, @NonNull Throwable throwable) {
                Log.e("log", "Login API failed: " + throwable.toString());
                liveDataBalance.postValue(null);
            }
        });
    }

}