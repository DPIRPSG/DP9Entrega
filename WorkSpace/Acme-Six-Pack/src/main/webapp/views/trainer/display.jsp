<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

	<!-- Listing grid -->
	<div>
	<table>

	<acme:display code="trainer.name" value="${trainer.name}"/>
	<acme:display code="trainer.surname" value="${trainer.surname}"/>
	<acme:display code="trainer.phone" value="${trainer.phone}"/>
	<acme:display code="trainer.username" value="${trainer.userAccount.username}"/>
	<tr>
		<th><spring:message code="trainer.picture" /> :</th>
		<td><img src="${trainer.picture}" style="width:204px;"/></td>
	</tr>
	
	</table>
	</div>

	
	<!-- Action links -->
	<div>
		<b><a href="trainer/trainer/edit.do"> 
			<spring:message code="trainer.edit" />
		</a></b>
	</div>
	<br/>
