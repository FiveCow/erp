package cn.itcast.erp.biz;
import java.io.IOException;
import java.io.OutputStream;

import cn.itcast.erp.entity.Orders;
/**
 * 订单业务逻辑层接口
 * @author Administrator
 *
 */
public interface IOrdersBiz extends IBaseBiz<Orders>{
	
	/**
	 * 采购审核
	 * @param uuid
	 * @param empUuid
	 */
	public void doCheck(Long uuid,Long empUuid);
	
	/**
	 * 采购确认
	 * @param uuid
	 * @param empUuid
	 */
	public void doStart(Long uuid,Long empUuid);
	
	
	/**
	 * 导出采购单或销售单
	 * @param out
	 * @param id
	 * @throws IOException 
	 */
	public void exportById(OutputStream out,Long id) throws IOException;
	
}

