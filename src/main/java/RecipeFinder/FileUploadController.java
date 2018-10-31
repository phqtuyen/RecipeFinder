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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @Autowired
    public FileUploadController() {
        this.recipeData = new RecipeData();
        this.ingredientData = new IngredientData();
        this.cookableRecipe = new RecipeData();
        this.uploadedIngredient = false;
        this.uploadedRecipe = false;
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
//            System.out.println("=============================");
//            for (Recipe recipe: this.cookableRecipe)
//                System.out.println(recipe);
//            System.out.println("=============================");
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
//            for (Recipe r: this.recipeData) {
//                System.out.println(r);
//                System.out.println("size " + Integer.toString(r.getIngredients().size()));
//                for (Ingredient i: r.getIngredients()) {
//                    System.out.println(i);
//                }
//                System.out.println("==============");
//            }
            this.uploadedRecipe = true;
            redirectAttributes.addFlashAttribute("errorMessage", "Upload recipes succeed.");
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
//            System.out.println("=============================");
//            for (Ingredient i : this.ingredientData) {
//                System.out.println(i);
//            }
//            System.out.println("=============================");
            this.ingredientData.sortByDate();
//            for (Ingredient i : this.ingredientData) {
//                System.out.println(i);
//            }
            this.uploadedIngredient = true;
            redirectAttributes.addFlashAttribute("errorMessage", "Upload ingredients succeed.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Upload ingredients fail.");
            System.out.println(e);
        }

        return "redirect:/";
    }


}
