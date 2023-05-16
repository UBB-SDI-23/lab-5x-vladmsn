package com.expense_tracker;

import com.expense_tracker.model.db.GroupEntity;
import com.expense_tracker.model.db.UserEntity;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;


@Slf4j
@ActiveProfiles("test")
@Testcontainers
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public abstract class AbstractTest {

    @Autowired
    protected MockMvc mockMvc;

    public static UserEntity getDefaultUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("john.doe");
        userEntity.setPassword("password");
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");
        userEntity.setEmail("john.doe@gmail.com");
        userEntity.setPreferredCurrency("USD");
        return userEntity;
    }

    public static GroupEntity getDefaultGroup() {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setName("Group 1");
        groupEntity.setPreferredCurrency("USD");
        groupEntity.setDescription("Group 1 description");
        groupEntity.setCreatedAt(LocalDate.now());
        groupEntity.setUpdatedAt(LocalDate.now());
        return groupEntity;
    }
}
