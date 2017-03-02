package com.shuwang.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.shuwang.config.ShuwangCloudConfig;
import com.shuwang.test.model.Order;
import com.shuwang.test.model.OrderItem;
import com.shuwang.test.service.OrderPrintService;

public class Application {
	public static void main(String[] args) {
		ShuwangCloudConfig.CLOUDPRINT_URL = "http://api.test.shuwang.info/gateway/rest";
		ShuwangCloudConfig.CLOUDPRINT_APPID = "12345";
		ShuwangCloudConfig.CLOUDPRINT_APPSECRET = "xxxxxxxxxxxxxxxxxx";
		//
		long orderid = System.currentTimeMillis();
		int devid = 12039;
		Order order = new Order();
		order.setContactAddr("测试地址");
		order.setContactName("数网");//名字
		order.setContactPhone("666666");//手机
		order.setCreateTime(new Date());//时间
		order.setOrderId(orderid);//订单号
		order.setOrderMemo("测试订单请查收");//备注
		order.setOrderSeq(1);//第几单
		order.setShopName("测试店铺");//店铺名称
		order.setTotalAmount("20");//总金额
		
		OrderItem orderItem=new OrderItem();
		orderItem.setAmount("20");//菜品金额
		orderItem.setName("鸡腿饭");//菜品
		orderItem.setQuantity(1);//菜品数量
		List<OrderItem>orderItems=new ArrayList<OrderItem>();
		orderItems.add(orderItem);
		order.setItems(orderItems);//菜品list集合
		
		//
		OrderPrintService orderPrintService = new OrderPrintService();
		for (int i=0; i<3; i++) {
			boolean success = orderPrintService.printOrder(orderid, devid, order);
			if (success) {
				System.out.println("success");
				break;
			} else {
				System.out.println("fail");
			}
		}
	}
}
