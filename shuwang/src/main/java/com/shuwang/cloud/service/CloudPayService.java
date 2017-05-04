package com.shuwang.cloud.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CloudPayService {
	protected final Logger log = LoggerFactory.getLogger(getClass());

	private String appid = null;
	private String appsecret = null;
	private String gatewayUrl = null;

	/**
	 * check whether initialized
	 * 
	 * @return boolean
	 */
	public boolean isInitialized() {
		return appid != null;
	}

	/**
	 * initial service
	 * 
	 * @param appid
	 *            应用编号
	 * @param appsecret
	 *            应用秘钥
	 * @param gatewayUrl
	 *            接口网关
	 */
	public void initial(String appid, String appsecret, String gatewayUrl) {
		this.appid = appid;
		this.appsecret = appsecret;
		this.gatewayUrl = gatewayUrl;
	}

	public boolean sendpay(long payid, int devid, int paytype, int amount,
			int time, String title,
			String printstream) {
		return sendpay(payid, devid, paytype, amount, time, title, printstream, null, null, null);
	}

	public boolean sendpay(long payid, int devid, int paytype, int amount,
			int time, String title,
			String printstream, String tts) {
		return sendpay(payid, devid, paytype, amount, time, title, printstream, tts, null, null);
	}

	public boolean sendpay(long payid, int devid, int paytype, int amount,
			int time, String title,
			String printstream, String tts, Integer mode, Integer paysta) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("method", "pay.cloud.order");
		params.put("appid", appid);
		params.put("timestamp", String.valueOf(System.currentTimeMillis()/1000));
		params.put("payid", payid);
		params.put("devid", String.valueOf(devid));
		params.put("title", title);
		params.put("paytype", String.valueOf(paytype));
		if (mode != null) {
			params.put("mode", String.valueOf(mode));
		}
		if (paysta != null) {
			params.put("paysta", String.valueOf(paysta));
		}

		params.put("amount", String.valueOf(amount));
		params.put("time", String.valueOf(time));
		if (tts != null && !tts.isEmpty()) {
			params.put("tts", tts);
		}
		String signature = GatewayProtocolService
				.signRequest(params, appsecret);
		params.put("sign", signature);
		params.put("printstream", printstream);
		String result = GatewayProtocolService.callDirect(params, gatewayUrl);
		log.debug("sendpay() result={}", result);
		return true;
	}
}
