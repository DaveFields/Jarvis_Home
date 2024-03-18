package com.example.jarvishome.ui.view.InfoProduct

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.jarvishome.R
import com.example.jarvishome.core.common.extensions.observe
import com.example.jarvishome.databinding.FragmentInfoProductBinding
import com.example.jarvishome.domain.model.Product
import com.example.jarvishome.domain.model.SelectedImages
import com.example.jarvishome.ui.viewmodel.InfoProductViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.jarvishome.ui.viewmodel.InfoProductViewModel.Event.SetupView
import com.example.jarvishome.ui.viewmodel.InfoProductViewModel.Event.SetupBars
import com.example.jarvishome.ui.viewmodel.InfoProductViewModel.Event.SetupScore
import com.example.jarvishome.ui.viewmodel.InfoProductViewModel.Event.ShowImage
import com.example.jarvishome.ui.viewmodel.InfoProductViewModel.Event.InitActions
import com.squareup.moshi.Moshi

class FragmentInfoProduct: BottomSheetDialogFragment() {
    companion object {
        const val TAG = "ModalBottomSheet"
        private const val ARG_PRODUCT = "product"
    }
    private lateinit var binding: FragmentInfoProductBinding
    private val viewModelProduct: InfoProductViewModel by viewModels()

    private lateinit var carboComponent: InfoBarComponent
    private lateinit var fatsComponent: InfoBarComponent
    private lateinit var proteinsComponent: InfoBarComponent

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoProductBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelProduct.initFlow()
        viewModelProduct.eventsFlow.observe(viewLifecycleOwner,::updateUi)

        // Inicializa InfoBarComponent para cada componente
        carboComponent = InfoBarComponent(binding.barCarbo)
        fatsComponent = InfoBarComponent(binding.barFats)
        proteinsComponent = InfoBarComponent(binding.barProteins)

        viewModelProduct.productCountLiveData.observe(viewLifecycleOwner) { count ->
            binding.tvTotalproduct.text = count.toString()
            binding.btnRemoveProduct.visibility = if (count > 0) View.VISIBLE else View.GONE
            binding.btAdd.visibility = if (count > 0) View.VISIBLE else View.GONE
            binding.tvTotalproduct.visibility = if (count > 0) View.VISIBLE else View.GONE
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
    }
    fun updateUi(event: InfoProductViewModel.Event) {
        Log.d(TAG+"Product", "updateUi: $event")
        with(binding) {
            when (event) {
                is InitActions -> {
                    binding.btnPlusProduct.setOnClickListener {
                        viewModelProduct.clickPlusProduct()
                    }
                    binding.btnRemoveProduct.setOnClickListener{
                        viewModelProduct.clickLessProduct()
                    }
                    binding.btAdd
                }
                is SetupView -> {
                    val productJson = arguments?.getString(ARG_PRODUCT)
                    val moshi = Moshi.Builder().build()
                    val jsonAdapter = moshi.adapter(Product::class.java)
                    viewModelProduct.product = jsonAdapter.fromJson(productJson ?: "")!!
                    viewModelProduct.showProduct()
                }
                is SetupBars -> {

                    binding.tvTotalCalInfo.text = "${event.product.nutriments.energy_kcal} ${event.product.nutriments.energy_unit}"

                    carboComponent.updateData(
                        (event.product.nutriments.carbohydrates_100g / 100).toFloat(),
                        getString(R.string.carbo),
                        "${event.product.nutriments.carbohydrates} ${event.product.nutriments.carbohydrates_unit}",
                        R.color.carbohydrates,
                        R.color.carbohydrates_info
                    )

                    fatsComponent.updateData(
                        (event.product.nutriments.fat_100g / 100).toFloat(),
                        getString(R.string.fat),
                        "${event.product.nutriments.fat} ${event.product.nutriments.fat_unit}",
                        R.color.fat,
                        R.color.fat_info
                    )

                    proteinsComponent.updateData(
                        (event.product.nutriments.proteins_100g / 100).toFloat(),
                        getString(R.string.protein),
                        "${event.product.nutriments.proteins} ${event.product.nutriments.proteins_unit}",
                        R.color.protein,
                        R.color.protein_info
                    )
                }
                is SetupScore -> {
                    tvEcoscore.text= event.score.toString()
                }
                is ShowImage -> {
                    loadValidImage(event.url,ivProduct)
                }
                else -> {}
            }
        }
    }
    fun loadValidImage(selectedImages: SelectedImages, imageView: ImageView) {
        val urlsToCheck = mutableListOf<String>()
        urlsToCheck.addAll(listOf(selectedImages.front.display.es))
        urlsToCheck.addAll(listOf(selectedImages.front.small.es))
        urlsToCheck.addAll(listOf(selectedImages.front.thumb.es))
        urlsToCheck.addAll(listOf(selectedImages.nutrition.display.es))
        urlsToCheck.addAll(listOf(selectedImages.nutrition.small.es))
        urlsToCheck.addAll(listOf(selectedImages.nutrition.thumb.es))

        loadNextValidImage(urlsToCheck.iterator(), imageView)
    }

    fun loadNextValidImage(urlsIterator: Iterator<String>, imageView: ImageView) {
        if (urlsIterator.hasNext()) {
            val url = urlsIterator.next()
            Glide.with(imageView.context)
                .load(url)
                .listener(object : RequestListener<Drawable> {
                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                        TODO("Not yet implemented")
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        loadNextValidImage(urlsIterator, imageView)
                        return true // Indica que el error ha sido manejado
                    }
                })
                .into(imageView)
        } else {
            // Si no hay más URLs para cargar, puedes manejarlo aquí
        }
    }
}