package cn.itcast.erp.biz.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.itcast.erp.biz.IReportBiz;
import cn.itcast.erp.dao.IReportDao;
/**
 * 报表业务逻辑类
 * @author Administrator
 *
 */
public class ReportBiz implements IReportBiz {

	private IReportDao  reportDao;
	
	public void setReportDao(IReportDao reportDao) {
		this.reportDao = reportDao;
	}

	/**
	 * 销售统计表
	 */
	public List orderReport(Date date1,Date date2) {
		//date1和date2必须同时为null或同时有值
		if( date1==null  || date2==null ){
			return reportDao.orderReport();
		}else{
			return reportDao.orderReport(date1,date2);
		}		
	}
	
	/**
	 * 销售趋势报表
	 * @param year 年份
	 * @return
	 */
	public List trendReport(int year){
		//如果year变量为0，让其等于当前年份
		if(year==0){
			year=util.DateUtil.getYear();//获取当前年份			
		}
				
		List list=new ArrayList();
		for(int i=1;i<=12;i++){
			
			Double sumMoney = reportDao.getSumMoney(year, i);
			Map map=new HashMap();
			map.put("month", i+"月");
			map.put("money", sumMoney);
			list.add(map);
		}
		return list;
	}
	
	
	/**
	 * 销售趋势折线图
	 * @param year
	 * @return
	 */
	public List trendChart(int year){
		//如果year变量为0，让其等于当前年份
		if(year==0){
			year=util.DateUtil.getYear();//获取当前年份			
		}
		
		Double[] moneys=new Double[12];		
		for(int i=0;i<12;i++){
			moneys[i]=reportDao.getSumMoney(year, i+1);
		}
		Map map=new HashMap();
		map.put("name", "全部商品");
		map.put("data", moneys);
		
		List list=new ArrayList();
		list.add(map);
		return list;		
	}

}
