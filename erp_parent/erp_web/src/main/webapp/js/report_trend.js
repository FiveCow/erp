$(function(){
	
	$('#grid').datagrid({
		url:'report_trendReport',
		columns:[[
		    {field:'month',title:'月份',width:100},      
		    {field:'money',title:'销售额',width:100}     
		  ]],
		onLoadSuccess:function(data){	
			//[{name:'全部数据',data:[10,323,113,33....] }]
			
			var value={name:'全部数据',data:[]};						
			for(var i=0;i<data.rows.length;i++){
				value.data.push( data.rows[i].money);				
			}			
			showLine([value]);//显示折线图	
		}
	});
	
	//showLine("report_trendChart.action");//页面打开时显示折线图
	
	/**
	 * 查询
	 */
	$('#btnSearch').bind('click',function(){
		var formdata= $('#searchForm').serializeJSON();
		$('#grid').datagrid('load',formdata);
		//showLine("report_trendChart.action?year="+formdata['year']);
	});
	
	
});

/**
 * 显示折线图
 */
function showLine(value){
	
	$('#container').highcharts({
        title: {
            text: '销售趋势分析',
            x: -20 //center
        },
        subtitle: {
            text: 'Source: www.itheima.com',
            x: -20
        },
        xAxis: {
            categories: ['1月', '2月', '3月', '4月', '5月', '6月',
                '7月', '8月', '9月', '10月', '11月', '12月']
        },
        yAxis: {
            title: {
                text: '销售额 (元)'
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        tooltip: {
            valueSuffix: '元'
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'middle',
            borderWidth: 0
        },
        series:value
    });
	
}