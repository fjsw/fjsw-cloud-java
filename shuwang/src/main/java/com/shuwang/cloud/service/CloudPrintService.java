package com.shuwang.cloud.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CloudPrintService {
	protected final Logger log = LoggerFactory.getLogger(getClass());
	private GatewayProtocolService gatewayService = new GatewayProtocolService();
	
	public boolean isInitialized() {
		return gatewayService.isInitialized();
	}
	
	public void Initial(String url, String appid, String appsecret) {
		gatewayService.Initial(url, appid, appsecret);
	}

	public boolean sendPrint(long printid, int devid, String title, String printStream, int mode) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("method", "print.cloud.text");
		params.put("appid", gatewayService.getAppid());
		params.put("timestamp", String.valueOf(System.currentTimeMillis()/1000));
		params.put("printid", String.valueOf(printid));
		params.put("devid", String.valueOf(devid));
		params.put("mode", String.valueOf(mode));
		if (title!=null && !title.isEmpty()) {
			params.put("title", title);
		}
		String signature = gatewayService.signRequest(params);
		params.put("sign", signature);
		params.put("printstream", printStream);
		String result = gatewayService.callDirect(params);
		log.debug("sendPrint() result={}", result);
		return true;
	}

	public boolean sendPrint(long printid, int devid, String title, String printStream, Integer mode, Integer ordermode, Integer timeout) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("method", "print.cloud.text");
		params.put("appid", gatewayService.getAppid());
		params.put("timestamp", String.valueOf(System.currentTimeMillis()/1000));
		params.put("printid", String.valueOf(printid));
		params.put("devid", String.valueOf(devid));
		if (mode != null) {
			params.put("mode", String.valueOf(mode));
		}
		if (title!=null && !title.isEmpty()) {
			params.put("title", title);
		}
		if (ordermode != null) {
			params.put("ordermode", String.valueOf(ordermode));
		}
		if (timeout != null) {
			params.put("timeout", String.valueOf(timeout));
		}
		String signature = gatewayService.signRequest(params);
		params.put("sign", signature);
		params.put("printstream", printStream);
		String result = gatewayService.callDirect(params);
		log.debug("sendPrint() result={}", result);
		return true;
	}
}
