package com.yarishllc.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.jersey.api.client.GenericType;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FoodTruck {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("kk:mm");
    public static final GenericType<List<FoodTruck>> LIST_TYPE = new GenericType<List<FoodTruck>>() {
    };

    private static final String NAME_ATTR = "applicant";
    private static final String LOCATION_ATTR = "location";
    private static final String STARTTIME_ATTR = "start24";
    private static final String ENDTIME_ATTR = "end24";
    private static final String DAYOFWEEK_ATTR = "dayofweekstr";
    final String name;
    final String location;
    final LocalTime startTime;
    final LocalTime endTime;
    final DayOfWeek dayOfWeek;

    @JsonCreator
    public FoodTruck(@JsonProperty(NAME_ATTR) String name,
                     @JsonProperty(LOCATION_ATTR) String location,
                     @JsonProperty(STARTTIME_ATTR) String startTime,
                     @JsonProperty(ENDTIME_ATTR) String endTime,
                     @JsonProperty(DAYOFWEEK_ATTR) String dayOfWeek) {
        this.name = name;
        this.location = location;
        this.startTime = LocalTime.parse(startTime, FORMATTER);
        this.endTime = LocalTime.parse(endTime, FORMATTER);
        this.dayOfWeek = DayOfWeek.valueOf(dayOfWeek.toUpperCase());
    }


    @JsonProperty(NAME_ATTR)
    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    @JsonProperty(STARTTIME_ATTR)
    public LocalTime getStartTime() {
        return startTime;
    }

    @JsonProperty(ENDTIME_ATTR)
    public LocalTime getEndTime() {
        return endTime;
    }

    @JsonProperty(DAYOFWEEK_ATTR)
    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }


    @Override
    public int hashCode() {

        return Objects.hash(name, location, startTime, endTime, dayOfWeek);
    }

    @Override
    public String toString() {
        return "FoodTruck{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", dayOfWeek=" + dayOfWeek +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodTruck foodTruck = (FoodTruck) o;
        return Objects.equals(name, foodTruck.name) &&
                Objects.equals(location, foodTruck.location) &&
                Objects.equals(startTime, foodTruck.startTime) &&
                Objects.equals(endTime, foodTruck.endTime) &&
                dayOfWeek == foodTruck.dayOfWeek;
    }
}
