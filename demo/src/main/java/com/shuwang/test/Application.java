package com.shuwang.test;

import java.io.UnsupportedEncodingException;

import com.shuwang.config.ShuwangCloudConfig;
import com.shuwang.test.model.Order;
import com.shuwang.test.service.OrderPrintService;

public class Application {
	public static void main(String[] args) {
		ShuwangCloudConfig.CLOUDPRINT_URL = "http://api.test.shuwang.info/gateway/rest";
		ShuwangCloudConfig.CLOUDPRINT_APPID = "12345";
		ShuwangCloudConfig.CLOUDPRINT_APPSECRET = "xxxxxxxxxxxxxxxxxx";
		//
		long orderid = System.currentTimeMillis();
		int devid = 12039;
		Order order = new Order();
		//
		OrderPrintService orderPrintService = new OrderPrintService();
		try {
			orderPrintService.printOrder(orderid, devid, order);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
