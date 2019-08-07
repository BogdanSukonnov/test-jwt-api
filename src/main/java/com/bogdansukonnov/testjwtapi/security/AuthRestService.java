package com.bogdansukonnov.testjwtapi.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.bogdansukonnov.testjwtapi.user.ApplicationUser;
import com.bogdansukonnov.testjwtapi.user.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static com.bogdansukonnov.testjwtapi.security.SecurityConstants.*;

@RestController
@RequestMapping(path = SIGN_UP_URL, method = RequestMethod.POST)
public class AuthRestService {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @RequestMapping(path = "/addUser", method = RequestMethod.POST)
    public ResponseEntity<?> addUser(@RequestParam String username, @RequestParam String password) {
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(username);
        if (applicationUser == null) {
            applicationUser = new ApplicationUser();
            applicationUser.setUsername(username);
            // TODO: hash password
            applicationUser.setPassword(password);
            applicationUserRepository.save(applicationUser);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(409).eTag("user already exist").build();
        }
    }

    @RequestMapping(path = "/checkUsername", method = RequestMethod.POST)
    public ResponseEntity<?> checkUsername(@RequestParam String username) {
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(username);
        if (applicationUser == null) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(409).eTag("user already exist").build();
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestParam String username, @RequestParam String password) throws AuthenticationException {
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(username);
        if (applicationUser == null) {
            return ResponseEntity.status(401).eTag("BAD CREDENTIALS").build();
        }

        String token = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRET));

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HEADER_STRING, TOKEN_PREFIX + token);

        return ResponseEntity.ok()
                .headers(responseHeaders).body("");

    }


}
