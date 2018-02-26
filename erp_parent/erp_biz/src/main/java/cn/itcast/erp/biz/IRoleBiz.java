package cn.itcast.erp.biz;
import java.util.List;

import cn.itcast.erp.entity.Role;
import cn.itcast.erp.entity.Tree;
/**
 * 角色业务逻辑层接口
 * @author Administrator
 *
 */
public interface IRoleBiz extends IBaseBiz<Role>{
	
	/**
	 * 读取菜单树
	 * @return
	 */
	public List<Tree> readRoleMenus(Long uuid);
	
	/**
	 * 更新角色菜单
	 * @param uuid
	 * @param checkedStr  选中的菜单ID
	 */
	public void updateRoleMenus(Long uuid,String checkedStr);
	
}

