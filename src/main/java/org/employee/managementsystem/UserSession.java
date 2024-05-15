package org.employee.managementsystem;

import org.employee.managementsystem.model.User;
import org.employee.managementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class UserSession {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User user;

    public UserSession() {
    }

    public boolean isAdmin() {
        User dbuser = userRepository.findByUsername(getUser().getUsername());
        return dbuser != null && dbuser.getRoles().stream().anyMatch(role -> role.equals("ADMIN"));
    }

    public User getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        if (auth.getPrincipal() instanceof User) {
            return (User) auth.getPrincipal();
        } else {
            return userRepository.findByUsername(auth.getName());
        }
    }

    public void setUser(User user) {
        this.user = user;
    }
}
