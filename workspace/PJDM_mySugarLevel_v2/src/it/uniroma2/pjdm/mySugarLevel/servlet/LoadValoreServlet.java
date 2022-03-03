package it.uniroma2.pjdm.mySugarLevel.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONObject;

import it.uniroma2.pjdm.mySugarLevel.dao.*;
import it.uniroma2.pjdm.mySugarLevel.entity.Valore;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LoadValoreServlet
 */
public class LoadValoreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private LoadValoreDAO dao;
    /**
     * Default constructor. 
     */
    public LoadValoreServlet() {}
	public void init() throws ServletException {
		String ip = getInitParameter("ip");
		String port = getInitParameter("port");
		String dbName = getInitParameter("dbName");
		String userName = getInitParameter("userName");
		String password = getInitParameter("password");	
	
		System.out.print("LoadValoreServlet. Opening DB connection...");
		
		dao = new LoadValoreDAOJDBCImpl(ip, port, dbName, userName, password);
		

		System.out.println("DONE.");
	}

	public void destroy() {
		System.out.print("LoadValoreServlet. Closing DB connection...");
		dao.closeConnection();
		System.out.println("DONE.");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("LoadValoreServlet. Invoking a doGet method.");
		if (request.getParameter("id_utente") == null || request.getParameter("data") == null || request.getParameter("orario") == null) {
			response.setStatus(400);
			response.getWriter().append("Occorre specificare un id_utente, una data e un orario.");
			
		} else {

			int id_utente = Integer.valueOf(request.getParameter("id_utente"));
			String data = request.getParameter("data");
			String orario = request.getParameter("orario");
		
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
		
			Valore valore = dao.loadValore(id_utente, data, orario);
		
			if (valore == null) {
				response.setStatus(404);
				response.getWriter().append("Non esiste un valore");
				return;
			}
				JSONObject valoreJson = new JSONObject(valore);
				out.print(valoreJson.toString());
				out.flush();
		}
	}
}
