package com.lvchehui.www.xiangbc.view.TextView.lotter;

import java.util.Random;


public class LotteryManage {
	public static LotteryInfo getRandomLottery() {
		LotteryInfo info = null;
		Random r = new Random();
		int num = r.nextInt(1000);
		if (num < 10) {
			info = new LotteryInfo(LotteryInfo.TYPE_FIRST, "200元",
					"我在幸运大转盘得了1等奖，吼吼！，亲们要一起来么，请访问http://www.4000592122.com/");
		} else if (num < 30) {
			info = new LotteryInfo(LotteryInfo.TYPE_SECOND, "100元",
					"我在幸运大转盘得了2等奖，吼吼！，亲们要一起来么，请访问：http://www.4000592122.com/");
		} else if (num < 100) {
			info = new LotteryInfo(LotteryInfo.TYPE_THIRD, "50元",
					"我在幸运大转盘得了3等奖，吼吼！，亲们要一起来么，请访问：http://www.4000592122.com/");
		} else {
			info = new LotteryInfo(LotteryInfo.TYPE_NONE, "再接再厉",
					"我在玩幸运大转盘，不过人品不太好，没中奖，亲们要一起来么，请访问：http://www.4000592122.com/");
		}
		return info;
	}
}
