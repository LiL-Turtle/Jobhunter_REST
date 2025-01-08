package vn.lilturtle.jobhunter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.lilturtle.jobhunter.service.EmailService;
import vn.lilturtle.jobhunter.service.SubscriberService;
import vn.lilturtle.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class EmailController {

    private final EmailService emailService;
    private final SubscriberService subscriberService;

    public EmailController(EmailService emailService, SubscriberService subscriberService) {
        this.emailService = emailService;
        this.subscriberService = subscriberService;
    }

    @GetMapping("/email")
    @ApiMessage("Send simple email")
    public String sendSimpleEmail() {
//        this.emailService.sendSimleEmail();
//        this.emailService.sendEmailSync("baothien210704@gmail.com", "test send email", "<h1> <b> hello </b> </h1>",
//                false, true);
//        this.emailService.sendEmailFromTemplateSync("baothien210704@gmail.com", "test send email", "job");
        this.subscriberService.sendSubscribersEmailJobs();
        return "ok";


    }
}
