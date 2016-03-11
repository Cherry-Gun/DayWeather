package com.wyb.dayweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wyb.dayweather.model.City;
import com.wyb.dayweather.model.County;
import com.wyb.dayweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 16/3/11.
 */
public class DayWeatherDB {

    //数据库的名称
    public static final String DB_NAME = "Day_Weather";

    //数据库的版本
    public static final int VERSION = 1;
    private static DayWeatherDB dayWeatherDB;
    private SQLiteDatabase db;

    //将构造方法私有化
    private DayWeatherDB(Context context) {
        DayWeatherOpenHelper dbHelper = new DayWeatherOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    //获取DayWeatherDB的实例的方法
    public synchronized static  DayWeatherDB getInstance(Context context) {
        if (dayWeatherDB == null) {
            dayWeatherDB = new DayWeatherDB(context);
        }
        return dayWeatherDB;
    }

    //将Province实例储存到数据库
    public void saveProvince(Province province) {
            if (province != null) {
                ContentValues values = new ContentValues();
                values.put("province_name", province.getProvinceName());
                values.put("province_dode", province.getProvinceCode());
                db.insert("Province", null, values);
            }
    }

    //从数据库读取全国所有省份（Province）的信息
    public List<Province> loadProvinces() {
        List<Province> list = new ArrayList<>();
        Cursor cursor = db.query("Province", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("provinceName")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("provinceCode")));
                list.add(province);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    //将City实例储存到数据库
    public void saveCity(City city) {
        if (city != null) {
            ContentValues values = new ContentValues();
            values.put("city_name", city.getCityName());
            values.put("city_dode", city.getCityCode());
            values.put("province_id", city.getProvinceId());
            db.insert("City", null, values);
        }
    }

    //从数据库读取某个省份下所有城市（City）的信息
    public List<City> loadCities(int provinceId) {
        List<City> list = new ArrayList<>();
        Cursor cursor = db.query("City", null, "province_id = ?", new String[]{String.valueOf(provinceId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(provinceId);
                list.add(city);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return  list;
    }

    //将County实例储存到数据库
    public void saveCounty(County county) {
        if (county != null) {
            ContentValues values = new ContentValues();
            values.put("countyName", county.getCountyName());
            values.put("countyCode", county.getCountyCode());
            values.put("cityId", county.getCityId());
            db.insert("County", null, values);
        }
    }

    //从数据库读取某个城市下所有村镇（County）的信息
    public List<County> loadCounties(int cityId) {
        List<County> list = new ArrayList<>();
        Cursor cursor = db.query("County", null, "city_id = ?", new String[]{String.valueOf(cityId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityId(cityId);
                list.add(county);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

}
