<#function url relativeUrl addTimestamp=false>
    <#assign ret=contextUrl(relativeUrl)/>
    <#if addTimestamp>
        <#assign ts=.now?string("yyyyMMddHHmmss")/>
        <#assign fix=(ret?index_of("?")==-1)?string("?","&")/>
        <#assign ret=ret+fix+"_stmp="+ts>s
    </#if>
    <#return ret/>
</#function>

<#function contextUrl relativeUrl extra...>
    <#if extra?? && extra?size!=0>
        <#return springMacroRequestContext.getContextUrl(relativeUrl,extra)/>
    <#else>
        <#return springMacroRequestContext.getContextUrl(relativeUrl)/>
    </#if>
</#function>



<#function img imgUrl>  
    <#return url(imgUrl,false)/>
</#function>