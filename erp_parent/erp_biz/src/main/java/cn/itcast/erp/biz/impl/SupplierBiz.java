package cn.itcast.erp.biz.impl;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cn.itcast.erp.biz.ISupplierBiz;
import cn.itcast.erp.dao.ISupplierDao;
import cn.itcast.erp.entity.Supplier;
import cn.itcast.erp.exception.ErpException;
/**
 * 供应商业务逻辑类
 * @author Administrator
 *
 */
public class SupplierBiz extends BaseBiz<Supplier> implements ISupplierBiz {

	private ISupplierDao supplierDao;
	
	public void setSupplierDao(ISupplierDao supplierDao) {
		this.supplierDao = supplierDao;
		setBaseDao(supplierDao);
	}

	/**
	 * 数据导出
	 * @param out
	 * @throws IOException
	 */
	public void export(OutputStream out,Supplier t1) throws IOException{
		
		//创建工作簿
		HSSFWorkbook book=new HSSFWorkbook();
		String sheetName="";
		if(t1.getType().equals("1")){
			sheetName="供应商";
		}
		if(t1.getType().equals("2")){
			sheetName="客户";
		}	
		if(sheetName.equals("")){
			throw new ErpException("参数错误");
		}
		
		//创建工作表
		HSSFSheet sheet = book.createSheet(sheetName);
		//创建标题行
		HSSFRow titleRow = sheet.createRow(0);
		titleRow.createCell(0).setCellValue("名称");
		titleRow.createCell(1).setCellValue("地址");
		titleRow.createCell(2).setCellValue("联系人");
		titleRow.createCell(3).setCellValue("电话");
		titleRow.createCell(4).setCellValue("Email");
		
		//设置列宽
		sheet.setColumnWidth(0, 5000);
		sheet.setColumnWidth(1, 8000);
		sheet.setColumnWidth(2, 3000);
		sheet.setColumnWidth(3, 5000);
		sheet.setColumnWidth(4, 8000);
		
		//创建内容行
		List<Supplier> list = supplierDao.getList(t1, null, null);
		
		for(int i=0;i<list.size();i++){
			HSSFRow row = sheet.createRow(i+1);
			Supplier supplier = list.get(i);
			row.createCell(0).setCellValue(supplier.getName());
			row.createCell(1).setCellValue(supplier.getAddress());
			row.createCell(2).setCellValue(supplier.getContact());
			row.createCell(3).setCellValue(supplier.getTele());
			row.createCell(4).setCellValue(supplier.getEmail());
		}
		
		book.write(out);
		
	}
	
	/**
	 * 供应商及客户数据导入
	 * @param input
	 * @throws IOException 
	 */
	public void doImport(InputStream input) throws IOException{
		//根据输入流得到工作簿对象
		HSSFWorkbook book=new HSSFWorkbook(input);
		HSSFSheet sheet = book.getSheetAt(0);//得到第一个工作表
		
		String sheetName=sheet.getSheetName();
		String type="";
		if(sheetName.equals("供应商")){
			type="1";
		}else if(sheetName.equals("客户")){
			type="2";
		}else
		{
			throw new ErpException("工作表名称不正确，请核实数据正确性");
		}
				
		for(int i=1;i<=sheet.getLastRowNum();i++){
			
			HSSFRow row = sheet.getRow(i);
			Supplier supplier=new Supplier();
			//判断名称是否存在
			supplier.setName(row.getCell(0).getStringCellValue());//名称
			
			List<Supplier> list = supplierDao.getList(null, supplier, null);
			
			if(list.size()>0){
				supplier=list.get(0);
			}			
			supplier.setAddress(row.getCell(1).getStringCellValue());//地址
			supplier.setContact(row.getCell(2).getStringCellValue());//联系人
			
			row.getCell(3).setCellType(HSSFCell.CELL_TYPE_STRING);//设置类型为文本类型
			supplier.setTele(row.getCell(3).getStringCellValue());//电话
			supplier.setEmail(row.getCell(4).getStringCellValue());//EMAI
			supplier.setType(type);//类型
			
			if(list.size()==0){
				supplierDao.add(supplier);
			}
			
		}
		
	}
	
}
