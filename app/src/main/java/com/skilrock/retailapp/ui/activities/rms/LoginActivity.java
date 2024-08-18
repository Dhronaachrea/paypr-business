package com.skilrock.retailapp.ui.activities.rms;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.dcastalia.localappupdate.DownloadApk;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.skilrock.retailapp.BuildConfig;
import com.skilrock.retailapp.R;
import com.skilrock.retailapp.dialog.AppVersionDialog;
import com.skilrock.retailapp.dialog.LanguageSelectionDialog;
import com.skilrock.retailapp.dialog.LanguageSelectionMyanmarDialog;
import com.skilrock.retailapp.interfaces.AppVersionListener;
import com.skilrock.retailapp.interfaces.ErrorDialogListener;
import com.skilrock.retailapp.interfaces.EventListener;
import com.skilrock.retailapp.interfaces.OverlayPermissionListener;
import com.skilrock.retailapp.models.rms.LoginBean;
import com.skilrock.retailapp.models.rms.TokenBean;
import com.skilrock.retailapp.models.rms.VerifyPosRequestBean;
import com.skilrock.retailapp.models.rms.VerifyPosResponseBean;
import com.skilrock.retailapp.permissions.AppPermissions;
import com.skilrock.retailapp.ui.WakeUpJobService;
import com.skilrock.retailapp.ui.activities.MainActivity;
import com.skilrock.retailapp.utils.AppConstants;
import com.skilrock.retailapp.utils.LocaleHelper;
import com.skilrock.retailapp.utils.LocationTrack;
import com.skilrock.retailapp.utils.LockNotificationView;
import com.skilrock.retailapp.utils.PlayerData;
import com.skilrock.retailapp.utils.ProgressBarDialog;
import com.skilrock.retailapp.utils.SharedPrefUtil;
import com.skilrock.retailapp.utils.Utils;
import com.skilrock.retailapp.viewmodels.rms.LoginViewModel;

