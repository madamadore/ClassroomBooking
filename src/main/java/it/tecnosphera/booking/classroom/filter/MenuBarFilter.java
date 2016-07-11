package it.tecnosphera.booking.classroom.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.web.filter.OncePerRequestFilter;

import it.tecnosphera.booking.classroom.model.User;
import it.tecnosphera.booking.classroom.repository.UserRepositoryInterface;

public class MenuBarFilter extends OncePerRequestFilter {

	@Autowired
	private UserRepositoryInterface userDao;

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws ServletException, IOException {
		
		//valori di default
		req.setAttribute("isLogged", false);
		req.setAttribute("isAdmin", false);
		
		//verifica se è stata fatta la richiesta di logout, in tal caso non ci sono controlli da fare
		if (req.getRequestURL().toString().contains("logout")) {
			chain.doFilter(req, res);
			return;
		}
		
		//verifica se l'utente è loggato
		if (SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				&& !"anonymousUser".equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
			req.setAttribute("isLogged", true);
			
			//verifica se è admin
			if (new SecurityContextHolderAwareRequestWrapper(req, null).isUserInRole("ROLE_ADMIN")) {
				req.setAttribute("isAdmin", true);
			}
		}
		
		chain.doFilter(req, res);
	}
}
