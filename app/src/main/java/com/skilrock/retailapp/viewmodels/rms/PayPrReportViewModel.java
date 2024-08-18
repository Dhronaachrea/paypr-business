package com.skilrock.retailapp.viewmodels.rms;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.skilrock.retailapp.models.rms.PayPrPayoutReportRequestBean;
import com.skilrock.retailapp.models.rms.PayPrPayoutReportResponseBean;
import com.skilrock.retailapp.network.APIClient;
import com.skilrock.retailapp.network.APIConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayPrReportViewModel extends ViewModel {
    private APIClient apiClient = APIConfig.getInstance().create(APIClient.class);

    private MutableLiveData<PayPrPayoutReportResponseBean> liveData = new MutableLiveData<>();

    public MutableLiveData<PayPrPayoutReportResponseBean> getLiveData(){
        return liveData;
    }

    public void payoutReportApi(String url, PayPrPayoutReportRequestBean requestBean, String  token) {
        final Call<PayPrPayoutReportResponseBean> payoutReportCall = apiClient.getPayoutReport(url, token, requestBean);
        Log.i("log", "PayoutReport API Request: " + payoutReportCall.request().toString());
        payoutReportCall.enqueue(new Callback<PayPrPayoutReportResponseBean>() {
            @Override
            public void onResponse(Call<PayPrPayoutReportResponseBean> call, Response<PayPrPayoutReportResponseBean> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    if (response.errorBody() != null) {
                        try {
                            String errorResponse = response.errorBody().string();
                            Log.e("log", "PayoutReport API Failed : " + errorResponse);
                            Gson gson = new GsonBuilder().create();
                            PayPrPayoutReportResponseBean model;
                            model = gson.fromJson(errorResponse, PayPrPayoutReportResponseBean.class);
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
                Log.i("log", "PayoutReport API Response : " + new Gson().toJson(response.body()));
                liveData.postValue(response.body());

            }
            @Override
            public void onFailure(Call<PayPrPayoutReportResponseBean> call, Throwable t) {
                Log.e("log", "PayoutReport API Failed : " + t.toString());
                liveData.postValue(null);

            }
        });

    }

}
