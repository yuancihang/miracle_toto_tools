<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="zh">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="<%= request.getContextPath() %>img/favicon.ico">

    <title>Dashboard Template for Bootstrap</title>

    <!-- Bootstrap core CSS -->
    <link href="<%= request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="<%= request.getContextPath() %>/css/dashboard.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="<%= request.getContextPath() %>/js/ie-emulation-modes-warning.js"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>
    <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container-fluid">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">梅露柯日服相关工具</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav navbar-right">
            <li><a href="#" onclick="go('m1_1', '/page/miracle/download_migration.jsp')">Dashboard</a></li>
            <li><a href="#" onclick="go('m2_1', '/page/base/content.jsp')">Settings</a></li>
            <li><a href="#" onclick="go('m3_1', '/page/base/content.jsp')">Profile</a></li>
            <li><a href="#">Help</a></li>
          </ul>
          <form class="navbar-form navbar-right">
            <input type="text" class="form-control" placeholder="Search...">
          </form>
        </div>
      </div>
    </nav>

    <div class="container-fluid">
      <div class="row">
        <div class="col-sm-3 col-md-2 sidebar">
          <ul class="nav nav-sidebar" id="m1">
            <li id="m1_1"><a href="#" onclick="go('m1_1', '/page/miracle/download_migration.jsp')">下载日服migration <span class="sr-only">(current)</span></a></li>
            <li id="m1_2"><a href="#" onclick="go('m1_2', '/page/miracle/yml2xls.jsp')">日服 yml-->>xls</a></li>
            <li id="m1_3"><a href="#" onclick="go('m1_3', '/page/miracle/rb2xls.jsp')">日服rb-->>xls</a></li>
            <li id="m1_4"><a href="#" onclick="go('m1_4', '/page/miracle/gencnrb.jsp')">还原中文rb</a></li>
            <!--  
            <li id="m1_5"><a href="#" onclick="go('m1_5', '/page/miracle/export_last_words.jsp')">导出最新词素表</a></li>
            -->
          </ul>
          <ul class="nav nav-sidebar hidden" id="m2">
            <li id="m2_1"><a href="#" onclick="go('m2_1', '/page/base/content.jsp')">Nav item</a></li>
            <li id="m2_2"><a href="#" onclick="go('m2_2', '/page/base/content.jsp')">Nav item again</a></li>
            <li id="m2_3"><a href="#" onclick="go('m2_3', '/page/base/content.jsp')">One more nav</a></li>
            <li id="m2_4"><a href="#" onclick="go('m2_4', '/page/base/content.jsp')">Another nav item</a></li>
            <li id="m2_5"><a href="#" onclick="go('m2_5', '/page/base/content.jsp')">More navigation</a></li>
          </ul>
          <ul class="nav nav-sidebar hidden" id="m3">
            <li id="m3_1"><a href="#" onclick="go('m3_1', '/page/base/content.jsp')">Nav item again</a></li>
            <li id="m3_2"><a href="#" onclick="go('m3_2', '/page/base/content.jsp')">One more nav</a></li>
            <li id="m3_3"><a href="#" onclick="go('m3_3', '/page/base/content.jsp')">Another nav item</a></li>
          </ul>
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <iframe src="#" width="100%" height="700" frameborder="0" scrolling="no" align="center" marginheight="0" marginwidth="0" id="iframepage" name="iframepage" onLoad="iFrameHeight()"></iframe>
		</div>
      </div>
    </div>

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="<%= request.getContextPath() %>/js/jquery-2.1.4.min.js"></script>
    <script src="<%= request.getContextPath() %>/js/bootstrap.min.js"></script>
    <!-- Just to make our placeholder images work. Don't actually copy the next line! -->
    <script src="<%= request.getContextPath() %>/js/holder.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="<%= request.getContextPath() %>/js/ie10-viewport-bug-workaround.js"></script>
    <script type="text/javascript">
		function iFrameHeight() {
			var ifm = document.getElementById("iframepage");
			var subWeb = document.frames ? document.frames["iframepage"].document
					:
					ifm.contentDocument;
			if (ifm != null && subWeb != null) {
				ifm.height = subWeb.body.scrollHeight;
			}
		}
		
		function go(menu_id, url){
			switch_menu_list = false;
			if (!(typeof(cur_menu) == "undefined")) {
				cur_menu.removeClass("active");
				old_menu_list_id = cur_menu_list.attr("id");
				new_menu_list_id = menu_id.split("_")[0]
				if(old_menu_list_id != new_menu_list_id){
					cur_menu_list.addClass("hidden");
					switch_menu_list = true;
				}
			}
			cur_menu = $("[id="+menu_id+"]");
			cur_menu.addClass("active");
			
			cur_menu_list = $("[id="+menu_id.split("_")[0]+"]");
			if(switch_menu_list == true){
				cur_menu_list.removeClass("hidden");
			}
			
			$("#iframepage").attr("src", "<%= request.getContextPath() %>"+url);
		}
		$(document).ready(function(){
			go('m1_1', '/page/miracle/download_migration.jsp');
		});
	</script>
  </body>
</html>
    