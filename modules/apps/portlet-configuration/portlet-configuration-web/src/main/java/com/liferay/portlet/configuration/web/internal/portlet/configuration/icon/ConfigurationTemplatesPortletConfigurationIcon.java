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

package com.liferay.portlet.configuration.web.internal.portlet.configuration.icon;

import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.portlet.configuration.icon.BasePortletConfigurationIcon;
import com.liferay.portal.kernel.portlet.configuration.icon.PortletConfigurationIcon;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.permission.GroupPermission;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portlet.configuration.kernel.util.PortletConfigurationApplicationType;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.WindowState;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eudaldo Alonso
 */
@Component(immediate = true, service = PortletConfigurationIcon.class)
public class ConfigurationTemplatesPortletConfigurationIcon
	extends BasePortletConfigurationIcon {

	@Override
	public String getMessage(PortletRequest portletRequest) {
		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		return _language.get(
			themeDisplay.getLocale(), "configuration-templates");
	}

	@Override
	public String getURL(
		PortletRequest portletRequest, PortletResponse portletResponse) {

		try {
			return PortletURLBuilder.create(
				PortletProviderUtil.getPortletURL(
					portletRequest,
					PortletConfigurationApplicationType.PortletConfiguration.
						CLASS_NAME,
					PortletProvider.Action.VIEW)
			).setMVCPath(
				"/edit_configuration_templates.jsp"
			).setRedirect(
				() -> {
					String redirect = ParamUtil.getString(
						portletRequest, "redirect");

					if (Validator.isNotNull(redirect)) {
						return redirect;
					}

					return null;
				}
			).setPortletResource(
				() -> {
					ThemeDisplay themeDisplay =
						(ThemeDisplay)portletRequest.getAttribute(
							WebKeys.THEME_DISPLAY);

					PortletDisplay portletDisplay =
						themeDisplay.getPortletDisplay();

					return portletDisplay.getId();
				}
			).setParameter(
				"portletConfiguration", true
			).setParameter(
				"returnToFullPageURL",
				() -> {
					String returnToFullPageURL = ParamUtil.getString(
						portletRequest, "returnToFullPageURL");

					if (Validator.isNotNull(returnToFullPageURL)) {
						return returnToFullPageURL;
					}

					return null;
				}
			).setWindowState(
				LiferayWindowState.POP_UP
			).buildString();
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}
		}

		return StringPool.BLANK;
	}

	@Override
	public double getWeight() {
		return 12.0;
	}

	@Override
	public boolean isShow(PortletRequest portletRequest) {
		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		PermissionChecker permissionChecker =
			themeDisplay.getPermissionChecker();

		try {
			if (!_groupPermission.contains(
					permissionChecker, themeDisplay.getScopeGroupId(),
					ActionKeys.MANAGE_ARCHIVED_SETUPS)) {

				return false;
			}
		}
		catch (PortalException portalException) {

			// LPS-52675

			if (_log.isDebugEnabled()) {
				_log.debug(portalException);
			}

			return false;
		}

		WindowState windowState = portletRequest.getWindowState();

		if (windowState.equals(LiferayWindowState.EXCLUSIVE)) {
			return false;
		}

		Layout layout = themeDisplay.getLayout();

		if (layout.isTypeControlPanel() ||
			isEmbeddedPersonalApplicationLayout(layout)) {

			return false;
		}

		PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();

		return portletDisplay.isShowConfigurationIcon();
	}

	@Override
	public boolean isShowInEditMode(PortletRequest portletRequest) {
		return true;
	}

	@Override
	public boolean isUseDialog() {
		return true;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ConfigurationTemplatesPortletConfigurationIcon.class);

	@Reference
	private GroupPermission _groupPermission;

	@Reference
	private Language _language;

}