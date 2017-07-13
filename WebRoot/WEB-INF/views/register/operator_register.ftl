<@htmlHeader/>
<@htmlBody>
	<@form action="#" class="fh5co-form animate-box" h2="操作员注册">
		<div class="form-group">
			<div class="alert alert-success" role="alert">提醒放在这里</div>
		</div>
		<@form_group id="storeId" value="店铺ID" type="text"/>
		<@form_group id="opName" value="您的姓名" type="text"/>
		<@form_group id="mobilephone" value="手机" type="text"/>
		<!-- 只有超管才能看到这个选项 并且选择用户类型-->
		<@form_group id="opType" value="用户类型" type="text"/>
		<@form_group id="loginName" value="登录名" type="text"/>
		<@form_group id="password" value="密码" type="text"/>
		<div class="form-group">
			<label for="re-password" class="sr-only">确认密码</label>
			<input type="password" class="form-control" id="re-password" placeholder="确认密码" autocomplete="off">
		</div>
		<div class="form-group">
			<input type="submit" value="操作员用户" class="btn btn-primary">
		</div>
	</@form>
</@htmlBody>
<@htmlFooter/>
	

