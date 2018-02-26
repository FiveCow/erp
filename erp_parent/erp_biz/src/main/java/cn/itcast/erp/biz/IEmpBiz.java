package cn.itcast.erp.biz;
import java.util.List;

import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Tree;
/**
 * 员工业务逻辑层接口
 * @author Administrator
 *
 */
public interface IEmpBiz extends IBaseBiz<Emp>{
	
	/**
	 * 根据用户名和密码查询用户
	 * @param username 用户名
	 * @param pwd 密码
	 * @return 用户信息
	 */
	public Emp findByUsernameAndPwd(String username,String pwd);
	
	/**
	 * 修改密码
	 * @param id
	 * @param oldPwd
	 * @param newPwd
	 */
	public void updatePwd(Long id,String oldPwd,String newPwd);
	
	
	/**
	 * 重置密码
	 * @param id
	 * @param oldPwd
	 * @param newPwd
	 */
	public void updatePwd_reset(Long id,String newPwd);
	
	
	/**
	 * 读取用户角色
	 * @param uuid 用户ID
	 * @return
	 */
	public List<Tree> readEmpRoles(Long uuid);
	
	
	/**
	 * 更新用户角色
	 * @param uuid
	 * @param checkedStr
	 */
	public void updateEmpRoles(Long uuid,String checkedStr);
}

