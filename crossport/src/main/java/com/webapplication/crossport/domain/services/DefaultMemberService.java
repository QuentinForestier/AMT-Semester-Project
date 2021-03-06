package com.webapplication.crossport.domain.services;

import com.webapplication.crossport.infra.models.Cart;
import com.webapplication.crossport.infra.models.Member;
import com.webapplication.crossport.infra.repository.MemberRepository;
import com.webapplication.crossport.config.security.AuthService;
import com.webapplication.crossport.ui.dto.MemberRegistrationDTO;
import com.webapplication.crossport.config.security.RequestType;
import com.webapplication.crossport.config.security.exception.RegistrationException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service that handles user information during registration process
 *
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Gazetta Florian
 * @author Herzig Melvyn
 * @author Lamrani Soulaymane
 */
@Service("memberService")
public class DefaultMemberService implements MemberService
{

    /**
     * Repository to save user
     */
    @Autowired
    private MemberRepository memberRepository;

    /**
     * Tries to register the user
     * @param user User information received.
     * @throws RegistrationException Threw when the registration failed ( for example, username already used)
     */
    @Override
    public void register(MemberRegistrationDTO user) throws RegistrationException {

        JSONObject jsonObject = authenticate(user);

        Member member = new Member();
        member.setId(jsonObject.getInt("id"));
        member.setUsername(jsonObject.getString("username"));
        member.setCart(new Cart());

        memberRepository.save(member);
    }

    /**
     * This methode has been created for testing purpose. Make a test class inheriting from DefaultMemberService.
     * Thus, we can test authenticate with custom server address / port on testing time.
     * @param user Serialized received user registration information
     */
    public JSONObject authenticate (MemberRegistrationDTO user) throws RegistrationException{

        // basic check
        if(!user.getPasswordConfirmation().equals(user.getPassword())){
            throw  new RegistrationException("Password and confirmation are not equal");
        }

        // Send to auth
        String response = AuthService.getInstance().makeRequest(RequestType.REGISTER, user.getUsername(), user.getPassword());
        JSONObject jsonObject = new JSONObject(response);

        // Checking errors
        if( jsonObject.has("error") || jsonObject.has("errors")) {
            RegistrationException registrationException = new RegistrationException();

            if(jsonObject.has("error")) {
                registrationException.addError(jsonObject.getString("error"));
            }
            else {
                JSONArray errors = jsonObject.getJSONArray("errors");
                for (int i = 0; i < errors.length(); i++) {
                    JSONObject error = errors.getJSONObject(i);
                    registrationException.addError(error.getString("property") + " " + error.getString("message"));
                }
            }

            throw registrationException;
        }

        return jsonObject;
    }
}
