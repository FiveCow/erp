package cn.itcast.erp.biz.impl;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

import cn.itcast.erp.biz.IMenuBiz;
import cn.itcast.erp.dao.IMenuDao;
import cn.itcast.erp.entity.Menu;
import redis.clients.jedis.Jedis;
/**
 * 菜单业务逻辑类
 * @author Administrator
 *
 */
public class MenuBiz extends BaseBiz<Menu> implements IMenuBiz {

	private IMenuDao menuDao;
	
	public void setMenuDao(IMenuDao menuDao) {
		this.menuDao = menuDao;
		setBaseDao(menuDao);
	}
	
	private Jedis jedis;
	
	public void setJedis(Jedis jedis) {
		this.jedis = jedis;
	}

	/**
	 * 根据用户ID查询菜单集合
	 */
	public List<Menu> getMenusByEmpuuid(Long empUuid) {
		//思路：从jedis中提取数据，如果能提取出来就返回该数据，如果无法提取，就从数据库中查询，并放入缓存
		
		String menuListJson = jedis.get("menuList_"+empUuid);
		if(menuListJson!=null){//如果能够查询出来
			List<Menu> menuList = JSON.parseArray(menuListJson, Menu.class);
			System.out.println("从缓存中提取菜单数据");
			return menuList;
			
		}else//如果不能查询出来
		{			
			List<Menu> menuList = menuDao.getMenusByEmpuuid(empUuid);//从数据库中查询
			jedis.set("menuList_"+empUuid, JSON.toJSONString(menuList));
			System.out.println("从数据库中提取数据，并将数据存入缓存");
			return menuList;
		}
		
	}
	
	/**
	 * 克隆菜单属性
	 * @param menu
	 * @return
	 */
	private Menu createMenu(Menu menu){
		Menu m=new Menu();//创建新树根
		m.setMenuid(menu.getMenuid());
		m.setMenuname(menu.getMenuname());
		m.setIcon(menu.getIcon());
		m.setUrl(menu.getUrl());
		m.setMenus(new ArrayList()  );		
		return m;		
	}

	/**
	 * 根据用户ID查询菜单树
	 * @param empUuid
	 * @return
	 */
	public Menu readMenuByEmpuuid(Long empUuid){
		
		List<Menu> menus = menuDao.getMenusByEmpuuid(empUuid);//当前用户的菜单集合
		Menu menu0=menuDao.get("0");//查询全部的菜单树
		Menu m0=createMenu(menu0);//克隆根菜单
	
		for(Menu menu1:menu0.getMenus()){//循环一级菜单
			
			Menu m1=createMenu(menu1);//克隆一级菜单
			
			for(Menu menu2:menu1.getMenus()){//循环二级菜单
								
				if(menus.contains(menu2)){//判断当二级菜单在用户的菜单集合中出现
					Menu m2=createMenu(menu2);//克隆二级菜单
					m1.getMenus().add(m2);
				}				
			}			
			if( m1.getMenus().size()>0 ){//如果新的一级菜单下有二级菜单
				m0.getMenus().add(m1);
			}			
		}
		return m0;
	}
	
}
