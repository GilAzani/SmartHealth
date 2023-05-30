package superapp.logic.service;

import superapp.data.IngredientEntity;

public interface SpoonaculerService {

	public IngredientEntity getIngredientDataByName(String ingredientName);
	
	public IngredientEntity getIngredientDataByIdAndAmountInGrams(Integer id, int amountInGrams);

	IngredientEntity getIngredientDataByName(String ingredientName, int amountInGrams);
	
}
