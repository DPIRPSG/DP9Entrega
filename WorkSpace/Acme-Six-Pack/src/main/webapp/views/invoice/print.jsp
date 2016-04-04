<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('CUSTOMER')">

<acme:exchange requestURI="invoice/customer/print.do?invoiceId=${invoice.id}"/>

	<!doctype html>
	<html>
	<head>
	    <meta charset="utf-8">
	    <title><spring:message code="invoice.yourInvoice" /></title>
	    
	    <style>
	    .invoice-box{
	        max-width:800px;
	        margin:auto;
	        padding:30px;
	        border:1px solid #eee;
	        box-shadow:0 0 10px rgba(0, 0, 0, .15);
	        font-size:16px;
	        line-height:24px;
	        font-family:'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif;
	        color:#555;
	    }
	    
	    .invoice-box table{
	        width:100%;
	        line-height:inherit;
	        text-align:left;
	    }
	    
	    .invoice-box table td{
	        padding:5px;
	        vertical-align:top;
	    }
	    
	    .invoice-box table tr td:nth-child(2){
	        text-align:right;
	    }
	    
	    .invoice-box table tr.top table td{
	        padding-bottom:20px;
	    }
	    
	    .invoice-box table tr.top table td.title{
	        font-size:45px;
	        line-height:45px;
	        color:#333;
	    }
	    
	    .invoice-box table tr.information table td{
	        padding-bottom:40px;
	    }
	    
	    .invoice-box table tr.heading td{
	        background:#eee;
	        border-bottom:1px solid #ddd;
	        font-weight:bold;
	    }
	    
	    .invoice-box table tr.details td{
	        padding-bottom:20px;
	    }
	    
	    .invoice-box table tr.item td{
	        border-bottom:1px solid #eee;
	    }
	    
	    .invoice-box table tr.item.last td{
	        border-bottom:none;
	    }
	    
	    .invoice-box table tr.total td:nth-child(2){
	        border-top:2px solid #eee;
	        font-weight:bold;
	    }
	    
	    @media only screen and (max-width: 600px) {
	        .invoice-box table tr.top table td{
	            width:100%;
	            display:block;
	            text-align:center;
	        }
	        
	        .invoice-box table tr.information table td{
	            width:100%;
	            display:block;
	            text-align:center;
	        }
	    }
	    </style>
	</head>
	
	<body>
	    <div class="invoice-box">
	        <table cellpadding="0" cellspacing="0">
	            <tr class="top">
	                <td colspan="2">
	                    <table>
	                        <tr>
	                            <td class="title">
	                                <h4>Acme Six-Pack</h4>
	                            </td>
	                            
	                            <td>
	                                <spring:message code="invoice.VAT" />: <jstl:out value="${invoice.VAT}"/><br>
	                                <spring:message code="invoice.creationMoment" />: <fmt:formatDate value="${invoice.creationMoment}" pattern="yyyy-MM-dd" /><br>
	                                <spring:message code="invoice.printMoment" />: <fmt:formatDate value="${printMoment}" pattern="yyyy-MM-dd" />
	                            </td>
	                        </tr>
	                    </table>
	                </td>
	            </tr>
	            
	            <tr class="information">
	                <td colspan="2">
	                    <table>
	                        <tr>
	                            <td>
	                               	Acme Six-Pack Co., Inc.
	                            </td>
	                            
	                            <td>
	                                <jstl:out value="${customer.name} ${customer.surname}"/>
	                            </td>
	                        </tr>
	                    </table>
	                </td>
	            </tr>
	            
	            <tr class="heading">
	                <td>
	                    <spring:message code="invoice.item" />
	                </td>
	                
	                <td>
	                    <spring:message code="invoice.price" />
	                </td>
	            </tr>
	            
	            <jstl:forEach var="feePayment" items="${invoice.feePayments}" >
			        <tr class="item">
		                <td>
		                    <jstl:out value="${feePayment.gym.name}"/> (<fmt:formatDate value="${feePayment.paymentMoment}" pattern="yyyy-MM-dd hh:mm" />)
		                </td>
		                
		                <td>
		                    <fmt:formatNumber
							value="${cookie['exchangeRate_rate'].value * feePayment.amount}"
							maxFractionDigits="2" minFractionDigits="2" /> <jstl:out value="${cookie['exchangeRate_currency'].value}"/>
		                </td>
		            </tr>
			    </jstl:forEach>
	            
	            <tr class="total">
	                <td></td>
	                
	                <td>
	                   <fmt:formatNumber
							value="${cookie['exchangeRate_rate'].value * invoice.totalCost}"
							maxFractionDigits="2" minFractionDigits="2" /> <jstl:out value="${cookie['exchangeRate_currency'].value}"/>
	                </td>
	            </tr>
	        </table>
	    </div>
	</body>
	</html>
	
	<a class="button noPrint" href='javascript:window.print(); void 0;'><spring:message code="invoice.print" /></a>
	<span class="noPrint"><acme:cancel code="invoice.print.cancel" url="/invoice/customer/list.do"/></span>
	
</security:authorize>


