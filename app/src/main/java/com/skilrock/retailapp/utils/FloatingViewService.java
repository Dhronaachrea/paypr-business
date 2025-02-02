package com.skilrock.retailapp.utils;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.skilrock.retailapp.BuildConfig;
import com.skilrock.retailapp.R;
import com.skilrock.retailapp.ui.activities.MainActivity;

public class FloatingViewService extends Service implements View.OnClickListener {

    private WindowManager mWindowManager;
    private View mFloatingView;
    private View collapsedView;
    private View expandedView;
    private ImageView imageIcon;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_REDELIVER_INTENT;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        try {
            mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_view, null);

            int LAYOUT_FLAG;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
            }
            final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    LAYOUT_FLAG,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

            mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            mWindowManager.addView(mFloatingView, params);

            collapsedView = mFloatingView.findViewById(R.id.layoutCollapsed);
            expandedView = mFloatingView.findViewById(R.id.layoutExpanded);
            imageIcon = mFloatingView.findViewById(R.id.collapsed_iv);

            imageIcon.setImageDrawable(Utils.getLogo(this, BuildConfig.app_name));

            expandedView.setOnClickListener(this);
            collapsedView.setOnClickListener(this);
            //adding an touchlistener to make drag movement of the floating widget
            mFloatingView.findViewById(R.id.relativeLayoutParent).setOnTouchListener(new View.OnTouchListener() {
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            initialX = params.x;
                            initialY = params.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            return true;

                        case MotionEvent.ACTION_UP:
                            //when the drag is ended switching the state of the widget
                            collapsedView.setVisibility(View.GONE);
                            expandedView.setVisibility(View.VISIBLE);
                            return true;

                        case MotionEvent.ACTION_MOVE:
                            //this code is helping the widget to move around the screen with fingers
                            params.x = initialX + (int) (event.getRawX() - initialTouchX);
                            params.y = initialY + (int) (event.getRawY() - initialTouchY);
                            mWindowManager.updateViewLayout(mFloatingView, params);
                            return true;
                    }
                    return false;
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
            super.onDestroy();
            if (mFloatingView != null) mWindowManager.removeView(mFloatingView);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutExpanded:
                //switching views
              /*  collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);*/
                break;

            case R.id.layoutCollapsed:
                //closing the widget
                stopSelf();
               /* Context context = getApplicationContext();
                Intent intent = new Intent(context, MainActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                try {
                    contentIntent.send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }*/
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                FloatingViewService.this.startActivity(intent);

                break;
        }
    }
}