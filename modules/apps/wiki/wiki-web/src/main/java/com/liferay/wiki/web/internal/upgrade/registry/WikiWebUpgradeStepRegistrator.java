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

package com.liferay.wiki.web.internal.upgrade.registry;

import com.liferay.portal.configuration.persistence.upgrade.ConfigurationUpgradeStepFactory;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.settings.SettingsFactory;
import com.liferay.portal.kernel.upgrade.BaseStagingGroupTypeSettingsUpgradeProcess;
import com.liferay.portal.kernel.upgrade.DummyUpgradeStep;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;
import com.liferay.wiki.constants.WikiPortletKeys;
import com.liferay.wiki.web.internal.configuration.WikiPortletInstanceConfiguration;
import com.liferay.wiki.web.internal.upgrade.v1_0_0.UpgradePortletSettings;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Iván Zaera
 * @author Manuel de la Peña
 */
@Component(immediate = true, service = UpgradeStepRegistrator.class)
public class WikiWebUpgradeStepRegistrator implements UpgradeStepRegistrator {

	@Override
	public void register(Registry registry) {
		registry.register("0.0.0", "1.0.2", new DummyUpgradeStep());

		registry.register(
			"0.0.1", "1.0.0", new UpgradePortletSettings(_settingsFactory));

		registry.register(
			"1.0.0", "1.0.1",
			new BaseStagingGroupTypeSettingsUpgradeProcess(
				_companyLocalService, _groupLocalService, WikiPortletKeys.WIKI,
				WikiPortletKeys.WIKI_ADMIN));

		registry.register(
			"1.0.1", "1.0.2",
			_configurationUpgradeStepFactory.createUpgradeStep(
				"com.liferay.wiki.configuration." +
					"WikiPortletInstanceConfiguration",
				WikiPortletInstanceConfiguration.class.getName()),
			_configurationUpgradeStepFactory.createUpgradeStep(
				"com.liferay.wiki.web.configuration." +
					"WikiPortletInstanceConfiguration",
				WikiPortletInstanceConfiguration.class.getName()));
	}

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private ConfigurationUpgradeStepFactory _configurationUpgradeStepFactory;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private SettingsFactory _settingsFactory;

}