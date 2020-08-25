package ca.toantrieu.dms.Controller;

import ca.toantrieu.dms.Model.AuthorizationRequest;
import ca.toantrieu.dms.Repository.UserRepository;
import ca.toantrieu.dms.Service.CustomUserDetailsServiceImpl;
import ca.toantrieu.dms.Service.CustomUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.annotations.Filter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Mock
    private UserRepository userRepository;

//    @Test
//    void welcome() throws Exception {
////        RequestBuilder request = MockMvcRequestBuilders.get("/");
////        MvcResult result = mvc.perform(request).andReturn();
////        assertEquals("Welcome to Dine-in Management System", result.getResponse().getContentAsString());
//    }
//    @Test
//    void welcomeAdmin() {
////        UserController mockController = new UserController();
////        String response = mockController.welcomeAdmin();
////        assertEquals("Welcome to Admin Page", response);
//    }
//
//    @Test
//    void getAllUsers() {
//    }
//
//    @Test
//    void getUserById() {
//    }
//
//    @Test
//    void createNewUser() {
//    }
//
//    @Test
//    void updateUserUserName() {
//    }
//
//    @Test
//    void updateUserEmail() {
//    }
//
//    @Test
//    void updateUserPassword() {
//    }
//
//    @Test
//    void deleteUser() {
//    }
//
//    @Test
//    void refreshToken() {
//    }
//
//    @Test
//    void register() {
//    }

//    private static String asJsonString(final Object obj) {
//        try {
//            final ObjectMapper mapper = new ObjectMapper();
//            final String jsonContent = mapper.writeValueAsString(obj);
//            return jsonContent;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

//    @Test
//    void existingUserCanGetTokenAndAuthenticated() throws Exception {
//        String username = "admin";
//        String password = "password";
//        AuthorizationRequest mockUserInfo = new AuthorizationRequest(username, password);
////        String body = "{\"userName\":\"" + username + "\", \"password\":\"" + password + "\"}";
//        RequestBuilder request = MockMvcRequestBuilders
//                .post("/login")
//                .content(asJsonString(mockUserInfo))
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON);
//        MvcResult result = mvc.perform(request).andExpect(status().isOk()).andReturn();
//        JSONArray arr = new JSONArray(result.getResponse().getContentAsString());
//        String token = (String) arr.get(0);
//        //Get user roles
////        for (int index = 0; index < arr.length(); index++){
////
////        }
//        //Check token
//
//        mvc.perform(MockMvcRequestBuilders.get("/")
//            .header("Authorization", "Bearer " + token)).andExpect(status().isOk());
//    }

//    @Test
//    void nonExistingUserCannotLogin() throws Exception {
//        String username = "anonymous";
//        String password = "password";
//        AuthorizationRequest mockUserInfo = new AuthorizationRequest(username, password);
//        mvc.perform(MockMvcRequestBuilders
//                .post("/login")
//                .content(asJsonString(mockUserInfo))
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isForbidden()).andReturn();
//    }


}