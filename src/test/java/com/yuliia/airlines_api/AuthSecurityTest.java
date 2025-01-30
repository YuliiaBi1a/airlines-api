package com.yuliia.airlines_api;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuliia.airlines_api.airports.AirportDtoRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ContextConfiguration
public class AuthSecurityTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void testRoleClientCannotAccessPrivateEndpoints() throws Exception {
        mockMvc.perform(get("/api/v1/private/airports"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void testAnonymousUserCannotAccessProtectedEndpoints() throws Exception {
        mockMvc.perform(get("/api/v1/private/flights"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testUserUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/login"))
                .andExpect(unauthenticated());
    }

    @Test
    void testAdminUserCanLogin() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/v1/login").with(user("admin").password("adminpass").roles("ADMIN")))
                .andExpect(authenticated())
                .andReturn()
                .getResponse();

        assertThat(response.getStatus(), is(HttpStatus.ACCEPTED.value()));
        assertThat(response.getContentAsString(), containsString("Logged"));
    }

    @Test
    void testLogout() throws Exception {
        mockMvc.perform(get("/api/v1/logout"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(status().isNoContent());
    }
}

