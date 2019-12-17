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

package com.liferay.layout.admin.web.internal.display.context;

import com.liferay.document.library.kernel.service.DLAppService;
import com.liferay.document.library.util.DLURLHelper;
import com.liferay.dynamic.data.mapping.exception.StorageException;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.service.DDMStructureServiceUtil;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.storage.StorageEngine;
import com.liferay.item.selector.ItemSelector;
import com.liferay.item.selector.ItemSelectorCriterion;
import com.liferay.item.selector.criteria.FileEntryItemSelectorReturnType;
import com.liferay.item.selector.criteria.URLItemSelectorReturnType;
import com.liferay.item.selector.criteria.image.criterion.ImageItemSelectorCriterion;
import com.liferay.layout.admin.web.internal.constants.LayoutAdminWebKeys;
import com.liferay.layout.seo.canonical.url.LayoutSEOCanonicalURLProvider;
import com.liferay.layout.seo.kernel.LayoutSEOLinkManager;
import com.liferay.layout.seo.model.LayoutSEOEntry;
import com.liferay.layout.seo.model.LayoutSEOSite;
import com.liferay.layout.seo.service.LayoutSEOEntryLocalServiceUtil;
import com.liferay.layout.seo.service.LayoutSEOSiteLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactoryUtil;
import com.liferay.portal.kernel.service.ClassNameLocalServiceUtil;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ListMergeable;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portlet.layoutsadmin.display.context.GroupDisplayContextHelper;

import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Alicia García
 */
public class LayoutsSEODisplayContext {

