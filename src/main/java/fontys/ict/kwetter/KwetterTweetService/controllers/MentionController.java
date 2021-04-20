package fontys.ict.kwetter.KwetterTweetService.controllers;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fontys.ict.kwetter.KwetterTweetService.models.HibernateProxyTypeAdapter;
import fontys.ict.kwetter.KwetterTweetService.models.Mention;
import fontys.ict.kwetter.KwetterTweetService.models.Tweet;
import fontys.ict.kwetter.KwetterTweetService.repositories.MentionRepository;
import fontys.ict.kwetter.KwetterTweetService.repositories.TweetRepository;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/mention")
public class MentionController {
    private final MentionRepository mentionRepository;
    private final TweetRepository tweetRepository;
    private final AmqpTemplate rabbitTemplate;
    private final Gson gson;

    @Value("${rabbitmq.exchange}")
    private String exchange;
    @Value("${rabbitmq.routingKey}")
    private String routingkey;

    public MentionController(MentionRepository mentionRepository, TweetRepository tweetRepository, AmqpTemplate rabbitTemplate) {
        this.mentionRepository = mentionRepository;
        this.tweetRepository = tweetRepository;
        this.rabbitTemplate = rabbitTemplate;
        gson = initiateGson();
    }

    @RequestMapping(value = "/{mentionId}", method = RequestMethod.GET)
    public ResponseEntity<?> getMention(@PathVariable("mentionId") Long mentionId) {
        return new ResponseEntity<>(gson.toJson(mentionRepository.findById(mentionId)), HttpStatus.OK);
    }

    @RequestMapping(value = "/getMentions/{username}", method = RequestMethod.GET)
    public ResponseEntity<?> getMentions(@PathVariable("username") String username) {
        if(username == null || username.isEmpty()){
            return new ResponseEntity<>("Username is empty!", HttpStatus.BAD_REQUEST);
        }
        List<Mention> mentions = mentionRepository.findByUsername(username);
        if(mentions.isEmpty()){
            return new ResponseEntity<>("No mentions found!", HttpStatus.NOT_FOUND);
        }
        ArrayList<Long> tweetIds = new ArrayList<>();
        for (Mention mention: mentions) {
            tweetIds.add(mention.getTweet().getId());
        }
        return new ResponseEntity<>(gson.toJson(tweetRepository.findByIdInOrderByDateDesc(tweetIds)), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(gson.toJson(mentionRepository.findAll()),HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createMention(@RequestBody Mention mention){
        return new ResponseEntity<>(gson.toJson(mentionRepository.save(mention)),HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updateMention(@RequestBody Mention mention){
        return new ResponseEntity<>(gson.toJson(mentionRepository.save(mention)),HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, consumes = "application/json")
    public ResponseEntity<?> deleteMention(@RequestBody Mention mention){
        mentionRepository.delete(mention);
        return new ResponseEntity<>("Mention deleted", HttpStatus.OK);
    }

    private Gson initiateGson() {
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY)
                .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        boolean exclude = false;
                        try {
                            exclude = EXCLUDE.contains(f.getName());

                        } catch (Exception ignore) {
                        }
                        return exclude;
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                });
        return b.create();
    }

    private static final List<String> EXCLUDE = new ArrayList<>() {{
        add("mentions");
        add("tags");
    }};
}
