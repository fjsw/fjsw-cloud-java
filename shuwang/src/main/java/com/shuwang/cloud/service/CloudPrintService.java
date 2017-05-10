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

	/**
	 * 向设备发送订单打印
	 * @param printid 打印编号(订单编号)
	 * @param devid 设备编号
	 * @param title 标题
	 * @param printStream 打印流
	 * @param mode 打印模式
	 * @return
	 */
	public boolean sendPrint(long printid, int devid, String title,
			String printStream, int mode) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("method", "print.cloud.text");
		params.put("appid", appid);
		params.put("timestamp",
				String.valueOf(System.currentTimeMillis() / 1000));
		params.put("printid", String.valueOf(printid));
		params.put("devid", String.valueOf(devid));
		params.put("mode", String.valueOf(mode));
		if (title != null && !title.isEmpty()) {
			params.put("title", title);
		}
		String signature = GatewayProtocolService
				.signRequest(params, appsecret);
		params.put("sign", signature);
		params.put("printstream", printStream);
		String result = GatewayProtocolService.callDirect(params, gatewayUrl);
		log.debug("sendPrint() result={}", result);
		return true;
	}

	/**
	 * 向设备发送订单打印
	 * @param printid 打印编号(订单编号)
	 * @param devid 设备编号
	 * @param title 标题
	 * @param printStream 打印流
	 * @param mode 打印模式
	 * @param ordermode 订单状态
	 * @param timeout 打印超时时间
	 * @return boolean
	 */
	public boolean sendPrint(long printid, int devid, String title,
			String printStream, Integer mode, Integer ordermode, Integer timeout) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("method", "print.cloud.text");
		params.put("appid", appid);
		params.put("timestamp",
				String.valueOf(System.currentTimeMillis() / 1000));
		params.put("printid", String.valueOf(printid));
		params.put("devid", String.valueOf(devid));
		if (mode != null) {
			params.put("mode", String.valueOf(mode));
		}
		if (title != null && !title.isEmpty()) {
			params.put("title", title);
		}
		if (ordermode != null) {
			params.put("ordermode", String.valueOf(ordermode));
		}
		if (timeout != null) {
			params.put("timeout", String.valueOf(timeout));
		}
		String signature = GatewayProtocolService
				.signRequest(params, appsecret);
		params.put("sign", signature);
		params.put("printstream", printStream);
		String result = GatewayProtocolService.callDirect(params, gatewayUrl);
		log.debug("sendPrint() result={}", result);
		return true;
	}

	/**
	 * 修改设备订单状态
	 * @param printid 打印编号(订单编号)
	 * @param devid 设备编号
	 * @param orderstatus 订单状态
	 * @return boolean
	 */
	public boolean changeOrderStatus(long printid, int devid, int orderstatus) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("method", "order.status.change");
		params.put("printid", String.valueOf(printid));
		params.put("devid", String.valueOf(devid));
		params.put("orderstatus", orderstatus);
		String result = GatewayProtocolService.callMethod(params, appid,
				appsecret, gatewayUrl);
		log.debug("changeOrderStatus() result={}", result);
		return true;
	}
}
