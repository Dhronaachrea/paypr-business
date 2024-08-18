package com.skilrock.retailapp.adapter.rms;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.skilrock.retailapp.R;
import com.skilrock.retailapp.models.rms.PayPrPayoutReportResponseBean;
import com.skilrock.retailapp.utils.Utils;

import java.util.List;

public class PayPrReportAdapter extends RecyclerView.Adapter<PayPrReportAdapter.ViewHolder> {

    Context context;
    List<PayPrPayoutReportResponseBean.ResponseData.Data> data;

    public PayPrReportAdapter(Context context, List<PayPrPayoutReportResponseBean.ResponseData.Data> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.payout_report, viewGroup, false);

        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        PayPrPayoutReportResponseBean.ResponseData.Data dataList = data.get(position);
        viewHolder.tvBeneficiaryName.setText(dataList.getBeneficiaryName());
        viewHolder.tvAccountNo.setText(String.format("#%s", dataList.getAccountNo()));
        viewHolder.tvBankName.setText(dataList.getBankName());
        viewHolder.tvAmount.setText(dataList.getTxnValue());

        if (dataList.getCreatedAt().trim().contains(" ")) {
            viewHolder.tvTime.setText(Utils.formatDate(dataList.getCreatedAt().split(" ")[0]));
            viewHolder.tvDate.setText(Utils.formatTime(dataList.getCreatedAt().split(" ")[1]));
        }
        if (dataList.getStatus().equalsIgnoreCase("PAYMENT_DONE")) {
            viewHolder.tv_status.setText(context.getString(R.string.success));
            viewHolder.tv_status.setTextColor(context.getColor(R.color.status_green));
        } else if (dataList.getStatus().equalsIgnoreCase("REQUESTED")) {
            viewHolder.tv_status.setText(R.string.initiated);
            viewHolder.tv_status.setTextColor(context.getColor(R.color.status_initiated));
        }
        if (dataList.getRemark() != null)
            viewHolder.tvRemark.setText(dataList.getRemark());
        else
            viewHolder.tvRemark.setVisibility(View.INVISIBLE);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvBeneficiaryName, tvAccountNo, tvBankName, tvRemark, tvAmount, tv_status, tvTime, tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBeneficiaryName   = itemView.findViewById(R.id.tvBeneficiaryName);
            tvAccountNo         = itemView.findViewById(R.id.tvAccountNo);
            tvBankName          = itemView.findViewById(R.id.tvBankName);
            tvRemark            = itemView.findViewById(R.id.tvRemark);
            tvAmount            = itemView.findViewById(R.id.tvAmount);
            tv_status           = itemView.findViewById(R.id.tvStatus);
            tvTime              = itemView.findViewById(R.id.tvTime);
            tvDate              = itemView.findViewById(R.id.tvDate);
        }

    }
}
