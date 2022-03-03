package it.uniroma2.pjdm.mySugarLevel.servlet;

import java.io.IOException;

import it.uniroma2.pjdm.mySugarLevel.dao.*;
import it.uniroma2.pjdm.mySugarLevel.entity.Valore;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class InsertValoreServlet
 */
public class InsertValoreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private InsertValoreDAO dao;
	
    public InsertValoreServlet() {}

	public void init() throws ServletException {
		String ip = getInitParameter("ip");
		String port = getInitParameter("port");
		String dbName = getInitParameter("dbName");
		String userName = getInitParameter("userName");
		String password = getInitParameter("password");	
	
		System.out.print("InsertValoreDAO. Opening DB connection...");
		
		dao = new InsertValoreDAOJDBCImpl(ip, port, dbName, userName, password);

		System.out.println("DONE.");
	}

	public void destroy() {
		System.out.print("InsertValoreDAO. Closing DB connection...");
		dao.closeConnection();
		System.out.println("DONE.");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("InsertValoreDAO. Invoking a doPost method.");
		if (request.getParameter("id_utente") == null || request.getParameter("data") == null || request.getParameter("orario") == null || request.getParameter("valore") == null || request.getParameter("insulina") == null || request.getParameter("cibo") == null) {
			response.setStatus(500);
			response.getWriter().append("Occorre specificare un id_utente, una data, un orario, un valore, e se hai fatto l'insulina o hai mangiato.");
			
		} else {
			
			int id_utente = Integer.valueOf(request.getParameter("id_utente"));
			String data = request.getParameter("data");
			String orario = request.getParameter("orario");
			int valore = Integer.valueOf(request.getParameter("valore"));
			int insulina = Integer.valueOf(request.getParameter("insulina"));
			int cibo = Integer.valueOf(request.getParameter("cibo"));
			
			Valore val = new Valore(id_utente,data,orario,valore,insulina,cibo);
			
			int res = dao.insertValore(val);
			
			if(res > 0) {
				response.setStatus(200);
				response.getWriter().append("Inserimento andato a buon fine.");
			} else if (res == -2){
				response.setStatus(300);
				response.getWriter().append("Valore duplicato.");
			} else {
				response.setStatus(500);
				response.getWriter().append("Inserimento non andato a buon fine.");
			}
		}
	}
}
