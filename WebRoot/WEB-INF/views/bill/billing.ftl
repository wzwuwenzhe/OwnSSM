<@htmlHeader>
	<@css path="/css/loginAndRegister/bill.css"/>
	<@css path="/css/billing/autocomplete.css" />
	<@js path="/js/autocomplete.js" />
	<@js path="/js/map.js" />
</@htmlHeader>
<@htmlBody>
	<@form action="/billing" onsubmit="return $form.submit(this,_loginCallback);" class="fh5co-form animate-box" h2="开单">
	<div class="form-group">
		<input type="hidden" id="cusId" name="cusId" />
		<div id="demo">
			<div class="wrapper">
				<div id="search-form"></div>
			</div>
		</div>
	</div>
	<div class="form-group">
		<table class="billtable">
			<thead>
				<tr>
					<td style="width:35%">商品名称</td>
					<td style="width:10%">数量</td>
					<td style="width:15%">单价(元)</td>
					<td style="width:20%">金额(元)</td>
					<td style="width:20%">操作</td>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td colspan="6"><input class="form-control" type="button" value="新增" onclick="addRecord(this)"/></td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="form-group">
		小计:<label id='smallCount'></label>元 
		<input type="hidden" name="smallCount"/>
		<br/>
		折扣金额:<input id='discount' name="discount" onkeyup="calculateTotal()" style="width:50px;" type="text" value="0" />元 <br/>
		应付金额:<label id='totalAmount'></label>元 
		<input type="hidden" name="totalAmount"/><br/>
		付款方式:
		<input type="radio" id="payType1" name="payType" value="1" checked="checked"/><label for="payType1">现金</label>
		<input type="radio" id="payType2" name="payType" value="2" /><label for="payType2">刷卡</label>
		<input type="radio" id="payType3" name="payType" value="3" /><label for="payType3">支付宝</label>
		<input type="radio" id="payType4" name="payType" value="4" /><label for="payType4">微信</label>
		<br/>
	</div>
	<@form_group id="remark" value="${(order.remark)!''}" desc="备注" type="text" name="remark" dataType="" msg="" />
	<div class="form-group">
		<input type="submit" value="下单" class="btn btn-primary">
		<input type="button" value="返回" class="btn btn-primary back"  onclick="window.history.back()">
	</div>
	</@form>
	<@showMsg type="danger"/>
	<script type="text/javascript">
		<#if _token?exists>_token='${_token}';</#if>
	
	var proposals = new Array();
	var map = new Map();
	<#list clientList as client>
		proposals[${client_index}] = '${client.name}';
		var Custom = new Object();
		Custom.id = '${client.id}';
		Custom.name = '${client.name}';
		Custom.phone = '${client.phone}';
		map.put('${client.name}',Custom);
	</#list>
	
	function cleanIdValue(){
		$("#cusId").val('');
	}
	function cleanValue(){
		$("#cusId").val('');
		$("#cusName").val('');
	}
	
	$(document).ready(function(){
		$('#search-form').autocomplete({
			hints: proposals,
			width: 300,
			height: 30,
			onSubmit: function(text){
				$('#message').html('Selected: <b>' + text + '</b>');			
			}
		});
	});
	


	function removeRecord(btn){
		$(btn).parent().parent().remove();
		calculateTotal();
	}
	function addRecord(btn){
		$(btn).parent().parent().before("<tr>"+
		"<td><input name='name' dataType='Require' msg='商品名称不能为空' class='form-control' type='text'/><input type='hidden' name='size' value='通码'/></td>"+
		"<td><input name='amount' dataType='Number' msg='数量必须为正整数' class='form-control' type='text' onkeyup='calculate(this)' /></td>"+
		"<td><input name='unitPrice' dataType='Double' msg='单价必须为数字(可包含小数)' class='form-control' type='text' onkeyup='calculate(this)'/></td>"+
		"<td><input name='price' class='price' type='hidden'  /> <label class='price' ></label>元</td>"+
		"<td><input  class='form-control' type='button'  value='删除' onclick='removeRecord(this)'/></td>"+
		"</tr>");
	}
	function calculate(number){
		var tr = $(number).parent().parent();
		$("#smallCount").html('');
		$("#totalAmount").html('');
		tr.find(".price").val('');
		tr.find(".price").html('');
		if(Validator.Double.test($(number).val())){
			var unitPrice = tr.find("input[name='unitPrice']").eq(0).val();
			var amount = tr.find("input[name='amount']").eq(0).val();
			if(amount=="" || !Validator.Integer.test(amount) || amount<=0){
				return;
			}
			if(unitPrice=="" || !Validator.Double.test(unitPrice) || unitPrice<0){
				return;
			}
			var prize = parseFloat(unitPrice)*parseFloat(amount);
			tr.find(".price").val(prize.toFixed(2));
			tr.find(".price").html(prize.toFixed(2));
		}
		calculateTotal()
	}
	function calculateTotal(){
		//小计
		var smallCount =0;
		$(".billtable").find("input[name='price']").each(function(){
			var each = $(this).val();
			if(Validator.Double.test(each)){
				smallCount += parseFloat(each);
			}
		});
		$("#smallCount").html(smallCount.toFixed(2));
		$("#smallCount").next().val(smallCount.toFixed(2));
		//应付
		var totalAmount =0;
		var discount = 0;
		if(Validator.Double.test($("#discount").val())){
			discount = parseFloat($("#discount").val());
		}
		var temp = parseFloat(smallCount.toFixed(2))-discount;
		$("#totalAmount").html(temp.toFixed(2));
		$("#totalAmount").next().val(temp.toFixed(2));
	}
	
	var _loginCallback=function(response){
			if(response.success==true){
			    $form.reset("#_form");
			    alert(response.message);
			    location.href='${url("")}';
			}else{
				alert(response.msg)
			}
		}
	
	
	</script>
</@htmlBody>
<@htmlFooter/>
	

