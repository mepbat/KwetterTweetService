package fontys.ict.kwetter.KwetterTweetService;

import com.google.gson.Gson;
import fontys.ict.kwetter.KwetterTweetService.controllers.TweetController;
import fontys.ict.kwetter.KwetterTweetService.models.Tweet;
import fontys.ict.kwetter.KwetterTweetService.models.dto.TweetDto;
import fontys.ict.kwetter.KwetterTweetService.repositories.HeartRepository;
import fontys.ict.kwetter.KwetterTweetService.repositories.MentionRepository;
import fontys.ict.kwetter.KwetterTweetService.repositories.TagRepository;
import fontys.ict.kwetter.KwetterTweetService.repositories.TweetRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = KwetterTweetServiceApplication.class)
@WebMvcTest(TweetController.class)
public class TweetIntegrationTests {


    @Autowired
    private MockMvc mvc;

    @MockBean
    private TweetRepository tweetRepository;
    @MockBean
    private TagRepository tagRepository;
    @MockBean
    private MentionRepository mentionRepository;
    @MockBean
    private HeartRepository heartRepository;
    @MockBean
    private AmqpTemplate rabbitTemplate;

    private final Gson gson = new Gson();

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void contextLoads() {
        assertThat(tweetRepository).isNotNull();
        assertThat(tagRepository).isNotNull();
        assertThat(mentionRepository).isNotNull();
        assertThat(heartRepository).isNotNull();
    }

    @Test
    public void createTweetAPI() throws Exception {
        TweetDto tweetDto = new TweetDto(0L,"test",null,"test");
        Tweet tweet = new Tweet(0L,"text", new Date(), 0L, "test", new HashSet<>(), new HashSet<>());

        given(tweetRepository.save(any())).willReturn(tweet);
        mvc.perform(MockMvcRequestBuilders
                .post("/tweet")
                .content(gson.toJson(tweetDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(gson.toJson(tweet)));
    }

    @Test
    public void createEmptyTweetAPI() throws Exception {
        TweetDto tweetDto = new TweetDto(0L,"",null, "test");
        mvc.perform(MockMvcRequestBuilders
                .post("/tweet")
                .content(gson.toJson(tweetDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Cannot post an empty tweet."));
    }

    @Test
    public void createNullTweetAPI() throws Exception {
        TweetDto tweetDto = new TweetDto(0L,null,null, "test");
        mvc.perform(MockMvcRequestBuilders
                .post("/tweet")
                .content(gson.toJson(tweetDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Cannot post an empty tweet."));
    }

    @Test
    public void createTweetCharacterLimitExceededAPI() throws Exception {
        TweetDto tweetDto = new TweetDto(0L,"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum",null, "test");
        mvc.perform(MockMvcRequestBuilders
                .post("/tweet")
                .content(gson.toJson(tweetDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Character limit of tweet Exceeded."));
    }

    @Test
    public void createTweetWithTagsAPI() throws Exception {
        TweetDto tweetDto = new TweetDto(0L,"#tag1 #tag2 #tag3",null,"test");
        Tweet tweet = new Tweet(0L,"#tag1 #tag2 #tag3", new Date(), 0L, "test", new HashSet<>(), new HashSet<>());

        given(tweetRepository.save(any())).willReturn(tweet);
        mvc.perform(MockMvcRequestBuilders
                .post("/tweet")
                .content(gson.toJson(tweetDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tags.length()").value("3"));
    }

    @Test
    public void createTweetWithMentionsAPI() throws Exception {
        TweetDto tweetDto = new TweetDto(0L,"@mention1 @mention2 @mention3",null,"test");
        Tweet tweet = new Tweet(0L,"@mention1 @mention2 @mention3", new Date(), 0L, "test", new HashSet<>(), new HashSet<>());

        given(tweetRepository.save(any())).willReturn(tweet);
        mvc.perform(MockMvcRequestBuilders
                .post("/tweet")
                .content(gson.toJson(tweetDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mentions.length()").value("3"));
    }

    @Test
    public void createTweetWithTagAndMentionAPI() throws Exception {
        TweetDto tweetDto = new TweetDto(0L,"#tag @mention",null,"test");
        Tweet tweet = new Tweet(0L,"#tag @mention", new Date(), 0L, "test", new HashSet<>(), new HashSet<>());

        given(tweetRepository.save(any())).willReturn(tweet);
        mvc.perform(MockMvcRequestBuilders
                .post("/tweet")
                .content(gson.toJson(tweetDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tags.length()").value("1"))
                .andExpect(jsonPath("$.mentions.length()").value("1"));
    }

}
