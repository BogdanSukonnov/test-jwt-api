package com.bogdansukonnov.testjwtapi.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.bogdansukonnov.testjwtapi.user.ApplicationUser;
import com.bogdansukonnov.testjwtapi.user.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.bogdansukonnov.testjwtapi.security.SecurityConstants.*;

@RestController
@RequestMapping(path = SIGN_UP_URL, method = RequestMethod.POST)
public class AuthRestService {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @RequestMapping(path = "/addUser", method = RequestMethod.POST)
    public ResponseEntity<?> addUser(@Valid @RequestBody UsernamePasswordRequest usrPassReq) {
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(usrPassReq.getUsername());
        if (applicationUser != null) {
            Map<String, String> fields = new HashMap<String, String>();
            fields.put("username", "user is already exist");
            ErrorBody errorBody = new ErrorBody(HttpStatus.CONFLICT, fields);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorBody);            
        } else {
            applicationUser = new ApplicationUser();
            applicationUser.setUsername(usrPassReq.getUsername());
            // TODO: hash password
            applicationUser.setPassword(usrPassReq.getPassword());
            applicationUserRepository.save(applicationUser);
            return ResponseEntity.ok().body(new OkBody("user added"));
        }
    }

    @PostMapping("/checkUsername")
    public ResponseEntity<?> checkUsername(@Valid @RequestBody UsernameRequest usrReq) {
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(usrReq.getUsername());
        if (applicationUser == null) {
            return ResponseEntity.ok().body(new OkBody("username is available"));
        } else {
            Map<String, String> fields = new HashMap<String, String>();
            fields.put("username", "user is already exist");
            ErrorBody errorBody = new ErrorBody(HttpStatus.CONFLICT, fields);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorBody);
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(
            @Valid @RequestBody UsernamePasswordRequest usrPassReq) throws AuthenticationException {
        ApplicationUser applicationUser = applicationUserRepository.findByUsernameAndPassword(
                usrPassReq.getUsername(), usrPassReq.getPassword());
        if (applicationUser == null) {
            Map<String, String> fields = new HashMap<String, String>();
            fields.put("username password", "username or password is incorrect");
            ErrorBody errorBody = new ErrorBody(HttpStatus.UNAUTHORIZED, fields);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorBody);
        }

        String token = JWT.create()
                .withSubject(usrPassReq.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRET));

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HEADER_STRING, TOKEN_PREFIX + token);

        return ResponseEntity.ok()
                .headers(responseHeaders).body(new OkBody("user authorized"));

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorBody handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> fields = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fields.put(fieldName, errorMessage);
        });
        return new ErrorBody(HttpStatus.BAD_REQUEST, fields);
    }

}
