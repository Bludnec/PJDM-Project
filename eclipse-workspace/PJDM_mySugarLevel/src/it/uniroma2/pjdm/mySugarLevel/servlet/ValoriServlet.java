package it.uniroma2.pjdm.mySugarLevel.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONObject;

import it.uniroma2.pjdm.mySugarLevel.dao.*;
import it.uniroma2.pjdm.mySugarLevel.entity.Valore;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ValoriServlet
 */
public class ValoriServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ValoriDAO dao;
	
    /**
     * Default constructor. 
     */
    public ValoriServlet() {
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
	
		System.out.print("ValoriServlet. Opening DB connection...");
		
		dao = new ValoriDAOJDBCImpl(ip, port, dbName, userName, password);
		

		System.out.println("DONE.");
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		System.out.print("ValoriServlet. Closing DB connection...");
		dao.closeConnection();
		System.out.println("DONE.");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("ValoriServlet. Invoking a doGet method.");
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
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("id_utente") == null || request.getParameter("data") == null || request.getParameter("orario") == null || request.getParameter("valore") == null || request.getParameter("insulina") == null || request.getParameter("cibo") == null) {
			response.setStatus(500);
			response.getWriter().append("Occorre specificare un id_utente, una data, un orario, un valore, e se hai fatto l'insulina o hai mangiato.");
			
		} else {
			/*if(Integer.valueOf(request.getParameter("insulina")) != 0 || Integer.valueOf(request.getParameter("insulina")) != 1 || Integer.valueOf(request.getParameter("cibo")) != 1 || Integer.valueOf(request.getParameter("cibo")) != 0 ) {
				response.setStatus(501);
				response.getWriter().append("insulina e cibo sbagliato")
			}*/
			
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
	

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("ValoriSerlvet. Invoking a doPut method...");
		if(request.getParameter("oldId_utente") == null || request.getParameter("oldData") == null || request.getParameter("oldOrario") == null) {
			response.setStatus(500);
			response.getWriter().append("Occorre specificare un id_utente, una data e un orario.");
			return;
		}
		
		if(request.getParameter("orario") == null || request.getParameter("valore") == null || request.getParameter("insulina") == null || request.getParameter("cibo") == null) {
			response.setStatus(500);
			response.getWriter().append("Occorre specificare un nuovo orario,valore,insulina e cibo");
			return;
		}
		
		int oldId_utente = Integer.valueOf(request.getParameter("oldId_utente"));
		String oldData = request.getParameter("oldData");
		String oldOrario = request.getParameter("oldOrario");
		
		Valore val = dao.loadValore(oldId_utente, oldData, oldOrario);
		
		String orario = request.getParameter("orario");
		int valore = Integer.valueOf(request.getParameter("valore"));
		int insulina = Integer.valueOf(request.getParameter("insulina"));
		int cibo = Integer.valueOf(request.getParameter("cibo"));
		
		boolean isOk = dao.modifyValore(val, orario, valore, insulina, cibo);
		
		if (isOk) {
			response.setStatus(200);
			response.getWriter().append("Modifica andata a buon fine.");
		} else {
			response.setStatus(500);
			response.getWriter().append("Modifica non andata a buon fine.");
		}
	
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("ValoriServlet. Invoking a doDelete method.");
		if(request.getParameter("id_utente") == null || request.getParameter("data") == null || request.getParameter("orario") == null) {
			response.setStatus(500);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().append("Occorre specificare un id_utente, una data e un orario.");
		}
		
		int id_utente = Integer.valueOf(request.getParameter("id_utente"));
		String data = request.getParameter("data");
		String orario = request.getParameter("orario");
		
		Valore oldValore = dao.loadValore(id_utente, data, orario);
		if(oldValore == null || !(oldValore.getId() == id_utente) || !(oldValore.getData().equalsIgnoreCase(data)) || !(oldValore.getOrario().equalsIgnoreCase(orario)) ) {
			response.setStatus(400);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().append("Purtroppo il seguente valore non è nel DB");
			return;
		}
		
		boolean isOk = dao.deleteValore(oldValore);
		
		if(isOk) {
			response.setStatus(200);
			response.getWriter().append("Cancellamento del valore andato a buon fine.");
		} else {
			response.setStatus(500);
			response.getWriter().append("Cancellamento del valore non andato a buon fine.");
		}
	}

}
