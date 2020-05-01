package modtweaker2.utils;

import static java.util.stream.Collectors.toList;
import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import minetweaker.api.item.IIngredient;
import modtweaker2.helpers.LogHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class BaseCraftingRemoval extends BaseListRemoval<IRecipe> {
	public BaseCraftingRemoval(String name, List<IRecipe> list, List<IRecipe> recipes) {
		super(name, list, recipes);
	}
	
    @Override
    public String getRecipeInfo(IRecipe recipe) {
        return LogHelper.getStackDescription(recipe.getRecipeOutput());
    }
    
    public static List<IRecipe> getRecipes(List<IRecipe> list, IIngredient output) {
        return list.parallelStream()
                .filter(recipe -> areOutputsMatch(recipe, output))
                .collect(toList());
    }

    public static boolean hasRecipe(List<IRecipe> list, IIngredient output) {
	    return list.parallelStream()
                .anyMatch(recipe -> areOutputsMatch(recipe, output));
    }

    private static boolean areOutputsMatch(IRecipe recipe, IIngredient output) {
	    ItemStack recipeOutput = recipe.getRecipeOutput();
	    return recipeOutput != null && matches(output, toIItemStack(recipeOutput));
    }
}