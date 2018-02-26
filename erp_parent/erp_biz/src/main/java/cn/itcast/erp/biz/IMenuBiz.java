package cn.itcast.erp.biz;
import java.util.List;

import cn.itcast.erp.entity.Menu;
/**
 * 菜单业务逻辑层接口
 * @author Administrator
 *
 */
public interface IMenuBiz extends IBaseBiz<Menu>{
	
	/**
	 * 根据指定的用户ID查询菜单集合
	 * @param empUuid 用户ID
	 * @return
	 */
	public List<Menu> getMenusByEmpuuid(Long empUuid);
	
	/**
	 * 根据用户ID查询菜单树
	 * @param empUuid
	 * @return
	 */
	public Menu readMenuByEmpuuid(Long empUuid);
}

