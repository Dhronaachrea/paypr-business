package com.skilrock.retailapp.adapter.rms;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skilrock.retailapp.R;
import com.skilrock.retailapp.interfaces.ModuleListener;
import com.skilrock.retailapp.models.rms.HomeDataBean;
import com.skilrock.retailapp.utils.AppConstants;
import com.skilrock.retailapp.utils.StringMapOla;

import java.util.List;

public class ScratchAndDrawGameModuleAdapter extends RecyclerView.Adapter<ScratchAndDrawGameModuleAdapter.ScratchAndDrawGameModuleViewHolder> {

    private List<HomeDataBean.ResponseData.ModuleBeanList.MenuBeanList> listMenus;
    private ModuleListener moduleListener;

    public ScratchAndDrawGameModuleAdapter(List<HomeDataBean.ResponseData.ModuleBeanList.MenuBeanList> listMenus, ModuleListener moduleListener) {
        this.listMenus = listMenus;
        this.moduleListener = moduleListener;
    }

    public class ScratchAndDrawGameModuleViewHolder extends RecyclerView.ViewHolder{

        public TextView tvCaption;
        public ImageView ivIcon;
        LinearLayout llContainer;

        public ScratchAndDrawGameModuleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCaption   = itemView.findViewById(R.id.tv_scratch_menu);
            ivIcon      = itemView.findViewById(R.id.ivIcon);
            llContainer = itemView.findViewById(R.id.ll_container);

            llContainer.setOnClickListener(v -> {
                HomeDataBean.ResponseData.ModuleBeanList.MenuBeanList menuBeanList = listMenus.get(getAdapterPosition());
                moduleListener.onModuleClicked(menuBeanList.getMenuCode(), getAdapterPosition(), menuBeanList);
            });
        }
    }

    @NonNull
    @Override
    public ScratchAndDrawGameModuleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.scratch_menu, viewGroup, false);
        return new ScratchAndDrawGameModuleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScratchAndDrawGameModuleViewHolder holder, int position) {
        HomeDataBean.ResponseData.ModuleBeanList.MenuBeanList menuBeanList = listMenus.get(position);
        holder.tvCaption.setText(StringMapOla.getCaption(menuBeanList.getMenuCode(), menuBeanList.getCaption()));
        if (menuBeanList.getMenuCode() != null) {
            if (menuBeanList.getMenuCode().trim().equalsIgnoreCase(AppConstants.OLA_REGISTER))
                holder.ivIcon.setBackgroundResource(R.drawable.icon_ola_registration);
            else if (menuBeanList.getMenuCode().trim().equalsIgnoreCase(AppConstants.OLA_DEPOSIT))
                holder.ivIcon.setBackgroundResource(R.drawable.icon_ola_deposit);
            else if (menuBeanList.getMenuCode().trim().equalsIgnoreCase(AppConstants.OLA_WITHDRAW))
                holder.ivIcon.setBackgroundResource(R.drawable.icon_ola_withdrawal);
            else if (menuBeanList.getMenuCode().trim().equalsIgnoreCase(AppConstants.OLA_PLR_SEARCH))
                holder.ivIcon.setBackgroundResource(R.drawable.icon_ola_player_search);
            else if (menuBeanList.getMenuCode().trim().equalsIgnoreCase(AppConstants.OLA_PLR_LEDGER))
                holder.ivIcon.setBackgroundResource(R.drawable.icon_ola_player_transaction);
            else if (menuBeanList.getMenuCode().trim().equalsIgnoreCase(AppConstants.OLA_PLR_DETAILS))
                holder.ivIcon.setBackgroundResource(R.drawable.icon_ola_player_details);
            else if (menuBeanList.getMenuCode().trim().equalsIgnoreCase(AppConstants.OLA_PLR_PASSWORD))
                holder.ivIcon.setBackgroundResource(R.drawable.icon_ola_forgot_password);
            else if (menuBeanList.getMenuCode().trim().equalsIgnoreCase(AppConstants.OLA_PLR_PENDING_TXN))
                holder.ivIcon.setBackgroundResource(R.drawable.icon_pending_transaction);
            else if (menuBeanList.getMenuCode().trim().equalsIgnoreCase(AppConstants.OLA_NET_GAMING_DETAILS))
                holder.ivIcon.setBackgroundResource(R.drawable.icon_net_gaming);
            else if (menuBeanList.getMenuCode().trim().equalsIgnoreCase(AppConstants.OLA_MY_PROMO))
                holder.ivIcon.setBackgroundResource(R.drawable.icon_share);
            else if (menuBeanList.getMenuCode().trim().equalsIgnoreCase(AppConstants.PAY_PR_CHANGE_PASSWORD))
                holder.ivIcon.setBackgroundResource(R.drawable.ic_change_pin);
            else if (menuBeanList.getMenuCode().trim().equalsIgnoreCase(AppConstants.PAY_PR_DISPLAY_QR_CODE))
                holder.ivIcon.setBackgroundResource(R.drawable.ic_disp_qr_code);
            else if (menuBeanList.getMenuCode().trim().equalsIgnoreCase(AppConstants.PAY_PR_DEPOSIT_REPORT))
                holder.ivIcon.setBackgroundResource(R.drawable.ic_deposit);
            else if (menuBeanList.getMenuCode().trim().equalsIgnoreCase(AppConstants.PAY_PR_LEDGER_REPORT))
                holder.ivIcon.setBackgroundResource(R.drawable.ic_ledger_report_paypr);
            else if (menuBeanList.getMenuCode().trim().equalsIgnoreCase(AppConstants.PAY_PR_PAYMENT_REPORT))
                holder.ivIcon.setBackgroundResource(R.drawable.ic_pay_pr_payment);
            else if (menuBeanList.getMenuCode().trim().equalsIgnoreCase(AppConstants.PAY_PR_PAYOUT))
                holder.ivIcon.setBackgroundResource(R.drawable.ic_payout);

            else
                holder.ivIcon.setBackgroundResource(R.drawable.icon_draw_game);
        }
    }

    @Override
    public int getItemCount() {
        return listMenus.size();
    }
}
