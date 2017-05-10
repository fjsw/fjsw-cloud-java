package com.shuwang.cloud.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.shuwang.cloud.service.GatewayProtocolService;
import com.shuwang.cloud.test.config.CloudPrintConfig;
import com.shuwang.cloud.test.model.Order;
import com.shuwang.cloud.test.model.OrderItem;
import com.shuwang.cloud.test.service.OrderPrintService;
import com.shuwang.cloud.test.service.PayOrderService;
import com.shuwang.cloud.util.GsonUtils;

public class Application {
	protected static final Logger log = LoggerFactory
			.getLogger(Application.class);

	public static void main(String[] args) {
		// printOrder();
		payOrder();
		//
		onAppCallback(
				"{\"appid\":\"12345\",\"method\":\"print.notify.status\",\"timestamp\":\"1494391030\",\"sign\":\"0\"}",
				CloudPrintConfig.CLOUDAPP_APPSECRET);
	}

	private static void payOrder() {
		long payid = System.currentTimeMillis();
		int devid = 12039;
		int amount = 100;
		String title = "支付宝收款1元";
		String body = "支付宝收款1元";
		String tts = "支付宝收款1元";
		//
		PayOrderService payOrderService = new PayOrderService();
		try {
			payOrderService.printPayOrder(payid, devid, amount, title, body,
					tts);
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
		}
	}

	static void printOrder() {
		//
		long orderid = System.currentTimeMillis();
		int devid = 12039;
		// order
		Order order = new Order();
		order.setContactAddr("测试地址");
		order.setContactName("数网");// 名字
		order.setContactPhone("666666");// 手机
		order.setCreateTime(new Date());// 时间
		order.setOrderId(orderid);// 订单号
		order.setOrderMemo("测试订单请查收");// 备注
		order.setOrderSeq(1);// 第几单
		order.setShopName("测试店铺");// 店铺名称
		order.setTotalAmount("20");// 总金额
		// order item
		OrderItem orderItem = new OrderItem();
		orderItem.setAmount("20");// 菜品金额
		orderItem.setName("鸡腿饭");// 菜品
		orderItem.setQuantity(1);// 菜品数量
		List<OrderItem> orderItems = new ArrayList<OrderItem>();
		orderItems.add(orderItem);
		order.setItems(orderItems);// 菜品list集合

		// 发送订单打印到设备
		OrderPrintService orderPrintService = new OrderPrintService();
		for (int i = 0; i < 3; i++) {
			boolean success = orderPrintService.printOrder(orderid, devid,
					order);
			if (success) {
				log.debug("printOrder() success");
				break;
			} else {
				log.debug("printOrder() fail");
			}
		}
	}

	static void onAppCallback(String body, String appsecret) {
		Gson gson = GsonUtils.getGson();
		@SuppressWarnings("unchecked")
		Map<String, Object> response = gson.fromJson(body, Map.class);
		if (!GatewayProtocolService.checkSignEqual(response, appsecret)) {
			log.debug("sign not equal");
			return;
		} else {
			log.debug("sign OK!");
		}
		//
		String method = response.get("method").toString();
		if (method.equals("print.notify.status")) {
			onPrintStatusChange(response);
		} else if (method.equals("order.notify.status")) {
			onOrderStatusChange(response);
		} else {
			log.info("method({}) unknown!");
		}
	}

	static void onPrintStatusChange(Map<String, Object> response) {
		long printid = Long.parseLong(response.get("printid").toString());
		int devid = Integer.parseInt(response.get("devid").toString());
		int status = Integer.parseInt(response.get("status").toString());
		log.debug("onPrintStatusChange() printid={}, devid={}, status={}",
				printid, devid, status);
	}

	static void onOrderStatusChange(Map<String, Object> response) {
		long printid = Long.parseLong(response.get("printid").toString());
		int devid = Integer.parseInt(response.get("devid").toString());
		int orderstatus = Integer.parseInt(response.get("orderstatus")
				.toString());
		log.debug("onOrderStatusChange() printid={}, devid={}, orderstatus={}",
				printid, devid, orderstatus);
	}
}
