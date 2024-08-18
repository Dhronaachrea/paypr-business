package com.skilrock.retailapp.ui.activities.rms;

import android.Manifest;
import android.app.DownloadManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.skilrock.retailapp.BuildConfig;
import com.skilrock.retailapp.R;
import com.skilrock.retailapp.dialog.AppVersionDialog;
import com.skilrock.retailapp.dialog.LanguageSelectionDialog;
import com.skilrock.retailapp.interfaces.AppVersionListener;
import com.skilrock.retailapp.interfaces.EventListener;
import com.skilrock.retailapp.models.VersionBean;
import com.skilrock.retailapp.models.rms.LoginBean;
import com.skilrock.retailapp.ui.activities.MainActivity;
import com.skilrock.retailapp.utils.LocaleHelper;
import com.skilrock.retailapp.utils.PlayerData;
import com.skilrock.retailapp.utils.ProgressBarDialog;
import com.skilrock.retailapp.utils.SharedPrefUtil;
import com.skilrock.retailapp.utils.Utils;
import com.skilrock.retailapp.viewmodels.SplashScreenViewModel;

import java.io.File;
import java.util.Objects;

public class SplashScreenActivity extends AppCompatActivity {

    private SplashScreenViewModel viewModel;
    private final String RMS = "rms";
    private VersionBean.ResponseData responseData = null;
    private  long downloadId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_splash_paypr_portrait);
        //setContentView(R.layout.activity_splash_cameroon_portrait);
        viewModel = ViewModelProviders.of(this).get(SplashScreenViewModel.class);
        viewModel.getLiveData().observe(this, observer);

        initializeWidgets();
        //Log.d("log", "IMEI Number: " + Utils.getImeiNumber(this));
        //Log.d("log", "Device ID: " + Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, SharedPrefUtil.getLanguage(base)));
    }
    
    private void initializeWidgets() {
        ImageView imageLogo = findViewById(R.id.image_logo);
        TextView tvVersion = findViewById(R.id.tvVersion);

        //imageLogo.setImageDrawable(Utils.getLogo(this, BuildConfig.app_name));
        tvVersion.setText(Utils.getAppVersionName(SplashScreenActivity.this));

        if (!BuildConfig.verify_pos_required) {
            viewModel.getAppVersion();
            getScreenSize();
        } else {
            getScreenSize();
            setDelay();
        }
    }

    Observer<VersionBean> observer = response -> {
        Log.e("log", "Response: " + response);
        if (response == null)
            Utils.showCustomErrorDialogAndFinish(SplashScreenActivity.this, BuildConfig.app_name, getString(R.string.something_went_wrong));
        else if (response.getResponseCode() == 0) {
            responseData = response.getResponseData();
            if (responseData.getStatusCode() == 0) {
                performVersioningOperation(responseData.getData());
            } else {
                String errorMsg = Utils.getResponseMessage(SplashScreenActivity.this, RMS, responseData.getStatusCode());
                Utils.showCustomErrorDialogAndFinish(SplashScreenActivity.this, BuildConfig.app_name, errorMsg);
            }
        } else {
            String errorMsg = Utils.getResponseMessage(SplashScreenActivity.this, RMS, response.getResponseCode());
            Utils.showCustomErrorDialogAndFinish(SplashScreenActivity.this, BuildConfig.app_name, errorMsg);
        }
    };

    private void performVersioningOperation(VersionBean.ResponseData.Data data) {
        int versionFromApi = Integer.parseInt(data.getAppVersion().replaceAll("\\.", ""));
        int versionFromApp = Utils.getAppVersionCode(SplashScreenActivity.this);

        if (versionFromApi > versionFromApp) {
            if (data.getDownloadMode().equalsIgnoreCase("FORCED"))
                showUpdateDialog(true, data.getDownloadPath(), data.getAppRemark());
            else if (data.getDownloadMode().equalsIgnoreCase("OPTIONAL"))
                showUpdateDialog(false, data.getDownloadPath(), data.getAppRemark());
            else
                Utils.showCustomErrorDialogAndFinish(SplashScreenActivity.this, BuildConfig.app_name, getString(R.string.technical_error));
        } else {
            setDelay();
        }
    }

    private void showUpdateDialog(boolean isUpdateForceFul, String url, String message) {
        AppVersionListener listener = this::onUpdateSelected;

        AppVersionDialog dialog = new AppVersionDialog(SplashScreenActivity.this, isUpdateForceFul, url, message, listener);
        dialog.show();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }

    private void onUpdateSelected(boolean isNow, String url) {
        if (isNow) {
            if (isWriteStoragePermissionGranted()) downloadApk(url);
        } else setDelay();
    }

    private void downloadApk(String url) {
        if (Utils.getModelCode().equalsIgnoreCase("NA")) {
            downloadUpdate(url);
        } else {
            downloadUpdate(url);
        }
    }

    public void downloadUpdate(String url) {
        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
        String fileName = "updated.apk";
        destination += fileName;
        final Uri uri = Uri.parse("file://" + destination);

        File file = new File(destination);
        if (file.exists())
            file.delete();

        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(url));
        request.setDestinationUri(uri);
        downloadId =  dm.enqueue(request);
        ProgressBarDialog.getProgressDialog().showProgressWithText(
                SplashScreenActivity.this, getString(R.string.updating_app_title), downloadId, dm);

        final String finalDestination = destination;
        final BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                //mProgressDialog.dismiss();
                ProgressBarDialog.getProgressDialog().dismiss();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Uri contentUri = FileProvider.getUriForFile(ctxt, BuildConfig.APPLICATION_ID + ".provider", new File(finalDestination));
                    Intent openFileIntent = new Intent(Intent.ACTION_VIEW);
                    openFileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    openFileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    openFileIntent.setData(contentUri);
                    startActivity(openFileIntent);
                    unregisterReceiver(this);
                    finish();
                } else {
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    install.setDataAndType(uri,
                            "application/vnd.android.package-archive");
                    startActivity(install);
                    unregisterReceiver(this);
                    finish();
                }
            }
        };
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void setDelay() {
        Handler handler = new Handler();
        int SPLASH_TIME_OUT = 1000;
        handler.postDelayed(() -> {
            if (BuildConfig.app_name.equalsIgnoreCase(getString(R.string.app_name_sisal)) && SharedPrefUtil.getLanguage(this).isEmpty()) {
                Log.w("log", "App Name: " + BuildConfig.app_name);
                Log.w("log", "App Language: " + SharedPrefUtil.getString(this, SharedPrefUtil.APP_LANGUAGE));
                EventListener listener = this::proceedToLogin;
                LanguageSelectionDialog dialog = new LanguageSelectionDialog(SplashScreenActivity.this, listener);
                dialog.show();
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                }
            } else {
                proceedToLogin();
            }
        }, SPLASH_TIME_OUT);
    }

    private void proceedToLogin() {
        if (SharedPrefUtil.getString(this, SharedPrefUtil.AUTH_TOKEN).isEmpty()) {
            Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            PlayerData.getInstance().setLoginData(new Gson().fromJson(SharedPrefUtil.getString(this, SharedPrefUtil.LOGIN_BEAN), LoginBean.class));

            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.i("log", "Permission is granted2");
                return true;
            } else {
                Log.v("log", "Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        } else {
            //permission is automatically granted on sdk<23 upon installation
            Log.v("log", "Permission is granted2");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                Log.d("log", "External storage2");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v("log", "Permission: " + permissions[0] + " was " + grantResults[0]);
                    if (responseData != null)
                        downloadApk(responseData.getData().getDownloadPath());
                }
                break;

            case 3:
                Log.d("log", "External storage1");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v("log", "Permission: " + permissions[0] + " was " + grantResults[0]);

                }
                break;
        }
    }

    private void getScreenSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        Log.i("log", "Screen Width: " + width);
        Log.i("log", "Screen Height: " + height);
        Log.i("log", "Model Code: " + Utils.getModelCode());
        Log.i("log", "Device Name: " + Utils.getDeviceName());
        Log.i("log", "Device Serial Number: " + Utils.getDeviceSerialNumber());
        Log.w("log", "Version Id: " + BuildConfig.versionId);
    }
}