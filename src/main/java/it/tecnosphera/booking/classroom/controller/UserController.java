package it.tecnosphera.booking.classroom.controller;

import it.tecnosphera.booking.classroom.model.User;
import it.tecnosphera.booking.classroom.repository.UserRepository;
import it.tecnosphera.booking.classroom.repository.UserRepositoryInterface;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserController extends HttpServlet  {

	UserRepositoryInterface userRepository;

	
	
	protected User loadUserFromDatabase() {
		User user = userRepository.getUser(1L);
		return user;
	}
	
	public UserController() {
		userRepository = new UserRepository();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = loadUserFromDatabase();
		
		request.setAttribute("user", user);
		
		//RequestDispatcher dispatcher = request.getRequestDispatcher("/views/user/insert");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/views/user/edit.jsp");
		dispatcher.forward(request, response);
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    		User user = new User();
    		user.setId(Long.parseLong(request.getParameter("id")));
    		user.setName(request.getParameter("name"));
    		user.setCognome(request.getParameter("cognome"));
    		user.setEmail(request.getParameter("email"));
    		user.setPassword(request.getParameter("password"));
    		
    		userRepository.save(user);

    		PrintWriter out = response.getWriter();
    		RequestDispatcher dispatcher = request.getRequestDispatcher("/views/user/view.jsp");
    		dispatcher.forward(request, response);
	}
}
