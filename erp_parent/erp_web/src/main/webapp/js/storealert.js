$(function(){
	
	$('#grid').datagrid({
		url:'storedetail_storeAlertList.action',
		columns:[[
		   {field:'uuid',title:'商品编号',width:100},       
		   {field:'name',title:'商品名称',width:100},       
		   {field:'storenum',title:'库存数量',width:100},       
		   {field:'outnum',title:'待发货数量',width:100} 
		]],
		toolbar: [{
			iconCls: 'icon-add',
			text:'发送预警邮件',
			handler: function(){				
				
				$.ajax({
					url:'storedetail_sendStoreAlertMail.action',
					dataType:'json',
					success:function(value){
						$.messager.alert('提示',value.message);						
					}					
				});
				
			}
		}]
		
		
	});
	
	
	
})