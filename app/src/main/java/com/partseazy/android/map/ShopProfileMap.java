package com.partseazy.android.map;


import com.partseazy.android.ui.model.catalogue.SortAttibute;
import com.partseazy.android.ui.model.supplier.WeekDay;
import com.partseazy.android.ui.model.supplier.shop.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by naveen on 14/4/17.
 */

public class ShopProfileMap {

    public static Map<String, String> weekDayMap;
    public static Map<String, String> shopFormatMap;
    public static Map<String, String> shopFloorMap;
    public static List<SortAttibute> sortList;

    public ShopProfileMap() {
        setWeekDaysMap();
        setShopFormatMap();
    }

    public static void setWeekDaysMap() {
        weekDayMap = new LinkedHashMap<>();
        weekDayMap.put("sun", "Sunday");
        weekDayMap.put("mon", "Monday");
        weekDayMap.put("tue", "Tuesday");
        weekDayMap.put("wed", "Wednesday");
        weekDayMap.put("thu", "Thursday");
        weekDayMap.put("fri", "Friday");
        weekDayMap.put("sat", "Saturday");
    }

    public static void setShopFormatMap() {
        shopFormatMap = new HashMap<>();
        shopFormatMap.put("branded", "Largely Branded");
        shopFormatMap.put("unbranded", "Largely Unbranded");
    }

    public static void setShopFloor() {
        shopFloorMap = new LinkedHashMap<>();
        shopFloorMap.put("-1", "Basement Floor");
        shopFloorMap.put("0", "Ground Floor");
        shopFloorMap.put("1", "First Floor");
        shopFloorMap.put("2", "Second Floor");
        shopFloorMap.put("3", "Third Floor");
        shopFloorMap.put("4", "Fourth Floor");
        shopFloorMap.put("5", "Fifth Floor");
    }

    public static List<String> getCategoryList(List<Category> categoryList) {
        List<String> resultList = new ArrayList<>();
        if (categoryList != null && categoryList.size() > 0) {
            for (Category category : categoryList) {
                if (category.categoryPath != null && category.categoryPath.size() > 0) {
                    if (category.categoryPath.size() == 3) {
                        resultList.add(category.categoryPath.get(2).name);
                    } else if (category.categoryPath.size() == 2) {
                        resultList.add(category.categoryPath.get(1).name);
                    }
                }
            }
        }
        return resultList;
    }

    public static List<WeekDay> getWeekDays(String day) {
        setWeekDaysMap();
        List<WeekDay> dayList = new ArrayList<>();
        for (Map.Entry<String, String> entry : weekDayMap.entrySet()) {
            WeekDay weekDay = new WeekDay();
            weekDay.key = entry.getKey();
            weekDay.value = entry.getKey();
            if (entry.getKey().equals(day)) {
                weekDay.isSelected = true;
            }
            dayList.add(weekDay);
        }

        return dayList;
    }

    public static String getFootfallValue(String key) {
//        setFootfallMap();
        if (StaticMap.footfallMap != null && StaticMap.footfallMap.containsKey(key)) {
            return StaticMap.footfallMap.get(key);
        }
        return null;
    }

    public static String getShopSizeValue(String key) {
        //  setShopSizeMap();
        if (StaticMap.shopSizeMap != null && StaticMap.shopSizeMap.containsKey(key)) {
            return StaticMap.shopSizeMap.get(key);
        }
        return null;
    }

    public static String getTurnOverValue(String key) {
        // setTurnOverMap();
        if (StaticMap.turnOverMap != null && StaticMap.turnOverMap.containsKey(key)) {
            return StaticMap.turnOverMap.get(key);
        }
        return null;
    }

    public static String getShopFormatValue(String key) {
        setShopFormatMap();
        if (shopFormatMap != null && shopFormatMap.containsKey(key)) {
            return shopFormatMap.get(key);
        }
        return null;
    }

    public static String getShopFloorValue(String key) {
        setShopFloor();
        if (shopFloorMap != null && shopFloorMap.containsKey(key)) {
            return shopFloorMap.get(key);
        }
        return null;
    }

    public static List<SortAttibute> getSortList() {
        sortList = new ArrayList<>();
        sortList.add(new SortAttibute("size_asc", "Size : Low to High"));
        sortList.add(new SortAttibute("size_desc", "Size : High to Low"));
        sortList.add(new SortAttibute("footfall_asc", "Footfall : Low to High"));
        sortList.add(new SortAttibute("footfall_desc", "Footfall : High to Low"));
        sortList.add(new SortAttibute("turnover_asc", "Turnover : Low to High"));
        sortList.add(new SortAttibute("turnover_desc", "Turnover : High to Low"));
        return sortList;

    }

}

