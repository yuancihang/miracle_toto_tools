<input type="hidden" id="userId" name="userId" value="<%= request.getAttribute("userId") %>">

<script>
function post(url, config){
	defaultConfig = {
	  userId : $("#userId").val(),
	  succ: function(){
		  alert("sucess");
	  },
	  fail: function(message){
		  alert("fail\n"+message);
	  },
	  succ_reload : true,
	  on_message : function(json, text){
		  alert(text);
	  },
	};
	$.extend(defaultConfig, config);
	
	data = {};
	for( p in defaultConfig){
		if((p != 'succ') && (p != 'fail') && (p != 'succ_reload') && (p != 'on_message')){
			data[p] = defaultConfig[p];
		}
	}
	
	$.post("<%= request.getContextPath() %>"+url,data,function(result){
      json = eval(result);
      if("success" in json){
    	  if(json.success == true){
        	  defaultConfig.succ();
        	  if(defaultConfig.succ_reload == true){
        		  window.location.reload();
        	  }
          }else{
        	  defaultConfig.fail(json.message);
          }
      }else{
    	  defaultConfig.on_message(json, result);
      }
      
    });
}  
</script>