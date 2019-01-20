package club.touchandgo.tag

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.kittinunf.fuel.*
import com.github.kittinunf.result.*
import kotlinx.android.synthetic.main.activity_test.*

class test : AppCompatActivity() {

    var URL = "http://207.246.122.125:8080/games"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)


            URL.httpGet()
            .responseString { request, response, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.error.exception
                    }
                    is Result.Success -> {
                        val data = result.get()
                        getResult.text = data
                    }
                }
            }
    }
}
