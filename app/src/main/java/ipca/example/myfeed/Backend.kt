package ipca.example.myfeed

import android.graphics.BitmapFactory
import android.widget.ImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.lang.Exception
import java.net.URL

object Backend {

    val BASE_API = "https://newsapi.org/v2/"

    fun fetchArticles( callback : (List<Article>)->Unit ) {
        val endpointArticles = "top-headlines?country=pt&apiKey=1765f87e4ebc40229e80fd0f75b6416c"

        GlobalScope.launch (Dispatchers.IO){
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(BASE_API + endpointArticles)
                .build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful){
                    // apresentar mensagem de erro
                }else{
                    val result = response.body!!.string()
                    //Log.d("myfeed", result)
                    val jsonResult = JSONObject(result)
                    if (jsonResult.get("status")=="ok"){
                        val articlesJSONArray = jsonResult.getJSONArray("articles")
                        val articles = arrayListOf<Article>()
                        for ( index in 0 until articlesJSONArray.length()){
                            val articleJSON = articlesJSONArray.get(index) as JSONObject
                            val article = Article.fromJson(articleJSON)
                            articles.add(article)
                        }
                        GlobalScope.launch (Dispatchers.Main){
                            callback.invoke(articles)
                        }
                    }
                }
            }
        }
    }


    fun fetchImageToView(urlToImage : String, imageView: ImageView){
        GlobalScope.launch (Dispatchers.IO){
            try {
                val inputStream = URL(urlToImage).openStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)
                GlobalScope.launch (Dispatchers.Main){
                    imageView.setImageBitmap(bitmap)
                }
            }catch (e: Exception){
                GlobalScope.launch (Dispatchers.Main){
                    imageView.setImageResource(R.drawable.ic_baseline_photo_size_select_actual_24)
                }
            }
        }
    }

}