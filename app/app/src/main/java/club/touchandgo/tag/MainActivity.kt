package club.touchandgo.tag

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock
import android.util.Log
import android.view.View
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.httpGet
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast



data class Game(val name : String, val public : Boolean){
    class Deserializer : ResponseDeserializable<Array<Game>> {
        override fun deserialize(content: String): Array<Game>? = Gson().fromJson(content, Array<Game>::class.java)
    }
}

class MainActivity : AppCompatActivity(), GameSelectionButtonView.Listener {

    var URL = "http://207.246.122.125:8080/getGames"
    var games: List<Game>? = ArrayList()

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
                runOnUiThread{
                    val (games, err) = result
                    if(games != null) {
                        for (index in games.indices) {
                            Log.d("HELLLLLLOOOOOOO", "IN LOOP")
                            val itemView = GameSelectionButtonView(applicationContext)
                            itemView.setTitle(games[index].name)
                            itemView.listener = this
                            gameList.addView(itemView)
                        }
                    }
                }
            }
    }

    override fun goToUsernameActivity(gameName: String) {
        val message  = gameName
        val intent = Intent(this, UsernameActivity::class.java).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, message)
        }
        startActivity(intent)
    }
}
