package com.shuwang.test.service;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.shuwang.config.ShuwangCloudConfig;
import com.shuwang.service.CloudPrintService;
import com.shuwang.test.model.Order;
import com.shuwang.test.model.OrderItem;

public class OrderPrintService {
	//拉宽高
	private static final byte FONTZOOM_WIDTH_HIGHT = 0x11;
	//拉宽
	//private static final byte FONTZOOM_WIDTH = 0x10;
	//拉高
	private static final byte FONTZOOM_HIGHT = 0x01;

	private CloudPrintService cloudprintService = new CloudPrintService();

	private int mode = ShuwangCloudConfig.CLOUDPRINT_MODE;

	public boolean printOrder(Long orderid, Integer devid,String title,String printStream)
			throws UnsupportedEncodingException {
		
		boolean send = cloudprintService.sendPrint(orderid, devid, title,
				printStream, mode);
		return send;
	}

	public boolean printOrder(Long orderid, Integer devid, Order order)
			throws UnsupportedEncodingException {
		
		StringBuffer printStreama = new StringBuffer("");
		// 放大字体
		printStreama
				.append("       ")
				.append(new String(formCustomFontLine(order.getShopName(),
						FONTZOOM_WIDTH_HIGHT))).append("\n\n");
		
		printStreama.append("------------------------------\n");
		printStreama
				.append("下单时间: ")
				.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(order.getCreateTime())).append("\n");
		printStreama.append(new String(CloudPrintService.formLinePadding(8)));
		//判断备注是否有内容
		if (order.getOrderMemo() != null && !order.getOrderMemo().isEmpty()) {
			//在拼接内容都要放大时候需要先行拼接在放大
			String BuyMessage = "备注: " + order.getOrderMemo();
			printStreama.append(new String(formCustomFontLine(BuyMessage,
					FONTZOOM_HIGHT)));
		}

		printStreama.append("------------------------------").append("\n");
		printStreama.append("  菜品           数量     金额 ").append("\n");
		printStreama.append("------------------------------").append("\n");
		//
		List<OrderItem>orderItems = order.getItems();
	    
		//为菜品专门NEW一个新的
		StringBuffer printcmena = new StringBuffer("");
		for (OrderItem orderItem : orderItems) {
			//判断菜品有多少字数
			if (orderItem.getName().length() > 10) {
				 printcmena.append(orderItem.getName())
					.append("\n");
				    //计算菜品到菜品数量之间空格数量
					for (int space_count = 18 - 0; space_count > 0; space_count--) {
						printcmena.append(" ");
					}
				
					printcmena.append("X")
						.append(orderItem.getQuantity());
				        int	num=String.valueOf(orderItem.getQuantity()).length();
				        //计算菜品数量到菜品金额之间空格数量
						for(int space_count = 7 - num; space_count > 0; space_count--){
							printcmena.append(" ");
						}
						printcmena.append(orderItem.getAmount()).append("\n");
			} else {
				printcmena.append(orderItem.getName());
				//计算菜品到菜品数量之间空格数量
				for (int space_count = 18 - orderItem.getName().length() * 2; space_count > 0; space_count--) {
					printcmena.append(" ");
				}
			
				printcmena.append("X").append(orderItem.getQuantity());
			    int	num=String.valueOf(orderItem.getQuantity()).length();
			  //计算菜品数量到菜品金额之间空格数量
				for(int space_count = 7 - num; space_count > 0; space_count--){
					printcmena.append(" ");
				}
				printcmena.append(orderItem.getAmount());
			}
		}
		//菜品拼接完成后拼接成完整订单
		String mena=new String(formCustomFontLine(printcmena.toString(), FONTZOOM_HIGHT));
		printStreama.append(mena);
		printStreama.append("------------------------------").append("\n");
		printStreama.append("总计:                     ").append(order.getTotalAmount())
				.append("\n");
		printStreama.append("------------------------------\n");
		//在拼接内容都要放大时候需要先行拼接在放大
		String addr ="地址： "+order.getContactAddr();	
		printStreama.append(new String(formCustomFontLine(addr,FONTZOOM_HIGHT)));
		
		String name ="姓名： "+order.getContactName();	
		printStreama.append(new String(formCustomFontLine(name,FONTZOOM_HIGHT)));
		
		String phone ="手机： "+order.getContactPhone();	
		printStreama.append(new String(formCustomFontLine(phone,FONTZOOM_HIGHT)));
		//设备侧边显示
		String title = "#"+order.getOrderSeq()+" 云打印第";

		//抛给云打印服务
		return printOrder(orderid, devid,title,printStreama.toString());
	}



	// 自定义字号放大
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
