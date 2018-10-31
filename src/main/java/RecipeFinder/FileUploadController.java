package RecipeFinder;

import java.io.IOException;

import RecipeFinder.DataModel.Ingredient;
import RecipeFinder.DataModel.IngredientData;
import RecipeFinder.DataModel.Recipe;
import RecipeFinder.DataModel.RecipeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import java.util.Map;

@Controller
public class FileUploadController {
    boolean uploadedRecipe;
    boolean uploadedIngredient;
    RecipeData recipeData;
    IngredientData ingredientData;
    RecipeData cookableRecipe;
    String recipeFileName;
    String ingredientFileName;

    @Autowired
    public FileUploadController() {
        this.init();
    }

    public void init() {
        this.recipeData = new RecipeData();
        this.ingredientData = new IngredientData();
        this.cookableRecipe = new RecipeData();
        this.uploadedIngredient = false;
        this.uploadedRecipe = false;
        this.recipeFileName = "";
        this.ingredientFileName = "";
    }

    @GetMapping("/refresh")
    public String refreshData() {
        this.init();
        return "redirect:/";
    }

    @GetMapping("/")
    public  String indexPage(Model model) throws IOException {
        Map<String, Object> attr = model.asMap();
        if (!attr.containsKey("recipes"))
            model.addAttribute("recipes", this.cookableRecipe);
        if (!attr.containsKey("message"))
            model.addAttribute("message", "");
        if (!attr.containsKey("errorMessage"))
            model.addAttribute("errorMessage", "");
        if (!attr.containsKey("recipeFileName"))
            model.addAttribute("recipeFileName", this.recipeFileName);
        if (!attr.containsKey("ingredientFileName"))
            model.addAttribute("ingredientFileName", this.ingredientFileName);
        return "index";
    }

    @GetMapping("/getRecipe")
    public String getRecipe(RedirectAttributes redirectAttributes) {
        if (this.uploadedRecipe == false) {
            redirectAttributes.addFlashAttribute("errorMessage", "You must provided some recipes.");
            return "redirect:/";
        }

        if (this.uploadedIngredient == false) {
            redirectAttributes.addFlashAttribute("errorMessage", "You must provided some ingredients.");
            return "redirect:/";
        }

        if (this.recipeData.size() > 0 && this.ingredientData.size() > 0) {
            this.cookableRecipe = this.recipeData.getCookableRecipes(this.ingredientData);
            this.cookableRecipe.sortByDate();
            if (this.cookableRecipe.size() > 0)
                redirectAttributes.addFlashAttribute("recipe", this.cookableRecipe);
            else
                redirectAttributes.addFlashAttribute("message", "Order Takeout");
        } else {
            redirectAttributes.addFlashAttribute("message", "Order Takeout");
        }

        return "redirect:/";
    }

    @PostMapping("/uploadRecipe")
    public String uploadRecipe(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            byte[] jsonData = file.getBytes();
            ObjectMapper objectMapper = new ObjectMapper();
            System.out.println("pass");
            this.recipeData = objectMapper.readValue(jsonData, RecipeData.class);
            this.uploadedRecipe = true;
            this.recipeFileName = file.getOriginalFilename();
            redirectAttributes.addFlashAttribute("errorMessage", "Upload recipes succeed.");
            redirectAttributes.addFlashAttribute("recipeFileName", this.recipeFileName);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Upload recipes fail.");
            System.out.println(e);
        }
        return "redirect:/";
    }

    @PostMapping("/uploadIngredient")
    public String uploadIngredient(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        CsvMapper mapper = new CsvMapper();
        try {
            MappingIterator<Ingredient> iterator = mapper.readerWithTypedSchemaFor(Ingredient.class).readValues(file.getInputStream());
            System.out.println("pass");
            int count = 0;
            while (iterator.hasNext()) {
                Ingredient temp = iterator.next();
                System.out.println(temp);
                this.ingredientData.add(temp);
                System.out.println("pass " + Integer.toString(count++));
            }
            System.out.println("pass 10 " + Integer.toString(this.ingredientData.size()));
            this.ingredientData = this.ingredientData.filterOutdatedItems();
            this.ingredientData.sortByDate();
            this.uploadedIngredient = true;
            this.ingredientFileName = file.getOriginalFilename();
            redirectAttributes.addFlashAttribute("errorMessage", "Upload ingredients succeed.");
            redirectAttributes.addFlashAttribute("ingredientFileName", this.ingredientFileName);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Upload ingredients fail.");
            System.out.println(e);
        }

        return "redirect:/";
    }


}
