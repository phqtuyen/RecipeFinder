package RecipeFinder;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class ControllerUnitTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldReturnIndexPage() throws Exception {
        this.mvc.perform(get("/")).andExpect(status().isOk());
    }

    @Test
    public void shouldSaveUploadedRecipeFile() throws Exception {
        String recipe = "[\n" +
                "\t{\n" +
                "\t\t\"name\": \"hasdhasdahsd\",\n" +
                "\t\t\"ingredients\": [\n" +
                "\t\t\t{\"item\":\"bread\", \"amount\":\"2\", \"unit\":\"slices\"},\n" +
                "\t\t\t{\"item\":\"cheese\", \"amount\":\"2\", \"unit\":\"slices\"}\n" +
                "\t\t]\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"name\": \"salad sandwich\",\n" +
                "\t\t\"ingredients\": [\n" +
                "\t\t\t{ \"item\":\"bread\", \"amount\":\"2\", \"unit\":\"slices\"},\n" +
                "\t\t\t{ \"item\":\"mixed salad\", \"amount\":\"100\", \"unit\":\"grams\"}\n" +
                "\t\t]\n" +
                "\t}\n" +
                "\n" +
                "]";
        MockMultipartFile multipartFile = new MockMultipartFile("file", "recipe.json",
                "json",
                recipe.getBytes());
        this.mvc.perform(fileUpload("/uploadRecipe").file(multipartFile))
                .andExpect(status().isFound());
    }

    @Test
    public void shouldSaveUploadedIngredientFile() throws Exception {
        String ingredient = "bread,10,slices,25/12/2018\n" +
                "cheese,10,slices,25/12/2018\n" +
                "butter,250,grams,25/12/2018\n" +
                "peanut butter,250,grams,02/12/2018\n" +
                "mixed salad,150,grams,20/12/2018\n" +
                "rice,30000,grams,31/10/2018\n" +
                "sugar,300000,grams,20/11/2018";
        MockMultipartFile multipartFile = new MockMultipartFile("file", "ingredient.csv",
                "text",
                ingredient.getBytes());
        this.mvc.perform(fileUpload("/uploadIngredient").file(multipartFile))
                .andExpect(status().isFound());
    }

    @Test
    public void shouldGetRecipe() throws Exception {
        this.shouldSaveUploadedIngredientFile();
        this.shouldSaveUploadedRecipeFile();
        this.mvc.perform(get("/getRecipe")).andExpect(status().isFound());
    }

    @Test
    public void shouldBeAbleToRefresh() throws Exception {
        this.mvc.perform(get("/getRecipe")).andExpect(status().isFound());
    }
}
