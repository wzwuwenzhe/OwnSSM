<@htmlHeader4Order >
	<body>
		<div id="searchDiv" style="text-align:center">
			<form id="searchForm" action="${url("/logSearch")}" method="post" >
			<input type="hidden" name="_token" value="${_token}"/>
			
			<div class="layui-fluid">
			  <div class="layui-row">
			    <div class="layui-col-sm3">
			      <div class="grid-demo">
					<@form_input value="${(entity.storeId)!''}" id="storeId" desc="店铺Id" name="storeId" dataType="Chinese" msg="" type="text" style="width:100px;"/>
				  </div>
			    </div>
			    <div class="layui-col-sm3">
			      <div class="grid-demo">
					<@form_input value="${(entity.operatorId)!''}" id="operatorId" desc="操作员ID" name="operatorId" dataType="Chinese" msg="客户名称必须全部为中文" type="text" style="width:100px;"/>
				  </div>
			    </div>
				<div class="layui-col-sm3">
			      <div class="grid-demo">
					<@form_input value="${(entity.operatorName)!''}" id="operatorName" desc="操作员姓名" name="operatorName" dataType="Chinese" msg="客户名称必须全部为中文" type="text" style="width:100px;"/>
				  </div>
			    </div>
			  </div>
			</div>
			<div style="margin: 10px;">
				<#assign _today=jodaTime.now().toString("yyyy-MM-dd HH:mm:ss")/>
				操作时间:<@DateFields style="width:155px;" needHour=true begin=_today beginValue="${(entity.beginDate)!''}" endValue="${(entity.endDate)!''}"/>
			</div>
			
			<div class="form-group">
				<input class="layui-btn layui-btn-radius" type="submit" value="查询"/>
				<input class="layui-btn layui-btn-primary layui-btn-radius" type="button" value="返回"   onclick="location.href='./index'" />
			</div>
			<input type="hidden" id="start" name="start"/>
			</form>
		</div>
	<div class="container">
		<div  >
		  <table id="table" class="layui-table" lay-filter="parse-table-demo">
  		  	<thead>
		  		<tr>
					<th style="width:80px">店铺ID</th>
				  	<th style="width:80px">操作员ID</th>
					<th style="width:65px">操作员姓名</th>
					<th style="width:65px">请求时间</th>
					<th style="width:100px">请求地址</th>
					<th style="width:200px">请求参数</th>
				  </tr>
		  	</thead>
			<tbody>
				<#list logList as log>
					<tr>
						<td>${log.storeId}</td>
						<td>${log.operatorId}</td>
						<td>${log.operatorName}</td>
						<td>${log.operateTime}</td>
						<td>${log.requestUrl}</td>
						<td>${log.params}</td>
					</tr>
				</#list>
			</tbody>
			</table>
		</div>
	</div>
	<br/>
	<div id="page"></div>

		
	<script type="text/javascript">
	 //分页功能
	  layui.use(['laypage', 'layer'], function(){
		  var laypage = layui.laypage
		  ,layer = layui.layer;
		  laypage.render({
		    elem: 'page'
		    ,count: '${page.total}'
		    ,curr :'${page.start}'
		    ,layout: ['count', 'prev', 'page', 'next']
		    ,jump: function(obj,first){
			    if(!first){
				    var start = obj.curr;
				    $("#start").val(start);
			    	$("#searchForm").submit();
			    }
		    }
	  });
	  });
	</script>
</body>
</@htmlHeader4Order>