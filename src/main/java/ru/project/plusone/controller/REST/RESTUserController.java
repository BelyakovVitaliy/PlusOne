package ru.project.plusone.controller.REST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.project.plusone.exception.UserException;
import ru.project.plusone.model.User;
import ru.project.plusone.service.interfaces.UserService;

@Controller
@RequestMapping("/")
public class RESTUserController {
	private final UserService userService;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public RESTUserController(UserService userService, PasswordEncoder passwordEncoder, PasswordEncoder passwordEncoder1) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder1;
	}

	@PostMapping(value = "register")
	public ResponseEntity register(@RequestBody User user, @RequestParam("newPassword") String newPassword,
	                               @RequestParam("repeatPassword") String repeatPassword) {
		if (!(newPassword.isEmpty() & repeatPassword.isEmpty())) {
			if (!newPassword.equals(repeatPassword)) {
				throw new UserException("New and repeated passwords are not equal!");
			}
			user.setPassword(passwordEncoder.encode(newPassword));
		} else
			throw new UserException("Both passwords have to be fulfilled");
		userService.addLocalUser(user);
		return ResponseEntity.ok().body("/login");
	}

	@PostMapping(value = "editUser/{id}")
	public ResponseEntity editUser(@PathVariable("id") Long userId, @RequestBody User user,
	                               @RequestParam("oldPassword") String oldPassword,
	                               @RequestParam("newPassword") String newPassword,
	                               @RequestParam("repeatPassword") String repeatPassword) {
		user.setId(userId);

		//Если есть заполненные и незаполненные пароля пароля, то выбросить исключение
		if (!((!oldPassword.isEmpty() && !newPassword.isEmpty() && !repeatPassword.isEmpty()) ||
				(oldPassword.isEmpty() && newPassword.isEmpty() && repeatPassword.isEmpty()))) {
			throw new UserException("All password fields have to be fulfilled");
		}
		User userFormDB = userService.get(userId);
		//Если все пароли заполнены ( нужно менять пароль)
		if(!(oldPassword.isEmpty() & newPassword.isEmpty() & repeatPassword.isEmpty())) {
			if (!newPassword.equals(repeatPassword)) {
				throw new UserException("New and repeated passwords are not equal!");
			}
			if (!passwordEncoder.matches(repeatPassword, userFormDB.getPassword())) {
				throw new UserException("Old password is incorrect");
			}
			user.setPassword(passwordEncoder.encode(newPassword));
		}else {
			//Если пароли не заполнены (не нужно менять пароль), то ставим старый
			user.setPassword(userFormDB.getPassword());
		}
		userService.updateLocalUser(user);
		return ResponseEntity.ok().body("/mainBoard");
	}
}

