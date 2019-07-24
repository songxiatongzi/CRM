<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
%>
<%--设置字符集--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
	<title>线索转换页面</title>
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>


<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<script type="text/javascript">
	$(function(){
		$("#isCreateTransaction").click(function(){
			if(this.checked){
				$("#create-transaction2").show(200);
			}else{
				$("#create-transaction2").hide(200);
			}
		});

		//添加时间控件 time-expectedDate
		$(".time-expectedDate").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});

		//为市场活动源[点击搜索市场活动按钮]搜索出所有的市场活动
		$("#selectActivity").keydown(function(event){

			if(event.keyCode == 13){

				$.ajax({

					url:"workbench/clue/getActivityListByName.do",
					data:{
						activityName:$.trim($("#selectActivity").val())
					},
					type:"get",
					dataType:"json",
					success:function(data){

						var html = "";
						//其中将id值取出，存放到添加交易的隐藏域中，用来创建交易时使用

						$.each(data,function(i,n){

							html += '<tr>';
							html += '<td><input type="radio" name="selectOne" value="'+n.id+'"/></td>';
							html += '<td id="'+n.id+'">'+n.name+'</td>';
							html += '<td>'+n.startDate+'</td>';
							html += '<td>'+n.endDate+'</td>';
							html += '<td>'+n.owner+'</td>';
							html += '</tr>';
						});

						$("#tbody").html(html);

					}
				});

				return false;
			}
		});

		//为[搜索市场活动绑定点击事件]
		$("#submitActivityBtn").click(function(){

			//取得选中id值
			var $selectOne = $("input[name=selectOne]:checked");
			var id = $selectOne.val();

			//将id 值保存在隐藏域中
			$("#activityId").val(id);
			//将标签中的姓名提取出来，存放到输入框中
			var name = $("#"+id).html();
			alert(name)
			$("#activityName").val(name);

			//清空所选数据框中的内容
			$("#selectActivity").val("");

			//清空选中的信息
			$("input[name=selectOne]").removeAttr("checked");

			//关闭模态窗口
			$("#searchActivityModal").modal("hide");
		});

		//为线索转换按钮绑定事件，执行线索转换操作
		$("#convertBtn").click(function(){
			//当按钮选中的时候，表示需要创建一条交易
			if($("#isCreateTransaction").prop("checked",true)){

				//当按钮选中的时候，就创建一笔交易
				$("#tranForm").submit();

			}else{
				//没有选中的时候，不创建交易，执行传统请求即可
				window.location.herf="workbench/clue/convent.do?clueId=${param.id}";
			}
		});
	});
</script>

</head>
<body>
	
	<!-- 搜索市场活动的模态窗口 -->
	<div class="modal fade" id="searchActivityModal" role="dialog" >
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">搜索市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
							  <%-- 为搜索市场活动搜索框添加搜索点击事件
							  		id="selectActivity"
							  --%>
						    <input type="text" class="form-control" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询" id="selectActivity">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
								<td></td>
							</tr>
						</thead>
						<tbody id="tbody">
							<%--<tr>--%>
								<%--<td><input type="radio" name="activity"/></td>--%>
								<%--<td>发传单</td>--%>
								<%--<td>2020-10-10</td>--%>
								<%--<td>2020-10-20</td>--%>
								<%--<td>zhangsan</td>--%>
							<%--</tr>--%>

						</tbody>
					</table>
					<div class="modal-footer">
						<%--
							为提交按钮绑定事件
							id="submitActivityBtn"
						--%>
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
						<button type="button" class="btn btn-primary" id="submitActivityBtn">提交</button>
					</div>
				</div>
			</div>
		</div>
	</div>

	<%--
	id=${c.id}
	&fullname=${c.fullname}&
	appellation=${c.appellation}&
	company=${c.company}
	&owner=${c.owner}
		通过param进行取值[使用了隐含对象]
	--%>

	<%--转换弹出窗口--%>
	<div id="title" class="page-header" style="position: relative; left: 20px;">
		<h4>转换线索 <small>${param.fullname}${param.appellation}-${param.company}</small></h4>
	</div>
	<div id="create-customer" style="position: relative; left: 40px; height: 35px;">
		新建客户：${param.company}
	</div>
	<div id="create-contact" style="position: relative; left: 40px; height: 35px;">
		新建联系人：${param.fullname}${param.appellation}
	</div>

	<%--为客户创建交易按钮帮顶事件--%>
	<div id="create-transaction1" style="position: relative; left: 40px; height: 35px; top: 25px;">
		<input type="checkbox" id="isCreateTransaction"/>
		为客户创建交易
	</div>
	<div id="create-transaction2" style="position: relative; left: 40px; top: 20px; width: 80%; background-color: #F7F7F7; display: none;" >

		<%--
			为表单发送传统请求
			money
			name
			expectedDate
			stage
			activityId
		--%>
		<%--提交表单--%>
		<form action="workbench/clue/convent.do" method="post" id="tranForm">

			<%--这里设置一个隐藏域
				创建一个flag ,后端根据接受的flag的值，进行判断，这个表单是不是有值提交过去
				flag = a;提交表单

			--%>
			<input type="hidden" name="flag" value="a">

			<%--以提交表单的方式提交线索的id
				注意这个线索的id 不管提交表单，还是不提交表单，这个线索id都要提交
			--%>
			<input type="hidden" name="clueId" value="${param.id}">
		  <div class="form-group" style="width: 400px; position: relative; left: 20px;">
		    <label for="money">金额<span>(￥)</span></label>
			  <input type="text" class="form-control" name="money">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="name">交易名称</label>
		    <input type="text" class="form-control" name="name" >
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="expectedDate">预计成交日期</label>
			  <%--在这里添加一个时间控件
			  		expectedDate (10位)
			  --%>
		    <input type="text" class="form-control time-expectedDate" id="expectedDate" name="expectedDate" readonly>
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="stage">阶段</label>
		    <select id="stage"  class="form-control" name="stage">
				<%--从application 中进行取值--%>

		    	<option selected value="0">--请选择--</option><!--设置默认选中状态-->
		    	<c:forEach items="${applicationScope.stage}" var="s">
					<option value="${s.value}">${s.text}</option>
				</c:forEach>
		    </select>
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="activity">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" data-toggle="modal" data-target="#searchActivityModal" style="text-decoration: none;"><span class="glyphicon glyphicon-search"></span></a></label>

			  <%--这里存储的是市场活动的名字--%>
		    <input type="text" class="form-control" id="activityName" placeholder="点击上面搜索按钮进行搜索" readonly>
			  <%--在这里设置隐藏域
			  		将所选中的市场活动的id,存储在这里
			  		activityId
			  --%>
			<input type="hidden" id="activityId" name="activityId">
		  </div>
		</form>
		
	</div>
	
	<div id="owner" style="position: relative; left: 40px; height: 35px; top: 50px;">
		记录的所有者：<br>
		<%--从参数列表中获取所有者--%>
		<b>${param.owner}</b>
	</div>

	<%--转换按钮--%>
	<div id="operation" style="position: relative; left: 40px; height: 35px; top: 100px;">
		<%--
			为线索转换按钮绑定事件
			id='convertBtn'
		--%>
		<input class="btn btn-primary" type="button" value="转换" id="convertBtn">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<input class="btn btn-default" type="button" value="取消">
	</div>
</body>
</html>