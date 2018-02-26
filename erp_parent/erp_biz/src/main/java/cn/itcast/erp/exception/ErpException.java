package cn.itcast.erp.exception;
/**
 * 自定义异常
 * @author Administrator
 *
 */
public class ErpException extends RuntimeException {

	
	public ErpException(String message){
		super(message);
	}
	
}
