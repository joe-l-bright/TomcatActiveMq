
import org.apache.catalina.User;
import org.apache.catalina.users.MemoryUser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class SenderController extends HttpServlet {

    private String message;

    public void init() throws ServletException {
        // Do required initialization
        message = "Message Sender";
    }

    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Set response content type
        response.setContentType("text/html");
        String message = this.message + username + password;
        new MessageSender(message);

        // Actual logic goes here.
        PrintWriter out = response.getWriter();
        out.println("<h1>" + message + "</h1>");
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {
        // Set response content type
        response.setContentType("text/html");

        // Actual logic goes here.
        PrintWriter out = response.getWriter();
        out.println("<h1>" + message + "</h1>" +
                "<form method=\"post\">\n" +
                "    <input type=\"text\" name=\"username\">\n" +
                "    <input type=\"password\" name=\"password\">\n" +
                "    <input type=\"submit\" value=\"login\">\n"+
                "</form>");
    }

    public void destroy() {
        // do nothing.
    }
}
