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

package com.liferay.knowledge.base.web.internal.upgrade.registry;

import com.liferay.knowledge.base.web.internal.upgrade.v1_0_0.UpgradePortletId;
import com.liferay.knowledge.base.web.internal.upgrade.v1_0_0.UpgradePortletSettings;
import com.liferay.knowledge.base.web.internal.upgrade.v1_1_0.UpgradePortletPreferences;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.settings.SettingsFactory;
import com.liferay.portal.kernel.upgrade.DummyUpgradeStep;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Adolfo Pérez
 * @author Roberto Díaz
 */
@Component(immediate = true, service = UpgradeStepRegistrator.class)
public class KnowledgeBaseWebUpgradeStepRegistrator
	implements UpgradeStepRegistrator {

	@Override
	public void register(Registry registry) {
		registry.register("0.0.0", "1.2.0", new DummyUpgradeStep());

		registry.register(
			"0.0.1", "1.0.0", new UpgradePortletId(),
			new UpgradePortletSettings(_settingsFactory));

		registry.register("1.0.0", "1.1.0", new UpgradePortletPreferences());

		registry.register(
			"1.1.0", "1.2.0",
			new com.liferay.knowledge.base.web.internal.upgrade.v1_2_0.
				UpgradePortletPreferences());
	}

	@Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED, unbind = "-")
	protected void setModuleServiceLifecycle(
		ModuleServiceLifecycle moduleServiceLifecycle) {
	}

	@Reference
	private SettingsFactory _settingsFactory;

}