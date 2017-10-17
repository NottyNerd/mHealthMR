package io.nottynerd.mhealthmr;

import android.*;
import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import io.nottynerd.mhealthmr.utility.RegisterPushTask;
import io.nottynerd.mhealthmr.utility.Utilities;
import okhttp3.internal.Util;

public class Splash extends AppCompatActivity {


    public static final String SPLASH_SCREEN_OPTION = "io.nottynerd.innovate.Splash";
    public static final String SPLASH_SCREEN_OPTION_1 = "Option 1";
    public static final String SPLASH_SCREEN_OPTION_2 = "Option 2";
    public static final String SPLASH_SCREEN_OPTION_3 = "Option 3";

    String pushedMessage="";
    private ImageView mLogo;
    private TextView mText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE); //Removing ActionBar
        setContentView(R.layout.activity_splash);

        mLogo = (ImageView) findViewById(R.id.icon);
        mText = (TextView) findViewById(R.id.title);

        String category = SPLASH_SCREEN_OPTION_1;
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(SPLASH_SCREEN_OPTION)) {
            category = extras.getString(SPLASH_SCREEN_OPTION, SPLASH_SCREEN_OPTION_1);
        }
        setAnimation(category);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }


    }

    /** Animation depends on category.
     * */
    private void setAnimation(String category) {
        if (category.equals(SPLASH_SCREEN_OPTION_1)) {
            animation1();
        } else if (category.equals(SPLASH_SCREEN_OPTION_2)) {
            animation2();
        } else if (category.equals(SPLASH_SCREEN_OPTION_3)) {
            animation2();
        }
    }

    private void animation1() {
        ObjectAnimator scaleXAnimation = ObjectAnimator.ofFloat(mLogo, "scaleX", 5.0F, 1.0F);
        scaleXAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleXAnimation.setDuration(1200);
        ObjectAnimator scaleYAnimation = ObjectAnimator.ofFloat(mLogo, "scaleY", 5.0F, 1.0F);
        scaleYAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleYAnimation.setDuration(1200);
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(mLogo, "alpha", 0.0F, 1.0F);
        alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        alphaAnimation.setDuration(1200);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleXAnimation).with(scaleYAnimation).with(alphaAnimation);
        animatorSet.setStartDelay(500);
        animatorSet.start();

        ObjectAnimator scaleXTextAnimation = ObjectAnimator.ofFloat(mText, "scaleX", 5.0F, 1.0F);
        scaleXAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleXAnimation.setDuration(1200);
        ObjectAnimator scaleYTextAnimation = ObjectAnimator.ofFloat(mText, "scaleY", 5.0F, 1.0F);
        scaleYAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleYAnimation.setDuration(1200);
        ObjectAnimator alphaTextAnimation = ObjectAnimator.ofFloat(mText, "alpha", 0.0F, 1.0F);
        alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        alphaAnimation.setDuration(1200);
        AnimatorSet animatorTextSet = new AnimatorSet();
        animatorTextSet.play(scaleXTextAnimation).with(scaleYTextAnimation).with(alphaTextAnimation);
        animatorTextSet.setStartDelay(500);
        animatorTextSet.start();
    }

    private void animation2() {
        mLogo.setAlpha(1.0F);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.translate_top_to_center);
        mLogo.startAnimation(anim);
        mText.startAnimation(anim);

    }


    private void startEvent()
    {

        Thread timer=new Thread()
        {
            public void run()
            {
                try
                {
                    Log.d("INFO", ""+getMobileNumber());
                    sleep(4000);


                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    Intent openStartingPoint;
                    String regDet = "";

                    openStartingPoint=new Intent(getApplicationContext(),MainActivity.class);
                    openStartingPoint.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                    startActivity(openStartingPoint);
                    overridePendingTransition(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);
                }
            }

        };
        timer.start();


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                  startEvent();
                }
                break;

            default:
                break;
        }
    }

    public String getMobileNumber()
    {
        String mobile_number="";
        try {

            TelephonyManager c = (TelephonyManager) Splash.this.getSystemService(Splash.this.TELEPHONY_SERVICE);
            mobile_number = c.getLine1Number();

            Utilities.savePersistedData(Utilities.PHONE_NUMBER, mobile_number, getApplicationContext());
            //send request to wimmlab server
            new RegisterPushTask(this, Utilities.getPersistedData(Utilities.GCM_REG_ID_KEY, Splash.this)).execute();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return mobile_number;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!checkPermissions())
        {
            requestPermissions();
        }
        else
        {
            startEvent();
        }
    }
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }
    private void startPermissionRequest() {
        ActivityCompat.requestPermissions(Splash.this,
                new String[]{Manifest.permission.READ_PHONE_STATE},
                0);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_PHONE_STATE);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i("INFO", "Displaying permission rationale to provide additional context.");


                            startPermissionRequest();


        } else {
            Log.i("INFO", "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startPermissionRequest();
        }
    }



}
