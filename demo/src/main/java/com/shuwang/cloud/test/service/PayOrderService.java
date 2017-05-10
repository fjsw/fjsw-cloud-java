package com.shuwang.cloud.test.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shuwang.cloud.config.ShuwangCloudConfig;
import com.shuwang.cloud.service.CloudPayService;
import com.shuwang.cloud.test.config.CloudPrintConfig;
import com.shuwang.cloud.util.CloudPrintUtil;

public class PayOrderService {
    protected final Logger log = LoggerFactory.getLogger(getClass());
	private CloudPayService cloudPayService = new CloudPayService();

	public boolean printPayOrder(long payid, int devid, int amount,
			String title, String body, String tts) {
		String printstream = new String(CloudPrintUtil.formCustomFontLine(body,
				CloudPrintUtil.FONTZOOM_WIDTH_HIGHT));
		int paytype = ShuwangCloudConfig.CLOUDPAY_PAY_TYPE_ALIPAY;
		Integer mode = ShuwangCloudConfig.CLOUDPAY_MODE_DISPLAY_ONLY;
		Integer paysta = ShuwangCloudConfig.CLOUDPAY_PAYSTA_PAID;
		Integer time = (int) (System.currentTimeMillis() / 1000);
		cloudPayService.initial(CloudPrintConfig.CLOUDAPP_APPID,
				CloudPrintConfig.CLOUDAPP_APPSECRET,
				CloudPrintConfig.GATEWAY_URL);
		boolean send = cloudPayService.sendpay(payid, devid, paytype, amount,
				time, title, printstream, tts, mode, paysta);
		return send;
	}
}
