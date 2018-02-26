var itemRowIndex;//当前操作的明细索引
$(function(){
	
	var url='orders_listByPage.action';
	
	//我的采购订单
	if(Request['operator']=='myorders' && Request['type']=='1'){
		url='orders_myListByPage.action?t1.type=1';
		document.title='我的采购订单';
		$('#btnAdd').show();
		$('#btnAdd').linkbutton({text:'采购申请'});
		$('#name').html("供应商");
	}
	
	//我的销售订单
	if(Request['operator']=='myorders' && Request['type']=='2'){
		url='orders_myListByPage.action?t1.type=2';
		document.title='我的销售订单';
		$('#btnAdd').show();
		$('#btnAdd').linkbutton({text:'销售订单录入'});
		$('#name').html("客户");
	}
	
	//采购订单查询
	if(Request['operator']=='orders' && Request['type']=='1' ){
		url+='?t1.type=1';
		document.title='采购订单查询';
	}
	
	//销售订单查询
	if(Request['operator']=='orders' && Request['type']=='2' ){
		url+='?t1.type=2';
		document.title='销售订单查询';
	}
	
	//审核
	if(Request['operator']=='check'){
		url+='?t1.type=1&t1.state=0';
		document.title='采购订单审核';
		$('#btnCheck').show();
	}
	
	//确认
	if(Request['operator']=='start'){
		url+='?t1.type=1&t1.state=1';
		document.title='采购订单确认';
		$('#btnStart').show();
	}
	
	
	//采购入库
	if(Request['operator']=='inStore'){
		url+='?t1.type=1&t1.state=2';
		document.title='采购订单入库';
		$('#btnEnd').show();
		$('#btnEnd').linkbutton({text:'入库'});
	}
	

	//销售出库
	if(Request['operator']=='outStore'){
		url+='?t1.type=2&t1.state=0';
		document.title='销售订单出库';
		$('#btnEnd').show();
		$('#btnEnd').linkbutton({text:'出库'});
	}
	
	$('#grid').datagrid({
		url:url,
		columns:getColumns(),
		  singleSelect:true,
		  pagination:true,
		  
		  onDblClickRow:function(rowIndex,rowData){
			  
			  $('#ordersWindow').window('open');
			  $('#uuid').html( rowData.uuid );//显示流水号
			  //下单日期
			  if(rowData.createtime!=null){
				  $('#createtime').html( new Date( rowData.createtime ).Format('yyyy-MM-dd') ); 
			  }
			  //审核日期
			  if(rowData.checktime!=null){
				  $('#checktime').html( new Date( rowData.checktime ).Format('yyyy-MM-dd') );
			  }
			  //确认日期
			  if(rowData.starttime!=null){
				  $('#starttime').html( new Date( rowData.starttime ).Format('yyyy-MM-dd') );
			  }
			  //入库日期
			  if(rowData.endtime!=null){
				  $('#endtime').html( new Date( rowData.endtime ).Format('yyyy-MM-dd') );
			  }
			  //下单人
			  $('#creater').html(  $('#creater_'+rowIndex).html()   );
			  //审核人
			  $('#checker').html(  $('#checker_'+rowIndex).html()   );
			  //确认人
			  $('#starter').html(  $('#starter_'+rowIndex).html()   );
			  //入库人
			  $('#ender').html(  $('#ender_'+rowIndex).html()   );
			  //供应商
			  $('#supplier').html( $('#supplier_'+rowIndex).html() );
			  //状态
			  $('#state').html( getState( rowData.state) );
			  
			  //显示运单号
			  $('#waybillSn').html( rowData.waybillsn );
			  
			  //加载订单明细
			  $('#itemgrid').datagrid('loadData', rowData.orderdetails );
			  
		  }
		
	});
	
	//订单明细
	$('#itemgrid').datagrid({
		columns:[[
		  		    {field:'uuid',title:'编号',width:100},
		  		    {field:'goodsuuid',title:'商品编号',width:100},
		  		    {field:'goodsname',title:'商品名称',width:100},
		  		    {field:'price',title:'价格',width:100},
		  		    {field:'num',title:'数量',width:100},
		  		    {field:'money',title:'金额',width:100},		  		   
		  		    {field:'state',title:'状态',width:100,formatter:function(value){
		  		    	return getDetailState(value);
		  		    }}	    
			]],
			singleSelect:true,
			fitColumns:true,
			onDblClickRow:function(rowIndex,rowData){
				$('#itemWindow').window('open');
				//显示商品ID
				$('#goodsuuid').html(rowData.goodsuuid );
				//显示商品名称
				$('#goodsname').html(rowData.goodsname );
				//显示数量
				$('#num').html(rowData.num);
				//订单明细ID
				$('#itemuuid').val(rowData.uuid);
				itemRowIndex=rowIndex;
				//控制入库按钮的显示与隐藏
				if(rowData.state=='0'){
					$('#btnEnd').show();
				}
				if(rowData.state=='1'){
					$('#btnEnd').hide();
				}
				
			}
	
		
	});
	
	//审核
	$('#btnCheck').bind('click',function(){
		doLogic('确定要审核吗？','orders_doCheck.action?id=');
	});
	
	
	//确认
	$('#btnStart').bind('click',function(){
		doLogic('确定要确认吗？','orders_doStart.action?id=');
	});
	
	//采购申请
	$('#btnAdd').bind('click',function(){
		$('#addOrdersWindow').window('open');
		
	});
	
	//导出
	$('#btnExport').bind('click',function(){
		//$.download('orders_exportById.action?id='+$('#uuid').html(),   {}  );
		$.download('orders_exportById.action', {id: $('#uuid').html() }  );
	});
	
	//查询物流信息
	$('#btnWaybill').bind('click',function(){
		
		$('#waybillWindow').window('open');
		
		$('#waybillGrid').datagrid({
			url:'orders_waybilldetailList.action?waybillSn='+$('#waybillSn').html(),
			columns:[[
			  {field:'exedate',title:'日期',width:100},        
			  {field:'exetime',title:'时间',width:100},        
			  {field:'info',title:'信息',width:300}           
			]]			
		});
		
	});
	
	//入库或出库
	$('#btnEnd').bind('click',function(){
		
		var url='';
		var info='';
		if(Request['operator']=='inStore'){
			url='orderdetail_doInStore.action';
			info='确定要入库吗？';
		}
		if(Request['operator']=='outStore'){
			url='orderdetail_doOutStore.action';
			info='确定要出库吗？';
		}
		
		$.messager.confirm('提示',info,function(r){
			
			if(r){				
				var formdata=$('#itemForm').serializeJSON();
				
				$.ajax({
					url:url,
					data:formdata,
					dataType:'json',
					type:'post',
					success:function(value){
						
						$.messager.alert('提示',value.message);
						
						if(value.success){
							$('#itemWindow').window('close');
							//更改数据
							$('#itemgrid').datagrid('getRows')[itemRowIndex].state='1';							
							//刷新表格
							$('#itemgrid').datagrid('loadData' , $('#itemgrid').datagrid('getData') );
							//重新请求订单主表
							$('#grid').datagrid('reload');
							
						}
						
						
					}
				});
			}
		});
		
	});
	
});

