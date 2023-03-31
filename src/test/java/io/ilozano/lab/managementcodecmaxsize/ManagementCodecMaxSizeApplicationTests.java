package io.ilozano.lab.managementcodecmaxsize;

import java.util.Arrays;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalManagementPort;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.StringUtils;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
		"management.codec.max-in-memory-size=" + ManagementCodecMaxSizeApplicationTests.MANAGEMENT_MAX_BYTES + "B",
		"spring.codec.max-in-memory-size=" + ManagementCodecMaxSizeApplicationTests.SERVER_MAX_BYTES + "B"
})
@AutoConfigureWebTestClient
class ManagementCodecMaxSizeApplicationTests {

	static final int MANAGEMENT_MAX_BYTES= 2000;
	static final int SERVER_MAX_BYTES = 1000;

	WebTestClient managementWebTestClient;

	@LocalServerPort
	int serverPort;
	@LocalManagementPort
	int managementPort;

	@Autowired
	WebTestClient webTestClient;

	@BeforeEach
	void setUp() {
		managementWebTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + managementPort).build();
	}

	@Test
	void serverResponds500WhenServerMaxInMemorySizeIsExceeded() {
		var toEcho = generateRouteString(SERVER_MAX_BYTES + 1);

		this.webTestClient.post().uri("/echo")
						  .contentType(MediaType.APPLICATION_JSON)
						  .bodyValue(toEcho)
						  .exchange()
						  .expectStatus()
						  .isEqualTo(500);
	}

	@Test
	void serverRespondsOkWhenServerMaxInMemorySizeIsNotExceeded() {
		var toEcho = generateRouteString(SERVER_MAX_BYTES);

		this.webTestClient.post().uri("/echo")
						  .contentType(MediaType.APPLICATION_JSON)
						  .bodyValue(toEcho)
						  .exchange()
						  .expectStatus()
						  .isOk()
						  .expectBody(String.class).isEqualTo(toEcho);
	}

	@Test
	void managementServerResponds500WhenServerMaxInMemorySizeIsExceeded() throws JsonProcessingException {
		var toEcho = generateRouteString(MANAGEMENT_MAX_BYTES + 1);

		managementWebTestClient.post().uri("/actuator/gateway/routes/my-route-1-1")
						  .contentType(MediaType.APPLICATION_JSON)
						  .bodyValue(toEcho)
						  .exchange()
						  .expectStatus()
						  .isEqualTo(500);
	}

	@Test
	void managementServerRespondsOkWhenServerMaxInMemorySizeIsNotExceeded() {
		var toEcho = generateRouteString(MANAGEMENT_MAX_BYTES);

		managementWebTestClient.post().uri("/actuator/gateway/routes/my-route-1-2")
						  .contentType(MediaType.APPLICATION_JSON)
						  .bodyValue(toEcho)
						  .exchange()
						  .expectStatus()
						  .isCreated();
	}

	private String generateRouteString(int numBytes) {
		String routeString = """
		{
				    "predicates": [
				        {
				            "name": "Path",
				            "args": {
				                "_genkey_0": "/test/**"
				            }
				        }
				    ],
				    "filters": [
				        {
				            "name": "StripPrefix",
				            "args": {
				                "_genkey_0": "1"
				            }
				        }
				    ],
				    "uri": "http://httpbin.org",
				    "metadata": {
				        "groupBy": "1"
				    },
				    "order": 0,
				    "metadata": { "key": "%s"}
				}""";

		int currentLength = routeString.getBytes().length - "%s".getBytes().length;

		return routeString.formatted(generateString(numBytes - currentLength));
	}
	private String generateString(int numBytes) {
		Character[] array = new Character[numBytes];
		Arrays.fill(array, 'a');
		return StringUtils.arrayToDelimitedString(array, "");
	}
}
