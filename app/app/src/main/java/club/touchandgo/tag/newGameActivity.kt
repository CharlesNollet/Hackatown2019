package club.touchandgo.tag

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class newGameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_game)
    }

    /** called when the user taps the Create Game Button */
    fun goToUsernameActivity(view: View) {
        val intent = Intent(this, usernameActivity::class.java)
        startActivity(intent)
    }
}
