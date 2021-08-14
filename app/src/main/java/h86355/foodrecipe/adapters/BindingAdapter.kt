package h86355.foodrecipe.adapters


import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.progressindicator.LinearProgressIndicator
import h86355.foodrecipe.R
import h86355.foodrecipe.utils.Constants.Companion.BASE_IMAGE_URL
import org.jsoup.Jsoup

object BindingAdapter {

    @BindingAdapter("loadRecipeImage")
    @JvmStatic
    fun loadRecipeImage(view: ShapeableImageView, url: String?) {
        url?.let {
            view.load(url) {
                crossfade(350)
                error(R.drawable.placeholder)
            }
        }
    }

    @BindingAdapter("loadIngredientImage")
    @JvmStatic
    fun loadIngredientImage(view: ShapeableImageView, image: String?) {
        image?.let {
            val ingredientImgUrl = BASE_IMAGE_URL + image
            view.load(ingredientImgUrl) {
                crossfade(350)
                error(R.drawable.placeholder)
            }
        }
    }

    @BindingAdapter("parseHtmlToString")
    @JvmStatic
    fun parseHtml(view: TextView, html: String?) {
        html?.let {
            val htmlStr = Jsoup.parse(html).text()
            view.text = htmlStr
        }
    }

    @BindingAdapter("cookingTimeFormat")
    @JvmStatic
    fun cookingTimeFormat(view: TextView, readyInMinutes: Int?) {
        readyInMinutes?.let {
            when {
                it < 60 -> view.text = "${it}m"
                it >= 60 -> {
                    val hour = it / 60
                    val minute = it % 60
                    view.text = "${hour}h ${minute}m"
                }
            }
        }
    }

    @BindingAdapter("shimmerEffectVisibilily")
    @JvmStatic
    fun shimmerEffectVisibility(view: ShimmerFrameLayout, visibiliy: Boolean) {
        if (visibiliy) {
            view.startShimmer()
        } else {
            view.stopShimmer()
        }
    }

    @BindingAdapter("recyclerViewVisibility")
    @JvmStatic
    fun recyclerViewVisibility(view: RecyclerView, visibiliy: Boolean) {
        if (visibiliy) {
            view.visibility = View.GONE
        } else {
            view.visibility = View.VISIBLE
        }
    }

    @BindingAdapter("nutritionVisibility")
    @JvmStatic
    fun nutritionVisibility(view: TextView, visibility: Boolean) {
        if (visibility) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }


    @BindingAdapter("searchLoadingVisibility")
    @JvmStatic
    fun searchLoadingVisibility(view: LinearProgressIndicator, visibiliy: Boolean) {
        if (visibiliy) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.INVISIBLE
        }
    }

}