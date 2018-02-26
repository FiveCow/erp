package cn.itcast.erp.dao;

import cn.itcast.erp.entity.Emp;
/**
 * 员工数据访问接口
 * @author Administrator
 *
 */
public interface IEmpDao extends IBaseDao<Emp>{
	
	/**
	 * 根据用户名和密码查询用户
	 * @param username 用户名
	 * @param pwd 密码
	 * @return 用户信息
	 */
	public Emp findByUsernameAndPwd(String username,String pwd);
}
