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
  <h2 class="sub-header">翻译excel + 日服rb -->>  中文rb</h2>
 <form id="my_form" action="<%= request.getContextPath() %>/MiracleServlet?operate=gencnrb" method="post" ENCTYPE="multipart/form-data">
 
  xls: <input type="file" name="xls"><br>
  rb1: <input type="file" name="rb1"><br>
  rb2: <input type="file" name="rb2"><br>
  rb3: <input type="file" name="rb3"><br>
  rb4: <input type="file" name="rb4"><br>
  rb5: <input type="file" name="rb5"><br>
  rb6: <input type="file" name="rb6"><br>
  
 
  <input type="submit" value="还原中文rb" class="btn btn-default">
 </form>
</div>          
</div>

<script type="text/javascript">
  $(document).ready(function(){
  	
  	
  });
  
</script>
</body>
</html>