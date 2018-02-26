function ajax(url,value,id,key){
	if(value!=null){
		$.ajax({
	  		url:url+value,
	  		dataType:'json',
	  		success:function(value){
	  			$('#'+id).html( value[key] );
	  		}		  		    		
	  	});		
	}
  	return "<span id='"+id +"'></span>";		
}