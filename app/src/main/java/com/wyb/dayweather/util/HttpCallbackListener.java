package com.wyb.dayweather.util;



public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);

}
