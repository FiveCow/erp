package cn.itcast.erp.biz;

import java.util.Date;
import java.util.List;

/**
 * 报表业务逻辑接口
 * @author Administrator
 *
 */
public interface IReportBiz {

	/**
	 * 销售统计报表
	 * @return
	 */
	public List orderReport(Date date1,Date date2);
	
	/**
	 * 销售趋势报表
	 * @param year 年份
	 * @return
	 */
	public List trendReport(int year);
	
	/**
	 * 销售趋势折线图
	 * @param year
	 * @return
	 */
	public List trendChart(int year);
	
}
