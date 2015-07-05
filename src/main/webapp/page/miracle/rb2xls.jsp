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
  <h2 class="sub-header">日服 rb-->>xls</h2>
 <form id="my_form" action="<%= request.getContextPath() %>/MiracleServlet?operate=rb2xls" method="post" ENCTYPE="multipart/form-data">
 
  rb1: <input type="file" name="rb1"><br>
  rb2: <input type="file" name="rb2"><br>
  rb3: <input type="file" name="rb3"><br>
  rb4: <input type="file" name="rb4"><br>
  rb5: <input type="file" name="rb5"><br>
  rb6: <input type="file" name="rb6"><br>
 
  <input type="submit" value="导出excel" class="btn btn-default">
 </form>
</div>          
</div>

<script type="text/javascript">
  $(document).ready(function(){
  	
  	
  });
  
</script>
</body>
</html>