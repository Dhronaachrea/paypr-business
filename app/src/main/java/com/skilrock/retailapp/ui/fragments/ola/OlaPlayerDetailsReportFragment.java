package com.skilrock.retailapp.ui.fragments.ola;

import android.annotation.SuppressLint;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skilrock.retailapp.R;
import com.skilrock.retailapp.adapter.ola.OlaPlayerDetailsAdapter;
import com.skilrock.retailapp.interfaces.OlaPlayerDetailsResponseListener;
import com.skilrock.retailapp.interfaces.PlayerSelectionListener;
import com.skilrock.retailapp.models.UrlOlaBean;
import com.skilrock.retailapp.models.ola.OlaPlayerDetailsReportRequestBean;
import com.skilrock.retailapp.models.ola.OlaPlayerDetailsResponseBean;
import com.skilrock.retailapp.models.rms.HomeDataBean;
import com.skilrock.retailapp.ui.fragments.BaseFragment;
import com.skilrock.retailapp.utils.AppConstants;
import com.skilrock.retailapp.utils.PlayerData;
import com.skilrock.retailapp.utils.ProgressBarDialog;
import com.skilrock.retailapp.utils.Utils;
import com.skilrock.retailapp.viewmodels.ola.OlaPlayerDetailsReportViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class OlaPlayerDetailsReportFragment extends BaseFragment implements View.OnClickListener {

    private OlaPlayerDetailsReportViewModel viewModel;
    private TextView tvStartDate, tvEndDate;
    private RecyclerView rvReport;
    private HomeDataBean.ResponseData.ModuleBeanList.MenuBeanList menuBean, playerSearchBean;
    private int PAGE_INDEX = 1;
    private boolean isLoading = false;
    private boolean isDataFinished = false;
    private ArrayList<OlaPlayerDetailsResponseBean.ResponseData> listRV = new ArrayList<>();
    private OlaPlayerDetailsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ola_player_details, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() != null) {
            //initialise view model
            viewModel = ViewModelProviders.of(this).get(OlaPlayerDetailsReportViewModel.class);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeWidgets(view);
        initScrollListener();
        /*viewModel.getLiveData().observe(this, observer);
        viewModel.getLiveDataLoadMore().observe(this, observerLoadMore);*/
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
            playerSearchBean = bundle.getParcelable("PlayerSearch");
        }

        tvStartDate.setText(Utils.getCurrentDate());
        tvEndDate.setText(Utils.getCurrentDate());

        Log.d("log", "List Size: " + (listRV.size()));
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
                if (validate())
                    callPlayerDetailApi(true);
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
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");

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

    /*Observer<OlaPlayerDetailsResponseBean> observer = new Observer<OlaPlayerDetailsResponseBean>() {

        @Override
        public void onChanged(@Nullable OlaPlayerDetailsResponseBean response) {
            if (progressDialog != null) progressDialog.dismiss();

            if (response == null) Utils.showCustomErrorDialog(getContext(), getString(R.string.player_details), getString(R.string.something_went_wrong));
            else if (response.getResponseCode() == 0) {
                if (response.getResponseData() != null && response.getResponseData().size() < 1) {
                    Utils.showCustomErrorDialog(getContext(), getString(R.string.player_details), getString(R.string.no_data_found));
                }
                else {
                    PAGE_INDEX = 1;
                    isDataFinished = false;
                    ArrayList<OlaPlayerDetailsResponseBean.ResponseData> list = response.getResponseData();
                    if (list != null) {
                        listRV.clear();
                        if (list.size() < AppConstants.PAGE_SIZE)
                            isDataFinished = true;
                        listRV.addAll(list);
                        PlayerSelectionListener listener = OlaPlayerDetailsReportFragment.this::redirectToPlayerSearchFragment;
                        adapter = new OlaPlayerDetailsAdapter(getContext(), listRV, listener);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                        rvReport.setLayoutManager(mLayoutManager);
                        rvReport.setItemAnimator(new DefaultItemAnimator());
                        rvReport.setAdapter(adapter);
                    }
                }
            } else {
                if (Utils.checkForSessionExpiry(master, response.getResponseCode())) return;

                String errorMsg = TextUtils.isEmpty(response.getResponseMessage()) ? getString(R.string.some_internal_error) : response.getResponseMessage();
                Utils.showCustomErrorDialog(getContext(), getString(R.string.player_details), errorMsg);
            }
        }
    };

    Observer<OlaPlayerDetailsResponseBean> observerLoadMore = response -> {
        if (response == null) {
            loadMoreAfterApiResponse(null);
            Toast.makeText(master, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
        }
        else if (response.getResponseCode() == 0) {
            if (response.getResponseData() != null && response.getResponseData().size() < 1) {
                Toast.makeText(master, getString(R.string.no_more_data), Toast.LENGTH_SHORT).show();
                isDataFinished = true;
                loadMoreAfterApiResponse(null);
            }
            else {
                if (Objects.requireNonNull(response.getResponseData()).size() < AppConstants.PAGE_SIZE)
                    isDataFinished = true;
                loadMoreAfterApiResponse(response.getResponseData());
            }
        } else {
            loadMoreAfterApiResponse(null);
            if (Utils.checkForSessionExpiry(master, response.getResponseCode())) return;

            String errorMsg = TextUtils.isEmpty(response.getResponseMessage()) ? getString(R.string.some_internal_error) : response.getResponseMessage();
            Toast.makeText(master, errorMsg, Toast.LENGTH_LONG).show();
        }
    };*/

    private void redirectToPlayerSearchFragment(String mobileNumber) {
        Bundle bundle = new Bundle();
        bundle.putString("title", playerSearchBean.getCaption().toUpperCase());
        bundle.putParcelable("MenuBean", playerSearchBean);
        bundle.putString("MobileNumber", mobileNumber);
        master.openFragment(new OlaPlayerTransactionReportFragment(), "OlaPlayerTransactionReportFragment", true, bundle);
    }

    private void callPlayerDetailApi(boolean isFirstTime) {
        if (adapter != null) adapter.clear();
        UrlOlaBean urlBean = Utils.getOlaUrlDetails(menuBean, getContext(), "playersDetail");
        if (urlBean != null) {
            if (isFirstTime)
                ProgressBarDialog.getProgressDialog().showProgress(master);

            String domain = PlayerData.getInstance().getLoginData().getResponseData().getData().getOrgCode() + "_" +
                    PlayerData.getInstance().getLoginData().getResponseData().getData().getOrgName();
            OlaPlayerDetailsReportRequestBean model = new OlaPlayerDetailsReportRequestBean();
            model.setAccept(urlBean.getAccept());
            model.setContentType(urlBean.getContentType());
            model.setFromDate(formatDate(tvStartDate));
            model.setToDate(formatDate(tvEndDate));

            model.setLimit(AppConstants.PAGE_SIZE);
            model.setOffset(isFirstTime?1:++PAGE_INDEX);
            model.setMobileNo("");
            model.setPassword(urlBean.getPassword());

            model.setUsername(urlBean.getUserName());
            model.setRetailOrgId(PlayerData.getInstance().getLoginData().getResponseData().getData().getOrgId());
            model.setPlrDomainCode(domain);
            model.setUrl(urlBean.getUrl());

            model.setRetailDomainCode(urlBean.getRetailDomainCode());

            if (isFirstTime)
                viewModel.callOlaPlayerDetail(model, listener);
            else
                viewModel.loadMore(model, listener);
        }
    }

    private String formatDate(TextView tvDate)  {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat originalFormat = new SimpleDateFormat("dd-mm-yyyy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat targetFormat = new SimpleDateFormat("dd/mm/yyyy" );
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

    private void initScrollListener() {
        rvReport.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == listRV.size() - 1) {
                        if (!isDataFinished) {
                            loadMore();
                            isLoading = true;
                        }
                    }
                }
            }
        });
    }

    private void loadMore() {
        try {
            listRV.add(null);
            adapter.notifyItemInserted(listRV.size() - 1);
            callPlayerDetailApi(false);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadMoreAfterApiResponse(ArrayList<OlaPlayerDetailsResponseBean.ResponseData> listData) {
        listRV.remove(listRV.size() - 1);
        if (listData != null) {
            int scrollPosition = listRV.size();
            adapter.notifyItemRemoved(scrollPosition);
            listRV.addAll(listData);
        }
        adapter.notifyDataSetChanged();
        isLoading = false;
    }

    OlaPlayerDetailsResponseListener listener = new OlaPlayerDetailsResponseListener() {
        @Override
        public void onResponse(OlaPlayerDetailsResponseBean response) {
            Log.d("log", "---------------------------------------");
            ProgressBarDialog.getProgressDialog().dismiss();

            if (response == null) Utils.showCustomErrorDialog(getContext(), getString(R.string.player_details), getString(R.string.something_went_wrong));
            else if (response.getResponseCode() == 0) {
                if (response.getResponseData() != null && response.getResponseData().size() < 1) {
                    Utils.showCustomErrorDialog(getContext(), getString(R.string.player_details), getString(R.string.no_data_found));
                }
                else {
                    PAGE_INDEX = 1;
                    isDataFinished = false;
                    ArrayList<OlaPlayerDetailsResponseBean.ResponseData> list = response.getResponseData();
                    if (list != null) {
                        listRV.clear();
                        if (list.size() < AppConstants.PAGE_SIZE)
                            isDataFinished = true;
                        listRV.addAll(list);
                        PlayerSelectionListener listener = OlaPlayerDetailsReportFragment.this::redirectToPlayerSearchFragment;
                        adapter = new OlaPlayerDetailsAdapter(getContext(), listRV, listener);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                        rvReport.setLayoutManager(mLayoutManager);
                        rvReport.setItemAnimator(new DefaultItemAnimator());
                        rvReport.setAdapter(adapter);
                    }
                }
            } else {
                if (Utils.checkForSessionExpiry(master, response.getResponseCode())) return;

                String errorMsg = TextUtils.isEmpty(response.getResponseMessage()) ? getString(R.string.some_internal_error) : response.getResponseMessage();
                Utils.showCustomErrorDialog(getContext(), getString(R.string.player_details), errorMsg);
            }
        }

        @Override
        public void onLoadMoreResponse(OlaPlayerDetailsResponseBean response) {
            if (response == null) {
                loadMoreAfterApiResponse(null);
                Toast.makeText(master, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
            else if (response.getResponseCode() == 0) {
                if (response.getResponseData() != null && response.getResponseData().size() < 1) {
                    Toast.makeText(master, getString(R.string.no_more_data), Toast.LENGTH_SHORT).show();
                    isDataFinished = true;
                    loadMoreAfterApiResponse(null);
                }
                else {
                    if (Objects.requireNonNull(response.getResponseData()).size() < AppConstants.PAGE_SIZE)
                        isDataFinished = true;
                    loadMoreAfterApiResponse(response.getResponseData());
                }
            } else {
                loadMoreAfterApiResponse(null);
                if (Utils.checkForSessionExpiry(master, response.getResponseCode())) return;

                String errorMsg = TextUtils.isEmpty(response.getResponseMessage()) ? getString(R.string.some_internal_error) : response.getResponseMessage();
                Toast.makeText(master, errorMsg, Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public void onDestroy() {
        ProgressBarDialog.getProgressDialog().dismiss();
        Utils.dismissCustomErrorDialog();
        super.onDestroy();
    }
}