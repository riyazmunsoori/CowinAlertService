package org.opensource.covidvaccine;

import org.json.JSONException;
import org.opensource.covidvaccine.controller.VaccineAlertController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class CovidVaccineAlertApplication implements CommandLineRunner {

    @Autowired
    private VaccineAlertController vaccineAlertController;

    public static void main(String[] args) {
        SpringApplication.run(CovidVaccineAlertApplication.class, args);
    }

    @Override
    public void run(String... args) throws MessagingException, JSONException, IOException {
        try{
            while(true){
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                System.out.println("Running vaccine alert service at: " + dateTimeFormatter.format(now));
                vaccineAlertController.runService();
                System.out.println("Sleeping for 1 minutes");
                Thread.sleep(1 * 60 * 1000);
            }
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }


    }
}
