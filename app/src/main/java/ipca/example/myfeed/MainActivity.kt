package ipca.example.myfeed


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    var articles = arrayListOf<Article>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listViewArticles = findViewById<ListView>(R.id.listviewArticles)
        val adapter = ArticlesAdapter()
        listViewArticles.adapter = adapter


        Backend.fetchArticles {
            articles = it as ArrayList<Article>
            adapter.notifyDataSetChanged()
        }

        /*
             var foo : (List<Article>)->Unit = {
                 articles = it as ArrayList<Article>
                 adapter.notifyDataSetChanged()
             }
             Backend.fetchArticles (foo)
       */
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

           Backend.fetchImageToView(articles[position].urlToImage?:"", imageView)

            rootView.isClickable = true

            rootView.setOnClickListener {
                val intent = Intent(this@MainActivity, ArticleDetailActivity::class.java)
                intent.putExtra("article", articles[position].toJSON().toString())

                startActivity(intent)
            }
            return rootView
        }
    }
}