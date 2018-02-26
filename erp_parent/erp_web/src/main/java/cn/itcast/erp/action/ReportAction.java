package cn.itcast.erp.action;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;

import cn.itcast.erp.biz.IReportBiz;

/**
 * 报表action
 * @author Administrator
 *
 */
public class ReportAction {
	
	private IReportBiz reportBiz;
		
	public void setReportBiz(IReportBiz reportBiz) {
		this.reportBiz = reportBiz;
	}

	private Date date1;//开始日期
	private Date date2;//截止日期
	
	public void setDate1(Date date1) {
		this.date1 = date1;
	}

	public void setDate2(Date date2) {
		this.date2 = date2;
	}

	/**
	 * 销售统计报表
	 */
	public void orderReport(){
		List list = reportBiz.orderReport(date1, date2);
		String jsonString = JSON.toJSONString(list);
		write(jsonString);
	}

	private int year;//年份
		
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * 销售趋势分析
	 */
	public void trendReport(){
		
		List list = reportBiz.trendReport(year);
		String jsonString = JSON.toJSONString(list);
		write(jsonString);
	}
	
	/**
	 * 销售趋势分析（折线图）
	 */
	public void trendChart(){
		List list = reportBiz.trendChart(year);
		String jsonString = JSON.toJSONString(list);
		write(jsonString);
	}
	
	/**
	 * 输出字符串
	 * @param jsonString
	 */
	public void write(String jsonString){

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");		
		try {
			response.getWriter().print(jsonString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
}
