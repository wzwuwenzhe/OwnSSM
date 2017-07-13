<#assign companyName="吴先森下单系统"/>

<#macro htmlHeader title="${companyName}" isMain=false>
<@cleanHeader title=title>
    <@js path="/js/jquery-1.11.1.min.js"/>
	<@js path="/js/bootstrap.min.js"/>
	<@js path="/js/jquery.waypoints.min.js"/>
	<#if isMain>
		<@css path="/css/main/bootstrap.min.css"/>
		<@css path="/css/main/flexslider.css"/>
		<@css path="/css/main/jquery.fancybox.css"/>
		<@css path="/css/main/main.css"/>
		<@css path="/css/main/responsive.css"/>
		<@css path="/css/main/animate.min.css"/>
		<@css path="/css/main/font-icon.css"/>
		<@css path="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css" remotecss=true/>
		<@js path="/js/main/jquery.flexslider-min.js"/>
		<@js path="/js/main/jquery.fancybox.pack.js"/>
		<@js path="/js/main/retina.min.js"/>
		<@js path="/js/main/modernizr.js"/>
		<@js path="/js/main/main.js"/>
	<#else>
		<@css path="https://fonts.googleapis.com/css?family=Open+Sans:400,700,300" remotecss=true/>
		<@css path="/css/loginAndRegister/bootstrap.min.css"/>
	    <@css path="/css/loginAndRegister/animate.css"/>
		<@css path="/css/loginAndRegister/style.css"/>
		<@js path="/js/loginAndRegister/jquery.placeholder.min.js"/>
		<@js path="/js/loginAndRegister/main.js"/>
	</#if>

	
    <#nested>
</@cleanHeader>
</#macro>

<#macro htmlBody>
	<body>
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
      <div class="flexslider">
        <ul class="slides">
          <li>
          </li>
          <li>
          </li>
        </ul>
      </div>
     </div>
  </div>
</section>
</#macro>

<#macro headerBanner4Main>
	<section class="banner" role="banner">
		<header id="header">
		<div class="header-content clearfix"> <a class="logo" href="index.html"><img src="${img("/image/main/logo.png")}" alt=""></a>
		</header>
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

<#macro project name imgName>
<div class="col-lg-3 col-md-6 col-sm-6 work"> 
	<a href="${imgLocation}" class="work-box"> <img src="${img("/image/main/"+imgName)}" alt="">
		<div class="overlay">
		  <div class="overlay-caption">
		    <h5>${name}</h5>
		    <p><i class="fa fa-search-plus fa-2x"></i></p>
		  </div>
		</div>
	</a> 
</div>
</#macro>

<#macro form action class h2>
	<form action="${action}" class="${class}" data-animate-effect="fadeIn">
		<h2>${h2}</h2>
		<#nested>
	</form>
</#macro>

<#macro form_group id value type >
	<div class="form-group">
		<label for="${id}" class="sr-only">${value}</label>
		<input type="${type}" class="form-control" id="${id}" placeholder="${value}" autocomplete="off">
	</div>
</#macro>

<#macro js path>
	<#assign fix=(path?index_of("?")==-1)?string("?","&")/>
    <script type="text/javascript" src="${url(path+fix+"_stmp="+_timestamp,false)}"></script>
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
    <meta name="keywords" content="${title}" />
    <meta name="description" content="${title}" />
<#nested>
</head>
</#macro>