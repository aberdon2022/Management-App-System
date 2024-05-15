package org.employee.managementsystem;

import jakarta.annotation.PostConstruct;
import org.employee.managementsystem.model.Employee;
import org.employee.managementsystem.model.User;
import org.employee.managementsystem.repository.EmployeeRepository;
import org.employee.managementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        User user1 = new User("user1", passwordEncoder.encode("1"), "USER");
        User user2 = new User("user2", passwordEncoder.encode("2"), "USER");
        User admin = new User("admin", passwordEncoder.encode("admin"), "ADMIN");

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(admin);
    }

    public boolean checkPassword (User user, String password) {
        User dbuser = userRepository.findByUsername(user.getUsername());
        if (dbuser != null) {
            return passwordEncoder.matches(password, dbuser.getPassword());
        } else {
            return false;
        }
    }

    public User registerUser (String username, String password) {
        User user = new User(username, passwordEncoder.encode(password));

        if (userRepository.findByUsername(user.getUsername()) == null) {
            user.setRoles(Collections.singletonList("USER"));
            return userRepository.save(user);
        } else {
            return null;
        }
    }

    public List<Employee> getAllUsers() {
        return employeeRepository.findAll();
    }

    public void addEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    public User loginUser (String username, String password) throws Exception {
        User user = userRepository.findByUsername(username);

        if (user != null && checkPassword(user, password)) {
            return user;
        } else {
            throw new Exception("Invalid username or password");
        }
    }
}
