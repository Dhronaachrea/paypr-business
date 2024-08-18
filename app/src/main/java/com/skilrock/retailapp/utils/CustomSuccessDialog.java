package com.skilrock.retailapp.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentManager;
import android.widget.LinearLayout;

import com.skilrock.retailapp.dialog.ConfirmationDialog;
import com.skilrock.retailapp.dialog.InfoDialog;
import com.skilrock.retailapp.dialog.SuccessDialog;
import com.skilrock.retailapp.dialog.WarningDialog;
import com.skilrock.retailapp.interfaces.ConfirmationListener;
import com.skilrock.retailapp.interfaces.DialogCloseListener;


public class CustomSuccessDialog {

    private static CustomSuccessDialog progresDialog;
    public Context context;
    private SuccessDialog dialog;
    private ConfirmationDialog confirmationDialog;
    InfoDialog infoDialog;
    WarningDialog warningDialog;


    private CustomSuccessDialog() {
    }

    public static synchronized CustomSuccessDialog getProgressDialog() {
        if (progresDialog == null) {
            progresDialog = new CustomSuccessDialog();
        }

        return progresDialog;
    }

    public void showCustomSuccessDialog(Context context, FragmentManager fragmentManager, String header, String message, int popCount) {
        dismiss();

        dialog = new SuccessDialog(context, fragmentManager, header, message, popCount);
        dialog.show();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }



    public void showCustomSuccessDialog(Context context, FragmentManager fragmentManager, String header, String message, int popCount, DialogCloseListener dialogCloseListener) {
        dismiss();
        dialog = new SuccessDialog(context, fragmentManager, header, message, popCount, dialogCloseListener);
        dialog.show();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }

    public void showCustomSuccessDialog(Context context, FragmentManager fragmentManager, String header, String message, int popCount, String buttonText) {
        dismiss();

        dialog = new SuccessDialog(context, fragmentManager, header, message, popCount, buttonText);
        dialog.show();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }

    void showCustomSuccessDialogAndLogout(Context context, FragmentManager fragmentManager, String header, String message, boolean isPerformLogout) {
        dismiss();

        dialog = new SuccessDialog(context, fragmentManager, header, message, isPerformLogout);
        dialog.show();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }

    void showCustomSuccessDialogAndLogout(Context context, String header, String message) {
        dismiss();

        dialog = new SuccessDialog(context, header, message);
        dialog.show();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }









    public void showCustomInfoDialog(Context context, FragmentManager fragmentManager, String header, String message, int popCount, String buttonText) {
        dismissInfo();

        infoDialog = new InfoDialog(context, fragmentManager, header, message, popCount, buttonText);
        infoDialog.show();
        if (infoDialog.getWindow() != null) {
            infoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            infoDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }


    void dismissConfirmation() {
        if (confirmationDialog != null && confirmationDialog.isShowing())
            confirmationDialog.cancel();
    }
    void showConfirmationDialog(Context context, boolean isResetPassword, String info, ConfirmationListener listener) {
        dismissConfirmation();

        confirmationDialog = new ConfirmationDialog(context, isResetPassword, info, listener);
        confirmationDialog.show();
        if (confirmationDialog.getWindow() != null) {
            confirmationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            confirmationDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }
    public void showCustomWarningDialog(Context context, FragmentManager fragmentManager, String header, String message, int popCount, String buttonText) {
        dismissWarning();

        warningDialog = new WarningDialog(context, fragmentManager, header, message, popCount, buttonText);
        warningDialog.show();
        if (warningDialog.getWindow() != null) {
            warningDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            warningDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing())
            dialog.cancel();
    }

    void dismissInfo() {
        if (infoDialog != null && infoDialog.isShowing())
            infoDialog.cancel();
    }

    void dismissWarning() {
        if (warningDialog != null && warningDialog.isShowing())
            warningDialog.cancel();
    }
}