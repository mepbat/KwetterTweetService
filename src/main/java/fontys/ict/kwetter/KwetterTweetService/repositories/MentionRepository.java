package fontys.ict.kwetter.KwetterTweetService.repositories;

import fontys.ict.kwetter.KwetterTweetService.models.Mention;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MentionRepository extends JpaRepository<Mention,Long> {
    Optional<Mention> findByUsername(String username);

}
