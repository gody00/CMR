package com.sungshin.labsoon.cmr.Global;

import android.app.Application;

import com.sungshin.labsoon.cmr.NaverService.NaverService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by user on 2017-05-08.
 */

public class ApplicationController extends Application {
    private static final String naverURL = "https://openapi.naver.com/v1/search/";
    private static ApplicationController instance;

    private NaverService naverService;

    public ApplicationController() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (instance == null) {
            instance = new ApplicationController();
        }
        instance = this;

        //NaverService Build
        buildNaverService();

    }
    public static ApplicationController getInstance() {
        return instance;
    }
    public NaverService getNaverService() {
        return naverService;
    }

    private void buildNaverService() {
        Retrofit.Builder builder = new Retrofit.Builder();
        Retrofit retrofit = builder
                .baseUrl(naverURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        naverService = retrofit.create(NaverService.class);
    }

}



