
var method="";//保存提交的方法名称 
$(function(){
	
	//如果listParam没有定义，则赋予初始值  typeof()  用于判断变量的类型  
	if(typeof(listParam)=="undefined"){
		listParam='';
	}
	
	
	
	//如果saveParam没有定义，则赋予初始值   typeof()  用于判断变量的类型  
	if(typeof(saveParam)=='undefined'){
		saveParam='';
	}
	
	//表格数据初始化
	$('#grid').datagrid({
		url:name+'_listByPage.action'+listParam,
		columns:columns,
		singleSelect:true,
		pagination:true,
		toolbar: [{
			iconCls: 'icon-add',
			text:'增加',
			handler: function(){				
				$('#editWindow').window('open');
				$('#editForm').form('clear');
				method="add";
			}
		},{
			iconCls: 'icon-save',
			text:'导出',
			handler: function(){				
				var formdata= $('#searchForm').serializeJSON();
				$.download(name+'_export.action'+listParam,formdata);
			}
		},{
			iconCls: 'icon-tip',
			text:'导入',
			handler: function(){				
				
				$('#uploadWindow').window('open');		
				
			}
		}]

	});
	
	//条件查询
	$('#btnSearch').bind('click',function(){
		var formdata= $('#searchForm').serializeJSON();
		$('#grid').datagrid('load',formdata);		
	});
	
	//导入
	$('#btnUpload').bind('click',function(){
		//ajax文件上传
		
		$.ajax({
			url:name+'_upload.action',
			dataType:'json',
			type:'post',
			data:new FormData($('#uploadForm')[0]),
			processData:false,
			contentType:false,
			success:function(value){
				$.messager.alert('提示',value.message);
				if(value.success){
					$('#uploadWindow').window('close');
					$('#grid').datagrid('reload');					
				}				
			}
			
		});
		
	});
	
	
	//保存
	$('#btnSave').bind('click',function(){
		//前端验证
		var isValidate = $('#editForm').form('validate');
		if(isValidate==false){
			return ;
		}
		
		var formdata= $('#editForm').serializeJSON();	
		
		$.ajax({
			url:name+'_'+method+'.action'+saveParam,
			data:formdata,
			dataType:'json',
			type:'post',
			success:function(value){
				
				if(value.success){
					$('#editWindow').window('close');
					$('#grid').datagrid('reload');
				}
				$.messager.alert('提示',value.message);				
			}
			
		});
		
		
	});
	
	
});

/**
 * 删除 
 */
function dele(id){
	
	$.messager.confirm('提示','确定要删除此记录吗？',function(r){
		if(r)
		{
			$.ajax({
				url:name+'_delete.action?id='+id,
				dataType:'json',
				success:function(value){
					if(value.success){
						$('#grid').datagrid('reload');
					}
					$.messager.alert('提示',value.message);
				}
			});		
		}	
	});	
}

/**
 * 编辑
 */
function edit(id){
	
	$('#editWindow').window('open');
	$('#editForm').form('clear');
	$('#editForm').form('load',name+'_get.action?id='+id);	
	method="update";
}