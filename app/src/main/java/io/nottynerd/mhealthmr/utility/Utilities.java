package io.nottynerd.mhealthmr.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.preference.PreferenceManager;

import java.io.InputStream;
import java.lang.reflect.Field;

/**
 * Created by NottyNerd on 26/09/2017.
 */




        import android.content.Context;
        import android.content.SharedPreferences;
        import android.content.res.AssetManager;
        import android.preference.PreferenceManager;
        import android.util.Log;

        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.io.InputStream;
        import java.lang.reflect.Field;
        import java.util.List;



public class Utilities {

    public static final String REGISTER_PUSH_URL = "http://wimmerwebapi.azurewebsites.net/api/Uid?mobileId=" ;
 public static final String RATING_PUSH_URL = "http://wimmerwebapi.azurewebsites.net/api/Rating" ;
    public static final String PHONE_NUMBER = "PHONE_NUMBER";
    public static String SENT_TOKEN_TO_SERVER ="SENT_TOKEN_TO_SERVER";
    public static String GCM_REG_ID_KEY="GCM_REG_ID_KEY";


    public Utilities() {


    }

    public String getObject(Context ctx) {

        String text="";
        try {
            AssetManager assetManager = ctx.getAssets();
            InputStream inputStream = null;


            inputStream = assetManager.open("inspiration.json");
            if ( inputStream != null)
            {
                int siz = inputStream.available();
                byte[] buffer = new byte[siz];
                inputStream.read(buffer);
                inputStream.close();
                text = new String(buffer);
            }
            //   Log.d("INFO",text);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }



    public static int getResId(String variableNames, Context context,
                               Class<?> c) {

        int imageIds;
        if(variableNames.contains(".")){
            variableNames= variableNames.replace("R.drawable.","");
        }
        try {

            Field idField = c.getDeclaredField(variableNames);
            imageIds = idField.getInt(idField);

            return imageIds;
        } catch (Exception e) {
            e.printStackTrace();
            return 0x7f010013;
        }
    }


    public static String CONSTANT_EXT="/010101DCD101010";

    /**
     * Used to save Persisted Data
     *
     *
     */
    public static void savePersistedData( String key , String value, Context context) {

        SharedPreferences sharedPreferences = PreferenceManager

                .getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(key, value);

        editor.commit();

    }

    /**
     * Used to get Persisted Data
     *
     * */
    public static String getPersistedData(String key, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String value = sharedPreferences.getString(key, "");
        return value;
    }



}
