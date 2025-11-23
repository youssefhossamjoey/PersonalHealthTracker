package com.example.personalhealthtracker.JsonTests;

import com.example.personalhealthtracker.domain.dto.UserAccount;
import com.example.personalhealthtracker.domain.entities.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserAccountJsonTest {
    @Autowired
    private JacksonTester<UserAccount> json;

    @Test
    void UserAccountSerializationTest() throws IOException {
        UserAccount userAccount = new UserAccount(UUID.fromString("00000000-0000-0000-0000-000000000001"),"user1","pass1",Role.MEMBER,LocalDateTime.parse("2024-01-01T00:00:00"));
        assertThat(json.write(userAccount)).isStrictlyEqualToJson("/expectedUserAccount.json");

        assertThat(json.write(userAccount)).hasJsonPathValue("@.id");
        assertThat(json.write(userAccount)).extractingJsonPathValue("@.id")
                .isEqualTo("00000000-0000-0000-0000-000000000001");

        assertThat(json.write(userAccount)).hasJsonPathStringValue("@.username");
        assertThat(json.write(userAccount)).extractingJsonPathStringValue("@.username")
                .isEqualTo("user1");

        assertThat(json.write(userAccount)).hasJsonPathStringValue("@.password");
        assertThat(json.write(userAccount)).extractingJsonPathStringValue("@.password")
                .isEqualTo("pass1");

        assertThat(json.write(userAccount)).hasJsonPathStringValue("@.role");
        assertThat(json.write(userAccount)).extractingJsonPathStringValue("@.role")
                .isEqualTo("MEMBER");

    }
    @Test
    void UserAccountDeserializationTest() throws IOException {
        String expected = """
                        {
                          "id": "00000000-0000-0000-0000-000000000001",
                          "username": "user1",
                          "password": "pass1",
                          "role": "MEMBER",
                          "createdAt": "2024-01-01T00:00:00"
                        }
           """;
        assertThat(json.parse(expected))
                .isEqualTo(new UserAccount(UUID.fromString("00000000-0000-0000-0000-000000000001"),"user1","pass1", Role.MEMBER,LocalDateTime.parse("2024-01-01T00:00:00")));
        assertThat(json.parseObject(expected).getId()).isEqualTo(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        assertThat(json.parseObject(expected).getUsername()).isEqualTo("user1");
        assertThat(json.parseObject(expected).getPassword()).isEqualTo("pass1");
        assertThat(json.parseObject(expected).getRole()).isEqualTo(Role.MEMBER);
    }
}
