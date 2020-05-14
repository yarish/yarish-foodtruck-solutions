package com.yarishllc.app;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.socrata.api.Soda2Consumer;
import com.socrata.builders.SoqlQueryBuilder;
import com.socrata.exceptions.SodaError;
import com.socrata.model.soql.OrderByClause;
import com.socrata.model.soql.SoqlQuery;
import com.socrata.model.soql.SortOrder;
import com.yarishllc.model.FoodTruck;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        try {
            SpringApplication.run(Application.class, args);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        System.exit(0);
    }

    @Override
    public void run(String[] args) throws IOException, SodaError, InterruptedException {
        final Soda2Consumer consumer = Soda2Consumer.newConsumer("https://data.sfgov.org");

        final String timeNow = LocalTime.now().format(FoodTruck.FORMATTER);
        final String dayOfWeek;

        {
            String dayOfWeekIntermediate = LocalDate.now().getDayOfWeek().toString();
            char[] dayOfWeekArray = dayOfWeekIntermediate.trim().toLowerCase().toCharArray();
            dayOfWeekArray[0] = Character.toUpperCase(dayOfWeekArray[0]);
            dayOfWeek = new String(dayOfWeekArray);
        }

        SoqlQuery deptOfStateQuery = new SoqlQueryBuilder()
                .addSelectPhrase("applicant")
                .addSelectPhrase("location")
                .addSelectPhrase("start24")
                .addSelectPhrase("end24")
                .addSelectPhrase("dayofweekstr")
                .setWhereClause(String.format("start24 <= '%s' AND end24 >= '%s' AND dayofweekstr = '%s'", timeNow, timeNow, dayOfWeek))
                .addOrderByPhrase(new OrderByClause(SortOrder.Ascending, "applicant"))
                .build();

        final List<FoodTruck> foodTrucks = consumer.query("jjew-r69b", deptOfStateQuery, FoodTruck.LIST_TYPE);

        final AtomicInteger counter = new AtomicInteger(0);
        final AtomicInteger width = new AtomicInteger(0);

        Collection<List<FoodTruck>> foodTruckPages = foodTrucks.stream().collect(Collectors.groupingBy(it -> {
            int length = it.getName().length();
            width.getAndUpdate(x -> x < length ? length : x);
            return counter.getAndIncrement() / 10;
        })).values();

        int columnWidth = width.get() + 1;

        final String columnFormat = "%-" + columnWidth + "s%s";

        for (List<FoodTruck> foodTruckList : foodTruckPages) {
            System.out.println(String.format(columnFormat, "Name", "Location"));

            for (FoodTruck foodTruck : foodTruckList) {
                System.out.println(String.format(columnFormat, foodTruck.getName(), foodTruck.getLocation()));
            }

            System.out.println();
            System.out.println("Press CTRL + C to terminate the program");
            System.out.println("Press any key to continue.");
            System.in.read();
        }
    }

}
