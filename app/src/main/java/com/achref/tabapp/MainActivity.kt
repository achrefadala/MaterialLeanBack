package com.achref.tabapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.achref.leanbackrecycler.MaterialLeanBack
import com.achref.tabapp.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso


class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mBinding.root
        setContentView(view)

        mBinding.materialLeanBack.adapter = object : MaterialLeanBack.Adapter<TestViewHolder?>() {
            override fun getLineCount(): Int {
                return 10
            }

            override fun getCellsCount(line: Int): Int {
                return 10
            }

            override fun onCreateViewHolder(viewGroup: ViewGroup, line: Int): TestViewHolder {
                val view: View = LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.cell_test, viewGroup, false)
                return TestViewHolder(view)
            }

            override fun onBindViewHolder(viewHolder: TestViewHolder?, i: Int) {
                viewHolder?.textView?.text = "test $i"
                val url =
                    "http://www.lorempixel.com/40" + viewHolder?.row.toString() + "/40" + viewHolder?.cell.toString() + "/"
                Picasso.get().load(url).into(viewHolder?.imageView)
            }

            override fun getTitleForRow(row: Int): String {
                return "Line $row"
            }

            override fun getEnlargedItemPosition(position: Int): Int {
                Toast.makeText(this@MainActivity, position.toString(), Toast.LENGTH_LONG).show()
                return super.getEnlargedItemPosition(position)
            }

            //Custom
            override fun getCustomViewForRow(
                viewgroup: ViewGroup,
                row: Int
            ): RecyclerView.ViewHolder? {
                return if (row == 3) {
                    val view: View = LayoutInflater.from(viewgroup.context)
                        .inflate(R.layout.header, viewgroup, false)
                    object : RecyclerView.ViewHolder(view) {}
                } else null
            }

            override fun isCustomView(row: Int): Boolean {
                return row == 3
            }

            override fun onBindCustomView(viewHolder: RecyclerView.ViewHolder?, row: Int) {
                super.onBindCustomView(viewHolder, row)
            }
        }
    }
}