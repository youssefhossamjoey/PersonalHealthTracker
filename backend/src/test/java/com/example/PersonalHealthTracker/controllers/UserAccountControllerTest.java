package com.example.personalhealthtracker.controllers;

import com.example.personalhealthtracker.domain.dto.UserAccount;
import com.example.personalhealthtracker.domain.entities.Role;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserAccountControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void UserAccountCanBeCreated() {
		UserAccount user = new UserAccount("user1", "pass1", Role.MEMBER);
		ResponseEntity<Void> response = restTemplate.postForEntity("/useraccount", user, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		URI location = response.getHeaders().getLocation();
		ResponseEntity<String> getResponse = restTemplate.getForEntity(location, String.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext dc = JsonPath.parse(getResponse.getBody());
		String id = dc.read("$.id");
		String username = dc.read("$.username");
		String role = dc.read("$.role");
		String password = dc.read("$.password");

		assertThat(id).isNull();
		assertThat(password).isNull();
		assertThat(username).isEqualTo("user1");
		assertThat(role).isEqualTo("MEMBER");
	}

	@Test
	public void shouldReturnAPageOfUserAccounts() {
		restTemplate.postForEntity("/useraccount", new UserAccount("u1", "p1", Role.MEMBER), Void.class);
		restTemplate.postForEntity("/useraccount", new UserAccount("u2", "p2", Role.MEMBER), Void.class);
		restTemplate.postForEntity("/useraccount", new UserAccount("u3", "p3", Role.MEMBER), Void.class);

		ResponseEntity<String> response = restTemplate.getForEntity("/useraccount?page=0&size=1", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext dc = JsonPath.parse(response.getBody());
		JSONArray content = dc.read("$.content");
		assertThat(content.size()).isEqualTo(1);
	}
}
