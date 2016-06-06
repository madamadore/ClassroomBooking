package it.tecnosphera.booking.classroom.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.tecnosphera.booking.classroom.model.Aula;

public class AulaController extends HttpServlet  {

	protected Aula loadAulaFromDatabase() {
		Aula aula = new Aula();
		aula.setId(1L);
		aula.setName("Aula Corsi");
		aula.setDescription("Aula al sesto piano, San Donato (MI)");
		return aula;
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Aula aula = loadAulaFromDatabase();
		
		request.setAttribute("aula", aula);
		String pageContext = request.getContextPath();
		RequestDispatcher dispatcher = request.getRequestDispatcher("/views/aula/edit.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    		Aula aula = new Aula();
    		aula.setId(Long.parseLong(request.getParameter("id")));
    		aula.setName(request.getParameter("name"));
    		aula.setDescription(request.getParameter("description"));
    		
    		PrintWriter out = response.getWriter();
    		RequestDispatcher dispatcher = request.getRequestDispatcher("/views/aula/view.jsp");
    		dispatcher.forward(request, response);
	}
}
