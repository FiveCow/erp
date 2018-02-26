package cn.itcast.erp.action;
import java.util.List;

import com.alibaba.fastjson.JSON;

import cn.itcast.erp.biz.IStoreBiz;
import cn.itcast.erp.entity.Store;

/**
 * 仓库Action 
 * @author Administrator
 *
 */
public class StoreAction extends BaseAction<Store> {

	private IStoreBiz storeBiz;
	
	public void setStoreBiz(IStoreBiz storeBiz) {
		this.storeBiz = storeBiz;
		setBaseBiz(storeBiz);
	}
	
	/**
	 * 我的仓库列表
	 */
	public void mylist(){
		Store store=new Store();
		store.setEmpuuid(getUser().getUuid());		
		setT1(store);
		list();		
	}
	
}
