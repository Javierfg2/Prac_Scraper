import java.io.IOException;
import java.util.ArrayList;


public class Main {
    public static void main(String[] args) throws IOException {

        String city = "madrid";
        String country_code = "es";


        //Example of use

        ArrayList<String> madrid_hotels = Booking_scraper.get_city_hotels(city, country_code);
        for (String hotel : madrid_hotels) {
            System.out.println("Hotel: " + hotel);
            System.out.println(Booking_scraper.get_valorations(hotel, country_code));

        }
    }
}


