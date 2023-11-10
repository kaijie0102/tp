package essenmakanan.parser;

import essenmakanan.command.AddIngredientCommand;
import essenmakanan.command.AddRecipeCommand;
import essenmakanan.command.Command;
import essenmakanan.command.DeleteIngredientCommand;
import essenmakanan.command.DeleteRecipeCommand;
import essenmakanan.command.DuplicateRecipeCommand;
import essenmakanan.command.EditIngredientCommand;
import essenmakanan.command.EditRecipeCommand;
import essenmakanan.command.ExitCommand;
import essenmakanan.command.FilterRecipesCommand;
import essenmakanan.command.HelpCommand;
import essenmakanan.command.PlanRecipesCommand;
import essenmakanan.command.StartRecipeCommand;
import essenmakanan.command.ViewIngredientsCommand;
import essenmakanan.command.ViewRecipesCommand;
import essenmakanan.command.ViewSpecificIngredientCommand;
import essenmakanan.command.ViewSpecificRecipeCommand;
import essenmakanan.exception.EssenCommandException;
import essenmakanan.exception.EssenFormatException;
import essenmakanan.exception.EssenOutOfRangeException;
import essenmakanan.ingredient.IngredientList;
import essenmakanan.recipe.RecipeList;

public class Parser {
    public Command parseCommand(String input, RecipeList recipes, IngredientList ingredients)
            throws EssenCommandException, EssenFormatException, EssenOutOfRangeException {
        Command command;

        String[] parsedInput = input.split(" ", 2);
        String commandType = parsedInput[0];
        String inputDetail = parsedInput.length == 1 ? "" : parsedInput[1].trim();

        switch (commandType) {
        case "start":
            command = new StartRecipeCommand(inputDetail, recipes, ingredients);
            break;
        case "add":
            if (inputDetail.startsWith("r/")) {
                // check that all fields needed for recipe is present
                if (!(inputDetail.contains("r/") && inputDetail.contains("s/") && inputDetail.contains("i/"))) {
                    System.out.println("Recipe have to include title, steps and ingredients!");
                    throw new EssenFormatException();
                }
                command = new AddRecipeCommand(inputDetail, recipes);
            } else if (inputDetail.startsWith("i/")) {
                command = new AddIngredientCommand(inputDetail, ingredients);
            } else {
                throw new EssenFormatException();
            }
            break;
        case "delete":
            if (inputDetail.startsWith("r/")) {
                command = new DeleteRecipeCommand(recipes, inputDetail);
            } else if (inputDetail.startsWith("i/")) {
                command = new DeleteIngredientCommand(ingredients, inputDetail);
            } else {
                throw new EssenFormatException();
            }
            break;
        case "view":
            if (inputDetail.equals("r")) {
                command = new ViewRecipesCommand(recipes);
            } else if (inputDetail.equals("i")) {
                command = new ViewIngredientsCommand(ingredients);
            } else if (inputDetail.startsWith("r/")) {
                assert (!inputDetail.equals("")) : "To view a recipe, make sure title is not empty";
                command = new ViewSpecificRecipeCommand(recipes, inputDetail);
            } else if (inputDetail.startsWith("i/")) {
                command = new ViewSpecificIngredientCommand(ingredients, inputDetail);
            } else {
                throw new EssenFormatException();
            }
            break;
        case "filter":
            if (inputDetail.startsWith("recipe")) {
                inputDetail = RecipeParser.parseFilterRecipeInput(inputDetail);
                command = new FilterRecipesCommand(inputDetail, recipes);
            } else {
                throw new EssenFormatException();
            }
            break;
        case "edit":
            if (inputDetail.startsWith("i/")) {
                command = new EditIngredientCommand(inputDetail, ingredients);
            } else if (inputDetail.startsWith("r/")) {
                command = new EditRecipeCommand(inputDetail, recipes);
            } else {
                throw new EssenFormatException();
            }
            break;
        case "duplicate":
            command = new DuplicateRecipeCommand(recipes, inputDetail);
            break;
        case "plan":
            command = new PlanRecipesCommand(ingredients, recipes, inputDetail);
            break;
        case "help":
            command = new HelpCommand();
            break;
        case "exit":
            command = new ExitCommand();
            break;
        default:
            throw new EssenCommandException();
        }

        return command;
    }
}
