package cn.itcast.erp.dao.impl;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import cn.itcast.erp.dao.ISupplierDao;
import cn.itcast.erp.entity.Supplier;
/**
 * 供应商数据访问类
 * @author Administrator
 *
 */
public class SupplierDao extends BaseDao<Supplier> implements ISupplierDao {

	
	/**
	 * 构建查询条件
	 * @param dep1
	 * @param dep2
	 * @param param
	 * @return
	 */
	public DetachedCriteria getDetachedCriteria(Supplier supplier1,Supplier supplier2,Object param){
		DetachedCriteria dc=DetachedCriteria.forClass(Supplier.class);
		if(supplier1!=null){
			if(supplier1.getName()!=null &&  supplier1.getName().trim().length()>0)
			{
				dc.add(Restrictions.like("name", supplier1.getName(), MatchMode.ANYWHERE));			
			}
			if(supplier1.getAddress()!=null &&  supplier1.getAddress().trim().length()>0)
			{
				dc.add(Restrictions.like("address", supplier1.getAddress(), MatchMode.ANYWHERE));			
			}
			if(supplier1.getContact()!=null &&  supplier1.getContact().trim().length()>0)
			{
				dc.add(Restrictions.like("contact", supplier1.getContact(), MatchMode.ANYWHERE));			
			}
			if(supplier1.getTele()!=null &&  supplier1.getTele().trim().length()>0)
			{
				dc.add(Restrictions.like("tele", supplier1.getTele(), MatchMode.ANYWHERE));			
			}
			if(supplier1.getEmail()!=null &&  supplier1.getEmail().trim().length()>0)
			{
				dc.add(Restrictions.like("email", supplier1.getEmail(), MatchMode.ANYWHERE));			
			}
			if(supplier1.getType()!=null &&  supplier1.getType().trim().length()>0)
			{
				dc.add(Restrictions.like("type", supplier1.getType(), MatchMode.ANYWHERE));			
			}
		
		}	
		if(supplier2!=null){
			
			if(supplier2.getName()!=null && supplier2.getName().length()>0){
				dc.add(Restrictions.eq("name", supplier2.getName()));
			}			
		}
		
		return dc;
	}
	
	
}

