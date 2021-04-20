package fontys.ict.kwetter.KwetterTweetService.repositories;

import fontys.ict.kwetter.KwetterTweetService.models.Mention;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MentionRepository extends JpaRepository<Mention,Long> {
    List<Mention> findByUsername(String username);

}
