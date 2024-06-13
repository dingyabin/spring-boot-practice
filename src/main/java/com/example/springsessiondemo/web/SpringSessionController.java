
package com.example.springsessiondemo.web;

import com.example.springsessiondemo.annotation.HasRole;
import com.example.springsessiondemo.annotation.NoNeedLogin;
import com.example.springsessiondemo.context.UserContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
@RestController
public class SpringSessionController {

    @Resource
    private HttpServletRequest httpServletRequest;


    @NoNeedLogin
    @PostMapping(value = "/login")
    public String login() {
        User user = new User();
        user.setName("xxx");
        user.setAge(10L);
        user.setRoles(Arrays.asList("admin", "dev"));

        httpServletRequest.getSession().setAttribute("user", user);
        return "ok";
    }


    @HasRole(hasRoles = {"admin", "boss"}, checkType = HasRole.HasRoleCheckType.HAS_ROLES_AND)
    @PostMapping(value = "/get")
    public User get() {
        User currentUser = UserContext.getCurrentUser();
        if (currentUser != null) {
            System.out.println(currentUser);
            return currentUser;
        }
        return new User();
    }


    @NoNeedLogin
    @PostMapping(value = "/noNeedLogin")
    public String testNoNeedLogin() {
        return "noNeedLogin ok";
    }

}
