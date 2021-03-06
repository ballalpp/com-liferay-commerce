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

package com.liferay.commerce.product.content.web.internal.portlet.action;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.constants.CommerceWebKeys;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.product.constants.CPPortletKeys;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.permission.CommerceProductViewPermission;
import com.liferay.commerce.product.util.CPContentContributor;
import com.liferay.commerce.product.util.CPContentContributorRegistry;
import com.liferay.commerce.product.util.CPInstanceHelper;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;

import java.util.Iterator;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marco Leo
 * @author Alessio Antonio Rendina
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + CPPortletKeys.CP_CONTENT_WEB,
		"mvc.command.name=checkCPInstance"
	},
	service = MVCActionCommand.class
)
public class CheckCPInstanceMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long cpDefinitionId = ParamUtil.getLong(
			actionRequest, "cpDefinitionId");
		String ddmFormValues = ParamUtil.getString(
			actionRequest, "ddmFormValues");

		CommerceContext commerceContext =
			(CommerceContext)actionRequest.getAttribute(
				CommerceWebKeys.COMMERCE_CONTEXT);

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		CommerceAccount commerceAccount = commerceContext.getCommerceAccount();

		long commerceAccountId = 0;

		if (commerceAccount != null) {
			commerceAccountId = commerceAccount.getCommerceAccountId();
		}

		_commerceProductViewPermission.check(
			themeDisplay.getPermissionChecker(), commerceAccountId,
			commerceContext.getCommerceChannelGroupId(), cpDefinitionId);

		JSONObject jsonObject = _jsonFactory.createJSONObject();

		try {
			CPInstance cpInstance = _cpInstanceHelper.getCPInstance(
				cpDefinitionId, ddmFormValues);

			if (cpInstance != null) {
				jsonObject.put("cpInstanceExist", true);
				jsonObject.put("cpInstanceId", cpInstance.getCPInstanceId());
				jsonObject.put("gtin", cpInstance.getGtin());
				jsonObject.put(
					"manufacturerPartNumber",
					cpInstance.getManufacturerPartNumber());

				jsonObject.put("sku", cpInstance.getSku());

				List<CPContentContributor> cpContentContributors =
					_cpContentContributorRegistry.getCPContentContributors();

				for (CPContentContributor cpContentContributor :
						cpContentContributors) {

					JSONObject valueJSONObject = cpContentContributor.getValue(
						cpInstance,
						_portal.getHttpServletRequest(actionRequest));

					Iterator<String> iterator = valueJSONObject.keys();

					while (iterator.hasNext()) {
						String key = iterator.next();

						jsonObject.put(key, valueJSONObject.get(key));
					}
				}
			}
			else {
				jsonObject.put("cpInstanceExist", false);
			}

			jsonObject.put("success", true);
		}
		catch (Exception e) {
			_log.error(e, e);

			jsonObject.put("error", e.getMessage());
			jsonObject.put("success", false);
		}

		hideDefaultErrorMessage(actionRequest);
		hideDefaultSuccessMessage(actionRequest);

		writeJSON(actionResponse, jsonObject);
	}

	protected void writeJSON(ActionResponse actionResponse, Object object)
		throws IOException {

		HttpServletResponse httpServletResponse =
			_portal.getHttpServletResponse(actionResponse);

		httpServletResponse.setContentType(ContentTypes.APPLICATION_JSON);

		ServletResponseUtil.write(httpServletResponse, object.toString());

		httpServletResponse.flushBuffer();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CheckCPInstanceMVCActionCommand.class);

	@Reference
	private CommerceProductViewPermission _commerceProductViewPermission;

	@Reference
	private CPContentContributorRegistry _cpContentContributorRegistry;

	@Reference
	private CPInstanceHelper _cpInstanceHelper;

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private Portal _portal;

}