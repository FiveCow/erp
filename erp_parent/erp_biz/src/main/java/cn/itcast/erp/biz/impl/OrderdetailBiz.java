package cn.itcast.erp.biz.impl;
import java.util.Date;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.redsun.bos.ws.impl.IWaybillWs;

import cn.itcast.erp.biz.IOrderdetailBiz;
import cn.itcast.erp.dao.IOrderdetailDao;
import cn.itcast.erp.dao.IStoredetailDao;
import cn.itcast.erp.dao.IStoreoperDao;
import cn.itcast.erp.dao.ISupplierDao;
import cn.itcast.erp.entity.Orderdetail;
import cn.itcast.erp.entity.Orders;
import cn.itcast.erp.entity.Storedetail;
import cn.itcast.erp.entity.Storeoper;
import cn.itcast.erp.entity.Supplier;
import cn.itcast.erp.exception.ErpException;
/**
 * 订单明细业务逻辑类
 * @author Administrator
 *
 */
public class OrderdetailBiz extends BaseBiz<Orderdetail> implements IOrderdetailBiz {

	private IOrderdetailDao orderdetailDao;
	
	public void setOrderdetailDao(IOrderdetailDao orderdetailDao) {
		this.orderdetailDao = orderdetailDao;
		setBaseDao(orderdetailDao);
	}

	private IStoredetailDao storedetailDao;//商品仓库库存表DAO
		
	public void setStoredetailDao(IStoredetailDao storedetailDao) {
		this.storedetailDao = storedetailDao;
	}

	private IStoreoperDao storeoperDao;//商品仓库库存变动记录Dao
	
	public void setStoreoperDao(IStoreoperDao storeoperDao) {
		this.storeoperDao = storeoperDao;
	}

	private IWaybillWs waybillWs;//红日物流
	
	public void setWaybillWs(IWaybillWs waybillWs) {
		this.waybillWs = waybillWs;
	}

	private ISupplierDao supplierDao;
	
	public void setSupplierDao(ISupplierDao supplierDao) {
		this.supplierDao = supplierDao;
	}



	/**
	 * 采购入库
	 * @param uuid
	 * @param empUuid
	 * @param storeUuid
	 */
	@RequiresPermissions("采购入库")
	public void doInStore(Long uuid,Long empUuid,Long storeUuid){
		
		//1.修改订单明细状态、操作人、操作时间 、仓库ID等数据
		//  根据ID查询订单明细的实体,修改值
		Orderdetail orderdetail = orderdetailDao.get(uuid);
		
		orderdetail.setState("1");//状态  
		orderdetail.setEndtime(new Date());//入库时间
		orderdetail.setEnder(empUuid);//入库人
		orderdetail.setStoreuuid(storeUuid);//仓库ID
		
		
		//2.修改商品仓库库存表的数量（增加）
		Storedetail sd=new Storedetail();
		sd.setGoodsuuid(orderdetail.getGoodsuuid());//商品ID
		sd.setStoreuuid(storeUuid);//仓库ID
		//根据指定的商品ID和仓库ID进行查询
		List<Storedetail> storedetailList = storedetailDao.getList(sd, null, null);
		if(storedetailList.size()==0){
			//新增记录
			sd.setNum(orderdetail.getNum());
			storedetailDao.add(sd);
		}else{
			sd= storedetailList.get(0);
			sd.setNum( sd.getNum()+orderdetail.getNum() );//在原有库存上加上订单明细的数量
		}
		
		
		//3.增加商品仓库库存变动记录（记录库存的变化）
		Storeoper storeoper=new Storeoper();
		storeoper.setEmpuuid(empUuid);//执行人
		storeoper.setGoodsuuid(orderdetail.getGoodsuuid());//商品ID
		storeoper.setNum(orderdetail.getNum());//库存变动数
		storeoper.setOpertime(orderdetail.getEndtime());//操作日期
		storeoper.setStoreuuid(storeUuid);//仓库ID
		storeoper.setType("1");//库存变动类型  1：入库  2：出库
		
		storeoperDao.add(storeoper);
		
		
		//4.当该订单明细的所属订单的所有订单明细均已入库，则修改订单状态
		
		Orders orders = orderdetail.getOrders();//该订单明细的所属订单
		
		//构建查询条件：
		Orderdetail od=new Orderdetail();
		od.setState("0");//状态为未入库的
		od.setOrders(orders);//订单为该订单明细的所属订单
		long count = orderdetailDao.getCount(od, null, null);
		//当所有订单明细均已入库
		if(count==0){
			
			orders.setState("3");//状态 3：已入库
			orders.setEnder(empUuid);
			orders.setEndtime(orderdetail.getEndtime());
		}
		
	}
	
	

