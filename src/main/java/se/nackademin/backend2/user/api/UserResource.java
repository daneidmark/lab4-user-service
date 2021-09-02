package se.nackademin.backend2.user.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.nackademin.backend2.user.application.SignupService;
import se.nackademin.backend2.user.domain.User;

@RestController
public class UserResource {
    private static final Logger LOG = LoggerFactory.getLogger(UserResource.class);
    private final SignupService signupService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserResource(final SignupService signupService, final PasswordEncoder passwordEncoder) {
        this.signupService = signupService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/user/signup")
    public void signUp(@RequestBody SignUpRequestDto signUpRequest) {
        LOG.info("Creating user {}", signUpRequest.getUsername());
        signupService.signUp(signUpRequest.getUsername(), passwordEncoder.encode(signUpRequest.getPassword()), signUpRequest.getRoles());
    }

    @GetMapping("/admin/hello")
    public String helloAdmin(@AuthenticationPrincipal User user) {
        return "hello, " + user.getUsername();
    }

    @GetMapping("/customer/hello")
    public String helloCustomer(@AuthenticationPrincipal User user) {
        return "hello, " + user.getUsername();
    }
}
