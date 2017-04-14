package com.shuwang.cloud.service;


import com.google.gson.Gson;
import com.shuwang.cloud.util.HmacMd5Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class GatewayProtocolService {
	protected static final Logger log = LoggerFactory.getLogger(GatewayProtocolService.class);
	private static HttpsService http = new HttpsService();
	private static Gson gson = new Gson();

	public static String callMethod(Map<String, Object> params,String appid,String appsecret,String gatewayUrl) {
		params.put("appid", appid);
		params.put("timestamp", String.valueOf(System.currentTimeMillis()/1000));
		// sign
		String signature = signRequest(params,appsecret);
		params.put("sign", signature);
		// call
		return callDirect(params,gatewayUrl);
	}

	public static String signRequest(Map<String, Object> params,String appsecret) {
		// sort keys
		List<Map.Entry<String, Object>> list = new ArrayList<Map.Entry<String, Object>>();
		list.addAll(params.entrySet());
		KeyComparator kc = new KeyComparator();
		Collections.sort(list, kc);
		// 把所有参数名和参数值串在一起
		StringBuilder query = new StringBuilder();
		for (Iterator<Map.Entry<String, Object>> it = list.iterator(); it.hasNext();) {
			Map.Entry<String, Object> item = it.next();
			Object value = item.getValue();
			if (value == null) {
				;
			}
			else if (value instanceof String) {
				String string_value = (String) value;
				if (!string_value.isEmpty()) {
					query.append(item.getKey()).append(string_value);
				}
			} else {
				query.append(item.getKey()).append(value);
			}
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
	
	public static boolean checkSignEqual(Map<String, Object> data,String appsecret) {
		String sign = (String) data.get("sign");
		if (sign == null || sign.isEmpty()) {
			return false;
		}
		//
		data.remove("sign");
		String signRequest = signRequest(data,appsecret);
		return sign.equals(signRequest);
	}

	private static class KeyComparator implements
			Comparator<Map.Entry<String, Object>> {
		public int compare(Map.Entry<String, Object> m,
				Map.Entry<String, Object> n) {
			return m.getKey().compareTo(n.getKey());
		}
	}
	
	public static String callDirect(final Map<String, Object> params,String url) {
		String jsonObject = gson.toJson(params);
		try {
			String text = http.jsonPost(url,jsonObject);
			return text;
		}catch (Exception e){
			e.printStackTrace();
			return e.getMessage();
		}
	}
}
