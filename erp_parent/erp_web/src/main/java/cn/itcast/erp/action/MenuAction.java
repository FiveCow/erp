package cn.itcast.erp.action;
import java.util.List;

import com.alibaba.fastjson.JSON;

import cn.itcast.erp.biz.IMenuBiz;
import cn.itcast.erp.entity.Menu;

/**
 * 菜单Action 
 * @author Administrator
 *
 */
public class MenuAction extends BaseAction<Menu> {

	private IMenuBiz menuBiz;
	
	public void setMenuBiz(IMenuBiz menuBiz) {
		this.menuBiz = menuBiz;
		setBaseBiz(menuBiz);
	}
	
	/**
	 * 获取菜单树
	 */
	public void getMenuTree(){
		
		Menu menu = menuBiz.readMenuByEmpuuid(getUser().getUuid());
		String jsonString = JSON.toJSONString(menu,true);
		
		write(jsonString);		
	}
	
	
	public void getMenusByEmpuuid(){
		List<Menu> menus = menuBiz.getMenusByEmpuuid(getId());
		String jsonString = JSON.toJSONString(menus);
		write(jsonString);
	}
	
}
