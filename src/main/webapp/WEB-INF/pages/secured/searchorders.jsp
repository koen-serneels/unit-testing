<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE HTML>
<html>
<head>
<link rel="stylesheet" type="text/css" href="<c:url  value='/resources/css/ui-lightness/jquery-ui-1.10.0.custom.min.css' />" />
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/datatables/jquery.dataTables.css' />" />
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/datatables/jquery.dataTables_themeroller.css' />" />

<script type="text/javascript" src="<c:url value='/resources/js/jquery/jquery-1.9.0.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/js/jquery/jquery-ui-1.10.0.custom.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/js/datatables/jquery.dataTables-1.9.4.min.js' />"></script>

<script type="text/javascript" src="<c:url value='/resources/js/searchorders/searchform.js' />"></script>
</head>

<body>
	<form id="searchOrdersForm">
		<table>
			<tr>
				<td><spring:message code="searchorders.product.name"/></td>
				<td><input id="productname" type="text" name="productName"/></td>
			</tr>
			<tr>
				<td><spring:message code="searchorders.product.description"/></td>
				<td><input id="productdescription" type="text" name="productDescription"/></td>
			</tr>
			<tr>
				<td><spring:message code="searchorders.order.date"/></td>
				<td><input id="orderdate" type="text" name="orderDate"/></td>
			</tr>
			<tr style="height: 15%"/>
			<tr>
				<td colspan="2">
					<input id="searchorders" type="button" value="<spring:message code="searchorders.search"/>"/>		
				</td>
		</table>
	</form>

	<div style="width: 50%; margin-top: 20px;">
		<table id="orders" style="display: none;">
			<thead>
				<tr>
					<th><spring:message code="searchorders.order.id"/></th>
					<th><spring:message code="searchorders.order.name"/></th>
					<th><spring:message code="searchorders.order.date"/></th>
				</tr>
			</thead>
		</table>
	</div>

	<script type="text/javascript">
		$('document').ready(function() {
			searchOrdersForm.init('${pageContext.request.contextPath}');
		});
	</script>
</body>
</html>