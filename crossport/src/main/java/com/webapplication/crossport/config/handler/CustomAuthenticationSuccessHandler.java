package com.webapplication.crossport.config.handler;

import com.webapplication.crossport.infra.models.Cart;
import com.webapplication.crossport.infra.models.Member;
import com.webapplication.crossport.infra.repository.CartRepository;
import com.webapplication.crossport.infra.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 *
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Gazetta Florian
 * @author Herzig Melvyn
 * @author Lamrani Soulaymane
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler
{
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    HttpSession session;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException
    {
        int memberId = (int) session.getAttribute("memberId");
        Member member = getMember(memberId);
        if (member == null) member = new Member();
        
        Cart cartInSession = Cart.getContextCart(session);
        Cart cartMember = member.getCart();

        cartMember.SyncCarts(cartInSession);

        cartRepository.save(cartMember);

        session.setAttribute("member", member);

        httpServletResponse.addCookie(new Cookie("jwt", authentication.getCredentials().toString()));
        httpServletResponse.sendRedirect("/home");
    }

    private Member getMember(int id)
    {
        return memberRepository.findById(id).orElse(null);
    }
}
