<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<security:authorize access = "hasRole('CUSTOMER')">

	<!-- Form -->
	<form:form action="invoice/customer/create.do" modelAttribute="invoiceForm">
		<!-- Hidden Attributes -->
		
		<!-- Editable Attributes -->
		
		<acme:textbox code="invoiceForm.invoiceesName" path="invoiceesName" />
		<acme:textbox code="invoiceForm.VAT" path="VAT" />
		<acme:textarea code="invoiceForm.description" path="description" />
		
		<form:label path="feePaymentsNotIssued">
			<spring:message code="invoiceForm.feePaymentsNotIssued" />
		</form:label>
		<form:select name="feePaymentsNotIssued" path="feePaymentsNotIssued" multiple="multiple">
<!-- 		    <option value="">---</option> -->
		    <jstl:forEach var="feePayment" items="${invoiceForm.feePaymentsNotIssued}" >
		        <form:option value="${feePayment.id}"><jstl:out value="${feePayment.gym.name} - ${feePayment.amount} (${feePayment.paymentMoment})"/></form:option>
		    </jstl:forEach>
		</form:select>
		<form:errors path="feePaymentsNotIssued" cssClass="error" />
		<br />
		<br />
		
		<!-- Action buttons -->
		<acme:submit name="save" code="invoice.create.save"/>
		&nbsp;
		<acme:cancel code="invoice.create.cancel" url="/invoice/customer/list.do"/>
		<br />
	
	</form:form>
		
</security:authorize>
