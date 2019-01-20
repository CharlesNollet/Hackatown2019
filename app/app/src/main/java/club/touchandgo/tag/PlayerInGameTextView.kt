package club.touchandgo.tag

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.player_name_item.view.*

class PlayerInGameTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {

    var playerName = ""

    init {
        LayoutInflater.from(context).inflate(R.layout.player_name_item, this, true)

    }

    fun setTitle(title:String) {
        playerName = title
        playerNameItem.text = title
    }

}