package com.test.trade;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class ControllerMVCTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    SystemDateManager systemDateManager;

    @Test
    public void test_failure_message_for_unsupported_date_format() throws Exception {
        this.mockMvc.perform(post("/v1/trades")
                .content(asJsonString(new TradeInput("t-1", "c-1", "b-1", 1, "13/04/2010")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(equalTo("Expected date format is ISO date format 'YYYY-MM-DD'")))
                .andDo(mvcResult -> System.out.println(mvcResult.getResponse().getContentAsString()));
    }

    @Test
    public void test_failure_message_for_earlier_maturity() throws Exception {
        this.mockMvc.perform(post("/v1/trades")
                .content(asJsonString(new TradeInput("t-1", "c-1", "b-1", 1, "2010-12-04")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("Unable to add trade with maturity date")))
                .andDo(mvcResult -> System.out.println(mvcResult.getResponse().getContentAsString()));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

