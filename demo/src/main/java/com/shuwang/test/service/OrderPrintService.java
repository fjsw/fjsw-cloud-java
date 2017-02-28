package com.shuwang.test.service;

import java.io.UnsupportedEncodingException;

import com.shuwang.config.ShuwangCloudConfig;
import com.shuwang.service.CloudPrintService;
import com.shuwang.test.model.Order;

public class OrderPrintService {
	private CloudPrintService cloudprintService = new CloudPrintService();

	private int mode = ShuwangCloudConfig.CLOUDPRINT_MODE;

	public boolean printOrder(Long orderid, Integer devid, String printStream)
			throws UnsupportedEncodingException {
		String title = "云打印";
		boolean send = cloudprintService.sendPrint(orderid, devid, title,
				printStream, mode);
		return send;
	}
	
	public boolean printOrder(Long orderid, Integer devid, Order order)
			throws UnsupportedEncodingException {
		String printStream = transPrintStream(order);
		//
		return printOrder(orderid, devid, printStream);
	}
	
	private String transPrintStream(Order order) {
		return "云打印测试\n\n\n";
	}
}
