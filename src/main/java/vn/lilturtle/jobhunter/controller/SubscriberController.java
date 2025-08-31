package vn.lilturtle.jobhunter.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.lilturtle.jobhunter.domain.Subscriber;
import vn.lilturtle.jobhunter.service.SubscriberService;
import vn.lilturtle.jobhunter.util.SecurityUtil;
import vn.lilturtle.jobhunter.util.annotation.ApiMessage;
import vn.lilturtle.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class SubscriberController {

    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping("/subscribers")
    @ApiMessage("Create a subscriber")
    public ResponseEntity<Subscriber> createSubscriber(@Valid @RequestBody Subscriber sub) throws IdInvalidException {
        //check email
        boolean isExist = this.subscriberService.isEmailExist(sub.getEmail());
        if (isExist) {
            throw new IdInvalidException(
                    "Email " + sub.getEmail() + " already exist"
            );
        }
        Subscriber currentSub = this.subscriberService.handleSaveSubscriber(sub);

        return ResponseEntity.status(HttpStatus.CREATED).body(currentSub);
    }

    @PutMapping("/subscribers")
    @ApiMessage("Update a subscriber")
    public ResponseEntity<Subscriber> updateSubscriber(@RequestBody Subscriber subsRequest) throws IdInvalidException {
        //check id
        Subscriber subsDB = this.subscriberService.findById(subsRequest.getId());
        if (subsDB == null) {
            throw new IdInvalidException(
                    "Id " + subsRequest.getId() + " does not exist"
            );
        }
        Subscriber currentSub = this.subscriberService.handleUpdateSubscriber(subsDB, subsRequest);
        return ResponseEntity.ok(currentSub);
    }

    @PostMapping("/subscribers/skills")
    @ApiMessage("Get subscriber's skill")
    public ResponseEntity<Subscriber> getSubscribersSkill() throws IdInvalidException {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        return ResponseEntity.ok(this.subscriberService.findByEmail(email));
    }

}
