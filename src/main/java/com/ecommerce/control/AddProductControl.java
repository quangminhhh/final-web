package com.ecommerce.control;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.ecommerce.dao.ProductDao;
import com.ecommerce.entity.Account;

@WebServlet(name = "AddProductControl", value = "/add-product")
@MultipartConfig
public class AddProductControl extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get product information from request.
        String productName = request.getParameter("product-name");
        double productPrice = Double.parseDouble((request.getParameter("product-price")));
        String productDescription = request.getParameter("product-description");
        int productCategory = Integer.parseInt(request.getParameter("product-category"));
        int productAmount = Integer.parseInt(request.getParameter("product-amount"));

        // Get upload image.
        Part part = request.getPart("product-image");
        InputStream inputStream = part.getInputStream();

        // Get the seller id for product.
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        int sellerId = account.getId();

        // Add product to database.
        ProductDao productDao = new ProductDao();
        productDao.addProduct(productName, inputStream, productPrice, productDescription, productCategory, sellerId, productAmount);
        response.sendRedirect("product-management");
    }
}