import java.io.File;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, AppConstants {

    private LoginViewModel loginViewModel;
    private EditText etUsername, etPassword;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private TextView textLanguage;
    private Button btnLogin;
    private TextInputLayout layoutPassword, layoutUserName;
    private final String RMS = "rms";
    private ImageView imageLogo;
    private LinearLayout llLanguage;
    private LoginBean LOGIN_BEAN;
    static final int WRITE_EXST = 101;
    static final int READ_EXST = 102;
    static String updateUrl;
    LockNotificationView lockNotificationView;
    customViewGroupBottom customViewGroupBottom;
    WindowManager windowManager;
    private ProgressDialog mProgressDialog;
    private JobScheduler jobScheduler;
    private JobInfo jobInfo;
    private AppVersionDialog appVersionDialog;
    private  long downloadId;
    public LocationManager manager;
    private final int LOCATION_REQUEST_CODE = 2;
    public Location currentLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private double lat          = 0.0;
    private double longitude    = 0.0;
    private LocationTrack locationTrack;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getSupportActionBar()).hide();

        //overlay_display_devices
        if (SharedPrefUtil.getLanguage(LoginActivity.this).equalsIgnoreCase(AppConstants.LANGUAGE_ARABIC))
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        else
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        setContentView(R.layout.login_fragment_paypr);
        if (BuildConfig.app_name.equalsIgnoreCase(AppConstants.PAYPR_BUSINESS)) {
            if (!AppPermissions.CheckGPs(this)) {
                AppPermissions.turnOnGps(this, "GPS Permission");
            }
            if (!AppPermissions.checkPermissionLocation(this)) {
                AppPermissions.requestPermissionLocation(this);
            } else {
                getCurrentLocation();
            }
        }

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        lockNotificationView = new LockNotificationView(this);

        initializeWidgets();

        createJobScheduler();
    }

    private void createJobScheduler() {
        jobScheduler = (JobScheduler)getApplicationContext()
                .getSystemService(JOB_SCHEDULER_SERVICE);

        ComponentName componentName = new ComponentName(this,
                WakeUpJobService.class);

        jobInfo = new JobInfo.Builder(1, componentName)
                .setPersisted(true)
                .setBackoffCriteria(6000, JobInfo.BACKOFF_POLICY_LINEAR)
                .setMinimumLatency(1000 * 10)
                .build();
    }



    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, SharedPrefUtil.getLanguage(base)));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 001) {
            if (BuildConfig.app_name.equalsIgnoreCase(AppConstants.CAMEROON)) {
                if (!Settings.canDrawOverlays(this)) {
                    // You don't have permission

                } else {
                    // Do as per your logic
                    preventStatusBarExpansion(this);
                }

            }
        }

    }

    public void initializeWidgets() {
        etUsername      = findViewById(R.id.et_username);
        etPassword      = findViewById(R.id.et_password);
        layoutPassword  = findViewById(R.id.textInputLayoutPassword);
        layoutUserName  = findViewById(R.id.textInputLayoutUser);
        btnLogin        = findViewById(R.id.btn_login);
        llLanguage      = findViewById(R.id.view_language);
        textLanguage    = findViewById(R.id.tv_language);
        imageLogo       = findViewById(R.id.image_logo);

        mProgressDialog = new ProgressDialog(LoginActivity.this);
        if (Utils.getDeviceName().equalsIgnoreCase(AppConstants.DEVICE_T2MINI)) {
            Objects.requireNonNull(mProgressDialog.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            mProgressDialog.getWindow().setDimAmount(0.7f);
        }

        mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        mProgressDialog.setMessage("Updating app...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);

        //imageLogo.setImageDrawable(Utils.getLogo(this, BuildConfig.app_name));

        if (!TextUtils.isEmpty(SharedPrefUtil.getUserName(LoginActivity.this))) {
            etUsername.setText(SharedPrefUtil.getUserName(LoginActivity.this));
            etPassword.requestFocus();
        }

        if (BuildConfig.app_name.equalsIgnoreCase(AppConstants.ACDC))
            llLanguage.setVisibility(View.GONE);

        if (BuildConfig.app_name.equalsIgnoreCase(AppConstants.SISAL))
            Utils.setMaxLength(etPassword, 5);

        /*if (BuildConfig.app_name.equalsIgnoreCase(AppConstants.CAMEROON))
            Utils.setMaxLength(etPassword, 5);*/

        btnLogin.setOnClickListener(this);
        llLanguage.setOnClickListener(this);
        findViewById(R.id.forgot_password).setOnClickListener(this);

        etPassword.setOnEditorActionListener(this::onEditorAction);

        languageCallBack();
        loginViewModel.getLiveDataVerifyPos().observe(this, observerVerifyPos);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                Utils.vibrate(this);
                getCurrentLocation();
               if (validate()) {
                    Context context = LoginActivity.this;
                    if (SharedPrefUtil.getString(context, SharedPrefUtil.AUTH_TOKEN).isEmpty()) {
                        ProgressBarDialog.getProgressDialog().showProgress(LoginActivity.this);
                        loginViewModel.getLiveDataToken().observe(this, observerTokenBean);
                        loginViewModel.getLiveDataLogin().observe(this, observerLoginBean);

                        if (BuildConfig.app_name.equalsIgnoreCase(AppConstants.PAYPR_BUSINESS)) {
                            loginViewModel.callTokenApi(etUsername.getText().toString().trim(),
                                    etPassword.getText().toString().trim(), android.provider.Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID), Utils.getModelCode());
                        }
                        else {
                            loginViewModel.callTokenApi(etUsername.getText().toString().trim(),
                                    etPassword.getText().toString().trim(), Utils.getDeviceSerialNumber(), Utils.getModelCode());
                        }
                    }
                }
                break;

            case R.id.forgot_password:
                Utils.vibrate(LoginActivity.this);
                if (Utils.isNetworkConnected(LoginActivity.this)) {
                    Bundle bundle = new Bundle();

                    if (BuildConfig.app_name.equalsIgnoreCase(AppConstants.ACDC) || BuildConfig.app_name.equalsIgnoreCase(AppConstants.CAMEROON))
                        bundle.putString("title", getString(R.string.forgot_password_title));
                    else
                        bundle.putString("title", getString(R.string.forgot_pin_title));

                    Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else
                    Toast.makeText(LoginActivity.this, getString(R.string.check_internet_connection), Toast.LENGTH_LONG).show();
                break;
            case R.id.view_language:
                onChangeLanguage();
                break;
        }
    }

    private void checkInternetSpeed(){
        ConnectivityManager cm = (ConnectivityManager) LoginActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
        int downSpeed = nc.getLinkDownstreamBandwidthKbps();
        int upSpeed = nc.getLinkUpstreamBandwidthKbps();

        Log.d("UPSPEED", upSpeed + "");
        Log.d("DOWNSPEED", downSpeed + "");

    }

    Observer<TokenBean> observerTokenBean = new Observer<TokenBean>() {
        @Override
        public void onChanged(@Nullable TokenBean tokenBean) {
            ProgressBarDialog.getProgressDialog().dismiss();

            if (tokenBean == null)
                Utils.showCustomErrorDialog(LoginActivity.this, getString(R.string.login_error), getString(R.string.something_went_wrong));
            else if (tokenBean.getResponseCode() == 0) {
                TokenBean.ResponseData responseData = tokenBean.getResponseData();
                if (responseData.getStatusCode() == 0) {
                    ProgressBarDialog.getProgressDialog().showProgress(LoginActivity.this);
                    loginViewModel.callLoginApi("Bearer " + tokenBean.getResponseData().getAuthToken().trim());
                } else {
                    //String errorMsg = TextUtils.isEmpty(responseData.getMessage()) ? getString(R.string.problem_in_loggin_in) : responseData.getMessage();
                    String errorMsg = Utils.getResponseMessage(LoginActivity.this, RMS, responseData.getStatusCode());
                    Utils.showCustomErrorDialog(LoginActivity.this, getString(R.string.login_error), errorMsg);
                }
            } else {
                //String errorMsg = TextUtils.isEmpty(tokenBean.getResponseMessage()) ? getString(R.string.some_internal_error) : tokenBean.getResponseMessage();
                String errorMsg = Utils.getResponseMessage(LoginActivity.this, RMS, tokenBean.getResponseCode());
                Utils.showCustomErrorDialog(LoginActivity.this, getString(R.string.login_error), errorMsg);
            }
        }
    };

    Observer<LoginBean> observerLoginBean = new Observer<LoginBean>() {
        @Override
        public void onChanged(@Nullable LoginBean loginBean) {
            ProgressBarDialog.getProgressDialog().dismiss();
            ErrorDialogListener listener = LoginActivity.this::onDialogClosed;
            if (loginBean == null)
                Utils.showCustomErrorDialog(LoginActivity.this, getString(R.string.login_error), getString(R.string.something_went_wrong));
            else if (loginBean.getResponseCode() == 0) {
                LOGIN_BEAN = loginBean;
                LoginBean.ResponseData responseData = loginBean.getResponseData();
                if (responseData.getData().getIsAffiliate() != null && responseData.getData().getIsAffiliate().equalsIgnoreCase(AppConstants.NO)) {
                    if (responseData.getStatusCode() == 0) {
                        //PlayerData.getInstance().setLoginData(LoginActivity.this, loginBean);

                        ProgressBarDialog.getProgressDialog().showProgress(LoginActivity.this);
                        VerifyPosRequestBean model = new VerifyPosRequestBean();
                        if (BuildConfig.app_name.equalsIgnoreCase(AppConstants.PAYPR_BUSINESS)) {
                            model.setLatitudes(String.format("%s", lat));
                            model.setLongitudes(String.format("%s", longitude));
                        }else{
                            model.setLatitudes("0");
                            model.setLongitudes("0");
                        }

                        model.setSimType("MTN");
                        try {
                            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                            model.setVersion(pInfo.versionName);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                            model.setVersion("");
                        }

                        if (BuildConfig.app_name.equalsIgnoreCase(AppConstants.PAYPR_BUSINESS)) {
                            model.setTerminalId(android.provider.Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                        }
                        else
                            model.setTerminalId(Utils.getDeviceSerialNumber());
                        model.setModelCode(Utils.getModelCode());
                        /*model.setTerminalId("NA");
                        model.setModelCode("NA");*/

                      performVersioningOperation(null);

                    } else {
                        String errorMsg = Utils.getResponseMessage(LoginActivity.this, RMS, responseData.getStatusCode());
                        Utils.showCustomErrorDialog(LoginActivity.this, getString(R.string.login_error), errorMsg);
                    }
                }else {
                    String errorMsg = getString(R.string.is_affiliate_error);
                    Utils.showCustomErrorDialog(LoginActivity.this, getString(R.string.login_error), errorMsg, listener);
                }
            } else {
                String errorMsg = Utils.getResponseMessage(LoginActivity.this, RMS, loginBean.getResponseCode());
                Utils.showCustomErrorDialog(LoginActivity.this, getString(R.string.login_error), errorMsg);
            }
        }
    };

    private void onDialogClosed() {
        finish();
    }

    Observer<VerifyPosResponseBean> observerVerifyPos = verifyPosResponseBean -> {
        ProgressBarDialog.getProgressDialog().dismiss();

        if (verifyPosResponseBean == null) {
            PlayerData.getInstance().destroyInstance();
            SharedPrefUtil.clearAppSharedPref(LoginActivity.this);
            Utils.showCustomErrorDialog(LoginActivity.this, BuildConfig.app_name, getString(R.string.something_went_wrong));
        } else if (verifyPosResponseBean.getResponseCode() == 0) {
            if (verifyPosResponseBean.getResponseData().getStatusCode() == 0) {
                performVersioningOperation(verifyPosResponseBean.getResponseData().getData().getLatestVersion());
            } else {
                PlayerData.getInstance().destroyInstance();
                SharedPrefUtil.clearAppSharedPref(LoginActivity.this);
                String errorMsg = Utils.getResponseMessage(LoginActivity.this, RMS, verifyPosResponseBean.getResponseData().getStatusCode());
                Utils.showCustomErrorDialog(LoginActivity.this, BuildConfig.app_name, errorMsg);
            }
        } else {
            //performLogoutCleanUp
            PlayerData.getInstance().destroyInstance();
            SharedPrefUtil.clearAppSharedPref(LoginActivity.this);
            String errorMsg = Utils.getResponseMessage(LoginActivity.this, RMS, verifyPosResponseBean.getResponseCode());
            Utils.showCustomErrorDialog(LoginActivity.this, BuildConfig.app_name, errorMsg);
        }
    };

    private void onChangeLanguage() {
        Log.w("log", "App Name: " + BuildConfig.app_name);
        Log.w("log", "App Language: " + SharedPrefUtil.getString(this, SharedPrefUtil.APP_LANGUAGE));
        EventListener listener = this::languageCallBack;

        if (BuildConfig.app_name.equalsIgnoreCase(AppConstants.OLA_MYANMAR)) {
            LanguageSelectionMyanmarDialog dialog = new LanguageSelectionMyanmarDialog(LoginActivity.this, listener);
            dialog.show();
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }
        } else {
            LanguageSelectionDialog dialog = new LanguageSelectionDialog(LoginActivity.this, listener);
            dialog.show();
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }
        }
    }

    private void performVersioningOperation(VerifyPosResponseBean.ResponseData.Data.LatestVersion model) {
        //Log.e("log", "IS NULL: " + model.isEmpty());
        if (model == null || model.isEmpty()) {
            saveUserName(etUsername.getText().toString());
            PlayerData.getInstance().setLoginData(LoginActivity.this, LOGIN_BEAN);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            //startActivityForResult(intent, MAIN_ACTIVITY_CODE);
            startActivity(intent);
            finish();
        } else {
            if (model.getIsMandatory().equalsIgnoreCase("YES"))
                showUpdateDialog(true, model.getDownloadURL(), model.getAppRemark());
            else if (model.getIsMandatory().equalsIgnoreCase("NO"))
                showUpdateDialog(false, model.getDownloadURL(), model.getAppRemark());
            else {
                PlayerData.getInstance().destroyInstance();
                SharedPrefUtil.clearAppSharedPref(LoginActivity.this);
                Utils.showCustomErrorDialog(LoginActivity.this, BuildConfig.app_name, getString(R.string.technical_error));
            }
        }

        //int versionFromApi = Integer.parseInt(model.getVersion().replaceAll("\\.", ""));
        //int versionFromApp = Utils.getAppVersionCode(LoginActivity.this);

        /*if (versionFromApi > versionFromApp) {
            if (model.getIsMandatory().equalsIgnoreCase("YES"))
                showUpdateDialog(true, model.getDownloadURL(), model.getAppRemark());
            else if (model.getIsMandatory().equalsIgnoreCase("NO"))
                showUpdateDialog(false, model.getDownloadURL(), model.getAppRemark());
            else
                Utils.showCustomErrorDialogAndFinish(LoginActivity.this, BuildConfig.app_name, getString(R.string.technical_error));
        } else {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }*/
    }

    private void showUpdateDialog(boolean isUpdateForceFul, String url, String message) {
        AppVersionListener listener = this::onUpdateSelected;

        appVersionDialog = new AppVersionDialog(LoginActivity.this, isUpdateForceFul, url, message, listener);
        appVersionDialog.show();
        if (appVersionDialog.getWindow() != null) {
            appVersionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            appVersionDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void onUpdateSelected(boolean isNow, String url) {
        if (appVersionDialog != null) appVersionDialog.dismiss();

        updateUrl = url;
        if (isNow) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                if (isWriteStoragePermissionGranted()) {
                    downloadUpdate(updateUrl);
                }
               /* UpdateApp updateApp = new UpdateApp();
                updateApp.setContext(getApplicationContext());
                updateApp.execute(url);*/
            } else {
                askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXST);
                askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXST);
            }
        } else {
            saveUserName(etUsername.getText().toString());
            PlayerData.getInstance().setLoginData(LoginActivity.this, LOGIN_BEAN);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            //startActivityForResult(intent, MAIN_ACTIVITY_CODE);
            startActivity(intent);
            finish();
        }
    }

    private void downloadApk(String url) {
        DownloadApk downloadApk = new DownloadApk(LoginActivity.this);
        downloadApk.startDownloadingApk(url);
    }

    public boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.i("log", "Permission is granted2");
                return true;
            } else {
                Log.v("log", "Permission is revoked2");
                ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        } else {
            //permission is automatically granted on sdk<23 upon installation
            Log.v("log", "Permission is granted2");
            return true;
        }
    }

    private void languageCallBack() {
        switch (SharedPrefUtil.getLanguage(LoginActivity.this)) {
            case LANGUAGE_ARABIC:
                textLanguage.setText(AppConstants.ARABIC);
                break;
            case LANGUAGE_FRENCH:
                textLanguage.setText(AppConstants.FRENCH);
                break;
            case LANGUAGE_BURMESE:
                textLanguage.setText(AppConstants.BURMESE);
                break;
            case LANGUAGE_THAI:
                textLanguage.setText(AppConstants.THAI);
                break;
            default:
                textLanguage.setText(AppConstants.ENGLISH);
                break;
        }
    }

    private boolean validate() {
        if (BuildConfig.app_name.equalsIgnoreCase(AppConstants.CAMEROON)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Show alert dialog to the user saying a separate permission is needed
                // Launch the settings activity if the user prefers
                if (!Settings.canDrawOverlays(this)) {
                    OverlayPermissionListener listener = () -> {
                        Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        startActivityForResult(myIntent, 001);
                    };

                    return false;
                } else {
                    preventStatusBarExpansion(this);
                }
            } else {
                preventStatusBarExpansion(this);
            }
        }
        if (TextUtils.isEmpty(etUsername.getText().toString().trim())) {
            etUsername.setError(getString(R.string.enter_user_name));
            etUsername.requestFocus();
            layoutUserName.startAnimation(Utils.shakeError());
            return false;
        }
        if (TextUtils.isEmpty(etPassword.getText().toString().trim())) {
            if (BuildConfig.app_name.equalsIgnoreCase(AppConstants.ACDC) || BuildConfig.app_name.equalsIgnoreCase(AppConstants.CAMEROON)) {
                //etPassword.setError(getString(R.string.enter_password));
                Utils.showRedToast(LoginActivity.this, getString(R.string.enter_password));
            } else if (BuildConfig.app_name.equalsIgnoreCase(AppConstants.OLA_MYANMAR))
                Utils.showRedToast(LoginActivity.this, getString(R.string.enter_password));
            else
                etPassword.setError(getString(R.string.enter_pin));
            etPassword.requestFocus();
            layoutPassword.startAnimation(Utils.shakeError());
            return false;
        }
        if (!Utils.isNetworkConnected(LoginActivity.this)) {
            Toast.makeText(LoginActivity.this, getString(R.string.check_internet_connection), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("log", "~~~ LOGIN FRAGMENT: onDestroyView() ~~~");
        try {
            windowManager.removeView(lockNotificationView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        loginViewModel.getLiveDataToken().removeObserver(observerTokenBean);
        loginViewModel.getLiveDataLogin().removeObserver(observerLoginBean);
        loginViewModel.getLiveDataVerifyPos().removeObserver(observerVerifyPos);
    }

    private boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            btnLogin.performClick();
            return true;
        }
        return false;
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(LoginActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, permission)) {
                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(LoginActivity.this, new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(LoginActivity.this, new String[]{permission}, requestCode);
            }
        } else {

            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    ProgressBarDialog.getProgressDialog().showProgressWithText(LoginActivity.this, getString(R.string.updating_app_title));
                    downloadUpdate(updateUrl);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // PERMISSION GRANTED
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(this), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            AppPermissions.showMessageOKCancel(LoginActivity.this, getString(R.string.allow_permission), this);
                        }
                    } else {
                        // PERMISSION DENIED
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (ContextCompat.checkSelfPermission(Objects.requireNonNull(this), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                AppPermissions.showMessageOKCancel(LoginActivity.this, getString(R.string.allow_permission), this);
                            }
                        }
                        Utils.showRedToast(LoginActivity.this, "Permission denied to use Access Location");

                    }
                    break;

                }
            // other 'case' lines to check for other
            // permissions this app might request
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
                LoginActivity.this, getString(R.string.updating_app_title), downloadId, dm);

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

    public void preventStatusBarExpansion(Context context) {
        //Utils.showRedToast(LoginActivity.this, getString(R.string.notification_bar_activated));
        windowManager = ((WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE));

        Activity activity = (Activity) context;
        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |

                // this is to enable the notification to recieve touch events
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |

                // Draws over status bar
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        int resId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int result = 0;
        if (resId > 0) {
            result = activity.getResources().getDimensionPixelSize(resId);
        }

        localLayoutParams.height = result;
        localLayoutParams.format = PixelFormat.TRANSPARENT;
        try {
            windowManager.addView(lockNotificationView, localLayoutParams);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /*public static class CustomViewGroup extends ViewGroup {

        public CustomViewGroup(Context context) {
            super(context);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            Log.v("CustomViewGroup", "**********Intercepted");
            return true;
        }
    }*/

    public void preventBottomBarExpansion(Context context) {
        windowManager = ((WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE));

        Activity activity = (Activity) context;
        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.gravity = Gravity.BOTTOM;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |

                // this is to enable the notification to recieve touch events
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |

                // Draws over status bar
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        int resId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int result = 0;
        if (resId > 0) {
            result = activity.getResources().getDimensionPixelSize(resId);
        }

        localLayoutParams.height = result;

        localLayoutParams.format = PixelFormat.TRANSPARENT;

        customViewGroupBottom = new customViewGroupBottom(context);

        windowManager.addView(customViewGroupBottom, localLayoutParams);
    }


    public class customViewGroupBottom extends ViewGroup {

        Context context;

        public customViewGroupBottom(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            Log.v("CustomViewGroup", "**********Intercepted");
            Intent intent = new Intent(context, LoginActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NO_ANIMATION);

            context.startActivity(intent);
            customViewGroupBottom.setVisibility(GONE);
            return true;
        }
    }

    private void saveUserName(String name) {
        SharedPrefUtil.putUserName(LoginActivity.this, name);
    }


    @SuppressLint("MissingPermission")
    public void getCurrentLocation() {
        locationTrack = new LocationTrack(LoginActivity.this);
        if (locationTrack.canGetLocation()) {
             longitude  = locationTrack.getLongitude();
             lat        = locationTrack.getLatitude();
        }
    }

}

