package ipca.example.myfeed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    val articles = arrayListOf<Article>()
    var urlString = "https://newsapi.org/v2/top-headlines?country=pt&apiKey=1765f87e4ebc40229e80fd0f75b6416c"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listViewArticles = findViewById<ListView>(R.id.listviewArticles)
        val adapter = ArticlesAdapter()
        listViewArticles.adapter = adapter

        GlobalScope.launch (Dispatchers.IO){
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(urlString)
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
                        for ( index in 0 until articlesJSONArray.length()){
                            val articleJSON = articlesJSONArray.get(index) as JSONObject
                            val article = Article.fromJson(articleJSON)
                            articles.add(article)
                        }
                        GlobalScope.launch (Dispatchers.Main){
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }

    }

    inner class ArticlesAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return articles.size
        }

        override fun getItem(position: Int): Any {
            return articles[position]
        }

        override fun getItemId(position: Int): Long {
            return 0L
        }

        override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_article,viewGroup,false)
            val textViewTitle = rootView.findViewById<TextView>(R.id.textViewTitle)
            val textViewDescription = rootView.findViewById<TextView>(R.id.textViewDescription)
            val imageView = rootView.findViewById<ImageView>(R.id.imageView)
            textViewTitle.text = articles[position].title
            textViewDescription.text = articles[position].description
            return rootView
        }
    }
}