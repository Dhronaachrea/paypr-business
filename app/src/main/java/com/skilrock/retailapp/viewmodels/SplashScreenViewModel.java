package com.skilrock.retailapp.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.skilrock.retailapp.BuildConfig;
import com.skilrock.retailapp.models.VersionBean;
import com.skilrock.retailapp.network.APIClient;
import com.skilrock.retailapp.network.APIConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenViewModel extends ViewModel {

    private APIClient client = APIConfig.getInstance().create(APIClient.class);

    private MutableLiveData<VersionBean> liveData = new MutableLiveData<>();

    public MutableLiveData<VersionBean> getLiveData() {
        return liveData;
    }

    public void getAppVersion() {
        Call<VersionBean> versionCall = client.getVersion(BuildConfig.app_type);

        Log.d("log", "Version Request: " + versionCall.request().toString());

        versionCall.enqueue(new Callback<VersionBean>() {
            @Override
            public void onResponse(@NonNull Call<VersionBean> call, @NonNull Response<VersionBean> response) {
                if (response.body() == null || !response.isSuccessful()) {
                    if (response.errorBody() != null) {
                        try {
                            String errorResponse = response.errorBody().string();
                            Log.e("log", "Version API Failed: " + errorResponse);
                            Gson gson = new GsonBuilder().create();
                            VersionBean model;
                            model= gson.fromJson(errorResponse, VersionBean.class);
                            liveData.postValue(model);
                        } catch (JsonSyntaxException e) {
                            liveData.postValue(null);
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                    liveData.postValue(null);
                    return;
                }

                Log.i("log", "Version API Response: " + new Gson().toJson(response.body()));
                /*response.body().getResponseData().getData().setAppVersion("1.0.1");
                response.body().getResponseData().getData().setAppRemark("Download Latest Version: 1.0.0");
                response.body().getResponseData().getData().setDownloadMode("OPTIONAL");*/
                liveData.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<VersionBean> call, @NonNull Throwable throwable) {
                Log.e("log", "Version API failed: " + throwable.toString());
                liveData.postValue(null);
            }
        });

    }

}
