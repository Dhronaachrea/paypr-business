package com.skilrock.retailapp.viewmodels.rms;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.skilrock.retailapp.models.rms.SearchUserRequestBean;
import com.skilrock.retailapp.models.rms.SearchUserResponseBean;
import com.skilrock.retailapp.network.APIClient;
import com.skilrock.retailapp.network.APIConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchUserViewModel extends ViewModel {

    private APIClient apiClient = APIConfig.getInstance().create(APIClient.class);

    private MutableLiveData<SearchUserResponseBean> liveData = new MutableLiveData<>();

    public MutableLiveData<SearchUserResponseBean> getLiveData(){
        return liveData;
    }

    public void searchUserApi(String url, String token, SearchUserRequestBean searchUserRequestBean) {
        Call<SearchUserResponseBean> reportCall = apiClient.searchUser(url, token, searchUserRequestBean);

        Log.d("log", "OLA Report Request: " + reportCall.request().toString());

        reportCall.enqueue(new Callback<SearchUserResponseBean>() {
            @Override
            public void onResponse(@NonNull Call<SearchUserResponseBean> call, @NonNull Response<SearchUserResponseBean> response) {
                if (response.body() == null || !response.isSuccessful()) {
                    if (response.errorBody() != null) {
                        try {
                            String errorResponse = response.errorBody().string();
                            Log.e("log", "search user Failed: " + errorResponse);
                            Gson gson = new GsonBuilder().create();
                            SearchUserResponseBean model;
                            model= gson.fromJson(errorResponse, SearchUserResponseBean.class);
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

                Log.i("log", "search user Response: " + new Gson().toJson(response.body()));
                liveData.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<SearchUserResponseBean> call, @NonNull Throwable throwable) {
                //Log.e("log", "OLA Report failed: " + throwable.toString());
                throwable.printStackTrace();
                liveData.postValue(null);
            }
        });
    }
}
