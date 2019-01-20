package club.touchandgo.tag

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.widget.EditText
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.activity_new_game.*
import org.json.JSONObject

class NewGameActivity : AppCompatActivity() {

    var URL = "http://207.246.122.125:8080/postGame"
    var error: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_game)

        createGameButton.setOnClickListener {
            if(editGameName.text.toString().isBlank()) {
                editGameName.error = "Game Name is blank"
            } else {
                postGame()
            }
        }
    }

    private fun postGame(){
        val json = JSONObject()
        json.put("name", editGameName.text.toString())
        json.put("public", "true")

        val request = URL.httpPost().body(json.toString())
        request.httpHeaders["Content-Type"] = "application/json"
        request.responseString{ _, response, result ->
            val code = response.httpStatusCode
            if(code == 400){
                error = true
            }
            when (result) {
                is Result.Failure -> {
                    val ex = result.error.exception
                }
                is Result.Success -> {
                    goToUsernameActivity()
                }
            }
        }
        if(error){
            editGameName.error = "Name already taken"
        }

    }

    /** called when the user taps the Create Game Button */
    private fun goToUsernameActivity() {
        val message  = editGameName.text.toString()
        val intent = Intent(this, UsernameActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
    }
}
