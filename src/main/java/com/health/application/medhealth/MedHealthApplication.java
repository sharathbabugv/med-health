package com.health.application.medhealth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableJpaAuditing
public class MedHealthApplication {
    public static void main(String[] args) {
        SpringApplication.run(MedHealthApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    CommandLineRunner run(UserService userService) {
//        return args -> {
//            userService.savePatient(new Patient(null, "gvbabu@gmail.com", "string1234", "Sharath", "M",
//                    LocalDate.of(1996, 6, 6), "9986060550"));
//            userService.savePatient(new Patient(null, "girish@gmail.com", "string1234", "Girish", "M",
//                    LocalDate.of(1996, 6, 6), "9986060550"));
//            userService.savePatient(new Patient(null, "sharathraju666@gmail.com", "string1234", "Nandan", "M",
//                    LocalDate.of(1996, 6, 6), "9986060550"));
//
//            userService.saveDoctor(new Doctor(null, "gvsharathbabu@gmail.com", "string1234", "Vani", "F",
//                    LocalDate.of(1996, 6, 6), "9986060550", "MBBS", "ENT", LocalDate.of(2020, 6, 6), null));
//            userService.saveDoctor(new Doctor(null, "bhargav@gmail.com", "string1234", "Bhargav", "M",
//                    LocalDate.of(1972, 6, 6), "9986060550", "MBBS", "DIABETES", LocalDate.of(1999, 6, 6), null));
//            userService.saveDoctor(new Doctor(null, "yashas@gmail.com", "string1234", "Yashas", "M",
//                    LocalDate.of(1969, 6, 6), "9986060550", "MBBS", "ENT", LocalDate.of(2000, 6, 6), null));
//        };
//    }

//    @Bean
//    CommandLineRunner run(KafkaTemplate<String, String> kafkaTemplate) {
//        return args -> {
//            kafkaTemplate.send("topic-send-email", "hello");
//        };}
}
