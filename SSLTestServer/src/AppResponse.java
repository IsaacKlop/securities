

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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
            int length = request.getContentLength();
            byte[] input = new byte[length];
            ServletInputStream sin = request.getInputStream();
            sin.read(input);
            sin.close();
 
            String receivedString = new String(input);
            response.setStatus(HttpServletResponse.SC_OK);
            
            PrintWriter writer = new PrintWriter("//var//lib//tomcat7//webapps//SSLTestServer//log.txt", "UTF-8");
    		writer.println(receivedString + "\n");
    		writer.close();
                        
        } catch (IOException e) {
 
            try{
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().print(e.getMessage());
                response.getWriter().close();
            } catch (IOException ioe) {
            }
        }   
	}
}
