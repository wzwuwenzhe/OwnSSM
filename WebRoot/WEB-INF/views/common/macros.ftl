<#assign companyName="吴先森下单系统"/>
<#assign userType='${userType}'/>

<#macro htmlHeader4Main title="${companyName}">
<@cleanHeader4Main title=title>
	<@css path="/css/main/bootstrap.min.css"/>
	<@css path="/css/main/flexslider.css"/>
	<@css path="/css/main/jquery.fancybox.css"/>
	<@css path="/css/main/main.css"/>
	<@css path="/css/main/responsive.css"/>
	<@css path="/css/main/animate.min.css"/>
	<@css path="/css/main/font-icon.css"/>
	<@css path="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css" remotecss=true/>

	<@js path="/js/loginAndRegister/jquery-1.11.1.min.js"/>
	<@js path="/js/main/bootstrap.min.js"/>
	<@js path="/js/main/jquery.flexslider-min.js"/>
	<@js path="/js/main/jquery.fancybox.pack.js"/>
	<@js path="/js/main/jquery.waypoints.min.js"/>
	<@js path="/js/main/modernizr.js"/>
	<@js path="/js/main/main.js"/>
	<@js path="/js/validator.js"/>
	<@js path="/js/layer/layer.js"/>
	<@js path="/js/layer/extend/layer.ext.js"/>
	<@js path="/js/common.js"/>
	<@js path="/js/jquery.form.js"/>
</@cleanHeader4Main>
</#macro>

<#macro htmlHeader title="${companyName}" >
<@cleanHeader title=title>
	<@css path="https://fonts.googleapis.com/css?family=Open+Sans:400,700,300" remotecss=true/>
	<@css path="/css/loginAndRegister/bootstrap.min.css"/>
    <@css path="/css/loginAndRegister/animate.css"/>
	<@css path="/css/loginAndRegister/style.css"/>
	<@js path="/js/loginAndRegister/jquery-1.11.1.min.js"/>
	<@js path="/js/loginAndRegister/jquery.placeholder.min.js"/>
	<@js path="/js/loginAndRegister/main.js"/>
	<@js path="/js/loginAndRegister/bootstrap.min.js"/>
	<@js path="/js/loginAndRegister/jquery.waypoints.min.js"/>
	<@js path="/js/validator.js"/>
	<@js path="/js/layer/layer.js"/>
	<@js path="/js/layer/extend/layer.ext.js"/>
	<@js path="/js/common.js"/>
	<@js path="/js/jquery.form.js"/>
	<@js path="/js/md5.js"/>
    <#nested>
</@cleanHeader>
</#macro>



<#macro htmlHeader4Order title="${companyName}" >
<!doctype html>
<html lang="zh">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> 
<meta name="viewport" content="width=device-width, initial-scale=1.0 maximum-scale=1.0, user-scalable=no"/>
<title>${title}</title>

