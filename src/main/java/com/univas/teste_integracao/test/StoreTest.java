package com.univas.teste_integracao.test;

import static org.junit.Assert.assertEquals;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.univas.teste_integracao.dto.Store;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class StoreTest {
	
	private ObjectMapper mapper = new ObjectMapper();
	private final String storeURL = "http://localhost:8080/store";

	private Response createStoreWithId(Long id) {
		Store store = new Store(352647, 13790324, "Angelo", "Cachoeira de minas", "08/06/2023", true);

		Response resp = RestAssured.given().body(store).contentType(ContentType.JSON).post(storeURL);
		return resp;
	}

	// Post success
	@Test
	public void testCreateStore_withSuccess() {
		Long id = 1001L;
		Response resp = createStoreWithId(id);
		resp.then().assertThat().statusCode(HttpStatus.SC_CREATED);
	}

	// Post fail with existing id
	@Test
	public void testCreateStore_withExistId() {
		Long id = 1L;
		Response resp = RestAssured.get(storeURL + "/" + id);

		if (resp.getStatusCode() == HttpStatus.SC_OK) {
			RestAssured.given().contentType(ContentType.JSON).post(storeURL).then().assertThat()
					.statusCode(HttpStatus.SC_BAD_REQUEST);
		} else {
			Response write = createStoreWithId(id);
			write.then().assertThat().statusCode(HttpStatus.SC_CREATED);
		}
	}

	// Post fail badrequest
	@Test
	public void testCreateStore_withWrongJson() {
		RestAssured.given().contentType(ContentType.JSON).post(storeURL).then().assertThat()
				.statusCode(HttpStatus.SC_BAD_REQUEST);
	}

	// Get success
	@Test
	public void testGetStoreById_withSuccessfull() {
		Long id = 1L;
		createStoreWithId(id);
		Response resp = RestAssured.get(storeURL + "/" + id);
		resp.then().assertThat().statusCode(HttpStatus.SC_OK);
	}

	// Get fail object not found
	@Test
	public void testGetStoreByid_withFail() {
		Long nonExistingDeliveryNumber = 600L;
		Response resp = RestAssured.get(storeURL + nonExistingDeliveryNumber);
		resp.then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND);
	}

	// Put success
	@Test
	public void testPutStoreById_withSuccess() {
		Long id = 1L;

		Response action = RestAssured.put(storeURL + "active/" + id);
		action.then().assertThat().statusCode(HttpStatus.SC_NO_CONTENT);
		assertEquals(HttpStatus.SC_NO_CONTENT, action.getStatusCode());

		Response resp = RestAssured.get(storeURL + id);
		resp.then().assertThat().statusCode(HttpStatus.SC_OK);
	}

	// Put fail not found
	@Test
	public void testPutStore_withNoExistCode() {
		Long id = 700L;

		Response resp = RestAssured.get(storeURL + id);
		if (resp.getStatusCode() == HttpStatus.SC_OK) {
			Response action = RestAssured.put(storeURL + "active/" + id);
			action.then().assertThat().statusCode(HttpStatus.SC_NO_CONTENT);
			assertEquals(HttpStatus.SC_NO_CONTENT, action.getStatusCode());
		} else {
			resp.then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND);
		}
	}

}