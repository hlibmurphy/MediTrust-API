package com.github.edocapi.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.edocapi.dto.RecallDto;
import com.github.edocapi.dto.RecallRequestDto;
import com.github.edocapi.model.Recall;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecallControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Post recall")
    public void addNumber_withRecallRequestDto_shouldReturnNumber() throws Exception {
        RecallRequestDto requestDto = createRecallRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(
                post("/recall")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();

        RecallDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), RecallDto.class);

        RecallDto expected = createRecallDto();
        Assertions.assertEquals(expected, actual,
                "The retrieved DTO should match the expected one");
    }

    private RecallRequestDto createRecallRequestDto() {
        return new RecallRequestDto()
                .setNumber("0123456789");
    }

    private RecallDto createRecallDto() {
        return new RecallDto()
                .setId(1L)
                .setNumber("0123456789")
                .setStatus(Recall.Status.PENDING);
    }
}