/**
 * 返回订单状态
 * @param value
 * @returns {String}
 */
function getState(value){
	if(Request['type']=='1'){
		if(value=='0'){
	  		return '未审核';
	  	}
	  	if(value=='1'){
	  		return '已审核';
	  	}
	  	if(value=='2'){
	  		return '已确认';
	  	}
	  	if(value=='3'){
	  		return '已入库';
	  	}		
	}
	if(Request['type']=='2'){
		if(value=='0'){
	  		return '未出库';
	  	}	  
	  	if(value=='3'){
	  		return '已出库';
	  	}		
	}
	
}
/**
 * 返回订单明细状态
 * @param value
 * @returns {String}
 */
function getDetailState(value){
	if(Request['type']=='1'){
		if(value=='0')
		{
			return '未入库';
		}
		if(value=='1')
		{
			return '已入库';
		}
	}
	if(Request['type']=='2'){
		if(value=='0')
		{
			return '未出库';
		}
		if(value=='1')
		{
			return '已出库';
		}
	}
	
}

/**
 * 采购审核或确认
 */
function doLogic(message,url){
	
	$.messager.confirm('提示',message,function(r){
		if(r){
			$.ajax({
				url:url+$('#uuid').html(),
				dataType:'json',
				success:function(value){
					if(value.success){
						//关闭窗口
						$('#ordersWindow').window('close');
						//刷新表格
						$('#grid').datagrid('reload');
					}
					$.messager.alert('提示',value.message);				
				}					
			});
		}			
	});
	
}

