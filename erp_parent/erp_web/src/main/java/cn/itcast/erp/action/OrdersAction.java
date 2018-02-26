package cn.itcast.erp.action;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;
import com.redsun.bos.ws.Waybilldetail;
import com.redsun.bos.ws.impl.IWaybillWs;

import cn.itcast.erp.biz.IOrdersBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Orderdetail;
import cn.itcast.erp.entity.Orders;
import cn.itcast.erp.exception.ErpException;

/**
 * 订单Action 
 * @author Administrator
 *
 */
public class OrdersAction extends BaseAction<Orders> {

	private IOrdersBiz ordersBiz;
	
	public void setOrdersBiz(IOrdersBiz ordersBiz) {
		this.ordersBiz = ordersBiz;
		setBaseBiz(ordersBiz);
	}
	
	private String json;
	
	public void setJson(String json) {
		this.json = json;
	}

	/**
	 * 保存订单
	 */
	public void add(){
		Emp emp = getUser();
		if(emp==null){
			write(ajaxReturn(false, "当前操作员未登陆"));
			return ;
		}
		
		try {
			Orders orders = getT();
			//将json字符串转换为订单从表集合
			List<Orderdetail> orderdetails = JSON.parseArray(json, Orderdetail.class);
			
			orders.setOrderdetails(orderdetails);
			
			orders.setCreater(emp.getUuid());//当前登陆人 ，为申请人
			ordersBiz.add(orders);			
			write(ajaxReturn(true, "保存订单成功"));
			
		}catch (ErpException e) {
	
			write(ajaxReturn(false,e.getMessage()));
		} catch (Exception e) {
			
			e.printStackTrace();
			write(ajaxReturn(false, "保存订单失败"));
		}
	}
	
	/**
	 * 采购订单审核
	 */
	public void doCheck(){
		
		Emp emp = getUser();
		if(emp==null){
			write(ajaxReturn(false, "当前用户未登陆"));
			return ;
		}
		
		try {
			ordersBiz.doCheck(getId(), emp.getUuid());
			write(ajaxReturn(true, "审核成功"));
		} catch (Exception e) {
			e.printStackTrace();
			write(ajaxReturn(false, "审核失败"));
		}
		
		
	}
	
	
	/**
	 * 采购订单确认
	 */
	public void doStart(){
		
		Emp emp = getUser();
		if(emp==null){
			write(ajaxReturn(false, "当前用户未登陆"));
			return ;
		}
		
		try {
			ordersBiz.doStart(getId(), emp.getUuid());
			write(ajaxReturn(true, "确认成功"));
		} catch (Exception e) {
			e.printStackTrace();
			write(ajaxReturn(false, "确认失败"));
		}
		
		
	}
	
	/**
	 * 我的采购(销售)订单
	 */
	public void myListByPage(){
		
		if(getT1()==null){
			setT1(new Orders());
		}		
		//设置条件		
		getT1().setCreater(getUser().getUuid());			
		super.listByPage();
	}
	
	
	/**
	 * 导出采购单
	 * @throws IOException 
	 */
	public void exportById() throws IOException{
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setHeader("Content-Disposition", "attachment;filename=orders_"+ getId() +".xls");
		ServletOutputStream out = response.getOutputStream();
		ordersBiz.exportById(out, getId());
		
	}
	
	
	private IWaybillWs waybillWs;//远程物流系统
	
	public void setWaybillWs(IWaybillWs waybillWs) {
		this.waybillWs = waybillWs;
	}

	private Long waybillSn;//运单号
		
	public void setWaybillSn(Long waybillSn) {
		this.waybillSn = waybillSn;
	}

	
	/**
	 * 根据运单号查询运单详情
	 */
	public void  waybilldetailList(){
		List<Waybilldetail> list = waybillWs.waybilldetailList(waybillSn);
		String jsonString = JSON.toJSONString(list);
		write(jsonString);
	}
	
	
}
