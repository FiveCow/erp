$(function(){
	
	$("#grid").datagrid({
		url:'emp_listByPage.action',
		columns:[[
		  		    {field:'uuid',title:'编号',width:100},
		  		    {field:'username',title:'登陆名',width:100},
		  		  
		  		    {field:'name',title:'真实姓名',width:100},
		  		    {field:'gender',title:'性别',width:100,formatter:function(value){
		  		    	if(value==1){
		  		    		return '男';
		  		    	}
		  		    	if(value==0){
		  		    		return '女';
		  		    	}
		  		    }},
		  		    {field:'email',title:'EMAIL',width:100},
		  		    {field:'tele',title:'电话',width:100},
		  		    {field:'address',title:'地址',width:100},
		  		    {field:'birthday',title:'出生年月日',width:100,formatter:function(value){
		  		    	return new Date(value).Format('yyyy-MM-dd');
		  		    }},
		  		    {field:'dep',title:'部门',width:100,formatter:function(value){
		  		    	if(value!=null){
		  		    		return value.name;
		  		    	}
		  		    }},

				    {field:'-',title:'操作',width:200,formatter:function(value,row,index)
				    	{
				    		return "<a href='#' onclick='updatePwd_reset("+row.uuid+")'>重置密码</a>";
				    	}}		    
				          ]],
				     singleSelect:true,
				     pagination:true
		
	});
	
	//保存新密码
	$('#btnSave').bind('click',function(){
		
		var formdata= $('#updatePwdForm').serializeJSON(); 
		
		$.ajax({
			url:'emp_updatePwd_reset.action',
			data:formdata,
			dataType:'json',
			type:'post',
			success:function(value){
				
				if(value.success){
					$('#updatePwdWindow').window('close');
				}
				$.messager.alert('提示',value.message);
			}		
			
		});
		
	});
	
});
/**
 * 重置密码
 */
function updatePwd_reset(id){
	
	$('#updatePwdWindow').window('open');
	//将ID值赋给form
	//$('#uuid').val(id);
	$('#updatePwdForm').form('load',{"id":id,"newPwd":""});
	
}
