<%--
  Created by IntelliJ IDEA.
  User: xymeow
  Date: 15/11/15
  Time: 下午4:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:directive.page import="lab1.*" />
<%@ page import="org.apache.lucene.document.Document" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.xml.transform.*" %>
<%@ page import="lab1.Result" %>
<%
  String path = request.getContextPath();
  String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
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
  <title>Search Result</title>
  <style>
    #textArea {
      border-right: 0px;
      font-family: STXihei;
      border-color: sandybrown;
      border-style: solid;
      margin-right: -5px;
      margin-left: 100px;
      height: 30px;
      width: 300px;
    }
    #search {
      width: 60px;
      border: 0px;
      border-top: 2px;
      font-family: STXihei;
      background-color: sandybrown;
      color: white;
      text-align: center;
      align-content: center;
      height: 30px;
    }
    p{
      font: 14px "STXihei";
      margin-left: 120px;
      margin-top: 5px;
    }
  </style>

  <script src="jquery-2.2.0.min.js"></script>
  <script src="bootstrap-3.3.5-dist/js/bootstrap.min.js"></script>
  <script type="text/javascript">
    function checkAll(){
      var keyword = document.getElementById("textArea").value;
      if (!keyword){
        $("#warning").modal();
        return false;
      }
      return true;
    }
  </script>
</head>

<body>

<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
  <div class="navbar-header">
    <a class="navbar-brand" href="#">Meow Search</a>
  </div>
  <div>
      <form class="navbar-form" action="search.jsp" name="search" method="get" onsubmit="return checkAll()">
        <table border="0" height="30px" width="450px">
          <tr>
            <td width ="66%"><input name="keyword" type="text" maxlength="100" id="textArea" placeholder="please enter key words"
                                    value="<%=request.getParameter("keyword")==null?"":request.getParameter("keyword")%>"></td>
            <td height="29" align="center"><input type="submit" value="Search!" id = "search"></td>
          </tr>
        </table>
    </form>
  </div>
</nav>

<%
  String keyword = request.getParameter("keyword");
    Search mysearcher = new Search();
    int pageSize = 10;
    int currentPage = 1;
    int maxResult = 100;
    Result results = mysearcher.search(keyword, maxResult);
    int begin = pageSize * (currentPage - 1) ;
    int end = Math.min(begin + pageSize, results.contents.size());
    SpellCheck checker = new SpellCheck();
    String correct = checker.search(keyword);
%>

<p style="color: #a9a9a9;font-size: small; margin-top: 60px">共查询到<%=results.totalDocs%>条结果,返回前<%if(results.totalDocs>100){%>100<%}
else %><%=results.totalDocs%>条, 第<%=currentPage%>页 </p>
<%   if(correct!=null){
%>
<p style="color: #31b0d5">您是不是要找: <a style="color: #31b0d5" href="search.jsp?keyword=<%=correct%>"><%=correct%></a></p>
<%}%>
<%
  for (int i=begin; i < end; i++){
%>
<p style="font-size: 18px;margin-bottom: 3px"><a href="<%=results.urls.get(i)%>"><%=results.titles.get(i)%></a></p>
<p style="color: #556b2f;font-size: small;margin-top: 1px" ><%=results.urls.get(i)%></p>
<p style="width: 700px;line-height: 130%"><%=results.contents.get(i)%></p>
<%if (results.moreLike.get(i)!= null){%>
<p style="color: sandybrown">相关新闻: <a href="<%=results.moreURL.get(i)%>" style="color: sandybrown;"><%=results.moreLike.get(i)%></a> </p>
<%}%>
<p style="color: #a9a9a9"><%=results.publishTime.get(i)%></p>

<%}
  session.setAttribute("result", results);
  session.setAttribute("keyword",request.getParameter("keyword"));
%>

<ul class="pagination" style="margin-left: 20%;color: sandybrown;">
  <li><a href="#">&laquo;</a> </li>
  <%for (int i = 1; i < Math.min(results.totalDocs, 100)/pageSize + 1; i ++ ){
    if(i == 1){%>
  <li class="active"><a href="search2.jsp?currentPage=<%=i%>"><%=i%><span class="sr-only">(current)</span> </a></li>
  <%
    }
    else{
  %>
   <li><a href="search2.jsp?currentPage=<%=i%>"><%=i%> </a></li>
  <%
      }
    }
    if (end - begin == pageSize){
      currentPage++;%>
  <li> <a href="search2.jsp?currentPage=<%=currentPage%>">&raquo;</a> </li>
 <% }%>
</ul>

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
