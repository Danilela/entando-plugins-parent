<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<h1><a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />"><s:text name="title.pageManagement" /></a></h1>

<div id="main">
<h2><s:text name="title.configPage" /></h2>

<s:set var="breadcrumbs_pivotPageCode" value="pageCode" />
<s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />

<div class="subsection-light">
<h3><s:text name="title.configPage.youAreDoing" /></h3>

<s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true"><s:param name="selectedNode" value="pageCode"></s:param></s:action>
<s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />

<h3 class="margin-more-top margin-more-bottom"><s:text name="name.showlet" />:&#32;<s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}" /></h3>

<s:form action="saveConfig" namespace="/do/jpphotogallery/Page/SpecialShowlet/Photogallery">
<p class="noscreen">
	<wpsf:hidden name="pageCode" />
	<wpsf:hidden name="frame" />
	<wpsf:hidden name="showletTypeCode" value="%{showlet.type.code}" />
</p>

	<s:if test="hasFieldErrors()">
<div class="message message_error">
<h4><s:text name="message.title.FieldErrors" /></h4>	
	<ul>
	<s:iterator value="fieldErrors">
		<s:iterator value="value">
		<li><s:property escape="false" /></li>
		</s:iterator>
	</s:iterator>
	</ul>
</div>
	</s:if>

<s:if test="showlet.config['contentType'] == null">
<%-- SELEZIONE DEL TIPO DI CONTENUTO --%>
<fieldset><legend><s:text name="title.contentInfo" /></legend>
<p>
	<label for="contentType" class="basic-mint-label"><s:text name="label.type"/>:</label>
	<wpsf:select useTabindexAutoIncrement="true" name="contentType" id="contentType" list="contentTypes" listKey="code" listValue="descr" cssClass="text" />
	<wpsf:submit useTabindexAutoIncrement="true" action="configShowlet" value="%{getText('label.continue')}" cssClass="button" />	
</p>
</fieldset>
</s:if>
<s:else>
<fieldset class="margin-bit-bottom"><legend><s:text name="title.contentInfo" /></legend>
<p>
	<label for="contentType" class="basic-mint-label"><s:text name="label.type"/>:</label>
	<wpsf:select useTabindexAutoIncrement="true" name="contentType" id="contentType" list="contentTypes" listKey="code" listValue="descr" disabled="true" value="%{getShowlet().getConfig().get('contentType')}" cssClass="text" />
	<wpsf:submit useTabindexAutoIncrement="true" action="changeContentType" value="%{getText('label.change')}" cssClass="button" />	
</p>
<p class="noscreen">
	<wpsf:hidden name="contentType" value="%{getShowlet().getConfig().get('contentType')}" />
</p>	
<p>
	<label for="category" class="basic-mint-label"><s:text name="label.category" />:</label>
	<wpsf:select useTabindexAutoIncrement="true" name="category" id="category" list="categories" listKey="code" listValue="getShortFullTitle(currentLang.code)" headerKey="" headerValue="%{getText('label.all')}" value="%{getShowlet().getConfig().get('category')}" cssClass="text" />
</p>
</fieldset>

<s:if test="null != filtersProperties && filtersProperties.size()>0" >
<table class="generic margin-bit-top" summary="<s:text name="note.page.contentListViewer.summary" />">
<caption><span><s:text name="title.filterOptions" /></span></caption>
<tr>
	<th><abbr title="<s:text name="label.number" />">N</abbr></th>
	<th><s:text name="name.filterDescription" /></th>
	<th><s:text name="label.order" /></th>
	<th class="icon" colspan="3"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th> 
