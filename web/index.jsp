<%--
  Created by IntelliJ IDEA.
  User: xymeow
  Date: 15/10/27
  Time: 上午10:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.*" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Bootstrap -->
    <link href="bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
    <base href="<%=basePath%>">
    <title>MeowSearch</title>
    <style>
        #textArea {
            border-right: 0px;
            font-family: STXihei;
            border-color: sandybrown;
            border-style: solid;
            margin-right: -5px;
            margin-top: 0.85px;
            height: 35px;
            width: 400px;
        }
        #search {
            width: 80px;
            border: 0px;
            border-top: 2px;
            font-family: STXihei;
            background-color: sandybrown;
            color: white;
            text-align: center;
            align-content: center;
            height: 35px;
        }
    </style>
    <script src="jquery-2.2.0.min.js"></script>
    <script src="bootstrap-3.3.5-dist/js/bootstrap.min.js"></script>
    <script type="text/javascript">
        function checkAll() {
            var keyword = document.getElementById("textArea").value;
            if (!keyword) {
                //$().alert("请输入查询关键词");
                $("#warning").modal();
                return false;
            }
            return true;
        }

    </script>
</head>

<body>
<p align="center" style="margin-top: 100px"><img src="logo.png"/></p>
<form action="search.jsp" name="search" method="get"
      enctype="application/x-www-form-urlencoded" onsubmit="return checkAll()" align="center">
    <div><input name="keyword" type="text" maxlength="100" id="textArea" placeholder="please enter key words"
                value="<%=request.getParameter("keyword")==null?"":request.getParameter("keyword")%>">
        <input type="submit" value="Search!" id="search" ></div>
</form>

<div class="modal fade" id="warning" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close"
                        data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="myModalLabel">
                    警告:
                </h4>
            </div>
            <div class="modal-body">
                关键词不能为空!
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-warning"
                        data-dismiss="modal" >朕知道了
                </button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
