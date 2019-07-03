package ru.project.plusone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import ru.project.plusone.service.interfaces.*;

@Transactional
public class DataInit {

    @Autowired
    RoleService roleService;

    @Autowired
    UserService userService;

    @Autowired
    EventService eventService;

    @Autowired
    SocialNetworkTypeService socialNetworkTypeService;

    @Autowired
    TagService tagService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    //DB initialization
    public void init() {


    }
}
