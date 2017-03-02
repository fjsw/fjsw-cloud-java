package com.shuwang.cloud.test.service;

import java.text.SimpleDateFormat;
import java.util.List;

import com.shuwang.cloud.config.ShuwangCloudConfig;
import com.shuwang.cloud.service.CloudPrintService;
import com.shuwang.cloud.test.config.CloudPringConfig;
import com.shuwang.cloud.test.model.Order;
import com.shuwang.cloud.test.model.OrderItem;
import com.shuwang.cloud.util.CloudPrintUtil;

public class OrderPrintService {
	private CloudPrintService cloudprintService = new CloudPrintService();

	private int mode = ShuwangCloudConfig.CLOUDPRINT_MODE_NORMAL;

	public boolean printOrder(Long orderid, Integer devid, String title,
			String printStream) {
		if (!cloudprintService.isInitialized()) {
			cloudprintService.Initial(CloudPringConfig.CLOUDPRINT_URL,
					CloudPringConfig.CLOUDPRINT_APPID,
					CloudPringConfig.CLOUDPRINT_APPSECRET);
		}
		boolean send = cloudprintService.sendPrint(orderid, devid, title,
				printStream, mode);
		return send;
	}

	public boolean printOrder(Long orderid, Integer devid, Order order) {
		StringBuffer printStreama = new StringBuffer("");
		// 放大字体
		String shopname = new String(CloudPrintUtil.formCustomFontLine(
				order.getShopName(), CloudPrintUtil.FONTZOOM_WIDTH_HIGHT));
		printStreama.append("       ").append(shopname).append("\n\n");

		printStreama.append("------------------------------\n");
		printStreama
				.append("下单时间: ")
				.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(order.getCreateTime())).append("\n");
		printStreama.append(new String(CloudPrintUtil.formLinePadding(8)));
		// 判断备注是否有内容
		if (order.getOrderMemo() != null && !order.getOrderMemo().isEmpty()) {
			// 在拼接内容都要放大时候需要先行拼接在放大
			String ordermemo = new String(CloudPrintUtil.formCustomFontLine(
					"备注: " + order.getOrderMemo(),
					CloudPrintUtil.FONTZOOM_HIGHT));
			printStreama.append(ordermemo);
		}

		printStreama.append("------------------------------").append("\n");
		printStreama.append("  菜品           数量     金额 ").append("\n");
		printStreama.append("------------------------------").append("\n");
		//
		List<OrderItem> orderItems = order.getItems();

		// 为菜品专门NEW一个新的
		StringBuffer printcmena = new StringBuffer("");
		for (OrderItem orderItem : orderItems) {
			// 判断菜品有多少字数
			if (orderItem.getName().length() > 10) {
				printcmena.append(orderItem.getName()).append("\n");
				// 计算菜品到菜品数量之间空格数量
				for (int space_count = 18 - 0; space_count > 0; space_count--) {
					printcmena.append(" ");
				}

				printcmena.append("X").append(orderItem.getQuantity());
				int num = String.valueOf(orderItem.getQuantity()).length();
				// 计算菜品数量到菜品金额之间空格数量
				for (int space_count = 7 - num; space_count > 0; space_count--) {
					printcmena.append(" ");
				}
				printcmena.append(orderItem.getAmount()).append("\n");
			} else {
				printcmena.append(orderItem.getName());
				// 计算菜品到菜品数量之间空格数量
				for (int space_count = 18 - orderItem.getName().length() * 2; space_count > 0; space_count--) {
					printcmena.append(" ");
				}

				printcmena.append("X").append(orderItem.getQuantity());
				int num = String.valueOf(orderItem.getQuantity()).length();
				// 计算菜品数量到菜品金额之间空格数量
				for (int space_count = 7 - num; space_count > 0; space_count--) {
					printcmena.append(" ");
				}
				printcmena.append(orderItem.getAmount());
			}
		}
		// 菜品拼接完成后拼接成完整订单
		String mena = new String(CloudPrintUtil.formCustomFontLine(
				printcmena.toString(), CloudPrintUtil.FONTZOOM_HIGHT));
		printStreama.append(mena);
		printStreama.append("------------------------------").append("\n");
		printStreama.append("总计:                     ")
				.append(order.getTotalAmount()).append("\n");
		printStreama.append("------------------------------\n");
		// 在拼接内容都要放大时候需要先行拼接在放大
		String addr = "地址： " + order.getContactAddr();
		printStreama.append(new String(CloudPrintUtil.formCustomFontLine(addr,
				CloudPrintUtil.FONTZOOM_HIGHT)));

		String name = "姓名： " + order.getContactName();
		printStreama.append(new String(CloudPrintUtil.formCustomFontLine(name,
				CloudPrintUtil.FONTZOOM_HIGHT)));

		String phone = "手机： " + order.getContactPhone();
		printStreama.append(new String(CloudPrintUtil.formCustomFontLine(phone,
				CloudPrintUtil.FONTZOOM_HIGHT)));
		printStreama.append("\n\n");
		// 设备侧边显示
		String title = "#" + order.getOrderSeq() + " 云打印";

		// 抛给云打印服务
		return printOrder(orderid, devid, title, printStreama.toString());
	}
}