/**
 * 获取列定义
 * @returns {Array}
 */
function getColumns(){
	if(Request['type']=='1'){
		return [[
		  		    {field:'uuid',title:'编号',width:100},
		  		    {field:'createtime',title:'生成日期',width:100,formatter:function(value){
		  		    	return new Date(value).Format('yyyy-MM-dd');
		  		    }},
		  		    {field:'checktime',title:'检查日期',width:100,formatter:function(value){
		  		    	return new Date(value).Format('yyyy-MM-dd');
		  		    }},
		  		    {field:'starttime',title:'开始日期',width:100,formatter:function(value){
		  		    	return new Date(value).Format('yyyy-MM-dd');
		  		    }},
		  		    {field:'endtime',title:'结束日期',width:100,formatter:function(value){
		  		    	return new Date(value).Format('yyyy-MM-dd');
		  		    }},
		  		    {field:'creater',title:'下单员',width:100,formatter:function(value,row,index){
		  		    			  		    	
		  		    	return ajax('emp_get.action?id=',value,'creater_'+index ,'t.name');
		  		    }},
		  		    {field:'checker',title:'审查员',width:100,formatter:function(value,row,index){
		  		    			  		    	
		  		    	return ajax('emp_get.action?id=',value,'checker_'+index ,'t.name');
		  		    }},
		  		    {field:'starter',title:'采购员',width:100,formatter:function(value,row,index){
		  		    			  		    	
		  		    	return ajax('emp_get.action?id=',value,'starter_'+index ,'t.name');
		  		    }},
		  		    {field:'ender',title:'库管员',width:100,formatter:function(value,row,index){
		  		    			  		    	
		  		    	return ajax('emp_get.action?id=',value,'ender_'+index ,'t.name');
		  		    }},
		  		    {field:'supplieruuid',title:'供应商',width:100,formatter:function(value,row,index){
		  		    			  		    	
		  		    	return ajax('supplier_get.action?id=',value,'supplier_'+index ,'t.name');
		  		    }},
		  		    {field:'totalmoney',title:'总金额',width:100},
		  		    {field:'state',title:'订单状态',width:100,formatter:function(value){
		  		    	return getState(value);
		  		    }},
		  		    {field:'waybillsn',title:'运单号',width:100}   
		  ]];		
	}
	if(Request['type']=='2'){
		return [[
		  		    {field:'uuid',title:'编号',width:100},
		  		    {field:'createtime',title:'生成日期',width:100,formatter:function(value){
		  		    	return new Date(value).Format('yyyy-MM-dd');
		  		    }},		  		    
		  		    {field:'endtime',title:'结束日期',width:100,formatter:function(value){
		  		    	return new Date(value).Format('yyyy-MM-dd');
		  		    }},
		  		    {field:'creater',title:'下单员',width:100,formatter:function(value,row,index){
		  		    			  		    	
		  		    	return ajax('emp_get.action?id=',value,'creater_'+index ,'t.name');
		  		    }},		  		    
		  		    {field:'ender',title:'库管员',width:100,formatter:function(value,row,index){
		  		    			  		    	
		  		    	return ajax('emp_get.action?id=',value,'ender_'+index ,'t.name');
		  		    }},
		  		    {field:'supplieruuid',title:'客户',width:100,formatter:function(value,row,index){
		  		    			  		    	
		  		    	return ajax('supplier_get.action?id=',value,'supplier_'+index ,'t.name');
		  		    }},
		  		    {field:'totalmoney',title:'总金额',width:100},
		  		    {field:'state',title:'订单状态',width:100,formatter:function(value){
		  		    	return getState(value);
		  		    }},
		  		    {field:'waybillsn',title:'运单号',width:100}   
		  ]];		
	}
	
}
