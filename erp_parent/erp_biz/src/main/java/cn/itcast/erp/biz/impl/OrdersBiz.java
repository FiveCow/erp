package cn.itcast.erp.biz.impl;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;

import cn.itcast.erp.biz.IOrdersBiz;
import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.dao.IOrdersDao;
import cn.itcast.erp.dao.ISupplierDao;
import cn.itcast.erp.entity.Orderdetail;
import cn.itcast.erp.entity.Orders;
import cn.itcast.erp.exception.ErpException;
/**
 * 订单业务逻辑类
 * @author Administrator
 *
 */
public class OrdersBiz extends BaseBiz<Orders> implements IOrdersBiz {

	private IOrdersDao ordersDao;
	
	public void setOrdersDao(IOrdersDao ordersDao) {
		this.ordersDao = ordersDao;
		setBaseDao(ordersDao);
	}
	
	private ISupplierDao supplierDao;//供应商数据访问层
	
	public void setSupplierDao(ISupplierDao supplierDao) {
		this.supplierDao = supplierDao;
	}

	private IEmpDao empDao;//员工数据访问层
		
	public void setEmpDao(IEmpDao empDao) {
		this.empDao = empDao;
	}

	/**
	 * 保存订单业务逻辑(采购申请、销售订单录入)
	 */
	public void add(Orders orders){
		
		if(!orders.getType().equals("1") && !orders.getType().equals("2") ){
			throw new ErpException("参数非法");
		}
		
		Subject subject = org.apache.shiro.SecurityUtils.getSubject();
		if(orders.getType().equals("1")){//采购申请
			if(!subject.isPermitted("采购申请")){
				throw new ErpException("当前用户没有采购申请权限");
			}			
		}
		if(orders.getType().equals("2")){//销售订单录入
			if(!subject.isPermitted("销售订单录入")){
				throw new ErpException("当前用户没有销售订单录入权限");
			}			
		}
		
		orders.setCreatetime(new Date());//订单创建日期
		//orders.setType("1");//订单类型 1：采购订单  2：销售订单
		orders.setState("0");//订单状态  0：未审核  1：已审核 2：已确认  3：已入库
		
		//合计数计算
		double totalmoney=0;
		//订单明细记录
		for(Orderdetail orderdetail:orders.getOrderdetails()){
			totalmoney+=orderdetail.getMoney();		
			orderdetail.setState("0");//订单明细的状态
		}	
		orders.setTotalmoney(totalmoney);
		
		ordersDao.add(orders);
	}
	

	

	/**
	 * 订单审核
	 */
	@RequiresPermissions("采购审核")
	public void doCheck(Long uuid, Long empUuid) {
		
		Orders orders = ordersDao.get(uuid);
		orders.setChecktime(new Date());//审核日期
		orders.setChecker(empUuid);//审核人
		orders.setState("1");//状态为1表示已审核
	}

	/**
	 * 订单确认
	 */
	@RequiresPermissions("采购确认")
	public void doStart(Long uuid, Long empUuid) {
		Orders orders = ordersDao.get(uuid);
		orders.setStarttime(new Date());//确认日期
		orders.setStarter(empUuid);//确认人
		orders.setState("2");//状态为2表示已确认
		
	}
	
