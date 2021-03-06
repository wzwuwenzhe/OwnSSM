<@htmlHeader4Order >
	<body>
	<div style="text-align:center">
		<input class="layui-btn layui-btn-radius" type="button" value="返回上一页" onclick="location.href='./showStockManage'"/>
		<input class="layui-btn layui-btn-normal layui-btn-radius" type="button" value="返回首页" onclick="location.href='./index'"/>
	</div>
	<div class="container">
		<div id="page">
		  <table id="table" class="layui-table" lay-filter="parse-table-demo">
			<thead>
			  <tr>
				<th>厂家</th>
				<th>颜色</th>
				<th>尺码</th>
				<th>单次数量</th>
				<th>入库时间</th>
				<th>款号</th>
				<th>库存</th>
			  	<th>合计</th>
				<th>操作</th>
			  </tr>
			</thead>
			<tbody>
			<#list storageList as storage>
				<tr>
					<#assign stockSize = storage.stockList?size >
					<#if (stockSize > 0 ) >
						<#assign stockList = storage.stockList >
						<#list stockList as stock>
							<#if (stock_index == 0) >
								<td>${stock.factoryName}</td>
								<td>${stock.color}</td>
								<td>${stock.size}</td>
								<td>${stock.amount}</td>
								<td>${stock.creationTime}</td>
							</#if>
						</#list>
					</#if>
					<td rowspan="${stockSize}">${storage.name}</td>
					<td rowspan="${stockSize}">${storage.stockLeft}</td>
					<td rowspan="${stockSize}">${storage.total}</td> 
					<td rowspan="${stockSize}">
						<input type="button" value="入库" onclick="putInStock('${storage.name}')"/>
						<#if userType=="1" || userType=="3">
							<input type="button" value="删除订单" onclick="deleteStorage('${storage.name}');" />
						</#if>
					</td>
				</tr>
				<#if (stockSize > 1 ) >
					<#assign _stockList = storage.stockList >
					<#list _stockList as stock>
						<#if (stock_index>0)>
							<tr>
								<td>${stock.factoryName}</td>
								<td>${stock.color}</td>
								<td>${stock.size}</td>
								<td>${stock.amount}</td>
								<td>${stock.creationTime}</td>
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
		function deleteStorage(storageName){
			if(!confirm("确定要删除该订单吗,删除后不可恢复!")){
				return;
			}
			$.ajax({
		        url:"./deleteStorage",
		        type:"POST",
		        dataType:"json",
		        data:{"name":storageName},
		        success:function(response){
		            alert(response.message)
		            location.href="./storageList"
		        },
		        error:function(){
		            alert("出错了,请联系管理员");
		        }
    		});
		}
		function putInStock(name){
			location.href='./putInStock?name='+name;
		}
	  </script>
	</body>
</@htmlHeader4Order>