package cn.itcast.erp.action;
import java.util.List;

import javax.mail.MessagingException;

import com.alibaba.fastjson.JSON;

import cn.itcast.erp.biz.IStoredetailBiz;
import cn.itcast.erp.entity.StoreAlert;
import cn.itcast.erp.entity.Storedetail;
import cn.itcast.erp.exception.ErpException;

/**
 * 仓库库存Action 
 * @author Administrator
 *
 */
public class StoredetailAction extends BaseAction<Storedetail> {

	private IStoredetailBiz storedetailBiz;
	
	public void setStoredetailBiz(IStoredetailBiz storedetailBiz) {
		this.storedetailBiz = storedetailBiz;
		setBaseBiz(storedetailBiz);
	}
	
	/**
	 * 库存预警报表
	 */
	public void storeAlertList(){
		List<StoreAlert> storeAlertList = storedetailBiz.getStoreAlertList();
		String jsonString = JSON.toJSONString(storeAlertList);
		write(jsonString);		
	}
	
	
	/**
	 * 发送库存警报邮件
	 */
	public void sendStoreAlertMail(){
		try {
			storedetailBiz.sendStoreAlertMail();
			write(ajaxReturn(true, "发送库存预警邮件成功"));
		}catch (MessagingException e) {
			write(ajaxReturn(false, "构建邮件信息出现错误，请核对配置"));
			e.printStackTrace();
		}catch (ErpException e) {
			write(ajaxReturn(false, e.getMessage()));
			e.printStackTrace();
		}catch (Exception e) {
			write(ajaxReturn(false, "发送邮件失败"));
			e.printStackTrace();
		}
		
	}
	
	
}
