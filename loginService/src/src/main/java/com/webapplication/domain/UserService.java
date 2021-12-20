package com.webapplication.domain;

import com.webapplication.infra.UserDAO;
import com.webapplication.infra.UserRepository;
import com.webapplication.ui.DTO.AccountResponse;
import com.webapplication.ui.DTO.LoginResponse;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.Objects;

@Service
public class UserService {
    @Value("${com.webapplication.loginCrossport.config.admin.username}")
    private String adminUsername;

    @Value("${com.webapplication.loginCrossport.config.admin.password}")
    private String adminPassword;

    @Value("${com.webapplication.loginCrossport.config.jwt.secret}")
    private String secret;

    @Autowired
    private UserRepository userRepository;

    // Creation de l'utilisateur admin
    @PostConstruct
    public void init() {
        UserDAO u = userRepository.findFirstByUsername(adminUsername);
        if (u == null) {
            createUser(adminUsername, adminPassword);
        }
    }

    public static boolean detectBlankInString(String message) {
        if (message.length() == 0)
            return true;

        System.out.println(message);
        for (int i = 0; i < message.length(); i++) {
            System.out.println(message.charAt(i));
            if (Character.isSpaceChar(message.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPasswordValid(String password) {
        if (password.length() < 8)
            return false;

        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#&()–\\[{}\\]_:;',?/*~$^+=<>]).*$");
    }

    public AccountResponse createUser(String userName, String password) {
        UserDAO u = new UserDAO(0L, userName, password);
        try {
            u = userRepository.save(u);
            return new AccountResponse(u.getId(), u.getUsername(), Role.User.toString());
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            return null;
        }
    }

    public LoginResponse login(String userName, String password) {
        UserDAO u = userRepository.findFirstByUsername(userName);

        if (u == null) {
            return null;
        }

        if (!Objects.equals(u.getPassword(), password)) {
            return null;
        }

        Role r = Objects.equals(u.getUsername(), adminUsername) ? Role.Admin : Role.User;

        return new LoginResponse(new AccountResponse(u.getId(), u.getUsername(), r.toString()), createJWT(r));
    }

    private String createJWT(Role role) {
        // signature algorithm
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // secret
        byte[] apiKeySecretBytes = secret.getBytes();
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        // set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(now)
                .setSubject("crossport")
                .setIssuer("crossport")
                .claim("role", role.toString())
                .signWith(signatureAlgorithm, signingKey)
                .setHeaderParam("typ", "JWT");

        // expiration
        long expMillis = nowMillis + 86400000;
        Date exp = new Date(expMillis);
        builder.setExpiration(exp);

        // Build
        return builder.compact();
    }
}