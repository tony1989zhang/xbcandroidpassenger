package com.lvchehui.www.xiangbc.utils;

import com.lvchehui.www.xiangbc.view.toast.ToastManager;

/**
 * Created by 张灿能 on 2016/6/15.
 * 作用： 计算车辆数量
 */
public class CalcUtil {

    public static Integer[] calCarCount(float peopleCount){
        int carCount ;
        int carCount2 ;

        Integer[] intCarCount = null;
        if (peopleCount > 162)
        {
            ToastManager.getManager().show("大订单");
        }
        else
        {
            if (peopleCount <= 54)
            {
                intCarCount = new Integer[1];
                carCount = calcCarSeat(peopleCount);
                intCarCount[0] = carCount;
            }
            else
            {
                if (peopleCount/2 > 54)
                {
                    intCarCount = new Integer[3];
                    carCount =  calcCarSeat(peopleCount / 3);
                    intCarCount[0] = carCount;
                    intCarCount[1] = carCount;
                    intCarCount[2] = carCount;
                }
                else
                {
                    intCarCount = new Integer[2];
                    carCount = calcCarSeat(peopleCount / 2);
                    carCount2 = calcCarSeat(peopleCount - carCount);
                    intCarCount[0] = carCount;
                    intCarCount[1] = carCount2;

                }
            }
        }
        return intCarCount;
    }
    private static int calcCarSeat(float count){

        float peopleCount = count;
        int carCount ;

        if (peopleCount <= 4)
        {
            carCount = 4;
        }
        else if  (peopleCount <= 6)
        {
            carCount = 6;
        }
        else if (peopleCount <= 9)
        {
            carCount = 9;
        }
        else if (peopleCount <= 12)
        {
            carCount = 12;
        }
        else if (peopleCount <= 15)
        {
            carCount = 15;
        }
        else if (peopleCount <= 18)
        {
            carCount = 18;
        }
        else if (peopleCount <= 21)
        {
            carCount = 21;
        }
        else if (peopleCount <= 24)
        {
            carCount = 24;
        }
        else if (peopleCount <= 28)
        {
            carCount = 28;
        }
        else if (peopleCount <= 34)
        {
            carCount = 34;
        }
        else if (peopleCount <= 38)
        {
            carCount = 38;
        }
        else if (peopleCount <= 46)
        {
            carCount = 46;
        }
        else if (peopleCount <= 50)
        {
            carCount = 50;
        }
        else
        {
            carCount = 54;
        }

        return carCount;
    }
}
