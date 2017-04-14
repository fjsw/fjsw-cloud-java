package com.shuwang.cloud.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CloudPrintService {
	protected final Logger log = LoggerFactory.getLogger(getClass());

	private String appid = null;
	private String appsecret = null;
	private String gatewayUrl = null;

	/**
	 * check whether initialized
	 * @return boolean
	 */
	public boolean isInitialized() {
		return appid != null;
	}

	/**
	 * initial service
	 * @param appid 应用编号
	 * @param appsecret 应用秘钥
	 * @param gatewayUrl 接口网关
	 */
	public void initial(String appid, String appsecret,String gatewayUrl) {
		this.appid = appid;
		this.appsecret = appsecret;
		this.gatewayUrl = gatewayUrl;
	}

	public boolean sendPrint(long printid, int devid, String title, String printStream, int mode) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("method", "print.cloud.text");
		params.put("appid", appid);
		params.put("timestamp", String.valueOf(System.currentTimeMillis()/1000));
		params.put("printid", String.valueOf(printid));
		params.put("devid", String.valueOf(devid));
		params.put("mode", String.valueOf(mode));
		if (title!=null && !title.isEmpty()) {
			params.put("title", title);
		}
		String signature = GatewayProtocolService.signRequest(params, appsecret);
		params.put("sign", signature);
		params.put("printstream", printStream);
		String result = GatewayProtocolService.callDirect(params, gatewayUrl);
		log.debug("sendPrint() result={}", result);
		return true;
	}

	public boolean sendPrint(long printid, int devid, String title, String printStream, Integer mode, Integer ordermode, Integer timeout) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("method", "print.cloud.text");
		params.put("appid", appid);
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
		String signature = GatewayProtocolService.signRequest(params, appsecret);
		params.put("sign", signature);
		params.put("printstream", printStream);
		String result = GatewayProtocolService.callDirect(params, gatewayUrl);
		log.debug("sendPrint() result={}", result);
		return true;
	}
}