	public LayoutsSEODisplayContext(
		DLAppService dlAppService, DLURLHelper dlurlHelper,
		LayoutSEOCanonicalURLProvider layoutSEOCanonicalURLProvider,
		LayoutSEOLinkManager layoutSEOLinkManager,
		LayoutSEOSiteLocalService layoutSEOSiteLocalService,
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse,
		StorageEngine storageEngine) {

		_dlAppService = dlAppService;
		_dlurlHelper = dlurlHelper;
		_layoutSEOCanonicalURLProvider = layoutSEOCanonicalURLProvider;
		_layoutSEOLinkManager = layoutSEOLinkManager;
		_layoutSEOSiteLocalService = layoutSEOSiteLocalService;
		_liferayPortletRequest = liferayPortletRequest;
		_liferayPortletResponse = liferayPortletResponse;
		_storageEngine = storageEngine;

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(_liferayPortletRequest);

		_groupDisplayContextHelper = new GroupDisplayContextHelper(
			httpServletRequest);

		_httpServletRequest = httpServletRequest;

		_itemSelector = (ItemSelector)liferayPortletRequest.getAttribute(
			LayoutAdminWebKeys.ITEM_SELECTOR);
		_themeDisplay = (ThemeDisplay)liferayPortletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	public Map<Locale, String> getCanonicalLayoutURLMap()
		throws PortalException {

		return _layoutSEOCanonicalURLProvider.getCanonicalURLMap(
			_selLayout, _themeDisplay);
	}

	public DDMFormValues getDDMFormValues() throws StorageException {
		LayoutSEOEntry selLayoutSEOEntry = getSelLayoutSEOEntry();

		if ((selLayoutSEOEntry == null) ||
			(selLayoutSEOEntry.getDDMStorageId() == 0)) {

			return null;
		}

		return _storageEngine.getDDMFormValues(
			selLayoutSEOEntry.getDDMStorageId());
	}

	public long getDDMStructurePrimaryKey() throws PortalException {
		if (_ddmStructure != null) {
			return _ddmStructure.getPrimaryKey();
		}

		Company company = _themeDisplay.getCompany();

		_ddmStructure = DDMStructureServiceUtil.getStructure(
			company.getGroupId(),
			ClassNameLocalServiceUtil.getClassNameId(
				LayoutSEOEntry.class.getName()),
			"custom-meta-tags");

		return _ddmStructure.getPrimaryKey();
	}

	public String getDefaultCanonicalURL() throws PortalException {
		return _layoutSEOCanonicalURLProvider.getDefaultCanonicalURL(
			_selLayout, _themeDisplay);
	}

	public String getDefaultOpenGraphImageURL() throws Exception {
		LayoutSEOSite layoutSEOSite =
			_layoutSEOSiteLocalService.fetchLayoutSEOSiteByGroupId(
				getGroupId());

		if ((layoutSEOSite == null) ||
			(layoutSEOSite.getOpenGraphImageFileEntryId() == 0) ||
			!layoutSEOSite.isOpenGraphEnabled()) {

			return null;
		}

		return _dlurlHelper.getImagePreviewURL(
			_dlAppService.getFileEntry(
				layoutSEOSite.getOpenGraphImageFileEntryId()),
			_themeDisplay);
	}

	public long getGroupId() {
		LayoutSEOEntry selLayoutSEOEntry = getSelLayoutSEOEntry();

		if (selLayoutSEOEntry == null) {
			return _groupDisplayContextHelper.getGroupId();
		}

		return selLayoutSEOEntry.getGroupId();
	}

	public String getItemSelectorURL() {
		ItemSelectorCriterion imageItemSelectorCriterion =
			new ImageItemSelectorCriterion();

		imageItemSelectorCriterion.setDesiredItemSelectorReturnTypes(
			new FileEntryItemSelectorReturnType(),
			new URLItemSelectorReturnType());

		PortletURL itemSelectorURL = _itemSelector.getItemSelectorURL(
			RequestBackedPortletURLFactoryUtil.create(_httpServletRequest),
			_liferayPortletResponse.getNamespace() +
				"openGraphImageSelectedItem",
			imageItemSelectorCriterion);

		return itemSelectorURL.toString();
	}

	public Long getLayoutId() {
		if (_layoutId != null) {
			return _layoutId;
		}

		_layoutId = LayoutConstants.DEFAULT_PARENT_LAYOUT_ID;

		Layout selLayout = _getSelLayout();

		if (selLayout != null) {
			_layoutId = selLayout.getLayoutId();
		}

		return _layoutId;
	}

	public String getPageTitle() throws PortalException {
		String portletId = (String)_httpServletRequest.getAttribute(
			WebKeys.PORTLET_ID);

		ListMergeable<String> titleListMergeable =
			(ListMergeable<String>)_httpServletRequest.getAttribute(
				WebKeys.PAGE_TITLE);
		ListMergeable<String> subtitleListMergeable =
			(ListMergeable<String>)_httpServletRequest.getAttribute(
				WebKeys.PAGE_SUBTITLE);

		return _layoutSEOLinkManager.getPageTitle(
			_selLayout, portletId, _themeDisplay.getTilesTitle(),
			titleListMergeable, subtitleListMergeable,
			_themeDisplay.getLocale());
	}

	public String getPageTitleSuffix() throws PortalException {
		Company company = _themeDisplay.getCompany();

		return _layoutSEOLinkManager.getPageTitleSuffix(
			_selLayout, company.getName());
	}

	public LayoutSEOEntry getSelLayoutSEOEntry() {
		Layout layout = _getSelLayout();

		if (layout == null) {
			return null;
		}

		return LayoutSEOEntryLocalServiceUtil.fetchLayoutSEOEntry(
			layout.getGroupId(), layout.isPrivateLayout(),
			layout.getLayoutId());
	}

	private Layout _getSelLayout() {
		if (_selLayout != null) {
			return _selLayout;
		}

		if (_getSelPlid() != LayoutConstants.DEFAULT_PLID) {
			_selLayout = LayoutLocalServiceUtil.fetchLayout(_getSelPlid());
		}

		return _selLayout;
	}

	private Long _getSelPlid() {
		if (_selPlid != null) {
			return _selPlid;
		}

		_selPlid = ParamUtil.getLong(
			_liferayPortletRequest, "selPlid", LayoutConstants.DEFAULT_PLID);

		return _selPlid;
	}

	private DDMStructure _ddmStructure;
	private final DLAppService _dlAppService;
	private final DLURLHelper _dlurlHelper;
	private final GroupDisplayContextHelper _groupDisplayContextHelper;
	private final HttpServletRequest _httpServletRequest;
	private final ItemSelector _itemSelector;
	private Long _layoutId;
	private final LayoutSEOCanonicalURLProvider _layoutSEOCanonicalURLProvider;
	private final LayoutSEOLinkManager _layoutSEOLinkManager;
	private final LayoutSEOSiteLocalService _layoutSEOSiteLocalService;
	private final LiferayPortletRequest _liferayPortletRequest;
	private final LiferayPortletResponse _liferayPortletResponse;
	private Layout _selLayout;
	private Long _selPlid;
	private final StorageEngine _storageEngine;
	private final ThemeDisplay _themeDisplay;

}