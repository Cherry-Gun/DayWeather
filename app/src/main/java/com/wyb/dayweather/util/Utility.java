package com.wyb.dayweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.wyb.dayweather.db.DayWeatherDB;
import com.wyb.dayweather.model.City;
import com.wyb.dayweather.model.County;
import com.wyb.dayweather.model.Province;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Utility {


    //解析并处理服务器返回的省份数据，并将解析出的数据放入省份（Province）数据表当中
    public synchronized static boolean handleProvincesResponse(DayWeatherDB dayWeatherDB, String response) {

        if (!TextUtils.isEmpty(response)) {
            String[] allProvinces = response.split(",");
            if (allProvinces != null && allProvinces.length > 0) {
                for (String p : allProvinces) {
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    //将解析出的数据存储到省份（Province）数据表中
                    dayWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }


    //解析并处理服务器返回的城市数据，并将解析出的数据放入城市（City）数据表当中
    public static boolean handleCitiesResponse(DayWeatherDB dayWeatherDB, String response, int provinceId) {

        if (!TextUtils.isEmpty(response)) {
            String[] allaCities = response.split(",");
            if (allaCities != null && allaCities.length > 0) {
                for (String c : allaCities) {
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    //将解析出的数据存储到城市（City）数据表中
                    dayWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }


    //解析并处理服务器返回的城市数据，并将解析出的数据放入城市（City）数据表当中
    public static boolean handleCountiesResponse(DayWeatherDB dayWeatherDB, String response, int cityId) {

        if (!TextUtils.isEmpty(response)) {
            String[] allCounties = response.split(",");
            if (allCounties != null && allCounties.length > 0) {
                for (String c : allCounties) {
                    String[] array = c.split("\\|");
                    County county = new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
                    //将解析出的数据存储到乡镇（County）数据表中
                    dayWeatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }


    //解析服务器返回的JSON数据，并将解析出的数据储存到本地
    public static void handleWeatherResponse(Context context, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
            String cityName = weatherInfo.getString("city");
            String weatherCode = weatherInfo.getString("cityid");
            String temp1 = weatherInfo.getString("temp1");
            String temp2 = weatherInfo.getString("temp2");
            String weatherDesp = weatherInfo.getString("weather");
            String publishTime = weatherInfo.getString("ptime");
            saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp, publishTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //将服务器返回的所有天气信息储存到SharedPreferences文件中
    private static void saveWeatherInfo(Context context, String cityName, String weatherCode, String temp1, String temp2, String weatherDesp, String publishTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CANADA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected", true);
        editor.putString("city_name", cityName);
        editor.putString("weather_code", weatherCode);
        editor.putString("temp1", temp1);
        editor.putString("temp2", temp2);
        editor.putString("weather_desp", weatherDesp);
        editor.putString("publish_time", publishTime);
        editor.putString("current_date", sdf.format(new Date()));
        editor.commit();
    }



}
