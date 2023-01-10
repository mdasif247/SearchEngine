package com.asif;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//this will be called when search button will be clicked
@WebServlet("/Search")
public class Search extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        //get the keyword searched
        String keyword=request.getParameter("keyword");
        System.out.println(keyword);

        try{
            Connection connection=DatabaseConnection.getConnection();
            //add keyword into history table
            PreparedStatement preparedStatement=connection.prepareStatement("Insert into history values(?, ?)");
            preparedStatement.setString(1,keyword);
            preparedStatement.setString(2,"http://localhost:8080/SearchEngine/Search?keyword="+keyword);
            preparedStatement.executeUpdate();

            //will display the pages based on the highest occurence of keyword in the page data
            //formula - len(pagedata)-len(replace(pagedata,"java","")/len("java")
            //resultset is a java-sql class which will store the result of sql statements in the form of linkedlist
            ResultSet resultSet=connection.createStatement().executeQuery("select pageTitle,pageLink,(length(lower(pageData))-length(replace(lower(pageData),'"+keyword+"',\"\")))/length('"+keyword+"') as countoccurence from pages order by countoccurence desc limit 30;");
            ArrayList<searchResult> results=new ArrayList<searchResult>();
            while(resultSet.next()){
                //call searchResult class
                searchResult searchResult=new searchResult();
                searchResult.setPageTitle(resultSet.getString("pageTitle"));
                searchResult.setPageLink(resultSet.getString("pageLink"));
                results.add(searchResult);
            }

            //will set the request attribute to results arraylist
            request.setAttribute("results",results);
            //will forward the request to the search.jsp
            request.getRequestDispatcher("/search.jsp").forward(request,response);
            //set content type as html
            response.setContentType("text/html");
            //get writer to write content in response
            PrintWriter out=response.getWriter();
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
        catch (ServletException servletException){
            servletException.printStackTrace();
        }
        catch (IOException ioException){  //will handle request forwarding
            ioException.printStackTrace();
        }

    }
}
