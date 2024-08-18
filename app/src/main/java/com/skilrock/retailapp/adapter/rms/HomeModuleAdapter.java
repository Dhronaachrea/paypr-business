package com.skilrock.retailapp.adapter.rms;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skilrock.retailapp.R;
import com.skilrock.retailapp.interfaces.HomeModuleListener;
import com.skilrock.retailapp.models.rms.HomeDataBean;
import com.skilrock.retailapp.utils.StringMapOla;
import com.skilrock.retailapp.utils.Utils;

import java.util.List;

public class HomeModuleAdapter extends RecyclerView.Adapter<HomeModuleAdapter.HomeModuleViewHolder> {

    private List<HomeDataBean.ResponseData.ModuleBeanList> moduleBeanLists;
    private HomeModuleListener homeModuleListener;
    private Context context;

    public HomeModuleAdapter(Context context, List<HomeDataBean.ResponseData.ModuleBeanList> moduleBeanList, HomeModuleListener homeModuleListener) {
        this.moduleBeanLists    = moduleBeanList;
        this.homeModuleListener = homeModuleListener;
        this.context            = context;
    }

    public class HomeModuleViewHolder extends RecyclerView.ViewHolder{

        public TextView tvCaption;
        public ImageView ivIcon;
        LinearLayout llContainer;

        public HomeModuleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCaption   = itemView.findViewById(R.id.tv_module);
            ivIcon      = itemView.findViewById(R.id.ivIcon);
            llContainer = itemView.findViewById(R.id.ll_container);

            llContainer.setOnClickListener(v -> {
                Utils.vibrate(context);
                HomeDataBean.ResponseData.ModuleBeanList moduleBeanList = moduleBeanLists.get(getAdapterPosition());
                homeModuleListener.onHomeModuleClicked(moduleBeanList.getModuleCode(), getAdapterPosition(), StringMapOla.getCaption(moduleBeanList.getModuleCode(), moduleBeanList.getDisplayName()), moduleBeanList.getMenuBeanList());
            });
        }
    }

    @NonNull
    @Override
    public HomeModuleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.game_list_type, viewGroup, false);
        return new HomeModuleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeModuleViewHolder holder, final int position) {
        HomeDataBean.ResponseData.ModuleBeanList moduleBeanList = moduleBeanLists.get(position);
        holder.tvCaption.setText(StringMapOla.getCaption(moduleBeanList.getModuleCode(), moduleBeanList.getDisplayName()));
        if (moduleBeanList.getModuleCode() != null) {
//            if (moduleBeanList.getModuleCode().trim().equalsIgnoreCase(AppConstants.DRAW_GAME))
//                holder.ivIcon.setBackgroundResource(R.drawable.icon_draw_game);
//            else if (moduleBeanList.getModuleCode().trim().equalsIgnoreCase(AppConstants.SCRATCH))
//                holder.ivIcon.setBackgroundResource(R.drawable.icon_scratch);
//            else if (moduleBeanList.getModuleCode().trim().equalsIgnoreCase(AppConstants.OLA))
//                holder.ivIcon.setBackgroundResource(R.drawable.group_1595);
//            else if (moduleBeanList.getModuleCode().trim().equalsIgnoreCase(AppConstants.BET_GAME))
//                holder.ivIcon.setBackgroundResource(R.drawable.icon_live_bet);
//            else if (moduleBeanList.getModuleCode().trim().equalsIgnoreCase(AppConstants.SLE))
//                holder.ivIcon.setBackgroundResource(R.drawable.sle);
//            else if (moduleBeanList.getModuleCode().trim().equalsIgnoreCase(AppConstants.CAMWIN247))
//                holder.ivIcon.setBackgroundResource(R.drawable.camwin_247_icon);
//            else if (moduleBeanList.getModuleCode().trim().equalsIgnoreCase(AppConstants.SBS))
//                holder.ivIcon.setBackgroundResource(R.drawable.icon_sports_betting);
//            else if (moduleBeanList.getModuleCode().trim().equalsIgnoreCase(AppConstants.VIRTUAL_SPORTS))
//                holder.ivIcon.setBackgroundResource(R.drawable.icon_virtual_sports);
        }
    }

    @Override
    public int getItemCount() {
        return moduleBeanLists.size();
    }

}
