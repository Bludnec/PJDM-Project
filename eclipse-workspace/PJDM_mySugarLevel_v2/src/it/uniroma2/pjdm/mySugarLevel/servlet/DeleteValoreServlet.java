package it.uniroma2.pjdm.mySugarLevel.servlet;

import java.io.IOException;

import it.uniroma2.pjdm.mySugarLevel.dao.DeleteValoreDAO;
import it.uniroma2.pjdm.mySugarLevel.dao.DeleteValoreDAOJDBCImpl;
import it.uniroma2.pjdm.mySugarLevel.dao.LoadValoreDAO;
import it.uniroma2.pjdm.mySugarLevel.dao.LoadValoreDAOJDBCImpl;
import it.uniroma2.pjdm.mySugarLevel.entity.Valore;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DeleteValoreServlet
 */
public class DeleteValoreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private DeleteValoreDAO dao;

	private LoadValoreDAO loadDao;
    /**
     * Default constructor. 
     */
    public DeleteValoreServlet() {}

	public void init() throws ServletException {
		String ip = getInitParameter("ip");
		String port = getInitParameter("port");
		String dbName = getInitParameter("dbName");
		String userName = getInitParameter("userName");
		String password = getInitParameter("password");	
	
		System.out.print("DeleteValoreServlet. Opening DB connection...");
		
		dao = new DeleteValoreDAOJDBCImpl(ip, port, dbName, userName, password);
		loadDao = new LoadValoreDAOJDBCImpl(ip, port, dbName, userName, password);
		
		System.out.println("DONE.");
	}

	public void destroy() {
		System.out.print("DeleteValoreServlet. Closing DB connection...");
		dao.closeConnection();
		System.out.println("DONE.");
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("DeleteValoreServlet. Invoking a doDelete method.");
		if(request.getParameter("id_utente") == null || request.getParameter("data") == null || request.getParameter("orario") == null) {
			response.setStatus(500);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().append("Occorre specificare un id_utente, una data e un orario.");
		}
		
		int id_utente = Integer.valueOf(request.getParameter("id_utente"));
		String data = request.getParameter("data");
		String orario = request.getParameter("orario");
		
		Valore oldValore = loadDao.loadValore(id_utente, data, orario);
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
