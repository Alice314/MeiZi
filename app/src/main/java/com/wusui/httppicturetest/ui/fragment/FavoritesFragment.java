package com.wusui.httppicturetest.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wusui.httppicturetest.R;

/**
 * Created by fg on 2016/3/27.
 */
public class FavoritesFragment extends Fragment{
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstance){
        super.onCreateView(inflater, container, savedInstance);
        View favoritesView = inflater.inflate(R.layout.fragment_favorites,container,false);
        return favoritesView;
    }

    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }
}
