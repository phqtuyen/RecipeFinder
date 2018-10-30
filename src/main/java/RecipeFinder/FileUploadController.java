package RecipeFinder;

//import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.io.File;
import java.nio.file.Files;

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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.csv.CsvParser;

import org.apache.commons.io.FileUtils;
import java.util.*;
import java.io.*;

@Controller
public class FileUploadController {
    RecipeData recipeData;
    IngredientData ingredientData;

    @Autowired
    public FileUploadController() {
        this.recipeData = new RecipeData();
        this.ingredientData = new IngredientData();
    }

    @GetMapping("/")
    public  String indexPage(Model model) throws IOException {
        return "index";
    }

    @GetMapping("/getRecipe")
    public String getRecipe() {

        return "redirect:/";
    }

    @PostMapping("/uploadRecipe")
    public String uploadRecipe(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            byte[] jsonData = file.getBytes();
            ObjectMapper objectMapper = new ObjectMapper();
            System.out.println("pass");
            this.recipeData = objectMapper.readValue(jsonData, RecipeData.class);
            for (Recipe r: this.recipeData) {
                System.out.println(r);
                System.out.println("size " + Integer.toString(r.getIngredients().size()));
                for (Ingredient i: r.getIngredients()) {
                    System.out.println(i);
                }
                System.out.println("==============");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return "redirect:/";
    }

    @PostMapping("/uploadIngredient")
    public String uploadIngredient(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        CsvMapper mapper = new CsvMapper();
        try {
            MappingIterator<Ingredient> iterator = mapper.readerWithTypedSchemaFor(Ingredient.class).readValues(file.getInputStream());
            while (iterator.hasNext()) {
                Ingredient temp = iterator.next();
                System.out.println(temp);
                this.ingredientData.add(temp);
            }
            for (Ingredient i : ingredientData) {
                System.out.println(i);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return "redirect:/";
    }


}
