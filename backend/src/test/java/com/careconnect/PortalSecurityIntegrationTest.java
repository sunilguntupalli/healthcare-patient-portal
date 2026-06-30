package com.careconnect;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PortalSecurityIntegrationTest {
  @Autowired MockMvc mvc;
  @Autowired ObjectMapper objectMapper;

  private JsonNode login(String email, String password) throws Exception {
    String response = mvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"email":"%s","password":"%s"}
                """.formatted(email, password)))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
    return objectMapper.readTree(response);
  }

  @Test
  void seededAccountsReceiveTheirAssignedRoles() throws Exception {
    org.junit.jupiter.api.Assertions.assertEquals("PATIENT", login("patient@careconnect.dev", "Password123!").get("role").asText());
    org.junit.jupiter.api.Assertions.assertEquals("DOCTOR", login("doctor@careconnect.dev", "Doctor123!").get("role").asText());
    org.junit.jupiter.api.Assertions.assertEquals("ADMIN", login("admin@careconnect.dev", "Admin123!").get("role").asText());
  }

  @Test
  void doctorCanReadScheduleButPatientCannotReadAdminData() throws Exception {
    String doctorToken = login("doctor@careconnect.dev", "Doctor123!").get("token").asText();
    String patientToken = login("patient@careconnect.dev", "Password123!").get("token").asText();

    mvc.perform(get("/api/doctor/appointments").header("Authorization", "Bearer " + doctorToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].patientName").value("Sunil Guntupalli"));

    mvc.perform(get("/api/admin/overview").header("Authorization", "Bearer " + patientToken))
        .andExpect(status().isForbidden());
  }

  @Test
  void invalidRegistrationReturnsFieldErrors() throws Exception {
    mvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"firstName":"","lastName":"Test","email":"not-an-email","password":"short"}
                """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.fields.firstName").exists())
        .andExpect(jsonPath("$.fields.email").exists())
        .andExpect(jsonPath("$.fields.password").exists());
  }
}
