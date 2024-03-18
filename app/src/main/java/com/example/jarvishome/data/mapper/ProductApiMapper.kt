package com.example.jarvishome.data.mapper

import android.util.Log
import com.example.jarvishome.data.base.Mapper
import com.example.jarvishome.data.model.NutrimentsReponseApi
import com.example.jarvishome.data.model.SearchProductResponseApi
import com.example.jarvishome.data.model.api.ImageSizeDTO
import com.example.jarvishome.data.model.api.ImageTypeDTO
import com.example.jarvishome.data.model.api.ImagesResponseApi
import com.example.jarvishome.domain.model.ImageSize
import com.example.jarvishome.domain.model.ImageType
import com.example.jarvishome.domain.model.Nutriments
import com.example.jarvishome.domain.model.Product
import com.example.jarvishome.domain.model.SelectedImages

class ProductApiMapper : Mapper<SearchProductResponseApi, Product> {
    override fun map(model: SearchProductResponseApi): Product {
        return Product(
            code = model.code,
            ecoscore_score = model.product.ecoscore_score ?:-1,
            nutriments = mapNutriments(model.product.nutriments),
            images = mapImages(model.product.selected_images)
        )
    }
    fun mapNutriments(model: NutrimentsReponseApi): Nutriments {
        return Nutriments(
            carbohydrates = model.carbohydrates,
            carbohydrates_unit = model.carbohydrates_unit,
            carbohydrates_100g = model.carbohydrates_100g,
            energy = model.energy,
            energy_unit = model.energy_unit,
            energy_kcal = model.energy_kcal,
            fat = model.fat,
            fat_unit = model.fat_unit,
            fat_100g = model.fat_100g,
            proteins = model.proteins,
            proteins_unit = model.proteins_unit,
            proteins_100g = model.proteins_100g
        )
    }
    fun mapImages(model: ImagesResponseApi): SelectedImages {
        val front = convertImageTypeDTO(model.front)
        val nutrition = convertImageTypeDTO(model.nutrition)

        return SelectedImages(front, nutrition)
    }
    fun convertImageSizeDTO(imageSizeDTO: ImageSizeDTO): ImageSize {
        return ImageSize(imageSizeDTO.es)
    }

    fun convertImageTypeDTO(imageTypeDTO: ImageTypeDTO): ImageType {
        val display = convertImageSizeDTO(imageTypeDTO.display)
        val small = convertImageSizeDTO(imageTypeDTO.small)
        val thumb = convertImageSizeDTO(imageTypeDTO.thumb)

        return ImageType(display, small, thumb)
    }
}