package cn.itcast.erp.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import cn.itcast.erp.dao.IReportDao;
/**
 * 报表数据访问层
 * @author Administrator
 *
 */
public class ReportDao extends HibernateDaoSupport implements IReportDao {

	/**
	 * 销售统计报表
	 */
	public List orderReport(Date date1,Date date2) {
		String hql="select new Map( t.name as name,sum(d.money) as y)"
				+ "from Goods g,Goodstype t,Orderdetail d ,Orders o "
				+ "where g.goodstype=t and d.orders=o and d.goodsuuid=g.uuid "
				+ "and o.type='2' and o.createtime>=? and o.createtime<=? "
				+ "group by t.name";
				
		return getHibernateTemplate().find(hql,date1,date2);
	}
	
	/**
	 * 销售统计报表
	 */
	public List orderReport() {
		String hql="select new Map( t.name as name,sum(d.money) as y)"
				+ "from Goods g,Goodstype t,Orderdetail d ,Orders o "
				+ "where g.goodstype=t and d.orders=o and d.goodsuuid=g.uuid "
				+ "and o.type='2' "
				+ "group by t.name";
				
		return getHibernateTemplate().find(hql);
	}

	
	/**
	 * 根据年和月统计销售额
	 * @param year 年份
	 * @param month 月份 
	 * @return 销售额合计
	 */
	public Double getSumMoney(int year,int month){
		
		String hql="select sum(d.money) "
				+ "from Orderdetail d,Orders o "
				+ "where d.orders=o and o.type='2' and year(o.createtime)=? and month(o.createtime)=? ";
		List<Double> list = (List<Double>) getHibernateTemplate().find(hql, year,month);
		if(list.get(0)!=null){
			return list.get(0);
		}else
		{
			return 0.00;
		}		 
	}
	
	
}
