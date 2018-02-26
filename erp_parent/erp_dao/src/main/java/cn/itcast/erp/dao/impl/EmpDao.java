package cn.itcast.erp.dao.impl;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.entity.Emp;
/**
 * 员工数据访问类
 * @author Administrator
 *
 */
public class EmpDao extends BaseDao<Emp> implements IEmpDao {

	
	/**
	 * 构建查询条件
	 * @param dep1
	 * @param dep2
	 * @param param
	 * @return
	 */
	public DetachedCriteria getDetachedCriteria(Emp emp1,Emp emp2,Object param){
		DetachedCriteria dc=DetachedCriteria.forClass(Emp.class);
		if(emp1!=null){
			if(emp1.getUsername()!=null &&  emp1.getUsername().trim().length()>0)
			{
				dc.add(Restrictions.like("username", emp1.getUsername(), MatchMode.ANYWHERE));			
			}
			if(emp1.getPwd()!=null &&  emp1.getPwd().trim().length()>0)
			{
				dc.add(Restrictions.like("pwd", emp1.getPwd(), MatchMode.ANYWHERE));			
			}
			if(emp1.getName()!=null &&  emp1.getName().trim().length()>0)
			{
				dc.add(Restrictions.like("name", emp1.getName(), MatchMode.ANYWHERE));			
			}
			if(emp1.getEmail()!=null &&  emp1.getEmail().trim().length()>0)
			{
				dc.add(Restrictions.like("email", emp1.getEmail(), MatchMode.ANYWHERE));			
			}
			if(emp1.getTele()!=null &&  emp1.getTele().trim().length()>0)
			{
				dc.add(Restrictions.like("tele", emp1.getTele(), MatchMode.ANYWHERE));			
			}
			if(emp1.getAddress()!=null &&  emp1.getAddress().trim().length()>0)
			{
				dc.add(Restrictions.like("address", emp1.getAddress(), MatchMode.ANYWHERE));			
			}
			if(emp1.getGender()!=null){
				dc.add(Restrictions.eq("gender", emp1.getGender()))	;	
			}
			
			if(emp1.getBirthday()!=null){
				dc.add(Restrictions.ge("birthday", emp1.getBirthday()));				
			}
			
			//按部门搜索
			if(emp1.getDep()!=null && emp1.getDep().getUuid()!=null){
				dc.add(Restrictions.eq("dep", emp1.getDep()));
			}
			
		}	
		if(emp2!=null){
			
			if(emp2.getBirthday()!=null){
				dc.add(Restrictions.le("birthday", emp2.getBirthday()));
			}			
		}
		
		return dc;
	}
	
	/**
	 * 根据用户名和密码查询用户
	 * @param username 用户名
	 * @param pwd 密码
	 * @return 用户信息
	 */
	public Emp findByUsernameAndPwd(String username,String pwd){
		String hql="from Emp where username=? and pwd=?";
		List<Emp> list = (List<Emp>) getHibernateTemplate().find(hql, username,pwd);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}		
	}
	
}

