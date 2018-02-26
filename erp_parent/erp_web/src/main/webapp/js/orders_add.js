var isEditingRowIndex;//全局变量 ：记录当前编辑行索引
$(function(){
	
	$('#ordersgrid').datagrid({
			columns:[[
		  		    {field:'goodsuuid',title:'商品编号',width:100,editor:{type:'numberbox',options:{
		  		    	disabled:true
		  		    }}},
		  		    {field:'goodsname',title:'商品名称',width:100,editor:{type:'combobox',options:{
		  		    	url:'goods_list.action',
		  		    	valueField:'name',
		  		    	textField:'name',
		  		    	onSelect:function(record){
		  		    		//获取价格编辑框对象
		  		    		var priceEdt= $('#ordersgrid').datagrid('getEditor',{index:isEditingRowIndex,field:'price'});
		  		    		//向价格编辑框赋值
		  		    		if(Request['type']=='1'){//采购申请
		  		    			$(priceEdt.target).val(record.inprice);
		  		    		}
		  		    		if(Request['type']=='2'){//销售订单录入
		  		    			$(priceEdt.target).val(record.outprice);
		  		    		}
		  		    		
		  		    		//获取商品编号编辑框对象
		  		    		var goodsuuidEdt= $('#ordersgrid').datagrid('getEditor',{index:isEditingRowIndex,field:'goodsuuid'});
		  		    		$(goodsuuidEdt.target).val(record.uuid);
		  		    		
		  		    		cal();//重新计算
		  		    		sum();//重新合计
		  		    	}
		  		    }}},
		  		    {field:'price',title:'价格',width:100,editor:{type:'numberbox',options:{
		  		    	precision:2
		  		    }}},
		  		    {field:'num',title:'数量',width:100,editor:'numberbox'},
		  		    {field:'money',title:'金额',width:100,editor:{type:'numberbox',options:{
		  		    	precision:2,
		  		    	disabled:true
		  		    }}},
		  		    {field:'-',title:'操作',width:100,formatter:function(value,row,index){
		  		    	return "<a href='#' onClick='deleteRow(" + index + ")'>删除</a>";
		  		    }}
			 ]],
			 singleSelect:true,
			 toolbar: [{
					iconCls: 'icon-add',
					text:'增加',
					handler: function(){				
						//追加一个新行
						$('#ordersgrid').datagrid('appendRow',{num:0,money:0});
						//alert('关闭第'+isEditingRowIndex+'行');
						//关闭上一次的编辑行
						$('#ordersgrid').datagrid('endEdit',isEditingRowIndex);
						//得到最后一行的索引
						isEditingRowIndex= $('#ordersgrid').datagrid('getRows').length-1;
						//alert('开启第'+isEditingRowIndex+'行');
						//将最后一行开启编辑
						$('#ordersgrid').datagrid('beginEdit',isEditingRowIndex);
						
						bindGridEvent();//绑定
					}
			 }],
			 onClickRow:function(rowIndex,rowData){
				 //结束上一次编辑行: 编辑框的值会写到单元格里
				 $('#ordersgrid').datagrid('endEdit',isEditingRowIndex);
				 //得到单击行的索引
				 isEditingRowIndex=rowIndex;
				 //将单击行开启编辑
				 $('#ordersgrid').datagrid('beginEdit',isEditingRowIndex);
				 
				 bindGridEvent();//绑定
			 }
		
	});
	
	
	//供应商下拉面板
	
	$('#supplieruuid').combogrid({
		url:'supplier_list.action?t1.type='+Request['type'],
		columns:[[
		  		    {field:'uuid',title:'编号',width:100},
		  		    {field:'name',title:'名称',width:100},
		  		    {field:'address',title:'地址',width:180},
		  		    {field:'contact',title:'联系人',width:100},
		  		    {field:'tele',title:'电话',width:100},
		  		    {field:'email',title:'EMAIL',width:100}	    
			    ]],
	    idField:'uuid',//提交那一列
	    textField:'name',//选择后，显示哪一列的值
		width:400,//控件本身的宽度
		panelWidth:700,//下拉面板的宽度
		mode:'remote'//远程模式，可以实现自动补全
	});
	
	//提交
	$('#btnSave').bind('click',function(){
		
		//先结束编辑行，否则最后一次编辑的数据无法提取出来
		$('#ordersgrid').datagrid('endEdit',isEditingRowIndex);
		
		//获取表单数据（供应商 ）
		var formdata=$('#orderForm').serializeJSON();
		
		
		//将表格数据（json字符串）追加到表单数据中.
		formdata['json']=JSON.stringify( $('#ordersgrid').datagrid('getRows'));
		
		$.ajax({
			url:'orders_add.action?t.type='+Request['type'],
			dataType:'json',
			type:'post',
			data:formdata,
			success:function(value){
				if(value.success){
					$('#sum').html('0.00');//清空合计
					$('#ordersgrid').datagrid('loadData',{total:0,rows:[]});//清空表格
					
					$('#addOrdersWindow').window('close');//关闭窗口
					$('#grid').datagrid('reload');
					
				}
				$.messager.alert('提示',value.message);
			}			
		});
		
		
	});
	
});

/**
 * 计算
 */
function cal(){
	//获取价格编辑框对象
	var priceEdt= $('#ordersgrid').datagrid('getEditor',{index:isEditingRowIndex,field:'price'});
	//获取价格
	var price= $( priceEdt.target ).val();	
	//获取数量编辑框对象
	var numEdt= $('#ordersgrid').datagrid('getEditor',{index:isEditingRowIndex,field:'num'});
	//获取数量
	var num= $( numEdt.target).val()	
	//计算金额
	var money=(price*num).toFixed(2);	
	//获取金额编辑框对象
	var moneyEdt = $('#ordersgrid').datagrid('getEditor',{index:isEditingRowIndex,field:'money'});
	//向金额编辑框赋值
	$(moneyEdt.target).val( money );
	//向金额单元格赋值:向row对象赋值
	
	$('#ordersgrid').datagrid('getRows')[isEditingRowIndex].money=money;
	
	
	
}

/**
 * 绑定表格内编辑框事件
 */
function bindGridEvent(){
	
	//获取价格编辑框对象
	var priceEdt= $('#ordersgrid').datagrid('getEditor',{index:isEditingRowIndex,field:'price'});
	//绑定价格编辑框的键盘输入事件
	$(priceEdt.target).bind('keyup',function(){
		cal();	
		sum();
	});
	
	//获取数量编辑框对象
	var numEdt= $('#ordersgrid').datagrid('getEditor',{index:isEditingRowIndex,field:'num'});
	//绑定数量编辑框的键盘输入事件
	$(numEdt.target).bind('keyup',function(){
		cal();	
		sum();
	});
	
}

/**
 * 删除行
 * @param index
 */
function deleteRow(index){
	//结束编辑
	$('#ordersgrid').datagrid('endEdit',isEditingRowIndex);
	 
	//删除指定的行
	$('#ordersgrid').datagrid('deleteRow',index);
	
	//获取表格数据
	var data=$('#ordersgrid').datagrid('getData');
	
	//加载数据
	$('#ordersgrid').datagrid('loadData',data);
	
	sum();
}
/**
 * 计算合计数
 */
function sum(){
	
	var money=0;//金额
	
	var rows= $('#ordersgrid').datagrid('getRows');//获取所有行
	
	for(var i=0;i<rows.length;i++){
		
		money+= parseFloat( rows[i].money);
	}
	$('#sum').html(money.toFixed(2));
}
