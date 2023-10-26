package essenmakanan.parser;

import essenmakanan.command.AddIngredientCommand;
import essenmakanan.command.AddRecipeCommand;
import essenmakanan.command.Command;
import essenmakanan.command.ExitCommand;
import essenmakanan.command.HelpCommand;
import essenmakanan.command.ViewIngredientsCommand;
import essenmakanan.command.ViewRecipesCommand;
import essenmakanan.exception.EssenMakananCommandException;
import essenmakanan.exception.EssenMakananFormatException;
import essenmakanan.ingredient.IngredientList;
import essenmakanan.recipe.RecipeList;

public class Parser {
    public boolean isIngredientCommand(String inputDetail) {
        return inputDetail.startsWith("r/");
    }

    public boolean isRecipeCommand(String inputDetail) {
        return inputDetail.startsWith("i/");
    }

    public Command parseCommand(String input, RecipeList recipes, IngredientList ingredients)
            throws EssenMakananCommandException, EssenMakananFormatException {
        Command command;

        String[] parsedInput = input.split(" ", 2);
        String commandType = parsedInput[0];
        String inputDetail = parsedInput.length == 1 ? "" : parsedInput[1].trim();

        switch (commandType) {
        case "add":
            if (isRecipeCommand(inputDetail)) {
                command = new AddRecipeCommand(inputDetail, recipes);
            } else if (isIngredientCommand(inputDetail)) {
                command = new AddIngredientCommand(inputDetail, ingredients);
            } else {
                throw new EssenMakananFormatException();
            }
            break;
        case "delete":
            if (isRecipeCommand(inputDetail)) {
                int recipeToDelete = RecipeParser.getRecipeId(inputDetail);
                command = new DeleteRecipeCommand(recipeToDelete);
            } else if (isIngredientCommand(inputDetail)) {
                int ingredientToDelete = IngredientParser.getIngredientId(inputDetail);
                command = new DeleteIngredientCommand(ingredientToDelete);
            } else {
                throw new EssenMakananFormatException();
            }
        case "view":
            if (inputDetail.equals("r")) {
                command = new ViewRecipesCommand(recipes);
            } else if (inputDetail.equals("i")) {
                command = new ViewIngredientsCommand(ingredients);
            } else {
                throw new EssenMakananFormatException();
            }
            break;
        case "help":
            command = new HelpCommand();
            break;
        case "exit":
            command = new ExitCommand();
            break;
        default:
            throw new EssenMakananCommandException();
        }

        return command;
    }
}
