package cn.itcast.erp.dao.impl;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import cn.itcast.erp.dao.IInventoryDao;
import cn.itcast.erp.entity.Inventory;
/**
 * 盘盈盘亏数据访问类
 * @author Administrator
 *
 */
public class InventoryDao extends BaseDao<Inventory> implements IInventoryDao {

	
	/**
	 * 构建查询条件
	 * @param dep1
	 * @param dep2
	 * @param param
	 * @return
	 */
	public DetachedCriteria getDetachedCriteria(Inventory inventory1,Inventory inventory2,Object param){
		DetachedCriteria dc=DetachedCriteria.forClass(Inventory.class);
		if(inventory1!=null){
			if(inventory1.getType()!=null &&  inventory1.getType().trim().length()>0)
			{
				dc.add(Restrictions.like("type", inventory1.getType(), MatchMode.ANYWHERE));			
			}
			if(inventory1.getState()!=null &&  inventory1.getState().trim().length()>0)
			{
				dc.add(Restrictions.like("state", inventory1.getState(), MatchMode.ANYWHERE));			
			}
			if(inventory1.getRemark()!=null &&  inventory1.getRemark().trim().length()>0)
			{
				dc.add(Restrictions.like("remark", inventory1.getRemark(), MatchMode.ANYWHERE));			
			}
		
		}		
		return dc;
	}
	
	
}

