<@htmlHeader4Order >
	<body>
		<div id="searchDiv" style="text-align:center">
			<form id="searchForm" action="${url("/logSearch")}" method="post" >
			<input type="hidden" name="_token" value="${_token}"/>
			
			<@form_input value="${(entity.storeId)!''}" id="storeId" desc="店铺Id" name="storeId" dataType="Chinese" msg="" type="text" style="width:100px;"/>
			<div class="layui-fluid">
			  <div class="layui-row">
			    <div class="layui-col-sm3">
			      <div class="grid-demo">
					<@form_input value="${(entity.operatorId)!''}" id="operatorId" desc="操作员ID" name="operatorId" dataType="Chinese" msg="客户名称必须全部为中文" type="text" style="width:100px;"/>
				  </div>
			    </div>
				<div class="layui-col-sm3">
			      <div class="grid-demo">
					<@form_input value="${(entity.OperatorName)!''}" id="operatorName" desc="操作员姓名" name="operatorName" dataType="Chinese" msg="客户名称必须全部为中文" type="text" style="width:100px;"/>
				  </div>
			    </div>
			  </div>
			</div>
			<div style="margin: 10px;">
				<#assign _today=jodaTime.now().toString("yyyyMMddHHmmss")/>
				操作时间:<@DateFields begin=_today beginValue="${(entity.startTime)!''}" endValue="${(entity.endTime)!''}"/>
			</div>
			
			<div class="form-group">
				<input class="layui-btn layui-btn-radius" type="submit" value="查询"/>
				<input class="layui-btn layui-btn-primary layui-btn-radius" type="button" value="返回"   onclick="location.href='./index'" />
			</div>
			</form>
		</div>
	<div class="container">
		<div id="page" >
		  <table id="table" class="layui-table" lay-filter="parse-table-demo">
  		  	<thead>
		  		<tr>
					<th>店铺ID</th>
				  	<th>操作员ID</th>
					<th>操作员姓名</th>
					<th>请求地址</th>
					<th>请求参数</th>
				  </tr>
		  	</thead>
			<tbody>
			</tbody>
			</table>
		</div>
	</div>
		
	<@form id="payform" action="/payForTheMoney" onsubmit="return $form.submit(this,_loginCallback);" class="fh5co-form animate-box" h2="" style="display:none">
		<div style="margin:10px;">
			付款方式：</br>
			<input type="radio" id="payType1" name="payType" value="1" checked="checked" /><label for="payType1">现金</label>
			<input type="radio" id="payType2" name="payType" value="2" /><label for="payType2">刷卡</label>
			<input type="radio" id="payType3" name="payType" value="3" /><label for="payType3">支付宝</label>
			<input type="radio" id="payType4" name="payType" value="4" /><label for="payType4">微信</label>
			<input type="radio" id="payType6" name="payType" value="6" /><label for="payType6">月结</label>
		</div>
    </@form>

	<@form id="deliverform" action="/deliverGoods" onsubmit="return $form.submit(this,_loginCallback);" class="fh5co-form animate-box" h2="" style="display:none">
		<div style="margin:10px;" id="deliverformDiv">
			<table id="table" class="layui-table" lay-filter="parse-table-demo">
  		  	<thead>
		  		<tr>
					<th>商品名称</th>
					<th>颜色</th>
					<th>尺码</th>
					<th>单价</th>
					<th>数量</th>
					<th>金额(元)</th>
			  	</tr>
		  	</thead>
			<tbody></tbody>
			</table>
		</div>
    </@form>
		
	<script type="text/javascript">
	
	</script>
</body>
</@htmlHeader4Order>