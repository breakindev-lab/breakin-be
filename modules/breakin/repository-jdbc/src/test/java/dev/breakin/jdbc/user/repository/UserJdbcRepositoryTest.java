package dev.breakin.jdbc.user.repository;

import dev.breakin.model.common.NotificationSettings;
import dev.breakin.model.user.User;
import dev.breakin.model.user.UserIdentity;
import dev.breakin.model.user.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * UserJdbcRepository 테스트
 *
 * @DataJdbcTest를 사용한 Spring Data JDBC 통합 테스트
 * Entity ↔ Domain 변환 로직 및 커스텀 쿼리 메서드 검증
 */
@DataJdbcTest
@ComponentScan("dev.breakin.jdbc.user.repository")
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserJdbcRepositoryTest {

    @Autowired
    private UserJdbcRepository userRepository;

    // 테스트 데이터
    private final User sampleUser = new User(
            null,                           // userId (자동 생성)
            "google123",                    // googleId
            "test@example.com",             // email
            "testNickname",                 // nickname
            UserRole.USER,                  // userRole
            List.of("Company A"),           // interestedCompanies
            List.of("Seoul"),               // interestedLocations
            NotificationSettings.defaultSettings(), // notificationSettings
            0L,                             // postCount
            0L,                             // commentCount
            0L,                             // likesReceived
            Instant.now(),                  // lastLoginAt
            true,                           // isActive
            false,                          // isWithdrawn
            null,                           // withdrawnAt
            Instant.now(),                  // createdAt
            Instant.now()                   // updatedAt
    );

    private final UserIdentity testIdentity = new UserIdentity(1L);
    private final UserIdentity nonExistingIdentity = new UserIdentity(999L);

    // ========== Entity↔Domain 변환 테스트 ==========

    @Test
    void save_withValidDomain_returnsConvertedDomain() {
        // given
        User userToSave = sampleUser;

        // when
        User saved = userRepository.save(userToSave);

        // then
        assertThat(saved).isNotNull();
        assertThat(saved.getUserId()).isNotNull();
        assertThat(saved.getGoogleId()).isEqualTo(userToSave.getGoogleId());
        assertThat(saved.getEmail()).isEqualTo(userToSave.getEmail());
        assertThat(saved.getNickname()).isEqualTo(userToSave.getNickname());
        assertThat(saved.getUserRole()).isEqualTo(userToSave.getUserRole());
        assertThat(saved.getInterestedCompanies()).containsExactlyElementsOf(userToSave.getInterestedCompanies());
        assertThat(saved.getInterestedLocations()).containsExactlyElementsOf(userToSave.getInterestedLocations());
    }

    @Test
    void save_withNullId_generatesIdAndReturns() {
        // given
        User userWithNullId = sampleUser;

        // when
        User saved = userRepository.save(userWithNullId);

        // then
        assertThat(saved.getUserId()).isNotNull();
        assertThat(saved.getGoogleId()).isEqualTo(userWithNullId.getGoogleId());
    }

    @Test
    void findById_existingId_returnsConvertedDomain() {
        // given
        User saved = userRepository.save(sampleUser);
        UserIdentity identity = new UserIdentity(saved.getUserId());

        // when
        Optional<User> found = userRepository.findById(identity);

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getUserId()).isEqualTo(saved.getUserId());
        assertThat(found.get().getGoogleId()).isEqualTo(saved.getGoogleId());
        assertThat(found.get().getEmail()).isEqualTo(saved.getEmail());
        assertThat(found.get().getNickname()).isEqualTo(saved.getNickname());
    }

    @Test
    void findById_nonExistingId_returnsEmpty() {
        // when
        Optional<User> found = userRepository.findById(nonExistingIdentity);

        // then
        assertThat(found).isEmpty();
    }

    @Test
    void findAll_withData_returnsConvertedList() {
        // given
        User saved1 = userRepository.save(sampleUser);
        User saved2 = userRepository.save(new User(
                null, "google456", "test2@example.com", "testNickname2",
                UserRole.USER, List.of(), List.of(), NotificationSettings.defaultSettings(),
                0L, 0L, 0L, Instant.now(), true, false, null, Instant.now(), Instant.now()
        ));

        // when
        List<User> all = userRepository.findAll();

        // then
        assertThat(all).hasSize(2);
        assertThat(all).extracting(User::getUserId)
                .containsExactlyInAnyOrder(saved1.getUserId(), saved2.getUserId());
    }

    @Test
    void findAll_emptyRepository_returnsEmptyList() {
        // when
        List<User> all = userRepository.findAll();

        // then
        assertThat(all).isEmpty();
    }

    // ========== 커스텀 쿼리 테스트 ==========

    @Test
    void findByGoogleId_existingGoogleId_returnsConvertedDomain() {
        // given
        User saved = userRepository.save(sampleUser);

        // when
        Optional<User> found = userRepository.findByGoogleId(saved.getGoogleId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getUserId()).isEqualTo(saved.getUserId());
        assertThat(found.get().getGoogleId()).isEqualTo(saved.getGoogleId());
    }

    @Test
    void findByGoogleId_nonExistingGoogleId_returnsEmpty() {
        // when
        Optional<User> found = userRepository.findByGoogleId("nonExisting");

        // then
        assertThat(found).isEmpty();
    }

    @Test
    void findByEmail_existingEmail_returnsConvertedDomain() {
        // given
        User saved = userRepository.save(sampleUser);

        // when
        Optional<User> found = userRepository.findByEmail(saved.getEmail());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getUserId()).isEqualTo(saved.getUserId());
        assertThat(found.get().getEmail()).isEqualTo(saved.getEmail());
    }

    @Test
    void findByEmail_nonExistingEmail_returnsEmpty() {
        // when
        Optional<User> found = userRepository.findByEmail("nonexisting@example.com");

        // then
        assertThat(found).isEmpty();
    }

    @Test
    void findByNickname_existingNickname_returnsConvertedDomain() {
        // given
        User saved = userRepository.save(sampleUser);

        // when
        Optional<User> found = userRepository.findByNickname(saved.getNickname());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getUserId()).isEqualTo(saved.getUserId());
        assertThat(found.get().getNickname()).isEqualTo(saved.getNickname());
    }

    @Test
    void findByNickname_nonExistingNickname_returnsEmpty() {
        // when
        Optional<User> found = userRepository.findByNickname("nonExistingNickname");

        // then
        assertThat(found).isEmpty();
    }
}
