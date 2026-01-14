package controller;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public abstract class BaseController extends HttpServlet {
	protected static final String FLASH_SUCCESS = "success";
	protected static final String FLASH_ERROR = "error";

	protected void setFlashMessage(
			HttpServletRequest request,
			String type,
			String message) {
		if (!FLASH_SUCCESS.equals(type) && !FLASH_ERROR.equals(type)) {
			throw new IllegalArgumentException("Invalid flash message type: " + type);
		}

		HttpSession session = request.getSession();
		session.setAttribute("flashMessage", message);
		session.setAttribute("flashMessageType", type);
	}

	protected void handleException(
			Exception e,
			HttpServletRequest request,
			HttpServletResponse response,
			String nextPage)
			throws ServletException, IOException {

		request.setAttribute("error", e.getMessage());

		RequestDispatcher rd = request.getRequestDispatcher(nextPage);
		rd.forward(request, response);
	}

}