package club.touchandgo.tag

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.github.kittinunf.fuel.*
import com.github.kittinunf.result.*
import kotlinx.android.synthetic.main.activity_test.*
import org.json.JSONObject

class test : AppCompatActivity() {

    val username = "user1"
    var postPlayer = "http://207.246.122.125:8080/postPlayer"
    var putPlayer = "http://10.200.1.146:8080/putPlayer/$username"
    var getPlayers = "http://207.246.122.125:8080/getPlayers"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
    }

    override fun onResume(){
        super.onResume()

        val json = JSONObject()
        json.put("username", username)
        json.put("lat", "120")
        json.put("long", "74")
        json.put("tag", "false")

        val request = putPlayer.httpPut().body(json.toString())
        request.httpHeaders["Content-Type"] = "application/json"
        request.responseString{ _, _, result ->
            when (result) {
                is Result.Failure -> {
                    val ex = result.error.exception
                    Log.e("Error", ex.toString())
                }
                is Result.Success -> {

                }
            }
        }

//        val json = JSONObject()
//        json.put("username", "user1")
//        json.put("lat", "112")
//        json.put("long", "74")
//        json.put("tag", "false")
//
//        val request = postPlayer.httpPost().body(json.toString())
//        request.httpHeaders["Content-Type"] = "application/json"
//        request.responseString{ _, _, result ->
//            when (result) {
//                is Result.Failure -> {
//                    val ex = result.error.exception
//                    Log.e("Error", ex.toString())
//                }
//                is Result.Success -> {
//
//                }
//            }
//        }

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
