package modtweaker2.mods.forestry.handlers;

import static modtweaker2.helpers.InputHelper.getFluid;
import static modtweaker2.helpers.InputHelper.toFluid;
import static modtweaker2.helpers.InputHelper.toILiquidStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.InputHelper.toStacks;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker2.helpers.LogHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import forestry.factory.gadgets.MachineSqueezer;
import forestry.factory.gadgets.MachineSqueezer.Recipe;
import forestry.factory.gadgets.MachineSqueezer.RecipeManager;

@ZenClass("mods.forestry.Squeezer")
public class Squeezer {
    
    public static final String name = "Forestry Squeezer";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void addRecipe(int timePerItem, IItemStack[] resources, ILiquidStack liquid, IItemStack remnants, int chance) {
		MineTweakerAPI.apply(new Add(new Recipe(timePerItem, toStacks(resources), toFluid(liquid), toStack(remnants), chance)));
		MachineSqueezer.RecipeManager.recipeFluids.add(getFluid(liquid));
		MachineSqueezer.RecipeManager.recipeInputs.addAll(Arrays.asList(toStacks(resources)));
	}

	private static class Add extends BaseListAddition<Recipe> {
		public Add(Recipe recipe) {
			super(Squeezer.name, MachineSqueezer.RecipeManager.recipes);
			recipes.add(recipe);
		}

		@Override
		public String getRecipeInfo(Recipe recipe) {
			return LogHelper.getStackDescription(recipe.liquid);
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void removeRecipe(IIngredient liquid) {
	    List<Recipe> recipes = new LinkedList<Recipe>();
	 
        for (Recipe r : RecipeManager.recipes) {
            if (r != null && r.liquid != null && matches(liquid, toILiquidStack(r.liquid))) {
                recipes.add(r);
            }
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", Squeezer.name, liquid.toString()));
        }
		    
	}

	private static class Remove extends BaseListRemoval<Recipe> {
		public Remove(List<Recipe> recipes) {
			super(Squeezer.name, MachineSqueezer.RecipeManager.recipes, recipes);

		}

		@Override
		public void apply() {

			super.apply();
		}
		
        @Override
        public String getRecipeInfo(Recipe recipe) {
            return LogHelper.getStackDescription(recipe.liquid);
        }
	}
}
