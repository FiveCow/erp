package cn.itcast.erp.entity;

import java.util.ArrayList;
import java.util.List;
/**
 * 生成tree控件所需数据
 * @author Administrator
 *
 */
public class Tree {
	
	private String id;//节点ID
	private String text;//节点文本
	private boolean checked;//是否被选中
	private List<Tree>  children;//下级节点
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public List<Tree> getChildren() {
		if(children==null){
			children=new ArrayList();
		}		
		return children;
	}
	public void setChildren(List<Tree> children) {
		this.children = children;
	}
	
	
	
	

}
