package cn.itcast.erp.dao.impl;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import cn.itcast.erp.dao.IOrderdetailDao;
import cn.itcast.erp.entity.Orderdetail;
/**
 * 订单明细数据访问类
 * @author Administrator
 *
 */
public class OrderdetailDao extends BaseDao<Orderdetail> implements IOrderdetailDao {

	
	/**
	 * 构建查询条件
	 * @param dep1
	 * @param dep2
	 * @param param
	 * @return
	 */
	public DetachedCriteria getDetachedCriteria(Orderdetail orderdetail1,Orderdetail orderdetail2,Object param){
		DetachedCriteria dc=DetachedCriteria.forClass(Orderdetail.class);
		if(orderdetail1!=null){
			if(orderdetail1.getGoodsname()!=null &&  orderdetail1.getGoodsname().trim().length()>0)
			{
				dc.add(Restrictions.like("goodsname", orderdetail1.getGoodsname(), MatchMode.ANYWHERE));			
			}
			if(orderdetail1.getState()!=null &&  orderdetail1.getState().trim().length()>0)
			{
				dc.add(Restrictions.like("state", orderdetail1.getState(), MatchMode.ANYWHERE));			
			}
			//实际上就是根据外键ordersuuid查询
			if(orderdetail1.getOrders()!=null && orderdetail1.getOrders().getUuid()!=null){
				dc.add(Restrictions.eq("orders", orderdetail1.getOrders()));
			}
			
		}		
		return dc;
	}
	
	
}

