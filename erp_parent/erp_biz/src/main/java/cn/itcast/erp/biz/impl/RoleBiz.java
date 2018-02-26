package cn.itcast.erp.biz.impl;
import java.util.ArrayList;
import java.util.List;

import cn.itcast.erp.biz.IRoleBiz;
import cn.itcast.erp.dao.IMenuDao;
import cn.itcast.erp.dao.IRoleDao;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Menu;
import cn.itcast.erp.entity.Role;
import cn.itcast.erp.entity.Tree;
import redis.clients.jedis.Jedis;
/**
 * 角色业务逻辑类
 * @author Administrator
 *
 */
public class RoleBiz extends BaseBiz<Role> implements IRoleBiz {

	private IRoleDao roleDao;
	
	public void setRoleDao(IRoleDao roleDao) {
		this.roleDao = roleDao;
		setBaseDao(roleDao);
	}
	
	private IMenuDao menuDao;
	
	public void setMenuDao(IMenuDao menuDao) {
		this.menuDao = menuDao;
	}

	private Jedis jedis;
		
	public void setJedis(Jedis jedis) {
		this.jedis = jedis;
	}

	/**
	 * 读取角色权限
	 * @param uuid 角色ID
	 * @return
	 */
	public List<Tree> readRoleMenus(Long uuid){

		List<Menu> menus = roleDao.get(uuid).getMenus();//当前角色拥有的菜单
		
		List trees=new ArrayList();
		
		Menu menu0 = menuDao.get("0");
		//循环一级菜单
		for(Menu menu1:menu0.getMenus()){
			
			Tree tree1=new Tree();
			tree1.setId(menu1.getMenuid());
			tree1.setText(menu1.getMenuname());
			
			//循环二级菜单
			for(Menu menu2:menu1.getMenus()){
				Tree tree2=new Tree();
				tree2.setId(menu2.getMenuid());
				tree2.setText(menu2.getMenuname());
				
				if(menus.contains(menu2)){//如果当前二级菜单是当前角色拥有的菜单，则选中
					tree2.setChecked(true);
				}				
				tree1.getChildren().add(tree2);//将二级菜单加到一级菜单的集合中
			}		
			trees.add(tree1);//将一级菜单加到返回的tree集合中
		}
				
		return trees;
	}
	
	/**
	 * 更新角色菜单
	 * @param uuid
	 * @param checkedStr  选中的菜单ID
	 */
	public void updateRoleMenus(Long uuid,String checkedStr){
		//将每个选中的菜单加到当前角色的菜单集合中,实际上是把数据保存到中间表中
		Role role = roleDao.get(uuid);//当前角色
		
		role.setMenus(new ArrayList()); //清除掉该角色的所有权限
		
		String[] menuIds = checkedStr.split(",");//拆分
		
		for(String menuId:menuIds){
			
			Menu menu = menuDao.get(menuId);
			
			role.getMenus().add(menu);//赋权限
		}
		
		//清除该角色下的所有员工的菜单缓存
		
		for(Emp emp: role.getEmps()){			
			try {
				jedis.del("menuList_"+emp.getUuid());
				System.out.println("清除menuList_"+emp.getUuid());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
}
