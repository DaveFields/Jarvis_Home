package com.example.jarvishome.data.model.api

data class ResponseProductDTO(
    val nutriments : ResponseNutrimentsDTO,
    val nutriscore_data : ResponseNutriscoreDataDTO,
    val images : ResponseImagesDTO,
    val selected_images: ResponseSelectedImagesDTO,
    val ecoscore_score : ResponseEcoScoreDTO,
    val ingredients : ResponseIngredientsDTO,
    val ingredients_analysis_tags : ResponseIngredientsAnalysisTagsDTO,
    val ingredients_text: ResponseIngredientsTextDTO,
    val nutrient_levels: ResponseNutrientsLevelsDTO,
    val traces_hierarchy: ResponseTracesHierarchyDTO
)
