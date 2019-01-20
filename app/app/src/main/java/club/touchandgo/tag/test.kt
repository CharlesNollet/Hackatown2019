package club.touchandgo.tag

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.github.kittinunf.fuel.*
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.result.*
import com.google.gson.Gson
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
}

