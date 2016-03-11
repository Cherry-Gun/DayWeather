package com.wyb.dayweather.util;

import android.text.TextUtils;

import com.wyb.dayweather.db.DayWeatherDB;
import com.wyb.dayweather.model.City;
import com.wyb.dayweather.model.County;
import com.wyb.dayweather.model.Province;



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



}
