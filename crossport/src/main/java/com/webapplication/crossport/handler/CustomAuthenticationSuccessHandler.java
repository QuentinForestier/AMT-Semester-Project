package com.webapplication.crossport.handler;

import com.webapplication.crossport.models.Cart;
import com.webapplication.crossport.models.CartArticle;
import com.webapplication.crossport.models.Member;
import com.webapplication.crossport.models.repository.CartRepository;
import com.webapplication.crossport.models.repository.MemberRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

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
                                        Authentication authentication) throws IOException, ServletException
    {

        int memberId = (int) session.getAttribute("memberId");
        Member member = getMember(memberId);
        if (member == null) member = new Member();
        
        Cart cartInSession = Cart.getContextCart(session);
        Cart cartMember = member.getCart();

        cartMember.SyncCarts(cartInSession);

        cartRepository.save(cartMember);

        session.setAttribute("member", member);

        httpServletResponse.sendRedirect("/home");
    }

    private Member getMember(int id)
    {
        return memberRepository.findById(id).orElse(null);
    }
}
