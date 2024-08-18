package com.skilrock.retailapp.ui.fragments.rms;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.skilrock.retailapp.BuildConfig;
import com.skilrock.retailapp.R;
import com.skilrock.retailapp.adapter.rms.ScratchAndDrawGameModuleAdapter;
import com.skilrock.retailapp.interfaces.ModuleListener;
import com.skilrock.retailapp.models.rms.HomeDataBean;
import com.skilrock.retailapp.ui.fragments.BaseFragment;
import com.skilrock.retailapp.ui.fragments.ola.OlaDepositFragment;
import com.skilrock.retailapp.ui.fragments.ola.OlaDepositMyanmarFragment;
import com.skilrock.retailapp.ui.fragments.ola.OlaDepositPayprFragment;
import com.skilrock.retailapp.ui.fragments.ola.OlaMyPromoFragment;
import com.skilrock.retailapp.ui.fragments.ola.OlaPendingTransactionCamroonFragment;
import com.skilrock.retailapp.ui.fragments.ola.OlaPendingTransactionFragment;
import com.skilrock.retailapp.ui.fragments.ola.OlaPlayerDetailsReportFragment;
import com.skilrock.retailapp.ui.fragments.ola.OlaPlayerForgotPasswordFragment;
import com.skilrock.retailapp.ui.fragments.ola.OlaPlayerSearchReportFragment;
import com.skilrock.retailapp.ui.fragments.ola.OlaPlayerSearchReportMyanmarFragment;
import com.skilrock.retailapp.ui.fragments.ola.OlaPlayerTransactionReportFragment;
import com.skilrock.retailapp.ui.fragments.ola.OlaPlayerTransactionReportMyanmarFragment;
import com.skilrock.retailapp.ui.fragments.ola.OlaRegistrationFragment;
import com.skilrock.retailapp.ui.fragments.ola.OlaRegistrationMyanmarFragment;
import com.skilrock.retailapp.ui.fragments.ola.OlaRegistrationPayprFragment;
import com.skilrock.retailapp.ui.fragments.ola.OlaWithdrawalFragment;
import com.skilrock.retailapp.ui.fragments.ola.OlaWithdrawalMyanmarFragment;
import com.skilrock.retailapp.utils.AppConstants;
import com.skilrock.retailapp.utils.PlayerData;
import com.skilrock.retailapp.utils.StringMapOla;
import com.skilrock.retailapp.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ScratchAndDrawGameFragment extends BaseFragment {

    private ArrayList<HomeDataBean.ResponseData.ModuleBeanList.MenuBeanList> list;
    //private TextView tvUserBalance;
    Bundle bundle = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            return inflater.inflate(R.layout.pay_pr_fragment, container, false);

    }

    ModuleListener moduleListener = (menuCode, index, menuBean) -> {
        bundle = new Bundle();
        bundle.putInt("index", index);
        bundle.putString("title", StringMapOla.getCaption(menuBean.getMenuCode(), menuBean.getCaption()));
        bundle.putParcelable("MenuBean", menuBean);
        switch (menuCode.trim()) {

            case AppConstants.OLA_REGISTER:
                try {
                    if (PlayerData.getInstance().getUserStatus().trim().equalsIgnoreCase("INACTIVE"))
                        Toast.makeText(master, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                    else {
                        if (BuildConfig.app_name.equalsIgnoreCase(AppConstants.OLA_MYANMAR)) {
                            master.openFragment(new OlaRegistrationMyanmarFragment(), "OlaRegistrationMyanmarFragment", true, bundle);
                            //master.openFragment(new OlaRegistrationFragmentMyanmar(), "OlaRegistrationFragmentMyanmar", true, bundle);
                        }
                        else if (BuildConfig.app_name.equalsIgnoreCase(AppConstants.PAYPR_BUSINESS))
                            master.openFragment(new OlaRegistrationPayprFragment(), "OlaRegistrationPayprFragment", true, bundle);
                        else
                            master.openFragment(new OlaRegistrationFragment(), "OlaRegistrationFragment", true, bundle);
                    }
                } catch (Exception e) {
                    if (BuildConfig.app_name.equalsIgnoreCase(AppConstants.OLA_MYANMAR))
                        master.openFragment(new OlaRegistrationMyanmarFragment(), "OlaRegistrationMyanmarFragment", true, bundle);
                    else if (BuildConfig.app_name.equalsIgnoreCase(AppConstants.PAYPR_BUSINESS))
                        master.openFragment(new OlaRegistrationPayprFragment(), "OlaRegistrationPayprFragment", true, bundle);
                    else
                        master.openFragment(new OlaRegistrationFragment(), "OlaRegistrationFragment", true, bundle);
                }
                break;
            case AppConstants.OLA_DEPOSIT:
                try {
                    if (PlayerData.getInstance().getUserStatus().trim().equalsIgnoreCase("INACTIVE"))
                        Toast.makeText(master, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                    else {
                        if (BuildConfig.app_name.equalsIgnoreCase(AppConstants.OLA_MYANMAR) ||
                                BuildConfig.app_name.equalsIgnoreCase(AppConstants.CAMEROON))
                            master.openFragment(new OlaDepositMyanmarFragment(), "OlaDepositMyanmarFragment", true, bundle);
                        else if (BuildConfig.app_name.equalsIgnoreCase(AppConstants.PAYPR_BUSINESS))
                            master.openFragment(new OlaDepositPayprFragment(), "OlaDepositPayprFragment", true, bundle);
                        else
                            master.openFragment(new OlaDepositFragment(), "OlaDepositFragment", true, bundle);
                    }
                } catch (Exception e) {
                    if (BuildConfig.app_name.equalsIgnoreCase(AppConstants.OLA_MYANMAR) ||
                            BuildConfig.app_name.equalsIgnoreCase(AppConstants.CAMEROON))
                        master.openFragment(new OlaDepositMyanmarFragment(), "OlaDepositMyanmarFragment", true, bundle);
                    else if (BuildConfig.app_name.equalsIgnoreCase(AppConstants.PAYPR_BUSINESS))
                        master.openFragment(new OlaDepositPayprFragment(), "OlaDepositPayprFragment", true, bundle);
                    else
                        master.openFragment(new OlaDepositFragment(), "OlaDepositFragment", true, bundle);
                }
                break;
            case AppConstants.OLA_WITHDRAW:
                try {
                    if (PlayerData.getInstance().getUserStatus().trim().equalsIgnoreCase("INACTIVE"))
                        Toast.makeText(master, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                    else {
                        if (BuildConfig.app_name.equalsIgnoreCase(AppConstants.OLA_MYANMAR) ||
                                BuildConfig.app_name.equalsIgnoreCase(AppConstants.CAMEROON))
                            master.openFragment(new OlaWithdrawalMyanmarFragment(), "OlaWithdrawalMyanmarFragment", true, bundle);
                        else
                            master.openFragment(new OlaWithdrawalFragment(), "OlaWithdrawalFragment", true, bundle);
                    }
                } catch (Exception e) {
                    master.openFragment(new OlaWithdrawalFragment(), "OlaWithdrawalFragment", true, bundle);
                }
                break;
            case AppConstants.OLA_MY_PROMO:
                try {
                    if (PlayerData.getInstance().getUserStatus().trim().equalsIgnoreCase("INACTIVE"))
                        Toast.makeText(master, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                    else {
                        master.openFragment(new OlaMyPromoFragment(), "OlaMyPromoFragment", true, bundle);
                    }
                } catch (Exception e) {
                    master.openFragment(new OlaMyPromoFragment(), "OlaMyPromoFragment", true, bundle);
                }
                break;
            case AppConstants.OLA_PLR_LEDGER:
                if (BuildConfig.app_name.equalsIgnoreCase(AppConstants.OLA_MYANMAR))
                    master.openFragment(new OlaPlayerTransactionReportMyanmarFragment(), "OlaPlayerTransactionReportMyanmarFragment", true, bundle);
                else
                    master.openFragment(new OlaPlayerTransactionReportFragment(), "OlaPlayerLedgerReportFragment", true, bundle);

                break;
            case AppConstants.OLA_PLR_DETAILS:
                bundle.putParcelable("PlayerSearch", getPlayerTransactionMenuDetails());
                master.openFragment(new OlaPlayerDetailsReportFragment(), "OlaPlayerDetailsReportFragment", true, bundle);
                break;
            case AppConstants.OLA_PLR_SEARCH:
                bundle.putParcelable("PlayerSearch", getPlayerTransactionMenuDetails());

                if (BuildConfig.app_name.equalsIgnoreCase(AppConstants.OLA_MYANMAR))
                    master.openFragment(new OlaPlayerSearchReportMyanmarFragment(), "OlaPlayerSearchReportMyanmarFragment", true, bundle);
                else
                    master.openFragment(new OlaPlayerSearchReportFragment(), "OlaPlayerSearchReportFragment", true, bundle);

                break;
            case AppConstants.OLA_PLR_PASSWORD:
                try {
                    if (PlayerData.getInstance().getUserStatus().trim().equalsIgnoreCase("INACTIVE"))
                        Toast.makeText(master, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                    else
                        master.openFragment(new OlaPlayerForgotPasswordFragment(), "OlaPlayerForgotPasswordFragment", true, bundle);
                } catch (Exception e) {
                    master.openFragment(new OlaPlayerForgotPasswordFragment(), "OlaPlayerForgotPasswordFragment", true, bundle);
                }
                break;
            case AppConstants.OLA_PLR_PENDING_TXN:
                bundle.putParcelable("PlayerSearch", getPlayerTransactionMenuDetails());

                if (BuildConfig.app_name.equalsIgnoreCase(AppConstants.CAMEROON))
                    master.openFragment(new OlaPendingTransactionCamroonFragment(), "OlaPendingTransactionCamroonFragment", true, bundle);
                else
                    master.openFragment(new OlaPendingTransactionFragment(), "OlaPendingTransactionFragment", true, bundle);

                break;
//            case AppConstants.OLA_NET_GAMING_DETAILS:
//                bundle.putParcelable("PlayerSearch", getPlayerTransactionMenuDetails());
//                master.openFragment(new OlaNetGamingReportFragment(), "OlaNetGamingReport", true, bundle);

            case AppConstants.PAY_PR_CHANGE_PASSWORD:
                bundle.putString("url", menuBean.getBasePath() + menuBean.getRelativePath());
                master.openFragment(new ChangePasswordFragment(), "ChangePasswordFragment", true, bundle);
                break;
            case AppConstants.PAY_PR_DISPLAY_QR_CODE:
                bundle.putString("url", menuBean.getBasePath() + menuBean.getRelativePath());
                master.openFragment(new DisplayQRFragment(), "DisplayQRFragment", true, bundle);
                break;
            case AppConstants.PAY_PR_DEPOSIT_REPORT:
                bundle.putString("url", menuBean.getBasePath() + menuBean.getRelativePath());
                master.openFragment(new OlaReportFragment(), "OlaReportFragment", true, bundle);
                break;
            case AppConstants.PAY_PR_LEDGER_REPORT:
                bundle.putString("url", menuBean.getBasePath() + menuBean.getRelativePath());
                master.openFragment(new LedgerReportFragment(), "LedgerReportFragment", true, bundle);
                break;
            case AppConstants.PAY_PR_PAYMENT_REPORT:
                bundle.putString("url", menuBean.getBasePath() + menuBean.getRelativePath());
                master.openFragment(new PaymentReportFragment(), "PaymentReportFragment", true, bundle);
                break;
            case AppConstants.PAY_PR_PAYOUT:
                master.openFragment(new PayoutFragment(), "PayoutFragment", true, bundle);
                break;
        }
    };



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeWidgets(view);
    }

    private void initializeWidgets(View view) {
        RecyclerView rvScratchAndDrawGameMenu = view.findViewById(R.id.rv_scratch_menu);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        LinearLayout llBack = view.findViewById(R.id.llBack);
        tvUsername = view.findViewById(R.id.tvUserName);
        tvUserBalance = view.findViewById(R.id.tvUserBalance);
        ImageView ivModule = view.findViewById(R.id.ivModule);
        ImageView ivDrawer = view.findViewById(R.id.iv_drawer);
        refreshBalance();
        rvScratchAndDrawGameMenu.setHasFixedSize(true);
        rvScratchAndDrawGameMenu.setLayoutManager(Utils.getDeviceName().equalsIgnoreCase(AppConstants.DEVICE_T2MINI) ? new GridLayoutManager(master, 4) : new GridLayoutManager(master, 2));
        Bundle bundle = getArguments();
        FragmentActivity activity = getActivity();
        if (bundle != null) {
            list = bundle.getParcelableArrayList("ListScratchGameModule");
            removeDisplayQrCodeMenu(list);
            ScratchAndDrawGameModuleAdapter scratchAndDrawGameModuleAdapter = new ScratchAndDrawGameModuleAdapter(list, moduleListener);
            rvScratchAndDrawGameMenu.setAdapter(scratchAndDrawGameModuleAdapter);
            if (activity != null) {
                activity.setTitle(bundle.getString("title"));
                if (BuildConfig.app_name.equalsIgnoreCase(AppConstants.PAYPR_BUSINESS)){
                    tvTitle.setText(bundle.getString("title"));
                    tvTitle.setTextColor(getActivity().getColor(R.color.colorDrawResultBar));
                }

                master.IS_SINGLE_MENU = bundle.getBoolean("isSingleMenu");

                if (bundle.getString("title").equalsIgnoreCase("SCRATCH")) {
                    //tvTitle.setVisibility(View.GONE);
                    ivModule.setVisibility(View.VISIBLE);
                    ivModule.setBackgroundResource(R.drawable.icon_scratch);
                } else if (bundle.getString("title").equalsIgnoreCase("PLAYER MANAGEMENT") ||
                        bundle.getString("title").equalsIgnoreCase(getString(R.string.player_management_caption))) {
                    ivModule.setVisibility(View.VISIBLE);
                    ivModule.setBackgroundResource(R.drawable.group_1595);
                } else {
                    tvTitle.setVisibility(View.VISIBLE);
                    ivModule.setVisibility(View.GONE);
                }

            }
        }

        llBack.setOnClickListener(v -> {
            if (master.IS_SINGLE_MENU)
                master.onClickOpenDrawer(llBack);
            else
                Objects.requireNonNull(getFragmentManager()).popBackStack();
        });

        if (master.IS_SINGLE_MENU) {
            master.enableDrawer(true);
            ivDrawer.setImageResource(R.drawable.ic_paypr_menu);
        }

        if (BuildConfig.app_name.equalsIgnoreCase(AppConstants.PAYPR_BUSINESS)) {
            ivModule.setVisibility(View.GONE);
        }
    }

    private HomeDataBean.ResponseData.ModuleBeanList.MenuBeanList getPlayerTransactionMenuDetails() {
        for (int index = 0; index < list.size(); index++) {
            if (list.get(index).getMenuCode().trim().equalsIgnoreCase(AppConstants.OLA_PLR_LEDGER))
                return list.get(index);
        }
        return null;
    }

    public void updateBalance() {
        tvUserBalance.setText(PlayerData.getInstance().getOrgBalance());
    }

    /*public void refreshBalance() {
        tvUserBalance.setText(PlayerData.getInstance().getOrgBalance());
    }*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null && data.getExtras() != null && data.getExtras().getBoolean("isBalanceUpdate")) {
            refreshBalance();
        } else {
            String errorMsg = getString(R.string.insert_paper_to_print);
            Utils.showCustomErrorDialog(getActivity(), getString(R.string.winning), errorMsg);
        }
    }

    /*@Override
    public void onBackPressed() {
        Log.w("log", "IS_SINGLE_MENU: " + IS_SINGLE_MENU);
        if (IS_SINGLE_MENU)
            master.finish();
        else
            super.onBackPressed();
    }*/

    private void removeDisplayQrCodeMenu(@NotNull ArrayList<HomeDataBean.ResponseData.ModuleBeanList.MenuBeanList> listMenuBean) {

        Iterator<HomeDataBean.ResponseData.ModuleBeanList.MenuBeanList> iterator = listMenuBean.iterator();
        while (iterator.hasNext()){
            HomeDataBean.ResponseData.ModuleBeanList.MenuBeanList name = iterator.next();
            if(name.getMenuCode().equalsIgnoreCase("M_OLA_DISPLAY_QR_CODE") && PlayerData.getInstance().getLoginData().getResponseData().getData().getQrCode() == null){
                iterator.remove();
            }
        }
    }

}