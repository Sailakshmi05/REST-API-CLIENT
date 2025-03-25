import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestaurantFetcher {

    // Replace with your actual API key from Zomato
    private static final String API_KEY = "pub_75726bdfc085840ad0c41e30e9f33706a7c73";
    private static final String API_URL = "https://developers.zomato.com/api/v2.1/search?entity_id=1&entity_type=city&count=5";

    public static void main(String[] args) {
        try {
            String response = fetchRestaurantData();
            displayRestaurants(response);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Method to fetch restaurant data from API
    private static String fetchRestaurantData() throws Exception {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("user-key", API_KEY);
        conn.setRequestProperty("Accept", "application/json");

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            response.append(output);
        }
        conn.disconnect();
        return response.toString();
    }

    // Method to parse and display restaurant data
    private static void displayRestaurants(String jsonData) {
        JSONObject jsonObject = new JSONObject(jsonData);
        JSONArray restaurants = jsonObject.getJSONArray("restaurants");

        System.out.println("Top 5 Restaurants in the City:");
        System.out.println("------------------------------------");

        for (int i = 0; i < restaurants.length(); i++) {
            JSONObject restaurant = restaurants.getJSONObject(i).getJSONObject("restaurant");
            String name = restaurant.getString("name");
            String cuisine = restaurant.getJSONObject("cuisines").toString();
            String address = restaurant.getJSONObject("location").getString("address");
            double rating = restaurant.getJSONObject("user_rating").getDouble("aggregate_rating");

            System.out.println("Name: " + name);
            System.out.println("Cuisine: " + cuisine);


            System.out.println("Address: " + address);
            System.out.println("Rating: " + rating);
            System.out.println("------------------------------------");
        }
    }
}