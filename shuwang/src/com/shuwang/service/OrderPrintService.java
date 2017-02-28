package com.shuwang.service;

import java.io.UnsupportedEncodingException;

import com.shuwang.config.ShuwangCloudConfig;

public class OrderPrintService {
	private CloudPrintService cloudprintService = new CloudPrintService();

	private int mode = ShuwangCloudConfig.CLOUDPRINT_MODE;

	public boolean printOrder(String printStream, Long orderid, Integer devid)
			throws UnsupportedEncodingException {
		String title = "云打印";
		boolean send = cloudprintService.sendPrint(orderid, devid, title,
				printStream, mode);
		return send;
	}
}
