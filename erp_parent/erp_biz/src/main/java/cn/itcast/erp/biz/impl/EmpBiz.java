package cn.itcast.erp.biz.impl;
import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.crypto.hash.Md5Hash;

import cn.itcast.erp.biz.IEmpBiz;
import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.dao.IRoleDao;
import cn.itcast.erp.dao.impl.RoleDao;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Role;
import cn.itcast.erp.entity.Tree;
import cn.itcast.erp.exception.ErpException;
import redis.clients.jedis.Jedis;
/**
 * 员工业务逻辑类
 * @author Administrator
 *
 */
public class EmpBiz extends BaseBiz<Emp> implements IEmpBiz {

	private IEmpDao empDao;
	
	public void setEmpDao(IEmpDao empDao) {
		this.empDao = empDao;
		setBaseDao(empDao);
	}

	private IRoleDao roleDao;//角色DAO
		
	public void setRoleDao(IRoleDao roleDao) {
		this.roleDao = roleDao;
	}

	private Jedis jedis;
		
	public void setJedis(Jedis jedis) {
		this.jedis = jedis;
	}

	/**
	 * 根据用户名和密码查询用户信息 
	 */
	public Emp findByUsernameAndPwd(String username, String pwd) {
		
		//对用户输入的密码进行加密		
		Md5Hash md5=new Md5Hash(pwd, username, 2);
		System.out.println( md5.toString());
		return empDao.findByUsernameAndPwd(username, md5.toString());
	}

	/**
	 * 增加员工
	 */
	public void add(Emp emp){
		
		//对密码进行加密
		//参数1：source  原密码
		//参数2：salt    盐      增加破解难度
		//参数3：hashIterations 散列次数   1  加密（原）  2   加密（ 加密（原））
		//让新增员工的初始密码与登陆名相同
		Md5Hash md5=new Md5Hash(emp.getUsername(), emp.getUsername(), 2);
		
		emp.setPwd(md5.toString());//将加密后的密码存回去
		
		empDao.add(emp);
	}
	
	/**
	 * 修改密码
	 * @param id
	 * @param oldPwd
	 * @param newPwd
	 */
	public void updatePwd(Long id,String oldPwd,String newPwd){
		
		//校验原密码是否正确
		Emp emp = empDao.get(id);
		Md5Hash md5_1=new Md5Hash(oldPwd, emp.getUsername(), 2);
		if(!emp.getPwd().equals( md5_1.toString() ) ){//如果旧密码不匹配
			//抛出自定义异常的方式来通知该方法的调用端，我这里没有修改密码
			throw new ErpException("原密码不正确！");
		}
		//修改密码
		
		Md5Hash md5_2=new Md5Hash(newPwd, emp.getUsername(), 2);
		emp.setPwd(  md5_2.toString() );
		
		
	}

	/**
	 * 管理员重置密码
	 */
	public void updatePwd_reset(Long id, String newPwd) {
	
		Emp emp = empDao.get(id);		
		//修改密码		
		Md5Hash md5_2=new Md5Hash(newPwd, emp.getUsername(), 2);
		emp.setPwd(  md5_2.toString() );
		
	}
	
	/**
	 * 读取用户角色
	 * @param uuid 用户ID
	 * @return
	 */
	public List<Tree> readEmpRoles(Long uuid){
		
		List<Tree> trees=new ArrayList();
		
		List<Role> roles = empDao.get(uuid).getRoles();//当前被操作用户的角色
		
		//读取全部角色
		List<Role> roleList = roleDao.getList(null, null, null);
		
		for(Role role:roleList){
			
			Tree tree=new Tree();
			tree.setId(String.valueOf(role.getUuid())); 
			tree.setText(role.getName());
			
			if(roles.contains(role)){
				tree.setChecked(true);
			}			
			trees.add(tree);
		}
		return trees;
	}
	
	/**
	 * 更新用户角色
	 * @param uuid 员工ID
	 * @param checkedStr
	 */
	public void updateEmpRoles(Long uuid,String checkedStr){
		
		String[] roleIds = checkedStr.split(",");
		
		Emp emp = empDao.get(uuid);
		emp.setRoles(new ArrayList());//清空原有角色
		for(String roleId:roleIds){
			
			Role role = roleDao.get(Long.valueOf(roleId));//必须转换类型
			emp.getRoles().add(role);//赋予用户角色
		}
		
		//清除该用户在redis中的菜单缓存数据
		try {
			jedis.del("menuList_"+uuid);
			System.out.println("清除了menuList_"+uuid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
