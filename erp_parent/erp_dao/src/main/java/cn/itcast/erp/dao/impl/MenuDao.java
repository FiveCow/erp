package cn.itcast.erp.dao.impl;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import cn.itcast.erp.dao.IMenuDao;
import cn.itcast.erp.entity.Menu;
/**
 * 菜单数据访问类
 * @author Administrator
 *
 */
public class MenuDao extends BaseDao<Menu> implements IMenuDao {

	
	/**
	 * 构建查询条件
	 * @param dep1
	 * @param dep2
	 * @param param
	 * @return
	 */
	public DetachedCriteria getDetachedCriteria(Menu menu1,Menu menu2,Object param){
		DetachedCriteria dc=DetachedCriteria.forClass(Menu.class);
		if(menu1!=null){
			if(menu1.getMenuid()!=null &&  menu1.getMenuid().trim().length()>0)
			{
				dc.add(Restrictions.like("menuid", menu1.getMenuid(), MatchMode.ANYWHERE));			
			}
			if(menu1.getMenuname()!=null &&  menu1.getMenuname().trim().length()>0)
			{
				dc.add(Restrictions.like("menuname", menu1.getMenuname(), MatchMode.ANYWHERE));			
			}
			if(menu1.getIcon()!=null &&  menu1.getIcon().trim().length()>0)
			{
				dc.add(Restrictions.like("icon", menu1.getIcon(), MatchMode.ANYWHERE));			
			}
			if(menu1.getUrl()!=null &&  menu1.getUrl().trim().length()>0)
			{
				dc.add(Restrictions.like("url", menu1.getUrl(), MatchMode.ANYWHERE));			
			}
		
		
		}		
		return dc;
	}
	
	/**
	 * 根据指定的用户ID查询菜单集合
	 * @param empUuid 用户ID
	 * @return
	 */
	public List<Menu> getMenusByEmpuuid(Long empUuid){
		
		String hql="select m  from Emp e join e.roles r  join r.menus m where e.uuid=?";
		
		return  (List<Menu>) getHibernateTemplate().find(hql, empUuid);
	}
	
}

