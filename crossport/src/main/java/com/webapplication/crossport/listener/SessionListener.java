package com.webapplication.crossport.listener;

import com.webapplication.crossport.models.Cart;
import com.webapplication.crossport.models.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Component
@WebListener
public class SessionListener implements HttpSessionListener
{
    @Autowired
    private CartService cartService;

    @Override
    public void sessionCreated(HttpSessionEvent event)
    {
        HttpSession session = event.getSession();

        System.out.println("New session created  : " + session.getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event)
    {
        HttpSession session = event.getSession();

        System.out.println("Session destroyed  : " + session.getId());
    }
}