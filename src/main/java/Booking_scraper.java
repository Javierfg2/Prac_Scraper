import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.json.JSONObject;


import java.io.IOException;
import java.util.*;

public class Booking_scraper {

    public static JSONObject get_ubication(String hotel_name, String country_code) throws IOException {

        Document doc = Jsoup.connect("https://www.booking.com/hotel/" + country_code + "/" + hotel_name + ".html").get();

        Element address_element = doc.select(".hp_address_subtitle").first();

        assert address_element != null;
        String address = address_element.text();

        Elements elements = doc.select("script");

        Element element = null;
        for (Element e : elements) {
            String script = e.html();
            if (script.contains("booking.env.b_map_center_latitude")) {
                element = e;
                break;
            }
        }

        String script = element.html();
        int start = script.indexOf("booking.env.b_map_center_latitude") + "booking.env.b_map_center_latitude".length() + 1;
        int end = script.indexOf(";", start);
        String value = script.substring(start, end).substring(2);

        double latitude = Double.parseDouble(value);


        element = null;
        for (Element e : elements) {
            script = e.html();
            if (script.contains("booking.env.b_map_center_longitude")) {
                element = e;
                break;
            }
        }

        assert element != null;
        script = element.html();
        start = script.indexOf("booking.env.b_map_center_longitude") + "booking.env.b_map_center_longitude".length() + 1;
        end = script.indexOf(";", start);
        value = script.substring(start, end).substring(2);

        double longitude = Double.parseDouble(value);

        JSONObject json = new JSONObject();
        json.put("address", address);
        json.put("latitude", latitude);
        json.put("longitude", longitude);

        return json;
    }

    public static JSONArray get_services(String hotel_name, String country_code) throws IOException {

        Document doc = Jsoup.connect("https://www.booking.com/hotel/" + country_code + "/" + hotel_name + ".html").get();

        Element element = doc.select("#hotelTmpl").first();

        assert element != null;
        Elements elements = element.getElementsByClass("bui-list__description");
        JSONArray services = new JSONArray();

        for (Element e : elements) {
            String service = e.text();
            JSONObject json = new JSONObject();
            json.put("service_name", service);
            services.put(json);

        }

        return services;

    }

    public static JSONArray get_reviews(String hotel_name, String country_code) throws IOException {
        Document doc = Jsoup.connect("https://www.booking.com/hotel/" + country_code + "/" + hotel_name + ".html").get();

        Elements elements = doc.getElementsByClass("db29ecfbe2 c688f151a2");
        JSONArray reviews = new JSONArray();

        for (Element e : elements) {
            String review = e.text().replaceAll("\\u201c", "").replaceAll("\u201d", "");
            JSONObject json = new JSONObject();
            json.put("review", review);
            reviews.put(json);
        }

        return reviews;
    }

    public static boolean is_in_array(Valoration valoration, ArrayList<Valoration> valorations) {
        boolean result = false;

        for (Valoration val : valorations) {
            if (val.equals(valoration)) {
                result = true;
                break;
            }
        }

        return result;
    }

    public static JSONArray get_valorations(String hotel_name, String country_code) throws IOException {
        Document doc = Jsoup.connect("https://www.booking.com/hotel/" + country_code + "/" + hotel_name + ".html").get();

        Elements elements = doc.getElementsByClass("a1b3f50dcd f7c6687c3d ef8295f3e6");

        ArrayList<Valoration> valorations = new ArrayList<>();

        for (Element e : elements) {
            String complete_valoration = e.text();
            int size = complete_valoration.length();
            String category = complete_valoration.substring(0, size - 3);
            double score = Double.parseDouble(complete_valoration.substring(size - 3));
            Valoration valoration = new Valoration(category, score);


            if (!is_in_array(valoration, valorations)) {
                valorations.add(valoration);
            }

        }

        JSONArray jsonArray = new JSONArray();
        for (Valoration v : valorations) {
            JSONObject json = new JSONObject();
            json.put("score", v.getScore());
            json.put("category", v.getCategory());
            jsonArray.put(json);
        }

        return jsonArray;
    }

    public static ArrayList<String> get_city_hotels(String city, String country_code) throws IOException {

        String url = "https://www.booking.com/region/" + country_code + "/" + city + ".html";

        Document doc = Jsoup.connect(url).get();

        Elements elements = doc.getElementsByClass("bui-spacer--medium");
        ArrayList<String> hotel_names = new ArrayList<>();

        for (Element e : elements) {

            String spacer = e.toString();
            int index_hotel = spacer.indexOf("hotel/es/");
            if (index_hotel != -1) {
                index_hotel += 9;
                int index_dot = spacer.substring(index_hotel).indexOf(".");
                index_dot += index_hotel;
                String name = (spacer.substring(index_hotel, index_dot));
                hotel_names.add(name);
            }

        }

        return hotel_names;

    }
}
