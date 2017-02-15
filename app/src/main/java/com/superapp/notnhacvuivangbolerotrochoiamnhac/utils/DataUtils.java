package com.superapp.notnhacvuivangbolerotrochoiamnhac.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.google.gson.Gson;
import com.superapp.notnhacvuivangbolerotrochoiamnhac.model.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by ManhNV on 7/23/16.
 */
public class DataUtils<T> {
    private static DataUtils INSTANCE =null;
    private int mCurrentLanguage;
    private HashMap<Integer,Question> questionHashMap;
    private List<Question> questions;
    private Context mContext;
    private boolean isEnableSound;
//    private static final String bundleId = this.getPa


    private DataUtils(Context context){
        mContext = context;
        CreateListLanguage(getJsonObject(context));


    }

    public boolean isEnableSound() {
        return isEnableSound;
    }

    public void setIsEnableSound(boolean isEnableSound) {
        this.isEnableSound = isEnableSound;
    }

    public static synchronized DataUtils getINSTANCE(Context context){
        if (INSTANCE==null){
            INSTANCE = new DataUtils(context);
        }
        return INSTANCE;
    }



    private String getJsonObject(Context context){
        String json = "";

        Log.d("DataUtils", "Get JSON data language");
        json = loadJSONFromAsset("json/questions.json",context);
        return json;
    }

    private String loadJSONFromAsset(String jsonFile,Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(jsonFile);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
    private void CreateListLanguage(String json){
        questionHashMap = new HashMap<Integer,Question>();
        questions = new ArrayList<Question>();
        List<Question> result = new ArrayList<Question>();
        try {
            JSONObject jsonArrObject = new JSONObject(json);
            JSONArray jsonArr = jsonArrObject.getJSONArray("questions");
            JSONObject jsonObj = null;
            Gson gson = new Gson();
            for (int i = 0; i < jsonArr.length(); i++) {
                jsonObj = jsonArr.getJSONObject(i);
                result.add(gson.fromJson(jsonObj.toString(), Question.class));
            }

            questions=result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public static int random(int min, int max){
        Random r = new Random();
        int ran = r.nextInt();
        ran = ran> 0 ? ran : ran*-1;
        ran = min + ran %(max-min+1);
        return ran;
    }

    public static void playSound(Context context,int soundResource){
        if (DataUtils.getINSTANCE(context).isEnableSound()) {
            MediaPlayer mp;
            mp = MediaPlayer.create(context, soundResource);
            mp.start();
        }
    }

}