<@css path="/js/layui/css/layui.css"/>
<@css path="/js/My97DatePicker/skin/WdatePicker.css"/>
<@js path="/js/loginAndRegister/jquery-1.11.1.min.js"/>
<@js path="/js/jquery.basictable.min.js"/>
<@js path="/js/My97DatePicker/WdatePicker.js"/>
<@js path="/js/layui/layui.js"/>
<style type="text/css">
table,table tr th, table tr td { border:1px solid #000000; }
</style>
</head>
<#nested>
</html>
</#macro>


<#assign _today=jodaTime.now().toString("yyyyMMdd")/>
<#macro DateFields begin=_today end=_today beginValue="" endValue="">
    <input type="text" <#if beginValue!="">value="${beginValue}"<#else>value="${begin}"</#if> class="Wdate" name="beginDate" id="beginDate" style="cursor:pointer;width:120px;" onclick="WdatePicker()" readonly="readonly" dataType="Require" msg="开始日期不能为空"/>
    </span>至</span>
    <input type="text" <#if beginValue!="">value="${endValue}"<#else>value="${end}"</#if> class="Wdate" name="endDate" id="endDate" style="cursor:pointer;width:120px;" onclick="WdatePicker()" readonly="readonly" dataType="Require" msg="结束日期不能为空"/>
</#macro>

<#macro htmlBody>
	<body id="billingBody">
		<div class="container">
			<div class="row">
				<div class="col-md-4 col-md-offset-4">
				<#nested>
				</div>
			</div>
		</div>
</#macro>

<#macro htmlFooter>
	<div class="row" style="padding-top: 60px; clear: both;">
		<div class="col-md-12 text-center"><p><small>&copy; 吴先森 造</small></p></div>
	</div>
	</body>
	</html>
</#macro>

<#macro htmlFooter4Main>
	<footer class="section footer">
	  <div class="footer-bottom">
	    <div class="container">
	      <div class="col-md-12">
	        <p></p>
	        <p>© 吴先森 造<br>
	      </div>
	    </div>
	  </div>
	</footer>
	</body>
	</html>
</#macro>

<#macro banner4Main>
<section id="banner" class="banner no-padding">
  <div class="container-fluid">
    <div class="row no-gutter">
      <!-- <div class="flexslider"> -->
        <ul class="slides">
          <li>
          </li>
          <li>
          </li>
        </ul>
      <!-- </div> -->
     </div>
  </div>
</section>
</#macro>

<#macro headerBanner4Main>
	<section class="banner" role="banner">
		<!--
			<header id="header">
			<div class="header-content clearfix"> <a class="logo" href="index.html"><img src="${img("/image/main/logo.png")}" alt=""></a>
			</header>
		-->
  	</section>
</#macro>

<#macro htmlBody4Main>
	<body>
	<section id="works" class="works section no-padding">
	  <div class="container-fluid">
	    <div class="row no-gutter">
		<#nested>        
	    </div>
	  </div>
	</section>
</#macro>

<#macro project name imgName href>
<div class="col-lg-3 col-md-6 col-sm-6 work"> 
	<a href="${url(href)}" class="work-box"> <img src="${img("/image/main/"+imgName)}" alt="">
		<h5>${name}</h5>
	</a> 
</div>
</#macro>

<#macro form  action class h2 id="_form" onsubmit="return $form.submit(this);" enctype="">
	<form id="${id}" accept-charset="UTF-8" <#if enctype!="">enctype="${enctype}"</#if> action="${url("${action}")}" onsubmit="${onsubmit}" method="post" class="${class}" data-animate-effect="fadeIn">
		<h2>${h2}</h2>
		<input type="hidden" name="_token" value="${_token}"/>
		<#nested>
	</form>
</#macro>

<#macro uploadImg id desc value="">
	<div class="uploadImgDiv">
		<input id="${id}" type="checkbox" <#if value!="">checked="checked"</#if> onclick="showUpload(this)" />
		<label for="${id}">${desc}</label>
		<input type="hidden" name="${id}" id="${id}FilePath"  <#if value!="">value="${value}"</#if> />
		<label class="uploadSuccess" style="display:none"><img src="${url("/image/complete.png")}"/></label>
		<input type="file" id="${id}File" name="file" style="display:none" onchange="uploadFile(this)"/>
		<div class="viewImg">
			<#if value!="">
				<img src="${value}?_stmp=${.now?string("yyyyMMddHHmmss")}" onclick="$(this).parent().prev().click();"/>
			</#if>
		</div>
	</div>
</#macro>

<#macro form_group id desc type name dataType msg  onkeyup="" value="" style="" >
	<div class="form-group" <#if style!="">style="${style}"</#if> >
		<label for="${id}" class="sr-only">${desc}</label>
		<input type="${type}" <#if value!="">value="${value}"</#if> <#if onkeyup!=""> onkeyup="${onkeyup}"</#if>
		dataType="${dataType}" name="${name}" msg="${msg}" class="form-control" id="${id}" placeholder="${desc}" autocomplete="off">
	</div>
</#macro>

<#macro form_input id name desc type dataType msg onkeyup="" value="" style="">
	<div class="layui-form-item">
	   <label class="layui-form-label">${desc}</label>
	   <div class="layui-input-block">
	   	 <input class="layui-input"  id="${id}" type="${type}" <#if value!="">value="${value}"</#if> <#if onkeyup!=""> onkeyup="${onkeyup}"</#if>
		dataType="${dataType}" name="${name}" msg="${msg}"  id="${id}" placeholder="${desc}" autocomplete="off" style="${style}">
	   </div>
	 </div>
</#macro>

<#macro js path remotecss=false>
	<#if remotecss>
		<script type="text/javascript" src="${path}"></script>
	<#else>
		<#assign fix=(path?index_of("?")==-1)?string("?","&")/>
	    <script type="text/javascript" src="${url(path+fix+"_stmp="+_timestamp,false)}"></script>
	</#if>
</#macro>

<#macro css path remotecss=false>
	<#if remotecss>
		<link type="text/css" rel="stylesheet" href="${path}"/>
	<#else>
		<#assign fix=(path?index_of("?")==-1)?string("?","&")/>
	    <link type="text/css" rel="stylesheet" href="${url(path+fix+"_stmp="+_timestamp,false)}"/>
	</#if>
	
</#macro>
<#macro cleanHeader title="${companyName}">
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">    
    <title>${title}</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="keywords" content="${title}" />
    <meta name="description" content="${title}" />
<#nested>
</head>
</#macro>

<#macro cleanHeader4Main title="${companyName}">
	<!doctype html>
	<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang=""> <![endif]-->
	<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8" lang=""> <![endif]-->
	<!--[if IE 8]>         <html class="no-js lt-ie9" lang=""> <![endif]-->
	<!--[if gt IE 8]><!-->
	<html class="no-js" lang="">
	<!--<![endif]-->
	<head>
	<meta charset="utf-8">
	<meta name="description" content="">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>${title}</title>
	<#nested>
	</head>
</#macro>

<#macro radio id name desc value   dataType="Number">
	<input type="radio" id="${id}" dataType="${dataType}"  name="${name}" value="${value}" />
	<label for="${id}">${desc}</label>
</#macro>

<#macro radioGroup name datas text="" lazyCheck=false>
<div class="form-group">
	<p>${text}</p>
    <#assign keys=datas?keys>
    <#list keys as key><label><input type="radio" name="${name}" value="${key}"<#if key_index==0 && !lazyCheck> dataType="Group"</#if>  msg="必须选择${text}"/>${datas[key]}</label></#list>
</div>
</#macro>

<#macro showMsg type="danger">
	<div class="form-group" >
		<div class="alert alert-${type}" role="alert" style="display:none"></div>
	</div>
</#macro>
