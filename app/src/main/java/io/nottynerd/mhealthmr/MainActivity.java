package io.nottynerd.mhealthmr;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import io.nottynerd.mhealthmr.notifications.MyFirebaseMessagingService;
import io.nottynerd.mhealthmr.utility.AppConstants;
import io.nottynerd.mhealthmr.utility.PostRatingTask;
import io.nottynerd.mhealthmr.utility.Rating;
import io.nottynerd.mhealthmr.utility.RegisterPushTask;
import io.nottynerd.mhealthmr.utility.Utilities;
import okhttp3.internal.Util;

public class MainActivity extends AppCompatActivity {

    String lastAlert="";
    private String pushedMessage=""; private String body="";
    private String msgType=""; private String title=""; String link ="";

    private TextView mDialogHeader;
    private TextView mDialogText;
    private TextView mDialogCompleteButton;
    private TextView mDialogSnoozeButton;
    private TextView mDialogDismissButton;
    private TextView rating_meaning;
    private ImageView mDialogImage;
    private RatingBar mRatingBar;
    int rating =1;
    Boolean enabled=true;
    String[] rating_description = {"Strongly disagree","Disagree","Somewhat disagree", "Neither agree or disagree", "Somewhat agree", "Agree", "Strongly agree"};

    Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        mDialogHeader = (TextView) findViewById(R.id.dialog_title);
        mDialogText = (TextView) findViewById(R.id.dialog_text);
        mDialogCompleteButton = (TextView) findViewById(R.id.dialog_complete);
        mDialogDismissButton = (TextView) findViewById(R.id.dialog_dismiss);
        mDialogSnoozeButton = (TextView) findViewById(R.id.dialog_snooze);
        mDialogImage = (ImageView) findViewById(R.id.dialog_image);
        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        rating_meaning =(TextView) findViewById(R.id.rating_meaning);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //To be implemented so we send rating, msg, type, token to server side
                if(TextUtils.isEmpty(Utilities.getPersistedData(Utilities.CONSTANT_EXT,MainActivity.this)))
                {
                    enabled = false;
                }

                if(enabled) {
                    Gson gs = new Gson();
                    Rating rt = new Rating();
                    rt.setMessage("" + mDialogText.getText());
                    rt.setMsgtype("1");
                    rt.setRating(rating);
                    rt.setUid(Utilities.getPersistedData(Utilities.GCM_REG_ID_KEY, MainActivity.this));
                    String body = gs.toJson(rt).toString();
                    Log.d("INFO", body);

                    new PostRatingTask(MainActivity.this, body).execute();
                }
                {
                    if(TextUtils.isEmpty(""+mDialogText.getText().toString())) {

                        Toast.makeText(MainActivity.this, "you have already rated this message!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                try {
                    Log.d("LOGGER", "Rating changed : " + v);
                    Log.d("LOGGER", "Rating changed : " + (int) ratingBar.getRating());

                    rating = (int) ratingBar.getRating();
                    if (rating > 1) {

                        rating_meaning.setText(rating_description[rating - 1]);
                        Log.d("LOGGER", "Rating Value" + rating + "#" + rating_description[rating - 1]);
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });



        showPush();
        initDialogButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void showPush()
    {
        pushedMessage = getIntent().getStringExtra("push");
        body = getIntent().getStringExtra("body");
        title = getIntent().getStringExtra("title");
        link = getIntent().getStringExtra("link");

        Log.d("PUSH",""+pushedMessage);
        Log.d("PUSH",""+body);
        Log.d("PUSH",""+title);Log.d("PUSH",""+link);
        if(!TextUtils.isEmpty(pushedMessage)){
            Utilities.savePersistedData(Utilities.CONSTANT_EXT,pushedMessage,this);
            Picasso.with(this).load(link).into(mDialogImage);
            showMessageDialog(pushedMessage,"Alert!");
        }
        else
        {
            if(!TextUtils.isEmpty(Utilities.getPersistedData(Utilities.CONSTANT_EXT,MainActivity.this)))
            {
                showMessageDialog(Utilities.getPersistedData(Utilities.CONSTANT_EXT,this),"Last Message Received!");
            }
            else {
                showMessageDialog(Utilities.getPersistedData(Utilities.CONSTANT_EXT, this), "You Have Rated The Last Received Message!");
            }
        }
    }

    public void showMessageDialog(String pushMessage, String titleText)
    {

        mDialogText.setText(pushMessage);
        mDialogHeader.setText(titleText);


    }

    public void  initDialogButtons()
    {
        mDialogCompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Are you sure you COMPLETED the task?", Snackbar.LENGTH_LONG)
                        .setAction("Yup!", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }).show();
            }
        });

        mDialogDismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "OK Dismissed", Snackbar.LENGTH_LONG).show();
            }
        });
        mDialogSnoozeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Snoozing...", Snackbar.LENGTH_LONG)
                        .setAction("SURE", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }).show();
            }
        });
    }


}