</tr>
<s:iterator value="filtersProperties" id="filter" status="rowstatus">
<tr>
	<td class="rightText"><s:property value="#rowstatus.index+1"/></td>
	<td>
		<s:text name="label.filterBy" /><strong>
			<s:if test="#filter['key'] == 'created'">
				<s:text name="label.creationDate" />
			</s:if>
			<s:elseif test="#filter['key'] == 'modified'">
				<s:text name="label.lastModifyDate" />			
			</s:elseif>
			<s:else>
				<s:property value="#filter['key']" />
			</s:else>
		</strong><s:if test="(#filter['start'] != null) || (#filter['end'] != null) || (#filter['value'] != null)">,
		<s:if test="#filter['start'] != null">
			<s:text name="label.filterFrom" /><strong>
				<s:if test="#filter['start'] == 'today'">
					<s:text name="label.today" />				
				</s:if>
				<s:else>
					<s:property value="#filter['start']" />
				</s:else>
			</strong>
		</s:if>
		<s:if test="#filter['end'] != null">
			<s:text name="label.filterTo" /><strong>
				<s:if test="#filter['end'] == 'today'">
					<s:text name="label.today" />				
				</s:if>
				<s:else>
					<s:property value="#filter['end']" />
				</s:else>
			</strong>
		</s:if>
		<s:if test="#filter['value'] != null">
			<s:text name="label.filterValue" />:<strong> <s:property value="#filter['value']" /></strong>
				<s:if test="#filter['likeOption'] == 'true'">
					<em>(<s:text name="label.filterValue.isLike" />)</em>
				</s:if>
		</s:if>
		</s:if>
	</td>
	<td>
	<s:if test="#filter['order'] == 'ASC'"><s:text name="label.order.ascendant" /></s:if>
	<s:if test="#filter['order'] == 'DESC'"><s:text name="label.order.descendant" /></s:if>
	</td>
	<td class="icon">
		<wpsa:actionParam action="moveFilter" var="actionName" >
			<wpsa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
			<wpsa:actionSubParam name="movement" value="UP" />
		</wpsa:actionParam>
		<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/go-up.png</s:set>		
		<wpsf:submit useTabindexAutoIncrement="true" action="%{#actionName}" type="image" src="%{#iconImagePath}" value="%{getText('label.moveUp')}" title="%{getText('label.moveUp')}" />
	</td>
	<td class="icon">	
		<wpsa:actionParam action="moveFilter" var="actionName" >
			<wpsa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
			<wpsa:actionSubParam name="movement" value="DOWN" />
		</wpsa:actionParam>
		<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/go-down.png</s:set>
		<wpsf:submit useTabindexAutoIncrement="true" action="%{#actionName}" type="image" src="%{#iconImagePath}" value="%{getText('label.moveDown')}" title="%{getText('label.moveDown')}" />
	</td>
	<td class="icon">	
		<wpsa:actionParam action="removeFilter" var="actionName" >
			<wpsa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
		</wpsa:actionParam>
		<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/delete.png</s:set>
		<wpsf:submit useTabindexAutoIncrement="true" action="%{#actionName}" type="image"  src="%{#iconImagePath}" value="%{getText('label.remove')}" title="%{getText('label.remove')}" />
	</td>	
</tr>
</s:iterator>
</table>
</s:if>
<s:else>
	<p><span class="important"><s:text name="title.filterOptions" />:</span> <s:text name="note.filters.none" /></p>		
</s:else>

<p class="margin-more-bottom"><wpsf:submit useTabindexAutoIncrement="true" action="newFilter" value="%{getText('label.add')}" cssClass="button" /></p>
<p class="noscreen">
	<wpsf:hidden name="filters" value="%{getShowlet().getConfig().get('filters')}" />
</p>

<fieldset><legend><s:text name="title.publishingOptions" /></legend>
<p>
	<label for="modelIdMaster"><s:text name="label.contentModelMaster" />:</label><br />
	<wpsf:select useTabindexAutoIncrement="true" name="modelIdMaster" id="modelIdMaster" value="%{getShowlet().getConfig().get('modelIdMaster')}" 
		list="%{getModelsForContentType(showlet.config['contentType'])}" headerKey="" headerValue="%{getText('label.default')}" listKey="id" listValue="description" cssClass="text" />
</p>
<p>
	<label for="modelIdPreview"><s:text name="label.contentModelPreview" />:</label><br />
	<wpsf:select useTabindexAutoIncrement="true" name="modelIdPreview" id="modelIdPreview" value="%{getShowlet().getConfig().get('modelIdPreview')}" 
		list="%{getModelsForContentType(showlet.config['contentType'])}" headerKey="" headerValue="%{getText('label.default')}" listKey="id" listValue="description" cssClass="text" />
</p>
</fieldset>

<p class="centerText"><wpsf:submit useTabindexAutoIncrement="true" action="saveConfig" value="%{getText('label.save')}" cssClass="button" /></p>

</s:else>

</s:form>

</div>
</div>