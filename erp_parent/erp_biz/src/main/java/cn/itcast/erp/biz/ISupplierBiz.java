package cn.itcast.erp.biz;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.itcast.erp.entity.Supplier;
/**
 * 供应商业务逻辑层接口
 * @author Administrator
 *
 */
public interface ISupplierBiz extends IBaseBiz<Supplier>{
	
	/**
	 * 数据导出
	 * @param out
	 * @throws IOException
	 */
	public void export(OutputStream out,Supplier t1) throws IOException;
	
	
	/**
	 * 供应商及客户数据导入
	 * @param input
	 * @throws IOException 
	 */
	public void doImport(InputStream input) throws IOException;
}

