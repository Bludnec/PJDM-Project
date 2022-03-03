package it.uniroma2.pjdm.mySugarLevel.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

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
 * Servlet implementation class ListaValoriServlet
 */
public class ListaValoriServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ValoriDAO dao;
	
    /**
     * Default constructor. 
     */
    public ListaValoriServlet() {
        // TODO Auto-generated constructor stub
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
	
		System.out.print("ListaValoriServlet. Opening DB connection...");
		
		dao = new ValoriDAOJDBCImpl(ip, port, dbName, userName, password);
		
		System.out.println("DONE.");	
	}
	

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("ListaValoriServlet. Invoking a doGet method.");
		if (request.getParameter("id_utente") == null || request.getParameter("data1") == null || request.getParameter("data2") == null ) {
			response.setStatus(400);
			response.getWriter().append("Occorre specificare due date.");
			
		} else {
			int id_utente = Integer.valueOf(request.getParameter("id_utente"));
			String data1 = request.getParameter("data1");
			String data2 = request.getParameter("data2");
		
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
		
			ArrayList<Valore> lista = dao.loadValoreByDate(id_utente,data1,data2);			
		
			if (lista == null) {
				response.setStatus(404);
				response.getWriter().append("Non esiste nessun valore tra le date " + data1 + " e " + data2);
				return;
			}
				JSONArray valoreJson = new JSONArray(lista);
				out.print(valoreJson.toString());
				out.flush();
		}		
	}
	
	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		System.out.print("ListaValoriServlet. Closing DB connection...");
		dao.closeConnection();
		System.out.println("DONE.");
	}

}
