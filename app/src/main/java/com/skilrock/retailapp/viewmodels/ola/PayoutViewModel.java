package com.skilrock.retailapp.viewmodels.ola;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.skilrock.retailapp.models.ola.OlaDepositResponseBean;
import com.skilrock.retailapp.models.ola.PayprBankDetailResponseBean;
import com.skilrock.retailapp.models.ola.PayprOlaPayoutRequestBean;
import com.skilrock.retailapp.models.ola.PayprPayoutResponseBean;
import com.skilrock.retailapp.models.ola.UrlOrgBean;
import com.skilrock.retailapp.models.rms.HomeDataBean;
import com.skilrock.retailapp.models.rms.LoginBean;
import com.skilrock.retailapp.network.APIClient;
import com.skilrock.retailapp.network.APIConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayoutViewModel extends ViewModel {

    private APIClient apiClient = APIConfig.getInstance().create(APIClient.class);

    private MutableLiveData<PayprBankDetailResponseBean> liveDataBankDetail = new MutableLiveData<>();

    public MutableLiveData<PayprBankDetailResponseBean> getLiveDataBankDetail() {
        return liveDataBankDetail;
    }

    private MutableLiveData<PayprPayoutResponseBean> liveDataPayout = new MutableLiveData<>();

    public MutableLiveData<PayprPayoutResponseBean> getLiveDataPayout() {
        return liveDataPayout;
    }

    public MutableLiveData<LoginBean> getLiveDataBalance() {
        return liveDataBalance;
    }


    private MutableLiveData<LoginBean> liveDataBalance = new MutableLiveData<>();

    public void callBankDetail(String token, UrlOrgBean bean, long orgId) {
        final Call<PayprBankDetailResponseBean> bankDetailCall = apiClient.getBankDetailPaypr(bean.getUrl(), token, bean.getContentType(), bean.getAccept(), bean.getBalance(), orgId);
        Log.i("log", "BankDetail API Request: " + bankDetailCall.request().toString());
        bankDetailCall.enqueue(new Callback<PayprBankDetailResponseBean>() {
            @Override
            public void onResponse(Call<PayprBankDetailResponseBean> call, Response<PayprBankDetailResponseBean> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    if (response.errorBody() != null) {
                        try {
                            String errorResponse = response.errorBody().string();
                            Log.e("log", "BankDetail API Failed : " + errorResponse);
                            Gson gson = new GsonBuilder().create();
                            PayprBankDetailResponseBean model;
                            model = gson.fromJson(errorResponse, PayprBankDetailResponseBean.class);
                            liveDataBankDetail.postValue(model);
                        } catch (JsonSyntaxException e) {
                            liveDataBankDetail.postValue(null);
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                    liveDataBankDetail.postValue(null);
                    return;
                }
                Log.i("log", "BankDetail API Response : " + new Gson().toJson(response.body()));
                liveDataBankDetail.postValue(response.body());
            }

            @Override
            public void onFailure(Call<PayprBankDetailResponseBean> call, Throwable throwable) {
                Log.e("log", "BankDetail API failed: " + throwable.toString());
                liveDataBankDetail.postValue(null);
            }
        });


    }

    public void callPayoutApi(PayprOlaPayoutRequestBean requestBean, HomeDataBean.ResponseData.ModuleBeanList.MenuBeanList menuBean, String token) {
        final Call<PayprPayoutResponseBean> payoutCall = apiClient.doPayoutPaypr(menuBean.getBasePath() + "" + menuBean.getRelativePath(), token, requestBean);
        Log.i("log", "Payout API Request: " + payoutCall.request().toString());
        payoutCall.enqueue(new Callback<PayprPayoutResponseBean>() {
            @Override
            public void onResponse(Call<PayprPayoutResponseBean> call, Response<PayprPayoutResponseBean> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    if (response.errorBody() != null) {
                        try {
                            String errorResponse = response.errorBody().string();
                            Log.e("log", "Payout API Failed : " + errorResponse);
                            Gson gson = new GsonBuilder().create();
                            PayprPayoutResponseBean model;
                            model = gson.fromJson(errorResponse, PayprPayoutResponseBean.class);
                            liveDataPayout.postValue(model);
                        } catch (JsonSyntaxException e) {
                            liveDataPayout.postValue(null);
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                    liveDataPayout.postValue(null);
                    return;
                }
                Log.i("log", "Payout API Response : " + new Gson().toJson(response.body()));
                liveDataPayout.postValue(response.body());
            }

            @Override
            public void onFailure(Call<PayprPayoutResponseBean> call, Throwable t) {
                Log.e("log", "Payout API Failed : " + t.toString());
                liveDataPayout.postValue(null);
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
