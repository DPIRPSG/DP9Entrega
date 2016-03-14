<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('CUSTOMER')">

	<!-- Form -->
	<form:form action="invoice/customer/draft.do" modelAttribute="invoice">
		<!-- Hidden Attributes -->
		<form:hidden path="id"/>
		<form:hidden path="version"/>
		
		<form:hidden path="creationMoment"/>
		<form:hidden path="feePayments"/>
		
		<!-- Editable Attributes -->
		
		<acme:textbox code="invoice.invoiceesName" path="invoiceesName" readonly="true"/>
		<acme:textbox code="invoice.VAT" path="VAT" readonly="true"/>
		<acme:textbox code="invoice.totalCost" path="totalCost" readonly="true"/>
		<acme:textarea code="invoice.description" path="description" readonly="true"/>
		
		<!-- Action buttons -->
		<acme:submit name="save" code="invoice.draft.confirm"/>
		&nbsp;
		<acme:submit name="cancel" code="invoice.draft.cancel"/>
<%-- 		<acme:cancel code="invoice.draft.cancel" url="/invoice/customer/create.do?invoiceId=${invoice.id}"/> --%>
		
	</form:form>
	
</security:authorize>


