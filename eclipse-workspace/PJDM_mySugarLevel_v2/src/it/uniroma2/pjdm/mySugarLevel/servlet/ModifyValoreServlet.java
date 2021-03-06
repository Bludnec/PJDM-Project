package it.uniroma2.pjdm.mySugarLevel.servlet;

import java.io.IOException;

import it.uniroma2.pjdm.mySugarLevel.dao.*;
import it.uniroma2.pjdm.mySugarLevel.entity.Valore;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ModifyValoreServlet
 */
public class ModifyValoreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ModifyValoreDAO dao;
	private LoadValoreDAO loadDao;
    /**
     * Default constructor. 
     */
    public ModifyValoreServlet() {}

	public void init() throws ServletException {
		String ip = getInitParameter("ip");
		String port = getInitParameter("port");
		String dbName = getInitParameter("dbName");
		String userName = getInitParameter("userName");
		String password = getInitParameter("password");	
	
		System.out.print("ModifyValoreServlet. Opening DB connection...");
		
		dao = new ModifyValoreDAOJDBCImpl(ip, port, dbName, userName, password);
		loadDao = new LoadValoreDAOJDBCImpl(ip, port, dbName, userName, password);
		
		System.out.println("DONE.");
	}

	public void destroy() {
		System.out.print("ModifyValoreServlet. Closing DB connection...");
		dao.closeConnection();
		System.out.println("DONE.");
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("ModifyValoreServlet. Invoking a doPut method...");
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
		
		Valore val = loadDao.loadValore(oldId_utente, oldData, oldOrario);
		
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
}
