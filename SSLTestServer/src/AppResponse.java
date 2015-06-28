

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/AppResponse")
public class AppResponse extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AppResponse() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		writer.println("Sup");
	}

	protected void processRequest (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter writer = response.getWriter();
		try {
			String username, password;
			username = request.getParameter("username");
			password = request.getParameter("password");
			if (username.equalsIgnoreCase("username") && password.equals("test")) {
				writer.print(true);
			} else {
				writer.print(false);
			}
		} finally {
			writer.close();
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		processRequest(request, response);
	}
}
