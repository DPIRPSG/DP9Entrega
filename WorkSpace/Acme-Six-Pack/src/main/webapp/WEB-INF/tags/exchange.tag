<%--
 * textbox.tag
 *
 * Copyright (C) 2014 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@ tag language="java" body-content="empty" %>

<%-- Taglibs --%>

<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<%-- Attributes --%> 
 
<%@ attribute name="requestURI" required="true" %>

<%-- Definition --%>
<jstl:set var="toEvaluate" value="${cookie.containsKey('exchangeRate_id') && cookie.containsKey('exchangeRate_all')}" />	

<jstl:if test="${toEvaluate=='false'}">
	<spring:message code="exchangeRate.errorLoad" /> <a href="exchangeRate/load.do?requestURI=${requestURI}"> <spring:message
				code="exchangeRate.here" /></a>
</jstl:if>

<jstl:if test="${toEvaluate=='true'}">
<div>
<form action="exchangeRate/edit.do">
	<input type="hidden" name="requestURI" value="${requestURI}"/>

	<jstl:set var="exchangeSplit" value="${fn:split(cookie['exchangeRate_all'].value, '|')}" />	
	
	<select name="exchangeRateId">
		<jstl:forEach var="exchangeI" items="${exchangeSplit}">
			<jstl:set var="exchangeSplitI" value="${fn:split(exchangeI, ',')}" />
			<jstl:if test="${exchangeSplitI[0] == cookie['exchangeRate_id'].value}">
				<option value="${exchangeSplitI[0]}" selected="selected">${exchangeSplitI[3]}</option>
			</jstl:if>
			<jstl:if test="${exchangeSplitI[0] != cookie['exchangeRate_id'].value}">
				<option value="${exchangeSplitI[0]}">${exchangeSplitI[3]}</option>
			</jstl:if>
		</jstl:forEach>	
	</select>
	 
	<input type="submit" value="<spring:message code="exchangeRate.apply" />" />&nbsp;
</form>

<spring:message code="exchangeRate.exchangeRate" var="message"/>
<jstl:out value="${message}: ${cookie['exchangeRate_name'].value} [${cookie['exchangeRate_currency'].value}]"/>

</div>
</jstl:if>



