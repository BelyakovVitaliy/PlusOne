package ru.project.plusone.controller.REST;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/rest/")
public class RESTMainController {

	@GetMapping(value = "pic/{link}")
	public byte[] getPicture(@PathVariable("link") String link) throws IOException {
		Path fileLocation = Paths.get("C://plusOne//pictures//" + link);
		return Files.readAllBytes(fileLocation);
	}
}
