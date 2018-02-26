package cn.itcast.erp.dao.impl;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import cn.itcast.erp.dao.IGoodsDao;
import cn.itcast.erp.entity.Goods;
/**
 * 商品数据访问类
 * @author Administrator
 *
 */
public class GoodsDao extends BaseDao<Goods> implements IGoodsDao {

	
	/**
	 * 构建查询条件
	 * @param dep1
	 * @param dep2
	 * @param param
	 * @return
	 */
	public DetachedCriteria getDetachedCriteria(Goods goods1,Goods goods2,Object param){
		DetachedCriteria dc=DetachedCriteria.forClass(Goods.class);
		if(goods1!=null){
			if(goods1.getName()!=null &&  goods1.getName().trim().length()>0)
			{
				dc.add(Restrictions.like("name", goods1.getName(), MatchMode.ANYWHERE));			
			}
			if(goods1.getOrigin()!=null &&  goods1.getOrigin().trim().length()>0)
			{
				dc.add(Restrictions.like("origin", goods1.getOrigin(), MatchMode.ANYWHERE));			
			}
			if(goods1.getProducer()!=null &&  goods1.getProducer().trim().length()>0)
			{
				dc.add(Restrictions.like("producer", goods1.getProducer(), MatchMode.ANYWHERE));			
			}
			if(goods1.getUnit()!=null &&  goods1.getUnit().trim().length()>0)
			{
				dc.add(Restrictions.like("unit", goods1.getUnit(), MatchMode.ANYWHERE));			
			}
			//商品类型搜索
			if(goods1.getGoodstype()!=null && goods1.getGoodstype().getUuid()!=null){
				dc.add(Restrictions.eq("goodstype", goods1.getGoodstype()));
			}
			
			
		}		
		return dc;
	}
	
	
}

