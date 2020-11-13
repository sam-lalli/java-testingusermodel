package com.lambdaschool.usermodel.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import com.lambdaschool.usermodel.services.HelperFunctions;
import com.lambdaschool.usermodel.services.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.swing.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class)
public class UserControllerUnitTest {

    //autowire mvc
    //mock your service, functions, and a list
        //we dont want to use the actual data or functions in testing

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    HelperFunctions helperFunctions;

    List<User> userList;

    @Before
    public void setUp() throws Exception {
        //we want to duplicate seedData so we don't actually use it
        userList = new ArrayList<>();

        Role r1 = new Role("Test admin");
        Role r2 = new Role("Test user");
        Role r3 = new Role("Test data");

        //cant use roleservice, going to set own id (can be whatever you want, nothing can have same id)
        r1.setRoleid(1);
        r2.setRoleid(2);
        r3.setRoleid(3);

        // admin, data, user
        User u1 = new User("admin",
                "password",
                "admin@lambdaschool.local");
        u1.getRoles()
                .add(new UserRoles(u1,
                        r1));
        u1.getRoles()
                .add(new UserRoles(u1,
                        r2));
        u1.getRoles()
                .add(new UserRoles(u1,
                        r3));
        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@email.local"));
        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@mymail.local"));

        //need to set user ids cant use save method
        u1.setUserid(11);

        // data, user
        User u2 = new User("Test cinnamon",
                "1234567",
                "cinnamon@lambdaschool.local");
        u2.getRoles()
                .add(new UserRoles(u2,
                        r2));
        u2.getRoles()
                .add(new UserRoles(u2,
                        r3));
        u2.getUseremails()
                .add(new Useremail(u2,
                        "cinnamon@mymail.local"));
        u2.getUseremails()
                .add(new Useremail(u2,
                        "hops@mymail.local"));
        u2.getUseremails()
                .add(new Useremail(u2,
                        "bunny@email.local"));

        u2.setUserid(12);

        // user
        User u3 = new User("Test barnbarn",
                "ILuvM4th!",
                "barnbarn@lambdaschool.local");
        u3.getRoles()
                .add(new UserRoles(u3,
                        r2));
        u3.getUseremails()
                .add(new Useremail(u3,
                        "barnbarn@email.local"));

        u3.setUserid(13);


        User u4 = new User("Test puttat",
                "password",
                "puttat@school.lambda");
        u4.getRoles()
                .add(new UserRoles(u4,
                        r2));

        u4.setUserid(14);


        User u5 = new User("Test misskitty",
                "password",
                "misskitty@school.lambda");
        u5.getRoles()
                .add(new UserRoles(u5,
                        r2));

        u5.setUserid(15);

        //need to add users to list
        userList.add(u1);
        userList.add(u2);
        userList.add(u3);
        userList.add(u4);
        userList.add(u5);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void listAllUsers() throws Exception {
        //URL
        String apiUrl = "/users/users";
        //What we want to run the method on (userList)
        Mockito.when(userService.findAll()).thenReturn(userList);

        //build request consumming json, taking contents and converting it as a string
        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult u = mockMvc.perform(rb).andReturn();
        String testResult = u.getResponse().getContentAsString();

        //need to turn the expected result into a string so it is comparable to testResult
        ObjectMapper mapper = new ObjectMapper();
        String expectedResult = mapper.writeValueAsString(userList);

        assertEquals(expectedResult, testResult);

    }

    @Test
    public void getUserById() throws Exception {

        String apiUrl = "/users/user/12";
        Mockito.when(userService.findUserById(12)).thenReturn(userList.get(1));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult u = mockMvc.perform(rb).andReturn();
        String testResult = u.getResponse().getContentAsString();

        //need to turn the expected result into a string so it is comparable to testResult
        ObjectMapper mapper = new ObjectMapper();
        String expectedResult = mapper.writeValueAsString((userList.get(1)));

        assertEquals(expectedResult, testResult);

    }

    @Test
    public void getUserByName() throws Exception{

        String apiUrl = "/users/user/name/admin";
        Mockito.when(userService.findByName("admin")).thenReturn(userList.get(0));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult u = mockMvc.perform(rb).andReturn();
        String testResult = u.getResponse().getContentAsString();

        //need to turn the expected result into a string so it is comparable to testResult
        ObjectMapper mapper = new ObjectMapper();
        String expectedResult = mapper.writeValueAsString((userList.get(0)));

        assertEquals(expectedResult, testResult);
    }

    @Test
    public void getUserLikeName() throws Exception{
        String apiUrl = "/users/user/name/like/ci";
        Mockito.when(userService.findByNameContaining("ci")).thenReturn(userList);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult u = mockMvc.perform(rb).andReturn();
        String testResult = u.getResponse().getContentAsString();

        //need to turn the expected result into a string so it is comparable to testResult
        ObjectMapper mapper = new ObjectMapper();
        String expectedResult = mapper.writeValueAsString((userList));

        assertEquals(expectedResult, testResult);
    }

    @Test
    public void addNewUser() throws Exception {

        String apiUrl = "/users/user";

        String testName = "test userbunny";
        User u3 = new User(testName,
                "ILuvM4th!",
                "test@lambdaschool.local");

        Role r2 = new Role("tester");
        r2.setRoleid(1);

        u3.getRoles()
                .add(new UserRoles(u3,
                        r2));
        u3.getUseremails()
                .add(new Useremail(u3,
                        "barnbarn@email.local"));

        ObjectMapper mapper = new ObjectMapper();
        String restaurantString = mapper.writeValueAsString(u3);

        Mockito.when(userService.save(any(User.class))).thenReturn(u3);

        RequestBuilder rb = MockMvcRequestBuilders.post(apiUrl)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(restaurantString);

        mockMvc.perform(rb).andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateFullUser() {
    }

    @Test
    public void updateUser() {
    }

    @Test
    public void deleteUserById() throws Exception {
        String apiUrl = "/users/user/11";
        RequestBuilder rb = MockMvcRequestBuilders.delete(apiUrl);
        mockMvc.perform(rb).andExpect(status().is2xxSuccessful()).andDo(MockMvcResultHandlers.print());
    }
}