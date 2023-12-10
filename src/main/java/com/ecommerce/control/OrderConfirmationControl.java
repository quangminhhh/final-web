package com.ecommerce.control;

import java.io.IOException;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ecommerce.entity.Order;

@WebServlet("/orderConfirmationControl")
public class OrderConfirmationControl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Lấy dữ liệu từ request
		String firstName = request.getParameter("first-name");
		String lastName = request.getParameter("last-name");
		String address = request.getParameter("address");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");

		// Create or fetch the order object
		Order order = new Order(); // Replace this with your actual order creation or fetching logic

		// Lấy tổng giá trị đơn hàng
		double totalOrderPrice = Double.parseDouble(request.getParameter("order-total-price"));

		// Gửi email
		sendOrderConfirmationEmail(firstName, lastName, address, email, phone, totalOrderPrice);

		// Chuyển hướng đến trang thank.jsp
		response.sendRedirect("thankyou.jsp");
	}

	private void sendOrderConfirmationEmail(String firstName, String lastName, String address, String email,
			String phone, double totalOrderPrice) {
		// Cấu hình properties cho session email
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");

		// Thông tin tài khoản email gửi
		String senderEmail = "thang1002lt@gmail.com";
		String senderPassword = "wktfbqfkuqrlabxc";

		// Tạo session email
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(senderEmail, senderPassword);
			}
		});

		try {
			// Tạo đối tượng MimeMessage
			Message message = new MimeMessage(session);

			// Đặt thông tin người gửi, người nhận và chủ đề
			message.setFrom(new InternetAddress(senderEmail));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			message.setSubject("Confirm successful order");

			// Đặt nội dung email
			String emailContent = "Hi " + firstName + " " + lastName + ",\n\n" + "Thank you for your order.\n"
					+ "\nTotal order value: " + totalOrderPrice + "$\n\n" + "Delivery address: " + address + "\n"
					+ "Contact phone number: " + phone + "\n\n" + "Best regards,\nShopper";
			message.setText(emailContent);

			// Gửi email
			Transport.send(message);

			System.out.println("Đã gửi email xác nhận đặt hàng thành công.");

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
