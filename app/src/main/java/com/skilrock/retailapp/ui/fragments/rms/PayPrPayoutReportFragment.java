package com.skilrock.retailapp.ui.fragments.rms;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.skilrock.retailapp.R;
import com.skilrock.retailapp.adapter.rms.PayPrReportAdapter;
import com.skilrock.retailapp.models.rms.HomeDataBean;
import com.skilrock.retailapp.models.rms.PayPrPayoutReportRequestBean;
import com.skilrock.retailapp.models.rms.PayPrPayoutReportResponseBean;
import com.skilrock.retailapp.ui.fragments.BaseFragment;
import com.skilrock.retailapp.utils.PlayerData;
import com.skilrock.retailapp.utils.ProgressBarDialog;
import com.skilrock.retailapp.utils.Utils;
import com.skilrock.retailapp.viewmodels.rms.PayPrReportViewModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class PayPrPayoutReportFragment extends BaseFragment implements View.OnClickListener {


    private PayPrReportViewModel viewModel;
    private TextView tvStartDate, tvEndDate;
    private RecyclerView rvReport;
    private LinearLayout llNetCollection;
    private TextView tvDeposit, tvWithdraw, tvNetCollection;
    private HomeDataBean.ResponseData.ModuleBeanList.MenuBeanList menuBean;
    private PayPrReportAdapter adapter;
    private final String RMS = "rms";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_paypr_payout_report, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() != null) {
            //initialise view model
            viewModel = ViewModelProviders.of(this).get(PayPrReportViewModel.class);
            viewModel.getLiveData().observe(this, observer);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeWidgets(view);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initializeWidgets(View view) {
        tvStartDate             = view.findViewById(R.id.tvStartDate);
        tvEndDate               = view.findViewById(R.id.tvEndDate);
        rvReport                = view.findViewById(R.id.rv_report);
        llNetCollection         = view.findViewById(R.id.llNetCollection);
        tvDeposit               = view.findViewById(R.id.tvDeposit);
        tvWithdraw              = view.findViewById(R.id.tvWithdraw);
        tvNetCollection         = view.findViewById(R.id.tvNetCollection);
        ImageView ivProceed     = view.findViewById(R.id.button_proceed);
        LinearLayout llFromDate = view.findViewById(R.id.containerFromDate);
        LinearLayout llEndDate  = view.findViewById(R.id.containerEndDate);
        TextView tvTitle        = view.findViewById(R.id.tvTitle);
        tvUsername              = view.findViewById(R.id.tvUserName);
        tvUserBalance           = view.findViewById(R.id.tvUserBalance);
        refreshBalance();

        llFromDate.setOnClickListener(this);
        llEndDate.setOnClickListener(this);
        ivProceed.setOnClickListener(this);

        Bundle bundle = getArguments();
        FragmentActivity activity = getActivity();
        if (bundle != null) {
            if (activity != null) {
                activity.setTitle(bundle.getString("title"));
                tvTitle.setText(bundle.getString("title"));
            }
            menuBean = bundle.getParcelable("MenuBean");
        }

        tvStartDate.setText(Utils.getPreviousDate(1));
        tvEndDate.setText(Utils.getCurrentDate());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.containerFromDate:
                Utils.openStartDateDialog(master, tvStartDate, tvEndDate);
                break;
            case R.id.containerEndDate:
                if (tvStartDate.getText().toString().equalsIgnoreCase(getString(R.string.start_date)))
                    Toast.makeText(master, getString(R.string.select_start_date), Toast.LENGTH_SHORT).show();
                else
                    Utils.openEndDateDialog(master, tvStartDate, tvEndDate);
                break;
            case R.id.button_proceed:
                if (!Utils.isNetworkConnected(master)) {
                    Toast.makeText(master, getString(R.string.check_internet_connection), Toast.LENGTH_LONG).show();
                    return;
                }
                if (validate()) {
                    if (adapter != null) adapter.clear();
                    ProgressBarDialog.getProgressDialog().showProgress(master);
                    PayPrPayoutReportRequestBean requestBean  =  new PayPrPayoutReportRequestBean();
                    requestBean.setFromDate(formatDate(tvStartDate));
                    requestBean.setToDate(formatDate(tvEndDate));
                    Integer domain = (int) PlayerData.getInstance().getLoginData().getResponseData().getData().getDomainId();
                    requestBean.setDomainId(domain);
                    requestBean.setOrgId(String.valueOf(PlayerData.getInstance().getLoginData().getResponseData().getData().getOrgId()));
                    requestBean.setOrgTypeCode("RET");
                    requestBean.setTxnTypeCode("PAYOUT");

                    viewModel.payoutReportApi(menuBean.getBasePath() + menuBean.getRelativePath(), requestBean,PlayerData.getInstance().getToken());
                }
                break;
        }
    }

    private boolean validate() {
        Utils.vibrate(Objects.requireNonNull(getContext()));
        if (tvStartDate.getText().toString().equalsIgnoreCase(getString(R.string.start_date))) {
            Toast.makeText(master, getString(R.string.select_start_date), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (tvEndDate.getText().toString().equalsIgnoreCase(getString(R.string.end_date))) {
            Toast.makeText(master, getString(R.string.select_end_date), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private String formatDate(TextView tvDate) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat originalFormat = new SimpleDateFormat("dd-mm-yyyy", Locale.ENGLISH);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
        Date date = null;
        try {
            date = originalFormat.parse(tvDate.getText().toString().trim());
            Log.d("log", "Old Format: " + originalFormat.format(date));
            Log.d("log", "New Format: " + targetFormat.format(date));

        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return targetFormat.format(date);
    }


    Observer<PayPrPayoutReportResponseBean> observer = response -> {
        ProgressBarDialog.getProgressDialog().dismiss();
        if (response == null)
            Utils.showCustomErrorDialog(getContext(), getString(R.string.payout), getString(R.string.something_went_wrong));
        else if (response.getResponseCode() != 0) {
            String errorMsg = Utils.getResponseMessage(master, RMS, response.getResponseCode());
            Utils.showCustomErrorDialog(getContext(), getString(R.string.payout), errorMsg);
        } else if (response.getResponseData().getStatusCode() != 0) {
            if (Utils.checkForSessionExpiry(master, response.getResponseData().getStatusCode()))
                return;
            String errorMsg = Utils.getResponseMessage(master, RMS, response.getResponseData().getStatusCode());
            Utils.showCustomErrorDialog(getContext(), getString(R.string.payout), errorMsg);
        } else if (response.getResponseData().getStatusCode() == 0) {
            adapter = new PayPrReportAdapter(master, response.getResponseData().getData());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            rvReport.setLayoutManager(mLayoutManager);
            rvReport.setItemAnimator(new DefaultItemAnimator());
            rvReport.setAdapter(adapter);
        } else
            Utils.showCustomErrorDialog(getContext(), getString(R.string.deposit_withdrawal), getString(R.string.something_went_wrong));
    };
}