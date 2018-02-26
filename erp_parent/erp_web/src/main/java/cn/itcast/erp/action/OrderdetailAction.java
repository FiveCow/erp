package cn.itcast.erp.action;
import cn.itcast.erp.biz.IOrderdetailBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Orderdetail;
import cn.itcast.erp.exception.ErpException;

/**
 * 订单明细Action 
 * @author Administrator
 *
 */
public class OrderdetailAction extends BaseAction<Orderdetail> {

	private IOrderdetailBiz orderdetailBiz;
	
	public void setOrderdetailBiz(IOrderdetailBiz orderdetailBiz) {
		this.orderdetailBiz = orderdetailBiz;
		setBaseBiz(orderdetailBiz);
	}
	
	private Long storeUuid;//仓库ID
		
	public void setStoreUuid(Long storeUuid) {
		this.storeUuid = storeUuid;
	}


	/**
	 * 采购入库
	 */
	public void doInStore(){
		
		Emp emp = getUser();
		if(emp==null){
			write(ajaxReturn(false, "当前用户未登陆"));
			return ;
		}
		
		
		try {
			orderdetailBiz.doInStore(getId(), emp.getUuid(), storeUuid);
			write(ajaxReturn(true, "采购入库成功"));
		} catch (Exception e) {
			
			e.printStackTrace();
			write(ajaxReturn(false, "采购入库失败"));
		}
		
	}
	
	/**
	 * 销售出库
	 */
	public void doOutStore(){
		
		Emp emp = getUser();
		if(emp==null){
			write(ajaxReturn(false, "当前用户未登陆"));
			return ;
		}		
		
		try {
			orderdetailBiz.doOutStore(getId(), emp.getUuid(), storeUuid);
			write(ajaxReturn(true, "销售出库成功"));
		}catch (ErpException e) {
			write(ajaxReturn(false, e.getMessage() ));
		} catch (Exception e) {
			
			e.printStackTrace();
			write(ajaxReturn(false, "销售出库失败"));
		}
		
	}
	
}
