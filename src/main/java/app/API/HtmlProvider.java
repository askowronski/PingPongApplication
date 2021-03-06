package app.API;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HtmlProvider {

    @Value("pingpong-1.0-SNAPSHOT")
    String appName;

    @RequestMapping(value = { "/Home", "/Players", "/Games", "/LogoPage", "/", "/CreateGame","/Feedback",
            "/CreatePlayer", "/TotalStats", "/PlayerProfile" })
    public String index() {
        return "index";
    }

}
