package ru.project.plusone.controller;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.OAuthException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.project.plusone.config.property.VkConfig;
import ru.project.plusone.model.Event;
import ru.project.plusone.model.SocialNetwork;
import ru.project.plusone.model.User;
import ru.project.plusone.service.interfaces.EventService;
import ru.project.plusone.service.interfaces.SocialNetworkTypeService;
import ru.project.plusone.service.interfaces.UserService;
import ru.project.plusone.service.interfaces.VKService;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;

@Controller
@RequestMapping("/")
public class MainController {

	private final EventService eventService;
	private final SocialNetworkTypeService socialNetworkTypeService;
	private final UserService userService;
	private VkApiClient vk;
	private VkConfig vkConfig;

	@Autowired
	public MainController(EventService eventService, VKService vkService, UserService userService, SocialNetworkTypeService socialNetworkTypeService, VkConfig vkConfig) {
		this.eventService = eventService;
		this.socialNetworkTypeService = socialNetworkTypeService;
		this.userService = userService;
		this.vkConfig = vkConfig;
	}

	@PostConstruct
	private void OAuthInit() {
		TransportClient transportClient = HttpTransportClient.getInstance();
		vk = new VkApiClient(transportClient);
	}

	@RequestMapping(value = "/signin", method = RequestMethod.GET)
	public String vkLogin() throws UnsupportedEncodingException {
		String encodedRedirectUrl  = URLEncoder.encode(vkConfig.getOAuth2RedirectUrl(), "UTF-8");
		return "redirect:https://oauth.vk.com/authorize?v=" + vkConfig.getVkApiVersion() + "&response_type=code&client_id=" +
				vkConfig.getVkApplicationId() + "&redirect_uri=" + encodedRedirectUrl + "&scope=email";
	}


	@RequestMapping("/getAccessToken")
	public ModelAndView callback(@RequestParam("code") String code, ModelAndView modelAndView, RedirectAttributes redir) {
		modelAndView.setViewName("mainBoard");
		try {
			UserAuthResponse authResponse = vk.oauth()
					.userAuthorizationCodeFlow(vkConfig.getVkApplicationId(), vkConfig.getVkApplicationClientSecret(),
							vkConfig.getOAuth2RedirectUrl(), code)
					.execute();
			UserActor actor = new UserActor(authResponse.getUserId(), authResponse.getAccessToken());
			JSONObject jsonUser = new JSONObject(vk.users().get(actor).executeAsString()).getJSONArray("response").getJSONObject(0);
			User user = new User();
			user.setFirstName(jsonUser.getString("first_name"));
			user.setLastName(jsonUser.getString("last_name"));
			SocialNetwork socialNetwork = new SocialNetwork(jsonUser.getString("id"), socialNetworkTypeService.getByName("VK"));
			user.setSocialNetworks(Collections.singleton(socialNetwork));
			user.setSocialId(jsonUser.getString("id"));
			user.setFromSocial(true);
			if(userService.getUserBySocialId(user.getSocialId()) == null) {
				if(userService.isRequiredFieldsValid(user)) {
					userService.addSocialUser(user);
				}else {
					redir.addFlashAttribute("user",user);
					modelAndView.setViewName("redirect:/registration");
				}
			}
			SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
			modelAndView.setViewName("redirect:/mainBoard");
		} catch (OAuthException e) {
			e.getRedirectUri();
		} catch (Exception e) {
			return modelAndView;
		}
		return modelAndView;
	}

	//TODO максимальный размер картинки в проперти
	@GetMapping(value = {"mainBoard"})
	public ModelAndView getMainPage(ModelAndView modelAndView) {
		modelAndView.setViewName("mainBoard");
		modelAndView.addObject("events", eventService.getAll());
		modelAndView.addObject("maxFileSize", 1048576);
		return modelAndView;
	}

	@GetMapping("addEvent")
	public ModelAndView addEvent(ModelAndView modelAndView) {
		modelAndView.setViewName("addEditEvent");
		modelAndView.addObject("event", new Event());
		modelAndView.addObject("maxFileSize", 1048576);
		modelAndView.addObject("action", "addNewEvent()");
		return modelAndView;
	}

	@GetMapping("editEvent/{id}")
	public ModelAndView editEvent(ModelAndView modelAndView, @PathVariable Long id, @AuthenticationPrincipal User user) {
		Event event = eventService.get(id);
		if (!event.getOwnerUser().equals(user)) {
			modelAndView.setStatus(HttpStatus.NOT_ACCEPTABLE);
			modelAndView.setViewName("mainBoard");
			return modelAndView;
		}
		modelAndView.setViewName("addEditEvent");
		modelAndView.addObject("maxFileSize", 1048576);
		modelAndView.addObject("action", "updateEvent()");
		modelAndView.addObject("event", event);
		return modelAndView;
	}

	@GetMapping(value = {"searchEvent"})
	public ModelAndView getSearchEventPage(ModelAndView modelAndView) {
		modelAndView.setViewName("KudaGo");
		return modelAndView;
	}

	@GetMapping(value = {"", "login"})
	public String homePage() {
		if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
			return "login";
		} else {
			return "redirect:/mainBoard";
		}
	}

	@GetMapping(value = "registration")
	public ModelAndView registerPage(ModelAndView modelAndView) {
		modelAndView.setViewName("registration");
		modelAndView.addObject("socials", socialNetworkTypeService.getAll());
		modelAndView.addObject("targetAction", "/register");
		return modelAndView;
	}

	@GetMapping(value = "editUser/{id}")
	public ModelAndView editUser(@PathVariable("id") Long id, ModelAndView modelAndView) {
		modelAndView.setViewName("registration");
		modelAndView.addObject("user", userService.get(id));
		modelAndView.addObject("socials", socialNetworkTypeService.getAll());
		modelAndView.addObject("targetAction", "/editUser/" + id);
		return modelAndView;
	}

	@GetMapping(value = "user/{id}")
	public ModelAndView userProfile(@PathVariable("id") Long id,ModelAndView modelAndView) {
		modelAndView.setViewName("userProfile");
		modelAndView.addObject(userService.get(id));
		return modelAndView;
	}
}
