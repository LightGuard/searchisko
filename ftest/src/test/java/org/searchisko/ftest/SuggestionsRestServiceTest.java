/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 */
package org.searchisko.ftest;

import com.jayway.restassured.http.ContentType;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Integration test for /sugggestions REST API.
 * <p/>
 * http://docs.jbossorg.apiary.io/#suggestionsapiquery
 *
 * @author Libor Krzyzanek
 * @see org.searchisko.api.rest.SuggestionsRestService
 */
@RunWith(Arquillian.class)
public class SuggestionsRestServiceTest {

	public static final String SUGGESTIONS_REST_API = DeploymentHelpers.DEFAULT_REST_VERSION + "suggestions/{apitype}?query={query}";

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {
		return DeploymentHelpers.createDeployment();
	}

	@ArquillianResource
	URL context;


	@Test
	@InSequence(10)
	public void assertSuggestionsQuery() throws MalformedURLException {
		given().contentType(ContentType.JSON)
				.pathParam("apitype", "query_string")
				.pathParam("query", "Hiberna")
				.expect()
//				.log().ifError()
				.statusCode(500)
				.when().get(new URL(context, SUGGESTIONS_REST_API).toExternalForm());
	}


	@Test
	@InSequence(20)
	public void assertSuggestionsProject() throws MalformedURLException {
		given().contentType(ContentType.JSON)
				.pathParam("apitype", "project")
				.pathParam("query", "gin")
				.expect()
				.log().ifError()
				.statusCode(200)
				.contentType(ContentType.JSON)
				.body("uuid", notNullValue())
				.when().get(new URL(context, SUGGESTIONS_REST_API).toExternalForm());
	}

}
