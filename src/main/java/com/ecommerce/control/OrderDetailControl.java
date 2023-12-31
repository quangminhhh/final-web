package com.ecommerce.control;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ecommerce.dao.OrderDao;
import com.ecommerce.entity.CartProduct;

@WebServlet(name = "OrderDetailControl", value = "/order-detail")
public class OrderDetailControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
	// Call DAO class to access with database.
    OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get order id from request.
        int orderId = Integer.parseInt(request.getParameter("order-id"));
        // Get order by id from database.
        List<CartProduct> list = orderDao.getOrderDetailHistory(orderId);

        request.setAttribute("order_detail_list", list);
        // Get request dispatcher and render to order-detail page.
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("order-detail.jsp");
        requestDispatcher.forward(request, response);
    }
}
