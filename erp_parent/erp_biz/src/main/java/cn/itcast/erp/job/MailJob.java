package cn.itcast.erp.job;

import javax.mail.MessagingException;

import cn.itcast.erp.biz.IStoredetailBiz;

public class MailJob {

	
	private IStoredetailBiz storedetailBiz;
		
	public void setStoredetailBiz(IStoredetailBiz storedetailBiz) {
		this.storedetailBiz = storedetailBiz;
	}


	/**
	 * 发送库存预警邮件
	 */
	public void sendStoreAlertMail(){
		
		System.out.println("开始发送库存预警邮件...");
		try {
			storedetailBiz.sendStoreAlertMail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
