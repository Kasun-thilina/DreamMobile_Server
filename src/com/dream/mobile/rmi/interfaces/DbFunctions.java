package com.dream.mobile.rmi.interfaces;

import java.awt.Color;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.sql.*;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.dream.mobile.server.ServerUI;

/**
 * 
 * @author Kasun Thilina Implementing the remote interface methods
 *
 */

public class DbFunctions implements RemoteInterface {
	Connection conn = null;
	Statement stmt = null;

	public DbFunctions() {
		// JDBC driver name and database URL
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";

		// Register JDBC driver and making the connection
		try {
			Class.forName("com.mysql.jdbc.Driver");

			// Open a connection
			ServerUI.printLine("Connecting to a selected database...", null);
			conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Questions_DB", "root", "root");
			ServerUI.printLine("Connected database successfully...", null);

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			ServerUI.printLine("Failed Connecting to the Database " + e, Color.RED);

		}

	}

	@Override
	public List<Questions> getQuestions() throws Exception {
		List<Questions> list = new ArrayList<Questions>();
		ArrayList<String> Questions = new ArrayList<String>();
		ArrayList<String> Answer_01 = new ArrayList<String>();
		ArrayList<String> Answer_02 = new ArrayList<String>();
		ArrayList<String> Answer_03 = new ArrayList<String>();
		ArrayList<String> Answer_04 = new ArrayList<String>();
		ArrayList<String> Answer_05 = new ArrayList<String>();

		// Execute a query
		ServerUI.printLine("Creating statement...", null);

		// Creating the query to access database
		stmt = conn.createStatement();
		// SQL Query to get data from database
		String sql = "SELECT * FROM questions";
		// Executing the SQL Query
		ResultSet rs = stmt.executeQuery(sql);
		Questions student = new Questions();

		// Extract data from result set
		while (rs.next()) {
			// Retrieve by column name
			int id = rs.getInt("id");

			String name = rs.getString("question");
			String answer_01 = rs.getString("answer_01");
			String answer_02 = rs.getString("answer_02");
			String answer_03 = rs.getString("answer_03");
			String answer_04 = rs.getString("answer_04");
			String answer_05 = rs.getString("answer_05");

			Questions.add(name);
			Answer_01.add(answer_01);
			Answer_02.add(answer_02);
			Answer_03.add(answer_03);
			Answer_04.add(answer_04);
			Answer_05.add(answer_05);

			// Setting the values
			list.add(student);
		}
		student.setQuestions(Questions);
		student.setAnswer_01(Answer_01);
		student.setAnswer_02(Answer_02);
		student.setAnswer_03(Answer_03);
		student.setAnswer_04(Answer_04);
		student.setAnswer_05(Answer_05);

		ServerUI.printLine(Questions.toString(), Color.LIGHT_GRAY);
		rs.close();
		return list;
	}

	// Implementing the interface method
	@Override
	public String saveAnswer(ArrayList<String> Answers) throws Exception {

		// Execute a query
		ServerUI.printLine("Saving Data...", null);

		// Creating the query to access database
		stmt = conn.createStatement();
		// SQL Query to get data from database
		String sql = "INSERT INTO analytics VALUES(NULL," + " '" + Answers.get(0) + "', " + " '" + Answers.get(1) + "',"
				+ " '" + Answers.get(2) + "'," + " '" + Answers.get(3) + "'," + " '" + Answers.get(4) + "'," + " '"
				+ Answers.get(5) + "'," + " '" + Answers.get(6) + "'," + " '" + Answers.get(7) + "'," + " '"
				+ Answers.get(8) + "'," + " '" + Answers.get(9) + "'," + " '" + Answers.get(10) + "')";
		System.out.println(Answers);
		// Executing the SQL Query
		int rs = stmt.executeUpdate(sql);
		// Questions student = new Questions();
		ServerUI.printLine("Answers:" + Answers, null);
		ServerUI.printLine("Data Saved Successfully", null);
		return "Data Saved Successfully";
	}

	@Override
	public ArrayList<Analytics> getAnalytics() throws Exception {
		// getting data from analytics table in column wise to send them to analytics
		// API
		// SELECT DISTINCT Name FROM analytics
		// Execute a query
		ArrayList<Analytics> analyticsList=new ArrayList<Analytics>();
		ServerUI.printLine("Getting Analytics Data From Server...", null);

		// Creating the query to access database
		stmt = conn.createStatement();
		// SQL Query to get data from database
		String sql = "SELECT * FROM analytics";
		// Executing the SQL Query
		ResultSet rs = stmt.executeQuery(sql);
		Analytics analytics = new Analytics();

		// Extract data from result set
		while (rs.next()) {
			analytics=new Analytics(
					rs.getInt("id"),
					rs.getString("Name"),
					rs.getString("OS"),
					rs.getString("amount"),
					rs.getString("ram"),
					rs.getString("screen"),
					rs.getString("back_camera"),
					rs.getString("front_camera"),
					rs.getString("storage"),
					rs.getString("micro_sd"),
					rs.getString("brand"),
					rs.getString("other")
					);
			analyticsList.add(analytics);
		}

		rs.close();
		ServerUI.printLine("Returning the following data to client ", null);

		return analyticsList;
	}

