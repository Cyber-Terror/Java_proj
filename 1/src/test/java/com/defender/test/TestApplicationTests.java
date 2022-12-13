package com.defender.test;

import com.defender.test.model.Dorm;
import com.defender.test.model.Request;
import com.defender.test.model.User;
import com.defender.test.repositories.IDormRepository;
import com.defender.test.repositories.IRequestRepository;
import com.defender.test.repositories.IUserRepository;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class TestApplicationTests {

    @MockBean
    private IDormRepository dormRepository;

    @MockBean
    private IUserRepository userRepository;

    @MockBean
    private IRequestRepository requestRepository;

    @Autowired
    WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testAddDorm() {
        List<Dorm> dorms = Arrays.asList(
                new Dorm("champ 1"),
                new Dorm("champ 2")
        );
        when(dormRepository.findAll()).thenReturn(dorms);

        Assert.assertEquals(dormRepository.findAll(), dorms);
    }

    @Test
    public void testGetChampionships() throws Exception {
        setUp();
        List<Dorm> dorms = Arrays.asList(
                new Dorm("champ 1"),
                new Dorm("champ 2")
        );

        when(dormRepository.findAll()).thenReturn(dorms);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/auth/champs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[*].name", Matchers.containsInAnyOrder("champ 1", "champ 2")));
    }

    @Test
    public void testAddUser() {
        List<User> users = Arrays.asList(
                new User("user1","user1"),
                new User("user2","user2")
        );
        when(userRepository.findAll()).thenReturn(users);

        Assert.assertEquals(userRepository.findAll(), users);
    }

    @Test
    public void testGetRequests() throws Exception {
        setUp();

        Dorm ch = new Dorm("champ1");
        Dorm ch2 = new Dorm("champ2");
        User us = new User("Hello","Test");

        List<Request> requests = Arrays.asList(
                new Request("","Pending",us,ch),
                new Request("","Pending",us,ch2)
        );

        when(requestRepository.findAll()).thenReturn(requests);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/auth/reques"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[*].status", Matchers.containsInAnyOrder("Pending","Pending")));
    }
}
