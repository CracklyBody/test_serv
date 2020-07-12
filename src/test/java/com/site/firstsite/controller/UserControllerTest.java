package com.site.firstsite.controller;

import com.site.firstsite.controller.abstracted.AbstractTest;
import com.site.firstsite.domain.User;
import com.site.firstsite.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Timestamp;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class UserControllerTest extends AbstractTest {

    private final String REST_CRET = "/registration/";
    private final String REST_URL="/main/";
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

//    @PostConstruct
//    void postConstruct(){
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//    }

    @Test
    public void whenGetUser_thenStatusOk() throws Exception{
        String stats =super.mapToJson("true");

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        User[] productlist = super.mapFromJson(content, User[].class);
        assertTrue(productlist.length > 0);
//        mockMvc.perform(get(REST_URL+ USER_ID))
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    public void whenCreateUser_thenStatusCreated() throws Exception{
        User user = new User();
        user.setUsername("Mich132rr4432ail");
        user.setPassword("1");
        user.setEmail("ff@gmail.com");
        user.setUrim("C:/Users/Max/Pictures/giphy.gif");
        user.setStatusTimestamp(new Timestamp((new Date()).getTime()));
        user.setStatus(true);

        String inputJson = super.mapToJson(user);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(REST_CRET)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andExpect(status().isOk()).andReturn();

        int status = mvcResult.getResponse().getStatus();

        String content = mvcResult.getResponse().getContentAsString();
        //assertThat(response.getBody(), notNullValue());
        //assertThat(response.getBody().getName(), is("Michail"));
    }
    @Test
    public void whenUpdateUser_thenStatusOk() throws Exception{
        String inputJson = new String("false");
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(REST_URL+35)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
    }
}
