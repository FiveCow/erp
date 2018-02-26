$(function(){
	
	$('#grid').datagrid({
		url:'report_orderReport.action',
		columns:[[
		      {field:'name',title:'商品分类',width:200},   
		      {field:'y',title:'销售额',width:200}  
		          ]],
		singleSelect:true,
		onLoadSuccess:function(data){			
			showPie(data.rows);//显示饼图		{total:100,rows:[]}	
		}
	});
	
	//查询
	$('#btnSearch').bind('click',function(){
		var formdata=$('#searchForm').serializeJSON();
		formdata['date2']+=" 23:59:59";
		
		$('#grid').datagrid('load',formdata);
		//带日期
		//showPie('report_orderReport.action?date1='+formdata['date1']+'&date2='+formdata['date2']);//显示饼图
	});
		
	//showPie('report_orderReport.action');//显示饼图
	
});

/**
 * 显示饼图
 * @param data
 */
function showPie(value){
	
	//读取饼图数据	
	$('#container').highcharts({
         chart: {
             plotBackgroundColor: null,
             plotBorderWidth: null,
             plotShadow: false,
             type: 'pie'
         },
         title: {
             text: '销售统计图'
         },
         tooltip: {
             pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
         },
         plotOptions: {
             pie: {
                 allowPointSelect: true,
                 cursor: 'pointer',
                 dataLabels: {
                     enabled: true
                 },
                 showInLegend: true
             }
         },
         series: [{
             name: "比例",
             colorByPoint: true,
             data: value
         }]
     });			
		
}