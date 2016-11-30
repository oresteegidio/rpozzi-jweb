package com.rp.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rp.configuration.Configuration;

/**
 * Servlet implementation class InitServlet
 */
@WebServlet(urlPatterns = { "/init" }, loadOnStartup = 1)
public class InitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InitServlet() {
        super();
    }

    @Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		InputStream inputStream = null;
		try {
			inputStream = config.getServletContext().getResourceAsStream("WEB-INF/config/config.properties");
			Properties props = new Properties();
			props.load(inputStream);
			Configuration.getInstance(props);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
    
    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("################### SAMPLE SERVLET - START ###################");
		System.out.println("################### SAMPLE SERVLET - END ###################");
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}

}