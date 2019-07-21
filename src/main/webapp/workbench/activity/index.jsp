<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
%>
<%--用来展示用户编码修改字符集
    如果换成jsp文件需要
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
	<%--模板--%>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
		<%--时间插件--%>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<link rel="stylesheet" type="text/css" href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css">

		<%--在这里导入分页插件--%>
		<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
		<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
		<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

<script type="text/javascript">

	$(function(){

		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});
		
		$("#addBtn").click(function(){

			$.ajax({
				/*发送请求到用户表中进行查询数据*/
				url:"workbench/activity/getUserList.do",
				type:"get",
				dataType:"json",
				success:function(data){
					//返回来是一个userList
					var html = "<option>--请选择--</option>";

					//遍历集合，将集合中的数据一次添加到<option>标签中
					$.each(data,function(i,n){
						//将取出来的数据存放到option中
						html += "<option value='"+ n.id +"'>"+n.name+"</option>";

					});
					//将html 文件转载到下拉列表中
					$("#create-owner").html(html);

					//将当前用户作为默认者选项
					var id = "${user.id}"; //获取用户的id,并将用户装载到下拉列表中
					$("#create-owner").val(id);

					//展开模态窗口
					$("#createActivityModal").modal("show");
				}

			});

		});

		//为保存按钮添加一个Ajax请求
		$("#saveBtn").click(function(){

			$.ajax({
				/*添加市场活动*/
				url:"workbench/activity/saveActivity.do",
				data:{

					owner:$.trim($("#create-owner").val()),
					name:$.trim($("#create-name").val()),
					startDate:$.trim($("#create-startDate").val()),
					endDate:$.trim($("#create-endDate").val()),
					cost:$.trim($("#create-cost").val()),
					description:$.trim($("#create-description").val())


				},
				type:"post",
				dataType:"json",
				success:function(data){
					alert(data.success);
					if(data.success){

						//重置
						//将jq 对象转换成为dom对象，进行重置
						$("#activitySaveForm")[0].reset();

						//关闭市场活动模态窗口
						$("#createActivityModal").modal("hide");
					}else{

						alert("市场活动添加失败");

					}
				}


			});
		});

		//1.页面加载完毕之后，进行分页查询
		pageList(1,2);

		//执行查询市场活动
		//需要传递的参数有六个
		$("#select-Btn").click(function(){

			//点击按钮时，暂时将查询的数据存放到蕴藏作用域中
			$("#hidden-name").val($.trim($("#select-name").val()));
			$("#hidden-owner").val($.trim($("#select-owner").val()));
			$("#hidden-startDate").val($.trim($("#select-startDate").val()));
			$("#hidden-endDate").val($.trim($("#select-endDate").val()));


			pageList(1,2);

		});

		//为全选按钮绑定事件
		$("#selectAll").click(function(){

			$("input[name=selectOne]").prop("checked",this.checked);
		});

		//反选按钮
		$("#activityBody").on("click",$("input[name=selectOne]"),function(){

			//on(触发事件的方式，需要绑定的元素，回调函数);
			$("#selectAll").prop("checked",$("input[name=selectOne]").length==$("input[name=selectOne]:checked").length);
		});

		//为删除按钮绑定删除事件
		$("#delBtn").click(function(){
			alert("delete");
			var $selectOne = $("input[name=selectOne]:checked");

			if($selectOne.length == 0){
				//表示没选中
				alert("请选择需要删除的记录");
			}else{

				if(confirm("您确定要删除吗？")){
					var param = "";
					for(var i = 0;i < $selectOne.length;i++){
						/*在这里获得复选框中的val()值*/
						param += "id="+$($selectOne[i]).val();

						// 在这里输出分割符号
						if(i < $selectOne.length - 1){
							param += "&";
						}

					}

					//将选中的数据发送ajax请求
					//这里param
					$.ajax({
						//通过id 值进行删除
						url:"workbench/activity/delBySelId.do",
						data:param,
						type:"post",
						dataType:"json",
						success:function(data){

							if(data.success){
								//删除成功，刷新列表
								pageList(1,$("#activityPage").bs_pagination('getOption','rowsPerPage'));
							}else{
								//删除失		败
								alert("删除失败");

							}
						}

					});

				}
			}


		});

		//为修改绑定事件[需要做]
		$("#editBtn").click(function(){

			var $selectOne = $("input[name=selectOne]:checked");

			if($selectOne.length == 0){
				alert("请选择您要修改的记录");
			}else if($selectOne.length > 1){
				alert("您只能选择一条数据进行修改")
			}else{
				alert("update");
				//对复选框进行取值
				var id = $selectOne.val();
				alert(id);
				//通过这个修改发送AJAx请求
				$.ajax({
					/*通过发送id 进行修改*/
					url:"workbench/activity/getUserListAndActivity.do",
					data:{
						id:id
					},
					type:"post",
					dataType:"json",
					success:function(data){
						/*
						* 	success : true/false
						* 	data
						* */
						var html = "<option></option>";

						//从后台查询所有的用户列表【"userList":userList,"aLiost":aList】
						$.each(data.userList,function(i,n){
							//遍历用户列表，并将用户列表装载到下拉列表中
							html += "<option value='"+n.id+"'>"+ n.name +"</option>";

						});

						//为修改下拉列表进行铺值
						$("#edit-owner").html(html);

						//为修改操作的模态窗口的表单元素铺设数值
						$("#edit-id").val(data.aList.id);
						$("#edit-name").val(data.aList.name);
						$("#edit-owner").val(data.aList.owner);
						$("#edit-startDate").val(data.aList.startDate);
						$("#edit-endDate").val(data.aList.endDate);
						$("#edit-cost").val(data.aList.cost);
						$("#edit-description").val(data.aList.description);

						//打开修改操作模态窗口
						$("#editActivityModal").modal("show");
					}

				});
			}

		});

		//为更新绑定事件[需要做]
		$("#activity-update").click(function(){

            //执行流程
            //1.得到文本框中的值，将值发送到修改页面
            //2.修改成功，关闭模态窗口
			$.ajax({

				url:"workbench/activity/updateActivityList.do",
				data:{
					id:$.trim($("#edit-id").val()),
					owner:$.trim($("#edit-owner").val()),
					name:$.trim($("#edit-name").val()),
					startDate:$.trim($("#edit-startDate").val()),
					endDate:$.trim($("#edit-endDate").val()),
					cost:$.trim($("#edit-cost").val()),
					description:$.trim($("#edit-description").val())

				},
				type:"post",
				dataType:"json",
				success:function(data){

					if(data.success){
						alert("市场活动修改成功");
						/*
							$("#activityPage").bs_pagination('getOption', 'currentPage'):
								维持当前页的页码
							$("#activityPage").bs_pagination('getOption', 'rowsPerPage')：
								维持每页展现的记录数
						 */
						pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
								,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));


						//关闭模态窗口
						$("#editActivityModal").modal("hide");

					}else{
						alert("市场活动修改失败");
					}

				}

			});
		});
	});

	//分页
	function pageList(pageNo,pageSize){

		alert("页面加载时执行");

		//点击分页组件的时候，将隐藏域中的数据取出来
		$("#select-name").val($.trim($("#hidden-name").val()));
		$("#select-owner").val($.trim($("#hidden-owner").val()));
		$("#select-startDate").val($.trim($("#hidden-startDate").val()));
		$("#select-endDate").val($.trim($("#hidden-endDate").val()));


		//分页查询

		$.ajax({

			url:"workbench/activity/pageList.do",
			data:{
				pageNo:pageNo,
				pageSize:pageSize,
				name:$.trim($("#select-name").val()),
				owner:$.trim($("#select-owner").val()),
				startDate:$.trim($("#select-startDate").val()),
				endDate:$.trim($("#select-endDate").val())
			},
			type:"get",
			dataType:"json",
			success:function(data){

				//对返回来的数据进行处理
				//前端需要总页数，所有条数的信息
				var html = "";
				//这里遍历的话要获取n的属性
				$.each(data.dataList,function(i,n){

					//对返回来的列表进行遍历
					html += '<tr class="active">';
					/*在这里设置单选按钮*/
					//在这里获取所选中id的值
					html += '<td><input type="checkbox" name="selectOne" value="'+n.id+'"/></td>';
					//通过在这里发送一个传统请求，发送一个传统请求

					html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>';
					html += '<td>'+n.owner+'</td>';
					html += '<td>'+n.startDate+'</td>';
					html += '<td>'+n.endDate+'</td>';
					html += '</tr>'


				});
				//将遍历出来的列表数据装载到table表中；
				$("#activityBody").html(html);

				//计算总页数
				var totalPages = data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1;

				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数

					visiblePageLinks: 3, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					//该回调函数的触发时机：当点击分页查询的时候触发
					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});
			}

		});
	}
	
