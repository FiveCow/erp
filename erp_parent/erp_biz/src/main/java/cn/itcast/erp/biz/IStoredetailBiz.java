package cn.itcast.erp.biz;
import java.util.List;

import javax.mail.MessagingException;

import cn.itcast.erp.entity.StoreAlert;
import cn.itcast.erp.entity.Storedetail;
/**
 * 仓库库存业务逻辑层接口
 * @author Administrator
 *
 */
public interface IStoredetailBiz extends IBaseBiz<Storedetail>{
	

	/**
	 * 查询库存预警报表
	 * @return
	 */
	public List<StoreAlert> getStoreAlertList();
	
	
	/**
	 * 发送库存预警邮件
	 * @throws MessagingException 
	 */
	public void sendStoreAlertMail() throws MessagingException;
	
}

