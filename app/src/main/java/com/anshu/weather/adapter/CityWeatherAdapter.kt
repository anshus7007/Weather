package com.anshu.weather.adapter

import android.content.Context
import android.content.Intent

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*

import androidx.recyclerview.widget.RecyclerView
import com.anshu.weather.R
import com.anshu.weather.db.db1_for_city_name.entity.CityWeather
import com.anshu.weather.city_view_model.CityViewModel
import com.anshu.weather.ui.activity.WeatherActivity


class CityWeatherAdapter(
    val context: Context,
    var items: List<CityWeather>,
    private val viewModel: CityViewModel

    ) : RecyclerView.Adapter<CityWeatherAdapter.CityWeatherViewHolder>(){



//         var multiSelect=false
//    var selected=false
//
//    var selectedItems= arrayListOf<CityWeather>()
//
//        private var actionModeCallbacks: ActionMode.Callback = object : ActionMode.Callback {
//            override fun onCreateActionMode(mode: ActionMode?, menu: Menu): Boolean {
//                multiSelect = true
//                menu.add("Delete")
//                return true
//            }
//
//            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
//                return false
//            }
//
//            override fun onActionItemClicked(mode: ActionMode, item: MenuItem?): Boolean {
//                for (intItem in selectedItems) {
//                    items.remove(intItem)
//                    Constants.cityList.remove(intItem)
//
//                    val result= DBAsyncTask(context, intItem, 2).execute().get()
//
//                    if(result){
//                        Constants.cityList.remove(intItem)
//                        notifyDataSetChanged()
//                        Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show()
//
//
//
//                    }
//                }
//                mode.finish()
//                return true
//            }
//
//            override fun onDestroyActionMode(mode: ActionMode) {
//                multiSelect = false
//                selected=false
//                selectedItems.clear()
//                notifyDataSetChanged()
//            }
//
//    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityWeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.city_weather_single_row,
            parent,
            false
        )
        return CityWeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: CityWeatherViewHolder, position: Int) {

        val currItem=items[position]
            if(currItem.wrongRight==false) {
                holder.wrong_rel.visibility=View.VISIBLE
                holder.right_rel.visibility=View.GONE
                holder.wrong_city.text = currItem.name

                holder.wrong_city.setOnClickListener {
                    val intent = Intent(context, WeatherActivity::class.java)
                    intent.putExtra("city_name", currItem.name)
                    intent.putExtra("from", "fromSearch")

                    context.startActivity(intent)
                }
                holder.wrong_delete.setOnClickListener {
//                val result = DBAsyncTask(context, cityEntity, 2).execute().get()
//
//                if (result) {
                    viewModel.delete(currItem)

                    Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show()

//
//                }
//
//                    Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show()
//
//                }

                }
            }
        else
            {
                holder.wrong_rel.visibility=View.GONE
                holder.right_rel.visibility=View.VISIBLE
                holder.right_city.text = currItem.name
                holder.right_city.setOnClickListener {
                    val intent = Intent(context, WeatherActivity::class.java)
                    intent.putExtra("city_name", currItem.name)
                    intent.putExtra("from", "fromSearch")

                    context.startActivity(intent)
                }
                holder.rightdelete.setOnClickListener {
//                val result = DBAsyncTask(context, cityEntity, 2).execute().get()
//
//                if (result) {
                    viewModel.delete(currItem)

                    Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show()

//
//                }
//
//                    Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show()
//
//                }

                }
            }
//        holder.update(currItem);

            val cityEntity = CityWeather(
                currItem.name,
                    currItem.wrongRight
            )

//        val db = Room.databaseBuilder(context!!, CityDatabase::class.java, "cityDB.db").allowMainThreadQueries().build()

//        holder.delete.setOnClickListener {
////                val result = DBAsyncTask(context, cityEntity, 2).execute().get()
////
////                if (result) {
//            viewModel.delete(currItem)
//
//                    Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show()
//
////
////                }
////
////                    Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show()
////
////                }
//
//        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
   inner class CityWeatherViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val right_rel:RelativeLayout=itemView.findViewById(R.id.right_rel)
        val wrong_city:TextView=itemView.findViewById(R.id.Wrongcity)
        val right_city:TextView=itemView.findViewById(R.id.Rightcity)

        val rightdelete:ImageView=itemView.findViewById(R.id.Rightdelete)
        val wrong_rel:RelativeLayout=itemView.findViewById(R.id.wrong_rel)
        val wrong_delete:ImageView=itemView.findViewById(R.id.Wrongdelete)

//
//        fun selectItem(item: CityWeather) {
//
//
//            if (multiSelect) {
//
//
//                if (selectedItems.contains(item)) {
//
//
//                    selectedItems.remove(item);
//                frameLayout.setBackgroundColor(Color.parseColor("#424242"))
//            } else {
//
//
//                    selectedItems.add(item);
//                frameLayout.setBackgroundColor(Color.LTGRAY)
//            }
//        }
//    }
//
//        fun update(value: CityWeather) {
////        textView.setText(value + "");
//        if (selectedItems.contains(value)) {
//            frameLayout.setBackgroundColor(Color.LTGRAY);
//        }
//            else
//        {
//            frameLayout.setBackgroundColor(Color.parseColor("#424242"))
//
//        }
//
//
//            frameLayout.setOnLongClickListener {
//                    view ->
//
//                (view.context as AppCompatActivity).startSupportActionMode(actionModeCallbacks)
//                selectItem(value)
//                selected=true
//                true
//            }
//            frameLayout.setOnClickListener(){
////                delete.setOnClickListener {
////                    selectItem(value)
////                }
////                city.setOnClickListener {
////                    selectItem(value);
////
////                }
//                selectItem(value)
//
//            }
//        }

    }

//    class DBAsyncTask(val context: Context, val restaurantEntity: CityWeather, val mode: Int): AsyncTask<Void, Void, Boolean>() {
//
//        val db = Room.databaseBuilder(context, CityDatabase::class.java, "cityDB.db").build()
//        override fun doInBackground(vararg params: Void?): Boolean {
//            when (mode) {
//
//                1 -> {
//                    db.getCityDao().upsert(restaurantEntity)
//                    db.close()
//                    return true
//
//                }
//                2 -> {
//                    db.getCityDao().delete(restaurantEntity)
//                    db.close()
//                    return true
//
//                }
//            }
//            return false
//        }
//
//
//    }

}