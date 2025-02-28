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

package com.liferay.friendly.url.internal.exportimport.data.handler;

import com.liferay.exportimport.data.handler.base.BaseStagedModelDataHandler;
import com.liferay.exportimport.kernel.lar.ExportImportPathUtil;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandler;
import com.liferay.exportimport.staged.model.repository.StagedModelRepository;
import com.liferay.friendly.url.model.FriendlyURLEntry;
import com.liferay.friendly.url.service.FriendlyURLEntryLocalService;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.xml.Element;

import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Adolfo Pérez
 */
@Component(immediate = true, service = StagedModelDataHandler.class)
public class FriendlyURLEntryStagedModelDataHandler
	extends BaseStagedModelDataHandler<FriendlyURLEntry> {

	public static final String[] CLASS_NAMES = {
		FriendlyURLEntry.class.getName()
	};

	@Override
	public FriendlyURLEntry fetchStagedModelByUuidAndGroupId(
		String uuid, long groupId) {

		return _stagedModelRepository.fetchStagedModelByUuidAndGroupId(
			uuid, groupId);
	}

	@Override
	public List<FriendlyURLEntry> fetchStagedModelsByUuidAndCompanyId(
		String uuid, long companyId) {

		return _stagedModelRepository.fetchStagedModelsByUuidAndCompanyId(
			uuid, companyId);
	}

	@Override
	public String[] getClassNames() {
		return CLASS_NAMES;
	}

	@Override
	protected void doExportStagedModel(
			PortletDataContext portletDataContext,
			FriendlyURLEntry friendlyURLEntry)
		throws Exception {

		Element friendlyURLEntryElement =
			portletDataContext.getExportDataElement(friendlyURLEntry);

		friendlyURLEntryElement.addAttribute(
			"resource-class-name", friendlyURLEntry.getClassName());

		portletDataContext.addZipEntry(
			ExportImportPathUtil.getModelPath(
				friendlyURLEntry, friendlyURLEntry.getUuid()),
			friendlyURLEntry.getUrlTitleMapAsXML());

		FriendlyURLEntry mainFriendlyURLEntry =
			_friendlyURLEntryLocalService.fetchMainFriendlyURLEntry(
				friendlyURLEntry.getClassNameId(),
				friendlyURLEntry.getClassPK());

		if (mainFriendlyURLEntry == null) {
			_friendlyURLEntryLocalService.setMainFriendlyURLEntry(
				friendlyURLEntry);
		}

		if (friendlyURLEntry.isMain()) {
			friendlyURLEntryElement.addAttribute(
				"mainEntry", Boolean.TRUE.toString());
		}

		portletDataContext.addClassedModel(
			friendlyURLEntryElement,
			ExportImportPathUtil.getModelPath(friendlyURLEntry),
			friendlyURLEntry);
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext,
			FriendlyURLEntry friendlyURLEntry)
		throws Exception {

		Element friendlyURLEntryElement =
			portletDataContext.getImportDataStagedModelElement(
				friendlyURLEntry);

		String className = friendlyURLEntryElement.attributeValue(
			"resource-class-name");

		long classNameId = _classNameLocalService.getClassNameId(className);

		Map<Long, Long> newPrimaryKeysMap =
			(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(className);

		FriendlyURLEntry existingFriendlyURLEntry =
			fetchStagedModelByUuidAndGroupId(
				friendlyURLEntry.getUuid(),
				portletDataContext.getScopeGroupId());

		FriendlyURLEntry importedFriendlyURLEntry = null;

		if ((existingFriendlyURLEntry == null) ||
			!portletDataContext.isDataStrategyMirror()) {

			importedFriendlyURLEntry =
				(FriendlyURLEntry)friendlyURLEntry.clone();

			importedFriendlyURLEntry.setGroupId(
				portletDataContext.getScopeGroupId());
			importedFriendlyURLEntry.setCompanyId(
				portletDataContext.getCompanyId());
			importedFriendlyURLEntry.setClassNameId(classNameId);
			importedFriendlyURLEntry.setClassPK(
				MapUtil.getLong(
					newPrimaryKeysMap, friendlyURLEntry.getClassPK(),
					friendlyURLEntry.getClassPK()));
			importedFriendlyURLEntry.setDefaultLanguageId(
				friendlyURLEntry.getDefaultLanguageId());

			importedFriendlyURLEntry = _stagedModelRepository.addStagedModel(
				portletDataContext, importedFriendlyURLEntry);

			boolean mainEntry = GetterUtil.getBoolean(
				friendlyURLEntryElement.attributeValue("mainEntry"));

			if (mainEntry) {
				_friendlyURLEntryLocalService.setMainFriendlyURLEntry(
					importedFriendlyURLEntry);
			}
		}
		else {
			importedFriendlyURLEntry = _stagedModelRepository.updateStagedModel(
				portletDataContext, existingFriendlyURLEntry);

			boolean mainEntry = GetterUtil.getBoolean(
				friendlyURLEntryElement.attributeValue("mainEntry"));

			if (mainEntry) {
				_friendlyURLEntryLocalService.setMainFriendlyURLEntry(
					existingFriendlyURLEntry);
			}
		}

		portletDataContext.importClassedModel(
			friendlyURLEntry, importedFriendlyURLEntry);
	}

	@Override
	protected StagedModelRepository<FriendlyURLEntry>
		getStagedModelRepository() {

		return _stagedModelRepository;
	}

	@Reference
	private ClassNameLocalService _classNameLocalService;

	@Reference
	private FriendlyURLEntryLocalService _friendlyURLEntryLocalService;

	@Reference(
		target = "(model.class.name=com.liferay.friendly.url.model.FriendlyURLEntry)"
	)
	private StagedModelRepository<FriendlyURLEntry> _stagedModelRepository;

}