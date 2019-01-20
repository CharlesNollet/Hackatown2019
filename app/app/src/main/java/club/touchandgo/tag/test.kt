package club.touchandgo.tag

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.github.kittinunf.fuel.*
import com.github.kittinunf.result.*
import kotlinx.android.synthetic.main.activity_test.*
import org.json.JSONObject

class test : AppCompatActivity() {

    var postPlayer = "http://207.246.122.125:8080/postPlayer"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
    }

    override fun onResume(){
        super.onResume()
        val json = JSONObject()
        json.put("username", "user1")
        json.put("lat", "112")
        json.put("long", "74")
        json.put("tag", "false")

        postPlayer.httpPost().body(json.toString()).responseString{ _, _, result ->
            when (result) {
                is Result.Failure -> {
                    val ex = result.error.exception
                    Log.e("Error", ex.toString())
                }
                is Result.Success -> {

                }
            }
        }

//        URL.httpGet().responseString { request, response, result ->
//            when (result) {
//                is Result.Failure -> {
//                    val ex = result.error.exception
//                }
//                is Result.Success -> {
//                    val data = result.get()
//                    getResult.text = data
//                }
//            }
//        }
    }
}
