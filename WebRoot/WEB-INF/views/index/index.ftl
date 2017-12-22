<@htmlHeader4Main />
<@headerBanner4Main/>
	<@banner4Main/>
		<@htmlBody4Main>
			<@project name="下单" href="/billing" imgName="bill.png"/>
			<@project name="订单查询" href="/orderSearch" imgName="searchOrder.png"/>
			<@project name="库存管理" href="/showStockManage" imgName="stock_manage.png"/>
			<@project name="客户管理" href="/showClient" imgName="customer.png"/>
			<@project name="工厂管理" href="/showFactory" imgName="factory.png"/>
			<#if (userType=="1" || userType=="3")>
				<@project name="店铺注册" href="/storeRegister" imgName="shop.png"/>
				<@project name="新增操作员" href="/operatorRegister" imgName="operator.png"/>
			</#if>
			<@project name="用户中心" href="/operatorInfo" imgName="operatorInfo.png"/>
			<div class="col-lg-3 col-md-6 col-sm-6 work"> 
				<a href="javascript:void(0)" onclick="shutdownPC()" class="work-box"> <img src="${img("/image/main/power.png")}" alt="">
					<h5>关机</h5>
				</a> 
			</div>
		<script type="text/javascript">
		function shutdownPC(){
			infoUtil.confirm("<p class='confirm-info'>您确定要关机吗?</p>",{
		        title:'关机二次确认',
		        closeBtn:0,
		        shift:-1,
		        btn: ['确定','取消'] //按钮
		    },function(_index){
		        $.post("./shutdownPC",function(){
					alert("关机完成!");
				});
				infoUtil.alert("<p class='confirm-info'>正在关机!</p>");
		    },function(){});
			
		}
		</script>
		</@htmlBody4Main>
<@htmlFooter4Main/>
