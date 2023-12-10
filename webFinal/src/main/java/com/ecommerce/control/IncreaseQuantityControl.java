package com.ecommerce.control;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ecommerce.entity.CartProduct;
import com.ecommerce.entity.Order;

@WebServlet("/increase-quantity")
public class IncreaseQuantityControl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);

        int productId = Integer.parseInt(request.getParameter("product-id"));
        Order order = (Order) session.getAttribute("order");
        double totalPrice = (double) session.getAttribute("total_price");

        // Thực hiện logic tăng số lượng sản phẩm trong giỏ hàng
        increaseQuantity(productId, order, totalPrice, session);

        response.sendRedirect("cart.jsp");
    }

    private void increaseQuantity(int productId, Order order, double totalPrice, HttpSession session) {
        List<CartProduct> list = order.getCartProducts();

        for (CartProduct cartProduct : list) {
            if (cartProduct.getProduct().getId() == productId) {
                // Tăng số lượng sản phẩm
                cartProduct.setQuantity(cartProduct.getQuantity() + 1);
                totalPrice += cartProduct.getProduct().getPrice();
                break; // Dừng vòng lặp sau khi xử lý sản phẩm
            }
        }

        // Cập nhật giá trị tổng giá trị
        session.setAttribute("total_price", totalPrice);
        session.setAttribute("order", order);
    }
}