</script>
</head>
<body>

	<%--在这里创建一个隐藏域对象
		将点击查询到数据暂时先存放到这里

		点击分页组件的时候，就不会再到数据库中去查询数据
	--%>
	<div>
		<input type="hidden" id="hidden-name"/>
		<input type="hidden" id="hidden-owner"/>
		<input type="hidden" id="hidden-startDate"/>
		<input type="hidden" id="hidden-endDate"/>
	</div>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form" id="activitySaveForm">

						<div>
							<%--将修改的id值暂时存放在隐藏域中--%>
							<input type="hidden" id="edit-id"/>
						</div>

						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">

									<%--将循环遍历的所有者装载到下拉列表中--%>
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" >
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<%--
									将开始于结束日期设置为只读
									利用时间插件，将时间设置为19位
								--%>
								<input type="text" class="form-control time"  readonly>
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time"  readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" >
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" ></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">

									<%--
										从数据库中获取数
										user.name
										进行铺设
										所有修改的数值全部以edit-* 进行操作
									--%>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-name" value="发传单">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-startDate" >
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-endDate" >
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<%--注意给文本域铺设值得时候需要注意
									中间部分不能存在空格
									<textarea></textarea>
								--%>
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal" id="activity-update">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

					<%--在这里需要将参数更改成为以 select-name ...来进行查询

					--%>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="select-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="select-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="select-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="select-endDate">
				    </div>
				  </div>

					<%--将查询按钮更换为button , 并为button绑定事件--%>
				  <button type="button" class="btn btn-default" id="select-Btn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
					<%--创建市场活动列表
						为创建市场活动按钮绑定事件
						addBtn
						editBtn
						delBtn
					--%>
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="delBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="selectAll"/></td>
							<%-- 全选与全不选--%>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<%--为市场活动添加id属性--%>
					<tbody id="activityBody">



					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage">
					<%--将分页插件引入到这个div中--%>
				</div>

			</div>
			
		</div>
		
	</div>
</body>
</html>