package com.skilrock.retailapp.ui.fragments.rms;

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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.skilrock.retailapp.R;
import com.skilrock.retailapp.dialog.SuccessDialog;
import com.skilrock.retailapp.interfaces.ConfirmationListener;
import com.skilrock.retailapp.models.ola.PayprBankDetailResponseBean;
import com.skilrock.retailapp.models.ola.PayprOlaPayoutRequestBean;
import com.skilrock.retailapp.models.ola.PayprPayoutResponseBean;
import com.skilrock.retailapp.models.ola.UrlOrgBean;
import com.skilrock.retailapp.models.rms.HomeDataBean;
import com.skilrock.retailapp.models.rms.LoginBean;
import com.skilrock.retailapp.ui.fragments.BaseFragment;
import com.skilrock.retailapp.utils.PlayerData;
import com.skilrock.retailapp.utils.ProgressBarDialog;
import com.skilrock.retailapp.utils.SharedPrefUtil;
import com.skilrock.retailapp.utils.Utils;
import com.skilrock.retailapp.viewmodels.ola.PayoutViewModel;
import java.util.Objects;


public class PayoutFragment extends BaseFragment implements View.OnClickListener {

    private PayoutViewModel viewModel;
    private EditText etAmount, etAmountConfirmAmount, etBankName, etBranch, etBeneficiaryName, etAccountNumber, etRemark;
    private HomeDataBean.ResponseData.ModuleBeanList.MenuBeanList menuBean;
    private final String RMS = "rms";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_payout, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() != null) {
            viewModel = ViewModelProviders.of(this).get(PayoutViewModel.class);
            viewModel.getLiveDataBankDetail().observe(this, observer);
            viewModel.getLiveDataPayout().observe(this, observerPayout);
            viewModel.getLiveDataBalance().observe(this, observerBalance);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeWidgets(view);
        callBankDetailApi();
    }

    private void callBankDetailApi() {
        UrlOrgBean urlBean = Utils.getDoPayAmountDetailUrl(menuBean, getActivity(), "getOrgDetails");
        if (urlBean != null) {
            viewModel.callBankDetail(PlayerData.getInstance().getToken(), urlBean,
                    PlayerData.getInstance().getLoginData().getResponseData().getData().getOrgId()
            );
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initializeWidgets(View view) {
        etAmount                    = view.findViewById(R.id.et_amount);
        etAmountConfirmAmount       = view.findViewById(R.id.et_amount_confirm_amount);
        etBankName                  = view.findViewById(R.id.et_bank_name);
        etBranch                    = view.findViewById(R.id.et_branch);
        etBeneficiaryName           = view.findViewById(R.id.et_beneficiary_name);
        etAccountNumber             = view.findViewById(R.id.et_account_number);
        etRemark                    = view.findViewById(R.id.et_remark);
        Button proceed_button       = view.findViewById(R.id.button);
        TextView tvTitle            = view.findViewById(R.id.tvTitle);
        tvUsername                  = view.findViewById(R.id.tvUserName);
        tvUserBalance               = view.findViewById(R.id.tvUserBalance);
        RadioGroup rgBankAccount    = view.findViewById(R.id.radio_group_bank);
        refreshBalance();

        proceed_button.setOnClickListener(this);
        Bundle bundle = getArguments();
        FragmentActivity activity = getActivity();
        if (bundle != null) {
            if (activity != null) {
                activity.setTitle(bundle.getString("title"));
                tvTitle.setText(bundle.getString("title"));
            }
            menuBean = bundle.getParcelable("MenuBean");
        }
        rgBankAccount.check(R.id.rb_existing);
        rgBankAccount.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rb = group.findViewById(checkedId);
            if (null != rb && checkedId > -1) {
                if (checkedId == R.id.rb_existing) {
                    callBankDetailApi();
                } else {
                    fillNewData();
                }
            }

        });
    }

    private void fillNewData() {
        etBranch.setEnabled(true);
        etBankName.setEnabled(true);
        etBeneficiaryName.setEnabled(true);
        etAccountNumber.setEnabled(true);

        etBranch.getText().clear();
        etBankName.getText().clear();
        etBeneficiaryName.getText().clear();
        etAccountNumber.getText().clear();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
           if (validate()){
               ConfirmationListener listener = PayoutFragment.this::callPayoutApi;
               String msg = getString(R.string.are_you_sure_to_payout) + " " + Utils.getDefaultCurrency(SharedPrefUtil.getLanguage(Objects.requireNonNull(getActivity()))) + " <font color=#606060>" + getText(etAmount) + " </font>" + getString(R.string.for_player) + " <font color=#606060>" + " To bank account no: " +getText(etAccountNumber) + "</font>?";
               Utils.showConfirmationDialog(getContext(), false, msg, listener);
           }
        }
    }

    private boolean validate() {
        Utils.vibrate(Objects.requireNonNull(getContext()));
        if (TextUtils.isEmpty(getText(etAmount))) {
            etAmount.setError(getString(R.string.this_field_cannot_be_empty));
            etAmount.requestFocus();
            etAmount.startAnimation(Utils.shakeError());
            return false;
        }
        if (TextUtils.isEmpty(getText(etAmountConfirmAmount))) {
            etAmountConfirmAmount.setError(getString(R.string.this_field_cannot_be_empty));
            etAmountConfirmAmount.requestFocus();
            etAmountConfirmAmount.startAnimation(Utils.shakeError());
            return false;
        }
        if (Integer.parseInt(getText(etAmount)) != Integer.parseInt(getText(etAmountConfirmAmount))) {
            Utils.showRedToast(getActivity(), getString(R.string.amount_error));
            return false;
        }
        if (TextUtils.isEmpty(getText(etBankName))) {
            etBankName.setError(getString(R.string.this_field_cannot_be_empty));
            etBankName.requestFocus();
            etBankName.startAnimation(Utils.shakeError());
            return false;
        }
        if (TextUtils.isEmpty(getText(etBranch))) {
            etBranch.setError(getString(R.string.this_field_cannot_be_empty));
            etBranch.requestFocus();
            etBranch.startAnimation(Utils.shakeError());
            return false;
        }
        if (TextUtils.isEmpty(getText(etBeneficiaryName))) {
            etBeneficiaryName.setError(getString(R.string.this_field_cannot_be_empty));
            etBeneficiaryName.requestFocus();
            etBeneficiaryName.startAnimation(Utils.shakeError());
            return false;
        }
        if (TextUtils.isEmpty(getText(etAccountNumber))) {
            etAccountNumber.setError(getString(R.string.this_field_cannot_be_empty));
            etAccountNumber.requestFocus();
            etAccountNumber.startAnimation(Utils.shakeError());
            return false;
        }
        if (TextUtils.isEmpty(getText(etRemark))) {
            etRemark.setError(getString(R.string.this_field_cannot_be_empty));
            etRemark.requestFocus();
            etRemark.startAnimation(Utils.shakeError());
            return false;
        }
        if (Double.parseDouble(getText(etAmount)) == 0) {
            etAmount.setError(getString(R.string.greater_than_zero));
            etAmount.requestFocus();
            etAmount.startAnimation(Utils.shakeError());
            return false;
        }
        return true;
    }

    private String getText(EditText editText) {
        return editText.getText().toString().trim();
    }

    Observer<PayprBankDetailResponseBean> observer = new Observer<PayprBankDetailResponseBean>() {
        @Override
        public void onChanged(@Nullable PayprBankDetailResponseBean response) {
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
                if (response.getResponseData().getData().getBankName() != null) {
                    etBankName.setText(response.getResponseData().getData().getBankName());
                    etBankName.setEnabled(false);
                }
                if (response.getResponseData().getData().getAccountNumber() != null) {
                    etAccountNumber.setText(response.getResponseData().getData().getAccountNumber());
                    etAccountNumber.setEnabled(false);
                }
                if (response.getResponseData().getData().getBeneficiaryName() != null) {
                    etBeneficiaryName.setText(response.getResponseData().getData().getBeneficiaryName());
                    etBeneficiaryName.setEnabled(false);
                }
                if (response.getResponseData().getData().getBranchName() != null) {
                    etBranch.setText(response.getResponseData().getData().getBranchName());
                    etBranch.setEnabled(false);
                }
            } else
                Utils.showCustomErrorDialog(getContext(), getString(R.string.payout), getString(R.string.something_went_wrong));
        }
    };

    private void callPayoutApi() {
        ProgressBarDialog.getProgressDialog().showProgress(Objects.requireNonNull(getActivity()));
        PayprOlaPayoutRequestBean requestBean = new PayprOlaPayoutRequestBean();
        requestBean.setAmount(Integer.parseInt(getText(etAmount)));
        requestBean.setConfirmAmount(Integer.parseInt(getText(etAmountConfirmAmount)));
        requestBean.setRemarks(getText(etRemark));
        requestBean.setOrgId(String.valueOf(PlayerData.getInstance().getLoginData().getResponseData().getData().getOrgId()));
        Integer domain = (int) PlayerData.getInstance().getLoginData().getResponseData().getData().getDomainId();
        requestBean.setDomainId(domain);
        requestBean.setType("BANK");
        requestBean.setBankName(getText(etBankName));
        requestBean.setAccountNumber(getText(etAccountNumber));
        requestBean.setBeneficiaryName(getText(etBeneficiaryName));
        requestBean.setBranchName(getText(etBranch));
        requestBean.setAppType("ANDROID_PORTRAIT");
        requestBean.setDevice("MOBILE");
        requestBean.setClientIp(Utils.getIp());
        viewModel.callPayoutApi(requestBean, menuBean,PlayerData.getInstance().getToken());
    }

    Observer<PayprPayoutResponseBean> observerPayout = response -> {
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
            ProgressBarDialog.getProgressDialog().showProgress(master);
            viewModel.getUpdatedBalance(PlayerData.getInstance().getToken());
        } else
            Utils.showCustomErrorDialog(getContext(), getString(R.string.payout), getString(R.string.something_went_wrong));
    };

    Observer<LoginBean> observerBalance = loginBean -> {
        ProgressBarDialog.getProgressDialog().dismiss();
        String errorMsg = getString(R.string.deposit_success_error_fetch_balance);
        if (loginBean == null)
            Utils.showCustomErrorDialogPop(getContext(), getString(R.string.payout), errorMsg, 1, getFragmentManager());
        else if (loginBean.getResponseCode() == 0) {
            LoginBean.ResponseData responseData = loginBean.getResponseData();
            if (responseData.getStatusCode() == 0) {
                PlayerData.getInstance().setLoginData(getActivity(), loginBean);
                refreshBalance();
                String successMsg = getString(R.string.payout_done);
                SuccessDialog dialog = new SuccessDialog(getActivity(), getFragmentManager(), "", successMsg, 1);
                dialog.show();
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                }
            } else {
                Utils.showCustomErrorDialogPop(getContext(), getString(R.string.payout), errorMsg, 1, getFragmentManager());
            }
        } else {
            Utils.showCustomErrorDialogPop(getContext(), getString(R.string.payout), errorMsg, 1, getFragmentManager());
        }
    };
}