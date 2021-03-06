<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %><%@
taglib uri="http://liferay.com/tld/clay" prefix="clay" %><%@
taglib uri="http://liferay.com/tld/frontend" prefix="liferay-frontend" %><%@
taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %><%@
taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<%@ page import="com.liferay.commerce.application.admin.web.internal.display.context.CommerceApplicationAdminDisplayContext" %><%@
page import="com.liferay.commerce.application.admin.web.internal.servlet.taglib.ui.CommerceApplicationBrandScreenNavigationConstants" %><%@
page import="com.liferay.commerce.application.constants.CommerceApplicationActionKeys" %><%@
page import="com.liferay.commerce.application.exception.NoSuchApplicationBrandException" %><%@
page import="com.liferay.commerce.application.exception.NoSuchApplicationModelException" %><%@
page import="com.liferay.commerce.application.model.CommerceApplicationBrand" %><%@
page import="com.liferay.commerce.application.model.CommerceApplicationModel" %><%@
page import="com.liferay.commerce.bom.model.CommerceBOMFolderApplicationRel" %><%@
page import="com.liferay.commerce.product.util.CPNavigationItemRegistryUtil" %><%@
page import="com.liferay.petra.string.StringPool" %><%@
page import="com.liferay.portal.kernel.language.LanguageUtil" %><%@
page import="com.liferay.portal.kernel.security.permission.ActionKeys" %><%@
page import="com.liferay.portal.kernel.util.Constants" %><%@
page import="com.liferay.portal.kernel.util.ParamUtil" %><%@
page import="com.liferay.portal.kernel.util.WebKeys" %><%@
page import="com.liferay.portal.kernel.webserver.WebServerServletTokenUtil" %><%@
page import="com.liferay.taglib.search.ResultRow" %><%@
page import="com.liferay.users.admin.configuration.UserFileUploadsConfiguration" %>

<%@ page import="javax.portlet.PortletURL" %>

<liferay-frontend:defineObjects />

<liferay-theme:defineObjects />

<portlet:defineObjects />

<%
String redirect = ParamUtil.getString(request, "redirect");

String backURL = ParamUtil.getString(request, "backURL", redirect);
%>