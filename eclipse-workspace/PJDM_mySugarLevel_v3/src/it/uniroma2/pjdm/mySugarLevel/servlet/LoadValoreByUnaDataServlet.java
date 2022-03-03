package it.uniroma2.pjdm.mySugarLevel.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.json.JSONArray;

import it.uniroma2.pjdm.muSugarLevel.dao.LoadValoreByUnaDataDAO;
import it.uniroma2.pjdm.muSugarLevel.dao.LoadValoreByUnaDataDAOJDBCImpl;
import it.uniroma2.pjdm.mySugarLevel.entity.Valore;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LoadValoreByUnaDataServlet
 */
public class LoadValoreByUnaDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private LoadValoreByUnaDataDAO dao;

    /**
     * Default constructor. 
     */
    public LoadValoreByUnaDataServlet() {}

	public void init() throws ServletException {
		String ip = getInitParameter("ip");
		String port = getInitParameter("port");
		String dbName = getInitParameter("dbName");
		String userName = getInitParameter("userName");
		String password = getInitParameter("password");	
	
		System.out.print("LoadValoreByUnaDataServlet. Opening DB connection...");
		
		dao = new LoadValoreByUnaDataDAOJDBCImpl(ip, port, dbName, userName, password);
		
		System.out.println("DONE.");
	}

	public void destroy() {
		System.out.print("LoadValoreByUnaDataServlet. Closing DB connection...");
		dao.closeConnection();
		System.out.println("DONE.");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("ListaSingolaServlet. Invoking a doGet method.");
		if (request.getParameter("id_utente") == null || request.getParameter("data") == null ) {
			response.setStatus(400);
			response.getWriter().append("Occorre specificare una data.");
			
		} else {
			int id_utente = Integer.valueOf(request.getParameter("id_utente"));
			String data = request.getParameter("data");
		
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
		
			ArrayList<Valore> lista = dao.loadValoreByUnaData(id_utente,data);			
		
			if (lista == null) {
				response.setStatus(404);
				response.getWriter().append("Non esiste nessun valore nella data selezionata.");
				return;
			}
				JSONArray valoreJson = new JSONArray(lista);
				out.print(valoreJson.toString());
				out.flush();
		}		
	}
}
