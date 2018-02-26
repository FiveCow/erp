package cn.itcast.erp.realm;

import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import cn.itcast.erp.biz.IEmpBiz;
import cn.itcast.erp.biz.IMenuBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Menu;
/**
 * 自定义realm
 * @author Administrator
 *
 */
public class ErpRealm extends AuthorizingRealm {

	
	private IEmpBiz empBiz;
	
	public void setEmpBiz(IEmpBiz empBiz) {
		this.empBiz = empBiz;
	}

	private IMenuBiz menuBiz;
	
	public void setMenuBiz(IMenuBiz menuBiz) {
		this.menuBiz = menuBiz;
	}

	/**
	 * 授权  
	 */
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		System.out.println("执行了授权的方法");
		
		Emp emp= (Emp) principals.getPrimaryPrincipal(); 
		
		List<Menu> menus = menuBiz.getMenusByEmpuuid(emp.getUuid());//指定用户的菜单集合
		
		SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();//类似于集合,包含授权信息
		for(Menu menu:menus){
			info.addStringPermission(menu.getMenuname());
		}
				
		return info;//返回null 的意思，就是当前用户没有任何权限
	}

	/**
	 * 认证 返回null表示用户名密码错误，  如果返回AuthenticationInfo实例表示成功
	 */
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("执行了认证的方法....");
		
		UsernamePasswordToken uptoken=(UsernamePasswordToken)token;
		
		String pwd=new String( uptoken.getPassword());//密码
		
		Emp emp = empBiz.findByUsernameAndPwd(uptoken.getUsername(), pwd);//验证用户名和密码是否正确
		if(emp==null){
			
			return null;
		}
		//参数1  principal    主角（身份对象）
		//参数2  credentials  密码
		//参数3  realmName	 realm名称  
		return new SimpleAuthenticationInfo(emp, pwd, getName());
	}

}
