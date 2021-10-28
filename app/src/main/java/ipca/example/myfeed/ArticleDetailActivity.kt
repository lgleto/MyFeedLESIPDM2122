package ipca.example.myfeed

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Toast
import org.json.JSONObject

class ArticleDetailActivity : AppCompatActivity() {

    private lateinit var webView : WebView
    private lateinit var article : Article

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)

        val jsonString =  intent.extras?.getString("article")
        article = Article.fromJson(JSONObject(jsonString))

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return when (item.itemId){
            R.id.action_share -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT, article.title)
                intent.putExtra(Intent.EXTRA_TEXT, article.url)
                startActivity((Intent.createChooser(intent, "My Feed")))
                true
            }
            R.id.action_open_browser -> {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(article.url)
                startActivity(i)
                true
            }
            else ->  super.onOptionsItemSelected(item)
        }


    }
}