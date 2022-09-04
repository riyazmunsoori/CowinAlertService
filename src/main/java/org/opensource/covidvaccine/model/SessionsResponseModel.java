package org.opensource.covidvaccine.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.util.List;

@Getter
@Setter(AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Builder(builderClassName = "SessionsResponseModelBuilder", toBuilder = true)
@JsonDeserialize(builder = SessionsResponseModel.SessionsResponseModelBuilder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionsResponseModel {

    private Integer centerId;
    private String name;
    private String address;
    private String stateName;
    private String districtName;
    private String blockName;
    private Integer pincode;
    private String from;
    private String to;
    private Integer latitude;
    private Integer longitude;
    private String feeType;
    private String sessionId;
    private String date;
    private Integer availableCapacityDose1;
    private Integer availableCapacityDose2;
    private Integer availableCapacity;
    private String fee;
    private Integer minAgeLimit;
    private String vaccineName;
    private List<String> slots;

    public static class SessionsResponseModelBuilder{
        @JsonProperty("center_id")
        private Integer centerId;

        @JsonProperty("name")
        private String name;

        @JsonProperty("address")
        private String address;

        @JsonProperty("state_name")
        private String stateName;

        @JsonProperty("district_name")
        private String districtName;

        @JsonProperty("block_name")
        private String blockName;

        @JsonProperty("pincode")
        private Integer pincode;

        @JsonProperty("from")
        private String slotTimeFrom;

        @JsonProperty("to")
        private String slotTimeTo;

        @JsonProperty("lat")
        private Integer latitude;

        @JsonProperty("long")
        private Integer longitude;

        @JsonProperty("fee_type")
        private String feeType;

        @JsonProperty("session_id")
        private String sessionId;

        @JsonProperty("date")
        private String date;

        @JsonProperty("available_capacity_dose1")
        private Integer availableCapacityDose1;

        @JsonProperty("available_capacity_dose2")
        private Integer availableCapacityDose2;

        @JsonProperty("available_capacity")
        private Integer availableCapacity;

        @JsonProperty("fee")
        private String fee;

        @JsonProperty("min_age_limit")
        private Integer minAgeLimit;

        @JsonProperty("vaccine")
        private String vaccineName;

        @JsonProperty("slots")
        private List<String> slots;
    }
}
