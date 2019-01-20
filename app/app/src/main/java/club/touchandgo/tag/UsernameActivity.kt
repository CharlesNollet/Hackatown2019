package club.touchandgo.tag

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.activity_username.*
import org.json.JSONObject

class UsernameActivity : AppCompatActivity() {

    var postPlayerURL = "http://207.246.122.125:8080/postPlayer"
    var getPlayersURL = "http://207.246.122.125:8080/getPlayers"
    lateinit var gameName: String
    var error: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_username)

        // Get the Intent that started this activity and extract the string
        gameName = intent.getStringExtra(EXTRA_MESSAGE)

        // Capture the layout's TextView and set the string as its text
        playersInGameMessage.text = playersInGameMessage.text.toString().plus(" ").plus(gameName)

        joinGameButton.setOnClickListener {
            getPlayerNames()
            postPlayerName()
        }
    }

    override fun onResume() {
        super.onResume()
        getPlayerNames()
    }

    private fun getPlayerNames(){
        getPlayersURL.httpGet().responseString { _, _, result ->
            when (result) {
                is Result.Failure -> {
                    val ex = result.error.exception
                }
                is Result.Success -> {
                    val data = result.get()
                    playerList.text = data
                }
            }
        }
    }

    private fun postPlayerName(){
        val json = JSONObject()
        json.put("game", gameName)
        json.put("username", editUsername.text.toString())
        json.put("lat", "112")
        json.put("long", "74")
        json.put("tag", "false")

        val request = postPlayerURL.httpPost().body(json.toString())
        request.httpHeaders["Content-Type"] = "application/json"
        request.responseString{ _, response, result ->
            val code = response.httpStatusCode
            if(code == 400){
                error = true
            }
            when (result) {
                is Result.Failure -> {
                    val ex = result.error.exception
                    Log.e("Error", ex.toString())
                }
                is Result.Success -> {
                    goToMapsActivity()
                }
            }
        }
        if(error){
            editUsername.error = "Username already taken"
        }
    }

    /** called when the user taps the Join Button */
    private fun goToMapsActivity() {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }
}
