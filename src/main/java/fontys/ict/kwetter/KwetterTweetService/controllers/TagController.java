package fontys.ict.kwetter.KwetterTweetService.controllers;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fontys.ict.kwetter.KwetterTweetService.models.HibernateProxyTypeAdapter;
import fontys.ict.kwetter.KwetterTweetService.models.Tag;
import fontys.ict.kwetter.KwetterTweetService.repositories.TagRepository;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/tag")
public class TagController {
    private final TagRepository tagRepository;
    private final AmqpTemplate rabbitTemplate;
    private final Gson gson;

    @Value("${rabbitmq.exchange}")
    private String exchange;
    @Value("${rabbitmq.routingKey}")
    private String routingkey;

    public TagController(TagRepository tagRepository, AmqpTemplate rabbitTemplate) {
        this.tagRepository = tagRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.gson = initiateGson();
    }

    @RequestMapping(value = "/trends", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getTrends(){
        List<String> trends = tagRepository.getTrends();
        List<Tag> tags = new ArrayList<>();
        for (String str:trends) {
            Tag tag = new Tag();
            tag.setTag(str);
            tags.add(tag);
        }
        return new ResponseEntity<>(gson.toJson(tags), HttpStatus.OK);
    }

    @RequestMapping(value = "/search/{tag}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> searchTags(@PathVariable String tag){
        return new ResponseEntity<>(gson.toJson(tagRepository.findAllByTagContaining(tag)), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    List<Tag> getAll() {
        return tagRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public @ResponseBody Tag createTag(@RequestBody Tag tag){
        return tagRepository.save(tag);
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public @ResponseBody Tag updateTag(@RequestBody Tag tag){
        return tagRepository.save(tag);
    }

    @RequestMapping(method = RequestMethod.DELETE, consumes = "application/json")
    public @ResponseBody void deleteTag(@RequestBody Tag tag){
        tagRepository.delete(tag);
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
