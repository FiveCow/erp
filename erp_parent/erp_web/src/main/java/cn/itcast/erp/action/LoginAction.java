package cn.itcast.erp.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionContext;

import cn.itcast.erp.biz.IEmpBiz;
import cn.itcast.erp.entity.Emp;

/**
 * 登陆action
 * @author Administrator
 *
 */
public class LoginAction {

	private IEmpBiz empBiz;

	public void setEmpBiz(IEmpBiz empBiz) {
		this.empBiz = empBiz;
	}
	
	private String username;//用户名
	private String pwd;//密码
	
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	/**
	 * 登陆检查
	 */
	public void checkUser(){
		//通过用户名和密码查询 用户对象
		/*
		Emp emp = empBiz.findByUsernameAndPwd(username, pwd);		
		if(emp==null){//没有这个用户名和密码
			write(ajaxReturn(false, "用户名和密码错误"));
		}else{
			write(ajaxReturn(true, "登陆成功"));
			//将emp对象存入session
			ActionContext.getContext().getSession().put("user", emp);
		}
		*/
		
		//1.创建令牌  （对用户名和密码的封装）
		UsernamePasswordToken token=new UsernamePasswordToken(username, pwd);
		//2.得到subject (shiro组件之一 ：提供当前用户相关的一组操作 ，包括登陆 )
		Subject subject = org.apache.shiro.SecurityUtils.getSubject();
		//3.执行登陆
		try {
			subject.login(token);
			//将用户对象放入session
			//不推荐的写法：ActionContext.getContext().getSession().put("user", subject.getPrincipal());
			
			write(ajaxReturn(true, "登陆成功"));
		} catch (AuthenticationException e) {
			e.printStackTrace();
			write(ajaxReturn(false, "用户名和密码错误"));
		}
		
		
	}
	
	/**
	 * 显示当前登陆人名称
	 */
	public void showName(){
		
		Emp emp= (Emp) SecurityUtils.getSubject().getPrincipal();//提取主角对象
		//Emp emp= (Emp) ActionContext.getContext().getSession().get("user");
		if(emp==null){
			write(ajaxReturn(false, ""));
		}else
		{
			write(ajaxReturn(true, emp.getName()));			
		}
	}
	
	/**
	 * 退出登陆
	 */
	public void loginOut(){
		
		//ActionContext.getContext().getSession().remove("user");//从session中移除user
		
		SecurityUtils.getSubject().logout();//退出登陆
		
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
	
	/**
	 * 标准结构返回体
	 * @param success
	 * @param message
	 * @return
	 */
	public String ajaxReturn(boolean success,String message){
		
		Map map=new HashMap();
		map.put("success", success);
		map.put("message", message);
		return JSON.toJSONString(map);		
	}
	
}
