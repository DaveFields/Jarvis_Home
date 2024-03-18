package com.example.jarvishome.data.model

import com.example.jarvishome.data.model.api.ImagesResponseApi
import com.example.jarvishome.data.model.api.ResponseSelectedImagesDTO
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
@JsonClass(generateAdapter = true)
data class ProductResponseApi(
    @Json(name = "nutriments") val nutriments : NutrimentsReponseApi,
    //@Json(name = "nutriscore_data") val nutriscore_data : ResponseNutriscoreDataDTO,
    //@Json(name = "images") val images : ResponseImagesDTO,
    @Json(name = "selected_images") val selected_images: ImagesResponseApi,
    @Json(name = "ecoscore_score") val ecoscore_score : Int?,
    //@Json(name = "ingredients") val ingredients : ResponseIngredientsDTO,
    //@Json(name = "ingredients_analysis_tags") val ingredients_analysis_tags : ResponseIngredientsAnalysisTagsDTO,
    //@Json(name = "ingredients_text") val ingredients_text: ResponseIngredientsTextDTO,
    //@Json(name = "nutrient_levels") val nutrient_levels: ResponseNutrientsLevelsDTO,
    //@Json(name = "traces_hierarchy") val traces_hierarchy: ResponseTracesHierarchyDTO*/
)
