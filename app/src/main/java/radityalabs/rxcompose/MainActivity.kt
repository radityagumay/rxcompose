package radityalabs.rxcompose

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainContract.View {
    companion object {
        val TAG = MainActivity::class.java.simpleName
    }

    private lateinit var mPresenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mPresenter = MainPresenter(this)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        fab.setOnClickListener {
            mPresenter.doSomeWork()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showCompleteWork(status: String) {
        Log.d(TAG, "completed $status")
    }

    override fun showLoading() {
        Log.d(TAG, "showLoading")
    }

    override fun hideLoading() {
        Log.d(TAG, "hideLoading")
    }

    override fun showAfterWork(number : Double) {
        Log.d(TAG, "after $number")
    }

    override fun showBeforeWork(number : Double) {
        Log.d(TAG, "before $number")
    }
}