package io.nottynerd.mhealthmr.utility;

/**
 * Created by NottyNerd on 03/10/2017.
 */


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

/**
 * Created by Beloved Egbedion, beloved.egbedion@gmail.com on 09/06/2017.
 */


import android.telephony.TelephonyManager;
import android.util.Log;

import io.nottynerd.mhealthmr.MainActivity;


@SuppressWarnings("WakeLock")
public class RegisterPushTask extends AsyncTask<String, String, String> {


    public Context ctx;
    private ProgressDialog prgrsDialg;
    String text;


    public RegisterPushTask(Context context, String token) {
        super();
        this.ctx = context;

        this.token = token ;
    }

    String user_id,token;
    String mobile_number;


    @Override
    protected void onPreExecute(){
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... arg0) {

        try {

            String url = Utilities.REGISTER_PUSH_URL + token+"~"+ getMobileNumber();
            Log.i("Reg", "GCM " + url);

            text= new GPRSManager(ctx).postDirectWebRequest(url);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            text = "fail";
        }

        return text;
    }

    @Override
    protected void onPostExecute(String result) {

        Utilities.savePersistedData(Utilities.GCM_REG_ID_KEY, token, ctx);
        Utilities.savePersistedData(Utilities.SENT_TOKEN_TO_SERVER, "true", ctx);


    }


    public String getMobileNumber()
    {
        mobile_number = Utilities.getPersistedData(Utilities.PHONE_NUMBER, ctx);
        return mobile_number;
    }


}







