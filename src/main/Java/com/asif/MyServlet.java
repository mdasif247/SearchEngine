package com.asif;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.IOException;

// /servlet will go to particular servlet
@WebServlet("/MyServlet")
//this is a servlet(Mini server)
public class MyServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
        //set content type as html
        response.setContentType("text/html");
        //get writer to write content in response
        PrintWriter out=response.getWriter();
        //write content in response
        out.println("<h2> This is my servlet</h2>");
    }
}
