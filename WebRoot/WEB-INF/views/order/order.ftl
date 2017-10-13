<@htmlHeader4Order >
	<body>
		<div id="searchDiv" style="text-align:center">
			<form id="searchForm" action="${url("/orderSearch")}" method="post" >
			<input type="hidden" name="_token" value="${_token}"/>
			
			<#if userType=="1">
				<@form_input value="${(entity.storeId)!''}" id="storeId" desc="店铺Id" name="storeId" dataType="Chinese" msg="" type="text" style="width:100px;"/>
			</#if>
			<div class="layui-fluid">
			  <div class="layui-row">
			    <div class="layui-col-sm3">
			      <div class="grid-demo grid-demo-bg1">
					<@form_input value="${(entity.orderId)!''}" id="orderId" desc="交易号" name="orderId" dataType="Chinese" msg="" type="text" style="width:100px;"/>
			      </div>
			    </div>
			    <div class="layui-col-sm3">
			      <div class="grid-demo">
					<@form_input value="${(entity.orderName)!''}" id="orderName" desc="商品名称" name="orderName" dataType="Chinese" msg="" type="text" style="width:100px;"/>
				  </div>
			    </div>
			    <div class="layui-col-sm3">
			      <div class="grid-demo grid-demo-bg1">
					<@form_input value="${(entity.cusName)!''}" id="cusName" desc="客户名称" name="cusName" dataType="Chinese" msg="客户名称必须全部为中文" type="text" style="width:100px;"/>
				  </div>
			    </div>
			    <div class="layui-col-sm3">
			      <div class="grid-demo">
					<@form_input value="${(entity.operatorId)!''}" id="operatorId" desc="操作员ID" name="operatorId" dataType="Chinese" msg="客户名称必须全部为中文" type="text" style="width:100px;"/>
				  </div>
			    </div>
			  </div>
			</div>
			<div style="margin: 10px;">
				查询时间:<@DateFields beginValue="${(entity.beginDate)!''}" endValue="${(entity.endDate)!''}"/>
			</div>
			
			<div class="form-group">
				<input class="layui-btn layui-btn-radius" type="submit" value="查询"/>
				<input class="layui-btn layui-btn-warm layui-btn-radius" type="button" value="报表打印" onclick="printReport()" />
				<input class="layui-btn layui-btn-primary layui-btn-radius" type="button" value="返回"   onclick="location.href='./index'" />
			</div>
			</form>
		</div>
	<div class="container">
		<div id="page" >
		  <table id="table" class="layui-table" lay-filter="parse-table-demo">
  		  	<thead>
		  		<tr>
				  	<th>序号</th>
					<th>商品名称</th>
					<th>单价</th>
					<th>数量</th>
					<th>金额(元)</th>
					<th>总计(元)</th>
					<th>付款方式</th>
					<th>客户名称</th>
					<th>操作员ID</th>
					<th>操作</th>
				  </tr>
		  	</thead>
			<tbody>
			<#list orderList as order>
				<tr>
					<#assign itemSize = order.itemList?size >
					<td rowspan="${itemSize}">${order_index+1}</td>
					<#if (order.itemList?size > 0 ) >
						<#assign itemList = order.itemList >
						<#list itemList as item>
							<#if (item_index == 0) >
								<td>${item.name}</td>
								<td>${item.unitPrice}</td>
								<td>${item.amount}</td>
								<td>${item.price}</td>
							</#if>
						</#list>
					</#if>
					<td rowspan="${itemSize}">${order.totalAmount}</td>
					<td rowspan="${itemSize}">${order.payTypeDesc}</td>
					<td rowspan="${itemSize}">${order.cusName}</td> 
					<td rowspan="${itemSize}">${order.operatorId}</td>
					<td rowspan="${itemSize}">
						<a href="javascript:void(0)" onclick="rePrint('${order.id}');">重新打印</a> | 
						<#if userType=="1" || userType=="3">
							<a href="javascript:void(0)" onclick="deleteOrder('${order.id}');" >删除订单</a>
						</#if>
					</td>
				</tr>
				<#if (order.itemList?size > 1 ) >
					<#assign _itemList = order.itemList >
					<#list _itemList as item>
						<#if (item_index>0)>
							<tr>
								<td>${item.name}</td>
								<td>${item.unitPrice}</td>
								<td>${item.amount}</td>
								<td>${item.price}</td>
							</tr>
						</#if>
						
					</#list>
				</#if>
		    </#list>
			</tbody>
			</table>
		</div>
	</div>
		
	<script type="text/javascript">
		$(document).ready(function() {
		  $('#table').basictable();
		});
		
		function rePrint(orderId){
			$.ajax({
		        url:"./rePrint",
		        type:"POST",
		        dataType:"json",
		        data:{"orderId":orderId},
		        success:function(response){
		            alert(response.message)
		        },
		        error:function(){
		            alert("出错了,请联系管理员");
		        }
    		});
		}
		
		function deleteOrder(orderId){
			if(!confirm("确定要删除该订单吗,删除后不可恢复!")){
				return;
			}
			$.ajax({
		        url:"./deleteOrder",
		        type:"POST",
		        dataType:"json",
		        data:{"orderId":orderId},
		        success:function(response){
		            alert(response.message)
		            $("#searchForm").submit();
		        },
		        error:function(){
		            alert("出错了,请联系管理员");
		        }
    		});
		}
		
		//报表打印
		function printReport(){
			var beginDate = $("#beginDate").val();
			var endDate = $("#endDate").val();
			var storeId = $("#storeId").val();
			$.ajax({
		        url:"./printReport",
		        type:"POST",
		        dataType:"json",
		        data:{"beginDate":beginDate,"endDate":endDate,"storeId":storeId},
		        success:function(response){
		            alert(response.message)
		        },
		        error:function(){
		            alert("出错了,请联系管理员");
		        }
    		});
		}
	  </script>
	</body>
</@htmlHeader4Order>