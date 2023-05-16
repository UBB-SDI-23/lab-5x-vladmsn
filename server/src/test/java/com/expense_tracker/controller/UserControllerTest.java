//package com.expense_tracker.controller;
//
//import com.expense_tracker.AbstractTest;
//import com.expense_tracker.model.Group;
//import com.expense_tracker.model.User;
//import com.expense_tracker.model.db.GroupEntity;
//import com.expense_tracker.model.db.GroupUserEntity;
//import com.expense_tracker.model.db.UserEntity;
//import com.expense_tracker.model.user.UserExpenses;
//import com.expense_tracker.model.user.UserGroupDetails;
//import com.expense_tracker.repository.GroupRepository;
//import com.expense_tracker.repository.GroupUserRepository;
//import com.expense_tracker.repository.UserRepository;
//import com.expense_tracker.service.UserService;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.test.annotation.DirtiesContext;
//import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
//import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Objects;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SuppressWarnings("OptionalGetWithoutIsPresent")
//public class UserControllerTest extends AbstractTest {
//
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private GroupRepository groupRepository;
//    @Autowired
//    private GroupUserRepository groupUserRepository;
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;
//
//    protected final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Test
//    public void testGetAllUsers() throws Exception {
//
//        UserEntity userEntity1 = getDefaultUser();
//        userEntity1.setUsername("john.doe");
//        userEntity1.setEmail("john.doe1@gmail.com");
//        userEntity1.setCreatedAt(LocalDate.now());
//        userEntity1.setUpdatedAt(LocalDate.now());
//
//        UserEntity userEntity2 = getDefaultUser();
//        userEntity2.setUsername("john.doe2");
//        userEntity2.setEmail("john.doe2@gmail.com");
//        userEntity2.setCreatedAt(LocalDate.now());
//        userEntity2.setUpdatedAt(LocalDate.now());
//
//        userRepository.save(userEntity1);
//        userRepository.save(userEntity2);
//
//        String result = mockMvc.perform(get("/api/v1/users")
//                        .contentType("application/json"))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        User[] users = objectMapper.readValue(result, User[].class);
//
//        assertResponseUser(userEntity1, users[0], 1);
//        assertResponseUser(userEntity2, users[1], 2);
//    }
//
//    @Test
//    public void testSaveUser() throws Exception {
//
//        UserEntity userEntity = getDefaultUser();
//
//        String result = mockMvc.perform(post("/api/v1/users")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(userEntity)))
//                .andExpect(status().isCreated())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        User user = objectMapper.readValue(result, User.class);
//        assertResponseUser(userEntity, user, 1);
//
//        Assertions.assertEquals(1, userRepository.count());
//        Assertions.assertNotNull(userRepository.findById(1).get());
//        Assertions.assertEquals(userEntity.getUsername(), userRepository.findById(1).get().getUsername());
//        Assertions.assertTrue(passwordEncoder.matches(userEntity.getPassword(), userRepository.findById(1).get().getPassword()));
//        Assertions.assertEquals(userEntity.getFirstName(), userRepository.findById(1).get().getFirstName());
//        Assertions.assertEquals(userEntity.getLastName(), userRepository.findById(1).get().getLastName());
//        Assertions.assertEquals(userEntity.getEmail(), userRepository.findById(1).get().getEmail());
//        Assertions.assertEquals(userEntity.getPreferredCurrency(), userRepository.findById(1).get().getPreferredCurrency());
//    }
//
//    @Test
//    public void testSaveUserWithExistingUsername() throws Exception {
//
//        UserEntity userEntity = getDefaultUser();
//        userEntity.setCreatedAt(LocalDate.now());
//        userEntity.setUpdatedAt(LocalDate.now());
//
//        userRepository.save(userEntity);
//
//        UserEntity userEntity1 = getDefaultUser();
//        mockMvc.perform(post("/api/v1/users")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(userEntity1)))
//                .andExpect(status().isConflict());
//    }
//
//    @Test
//    public void testSaveUserWithInvalidEmail() throws Exception {
//
//        UserEntity userEntity = getDefaultUser();
//        userEntity.setEmail("invalidEmail");
//
//        mockMvc.perform(post("/api/v1/users")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(userEntity)))
//                .andExpect(status().isBadRequest())
//                .andExpect(result -> Assertions.assertTrue(Objects.requireNonNull(result.getResolvedException())
//                        .getMessage().contains("Email is not valid. \n")));
//    }
//
//    @Test
//    void testUserGroupDetails() throws Exception {
//        UserEntity userEntity = getDefaultUser();
//        userEntity.setCreatedAt(LocalDate.now());
//        userEntity.setUpdatedAt(LocalDate.now());
//        userRepository.save(userEntity);
//
//        GroupEntity groupEntity = getDefaultGroup();
//        groupEntity.setCreatedAt(LocalDate.now());
//        groupRepository.save(groupEntity);
//
//        GroupUserEntity groupUserEntity = new GroupUserEntity();
//        GroupUserEntity.GroupUsersPk groupUserPk =  new GroupUserEntity.GroupUsersPk();
//        groupUserPk.setGroupId(groupEntity.getId());
//        groupUserPk.setUserId(userEntity.getId());
//        groupUserEntity.setPk(groupUserPk);
//        groupUserEntity.setJoinedAt(LocalDate.now());
//        groupUserEntity.setIsActive(true);
//        groupUserEntity.setNickname("nickname");
//        groupUserRepository.save(groupUserEntity);
//
//
//        String result = mockMvc.perform(get("/api/v1/users/group_details/{id}", 1)
//                        .contentType("application/json"))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        List<UserGroupDetails> userGroupDetails = objectMapper.readValue(result, new TypeReference<List<UserGroupDetails>>(){});
//        Assertions.assertEquals(1, userGroupDetails.size());
//
//        UserGroupDetails userGroupDetails1 = userGroupDetails.get(0);
//        Assertions.assertEquals(userEntity.getId(), userGroupDetails1.getUserId());
//        Assertions.assertEquals(userEntity.getUsername(), userGroupDetails1.getUsername());
//        Assertions.assertEquals(userEntity.getFirstName(), userGroupDetails1.getFirstName());
//        Assertions.assertEquals(userEntity.getLastName(), userGroupDetails1.getLastName());
//        Assertions.assertEquals(userEntity.getEmail(), userGroupDetails1.getEmail());
//        Assertions.assertEquals(userEntity.getPreferredCurrency(), userGroupDetails1.getPreferredCurrency());
//        Assertions.assertEquals(0, userGroupDetails1.getBalance());
//        Assertions.assertEquals(0, userGroupDetails1.getTotalPayed());
//        Assertions.assertEquals(0, userGroupDetails1.getTotalShare());
//    }
//
//    @Test
//    public void testUserExpensesByGroupId() throws Exception {
//
//        UserEntity userEntity = getDefaultUser();
//        userEntity.setCreatedAt(LocalDate.now());
//        userEntity.setUpdatedAt(LocalDate.now());
//        userRepository.save(userEntity);
//
//        GroupEntity groupEntity = getDefaultGroup();
//        groupRepository.save(groupEntity);
//
//        GroupUserEntity groupUserEntity = new GroupUserEntity();
//        GroupUserEntity.GroupUsersPk groupUserPk =  new GroupUserEntity.GroupUsersPk();
//        groupUserPk.setGroupId(groupEntity.getId());
//        groupUserPk.setUserId(userEntity.getId());
//        groupUserEntity.setPk(groupUserPk);
//        groupUserEntity.setNickname("nickname");
//        groupUserEntity.setJoinedAt(LocalDate.now());
//        groupUserEntity.setIsActive(true);
//        groupUserRepository.save(groupUserEntity);
//
//        String result = mockMvc.perform(get("/api/v1/users/details/{id}", 1)
//                        .queryParam("group_id", String.valueOf(1))
//                        .contentType("application/json"))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        UserExpenses userExpenses = objectMapper.readValue(result, UserExpenses.class);
//
//        Assertions.assertEquals(userEntity.getId(), userExpenses.getId());
//        Assertions.assertEquals(userEntity.getUsername(), userExpenses.getUsername());
//        Assertions.assertEquals(userEntity.getPreferredCurrency(), userExpenses.getPreferredCurrency());
//        Assertions.assertEquals(List.of(), userExpenses.getExpenses());
//    }
//
//
//    private void assertResponseUser(UserEntity userEntity, User user, int id) {
//        Assertions.assertEquals(id, user.getId());
//        Assertions.assertEquals(userEntity.getUsername(), user.getUsername());
//        Assertions.assertEquals(userEntity.getFirstName(), user.getFirstName());
//        Assertions.assertEquals(userEntity.getLastName(), user.getLastName());
//        Assertions.assertEquals(userEntity.getEmail(), user.getEmail());
//        Assertions.assertEquals(userEntity.getPreferredCurrency(), user.getPreferredCurrency());
//    }
//}
