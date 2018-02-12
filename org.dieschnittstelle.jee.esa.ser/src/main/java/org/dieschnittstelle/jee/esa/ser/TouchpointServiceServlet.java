package org.dieschnittstelle.jee.esa.ser;

import java.io.ObjectOutputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.dieschnittstelle.jee.esa.utils.Utils.*;

import org.apache.log4j.Logger;

public class TouchpointServiceServlet extends HttpServlet {

	protected static Logger logger = Logger
			.getLogger(TouchpointServiceServlet.class);

	public TouchpointServiceServlet() {
		show("TouchpointServiceServlet: constructor invoked\n");
	}
	
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) {

		logger.info("doGet()");

		// we assume here that GET will only be used to return the list of all
		// touchpoints

		// obtain the executor for reading out the touchpoints
		TouchpointCRUDExecutor exec = (TouchpointCRUDExecutor) getServletContext()
				.getAttribute("touchpointCRUD");
		try {
			// set the status
			response.setStatus(HttpServletResponse.SC_OK);
			// obtain the output stream from the response and write the list of
			// touchpoints into the stream
			ObjectOutputStream oos = new ObjectOutputStream(
					response.getOutputStream());
			// write the object
			oos.writeObject(exec.readAllTouchpoints());
			oos.close();
		} catch (Exception e) {
			String err = "got exception: " + e;
			logger.error(err, e);
			throw new RuntimeException(e);
		}

	}
	
	/*
	@Override	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) {

		// assume POST will only be used for touchpoint creation, i.e. there is
		// no need to check the uri that has been used

		// obtain the executor for reading out the touchpoints from the servlet context using the touchpointCRUD attribute

		try {
			// create an ObjectInputStream from the request's input stream
		
			// read an AbstractTouchpoint object from the stream
		
			// call the create method on the executor and take its return value
		
			// set the response status as successful, using the appropriate
			// constant from HttpServletResponse
		
			// then write the object to the response's output stream, using a
			// wrapping ObjectOutputStream
		
			// ... and write the object to the stream
		
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	*/


	
}
