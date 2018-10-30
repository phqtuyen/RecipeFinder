package RecipeFinder;

import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FileUploadController {

    @Autowired
    public FileUploadController() {

    }

    @GetMapping("/")
    public  String indexPage(Model model) throws IOException {
        return "index";
    }

    @PostMapping("/uploadRecipe")
    public String uploadRecipe(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        return "redirect:/";
    }

    @PostMapping("/uploadIngredient")
    public String uploadIngredient(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        return "redirect:/";
    }


}
