<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%@ include file="/page/base/head.jsp" %>
</head>
<body>
<%@ include file="/page/base/common.jsp" %>
<div class="container-fluid">
<div class="row">
 <input type="button" id="b1" value="post测试1" class="btn btn-default">
 <input type="button" id="b2" value="post测试2" class="btn btn-default">
 
 <table class="table table-striped table-bordered table-hover table-condensed">
   <tr>
     <th>11</th>
     <th>22</th>
   </tr>
   <tr>
     <td>11111111111111</td>
     <td>2222222222222</td>
   </tr>
   <tr>
     <td>11111111111111</td>
     <td>2222222222222</td>
   </tr>
   <tr>
     <td>11111111111111</td>
     <td>2222222222222</td>
   </tr>
   <tr>
     <td>11111111111111</td>
     <td>2222222222222</td>
   </tr>
 </table>
 
 <br>
 
 <form id="my_form">
  111: <input type="text" value="text"><br>
  2222: <input type="text" value="text" size=60><br>
  3333:
  <textarea cols=100 rows=10>===============</textarea><br>
 
  <input type="submit" value="submit" class="btn btn-default">
 </form>
 
 <br>
 
<button type="button" class="btn btn-default" data-toggle="modal" data-target="#exampleModal" data-whatever="@getbootstrap">模态对话框</button>

<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="exampleModalLabel">修改数据</h4>
      </div>
      <div class="modal-body">
         <form id="form1">
		  111: <input type="text" value="text" id="t1"><br>
		  2222: <input type="text" value="text" size=60><br>
		  3333:
		  <textarea cols=80 rows=10 id="t3">===============</textarea><br>
		 </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary" onclick="save_msg($('#exampleModal'), $('#form1'))">保存</button>
      </div>
    </div>
  </div>
</div>

<br>
<hr>
<form action="<%= request.getContextPath() %>/DemoServlet?operate=upload" method="post" ENCTYPE="multipart/form-data">
  <input type="file" name="f1"><br>
  <input type="submit" value="上传" class="btn btn-default">
</form>
<br>
<a href="<%= request.getContextPath() %>/DemoServlet?operate=download">下载</a>


 <div id="output"></div>
  <script type="text/javascript">
    function save_msg(modelQuery, formQuery){
    	//alert(formQuery.find("#t1").val());
    	alert(formQuery.find("#t3").val());
    	modelQuery.modal('hide');
    }
    $(document).ready(function(){
    	$("#b1").click(function(){
    		post("/DemoServlet", {
    			operate : 'updateInfo',
    			succ_reload : 'false',
    	    });
    	});
    	$("#b2").click(function(){
    		post("/DemoServlet", {
    			operate : 'jsonInfo',
    			on_message : function(json, text){
    				alert(json.length);
    			},
    	    });
    	});
    	
    });
    
  </script>        
</div>          
</div>
</body>
</html>