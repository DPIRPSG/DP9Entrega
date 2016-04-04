<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<acme:exchange requestURI="${requestURI}"/>

<br/>

<security:authorize access="hasRole('CUSTOMER')">
	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag" keepStatus="false"
		name="invoices" requestURI="${requestURI}" id="row_invoice">
		
		<!-- Action links -->
		<display:column>
			<acme:link href="invoice/customer/print.do?invoiceId=${row_invoice.id}" code="invoice.print"/>
		</display:column>
		
		
		<!-- Attributes -->
		<spring:message code="invoice.invoiceesName" var="invoiceesNameHeader" />
		<acme:displayColumn title="${invoiceesNameHeader}" sorteable="true" value="${row_invoice.invoiceesName}"/>
		
		<spring:message code="invoice.VAT" var="VATHeader" />
		<acme:displayColumn title="${VATHeader}" sorteable="true" value="${row_invoice.VAT}"/>
	
		<spring:message code="invoice.creationMoment" var="creationMomentHeader" />
		<acme:displayColumn title="${creationMomentHeader}" sorteable="true" value="${row_invoice.creationMoment}" format="{0,date,yyyy/MM/dd}"/>

		<spring:message code="invoice.totalCost" var="totalCostHeader" />
		<display:column title="${totalCostHeader}" sortable="true">
			<fmt:formatNumber
				value="${cookie['exchangeRate_rate'].value * row_invoice.totalCost}"
				maxFractionDigits="2" minFractionDigits="2" />
		</display:column>

		<spring:message code="invoice.description" var="descriptionHeader" />
		<acme:displayColumn title="${descriptionHeader}" sorteable="false" value="${row_invoice.description}"/>
			
	</display:table>
		
	<div>
		<acme:link href="invoice/customer/create.do" code="invoice.create"/>
	</div>
	
</security:authorize>


