package com.example.mymaps

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymaps.Models.Place
import com.example.mymaps.Models.UserMap
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_main.*

const val EXTRA_USER_MAP = "EXTRA_USER_MAP"
const val EXTRA_MAP_TITLE = "EXTRA_MAP_TITLE"
private const val REQUEST_CODE = 1234
private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private lateinit var userMaps: MutableList<UserMap>
    private lateinit var mapAdapter: MapsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userMaps = generateSampleData().toMutableList()

        rvMaps.layoutManager = LinearLayoutManager(this)
        mapAdapter = MapsAdapter(this, userMaps, object: MapsAdapter.OnClickListener{
            override fun onItemClick(postion: Int) {
                Log.i(TAG, "onItemClick $postion")
                val intent = Intent(this@MainActivity, DisplayMapActivity::class.java)
                intent.putExtra(EXTRA_USER_MAP, userMaps[postion])
                startActivity(intent)
            }
        })
        rvMaps.adapter = mapAdapter

        fabCreateMap.setOnClickListener{
            Log.i(TAG, "Tap on FAB")
            showAlertDialog()
        }
    }

    private fun showAlertDialog() {
        val mapFormView = LayoutInflater.from(this).inflate(R.layout.dialog_create_map, null)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Map Title")
            .setView(mapFormView)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("OK", null)
            .show()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener{
            val title = mapFormView.findViewById<EditText>(R.id.etTitle).text.toString()
            if (title.trim().isEmpty()) {
                Toast.makeText(this, "Map must have non-empty title", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            //Menavigasi untuk membuat map activity
            val intent = Intent(this@MainActivity, CreateMapActivity::class.java)
            intent.putExtra(EXTRA_MAP_TITLE, title)
            startActivityForResult(intent, REQUEST_CODE)
            dialog.dismiss()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE &&resultCode == Activity.RESULT_OK){
            val userMap = data?.getSerializableExtra(EXTRA_USER_MAP) as UserMap
            Log.i(TAG, "onActivityResult wih new map title ${userMap.title}")
            userMaps.add(userMap)
            mapAdapter.notifyItemInserted(userMaps.size - 1)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun generateSampleData(): List<UserMap> {
        return listOf(
            UserMap(
                "Memories from Studytour",
                listOf(
                    Place("ITB Library", "Best building at ITB", -6.887938009870311, 107.61073964386473),
                    Place("Malioboro", "Many long nights in this street", -7.792348890785808, 110.36585283965266),
                    Place("THE 1O1 Bandung Dago", "First meet with my best friend", -6.905164311855221, 107.61030719197701)
                )
            ),
            UserMap("July vacation planning!",
                listOf(
                    Place("Bandung", "Overnight layover", -6.921218226811241, 107.60928067013467),
                    Place("Purwakarta", "Family visit + wedding!", -6.553035706101386, 107.44480820863332),
                    Place("Subang", "Experience a new vacation spot", -6.555957136714228, 107.75793165865987)
                )),
            UserMap("Malang travel itinerary",
                listOf(
                    Place("Jodipan Malang", "A colorful stairs village", -7.983272671092126, 112.63758066481562),
                    Place("Jatim Park 2", "Family-friendly park with many varieties of animals", -7.888734320975891, 112.52957468198291),
                    Place("Museum Angkut", "A huge museum of transportation", -7.877941822648657, 112.52030301422366),
                    Place("Mt Bromo", "Looking for sunrise", -7.9401309829154325, 112.95249098032352)
                )
            ),
            UserMap("Restaurants to try",
                listOf(
                    Place("Kubu at Mandapa", "Retro diner in Bali", -8.485095902496056, 115.24437113062048),
                    Place("Pupuk Bawang", "Malang upscale dining with an amazing view", -7.866475768453892, 112.51180520261092),
                    Place("Sushi Ichi", "Elegant sushi in Jakarta", -6.193277885126807, 106.82371059294789),
                    Place("Jittlada", "Authentic Thai food, served with love", -6.301725891181579, 106.6542911274702)
                )
            )
        )
    }
}