var id;

var clickRow=function(rowIndex,rowData){
	id=rowData.uuid;
	$('#tree').tree({
		url:'role_readRoleMenus.action?id='+id,
		animate:true,
		checkbox:true
	});
};

/**
 * 保存选中的节点
 */
function save(){
	
   var nodes=$('#tree').tree('getChecked');//获取选中的节点集合
   
   var checkedStr="";
   for(var i=0;i<nodes.length;i++){
	   if(i>0){
		   checkedStr += ","; 
	   }	   
	   checkedStr += nodes[i].id ;
   }
  
   $.ajax({
	   url:'role_updateRoleMenus.action',
	   dataType:'json',
	   type:'post',
	   data:{"id": id ,"checkedStr":checkedStr},
	   success:function(value){		   
		   $.messager.alert('提示',value.message);		   
	   }
	   
	   
   });

 
   
	
}