package club.touchandgo.tag

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*

data class Game(val name : String, val isPublic : Boolean){
    class Deserializer : ResponseDeserializable<Array<Game>> {
        override fun deserialize(content: String): Array<Game>? = Gson().fromJson(content, Array<Game>::class.java)
    }
}

class MainActivity : AppCompatActivity() {

    var URL = "http://207.246.122.125:8080/getGames"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume(){
        super.onResume()
        getGameNames()
    }

    /** called when the user taps the New Game Button */
    fun goToNewGameActivity(view: View) {
        val intent = Intent(this, NewGameActivity::class.java)
        startActivity(intent)
    }

    private fun getGameNames(){
        URL.httpGet()
            .responseObject(Game.Deserializer()) { request, response, result ->
                val (games, err) = result
                game1Button.text = games!![0].name
                game2Button.text = games!![1].name
                game3Button.text = games!![2].name
                game4Button.text = games!![3].name
            }
    }
}
