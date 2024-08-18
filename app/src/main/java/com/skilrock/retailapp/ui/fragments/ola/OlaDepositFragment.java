package com.skilrock.retailapp.ui.fragments.ola;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skilrock.retailapp.R;
import com.skilrock.retailapp.dialog.SuccessDialog;
import com.skilrock.retailapp.interfaces.ConfirmationListener;
import com.skilrock.retailapp.models.UrlOlaBean;
import com.skilrock.retailapp.models.ola.OlaDepositRequestBean;
import com.skilrock.retailapp.models.ola.OlaDepositResponseBean;
import com.skilrock.retailapp.models.rms.HomeDataBean;
import com.skilrock.retailapp.models.rms.LoginBean;
import com.skilrock.retailapp.ui.fragments.BaseFragment;
import com.skilrock.retailapp.utils.AppConstants;
import com.skilrock.retailapp.utils.PlayerData;
import com.skilrock.retailapp.utils.ProgressBarDialog;
import com.skilrock.retailapp.utils.Utils;
import com.skilrock.retailapp.viewmodels.ola.OlaDepositViewModel;

import java.util.Objects;

public class OlaDepositFragment extends BaseFragment implements View.OnClickListener {

    private OlaDepositViewModel viewModel;
    private EditText etMobile, etAmount;
    private HomeDataBean.ResponseData.ModuleBeanList.MenuBeanList menuBean;
    private TextInputLayout tilAmount, tilMobile;
    private Button button;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ola_deposit, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = ViewModelProviders.of(this).get(OlaDepositViewModel.class);
        viewModel.getDepositLiveData().observe(this, observer);
        viewModel.getLiveDataBalance().observe(this, observerBalance);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeWidgets(view);
    }

    private void initializeWidgets(View view) {
        etMobile            = view.findViewById(R.id.et_mobile_number);
        etAmount            = view.findViewById(R.id.et_amount);
        tilAmount           = view.findViewById(R.id.til_amount);
        tilMobile           = view.findViewById(R.id.til_mobile);
        button              = view.findViewById(R.id.button);
        TextView tvTitle    = view.findViewById(R.id.tvTitle);
        tvUsername          = view.findViewById(R.id.tvUserName);
        tvUserBalance       = view.findViewById(R.id.tvUserBalance);
        refreshBalance();

        button.setOnClickListener(this);
        Bundle bundle = getArguments();
        FragmentActivity activity = getActivity();
        if (bundle != null) {
            if (activity != null) {
                activity.setTitle(bundle.getString("title"));
                tvTitle.setText(bundle.getString("title"));
            }
            menuBean = bundle.getParcelable("MenuBean");
        }

        etAmount.setOnEditorActionListener(this::onEditorAction);
        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();
                if (str.contains(".")) {
                    String[] data = str.split("\\.");
                    if (data.length > 1) {
                        if (data[1].length() > 2) {
                            String txt = data[0] + "." + data[1].substring(0, 2);
                            etAmount.setText(txt);
                            etAmount.setSelection(txt.length());
                        }
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                if (validate()) {
                    ConfirmationListener listener = OlaDepositFragment.this::callDepositApi;
                    String msg = "Are you sure you want to deposit $<font color=#606060>" + getText(etAmount) + " </font>for Mobile Number <font color=#606060>" + getText(etMobile) + "</font>?";
                    Utils.showConfirmationDialog(getContext(), false, msg, listener);
                }
                break;
        }
    }

    private boolean validate() {
        Utils.vibrate(Objects.requireNonNull(getContext()));
        if (TextUtils.isEmpty(getText(etMobile))) {
            etMobile.setError(getString(R.string.this_field_cannot_be_empty));
            etMobile.requestFocus();
            tilMobile.startAnimation(Utils.shakeError());
            return false;
        }
        if (TextUtils.isEmpty(getText(etAmount))) {
            etAmount.setError(getString(R.string.this_field_cannot_be_empty));
            etAmount.requestFocus();
            tilAmount.startAnimation(Utils.shakeError());
            return false;
        }
        if (getText(etAmount).equalsIgnoreCase(".")) {
            etAmount.setError("Invalid Input");
            etAmount.requestFocus();
            tilAmount.startAnimation(Utils.shakeError());
            return false;
        }
        if (Double.parseDouble(getText(etAmount)) == 0) {
            etAmount.setError("Amount must be greater than 0");
            etAmount.requestFocus();
            tilAmount.startAnimation(Utils.shakeError());
            return false;
        }
        if (getContext() != null) {
            if (!Utils.isNetworkConnected(getContext())) {
                Toast.makeText(master, getString(R.string.check_internet_connection), Toast.LENGTH_LONG).show();
                return false;
            }
        }

        return true;
    }

    private void callDepositApi() {
        UrlOlaBean urlBean = Utils.getOlaUrlDetails(menuBean, getContext(), "deposit");
        if (urlBean != null) {
            ProgressBarDialog.getProgressDialog().showProgress(master);
            String domain = PlayerData.getInstance().getLoginData().getResponseData().getData().getOrgCode() + "_" + PlayerData.getInstance().getLoginData().getResponseData().getData().getOrgName();
            OlaDepositRequestBean model = new OlaDepositRequestBean();
            model.setDepositAmt(Double.parseDouble(getText(etAmount)));
            model.setPlayerUserName(getText(etMobile));
            model.setRetailerUserName(PlayerData.getInstance().getUsername());
            model.setPlrDomainCode(domain);
            model.setPlrMerchantCode(AppConstants.PLR_MERCHANT_CODE);
            model.setRetailDomainCode(urlBean.getRetailDomainCode());
            model.setRetailMerchantCode(urlBean.getRetailMerchantCode());
            model.setToken(PlayerData.getInstance().getToken().split(" ")[1]);
            model.setRetUserId(Integer.parseInt(PlayerData.getInstance().getUserId()));
            model.setAppType(AppConstants.APP_TYPE);
            model.setDeviceType(AppConstants.DEVICE_TYPE);
            model.setPaymentType(AppConstants.PAYMENT_TYPE);
            model.setTerminalId(AppConstants.TERMINAL_ID);

            viewModel.callDeposit(urlBean, model);
        }
    }

    Observer<OlaDepositResponseBean> observer = new Observer<OlaDepositResponseBean>() {
        @Override
        public void onChanged(@Nullable OlaDepositResponseBean response) {
            ProgressBarDialog.getProgressDialog().dismiss();

            if (response == null)
                Utils.showCustomErrorDialog(getContext(), getString(R.string.deposit_error), getString(R.string.something_went_wrong));
            else if (response.getResponseCode() == 0) {
                ProgressBarDialog.getProgressDialog().showProgress(master);
                viewModel.getUpdatedBalance(PlayerData.getInstance().getToken());
            } else {
                if (Utils.checkForSessionExpiry(master, response.getResponseCode())) return;

                String errorMsg = TextUtils.isEmpty(response.getResponseMessage()) ? getString(R.string.some_internal_error) : response.getResponseMessage();
                Utils.showCustomErrorDialog(getContext(), getString(R.string.deposit_error), errorMsg);
            }
        }
    };

    Observer<LoginBean> observerBalance = new Observer<LoginBean>() {
        @Override
        public void onChanged(@Nullable LoginBean loginBean) {
            ProgressBarDialog.getProgressDialog().dismiss();

            String errorMsg = getString(R.string.deposit_success_error_fetch_balance);

            if (loginBean == null)
                Utils.showCustomErrorDialogPop(getContext(), getString(R.string.deposit), errorMsg, 1, getFragmentManager());
            else if (loginBean.getResponseCode() == 0) {
                LoginBean.ResponseData responseData = loginBean.getResponseData();
                if (responseData.getStatusCode() == 0) {
                    PlayerData.getInstance().setLoginData(master, loginBean);
                    refreshBalance();
                    String successMsg = getString(R.string.amt_deposit_success);
                    SuccessDialog dialog = new SuccessDialog(master, getFragmentManager(), "", successMsg, 1);
                    dialog.show();
                    if (dialog.getWindow() != null) {
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    }
                } else {
                    Utils.showCustomErrorDialogPop(getContext(), getString(R.string.deposit), errorMsg, 1, getFragmentManager());
                }
            } else {
                Utils.showCustomErrorDialogPop(getContext(), getString(R.string.deposit), errorMsg, 1, getFragmentManager());
            }
        }
    };

    private String getText(EditText editText) {
        return editText.getText().toString().trim();
    }

    private boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            button.performClick();
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        ProgressBarDialog.getProgressDialog().dismiss();
        Utils.dismissCustomErrorDialog();
        super.onDestroy();
    }
}
