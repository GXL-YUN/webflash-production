package cn.enilu.flash.utils;// Import necessary packages
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Import necessary packages
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

// Define class GetAddBrowser
public class GetAddBrowser {

    //Java  获取谷歌 edge浏览器收藏夹内容
    // Define method getChromeBookmarks
    public static List<String> getChromeBookmarks() {
        // Initialize empty list to store bookmarks
        List<String> bookmarks = new ArrayList<>();

        try {
            // Establish connection to Chrome bookmarks database
            Connection connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/Lenovo/AppData/Local/Google/Chrome/User Data/Default/Bookmarks");

            // Create statement to query bookmarks
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM bookmarks");

            // Iterate through results and add bookmarks to list
            while (resultSet.next()) {
                bookmarks.add(resultSet.getString("url"));
            }

            // Close connection and statement
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Return list of bookmarks
        return bookmarks;
    }

    // Define method getEdgeFavorites
    public static List<String> getEdgeFavorites() {
        // Initialize empty list to store favorites
        List<String> favorites = new ArrayList<>();

        try {
            // Establish connection to Edge favorites database
            Connection connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/Lenovo/AppData/Local/Microsoft/Edge/User Data/Default/Favorites");

            // Create statement to query favorites
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM favorites");

            // Iterate through results and add favorites to list
            while (resultSet.next()) {
                favorites.add(resultSet.getString("url"));
            }

            // Close connection and statement
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Return list of favorites
        return favorites;
    }

    // Define method checkWebsitePerformance
    public static int checkWebsitePerformance(String url) {
        try {
            // Open connection to website URL
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

            // Set request method to HEAD
            connection.setRequestMethod("HEAD");

            // Get response code
            int responseCode = connection.getResponseCode();

            // Close connection
            connection.disconnect();

            // Return response code
            return responseCode;
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return -1 if an error occurred
        return -1;
    }

}

 // This will print the response code of the website, indicating its performance.