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

package com.liferay.commerce.product.subscription.type.web.internal;

import com.liferay.commerce.product.constants.CPConstants;
import com.liferay.commerce.product.subscription.type.web.internal.constants.CPSubscriptionTypeConstants;
import com.liferay.commerce.product.util.CPSubscriptionType;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.util.CalendarFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	enabled = false, immediate = true,
	property = {
		"commerce.product.subscription.type.name=" + CPConstants.MONTHLY_SUBSCRIPTION_TYPE,
		"commerce.product.subscription.type.order:Integer=30"
	},
	service = CPSubscriptionType.class
)
public class MonthlyCPSubscriptionTypeImpl implements CPSubscriptionType {

	@Override
	public String getLabel(Locale locale) {
		return _language.get(locale, "month");
	}

	@Override
	public String getName() {
		return CPConstants.MONTHLY_SUBSCRIPTION_TYPE;
	}

	@Override
	public Date getSubscriptionNextIterationDate(
		TimeZone timeZone, int subscriptionLength,
		UnicodeProperties subscriptionTypeSettingsUnicodeProperties,
		Date lastIterationDate) {

		Calendar calendar = CalendarFactoryUtil.getCalendar(timeZone);

		if (lastIterationDate == null) {
			lastIterationDate = getSubscriptionStartDate(
				timeZone, subscriptionTypeSettingsUnicodeProperties);
		}

		calendar.setTime(lastIterationDate);

		calendar.add(Calendar.MONTH, subscriptionLength);

		int monthlyMode = GetterUtil.getInteger(
			subscriptionTypeSettingsUnicodeProperties.get("monthlyMode"));

		if (monthlyMode ==
				CPSubscriptionTypeConstants.MODE_EXACT_DAY_OF_MONTH) {

			int monthDay = GetterUtil.getInteger(
				subscriptionTypeSettingsUnicodeProperties.get("monthDay"));

			int dayOfMonthActualMaximum = calendar.getActualMaximum(
				Calendar.DAY_OF_MONTH);

			if (monthDay > dayOfMonthActualMaximum) {
				monthDay = dayOfMonthActualMaximum;
			}

			if (monthDay > 0) {
				calendar.set(Calendar.DAY_OF_MONTH, monthDay);
			}
		}

		return calendar.getTime();
	}

	@Override
	public Date getSubscriptionStartDate(
		TimeZone timeZone,
		UnicodeProperties subscriptionTypeSettingsUnicodeProperties) {

		Date date = new Date();

		if ((subscriptionTypeSettingsUnicodeProperties == null) ||
			subscriptionTypeSettingsUnicodeProperties.isEmpty()) {

			return date;
		}

		Calendar calendar = CalendarFactoryUtil.getCalendar(
			date.getTime(), timeZone);

		int dayOfMonthActualMaximum = calendar.getActualMaximum(
			Calendar.DAY_OF_MONTH);

		int today = calendar.get(Calendar.DAY_OF_MONTH);

		int monthlyMode = GetterUtil.getInteger(
			subscriptionTypeSettingsUnicodeProperties.get("monthlyMode"));

		if (monthlyMode ==
				CPSubscriptionTypeConstants.MODE_EXACT_DAY_OF_MONTH) {

			int monthDay = GetterUtil.getInteger(
				subscriptionTypeSettingsUnicodeProperties.get("monthDay"));

			if (monthDay > dayOfMonthActualMaximum) {
				monthDay = dayOfMonthActualMaximum;
			}

			if (monthDay < today) {
				return date;
			}

			calendar.set(Calendar.DAY_OF_MONTH, monthDay);
		}
		else if (monthlyMode ==
					CPSubscriptionTypeConstants.MODE_LAST_DAY_OF_MONTH) {

			calendar.set(Calendar.DAY_OF_MONTH, dayOfMonthActualMaximum);
		}

		return calendar.getTime();
	}

	@Reference
	private Language _language;

}