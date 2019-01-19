package club.touchandgo.tag

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View
import android.widget.EditText

class NewGameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_game)
    }

    /** called when the user taps the Create Game Button */
    fun goToUsernameActivity(view: View) {
        val gameName = findViewById<EditText>(R.id.editGameName)
        val message  = gameName.text.toString()
        val intent = Intent(this, UsernameActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
    }
}