	/**
	 * 导出采购单或销售单
	 * @param out
	 * @param id
	 * @throws IOException 
	 */
	public void exportById(OutputStream out,Long id) throws IOException{
		
		HSSFWorkbook book=new HSSFWorkbook();//工作簿
		HSSFSheet sheet = book.createSheet("采购单");//工作表
		
		Orders orders = ordersDao.get(id);//根据ID查询订单
		int rowCount=10+orders.getOrderdetails().size();
		
		/***** 设置边框 ******/
		//内容样式
		HSSFCellStyle style_content = book.createCellStyle();
		style_content.setBorderBottom(HSSFCellStyle.BORDER_THIN);//底端边框
		style_content.setBorderTop(HSSFCellStyle.BORDER_THIN);//顶端边框
		style_content.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左侧边框
		style_content.setBorderRight(HSSFCellStyle.BORDER_THIN);//右侧边框
		
		for(int i=2;i<rowCount;i++){
			HSSFRow row = sheet.createRow(i);//创建内容行			
			for(int j=0;j<4;j++){
				HSSFCell cell = row.createCell(j);//创建单元格
				cell.setCellStyle(style_content);//为单元格设置样式			
			}			
		}	
		
		/****  合并单元格  *****/
		//参数： 起始行,结束行，起始列，结束列
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));//标题行合并
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 3));//供应商名称合并
		sheet.addMergedRegion(new CellRangeAddress(7, 7, 0, 3));//订单明细标题合并
		
		/****  设置单元格的值  ******/
		sheet.createRow(0).createCell(0).setCellValue("采购单");
		
		sheet.getRow(2).getCell(0).setCellValue("供应商");
		sheet.getRow(3).getCell(0).setCellValue("下单日期");
		sheet.getRow(4).getCell(0).setCellValue("审核日期");
		sheet.getRow(5).getCell(0).setCellValue("确认日期");
		sheet.getRow(6).getCell(0).setCellValue("入库日期");		
		sheet.getRow(3).getCell(2).setCellValue("经办人");
		sheet.getRow(4).getCell(2).setCellValue("经办人");
		sheet.getRow(5).getCell(2).setCellValue("经办人");
		sheet.getRow(6).getCell(2).setCellValue("经办人");		
		sheet.getRow(7).getCell(0).setCellValue("订单明细");		
		sheet.getRow(8).getCell(0).setCellValue("商品名称");		
		sheet.getRow(8).getCell(1).setCellValue("价格");		
		sheet.getRow(8).getCell(2).setCellValue("数量");		
		sheet.getRow(8).getCell(3).setCellValue("金额");		
		sheet.getRow(rowCount-1).getCell(0).setCellValue("合计");	
		
		/***** 设置行高和列宽   ******/
		//设置列宽
		for(int i=0;i<4;i++){
			sheet.setColumnWidth(i, 5000);
		}
		//设置行高
		for(int i=2;i<rowCount;i++){			
			sheet.getRow(i).setHeight((short)500);
		}
		//设置标题行高
		sheet.getRow(0).setHeight((short)1000);
		
		
		/****  设置内容区对齐方式和字体   ******/
		style_content.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
		style_content.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
		//内容字体
		HSSFFont font_content = book.createFont();//创建字体类型
		font_content.setFontName("宋体");//设置字体名称
		font_content.setFontHeightInPoints((short)11);//设置字体大小		
		style_content.setFont(font_content);//将字体设置到样式(因为字体是样式的一部分)
		
		/****  设置标题的对其方式和字体  *****/
		HSSFCellStyle style_title = book.createCellStyle();
		style_title.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style_title.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
		sheet.getRow(0).getCell(0).setCellStyle(style_title);//将样式给标题的单元格
		//标题字体
		HSSFFont font_title = book.createFont();
		font_title.setFontName("黑体");
		font_title.setBold(true);//加粗
		font_title.setFontHeightInPoints((short)18);
		style_title.setFont(font_title);
		
		/**** 日期格式输出  *****/
		
		HSSFDataFormat dataFormat = book.createDataFormat();
		HSSFCellStyle style_date = book.createCellStyle();//创建带日期格式的样式
		style_date.cloneStyleFrom(style_content);//克隆样式
		
		style_date.setDataFormat(dataFormat.getFormat("yyyy-MM-dd hh:mm"));//日期格式
		
		sheet.getRow(3).getCell(1).setCellStyle(style_date);
		sheet.getRow(4).getCell(1).setCellStyle(style_date);
		sheet.getRow(5).getCell(1).setCellStyle(style_date);
		sheet.getRow(6).getCell(1).setCellStyle(style_date);
		
		
		/**** 向表格填入数据   *****/
		
		
		//供应商		
		sheet.getRow(2).getCell(1).setCellValue(supplierDao.get(  orders.getSupplieruuid()).getName() );
		//日期
		if(orders.getCreatetime()!=null){
			sheet.getRow(3).getCell(1).setCellValue(orders.getCreatetime());
		}
		if(orders.getChecktime()!=null){
			sheet.getRow(4).getCell(1).setCellValue(orders.getChecktime());
		}
		if(orders.getStarttime()!=null){
			sheet.getRow(5).getCell(1).setCellValue(orders.getStarttime());
		}
		if(orders.getEndtime()!=null){
			sheet.getRow(6).getCell(1).setCellValue(orders.getEndtime());
		}
		//经办人
		if(orders.getCreater()!=null){
			sheet.getRow(3).getCell(3).setCellValue( empDao.get(orders.getCreater()).getName() );
		}
		if(orders.getChecker()!=null){
			sheet.getRow(4).getCell(3).setCellValue( empDao.get(orders.getChecker()).getName());
		}
		if(orders.getStarter()!=null){
			sheet.getRow(5).getCell(3).setCellValue( empDao.get(orders.getStarter()).getName() );
		}
		if(orders.getEnder()!=null){
			sheet.getRow(6).getCell(3).setCellValue( empDao.get(orders.getEnder()).getName());
		}
				
		//订单明细
		int rowIndex=9;
		for( Orderdetail orderdetail:orders.getOrderdetails() ){
			
			sheet.getRow(rowIndex).getCell(0).setCellValue(orderdetail.getGoodsname());//商品名称
			sheet.getRow(rowIndex).getCell(1).setCellValue(orderdetail.getPrice());//价格 
			sheet.getRow(rowIndex).getCell(2).setCellValue(orderdetail.getNum());//数量
			sheet.getRow(rowIndex).getCell(3).setCellValue(orderdetail.getMoney());//金额	
			rowIndex++;
		}
		//合计
		sheet.getRow(rowIndex).getCell(3).setCellValue(orders.getTotalmoney());
		book.write(out);
		
		
	}
	
}
