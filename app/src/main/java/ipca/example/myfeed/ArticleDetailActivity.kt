package ipca.example.myfeed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.webkit.WebChromeClient
import android.webkit.WebView
import org.json.JSONObject

class ArticleDetailActivity : AppCompatActivity() {

    private lateinit var webView : WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)

        val jsonString =  intent.extras?.getString("article")
        val article = Article.fromJson(JSONObject(jsonString))

        title = article.title

        webView = findViewById(R.id.webView)
        article.url?.let {
            webView.loadUrl(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.web_menu, menu)
        return true
    }
}