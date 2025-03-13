package com.example.iprwcspringbootjeremy.Seeder;

import com.example.iprwcspringbootjeremy.Model.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.event.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class DatabaseSeeder {

    @Autowired
    private AdminAccountSeeder adminAccountSeeder;

    @Autowired
    private CategorySeeder categorySeeder;

    @Autowired
    private ProductSeeder productSeeder;

    private boolean alreadySeeded = false;


    @EventListener
    public void seed(ContextRefreshedEvent event) {
        if (alreadySeeded) {
            return;
        }

        adminAccountSeeder.seed();
        Category electronics = categorySeeder.seed("Electronics");
        Category books = categorySeeder.seed("Books");
        Category megazines = categorySeeder.seed("Megazines");
        Category music = categorySeeder.seed("Music");
        productSeeder.seed("Macbook", 999.99, 10, "Macbook M4 13 inch", "src/main/resources/images/macbook.jpg", electronics);
        productSeeder.seed("iPhone", 699.99, 100, "iPhone 12","src/main/resources/images/iphone.jpg", electronics);
        productSeeder.seed("Samsung Galaxy", 599.99, 100, "Samsung Galaxy S21","src/main/resources/images/samsung-galaxy.jpg", electronics);
        productSeeder.seed("Java Programming", 19.99, 100, "Java Programming for beginners","src/main/resources/images/java-programming.jpg", books);
        productSeeder.seed("Spring Boot", 29.99, 100, "Spring Boot for beginners","src/main/resources/images/spring-boot.jpg", books);
        productSeeder.seed("Python Programming", 19.99, 100, "Python Programming for beginners","src/main/resources/images/python-programming.jpg", books);
        productSeeder.seed("Time", 5.99, 1000, "Time Megazine","src/main/resources/images/time.jpg", megazines);
        productSeeder.seed("National Geographic", 6.99, 1000, "National Geographic Megazine","src/main/resources/images/national-geographic.jpg", megazines);
        productSeeder.seed("Forbes", 7.99, 1000, "Forbes Megazine","src/main/resources/images/forbes.jpg", megazines);
        productSeeder.seed("The Beatles", 9.99, 100, "The Beatles","src/main/resources/images/the-beatles.jpg", music);
        productSeeder.seed("Elton John", 9.99, 100, "Elton John","src/main/resources/images/elton-john.jpg", music);
        productSeeder.seed("Michael Jackson", 9.99, 100, "Michael Jackson","src/main/resources/images/micheal-jackson.jpg", music);

        this.alreadySeeded = true;
    }
}
