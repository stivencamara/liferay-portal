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

package com.liferay.oauth2.provider.service.base;

import com.liferay.oauth2.provider.model.OAuth2Application;
import com.liferay.oauth2.provider.service.OAuth2ApplicationService;
import com.liferay.oauth2.provider.service.persistence.OAuth2AccessTokenPersistence;
import com.liferay.oauth2.provider.service.persistence.OAuth2ApplicationPersistence;
import com.liferay.oauth2.provider.service.persistence.OAuth2RefreshTokenPersistence;
import com.liferay.oauth2.provider.service.persistence.OAuth2ScopeGrantPersistence;

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.service.BaseServiceImpl;
import com.liferay.portal.kernel.service.persistence.GroupPersistence;
import com.liferay.portal.kernel.service.persistence.UserPersistence;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.spring.extender.service.ServiceReference;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the o auth2 application remote service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.oauth2.provider.service.impl.OAuth2ApplicationServiceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.oauth2.provider.service.impl.OAuth2ApplicationServiceImpl
 * @see com.liferay.oauth2.provider.service.OAuth2ApplicationServiceUtil
 * @generated
 */
public abstract class OAuth2ApplicationServiceBaseImpl extends BaseServiceImpl
	implements OAuth2ApplicationService, IdentifiableOSGiService {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link com.liferay.oauth2.provider.service.OAuth2ApplicationServiceUtil} to access the o auth2 application remote service.
	 */

	/**
	 * Returns the o auth2 application local service.
	 *
	 * @return the o auth2 application local service
	 */
	public com.liferay.oauth2.provider.service.OAuth2ApplicationLocalService getOAuth2ApplicationLocalService() {
		return oAuth2ApplicationLocalService;
	}

	/**
	 * Sets the o auth2 application local service.
	 *
	 * @param oAuth2ApplicationLocalService the o auth2 application local service
	 */
	public void setOAuth2ApplicationLocalService(
		com.liferay.oauth2.provider.service.OAuth2ApplicationLocalService oAuth2ApplicationLocalService) {
		this.oAuth2ApplicationLocalService = oAuth2ApplicationLocalService;
	}

	/**
	 * Returns the o auth2 application remote service.
	 *
	 * @return the o auth2 application remote service
	 */
	public OAuth2ApplicationService getOAuth2ApplicationService() {
		return oAuth2ApplicationService;
	}

	/**
	 * Sets the o auth2 application remote service.
	 *
	 * @param oAuth2ApplicationService the o auth2 application remote service
	 */
	public void setOAuth2ApplicationService(
		OAuth2ApplicationService oAuth2ApplicationService) {
		this.oAuth2ApplicationService = oAuth2ApplicationService;
	}

	/**
	 * Returns the o auth2 application persistence.
	 *
	 * @return the o auth2 application persistence
	 */
	public OAuth2ApplicationPersistence getOAuth2ApplicationPersistence() {
		return oAuth2ApplicationPersistence;
	}

	/**
	 * Sets the o auth2 application persistence.
	 *
	 * @param oAuth2ApplicationPersistence the o auth2 application persistence
	 */
	public void setOAuth2ApplicationPersistence(
		OAuth2ApplicationPersistence oAuth2ApplicationPersistence) {
		this.oAuth2ApplicationPersistence = oAuth2ApplicationPersistence;
	}

	/**
	 * Returns the counter local service.
	 *
	 * @return the counter local service
	 */
	public com.liferay.counter.kernel.service.CounterLocalService getCounterLocalService() {
		return counterLocalService;
	}

	/**
	 * Sets the counter local service.
	 *
	 * @param counterLocalService the counter local service
	 */
	public void setCounterLocalService(
		com.liferay.counter.kernel.service.CounterLocalService counterLocalService) {
		this.counterLocalService = counterLocalService;
	}

	/**
	 * Returns the o auth2 access token local service.
	 *
	 * @return the o auth2 access token local service
	 */
	public com.liferay.oauth2.provider.service.OAuth2AccessTokenLocalService getOAuth2AccessTokenLocalService() {
		return oAuth2AccessTokenLocalService;
	}

	/**
	 * Sets the o auth2 access token local service.
	 *
	 * @param oAuth2AccessTokenLocalService the o auth2 access token local service
	 */
	public void setOAuth2AccessTokenLocalService(
		com.liferay.oauth2.provider.service.OAuth2AccessTokenLocalService oAuth2AccessTokenLocalService) {
		this.oAuth2AccessTokenLocalService = oAuth2AccessTokenLocalService;
	}

	/**
	 * Returns the o auth2 access token remote service.
	 *
	 * @return the o auth2 access token remote service
	 */
	public com.liferay.oauth2.provider.service.OAuth2AccessTokenService getOAuth2AccessTokenService() {
		return oAuth2AccessTokenService;
	}

	/**
	 * Sets the o auth2 access token remote service.
	 *
	 * @param oAuth2AccessTokenService the o auth2 access token remote service
	 */
	public void setOAuth2AccessTokenService(
		com.liferay.oauth2.provider.service.OAuth2AccessTokenService oAuth2AccessTokenService) {
		this.oAuth2AccessTokenService = oAuth2AccessTokenService;
	}

	/**
	 * Returns the o auth2 access token persistence.
	 *
	 * @return the o auth2 access token persistence
	 */
	public OAuth2AccessTokenPersistence getOAuth2AccessTokenPersistence() {
		return oAuth2AccessTokenPersistence;
	}

	/**
	 * Sets the o auth2 access token persistence.
	 *
	 * @param oAuth2AccessTokenPersistence the o auth2 access token persistence
	 */
	public void setOAuth2AccessTokenPersistence(
		OAuth2AccessTokenPersistence oAuth2AccessTokenPersistence) {
		this.oAuth2AccessTokenPersistence = oAuth2AccessTokenPersistence;
	}

	/**
	 * Returns the group local service.
	 *
	 * @return the group local service
	 */
	public com.liferay.portal.kernel.service.GroupLocalService getGroupLocalService() {
		return groupLocalService;
	}

	/**
	 * Sets the group local service.
	 *
	 * @param groupLocalService the group local service
	 */
	public void setGroupLocalService(
		com.liferay.portal.kernel.service.GroupLocalService groupLocalService) {
		this.groupLocalService = groupLocalService;
	}

	/**
	 * Returns the group remote service.
	 *
	 * @return the group remote service
	 */
	public com.liferay.portal.kernel.service.GroupService getGroupService() {
		return groupService;
	}

	/**
	 * Sets the group remote service.
	 *
	 * @param groupService the group remote service
	 */
	public void setGroupService(
		com.liferay.portal.kernel.service.GroupService groupService) {
		this.groupService = groupService;
	}

	/**
	 * Returns the group persistence.
	 *
	 * @return the group persistence
	 */
	public GroupPersistence getGroupPersistence() {
		return groupPersistence;
	}

	/**
	 * Sets the group persistence.
	 *
	 * @param groupPersistence the group persistence
	 */
	public void setGroupPersistence(GroupPersistence groupPersistence) {
		this.groupPersistence = groupPersistence;
	}

	/**
	 * Returns the resource local service.
	 *
	 * @return the resource local service
	 */
	public com.liferay.portal.kernel.service.ResourceLocalService getResourceLocalService() {
		return resourceLocalService;
	}

	/**
	 * Sets the resource local service.
	 *
	 * @param resourceLocalService the resource local service
	 */
	public void setResourceLocalService(
		com.liferay.portal.kernel.service.ResourceLocalService resourceLocalService) {
		this.resourceLocalService = resourceLocalService;
	}

	/**
	 * Returns the user local service.
	 *
	 * @return the user local service
	 */
	public com.liferay.portal.kernel.service.UserLocalService getUserLocalService() {
		return userLocalService;
	}

	/**
	 * Sets the user local service.
	 *
	 * @param userLocalService the user local service
	 */
	public void setUserLocalService(
		com.liferay.portal.kernel.service.UserLocalService userLocalService) {
		this.userLocalService = userLocalService;
	}

	/**
	 * Returns the user remote service.
	 *
	 * @return the user remote service
	 */
	public com.liferay.portal.kernel.service.UserService getUserService() {
		return userService;
	}

	/**
	 * Sets the user remote service.
	 *
	 * @param userService the user remote service
	 */
	public void setUserService(
		com.liferay.portal.kernel.service.UserService userService) {
		this.userService = userService;
	}

	/**
	 * Returns the user persistence.
	 *
	 * @return the user persistence
	 */
	public UserPersistence getUserPersistence() {
		return userPersistence;
	}

	/**
	 * Sets the user persistence.
	 *
	 * @param userPersistence the user persistence
	 */
	public void setUserPersistence(UserPersistence userPersistence) {
		this.userPersistence = userPersistence;
	}

	/**
	 * Returns the o auth2 refresh token local service.
	 *
	 * @return the o auth2 refresh token local service
	 */
	public com.liferay.oauth2.provider.service.OAuth2RefreshTokenLocalService getOAuth2RefreshTokenLocalService() {
		return oAuth2RefreshTokenLocalService;
	}

	/**
	 * Sets the o auth2 refresh token local service.
	 *
	 * @param oAuth2RefreshTokenLocalService the o auth2 refresh token local service
	 */
	public void setOAuth2RefreshTokenLocalService(
		com.liferay.oauth2.provider.service.OAuth2RefreshTokenLocalService oAuth2RefreshTokenLocalService) {
		this.oAuth2RefreshTokenLocalService = oAuth2RefreshTokenLocalService;
	}

	/**
	 * Returns the o auth2 refresh token remote service.
	 *
	 * @return the o auth2 refresh token remote service
	 */
	public com.liferay.oauth2.provider.service.OAuth2RefreshTokenService getOAuth2RefreshTokenService() {
		return oAuth2RefreshTokenService;
	}

	/**
	 * Sets the o auth2 refresh token remote service.
	 *
	 * @param oAuth2RefreshTokenService the o auth2 refresh token remote service
	 */
	public void setOAuth2RefreshTokenService(
		com.liferay.oauth2.provider.service.OAuth2RefreshTokenService oAuth2RefreshTokenService) {
		this.oAuth2RefreshTokenService = oAuth2RefreshTokenService;
	}

	/**
	 * Returns the o auth2 refresh token persistence.
	 *
	 * @return the o auth2 refresh token persistence
	 */
	public OAuth2RefreshTokenPersistence getOAuth2RefreshTokenPersistence() {
		return oAuth2RefreshTokenPersistence;
	}

	/**
	 * Sets the o auth2 refresh token persistence.
	 *
	 * @param oAuth2RefreshTokenPersistence the o auth2 refresh token persistence
	 */
	public void setOAuth2RefreshTokenPersistence(
		OAuth2RefreshTokenPersistence oAuth2RefreshTokenPersistence) {
		this.oAuth2RefreshTokenPersistence = oAuth2RefreshTokenPersistence;
	}

	/**
	 * Returns the o auth2 scope grant local service.
	 *
	 * @return the o auth2 scope grant local service
	 */
	public com.liferay.oauth2.provider.service.OAuth2ScopeGrantLocalService getOAuth2ScopeGrantLocalService() {
		return oAuth2ScopeGrantLocalService;
	}

	/**
	 * Sets the o auth2 scope grant local service.
	 *
	 * @param oAuth2ScopeGrantLocalService the o auth2 scope grant local service
	 */
	public void setOAuth2ScopeGrantLocalService(
		com.liferay.oauth2.provider.service.OAuth2ScopeGrantLocalService oAuth2ScopeGrantLocalService) {
		this.oAuth2ScopeGrantLocalService = oAuth2ScopeGrantLocalService;
	}

	/**
	 * Returns the o auth2 scope grant persistence.
	 *
	 * @return the o auth2 scope grant persistence
	 */
	public OAuth2ScopeGrantPersistence getOAuth2ScopeGrantPersistence() {
		return oAuth2ScopeGrantPersistence;
	}

	/**
	 * Sets the o auth2 scope grant persistence.
	 *
	 * @param oAuth2ScopeGrantPersistence the o auth2 scope grant persistence
	 */
	public void setOAuth2ScopeGrantPersistence(
		OAuth2ScopeGrantPersistence oAuth2ScopeGrantPersistence) {
		this.oAuth2ScopeGrantPersistence = oAuth2ScopeGrantPersistence;
	}

	public void afterPropertiesSet() {
	}

	public void destroy() {
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return OAuth2ApplicationService.class.getName();
	}

	protected Class<?> getModelClass() {
		return OAuth2Application.class;
	}

	protected String getModelClassName() {
		return OAuth2Application.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = oAuth2ApplicationPersistence.getDataSource();

			DB db = DBManagerUtil.getDB();

			sql = db.buildSQL(sql);
			sql = PortalUtil.transformSQL(sql);

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(dataSource,
					sql);

			sqlUpdate.update();
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	@BeanReference(type = com.liferay.oauth2.provider.service.OAuth2ApplicationLocalService.class)
	protected com.liferay.oauth2.provider.service.OAuth2ApplicationLocalService oAuth2ApplicationLocalService;
	@BeanReference(type = OAuth2ApplicationService.class)
	protected OAuth2ApplicationService oAuth2ApplicationService;
	@BeanReference(type = OAuth2ApplicationPersistence.class)
	protected OAuth2ApplicationPersistence oAuth2ApplicationPersistence;
	@ServiceReference(type = com.liferay.counter.kernel.service.CounterLocalService.class)
	protected com.liferay.counter.kernel.service.CounterLocalService counterLocalService;
	@BeanReference(type = com.liferay.oauth2.provider.service.OAuth2AccessTokenLocalService.class)
	protected com.liferay.oauth2.provider.service.OAuth2AccessTokenLocalService oAuth2AccessTokenLocalService;
	@BeanReference(type = com.liferay.oauth2.provider.service.OAuth2AccessTokenService.class)
	protected com.liferay.oauth2.provider.service.OAuth2AccessTokenService oAuth2AccessTokenService;
	@BeanReference(type = OAuth2AccessTokenPersistence.class)
	protected OAuth2AccessTokenPersistence oAuth2AccessTokenPersistence;
	@ServiceReference(type = com.liferay.portal.kernel.service.GroupLocalService.class)
	protected com.liferay.portal.kernel.service.GroupLocalService groupLocalService;
	@ServiceReference(type = com.liferay.portal.kernel.service.GroupService.class)
	protected com.liferay.portal.kernel.service.GroupService groupService;
	@ServiceReference(type = GroupPersistence.class)
	protected GroupPersistence groupPersistence;
	@ServiceReference(type = com.liferay.portal.kernel.service.ResourceLocalService.class)
	protected com.liferay.portal.kernel.service.ResourceLocalService resourceLocalService;
	@ServiceReference(type = com.liferay.portal.kernel.service.UserLocalService.class)
	protected com.liferay.portal.kernel.service.UserLocalService userLocalService;
	@ServiceReference(type = com.liferay.portal.kernel.service.UserService.class)
	protected com.liferay.portal.kernel.service.UserService userService;
	@ServiceReference(type = UserPersistence.class)
	protected UserPersistence userPersistence;
	@BeanReference(type = com.liferay.oauth2.provider.service.OAuth2RefreshTokenLocalService.class)
	protected com.liferay.oauth2.provider.service.OAuth2RefreshTokenLocalService oAuth2RefreshTokenLocalService;
	@BeanReference(type = com.liferay.oauth2.provider.service.OAuth2RefreshTokenService.class)
	protected com.liferay.oauth2.provider.service.OAuth2RefreshTokenService oAuth2RefreshTokenService;
	@BeanReference(type = OAuth2RefreshTokenPersistence.class)
	protected OAuth2RefreshTokenPersistence oAuth2RefreshTokenPersistence;
	@BeanReference(type = com.liferay.oauth2.provider.service.OAuth2ScopeGrantLocalService.class)
	protected com.liferay.oauth2.provider.service.OAuth2ScopeGrantLocalService oAuth2ScopeGrantLocalService;
	@BeanReference(type = OAuth2ScopeGrantPersistence.class)
	protected OAuth2ScopeGrantPersistence oAuth2ScopeGrantPersistence;
}