	@Override
	public Boolean adminLogin(String username, String password) throws Exception {
		ResultSet rs = null;
		// Execute a query

		// System.out.println("Checking for username password validity");
		ServerUI.printLine("Checking for username password validity", null);

		// Creating the query to access database
		stmt = conn.createStatement();
		// SQL Query to get data from database
		String sql = "SELECT username FROM userdb WHERE username='" + username + "' AND password='" + password + "'";
		// Executing the SQL Query
		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (rs.next()) {
			rs.close();
			return true;

		} else {
			rs.close();
			return false;
		}

	}

	@Override
	public ArrayList<String> getOnlyAnswers(int coloumnID) throws Exception {
		ResultSet rs = null;
		ArrayList<String> answers = new ArrayList<String>();

		// Execute a query

		// System.out.println("Checking for username password validity");
		ServerUI.printLine("Getting Only the Answers From the Server....", null);

		// Creating the query to access database
		stmt = conn.createStatement();
		// SQL Query to get data from database
		String sql = "SELECT answer_01,answer_02,answer_03,answer_04,answer_05 FROM questions WHERE id=" + coloumnID;
		// Executing the SQL Query
		try {
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				answers.add(rs.getString("answer_01"));
				answers.add(rs.getString("answer_02"));
				answers.add(rs.getString("answer_03"));
				answers.add(rs.getString("answer_04"));
				answers.add(rs.getString("answer_05"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return answers;

	}
	
	

	@SuppressWarnings("unchecked")
	@Override
	public String getChart(ArrayList<String> labelNames,ArrayList<Integer> values,String heading,String chartType) throws Exception {
		/**
		 * Creating the JSON Array for the API request using json-simple.jar external
		 * library
		 */
		
		// Main JSON Object for chart
		JSONObject chart = new JSONObject();
		// Content of the chart node
		JSONObject chartContent = new JSONObject();

		// JSON Object for data node
		JSONObject data = new JSONObject();

		// JSON Array for graph labels
		JSONArray labels = new JSONArray();
		labels.addAll(labelNames);
		

		// Setting up values for datasets node
		// Main JSON array of dataSets
		JSONArray dataSets = new JSONArray();
		// ArrayList for the DataSet Values
		LinkedHashMap dataSetValues = new LinkedHashMap();
		// ArrayList for the Label Values
		/*ArrayList<String> dataSetLabel = new ArrayList<String>();
		dataSetLabel.add(heading);*/
		// dataSetLabel.add("2");

		// ArrayList for the Data Values
		ArrayList<Integer> dataSetData = new ArrayList<Integer>();
		dataSetData.addAll(values);

		// setting up the Chart JSON Array with values

		data.put("labels", labels);
		dataSetValues.put("label", heading);
		dataSetValues.put("data", dataSetData);
		dataSets.add(dataSetValues);
		data.put("datasets", dataSets);
		chartContent.put("type", chartType);
		chartContent.put("data", data);
		chart.put("chart", chartContent);

		System.out.println(chart.toString());

		HttpRequest request = HttpRequest.newBuilder().POST(BodyPublishers.ofString(chart.toJSONString()))
				.uri(URI.create("https://quickchart.io/chart/create")).setHeader("User-Agent", "Java 11 HttpClient Bot")
				.header("Content-Type", "application/json").build();
		// HTTP Client for API Connection
		final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		// print status code
		System.out.println(response.statusCode());

		// print response body
		System.out.println(response.body());

		String url = null;
		try {
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(response.body());
			url = (String) jsonObject.get("url");
			System.out.println(url);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return url;

	}

	@Override
	public int getItemCount(String coloumnName,String itemName) throws Exception {
		ResultSet rs = null;
		Integer count=0;

		ServerUI.printLine("Getting item count of"+itemName, null);

		// Creating the query to access database
		stmt = conn.createStatement();
		// SQL Query to get data from database
		String sql = "SELECT COUNT( * ) as total FROM analytics WHERE "+coloumnName+"='"+ itemName+"'";
		// Executing the SQL Query
		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		while(rs.next()) {
			count=rs.getInt("total");
		}

		return count;
	}

}