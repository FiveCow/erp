$(function(){
	
	
	$('#grid').datagrid({
		url:'storedetail_listByPage.action',
		columns:[[
		  		    {field:'uuid',title:'编号',width:100},
		  		    {field:'storeuuid',title:'仓库',width:100,formatter:function(value,row,index){
		  		    	return ajax('store_get.action?id=',value,'store_'+index, 't.name'   );
		  		    }},
		  		    {field:'goodsuuid',title:'商品',width:100,formatter:function(value,row,index){
		  		    	return ajax('goods_get.action?id=',value,'goods_'+index, 't.name'   );
		  		    }},
		  		    {field:'num',title:'数量',width:100}    
		]],
		singleSelect:true,
		pagination:true
		
		
	});
	
	
	/**
	 * 查询
	 */
	$('#btnSearch').bind('click',function(){
		
		$('#grid').datagrid('load', $('#searchForm').serializeJSON() );
		
	});
	
});