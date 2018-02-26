package cn.itcast.erp.dao.impl;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import cn.itcast.erp.dao.IStoredetailDao;
import cn.itcast.erp.entity.StoreAlert;
import cn.itcast.erp.entity.Storedetail;
/**
 * 仓库库存数据访问类
 * @author Administrator
 *
 */
public class StoredetailDao extends BaseDao<Storedetail> implements IStoredetailDao {

	
	/**
	 * 构建查询条件
	 * @param dep1
	 * @param dep2
	 * @param param
	 * @return
	 */
	public DetachedCriteria getDetachedCriteria(Storedetail storedetail1,Storedetail storedetail2,Object param){
		DetachedCriteria dc=DetachedCriteria.forClass(Storedetail.class);
		if(storedetail1!=null){
			
			if(storedetail1.getGoodsuuid()!=null){
				dc.add(Restrictions.eq("goodsuuid", storedetail1.getGoodsuuid()));
			}
			if(storedetail1.getStoreuuid()!=null){
				dc.add(Restrictions.eq("storeuuid", storedetail1.getStoreuuid()));
			}
			
		}		
		return dc;
	}

	/**
	 * 库存预警报表
	 */
	public List<StoreAlert> getStoreAlertList() {
		// TODO Auto-generated method stub
		return (List<StoreAlert>) getHibernateTemplate().find("from StoreAlert where storenum<outnum");
	}
	
	
}

