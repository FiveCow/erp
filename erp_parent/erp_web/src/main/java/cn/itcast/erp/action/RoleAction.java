package cn.itcast.erp.action;
import java.util.List;

import com.alibaba.fastjson.JSON;

import cn.itcast.erp.biz.IRoleBiz;
import cn.itcast.erp.entity.Role;
import cn.itcast.erp.entity.Tree;

/**
 * 角色Action 
 * @author Administrator
 *
 */
public class RoleAction extends BaseAction<Role> {

	private IRoleBiz roleBiz;
	
	public void setRoleBiz(IRoleBiz roleBiz) {
		this.roleBiz = roleBiz;
		setBaseBiz(roleBiz);
	}
	
	/**
	 * 读取角色权限
	 */
	public void readRoleMenus(){
		
		List<Tree> list = roleBiz.readRoleMenus(getId());
		String jsonString = JSON.toJSONString(list);
		write(jsonString);
	}
	
	private String checkedStr;//选中的菜单ID串
		
	public void setCheckedStr(String checkedStr) {
		this.checkedStr = checkedStr;
	}

	/**
	 * 更新角色权限
	 */
	public void updateRoleMenus(){
		try {
			roleBiz.updateRoleMenus(getId(), checkedStr);
			write(ajaxReturn(true, "保存角色权限成功"));
			
		} catch (Exception e) {
			e.printStackTrace();
			write(ajaxReturn(false, "保存角色权限失败"));
		}
		
	}
	
}
