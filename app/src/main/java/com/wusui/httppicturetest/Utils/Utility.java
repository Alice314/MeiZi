package com.wusui.httppicturetest.Utils;


import com.google.gson.Gson;
import com.wusui.httppicturetest.model.Status;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by fg on 2016/3/25.
 */
public class Utility {
    private static List<String> urlList = new ArrayList<>();
    private static Status fromJson;
    private static List<Status.results> resultses;
    public static List<String> handlePictureResponse(String response){

        Gson gson = new Gson();
        fromJson = gson.fromJson(response,Status.class);
        resultses = fromJson.getResults();
        for (int i = 0; i < resultses.size(); i++) {
            urlList.add(resultses.get(i).getUrl());
        }
        return urlList;
    }
}
