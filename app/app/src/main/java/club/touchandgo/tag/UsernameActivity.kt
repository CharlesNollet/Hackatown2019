package club.touchandgo.tag

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_username.*

class UsernameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_username)

        // Get the Intent that started this activity and extract the string
        val message = intent.getStringExtra(EXTRA_MESSAGE)

        // Capture the layout's TextView and set the string as its text
        playersInGameMessage.text = playersInGameMessage.text.toString().plus(" ").plus(message)
    }

    /** called when the user taps the Join Button */
    fun goToMapsActivity(view: View) {
        val intent = Intent(this, test::class.java)
        startActivity(intent)
    }
}
