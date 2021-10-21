package ipca.example.myfeed

import org.json.JSONObject
import java.util.Date

class Article {

    var title       : String? = null
    var description : String? = null
    var url         : String? = null
    var urlToImage  : String? = null
    var publishedAt : Date?   = null

    companion object{
        fun fromJson(jsonObject: JSONObject) : Article {
            val article = Article()
            article.title       = jsonObject.getString("title")
            article.description = jsonObject.getString("description")
            article.url         = jsonObject.getString("url")
            article.urlToImage  = jsonObject.getString("urlToImage")
            //article.publishedAt = jsonObject.getString("publishedAt")
            return article
        }
    }
}