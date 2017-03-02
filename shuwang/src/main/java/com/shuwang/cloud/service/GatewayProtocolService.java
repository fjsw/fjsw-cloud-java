package com.shuwang.cloud.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shuwang.cloud.util.HmacMd5Util;

public class GatewayProtocolService {
	protected final Logger log = LoggerFactory.getLogger(getClass());
	private HttpsService httpService = new HttpsService();

	private String url;
	private String appid;
	private String appsecret;
	
	public boolean isInitialized() {
		return (url != null && !url.isEmpty());
	}
	
	public void Initial(String url, String appid, String appsecret) {
		this.url = url;
		this.appid = appid;
		this.appsecret = appsecret;
	}

	public String callDirect(Map<String, Object> params) {
		String result = null;
		try {
			// call
			result = httpService.jsonPost(url, params);
		} catch(Exception ex) {
			ex.printStackTrace();
			log.info("callDirect() exception({})", ex.getMessage());
		}
		return result;
	}

	public String callMethod(Map<String, Object> params) {
		params.put("appid", appid);
		params.put("timestamp", String.valueOf(System.currentTimeMillis()/1000));
		// sign
		String signature = signRequest(params);
		params.put("sign", signature);
		//
		String result = null;
		try {
			// call
			result = httpService.jsonPost(url, params);
		} catch(Exception ex) {
			ex.printStackTrace();
			log.info("callMethod() exception({})", ex.getMessage());
		}
		return result;
	}
	
	public String getAppid() {
		return appid;
	}

	public String signRequest(Map<String, Object> params) {
		// sort keys
		List<Map.Entry<String, Object>> list = new ArrayList<Map.Entry<String, Object>>();
		list.addAll(params.entrySet());
		KeyComparator kc = new KeyComparator();
		Collections.sort(list, kc);
		// 把所有参数名和参数�?�串在一�?
		StringBuilder query = new StringBuilder();
		for (Iterator<Map.Entry<String, Object>> it = list.iterator(); it.hasNext();) {
			Map.Entry<String, Object> item = it.next();
			query.append(item.getKey()).append(item.getValue());
		}
		log.debug("query={}", query.toString());
		// 第三步：使用MD5/HMAC加密
		String sign = null;
		try {
			sign = HmacMd5Util.encryptHMAC(query.toString(), appsecret);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sign;
	}

	private static class KeyComparator implements
			Comparator<Map.Entry<String, Object>> {
		public int compare(Map.Entry<String, Object> m,
				Map.Entry<String, Object> n) {
			return m.getKey().compareTo(n.getKey());
		}
	}
}
