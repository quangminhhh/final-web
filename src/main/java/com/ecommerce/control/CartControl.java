package com.ecommerce.control;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ecommerce.dao.ProductDao;
import com.ecommerce.entity.CartProduct;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.Product;

@WebServlet(name = "CartControl", value = "/cart")
public class CartControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// Call DAO class to access with database.
	ProductDao productDao = new ProductDao();


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		
		
		// Xử lý yêu cầu khi người dùng nhấn nút "Minus"
	    if ("minus".equals(request.getParameter("action"))) {
	        response.sendRedirect("decrease-quantity?product-id=" + request.getParameter("product-id"));
	        return;
	    }

	    // Xử lý yêu cầu khi người dùng nhấn nút "Plus"
	    if ("plus".equals(request.getParameter("action"))) {
	        response.sendRedirect("increase-quantity?product-id=" + request.getParameter("product-id"));
	        return;
	    }
	  
	    
	 //request is remove product from cart or not.
        if (request.getParameter("remove-product-id") != null) {
            // Call the RemoveFromCartServlet using RequestDispatcher
            RequestDispatcher dispatcher = request.getRequestDispatcher("/removeFromCart");
            dispatcher.forward(request, response);
            return;
        }
        
        

		// Initialize default value for quantity and productId.
		int quantity = 1;
		int productId;
		// Check is the total price of order exist or not.
		double totalPrice;
		if (session.getAttribute("total_price") == null) {
			totalPrice = 0;
		} else {
			totalPrice = (double) session.getAttribute("total_price");
		}

		// Generate if product exist in database.
		if (request.getParameter("product-id") != null) {
			// Get the id of product from request.
			productId = Integer.parseInt(request.getParameter("product-id"));

			// Get product information from database.
			Product product = productDao.getProduct(productId);
			if (product != null) {
				// Get the quantity of the adding product.
				if (request.getParameter("quantity") != null) {
					// Get the quantity of the product if the quantity is more than 1.
					quantity = Integer.parseInt(request.getParameter("quantity"));
					// Check if the request quantity is more than the number of products left or
					// not.
					if ((product.getAmount() - quantity) < 0) {
						response.sendRedirect("product-detail?id=" + product.getId() + "&invalid-quantity=1");
						return;
					}
				}
				// Check the product has been added to cart yet.
				if (session.getAttribute("order") == null) {
					// Create an order and list of product for it.
					Order order = new Order();
					List<CartProduct> list = new ArrayList<>();

					// Create a product and its quantity for the order.
					CartProduct cartProduct = new CartProduct();
					cartProduct.setQuantity(quantity);
					cartProduct.setProduct(product);
					cartProduct.setPrice(product.getPrice());

					// Count the total price of the order.
					totalPrice += (product.getPrice() * quantity);

					// Add product to list.
					list.add(cartProduct);

					// Add list of cart products to order.
					order.setCartProducts(list);

					session.setAttribute("total_price", totalPrice);
					session.setAttribute("order", order);
				} else {
					// Get exist order from session.
					Order order = (Order) session.getAttribute("order");
					// Get the list of products from order.
					List<CartProduct> list = order.getCartProducts();

					// Increase the product quantity if it is already exist in cart.
					boolean flag = false;
					for (CartProduct cartProduct : list) {
						if (cartProduct.getProduct().getId() == product.getId()) {
							cartProduct.setQuantity(cartProduct.getQuantity() + quantity);
							totalPrice += product.getPrice() * quantity;
							flag = true;
						}
					}

					// Add new product to existing cart.
					if (!flag) {
						CartProduct cartProduct = new CartProduct();
						cartProduct.setQuantity(quantity);
						cartProduct.setProduct(product);
						cartProduct.setPrice(product.getPrice());
						totalPrice += product.getPrice() * quantity;
						list.add(cartProduct);
					}

					session.setAttribute("total_price", totalPrice); 
					session.setAttribute("order", order);
				}
			}
			response.sendRedirect("product-detail?id=" + productId);
		}
		
	}
}
