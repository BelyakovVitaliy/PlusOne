package ru.project.plusone.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.project.plusone.exception.UserException;
import ru.project.plusone.model.SocialNetwork;
import ru.project.plusone.model.SocialNetworkType;
import ru.project.plusone.model.User;
import ru.project.plusone.repository.interfaces.UserRepository;
import ru.project.plusone.service.interfaces.SocialNetworkTypeService;
import ru.project.plusone.service.interfaces.UserService;
import ru.project.plusone.service.interfaces.VKService;
import ru.project.plusone.tool.RegexpPatterns;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final VKService vkService;
	private final SocialNetworkTypeService socialNetworkTypeService;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, VKService vkService, SocialNetworkTypeService socialNetworkTypeService) {
		this.userRepository = userRepository;
		this.vkService = vkService;
		this.socialNetworkTypeService = socialNetworkTypeService;
	}

	@Override
	public User getLocalUserByEmail(String email) {
		return userRepository.getUserByisFromSocialFalseAndEmail(email);
	}

	@Override
	public User getUserByEmail(String email) {
		return userRepository.getUserByEmail(email);
	}

	@Override
	public User get(Long id) {
		return userRepository.getById(id);
	}

	@Override
	public User getUserBySocialId(String id) {
		return userRepository.getUserBySocialId(id);
	}

	@Override
	public void addLocalUser(User user) {
		if (userRepository.getUserByisFromSocialFalseAndEmail(user.getEmail()) != null) {
			throw new UserException("User with this email is already exists");
		}
		checkValidityForLocalUser(user);
		userRepository.saveAndFlush(user);
	}

	@Override
	public void addSocialUser(User user) {
		if (userRepository.getUserBySocialId(user.getSocialId()) != null) {
			throw new UserException("User with this social id is already exists");
		}
		checkUserSocials(user);
		userRepository.saveAndFlush(user);

	}

	private void checkValidityForLocalUser(User user) {
		checkRequiredFields(user);
		if (user.getEmail().isEmpty()) {
			throw new UserException("E-mail is required");
		}
		Pattern pattern = Pattern.compile(RegexpPatterns.EMAIL_PATTERN);
		if (!pattern.matcher(user.getEmail()).matches()) {
			throw new UserException("Check e-mail's format");
		}
		checkUserSocials(user);
	}

	private void checkRequiredFields(User user) {
		if (user.getFirstName().isEmpty()) {
			throw new UserException("First name is required");
		}
	}

	@Override
	public boolean isRequiredFieldsValid (User user) {
		try {
			checkRequiredFields(user);
		}catch (UserException e) {
			return false;
		}
		return true;
	}

	private void checkValidityForSocialUser(User user) {
		checkRequiredFields(user);
		if (user.getSocialId().isEmpty()) {
			throw new UserException("Social id is required for authorisation via social network");
		}
	}

	private void checkUserSocials(User user) {
		List<String> userSnt = user.getSocialNetworks().stream().map(e -> e.getSocialNetworkType().getName()).collect(Collectors.toList());
		HashSet<String> SNTWithoutDiplicates = new HashSet<>(userSnt);
		if (userSnt.size() != SNTWithoutDiplicates.size()) {
			throw new UserException("Допустимо использовать только одну ссылку для одной социальной сети");
		}

		List<SocialNetworkType> SNTypes = new ArrayList<>(socialNetworkTypeService.getAll());
		for (SocialNetwork s : user.getSocialNetworks()) {
			if (!SNTypes.contains(s.getSocialNetworkType())) {
				throw new UserException("Social network type " + s.getSocialNetworkType().getName() + "is not exists");
			}

			//TODO слишком тупо
			for (SocialNetworkType snt : SNTypes) {
				if (snt.equals(s.getSocialNetworkType())) {
					s.setSocialNetworkType(snt);
				}
			}
			SocialNetworkType snType = s.getSocialNetworkType();
			Pattern pattern = Pattern.compile(snType.getRegExp());
			String socialLink = s.getLink();
			if (!pattern.matcher(s.getLink()).matches()) {
				socialLink = vkService.getAnotherSocialLinkFormat(s.getLink());
			}
			if (socialLink == null) {
				throw new UserException("Social link is not valid");
			}
		}
	}

	@Override
	public void delete(User user) {
		userRepository.delete(user);
	}

	@Override
	public void updateLocalUser(User user) {
		User userFromDB = userRepository.getUserByisFromSocialFalseAndEmail(user.getEmail());
		if (userFromDB != null && !userFromDB.getId().equals(user.getId())) {
			throw new UserException("User with this email is already exists");
		}
		checkValidityForLocalUser(user);
		userRepository.saveAndFlush(user);
	}

	@Override
	public void updateSocialUser(User user) {
		User userFromDB = userRepository.getUserBySocialId(user.getSocialId());
		if (userFromDB != null && !userFromDB.getId().equals(user.getId())) {
			throw new UserException("User with this social id is already exists");
		}
		checkValidityForSocialUser(user);
		userRepository.saveAndFlush(user);
	}

	@Override
	public void deleteById(Long id) {
		userRepository.deleteById(id);
	}

	@Override
	public Set<User> getAll() {
		return new HashSet<>(userRepository.findAll());
	}
}
