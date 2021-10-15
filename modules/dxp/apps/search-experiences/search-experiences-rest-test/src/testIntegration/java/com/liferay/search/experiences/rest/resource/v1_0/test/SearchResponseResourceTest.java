/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.search.experiences.rest.resource.v1_0.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.test.util.ConfigurationTemporarySwapper;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.log.LogCapture;
import com.liferay.portal.test.log.LoggerTestUtil;
import com.liferay.search.experiences.rest.client.dto.v1_0.SearchResponse;
import com.liferay.search.experiences.rest.client.pagination.Pagination;
import com.liferay.search.experiences.rest.client.problem.Problem;

import java.util.Collections;

import org.hamcrest.CoreMatchers;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Brian Wing Shun Chan
 * @author André de Oliveira
 */
@RunWith(Arquillian.class)
public class SearchResponseResourceTest
	extends BaseSearchResponseResourceTestCase {

	@Override
	@Test
	public void testPostSearch() throws Exception {
		super.testPostSearch();

		_testPostSearch();
		_testPostSearchWithBlueprint();
		_testPostSearchWithJSONIssue();
		_testPostSearchWithMultipleQueryIssues();
		_testPostSearchWithMultipleSchemaIssuesOnlyFirstIsReported();
		_testPostSearchWithSearchEngineIssue();
	}

	@Override
	protected SearchResponse testPostSearch_addSearchResponse(
			SearchResponse searchResponse)
		throws Exception {

		return searchResponse;
	}

	private String _read() throws Exception {
		Class<?> clazz = getClass();

		Thread currentThread = Thread.currentThread();

		StackTraceElement[] stackTraceElements = currentThread.getStackTrace();

		return StringUtil.read(
			clazz.getResourceAsStream(
				StringBundler.concat(
					clazz.getSimpleName(), StringPool.PERIOD,
					stackTraceElements[2].getMethodName(), ".json")));
	}

	private void _testPostSearch() throws Exception {
		searchResponseResource.postSearch(null, null, _PAGINATION);
	}

	private void _testPostSearchWithBlueprint() throws Exception {
		searchResponseResource.postSearch(null, _read(), _PAGINATION);
	}

	private void _testPostSearchWithJSONIssue() throws Exception {
		try {
			searchResponseResource.postSearch(
				null, "{ broken JSON syntax }", _PAGINATION);

			Assert.fail();
		}
		catch (Problem.ProblemException problemException) {
			Assert.assertThat(
				problemException.getMessage(),
				CoreMatchers.containsString("Input is invalid JSON"));
		}
	}

	private void _testPostSearchWithMultipleQueryIssues() throws Exception {
		try {
			try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
					_CLASS_NAME_EXCEPTION_MAPPER, LoggerTestUtil.ERROR)) {

				searchResponseResource.postSearch(null, _read(), _PAGINATION);

				Assert.fail();
			}
		}
		catch (Problem.ProblemException problemException) {
			Assert.assertThat(
				problemException.getMessage(),
				CoreMatchers.allOf(
					CoreMatchers.containsString("Invalid query entry at: 0"),
					CoreMatchers.containsString("The key \"value\" is not set"),
					CoreMatchers.containsString("Invalid query entry at: 1"),
					CoreMatchers.containsString(
						"Unresolved template variables: [ipstack.latitude, " +
							"ipstack.longitude]")));
		}
	}

	private void _testPostSearchWithMultipleSchemaIssuesOnlyFirstIsReported()
		throws Exception {

		try {
			searchResponseResource.postSearch(null, _read(), _PAGINATION);

			Assert.fail();
		}
		catch (Problem.ProblemException problemException) {
			Assert.assertThat(
				problemException.getMessage(),
				CoreMatchers.containsString(
					StringBundler.concat(
						"Property \"configuration\" is not defined in ",
						"SXPBlueprint. Property \"general\" is not defined in ",
						"Configuration. Property \"incorrectFirst\" is not ",
						"defined in General.")));
		}
	}

	private void _testPostSearchWithSearchEngineIssue() throws Exception {
		String message = StringBundler.concat(
			"org.elasticsearch.ElasticsearchStatusException: ",
			"ElasticsearchStatusException[Elasticsearch exception ",
			"[type=parsing_exception, reason=[deliberately mistyped] query ",
			"malformed, no start_object after query name]]");

		try {
			try (ConfigurationTemporarySwapper configurationTemporarySwapper =
					new ConfigurationTemporarySwapper(
						_CONFIGURATION_PID_ELASTICSEARCH,
						new HashMapDictionary<>(
							Collections.singletonMap(
								"logExceptionsOnly", false)))) {

				try (LogCapture logCapture =
						LoggerTestUtil.configureLog4JLogger(
							_CLASS_NAME_EXCEPTION_MAPPER,
							LoggerTestUtil.ERROR)) {

					searchResponseResource.postSearch(
						null, _read(), _PAGINATION);
				}
			}

			Assert.fail();
		}
		catch (Problem.ProblemException problemException) {
			Assert.assertThat(
				problemException.getMessage(),
				CoreMatchers.containsString(message));
		}

		try (ConfigurationTemporarySwapper configurationTemporarySwapper =
				new ConfigurationTemporarySwapper(
					_CONFIGURATION_PID_ELASTICSEARCH,
					new HashMapDictionary<>(
						Collections.singletonMap("logExceptionsOnly", true)))) {

			try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
					_CLASS_NAME_ELASTICSEARCH_INDEX_SEARCHER,
					LoggerTestUtil.ERROR)) {

				SearchResponse searchResponse =
					searchResponseResource.postSearch(
						null, _read(), _PAGINATION);

				Assert.assertNull(searchResponse.getResponse());

				Assert.assertThat(
					searchResponse.getResponseString(),
					CoreMatchers.containsString(message));
			}
		}
	}

	private static final String _CLASS_NAME_ELASTICSEARCH_INDEX_SEARCHER =
		"com.liferay.portal.search.elasticsearch7.internal." +
			"ElasticsearchIndexSearcher";

	private static final String _CLASS_NAME_EXCEPTION_MAPPER =
		"com.liferay.portal.vulcan.internal.jaxrs.exception.mapper." +
			"ExceptionMapper";

	private static final String _CONFIGURATION_PID_ELASTICSEARCH =
		"com.liferay.portal.search.elasticsearch7.configuration." +
			"ElasticsearchConfiguration";

	private static final Pagination _PAGINATION = Pagination.of(1, 1);

}