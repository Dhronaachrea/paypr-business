package com.skilrock.retailapp.ui.fragments.rms;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;

import com.skilrock.retailapp.BuildConfig;
import com.skilrock.retailapp.R;
import com.skilrock.retailapp.adapter.rms.HomeModuleAdapter;
import com.skilrock.retailapp.interfaces.HomeModuleListener;
import com.skilrock.retailapp.models.BetDeviceResponseBean;
import com.skilrock.retailapp.models.rms.ConfigurationResponseBean;
import com.skilrock.retailapp.models.rms.HomeDataBean;
import com.skilrock.retailapp.network.APIConfig;
import com.skilrock.retailapp.ui.activities.MainActivity;
import com.skilrock.retailapp.ui.fragments.BaseFragment;
import com.skilrock.retailapp.utils.AppConstants;
import com.skilrock.retailapp.utils.ConfigData;
import com.skilrock.retailapp.utils.PlayerData;
import com.skilrock.retailapp.utils.ProgressBarDialog;
import com.skilrock.retailapp.utils.SharedPrefUtil;
import com.skilrock.retailapp.utils.Utils;
import com.skilrock.retailapp.viewmodels.rms.HomeViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.O)
public class HomeFragment extends BaseFragment implements View.OnClickListener{

    private RecyclerView rvHomeModules;
    private HomeViewModel homeViewModel;
    private RelativeLayout llAppBar;
    private ArrayList<HomeDataBean.ResponseData.ModuleBeanList> moduleBeanLists = new ArrayList<>();
    private ImageView imageLogo, ivApp;
    private final String RMS = "rms";
    String gameData[] = null;
    Bitmap bitmap = null;
    Bundle bundle;
    private String game_url;
    private String title;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.game_list_type_fragment_pay_pr, container, false);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        homeViewModel.getLiveHomeData().observe(this, observer);
        homeViewModel.getLiveDataConfig().observe(this, observerConfigBean);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProgressBarDialog.getProgressDialog().showProgress(master);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeWidgets(view);
        master.unLockDrawer();
    }

    private void initializeWidgets(View view) {
        rvHomeModules               = view.findViewById(R.id.rv_home_modules);
        imageLogo                   = view.findViewById(R.id.image_logo);
        tvUserBalance               = view.findViewById(R.id.tvBal);
        tvUsername                  = view.findViewById(R.id.tvUserName);
        llAppBar                    = view.findViewById(R.id.ll_aap_bar);
        ivApp                       = view.findViewById(R.id.ivApp);

        try {
            refreshBalance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentActivity activity = getActivity();
        if (activity != null)
            activity.setTitle(R.string.home);

        if (moduleBeanLists != null && !moduleBeanLists.isEmpty())
            setAdapter(moduleBeanLists);
        else
            callConfigDataApi();

        if (BuildConfig.app_name.equalsIgnoreCase(AppConstants.PAYPR_BUSINESS)) {
            ivApp.setVisibility(View.GONE);
        }
    }

    HomeModuleListener homeModuleListener = (moduleCode, index, displayName, listMenuBean) -> {
        bundle = new Bundle();
        switch (moduleCode.trim()) {

            case AppConstants.SCRATCH:
                bundle.putInt("index", index);
                bundle.putParcelableArrayList("ListScratchGameModule", listMenuBean);
                bundle.putString("title", displayName);
                master.openFragment(new ScratchAndDrawGameFragment(), "ScratchFragment", true, bundle);
                break;

            case AppConstants.OLA:
                if (PlayerData.getInstance().getIsAffiliate().equalsIgnoreCase(AppConstants.NO))
                    removeMyPromoCode(listMenuBean);
                bundle.putInt("index", index);
                bundle.putParcelableArrayList("ListScratchGameModule", listMenuBean);
                bundle.putString("title", displayName);
                master.openFragment(new ScratchAndDrawGameFragment(), "ScratchFragment", true, bundle);
                break;


                    }
    };

    private void removeMyPromoCode(@NotNull ArrayList<HomeDataBean.ResponseData.ModuleBeanList.MenuBeanList> listMenuBean) {

        Iterator<HomeDataBean.ResponseData.ModuleBeanList.MenuBeanList> iterator = listMenuBean.iterator();
        while (iterator.hasNext()){
            HomeDataBean.ResponseData.ModuleBeanList.MenuBeanList name = iterator.next();
            if(name.getMenuCode().equalsIgnoreCase("M_OLA_PROMO_CODE") || name.getMenuCode().equalsIgnoreCase("M_NET_GAMING_DETAILS")){
                iterator.remove();
            }
        }
    }

    private HomeDataBean.ResponseData.ModuleBeanList.MenuBeanList getUrlForBetGames(ArrayList<HomeDataBean.ResponseData.ModuleBeanList.MenuBeanList> list) {
        for (HomeDataBean.ResponseData.ModuleBeanList.MenuBeanList menuBeanList : list) {
            if (menuBeanList.getMenuCode().equalsIgnoreCase("BET_GAME")) {
                return menuBeanList;
            }
        }
        return null;
    }

    private void callConfigDataApi(){
        try {
            String url = APIConfig.getInstance().baseUrl() + APIConfig.rmsConfigUrl;
            Integer domain = (int) PlayerData.getInstance().getLoginData().getResponseData().getData().getDomainId();

            homeViewModel.getConfigApi(url, PlayerData.getInstance().getToken(), domain);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Observer<ConfigurationResponseBean> observerConfigBean = new Observer<ConfigurationResponseBean>() {
        @Override
        public void onChanged(@Nullable ConfigurationResponseBean configurationResponseBean) {
            ProgressBarDialog.getProgressDialog().dismiss();

            if (configurationResponseBean == null) {
                Utils.showCustomErrorDialogAndLogout(getContext(), getString(R.string.config_error), getString(R.string.something_went_wrong), true);
            }
            else if (configurationResponseBean.getResponseCode() == 0) {
                if (configurationResponseBean.getResponseData().getStatusCode() == 0) {
                    ConfigData.getInstance().setConfigData(master, configurationResponseBean.getResponseData().getData().get(0));
                    homeViewModel.getHomeModuleList(PlayerData.getInstance().getToken(), PlayerData.getInstance().getUserId(), master);
                    refreshBalance();
                    SharedPrefUtil.putString(master, SharedPrefUtil.CONTACT_US_NUMBER, configurationResponseBean.getResponseData().getData().get(0).getCallUsNumber());
                } else {
                    if (Utils.checkForSessionExpiry(master, configurationResponseBean.getResponseData().getStatusCode()))
                        return;
                    String errorMsg = Utils.getResponseMessage(master, RMS, configurationResponseBean.getResponseData().getStatusCode());
                    Utils.showCustomErrorDialogAndLogout(getContext(), getString(R.string.config_error), errorMsg, true);
                }
            } else {
                String errorMsg = Utils.getResponseMessage(master, RMS, configurationResponseBean.getResponseCode());
                Utils.showCustomErrorDialogAndLogout(getContext(), getString(R.string.config_error), errorMsg, true);
            }
        }
    };

    Observer<HomeDataBean> observer = homeDataBean -> {
        ProgressBarDialog.getProgressDialog().dismiss();
        if (homeDataBean == null) {
            //for testing
            Utils.showCustomErrorDialogAndLogout(getContext(), "", getString(R.string.something_went_login_again), true);
        }
        else if (homeDataBean.getResponseCode() == 0) {
            if (homeDataBean.getResponseData().getStatusCode() == 0) {
                moduleBeanLists = homeDataBean.getResponseData().getModuleBeanLists();
                if (moduleBeanLists == null || moduleBeanLists.isEmpty()) {
                    Utils.showCustomErrorDialogAndLogout(getContext(), "", getString(R.string.some_technical_issue), true);
                }
                else {
                    boolean flag = false;
                    for (HomeDataBean.ResponseData.ModuleBeanList module: moduleBeanLists) {
                        String moduleCode = module.getModuleCode();
                        if (moduleCode.equalsIgnoreCase(AppConstants.SCRATCH) || moduleCode.equalsIgnoreCase(AppConstants.OLA) || moduleCode.equalsIgnoreCase(AppConstants.DRAW_GAME))
                            flag = true;
                    }

                    if (flag) {
                        removeNonGameModules(moduleBeanLists);
                        checkForSingleMenu(moduleBeanLists);
                    }
                    else {
                        Utils.showCustomErrorDialogAndLogout(getContext(), "", getString(R.string.some_technical_issue), true);
                    }
                }
            } else {
                if (homeDataBean.getResponseData().getStatusCode() == 3021) {
                    String errorMsg = Utils.getResponseMessage(master, RMS, homeDataBean.getResponseData().getStatusCode());
                    Utils.showCustomErrorDialogAndLogout(getContext(), "", errorMsg, true);
                }
                else {
                    if (Utils.checkForSessionExpiry(master, homeDataBean.getResponseData().getStatusCode()))
                        return;
                    String errorMsg = Utils.getResponseMessage(master, RMS, homeDataBean.getResponseData().getStatusCode());
                    //Utils.showCustomErrorDialog(getContext(), "", errorMsg);
                    Utils.showCustomErrorDialogAndLogout(getContext(), "", errorMsg, true);
                }
            }
        } else {
            //String errorMsg = TextUtils.isEmpty(homeDataBean.getResponseMessage()) ? getString(R.string.some_internal_error) : homeDataBean.getResponseMessage();
            String errorMsg = Utils.getResponseMessage(master, RMS, homeDataBean.getResponseCode());
            Utils.showCustomErrorDialog(getContext(), "", errorMsg);
        }
    };

    private void removeNonGameModules(List<HomeDataBean.ResponseData.ModuleBeanList> moduleBeanLists) {
        List<HomeDataBean.ResponseData.ModuleBeanList> drawerList = new ArrayList<>();
        if (moduleBeanLists != null && moduleBeanLists.size() > 0) {
            for (int index = 0; index < moduleBeanLists.size(); index++) {
                if (moduleBeanLists.size() > 0 && moduleBeanLists.get(index).getModuleCode().trim().equalsIgnoreCase(AppConstants.USERS))
                    drawerList.add(moduleBeanLists.get(index));
                if (moduleBeanLists.size() > 0 && moduleBeanLists.get(index).getModuleCode().trim().equalsIgnoreCase(AppConstants.REPORTS))
                    drawerList.add(moduleBeanLists.get(index));
            }

            for (int index = 0; index < drawerList.size(); index++) {
                if (drawerList.size() > 0 && drawerList.get(index).getModuleCode().trim().equalsIgnoreCase(AppConstants.USERS))
                    moduleBeanLists.remove(drawerList.get(index));
                if (drawerList.size() > 0 && drawerList.get(index).getModuleCode().trim().equalsIgnoreCase(AppConstants.REPORTS))
                    moduleBeanLists.remove(drawerList.get(index));
            }
        }

        if (BuildConfig.app_name.equalsIgnoreCase(AppConstants.PAYPR_BUSINESS) && PlayerData.getInstance().getLoginData().getResponseData().getData().getQrCode() != null) {
            for (int index = 0; index < drawerList.size(); index++) {
                if (drawerList.size() > 0 && drawerList.get(index).getModuleCode().trim().equalsIgnoreCase(AppConstants.USERS)) {
                    ArrayList<HomeDataBean.ResponseData.ModuleBeanList.MenuBeanList> menuList = drawerList.get(index).getMenuBeanList();
                    for (int i = 0; i < menuList.size(); i++) {
                        if (menuList.get(i).getMenuCode().equalsIgnoreCase(AppConstants.QR_CODE_REGISTRATION)) {
                            HomeDataBean.ResponseData.ModuleBeanList.MenuBeanList menu = menuList.get(i);
                            menu.setMenuCode(AppConstants.NATIVE_DISPLAY_QR);
                            menu.setCaption(getString(R.string.display_qr));
                            menu.setMenuId(AppConstants.MENU_ID_DISPLAY_QR);
                        }
                    }
                }
            }
        }

        if (getActivity() != null && drawerList.size() > 0)
            ((MainActivity) getActivity()).setDrawerElements(drawerList);
    }

    @Override
    public void onClick(View view) {

    }

    private void checkForSingleMenu(ArrayList<HomeDataBean.ResponseData.ModuleBeanList> moduleBeanLists) {
        try {
            setAdapter(moduleBeanLists);
            if (moduleBeanLists.size() == 1) {
                llAppBar.setVisibility(View.INVISIBLE);
                rvHomeModules.setVisibility(View.INVISIBLE);
                SharedPrefUtil.putBoolean(Objects.requireNonNull(getContext()), SharedPrefUtil.SHOW_HOME_SCREEN, false);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("ListScratchGameModule", moduleBeanLists.get(0).getMenuBeanList());
                bundle.putString("title", moduleBeanLists.get(0).getDisplayName());
                bundle.putBoolean("isSingleMenu", true);
                //Objects.requireNonNull(getFragmentManager()).popBackStack();
                master.openFragment(new ScratchAndDrawGameFragment(), "ScratchFragment", true, bundle);
            } else {
                setAdapter(moduleBeanLists);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAdapter(ArrayList<HomeDataBean.ResponseData.ModuleBeanList> moduleBeanLists) {
        llAppBar.setVisibility(View.VISIBLE);
        rvHomeModules.setHasFixedSize(true);
        if(Utils.getDeviceName().equalsIgnoreCase(AppConstants.DEVICE_T2MINI))
            rvHomeModules.setLayoutManager(new GridLayoutManager(master, 4));
        else
            rvHomeModules.setLayoutManager(new GridLayoutManager(master, 2));
        rvHomeModules.setVisibility(View.VISIBLE);
        HomeModuleAdapter homeModuleAdapter = new HomeModuleAdapter(getContext(), moduleBeanLists, homeModuleListener);
        rvHomeModules.setAdapter(homeModuleAdapter);
    }

}