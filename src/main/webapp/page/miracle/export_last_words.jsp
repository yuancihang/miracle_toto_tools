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
  <h2 class="sub-header"></h2>
 <form id="my_form" action="<%= request.getContextPath() %>/MiracleServlet">
 <input type="hidden" id="operate" name="operate" value="exportLastWords">
 
  <input type="submit" value="导出最新词素表" class="btn btn-default">
 </form>
</div>          
</div>

<script type="text/javascript">
  $(document).ready(function(){
  	
  	
  });
  
</script>
</body>
</html>