package com.rp.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Servlet implementation class UploadServlet
 */
@WebServlet("/upload")
@MultipartConfig
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(UploadServlet.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadServlet() {
        super();
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
	
	private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("################### UPLOAD SERVLET - START ###################");
		InputStream in = null;
		try {
			/* ========================== GET User Principal - START */
			String userName = null;
			try {
				userName = request.getUserPrincipal().getName();
				System.out.println("############# USER AUTHENTICATED = " + userName);
			} catch (NullPointerException e) {
				/* ############################# UNCOMMENT WHEN AUTHENTICATION FIXED - START */
				if (userName == null) {
					System.out.println("################## USERNAME IS NULL, set to italy");
					userName = "italy";
				}
				/* ############################# UNCOMMENT WHEN AUTHENTICATION FIXED - END */
			}
			/* ========================== GET User Principal - END */
			/* ########################## UPLOAD FILE - START */
			Part uploadedFile = request.getPart("file");
			String fileName = null;
			try {
				fileName = uploadedFile.getSubmittedFileName();
			} catch (Exception e1) {
				//fileName = Configuration.DEFAULT_UPLOAD_FILENAME;
			}
			fileName = fileName.substring(0, fileName.indexOf("."));
			System.out.println("Submitted (stripped) FileName = " + fileName);
			long length = uploadedFile.getSize();
			byte[] bytes = new byte[(int) length];
			in = uploadedFile.getInputStream();
			in.read(bytes);
			/*OpenStackClient os = new OpenStackClient();
			os.upload(in);*/
			/* ########################## UPLOAD FILE - END */
			/* ########################## BUILD JSON RESPONSE - START */
			/*String clientId = ((ClientApplication) configuration.get(userName)).getClientId();
			System.out.println("############# LOGGED USER = " + userName + " - " + clientId);
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("docId", dbResponse.getId());
			String jsonResponse = jsonObj.toJSONString();
			response.setContentType("application/json");
			response.getWriter().write(jsonResponse);*/
			/* ########################## BUILD JSON RESPONSE - END */
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("################### UPLOAD SERVLET - END ###################");
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}