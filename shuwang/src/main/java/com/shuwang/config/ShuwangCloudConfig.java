package com.shuwang.config;

public class ShuwangCloudConfig {
	public static String CLOUDPRINT_URL = null;
	public static String CLOUDPRINT_APPID = null;
	public static String CLOUDPRINT_APPSECRET = null;

	// mode 打印选项
	public static int CLOUDPRINT_MODE_NORMAL = 4;//缺省4按订单方式处理

	// ordermode 订单模式
	public static int CLOUDPRINT_ORDERMODE_NORMAL = 1;//缺省1普通订单需要接单
	public static int CLOUDPRINT_ORDERMODE_FORCE_PRINT = 2;//2已接单订单强制打印
	public static int CLOUDPRINT_ORDERMODE_DISPLAY_ONLY = 3;//3已接单只显示不打印

	// timeout 超时秒数
	public static int CLOUDPRINT_TIMEOUT = 600;//缺省600秒超时
}
