package cn.itcast.erp.biz.impl;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import cn.itcast.erp.biz.IStoredetailBiz;
import cn.itcast.erp.dao.IStoredetailDao;
import cn.itcast.erp.entity.StoreAlert;
import cn.itcast.erp.entity.Storedetail;
import cn.itcast.erp.exception.ErpException;
import util.MailUtil;
/**
 * 仓库库存业务逻辑类
 * @author Administrator
 *
 */
public class StoredetailBiz extends BaseBiz<Storedetail> implements IStoredetailBiz {

	private IStoredetailDao storedetailDao;
	
	public void setStoredetailDao(IStoredetailDao storedetailDao) {
		this.storedetailDao = storedetailDao;
		setBaseDao(storedetailDao);
	}
	
	private MailUtil mailUtil;//邮件工具类
	
	public void setMailUtil(MailUtil mailUtil) {
		this.mailUtil = mailUtil;
	}


	/**
	 * 库存预警报表
	 */
	public List<StoreAlert> getStoreAlertList() {

		return storedetailDao.getStoreAlertList();
	}

	private String toAddress;//收件箱
	private String subject;//主题
	private String text;//正文
		
	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setText(String text) {
		this.text = text;
	}

	/**
	 * 发送库存预警邮件
	 * @throws MessagingException 
	 */
	public void sendStoreAlertMail() throws MessagingException{
		//得到库存预警列表
		List<StoreAlert> storeAlertList = storedetailDao.getStoreAlertList();
		if(storeAlertList.size()>0){
		
			mailUtil.sendMail(toAddress, 
					subject.replace("[time]", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())), 
					text.replace("[count]", String.valueOf(storeAlertList.size()) ));
		}else{
			throw new ErpException("没有库存预警信息");
		}		
		
	}
	
}
