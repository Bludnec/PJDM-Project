package it.uniroma2.pjdm.mySugarLevel.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.json.JSONArray;

import it.uniroma2.pjdm.mySugarLevel.dao.ValoriDAO;
import it.uniroma2.pjdm.mySugarLevel.dao.ValoriDAOJDBCImpl;
import it.uniroma2.pjdm.mySugarLevel.entity.Valore;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ListaValoriUnaData
 */
public class ListaSingolaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private ValoriDAO dao;

    /**
     * Default constructor. 
     */
    public ListaSingolaServlet() {
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init() throws ServletException {
		String ip = getInitParameter("ip");
		String port = getInitParameter("port");
		String dbName = getInitParameter("dbName");
		String userName = getInitParameter("userName");
		String password = getInitParameter("password");	
	
		System.out.print("ListaSingolaServlet. Opening DB connection...");
		
		dao = new ValoriDAOJDBCImpl(ip, port, dbName, userName, password);
		
		System.out.println("DONE.");
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		System.out.print("ListaSingolaServlet. Closing DB connection...");
		dao.closeConnection();
		System.out.println("DONE.");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
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
