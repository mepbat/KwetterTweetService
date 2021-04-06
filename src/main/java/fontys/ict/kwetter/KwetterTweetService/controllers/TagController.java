package fontys.ict.kwetter.KwetterTweetService.controllers;

import fontys.ict.kwetter.KwetterTweetService.models.Tag;
import fontys.ict.kwetter.KwetterTweetService.repositories.TagRepository;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/tag")
public class TagController {
    private final TagRepository tagRepository;
    private final AmqpTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;
    @Value("${rabbitmq.routingKey}")
    private String routingkey;

    public TagController(TagRepository tagRepository, AmqpTemplate rabbitTemplate) {
        this.tagRepository = tagRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RequestMapping(value = "/{tagId}", method = RequestMethod.GET)
    public @ResponseBody
    Optional<Tag> getTag(@PathVariable("tagId") Long tagId) {
        return tagRepository.findById(tagId);
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
}
