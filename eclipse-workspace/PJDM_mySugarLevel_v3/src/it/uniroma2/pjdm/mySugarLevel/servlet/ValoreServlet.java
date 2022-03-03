package it.uniroma2.pjdm.mySugarLevel.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONObject;

import it.uniroma2.pjdm.muSugarLevel.dao.ValoreDAO;
import it.uniroma2.pjdm.muSugarLevel.dao.ValoreDAOJDBCImpl;
import it.uniroma2.pjdm.mySugarLevel.entity.Valore;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ValoreServlet
 */
public class ValoreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private ValoreDAO dao;

    public ValoreServlet() {}

	public void init() throws ServletException {
		String ip = getInitParameter("ip");
		String port = getInitParameter("port");
		String dbName = getInitParameter("dbName");
		String userName = getInitParameter("userName");
		String password = getInitParameter("password");	
	
		System.out.print("ValoreServlet. Opening DB connection...");
		
		dao = new ValoreDAOJDBCImpl(ip, port, dbName, userName, password);
		
		System.out.println("DONE.");
	}

	public void destroy() {
		System.out.print("ValoreServlet. Closing DB connection...");
		dao.closeConnection();
		System.out.println("DONE.");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("ValoreServlet. Invoking a doGet method.");
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("ValoreServlet. Invoking a doPost method.");
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


	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("ValoreServlet. Invoking a doDelete method.");
		if(request.getParameter("id_utente") == null || request.getParameter("data") == null || request.getParameter("orario") == null) {
			response.setStatus(500);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().append("Occorre specificare un id_utente, una data e un orario.");
		}
		
		int id_utente = Integer.valueOf(request.getParameter("id_utente"));
		String data = request.getParameter("data");
		String orario = request.getParameter("orario");
		
		Valore oldValore = dao.loadValore(id_utente, data, orario);
		String oldData;
		String[] parts = null;
		String[] newParts = null;
		
		/* Divido le date in array per fare bene il confronto giorno,mese,anno*/
		if(oldValore != null) {
			oldData = oldValore.getData();
			parts = oldData.split("-");
			newParts = data.split("/");
		}
		
		if(oldValore == null || !(oldValore.getId() == id_utente) || !(parts[0].equals(newParts[0])) || !(parts[1].equals(newParts[1])) || !(parts[2].equals(newParts[2]))|| !(oldValore.getOrario().equalsIgnoreCase(orario)) ) {
			response.setStatus(400);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().append("Purtroppo il seguente valore non e' nel DB");
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
