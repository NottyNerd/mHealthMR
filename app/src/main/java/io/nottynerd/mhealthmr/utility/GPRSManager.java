package io.nottynerd.mhealthmr.utility;

import android.content.Context;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;


/**
 * Created by NottyNerd on 03/10/2017.
 */

class GPRSManager {
    Context context;

    public GPRSManager() {


    }

    public GPRSManager(Context context) {

        this();
        this.context = context;
    }





    public static String run( String url, String body ) throws IOException {
        OkHttpClient client = new OkHttpClient();
        //client.setConnectTimeout(EtzModel.TIMEOUT, TimeUnit.SECONDS); // connect timeout
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON, body);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();

        return response.body().string();

    }


    public static String run( String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        //client.setConnectTimeout(EtzModel.TIMEOUT, TimeUnit.SECONDS); // connect timeout

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();

        return response.body().string();

    }

    public static String postDirectWebRequest( String finalURL ) {

        try {
            String s = run( finalURL.trim());
            return s;
        }

        catch ( IOException e ) {
            //e.printStackTrace();
            //return e.getMessage();
            return "";
        }
        catch ( Exception e ) {
            //e.printStackTrace();
            //return e.getMessage();
            return "";
        }
    }


    public static String postDirectWebRequest( String finalURL, String body ) {

        try {
            String s = run( finalURL.trim(), body );
            return s;
        }

        catch ( IOException e ) {
            e.printStackTrace();
            //return e.getMessage();
            return "";
        }
        catch ( Exception e ) {
            e.printStackTrace();
            //return e.getMessage();
            return "";
        }
    }


}
