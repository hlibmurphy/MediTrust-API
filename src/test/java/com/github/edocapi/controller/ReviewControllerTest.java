package com.github.edocapi.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.edocapi.dto.CreateReviewRequestDto;
import com.github.edocapi.dto.ReviewDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReviewControllerTest {
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
    @DisplayName("Get all reviews")
    @Sql(scripts = {
            "classpath:db/doctor_schedules/add-schedule-to-doctor-schedules-table.sql",
            "classpath:db/doctors/add-doctor-to-doctors-table.sql",
            "classpath:db/reviews/add-review-to-reviews-table.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/reviews/remove-review-from-reviews-table.sql",
            "classpath:db/doctors/remove-doctor-from-doctors-table.sql",
            "classpath:db/doctor_schedules/remove-schedule-from-doctor-schedules-table.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllReviews_withPageable_shouldReturnAllReviews() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/reviews")
                )
                .andExpect(status().isOk())
                .andReturn();

        ReviewDto[] actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                ReviewDto[].class);
        Assertions.assertEquals(1, actual.length);
        Assertions.assertEquals(1L, actual[0].getId());
        Assertions.assertEquals("Text", actual[0].getText());
    }

    @Test
    @DisplayName("Create a review")
    @Sql(scripts = {
            "classpath:db/users/add-user-to-users-table.sql",
            "classpath:db/users_roles/add-user-and-role-to-users-roles-table.sql",
            "classpath:db/doctor_schedules/add-schedule-to-doctor-schedules-table.sql",
            "classpath:db/doctors/add-doctor-to-doctors-table.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/reviews/remove-review-from-reviews-table.sql",
            "classpath:db/doctors/remove-doctor-from-doctors-table.sql",
            "classpath:db/doctor_schedules/remove-schedule-from-doctor-schedules-table.sql",
            "classpath:db/users_roles/remove-user-and-role-from-users-roles-table.sql",
            "classpath:db/users/remove-user-from-users-table.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails("0111111111")
    public void createReview_withCreateReviewRequestDto_shouldCreateReview() throws Exception {
        CreateReviewRequestDto createReviewRequestDto = createReviewRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(createReviewRequestDto);
        MvcResult result = mockMvc.perform(
                        post("/reviews")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        ReviewDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                ReviewDto.class);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals("Text", actual.getText());
    }

    private CreateReviewRequestDto createReviewRequestDto() {
        return new CreateReviewRequestDto()
                .setText("Text")
                .setRating(5)
                .setDoctorId(1L);
    }
}
