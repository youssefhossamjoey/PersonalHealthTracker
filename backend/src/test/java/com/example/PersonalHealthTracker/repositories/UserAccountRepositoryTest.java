package com.example.personalhealthtracker.repositories;

import com.example.personalhealthtracker.domain.entities.Role;
import com.example.personalhealthtracker.domain.entities.UserAccountEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserAccountRepositoryTest {
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Test
    public void UserAccountCanBeCreated() {
        UserAccountEntity user = new UserAccountEntity("user1", "pass1", Role.MEMBER);
        assertNull(user.getId());
        UserAccountEntity saved = userAccountRepository.save(user);

        assertNotNull(saved.getId());
        assertDoesNotThrow(() -> UUID.fromString(saved.getId().toString()));

    }

    @Test
    public void UserAccountCanBeCreatedAndFetched() {
        UserAccountEntity user = new UserAccountEntity("user1", "pass1", Role.MEMBER);
        UserAccountEntity saved = userAccountRepository.save(user);

        UUID id = saved.getId();
        Optional<UserAccountEntity> fetched = userAccountRepository.findById(id);

        assertThat(fetched.isPresent());
        assertThat(fetched.get().getUsername()).isEqualTo("user1");

    }

    @Test
    public void MultipleUserAccountCanBeCreatedAndFetched() {
        UserAccountEntity user = new UserAccountEntity("user1", "pass1", Role.MEMBER);
        UserAccountEntity saved = userAccountRepository.save(user);

        user = new UserAccountEntity("user2", "pass2", Role.MEMBER);
        saved = userAccountRepository.save(user);

        user = new UserAccountEntity("user3", "pass3", Role.MEMBER);
        saved = userAccountRepository.save(user);

        List<UserAccountEntity> fetched = StreamSupport.stream(userAccountRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        assertThat(fetched.size()).isEqualTo(3);
        assertThat(fetched.get(0).getUsername()).isEqualTo("user1");
        assertThat(fetched.get(1).getUsername()).isEqualTo("user2");
        assertThat(fetched.get(2).getUsername()).isEqualTo("user3");
    }

    @Test
    @DirtiesContext
    public void MultiplePagedUserAccountCanBeCreatedAndFetched() {
        UserAccountEntity user = new UserAccountEntity("user1", "pass1", Role.MEMBER);
        UserAccountEntity saved = userAccountRepository.save(user);

        user = new UserAccountEntity("user2", "pass2", Role.MEMBER);
        saved = userAccountRepository.save(user);

        user = new UserAccountEntity("user3", "pass3", Role.MEMBER);
        saved = userAccountRepository.save(user);

        Pageable pageable;
        Page<UserAccountEntity> fetched = userAccountRepository
                .findAll(PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "username")));

        assertThat(fetched.getContent().size()).isEqualTo(1);
        assertThat(fetched.getTotalPages()).isEqualTo(3);
        assertThat(fetched.getContent().get(0).getUsername()).isEqualTo("user1");

        assertThat(fetched.getNumber()).isEqualTo(0); // page index
        assertThat(fetched.getSize()).isEqualTo(1); // page size requested
        assertThat(fetched.hasNext()).isTrue(); // page 0 has next page
    }
}
