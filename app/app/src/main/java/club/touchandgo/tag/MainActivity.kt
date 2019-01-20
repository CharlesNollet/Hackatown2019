package club.touchandgo.tag

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_test.*

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
        URL.httpGet().responseString { _, _, result ->
            when (result) {
                is Result.Failure -> {
                    val ex = result.error.exception
                }
                is Result.Success -> {
                    val data = result.get()
                    game1Button.text = data
                }
            }
        }
    }
}
