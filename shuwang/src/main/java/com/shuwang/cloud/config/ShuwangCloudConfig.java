package com.shuwang.cloud.config;

public class ShuwangCloudConfig {
	// mode 打印选项
	public static int CLOUDPRINT_MODE_NORMAL = 4;// 缺省4按订单方式处理

	// ordermode 订单模式
	public static int CLOUDPRINT_ORDERMODE_NORMAL = 1;// 缺省1普通订单需要接单
	public static int CLOUDPRINT_ORDERMODE_FORCE_PRINT = 2;// 2已接单订单强制打印
	public static int CLOUDPRINT_ORDERMODE_DISPLAY_ONLY = 3;// 3已接单只显示不打印

	// timeout 超时秒数
	public static int CLOUDPRINT_TIMEOUT = 600;// 缺省600秒超时

	// MODE 支付订单打印模式
	public static int CLOUDPAY_MODE_NORMAL = 1;// 1只打印
	public static int CLOUDPAY_MODE_FORCE_PRINT = 2;// 2只显示
	public static int CLOUDPAY_MODE_DISPLAY_ONLY = 3;// 缺省3打印加显示

	// PayType_t支付类型表
	public static int CLOUDPAY_PAY_TYPE_UNKNOWN = 0;// 未知类型
	public static int CLOUDPAY_PAY_TYPE_ALIPAY = 1;// 2阿里支付( 支付宝 )
	public static int CLOUDPAY_PAY_TYPE_WEIXIN = 2;// 3微信支付
	public static int CLOUDPAY_PAY_TYPE_BAIDU = 3;// 百度钱包
	public static int CLOUDPAY_PAY_TYPE_BESTPAY = 4;// 翼支付
	public static int CLOUDPAY_PAY_TYPE_MULTI = 0xf0;// 3混合类型(分单支付类型)
	public static int CLOUDPAY_PAY_TYPE_CART= 0xf1;// 2银行卡
	public static int CLOUDPAY_PAY_TYPE_NOBAR = 0xfe;// 没有条码

	// PaySta_t 支付状态表
	public static int CLOUDPAY_PAYSTA_PAID = 1;// 已支付
	public static int CLOUDPAY_PAYSTA_REFUNDING = 2;// 2退款中
	public static int CLOUDPAY_PAYSTA_REFUNDED = 3;// 已退款
	public static int CLOUDPAY_PAYSTA_REFUNDFAIL = 10;// 退款失败
	public static int CLOUDPAY_PAYSTA_PAING = 4;// 正在处理
}
