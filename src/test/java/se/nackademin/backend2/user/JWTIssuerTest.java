package se.nackademin.backend2.user;

import org.junit.jupiter.api.Test;
import se.nackademin.backend2.user.domain.User;
import se.nackademin.backend2.user.security.JWTIssuer;

import java.util.Base64;

class JWTIssuerTest {

    @Test
    public void canIssueToken() {
      /*  JWTIssuer jwtIssuer = new JWTIssuer();
        String token = jwtIssuer.generateToken(new User("username", "password", List.of("ADMIN")));
        System.out.println(token);
        byte[] decode = Base64.getDecoder().decode(token.getBytes());
        System.out.println(new String(decode));*/
    }
}