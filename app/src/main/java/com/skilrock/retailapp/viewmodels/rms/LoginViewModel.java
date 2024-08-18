package com.skilrock.retailapp.viewmodels.rms;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.skilrock.retailapp.models.rms.LoginBean;
import com.skilrock.retailapp.models.rms.TokenBean;
import com.skilrock.retailapp.models.rms.VerifyPosResponseBean;
import com.skilrock.retailapp.network.APIClient;
import com.skilrock.retailapp.network.APIConfig;
import com.skilrock.retailapp.utils.AppConstants;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    private APIClient client = APIConfig.getInstance().create(APIClient.class);

    private MutableLiveData<TokenBean> liveDataToken = new MutableLiveData<>();
    private MutableLiveData<LoginBean> liveDataLogin = new MutableLiveData<>();
    private MutableLiveData<VerifyPosResponseBean> liveDataVerifyPos = new MutableLiveData<>();

    public MutableLiveData<VerifyPosResponseBean> getLiveDataVerifyPos() {
        return liveDataVerifyPos;
    }

    public MutableLiveData<TokenBean> getLiveDataToken() {
        return liveDataToken;
    }

    public MutableLiveData<LoginBean> getLiveDataLogin() {
        return liveDataLogin;
    }

    public void callRetailerApi(final String username, final String password) {

        final Call<TokenBean> tokenCall = client.getToken(username, password, AppConstants.RMS, AppConstants.CLIENT_SCERET, "NA", "NA");
        Log.d("log", "Token Request: " + tokenCall.request().toString());

        tokenCall.enqueue(new Callback<TokenBean>() {
            @Override
            public void onResponse(@NonNull Call<TokenBean> call, @NonNull Response<TokenBean> response) {
                if (response.body() == null || !response.isSuccessful()) {
                    if (response.errorBody() != null) {
                        try {
                            String errorResponse = response.errorBody().string();
                            Log.e("log", "Token API Failed: " + errorResponse);
                            Gson gson = new GsonBuilder().create();
                            TokenBean model;
                            model = gson.fromJson(errorResponse, TokenBean.class);
                            liveDataToken.postValue(model);
                        } catch (IOException e) {
                            liveDataToken.postValue(null);
                            e.printStackTrace();
                        }
                        return;
                    }
                    liveDataToken.postValue(null);
                    return;
                }

                Log.i("log", "Token API Response: " + new Gson().toJson(response.body()));
                liveDataToken.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<TokenBean> call, @NonNull Throwable throwable) {
                Log.e("log", "Token API failed: " + throwable.toString());
                liveDataToken.postValue(null);
            }
        });
    }

    public void callTokenApi(final String username, final String password, String terminalId, String modelCode) {

        final Call<TokenBean> tokenCall = client.getToken(username, password, AppConstants.RMS, AppConstants.CLIENT_SCERET, terminalId, modelCode);
        Log.d("log", "Token Request Header: " + tokenCall.request().headers().toString());
        Log.d("log", "Token Request: " + tokenCall.request().toString());

        tokenCall.enqueue(new Callback<TokenBean>() {
            @Override
            public void onResponse(@NonNull Call<TokenBean> call, @NonNull Response<TokenBean> response) {
                if (response.body() == null || !response.isSuccessful()) {
                    if (response.errorBody() != null) {
                        try {
                            String errorResponse = response.errorBody().string();
                            Log.e("log", "Token API Failed: " + errorResponse);
                            Gson gson = new GsonBuilder().create();
                            TokenBean model;
                            model = gson.fromJson(errorResponse, TokenBean.class);
                            liveDataToken.postValue(model);
                        } catch (IOException e) {
                            liveDataToken.postValue(null);
                            e.printStackTrace();
                        }
                        return;
                    }
                    liveDataToken.postValue(null);
                    return;
                }

                Log.i("log", "Token API Response: " + new Gson().toJson(response.body()));
                liveDataToken.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<TokenBean> call, @NonNull Throwable throwable) {
                Log.e("log", "Token API failed: " + throwable.toString());
                liveDataToken.postValue(null);
            }
        });
    }

    public void callLoginApi(final String authToken) {
        Call<LoginBean> loginCall = client.getLogin(authToken);
        Log.d("log", "Login Header: " + loginCall.request().headers().toString());
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
                            liveDataLogin.postValue(model);
                        } catch (IOException e) {
                            liveDataLogin.postValue(null);
                            e.printStackTrace();
                        }
                        return;
                    }
                    liveDataLogin.postValue(null);
                    return;
                }

                Log.i("log", "Login API Response: " + new Gson().toJson(response.body()));
                if (response.body().getResponseCode() == 0 && response.body().getResponseData().getStatusCode() == 0) {
                    response.body().setToken(authToken);
                }
                liveDataLogin.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<LoginBean> call, @NonNull Throwable throwable) {
                Log.e("log", "Login API failed: " + throwable.toString());
                liveDataLogin.postValue(null);
            }
        });
    }

//    public void callVerifyPosApi(String token, VerifyPosRequestBean model) {
//
//        Call<VerifyPosResponseBean> verifyPosResponseBeanCall = client.postVerifyPos(token, model);
//
//        Log.d("log", "VerifyPos Request: " + verifyPosResponseBeanCall.request().toString());
//
//        verifyPosResponseBeanCall.enqueue(new Callback<VerifyPosResponseBean>() {
//            @Override
//            public void onResponse(@NonNull Call<VerifyPosResponseBean> call, @NonNull Response<VerifyPosResponseBean> response) {
//                if (response.body() == null || !response.isSuccessful()) {
//                    if (response.errorBody() != null) {
//                        try {
//                            String errorResponse = response.errorBody().string();
//                            Log.e("log", "VerifyPos API Failed: " + errorResponse);
//                            Gson gson = new GsonBuilder().create();
//                            VerifyPosResponseBean model;
//                            model= gson.fromJson(errorResponse, VerifyPosResponseBean.class);
//                            liveDataVerifyPos.postValue(model);
//                        } catch (IOException e) {
//                            Log.e("log", "VerifyPos API Failed: " + e.getMessage());
//                            liveDataVerifyPos.postValue(null);
//                        }
//                        return;
//                    }
//                    liveDataVerifyPos.postValue(null);
//                    return;
//                }
//
//                Log.i("log", "VerifyPos Response: " + new Gson().toJson(response.body()));
//                liveDataVerifyPos.postValue(response.body());
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<VerifyPosResponseBean> call, @NonNull Throwable throwable) {
//                Log.e("log", "VerifyPos Failed: " + throwable.getMessage());
//                liveDataVerifyPos.postValue(null);
//            }
//        });
//    }

    /*public void callConfigApi(final String authToken) {
        Call<LoginBean> loginCall = client.getLogin(authToken);

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
                            liveDataLogin.postValue(model);
                        } catch (IOException e) {
                            liveDataLogin.postValue(null);
                            e.printStackTrace();
                        }
                        return;
                    }
                    liveDataLogin.postValue(null);
                    return;
                }

                Log.i("log", "Login API Response: " + new Gson().toJson(response.body()));
                if (response.body().getResponseCode() == 0 && response.body().getResponseData().getStatusCode() == 0)
                    response.body().setToken(authToken);
                liveDataLogin.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<LoginBean> call, @NonNull Throwable throwable) {
                Log.e("log", "Login API failed: " + throwable.toString());
                liveDataLogin.postValue(null);
            }
        });
    }*/

}