	/**
	 * 销售出库
	 * @param uuid
	 * @param empUuid
	 * @param storeUuid
	 */
	@RequiresPermissions("销售订单出库")
	public void doOutStore(Long uuid,Long empUuid,Long storeUuid){
		
		//1.修改订单明细状态、操作人、操作时间 、仓库ID等数据
		//  根据ID查询订单明细的实体,修改值
		Orderdetail orderdetail = orderdetailDao.get(uuid);
		
		orderdetail.setState("1");//状态  1：已出库
		orderdetail.setEndtime(new Date());//出库时间
		orderdetail.setEnder(empUuid);//出库人
		orderdetail.setStoreuuid(storeUuid);//仓库ID
		
		
		//2.修改商品仓库库存表的数量（减少）
		Storedetail sd=new Storedetail();
		sd.setGoodsuuid(orderdetail.getGoodsuuid());//商品ID
		sd.setStoreuuid(storeUuid);//仓库ID
		//根据指定的商品ID和仓库ID进行查询
		List<Storedetail> storedetailList = storedetailDao.getList(sd, null, null);
		if(storedetailList.size()==0){
			//库存不足
			throw new ErpException("库存不足");
		}else{
			sd= storedetailList.get(0);
			sd.setNum( sd.getNum()-orderdetail.getNum() );//在原有库存上减上订单明细的数量
			if(sd.getNum()<0){
				//库存不足
				throw new ErpException("库存不足");
			}
		}
		//3.增加商品仓库库存变动记录（记录库存的变化）
		Storeoper storeoper=new Storeoper();
		storeoper.setEmpuuid(empUuid);//执行人
		storeoper.setGoodsuuid(orderdetail.getGoodsuuid());//商品ID
		storeoper.setNum(orderdetail.getNum());//库存变动数
		storeoper.setOpertime(orderdetail.getEndtime());//操作日期
		storeoper.setStoreuuid(storeUuid);//仓库ID
		storeoper.setType("2");//库存变动类型  1：入库  2：出库
		
		storeoperDao.add(storeoper);
		
		
		//4.当该订单明细的所属订单的所有订单明细均已入库，则修改订单状态
		
		Orders orders = orderdetail.getOrders();//该订单明细的所属订单
		
		//构建查询条件：
		Orderdetail od=new Orderdetail();
		od.setState("0");//状态为未出库的
		od.setOrders(orders);//订单为该订单明细的所属订单
		long count = orderdetailDao.getCount(od, null, null);
		//当所有订单明细均已出库
		if(count==0){
			
			orders.setState("3");//状态 3：已出库
			orders.setEnder(empUuid);
			orders.setEndtime(orderdetail.getEndtime());
			
			//调用物流系统，自动下单
			//查询客户
			Supplier supplier = supplierDao.get(  orders.getSupplieruuid());
			//下单后返回运单号
			Long sn= waybillWs.addWaybill(1L, supplier.getAddress(),
					supplier.getName()+" "+ supplier.getContact(), supplier.getTele(), "--");
			
			orders.setWaybillsn(sn);
			
		}
		
	}
	
	
}
