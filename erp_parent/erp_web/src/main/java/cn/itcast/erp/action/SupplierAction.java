package cn.itcast.erp.action;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import cn.itcast.erp.biz.ISupplierBiz;
import cn.itcast.erp.entity.Supplier;
import cn.itcast.erp.exception.ErpException;

/**
 * 供应商Action 
 * @author Administrator
 *
 */
public class SupplierAction extends BaseAction<Supplier> {

	private ISupplierBiz supplierBiz;
	
	public void setSupplierBiz(ISupplierBiz supplierBiz) {
		this.supplierBiz = supplierBiz;
		setBaseBiz(supplierBiz);
	}
	
	
	public void list(){
		
		if(getT1()==null){
			setT1(new Supplier());
		}		
		getT1().setName(getQ());
		
		super.list();
		
	}
	
	
	/**
	 * 数据导出
	 * @throws IOException 
	 */
	public void export() throws IOException{
		
		HttpServletResponse response = ServletActionContext.getResponse();
		//为啥没识别中文文件名？  因为我们工程的编码采用UTF-8  ,网络传输的编码ISO-8859-1
		String name="";
		if(getT1().getType().equals("1")){
			name="供应商.xls";
		}
		if(getT1().getType().equals("2")){
			name="客户.xls";
		}		
		String fileName=new String(name.getBytes(),"ISO-8859-1");
		response.setHeader("Content-Disposition", "attachment;filename="+fileName);
		ServletOutputStream out = response.getOutputStream();
		supplierBiz.export(out,getT1());
		
	}
	
	private File file;//上传文件对象
	private String fileFileName;//文件名
	private String fileContentType;//文件类型
	
	public void setFile(File file) {
		this.file = file;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

	
	/**
	 * 文件上传
	 */
	public void upload(){
		System.out.println(fileContentType);
		if(!fileContentType.equals("application/vnd.ms-excel")){
			write(ajaxReturn(false, "请上传excel文件"));
			return;
		}
		
		try {
			supplierBiz.doImport(new FileInputStream(file));
			write(ajaxReturn(true, "上传成功"));
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
			write(ajaxReturn(false, "上传的文件不存在"));
		} catch (IOException e) {
			
			e.printStackTrace();
			write(ajaxReturn(false, "IO异常"));
		}catch (ErpException e) {
			
			e.printStackTrace();
			write(ajaxReturn(false, e.getMessage()));
		}catch (Exception e) {			
			e.printStackTrace();
			write(ajaxReturn(false, e.getMessage()));
		}
	}
	
	
}
