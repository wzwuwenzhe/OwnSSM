<@htmlHeader4Order >
	<body>
	<div class="container">
		<div id="searchDiv" style="text-align:center">
			<form action="${url("/orderSearch")}" method="post" >
			<input type="hidden" name="_token" value="${_token}"/>
			<@form_group id="cusName" desc="商品名称" name="itemName" dataType="Chinese" msg="" type="text"/>
			<@form_group id="cusName" desc="交易号" name="orderId" dataType="Chinese" msg="" type="text"/>
			<@form_group id="cusName" desc="客户名称" name="cusName" dataType="Chinese" msg="客户名称必须全部为中文" type="text"/>
			<@form_group id="cusName" desc="操作员ID" name="operatorId" dataType="Chinese" msg="客户名称必须全部为中文" type="text"/>
			查询时间:<@DateFields beginValue="${(entity.beginDate)!''}" endValue="${(entity.endDate)!''}"/>
			<div class="form-group">
				<input type="submit" value="查询"/>
			</div>
			</form>
		</div>
		<div id="page">
		  <table id="table">
			<thead>
			  <tr>
				<th>Name</th>
				<th>Age</th>
				<th>Gender</th>
				<th>Height</th>
				<th>Province</th>
				<th>Sport</th>
			  </tr>
			</thead>
			<tbody>
			  <tr>
				<td>Jill Smith</td>
				<td>25</td> 
				<td>Female</td>
				<td>5'4</td>
				<td>British Columbia</td>
				<td>Volleyball</td>
			  </tr>
			</tbody>
		  </table>
		</div>
	</div>
	
	<script type="text/javascript">
		$(document).ready(function() {
		  $('#table').basictable();
		});
	  </script>
	
	</body>
</@htmlHeader4Order>