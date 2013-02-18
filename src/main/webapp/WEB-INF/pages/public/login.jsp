<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE HTML>
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="<c:url value='/resources/css/testing.css' />" />
</head>
<body>
<spring:url value="/j_spring_security_check" var="login" />
<form action="${login}" method="POST">
		<div id="container">
			<div id="position">
				<div id="content">
					<table>
						<tr>
							<td><spring:message code="login.username"/></td>
							<td><input id="username" type="text" name="j_username"/></td>
						</tr>
						<tr>
							<td><spring:message code="login.password"/></td>
							<td><input id="password" type="password" name="j_password"/></td>
						</tr>
						<tr>
							<td colspan="2">
								<button id="login" type="submit"><spring:message code="login.login"/></button>
								<span style="color: red;">
									${SPRING_SECURITY_LAST_EXCEPTION.message}
								</span>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
	</form>
</body>
</html>