<@htmlHeader>
	<@css path="/css/loginAndRegister/bill.css"/>
</@htmlHeader>
<@htmlBody>
	<@form action="/billing" onsubmit="return $form.submit(this,_loginCallback);" class="fh5co-form animate-box" h2="开单">
	<div class="form-group">
		<#if (clientList?size>0)>
		客户:<select name="cusId">
				<#list clientList as client>
					<option value="${client.id}">${client.name}</option>
				</#list>
			</select>
		<#else>
			<a style="text-decoration:none;" href="${url("/clientRegister")}">您还没有客户,请先点我添加客户</a>
		</#if>
	</div>
	<div class="form-group">
		<table class="billtable">
			<thead>
				<tr>
					<td style="width:35%">商品名称</td>
					<td style="width:10%">尺寸</td>
					<td style="width:15%">单价(元)</td>
					<td style="width:10%">数量</td>
					<td style="width:20%">金额(元)</td>
					<td style="width:10%">操作</td>
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
		<input type="hidden" name="totalAmount"/>
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

	
	function removeRecord(btn){
		$(btn).parent().parent().remove();
		calculateTotal();
	}
	function addRecord(btn){
		$(btn).parent().parent().before("<tr>"+
		"<td><input name='name' dataType='Require' msg='商品名称不能为空' class='form-control' type='text'/></td>"+
		"<td><select name='size'><option value='M'>M</option><option value='L'>L</option><option value='XL'>XL</option></select></td>"+
		"<td><input name='unitPrice' dataType='Double' msg='单价必须为数字(可包含小数)' class='form-control' type='text' onkeyup='calculate(this)'/></td>"+
		"<td><input name='amount' dataType='Number' msg='数量必须为正整数' class='form-control' type='text' onkeyup='calculate(this)' /></td>"+
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
	

