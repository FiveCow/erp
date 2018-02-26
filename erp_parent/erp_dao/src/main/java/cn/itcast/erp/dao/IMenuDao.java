package cn.itcast.erp.dao;

import java.util.List;

import cn.itcast.erp.entity.Menu;
/**
 * 菜单数据访问接口
 * @author Administrator
 *
 */
public interface IMenuDao extends IBaseDao<Menu>{
	
	/**
	 * 根据指定的用户ID查询菜单集合
	 * @param empUuid 用户ID
	 * @return
	 */
	public List<Menu> getMenusByEmpuuid(Long empUuid);
}
