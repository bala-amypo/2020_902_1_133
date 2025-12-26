import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/status")
public class SimpleStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Store server start time
    private final Instant startTime = Instant.now();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json"); // JSON response
        response.setStatus(HttpServletResponse.SC_OK); // HTTP 200 OK

        long uptimeSeconds = java.time.Duration.between(startTime, Instant.now()).getSeconds();

        String jsonResponse = String.format(
            "{ \"status\": \"running\", \"uptime_seconds\": %d }",
            uptimeSeconds
        );

        PrintWriter out = response.getWriter();
        out.println(jsonResponse);
        out.close();
    }
}
