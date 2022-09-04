import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensource.covidvaccine.model.SessionsResponseModel;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class SessionsResponseModelTest {
    public static void main(String[] args) throws IOException, JSONException {
        JSONObject jsonObject = new JSONObject(loadFileAsString("src/test/resources/sessionModel.json"));
        JSONArray jsonArray = jsonObject.getJSONArray("sessions");
        System.out.println(jsonArray);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        CollectionType collectionType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, SessionsResponseModel.class);

        List<SessionsResponseModel> sessionsResponseModelList = objectMapper.readValue(jsonArray.toString(), collectionType);
        System.out.println(sessionsResponseModelList);
    }

    private static String loadFileAsString(String filePath) throws IOException {
        return Files.readString(Paths.get(filePath), StandardCharsets.UTF_8);
    }
}
