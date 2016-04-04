<%--
 * header.jsp
 *
 * Copyright (C) 2014 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<div class="noPrint">
<div>
	<a href=""> 
		<img src="images/logo.png" style="height:128px;" alt="ACME, Inc.  Your gym Company" /></a>
	<br/>

</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		<security:authorize access="hasRole('CUSTOMER')">
			<li><a class="fNiv"><spring:message	code="master.page.manage.gym" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="gym/customer/list-feepayments-not-active.do"><spring:message code="master.page.manage.gym.not-pay" /></a></li>
					<li><a href="gym/customer/list-feepayments-active.do"><spring:message code="master.page.manage.gym.pay" /></a></li>
				</ul>
			</li>
			<li><a class="fNiv" href="service/customer/list.do"><spring:message code="master.page.service" /></a></li>
			<li><a class="fNiv" href="trainer/list.do"><spring:message code="master.page.trainer" /></a></li>
			<li><a class="fNiv" href="feePayment/customer/list.do"><spring:message code="master.page.feePayments" /></a></li>
			<li><a class="fNiv" href="invoice/customer/list.do"><spring:message code="master.page.invoice" /></a></li>
			<li><a class="fNiv" href="activity/customer/list.do"><spring:message code="master.page.activity" /></a>	</li>	
		</security:authorize>
		
		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv"><spring:message	code="master.page.manage" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="feePayment/administrator/list.do"><spring:message code="master.page.list.feePayments" /></a></li>
					<li><a href="gym/administrator/list.do"><spring:message code="master.page.manage.gym" /></a></li>
					<li><a href="service/administrator/list.do"><spring:message code="master.page.manage.service" /></a></li>
					<li><a href="activity/administrator/list.do"><spring:message code="master.page.manage.activity" /></a></li>
					<li><a href="spamTerm/administrator/list.do"><spring:message code="master.page.manage.spamTerms" /></a></li>
					<li><a href="exchangeRate/administrator/list.do"><spring:message code="master.page.exchangeRate" /></a></li>
					<li><a href="trainer/administrator/register.do"><spring:message code="master.page.register.trainer" /></a></li>
				</ul>
			</li>
			<li><a class="fNiv"><spring:message	code="master.page.list" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="trainer/list.do"><spring:message code="master.page.trainer" /></a></li>
				</ul>
			</li>
			<li><a class="fNiv" href="dashboard/administrator/list.do"><spring:message code="master.page.dashboard" /></a></li>
		</security:authorize>
		
		<security:authorize access="hasRole('TRAINER')">
			<li><a class="fNiv"><spring:message	code="master.page.manage" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="service/trainer/specialised.do"><spring:message code="master.page.manage.service" /></a></li>
					<li><a href="curriculum/trainer/list.do"><spring:message code="master.page.manage.curriculum" /></a></li>
				</ul>
			</li>
			<li><a class="fNiv" href="trainer/list.do"><spring:message code="master.page.trainer" /></a></li>
			<li><a class="fNiv" href="gym/list.do"><spring:message code="master.page.gym" /></a></li>
			
		</security:authorize>
				
		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="gym/list.do"><spring:message code="master.page.gym" /></a></li>
			<li><a class="fNiv" href="service/list.do"><spring:message code="master.page.service" /></a></li>
			<li><a class="fNiv" href="trainer/list.do"><spring:message code="master.page.trainer" /></a></li>
			<li><a class="fNiv" href="customer/create.do"><spring:message code="master.page.register" /></a></li>
			<li><a class="fNiv" href="security/login.do"><spring:message code="master.page.login" /></a></li>
		</security:authorize>
		
		<security:authorize access="isAuthenticated()">
			<li><a class="fNiv" href="folder/actor/list.do"><spring:message code="master.page.messages" /></a></li>
		
			<!-- <li><a href="j_spring_security_logout"><spring:message code="master.page.logout" />(<security:authentication property="principal.username" />)</a></li> -->
			<li><a class="fNiv"><security:authentication property="principal.username" /></a>
				<ul>
					<li class="arrow"></li>
					<security:authorize access="hasRole('CUSTOMER')">
						<li><a href="customer/customer/display.do"><spring:message code="master.page.customer.info" /></a></li>
					</security:authorize>
					<security:authorize access="hasRole('ADMIN')">
						<li><a href="admin/administrator/display.do"><spring:message code="master.page.customer.info" /></a></li>
					</security:authorize>	
					<security:authorize access="hasRole('TRAINER')">
						<li><a href="trainer/trainer/display.do"><spring:message code="master.page.customer.info" /></a></li>
					</security:authorize>					
					<li><b><a href="j_spring_security_logout"><spring:message code="master.page.logout" /></a></b></li>
				</ul>
			</li>
		</security:authorize>
	</ul>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>

<!-- OJO !!!! El script de cancel.tag está en el footer!!!! -->

</div>


