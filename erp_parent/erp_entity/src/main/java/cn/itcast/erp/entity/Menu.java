package cn.itcast.erp.entity;

import java.util.List;
import java.util.Set;

/**
 * 菜单实体类
 * @author Administrator *
 */
public class Menu {	
	private String menuid;//菜单ID
	private String menuname;//菜单名称
	private String icon;//图标
	private String url;//URL
	//private String pid;//上级菜单ID
	
	private List<Menu> menus;//下级菜单 ，因为我们要让菜单是有顺序地显示，所以我们用List集合
	
	public List<Menu> getMenus() {
		return menus;
	}
	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}
	
	public String getMenuid() {		
		return menuid;
	}
	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}
	public String getMenuname() {		
		return menuname;
	}
	public void setMenuname(String menuname) {
		this.menuname = menuname;
	}
	public String getIcon() {		
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getUrl() {		
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

}
