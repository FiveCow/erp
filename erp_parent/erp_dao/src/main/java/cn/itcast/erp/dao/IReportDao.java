package cn.itcast.erp.dao;

import java.util.Date;
import java.util.List;

/**
 * 报表数据访问接口
 * @author Administrator
 *
 */
public interface IReportDao {
	
	/**
	 * 销售统计报表
	 * @return
	 */
	public List orderReport(Date date1,Date date2);
	
	/**
	 * 销售统计报表
	 * @return
	 */
	public List orderReport();
	
	/**
	 * 根据年和月统计销售额
	 * @param year 年份
	 * @param month 月份 
	 * @return 销售额合计
	 */
	public Double getSumMoney(int year,int month);

}
