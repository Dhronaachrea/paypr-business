package com.skilrock.retailapp.ui.fragments.rms;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.skilrock.retailapp.adapter.rms.BillReportAdapter;
import com.skilrock.retailapp.dialog.BillPaymentsDialog;
import com.skilrock.retailapp.models.rms.BillPaymentsResponseBean;
import com.skilrock.retailapp.models.rms.BillReportRequestBean;
import com.skilrock.retailapp.models.rms.BillReportResponseBean;
import com.skilrock.retailapp.models.rms.HomeDataBean;
import com.skilrock.retailapp.ui.fragments.BaseFragment;
import com.skilrock.retailapp.utils.PlayerData;
import com.skilrock.retailapp.utils.ProgressBarDialog;
import com.skilrock.retailapp.utils.Utils;
import com.skilrock.retailapp.viewmodels.rms.BillReportViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class BillReportFragment extends BaseFragment implements View.OnClickListener {

    private BillReportViewModel viewModel;
    private TextView tvStartDate, tvEndDate;
    private RecyclerView rvReport;
    private HomeDataBean.ResponseData.ModuleBeanList.MenuBeanList menuBean;
    private BillReportAdapter adapter;
    private final String RMS = "rms";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report_ola, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() != null) {
            //initialise view model
            viewModel = ViewModelProviders.of(this).get(BillReportViewModel.class);
            viewModel.getLiveData().observe(this, observer);
            viewModel.getLiveDataBillPayments().observe(this, observerBillPayments);
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

        tvStartDate.setText(Utils.getPreviousDate(30));
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
                    Toast.makeText( master, getString(R.string.check_internet_connection), Toast.LENGTH_LONG).show();
                    return;
                }
                if (validate()) {
                    if (adapter != null) adapter.clear();

                    ProgressBarDialog.getProgressDialog().showProgress(master);

                    BillReportRequestBean model = new BillReportRequestBean();
                    model.setOrgId(PlayerData.getInstance().getOrgId());
                    model.setOrgTypeCode(PlayerData.getInstance().getLoginData().getResponseData().getData().getOrgTypeCode());
                    model.setDomainId(PlayerData.getInstance().getLoginData().getResponseData().getData().getDomainId());
                    model.setStartDate(formatDate(tvStartDate));
                    model.setEndDate(formatDate(tvEndDate));

                    viewModel.callBillReportApi(PlayerData.getInstance().getToken(), Utils.getRmsUrlDetails(menuBean, getContext(), "billList"), model);
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

        /*try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat format = new SimpleDateFormat("dd-mm-yyyy"); //"yyyy-mm-dd"

            Date date1 = format.parse(tvStartDate.getText().toString().trim());
            Date date2 = format.parse(tvEndDate.getText().toString().trim());

            if (date2.compareTo(date1) < 0) {
                Toast.makeText(master, getString(R.string.from_date_greater_than_to_date), Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }*/

        return true;
    }

    Observer<BillReportResponseBean> observer = response -> {
        ProgressBarDialog.getProgressDialog().dismiss();

        if (response == null)
            Utils.showCustomErrorDialog(getContext(), getString(R.string.bill_report), getString(R.string.something_went_wrong));
        else if (response.getResponseCode() != 0) {
            //String errorMsg = TextUtils.isEmpty(response.getResponseMessage())?getString(R.string.some_internal_error):response.getResponseMessage();
            String errorMsg = Utils.getResponseMessage(master, RMS, response.getResponseCode());
            Utils.showCustomErrorDialog(getContext(), getString(R.string.bill_report), errorMsg);
        }
        else if(response.getResponseData().getStatusCode() != 0) {
            if (Utils.checkForSessionExpiry(master, response.getResponseData().getStatusCode()))
                return;
            //String errorMsg = TextUtils.isEmpty(response.getResponseData().getMessage())?getString(R.string.some_internal_error):response.getResponseData().getMessage();
            String errorMsg = Utils.getResponseMessage(master, RMS, response.getResponseData().getStatusCode());
            Utils.showCustomErrorDialog(getContext(), getString(R.string.bill_report), errorMsg);
        }
        else if (response.getResponseData().getStatusCode() == 0) {
            ArrayList<BillReportResponseBean.ResponseData.Data.OrgBillDetail.BillData> billData = response.getResponseData().getData().getOrgBillDetails().get(0).getBillData();
            if (billData == null)
                Utils.showCustomErrorDialog(getContext(), getString(R.string.bill_report), getString(R.string.no_data_found));
            else if (billData.size() < 1)
                Utils.showCustomErrorDialog(getContext(), getString(R.string.bill_report), getString(R.string.no_data_found));
            else {
                //BillSelectionListener listener = this::callBillPaymentsApi;

                adapter = new BillReportAdapter(billData, getContext());
                rvReport.setLayoutManager(new LinearLayoutManager(getContext()));
                rvReport.setItemAnimator(new DefaultItemAnimator());
                rvReport.setAdapter(adapter);
            }
        }
        else
            Utils.showCustomErrorDialog(getContext(), getString(R.string.bill_report), getString(R.string.something_went_wrong));
    };

    private String formatDate(TextView tvDate)  {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat originalFormat = new SimpleDateFormat("dd-mm-yyyy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-mm-dd" );
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

    private void callBillPaymentsApi(long billId) {
        if (!Utils.isNetworkConnected(master)) {
            Toast.makeText( master, getString(R.string.check_internet_connection), Toast.LENGTH_LONG).show();
            return;
        }
        ProgressBarDialog.getProgressDialog().showProgress(master);
        viewModel.callBillPaymentApi(Utils.getRmsUrlDetails(menuBean, getContext(), "billPayments"),
                PlayerData.getInstance().getOrgId(), PlayerData.getInstance().getLoginData().getResponseData().getData().getDomainId(), billId);
    }

    Observer<BillPaymentsResponseBean> observerBillPayments = response -> {
        ProgressBarDialog.getProgressDialog().dismiss();

        if (response == null)
            Utils.showCustomErrorDialog(getContext(), getString(R.string.bill_report), getString(R.string.something_went_wrong));
        else if (response.getResponseCode() != 0) {
            //String errorMsg = TextUtils.isEmpty(response.getResponseMessage())?getString(R.string.some_internal_error):response.getResponseMessage();
            String errorMsg = Utils.getResponseMessage(master, RMS, response.getResponseCode());
            Utils.showCustomErrorDialog(getContext(), getString(R.string.bill_report), errorMsg);
        }
        else if(response.getResponseData().getStatusCode() != 0) {
            if (Utils.checkForSessionExpiry(master, response.getResponseData().getStatusCode()))
                return;
            //String errorMsg = TextUtils.isEmpty(response.getResponseData().getMessage())?getString(R.string.some_internal_error):response.getResponseData().getMessage();
            String errorMsg = Utils.getResponseMessage(master, RMS, response.getResponseData().getStatusCode());
            Utils.showCustomErrorDialog(getContext(), getString(R.string.bill_report), errorMsg);
        }
        else if (response.getResponseData().getStatusCode() == 0) {
            ArrayList<BillPaymentsResponseBean.ResponseData.Data> paymentData = response.getResponseData().getData();
            if (paymentData == null)
                Utils.showCustomErrorDialog(getContext(), getString(R.string.bill_report), getString(R.string.no_data_found));
            else if (paymentData.size() < 1)
                Utils.showCustomErrorDialog(getContext(), getString(R.string.bill_report), getString(R.string.no_data_found));
            else {
                BillPaymentsDialog dialog = new BillPaymentsDialog(getContext(), paymentData);
                dialog.show();
                dialog.show();
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                }
            }
        }
        else
            Utils.showCustomErrorDialog(getContext(), getString(R.string.bill_report), getString(R.string.something_went_wrong));
    };
}