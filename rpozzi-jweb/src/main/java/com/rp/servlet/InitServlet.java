package com.rp.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.rp.configuration.Configuration;
import com.rp.customer.service.CustomerRestClient;

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
		System.out.println("################### INIT SERVLET - START ###################");
		String jsonStr = new CustomerRestClient().getCustomersByProv("MI");
		System.out.println(jsonStr);
		JSONParser jsonParser = new JSONParser();
		JSONArray obj = null;
		try {
			obj = (JSONArray) jsonParser.parse(jsonStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		for (Iterator<JSONObject> iterator = obj.iterator(); iterator.hasNext();) {
			JSONObject jsonObj = (JSONObject) iterator.next();
			Set<String> jsonObjKeys = jsonObj.keySet();
		}
		System.out.println("################### INIT SERVLET - END ###################");
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