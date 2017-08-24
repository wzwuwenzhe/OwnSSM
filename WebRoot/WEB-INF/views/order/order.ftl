<@htmlHeader4Order >
	<body>
	<div class="container">
		<div id="searchDiv" style="text-align:center">
			<form id="searchForm" action="${url("/orderSearch")}" method="post" >
			<input type="hidden" name="_token" value="${_token}"/>
			<#if userType=="1">
				<@form_group value="${(entity.storeId)!''}" id="storeId" desc="店铺Id" name="storeId" dataType="Chinese" msg="" type="text"/>
			</#if>
			<table>
				<tr>
					<td><@form_group value="${(entity.orderId)!''}" id="orderId" desc="交易号" name="orderId" dataType="Chinese" msg="" type="text"/></td>
					<td><@form_group value="${(entity.orderName)!''}" id="orderName" desc="商品名称" name="orderName" dataType="Chinese" msg="" type="text"/></td>
				</tr>
				<tr>
					<td><@form_group value="${(entity.cusName)!''}" id="cusName" desc="客户名称" name="cusName" dataType="Chinese" msg="客户名称必须全部为中文" type="text"/></td>
					<td><@form_group value="${(entity.operatorId)!''}" id="operatorId" desc="操作员ID" name="operatorId" dataType="Chinese" msg="客户名称必须全部为中文" type="text"/></td>
				</tr>
				<tr>
					<td colspan="2">查询时间:<@DateFields beginValue="${(entity.beginDate)!''}" endValue="${(entity.endDate)!''}"/></td>
				</tr>
			</table>
			
			<div class="form-group">
				<input type="submit" value="查询"/>
				<input type="button" value="报表打印" onclick="printReport()" />
				<input type="button" value="返回"   onclick="location.href='./index'" />
			</div>
			</form>
		</div>
		<div id="page">
		  <table id="table">
			<thead>
			  <tr>
				<th>商品名称</th>
				<th>单价</th>
				<th>尺寸</th>
				<th>数量</th>
				<th>金额(元)</th>
				<th>总计(元)</th>
			  	<th>序号</th>
				<th>客户名称</th>
				<th>操作员ID</th>
				<th>操作</th>
			  </tr>
			</thead>
			<tbody>
			<#list orderList as order>
				<tr>
					<#assign itemSize = order.itemList?size >
					<#if (order.itemList?size > 0 ) >
						<#assign itemList = order.itemList >
						<#list itemList as item>
							<#if (item_index == 0) >
								<td>${item.name}</td>
								<td>${item.unitPrice}</td>
								<td>${item.size}</td>
								<td>${item.amount}</td>
								<td>${item.price}</td>
							</#if>
						</#list>
					</#if>
					<td rowspan="${itemSize}">${order.totalAmount}</td>
					<td rowspan="${itemSize}">${order_index+1}</td>
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
								<td>${item.size}</td>
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