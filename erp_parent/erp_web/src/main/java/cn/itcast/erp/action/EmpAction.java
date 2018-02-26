package cn.itcast.erp.action;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionContext;

import cn.itcast.erp.biz.IEmpBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Tree;
import cn.itcast.erp.exception.ErpException;

/**
 * 员工Action 
 * @author Administrator
 *
 */
public class EmpAction extends BaseAction<Emp> {

	private IEmpBiz empBiz;
	
	public void setEmpBiz(IEmpBiz empBiz) {
		this.empBiz = empBiz;
		setBaseBiz(empBiz);
	}
	
	private String oldPwd;//原密码
	private String newPwd;//新密码
	
	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}
	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}




	/**
	 * 修改密码
	 */
	public void updatePwd(){
		
		try {
			empBiz.updatePwd(getUser().getUuid(), oldPwd, newPwd);
			write(ajaxReturn(true, "密码修改成功"));
		} catch (ErpException e) {			
			write(ajaxReturn(false, e.getMessage() ));
		}catch (Exception e) {
			
			e.printStackTrace();
			write(ajaxReturn(false, "密码修改失败"));
		}
		
	}
	
	/**
	 *  重置密码
	 */
	public void updatePwd_reset(){
		
		try {
			empBiz.updatePwd_reset(getId(), newPwd);//getId()是BaseAction定义的方法
			write(ajaxReturn(true, "密码重置成功"));
		} catch (Exception e) {
			e.printStackTrace();
			write(ajaxReturn(false, "密码重置失败"));
		}
		
	}
	
	/**
	 * 读取用户角色
	 */
	public void readEmpRoles(){
		List<Tree> trees = empBiz.readEmpRoles(getId());
		String jsonString = JSON.toJSONString(trees);
		write(jsonString);
	}
	
	private String checkedStr;
		
	public void setCheckedStr(String checkedStr) {
		this.checkedStr = checkedStr;
	}
	
	/**
	 * 更新用户角色
	 */
	public void updateEmpRoles(){
		try {
			empBiz.updateEmpRoles(getId(), checkedStr);
			write(ajaxReturn(true, "更新用户角色成功"));
		} catch (Exception e) {
			
			e.printStackTrace();
			write(ajaxReturn(false, "更新用户角色失败"));
		}
		
	}
	
}
