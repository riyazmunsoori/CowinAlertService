package org.opensource.covidvaccine.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensource.covidvaccine.model.SessionsResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.List;

@Component
public class VaccineAlertController {

    @Autowired
    private JavaMailSender mailSender;

    public void runService() throws IOException, JSONException, MessagingException {
        String cowinApiURL = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin";
        CloseableHttpClient httpClient = HttpClients.custom().build();

        String[] pinCodeList = {"110085"};
        String[] dateList = {"13-07-2021"};

        //delhi state id 9
        //north west delhi - id 143
        //north delhi - id 146
        for(String date: dateList){
            for(String pincode: pinCodeList){
                HttpUriRequest vaccineByPinRequest = RequestBuilder.get()
                        .setUri(cowinApiURL)
                        .addParameter("pincode", pincode)
                        .addParameter("date", date)
                        .setHeader(HttpHeaders.ACCEPT, "application/json")
                        .setHeader(HttpHeaders.USER_AGENT, "Chrome/90.0.4430.93")
                        .build();

                System.out.println("Request URL: " + vaccineByPinRequest.getURI());

                CloseableHttpResponse response = httpClient.execute(vaccineByPinRequest);
                if(response.getStatusLine().getStatusCode() == 200){
                    System.out.println("GET Request successful to Cowin API. Checking if Vaccine available");
                    HttpEntity entity = response.getEntity();
                    if(entity != null) {
                        List<SessionsResponseModel> sessionsResponseModelList = parseJsonToModel(EntityUtils.toString(entity));
                        sendEmailAlert(sessionsResponseModelList);
                    }
                }
            }
        }

        httpClient.close();
    }

    private void sendEmailAlert(List<SessionsResponseModel> sessionsResponseModelList) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        for(SessionsResponseModel session: sessionsResponseModelList){
            if(session.getAvailableCapacityDose1() > 0 && session.getMinAgeLimit() == 45
                    && (session.getVaccineName().equalsIgnoreCase("COVISHIELD")) && (session.getFeeType().equalsIgnoreCase("FREE"))){
                //TODO fetch from EVA vault
                helper.setFrom("");
                helper.setSubject("Covid Vaccine Slot Open: Alert");

                System.out.println("Found open slot for Covishield Dose 1 - Creating email alert");
                //TODO fetch from UI input over HTTPS
                helper.setTo(new String[]{""});

                helper.setText(getEmailText(session));
                System.out.println("Sending email alert: ");
                mailSender.send(mimeMessage);
                System.out.println("Email Alert sent successfully");
            }
        }
    }

    private List<SessionsResponseModel> parseJsonToModel(String jsonResponse) throws JSONException, JsonProcessingException {
        JSONObject jsonObject = new JSONObject(jsonResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        CollectionType collectionType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, SessionsResponseModel.class);

        return objectMapper.readValue(jsonObject.getJSONArray("sessions").toString(), collectionType);
    }

    private String getEmailText(SessionsResponseModel session){
        StringBuilder emailTextBuilder = new StringBuilder();

        emailTextBuilder.append("Vaccine Center Available (See the details below): ");
        emailTextBuilder.append("\n Center Name: ");
        emailTextBuilder.append(session.getName());
        emailTextBuilder.append("\n Center Address: ");
        emailTextBuilder.append(session.getAddress());
        emailTextBuilder.append("\n Center BlockName: ");
        emailTextBuilder.append(session.getBlockName());
        emailTextBuilder.append("\n Center DistrictName: ");
        emailTextBuilder.append(session.getDistrictName());
        emailTextBuilder.append("\n Pincode: ");
        emailTextBuilder.append(session.getPincode());
        emailTextBuilder.append("\n Date: ");
        emailTextBuilder.append(session.getDate());
        emailTextBuilder.append("\n Vaccine Name: ");
        emailTextBuilder.append(session.getVaccineName());
        emailTextBuilder.append("\n Min Age Limit: ");
        emailTextBuilder.append(session.getMinAgeLimit());
        emailTextBuilder.append("\n Available Slots: ");
        emailTextBuilder.append(session.getSlots());
        emailTextBuilder.append("\n Capacity Remaining: ");
        emailTextBuilder.append(session.getAvailableCapacity());
        emailTextBuilder.append("\n Fee Type: ");
        emailTextBuilder.append(session.getFeeType());
        emailTextBuilder.append("\n Fee Amount: ");
        emailTextBuilder.append(session.getFee());

        return emailTextBuilder.toString();
    }
}
