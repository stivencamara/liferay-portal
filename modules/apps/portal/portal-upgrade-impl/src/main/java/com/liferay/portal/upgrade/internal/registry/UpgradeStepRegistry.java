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

package com.liferay.portal.upgrade.internal.registry;

import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.upgrade.UpgradeStep;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Preston Crary
 */
public class UpgradeStepRegistry implements UpgradeStepRegistrator.Registry {

	public UpgradeStepRegistry(int buildNumber) {
		_buildNumber = buildNumber;
	}

	public List<UpgradeStep> getInitialUpgradeSteps() {
		return _initialUpgradeSteps;
	}

	public List<UpgradeInfo> getUpgradeInfos() {
		return _upgradeInfos;
	}

	@Override
	public void register(
		String fromSchemaVersionString, String toSchemaVersionString,
		UpgradeStep... upgradeSteps) {

		_createUpgradeInfos(
			fromSchemaVersionString, toSchemaVersionString, _buildNumber,
			upgradeSteps);
	}

	@Override
	public void registerInitialUpgradeSteps(UpgradeStep... upgradeSteps) {
		Collections.addAll(_initialUpgradeSteps, upgradeSteps);
	}

	private void _createUpgradeInfos(
		String fromSchemaVersionString, String toSchemaVersionString,
		int buildNumber, UpgradeStep... upgradeSteps) {

		if (ArrayUtil.isEmpty(upgradeSteps)) {
			return;
		}

		String upgradeInfoFromSchemaVersionString = fromSchemaVersionString;

		List<UpgradeStep> upgradeStepsList = new ArrayList<>();

		for (UpgradeStep upgradeStep : upgradeSteps) {
			if (upgradeStep instanceof UpgradeProcess) {
				UpgradeProcess upgradeProcess = (UpgradeProcess)upgradeStep;

				for (UpgradeStep innerUpgradeStep :
						upgradeProcess.getUpgradeSteps()) {

					upgradeStepsList.add(innerUpgradeStep);
				}
			}
			else {
				upgradeStepsList.add(upgradeStep);
			}
		}

		for (int i = 0; i < (upgradeStepsList.size() - 1); i++) {
			UpgradeStep upgradeStep = upgradeStepsList.get(i);

			String upgradeInfoToSchemaVersionString =
				toSchemaVersionString + ".step" +
					(i - upgradeStepsList.size() + 1);

			UpgradeInfo upgradeInfo = new UpgradeInfo(
				upgradeInfoFromSchemaVersionString,
				upgradeInfoToSchemaVersionString, buildNumber, upgradeStep);

			_upgradeInfos.add(upgradeInfo);

			upgradeInfoFromSchemaVersionString =
				upgradeInfoToSchemaVersionString;
		}

		UpgradeInfo upgradeInfo = new UpgradeInfo(
			upgradeInfoFromSchemaVersionString, toSchemaVersionString,
			buildNumber, upgradeStepsList.get(upgradeStepsList.size() - 1));

		_upgradeInfos.add(upgradeInfo);
	}

	private final int _buildNumber;
	private final List<UpgradeStep> _initialUpgradeSteps = new ArrayList<>();
	private final List<UpgradeInfo> _upgradeInfos = new ArrayList<>();

}