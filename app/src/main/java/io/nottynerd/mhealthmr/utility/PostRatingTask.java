package io.nottynerd.mhealthmr.utility;

/**
 * Created by NottyNerd on 03/10/2017.
 */


        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.os.AsyncTask;
        import android.support.v7.app.AlertDialog;
        import android.text.TextUtils;
        import android.util.Log;
        import android.view.View;
        import android.widget.Toast;

        import io.nottynerd.mhealthmr.MainActivity;

/**
 * Created by Beloved Egbedion, beloved.egbedion@gmail.com on 09/06/2017.
 */





@SuppressWarnings("WakeLock")
public class PostRatingTask extends AsyncTask<String, String, String> {


    public Context ctx;
    private ProgressDialog mProgressDialog;
    String text;


    public PostRatingTask(Context context, String body) {
        super();
        this.ctx = context;

        this.body = body ;
    }

    String user_id,body;




    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(ctx);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    protected String doInBackground(String... arg0) {

        try {

            String url = Utilities.RATING_PUSH_URL;
            Log.i("INFO", "RATING" + url);

            text= new GPRSManager(ctx).postDirectWebRequest(url,body);

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
        //Do something fancy
        mProgressDialog.dismiss();
        mProgressDialog.cancel();
        Log.d("INFO",result);
        if (!TextUtils.isEmpty(result))
        {

            if(result.equalsIgnoreCase("Execution Successful"))
            {
                result ="Rating Submitted Successfully";
            }
            Toast.makeText(ctx,result,Toast.LENGTH_SHORT).show();
            Utilities.savePersistedData(Utilities.CONSTANT_EXT,"",ctx);
            Intent intent = new Intent(ctx, MainActivity.class);
            ctx.startActivity(intent);
//            new AlertDialog.Builder(ctx)
//                    .setMessage(result)
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//
//                        }
//                    })
//                    .show();
        }

    }




}







