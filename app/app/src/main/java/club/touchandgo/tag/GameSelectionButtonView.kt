package club.touchandgo.tag

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.game_selection_item.view.*

class GameSelectionButtonView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {

    lateinit var listener:GameSelectionButtonView.Listener
    var gameName = ""

    init {
        LayoutInflater.from(context).inflate(R.layout.game_selection_item, this, true)
        gameButtonItem.setOnClickListener {
            listener.goToUsernameActivity(gameName)
        }
    }

    fun setTitle(title:String) {
        gameName = title
        gameButtonItem.text = title
        gameButtonItem.paintFlags = gameButtonItem.paintFlags or Paint.FAKE_BOLD_TEXT_FLAG
    }

    interface Listener{
        fun goToUsernameActivity(name: String)
    }
}
