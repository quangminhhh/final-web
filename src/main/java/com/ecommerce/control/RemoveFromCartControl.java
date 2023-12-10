package com.ecommerce.control;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ecommerce.entity.CartProduct;
import com.ecommerce.entity.Order;

@WebServlet("/removeFromCart")
public class RemoveFromCartControl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Check if request is remove product from cart or not.
        if (request.getParameter("remove-product-id") != null) {
            Order order = (Order) session.getAttribute("order");
            double totalPrice = (double) session.getAttribute("total_price");
            int productId = Integer.parseInt(request.getParameter("remove-product-id"));

            // Call the method to remove product and update total price
            removeCartProduct(productId, order, totalPrice, session);

            response.sendRedirect("cart.jsp");
        }
    }

    private void removeCartProduct(int productId, Order order, double totalPrice, HttpSession session) {
        // Get list of products from the existing order.
        List<CartProduct> list = order.getCartProducts();

        // Iterator.remove is the only safe way to modify a collection during iteration
        for (Iterator<CartProduct> iterator = list.iterator(); iterator.hasNext();) {
            // Get the cart product object from list.
            CartProduct cartProduct = iterator.next();

            // Delete the product if its id equals the id of deleting product.
            if (cartProduct.getProduct().getId() == productId) {
                // Remove price of deleting product from total price.
                totalPrice -= (cartProduct.getPrice() * cartProduct.getQuantity());

                // Remove product from cart.
                iterator.remove();
            }
        }

        // Set the updated total price to the session attribute
        session.setAttribute("total_price", totalPrice);
    }
}
