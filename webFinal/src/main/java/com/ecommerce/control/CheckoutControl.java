package com.ecommerce.control;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ecommerce.dao.AccountDao;
import com.ecommerce.dao.OrderDao;
import com.ecommerce.entity.Account;
import com.ecommerce.entity.Order;

@WebServlet(name = "CheckoutControl", value = "/checkout")
public class CheckoutControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
	// Call DAO class to access with database.
    OrderDao orderDao = new OrderDao();
    AccountDao accountDao = new AccountDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	
        HttpSession session = request.getSession();
        // Get information from input field.
        String firstName = request.getParameter("first-name");
        String lastName = request.getParameter("last-name");
        String address = request.getParameter("address");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        if (session.getAttribute("account") == null) {
            response.sendRedirect("login.jsp");
        }
        else {
            double totalPrice = (double) session.getAttribute("total_price");
            Order order = (Order) session.getAttribute("order");
            Account account = (Account) session.getAttribute("account");

            // Insert information to account.
            int accountId = account.getId();
            accountDao.updateProfileInformation(accountId, firstName, lastName, address, email, phone);
            // Insert order to database.
            orderDao.createOrder(account.getId(), totalPrice, order.getCartProducts());
			
			
			 session.removeAttribute("order"); session.removeAttribute("total_price");
			 
			 

            RequestDispatcher requestDispatcher = request.getRequestDispatcher("orderConfirmationControl");
            requestDispatcher.forward(request, response);
        }
    
    }
}
