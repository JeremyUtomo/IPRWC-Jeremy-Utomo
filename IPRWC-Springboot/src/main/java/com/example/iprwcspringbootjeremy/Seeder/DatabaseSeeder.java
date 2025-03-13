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
        Category magazines = categorySeeder.seed("Magazine");
        Category music = categorySeeder.seed("Music");
        productSeeder.seed("Macbook", 999.99, 10, "Macbook M4 13 inch", "macbook.jpg", electronics);
        productSeeder.seed("iPhone", 699.99, 100, "iPhone 12","iphone.jpg", electronics);
        productSeeder.seed("Samsung Galaxy", 599.99, 100, "Samsung Galaxy S21","samsung-galaxy.jpg", electronics);
        productSeeder.seed("Java Programming", 19.99, 100, "Java Programming for beginners","java-programming.jpg", books);
        productSeeder.seed("Spring Boot", 29.99, 100, "Spring Boot for beginners","spring-boot.jpg", books);
        productSeeder.seed("Python Programming", 19.99, 100, "Python Programming for beginners","python-programming.jpg", books);
        productSeeder.seed("Time", 5.99, 1000, "Time Magazine Morgan Freeman","time.jpg", magazines);
        productSeeder.seed("National Geographic", 6.99, 1000, "National Geographic Magazine Yellowstone","national-geographic.jpg", magazines);
        productSeeder.seed("Forbes", 7.99, 1000, "Forbes Magazine Travis Scott","forbes.jpg", magazines);
        productSeeder.seed("The Beatles", 9.99, 100, "The Beatles A Hard Day's Night","the-beatles.jpg", music);
        productSeeder.seed("Elton John", 9.99, 100, "Elton John Goodbye Yellow Brick Road","elton-john.jpg", music);
        productSeeder.seed("New Jeans", 29.99, 5, "New Jeans Get up Ep","new-jeans.jpg", music);

        this.alreadySeeded = true;
    }
}
