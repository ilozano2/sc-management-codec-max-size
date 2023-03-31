package io.ilozano.lab.managementcodecmaxsize;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@RequestMapping(value = "/echo", method = RequestMethod.POST)
	String echo(@RequestBody String toEcho) {
		return toEcho;
	}
}
