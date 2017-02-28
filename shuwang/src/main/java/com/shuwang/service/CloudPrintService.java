package com.shuwang.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CloudPrintService {
	protected final Logger log = LoggerFactory.getLogger(getClass());
	private GatewayProtocolService gatewayService = new GatewayProtocolService();

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

	public static byte[] formBarcodeSteam(String url) {
		byte ver = 0;// 二维码版�?(建议�?0自动选择)
		byte amp = 6;// 放大倍数(建议�?6)
		byte lever = 0;// 二维码纠错等�?0~3，一般填0
		//
		byte[] barcode_bytes = new byte[8 + url.length()];
		barcode_bytes[0] = 0x1D;
		barcode_bytes[1] = 0x6B;
		barcode_bytes[2] = 0x5D;
		barcode_bytes[3] = ver;
		barcode_bytes[4] = amp;
		barcode_bytes[5] = lever;
		barcode_bytes[6] = (byte) url.length(); // nL nH: 数据长度
		barcode_bytes[7] = 0x00;// nL nH: 数据长度
		System.arraycopy(url.getBytes(), 0, barcode_bytes, 8, url.length());
		return barcode_bytes;
	}

	public static byte[] formLinePadding(int n) {
		// 打印并走纸�?? 用\n相当于物理行24点，这个指令可以精确�?1个点
		byte[] linepadding_bytes = new byte[3];
		linepadding_bytes[0] = 0x1B;
		linepadding_bytes[1] = 0x4A;
		linepadding_bytes[2] = (byte) n;
		return linepadding_bytes;
	}

	// 自定义字号放�?
	public static byte[] formCustomFontLine(String str, byte fontzoom) {
		byte[] data = str.getBytes();
		//
		byte[] customfont_bytes = new byte[3 + data.length + 3];
		customfont_bytes[0] = 0x1D;
		customfont_bytes[1] = 0x21;
		customfont_bytes[2] = fontzoom;// 字体大小，推荐放大到 0x01 0x10 0x11
		System.arraycopy(data, 0, customfont_bytes, 3, data.length);
		customfont_bytes[data.length + 3] = 0x0A;
		customfont_bytes[data.length + 4] = 0x1B;
		customfont_bytes[data.length + 5] = 0x40;
		return customfont_bytes;
	}
}
