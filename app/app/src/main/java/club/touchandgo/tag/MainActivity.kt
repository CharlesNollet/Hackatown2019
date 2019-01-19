package club.touchandgo.tag

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /** called when the user taps the New Game Button */
    fun goToNewGameActivity(view: View) {
        val intent = Intent(this, NewGameActivity::class.java)
        startActivity(intent)
    }
}
