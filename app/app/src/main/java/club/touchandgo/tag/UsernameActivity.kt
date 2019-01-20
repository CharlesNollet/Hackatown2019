package club.touchandgo.tag

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.widget.LinearLayout
import com.github.kittinunf.fuel.core.ResponseDeserializable
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_new_game.*
import kotlinx.android.synthetic.main.activity_username.*
import org.json.JSONObject

data class Player(val game : String, val username : String, val lat : Float, val long : Float, val tag : Boolean ){
    class Deserializer : ResponseDeserializable<Array<Player>> {
        override fun deserialize(content: String): Array<Player>? = Gson().fromJson(content, Array<Player>::class.java)
    }
}

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
            if(editUsername.text.toString().isBlank()) {
                editUsername.error = "Username is blank"
            } else {
                postPlayerName()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getPlayerNames()
    }

    private fun getPlayerNames(){
        getPlayersURL.httpGet().responseObject(Player.Deserializer()) { request, response, result ->
            runOnUiThread{
                val (players, err) = result
                if (players != null){
                    if((playerList as LinearLayout).childCount > 0){
                        (playerList as LinearLayout).removeAllViews()
                    }
                    for (index in players.indices) {
                        if (players[index].game == gameName) {
                            val itemView = PlayerInGameTextView(applicationContext)
                            itemView.setTitle(players[index].username)
                            playerList.addView(itemView)
                        }
                    }
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
