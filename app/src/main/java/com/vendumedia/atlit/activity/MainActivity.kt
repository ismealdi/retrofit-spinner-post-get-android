package com.vendumedia.atlit.activity

import android.app.DatePickerDialog
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent

import com.vendumedia.atlit.R
import kotterknife.bindView
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.vendumedia.atlit.api.Indonesia
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import android.widget.Spinner



class MainActivity : AppCompatActivity() {

    val provinsi: Spinner by bindView(R.id.in_provinsi)
    var provinces: MutableList<String> = mutableListOf<String>()
    var provincesId: MutableList<Int> = mutableListOf<Int>()

    val city: Spinner by bindView(R.id.in_kota)
    var cities: MutableList<String> = mutableListOf<String>()
    var citiesId: MutableList<Int> = mutableListOf<Int>()

    val district: Spinner by bindView(R.id.in_kecamatan)
    var districts: MutableList<String> = mutableListOf<String>()
    var districtsId: MutableList<Long> = mutableListOf<Long>()

    val village: Spinner by bindView(R.id.in_desa)
    var villages: MutableList<String> = mutableListOf<String>()
    var villagesId: MutableList<Long> = mutableListOf<Long>()

    val progressAlamat: ProgressBar by bindView(R.id.progress_alamat)
    val golonganDarah: Spinner by bindView(R.id.in_golongan_darah)
    val aktaKelahiran: Spinner by bindView(R.id.in_akta_kelahiran)
    val sttb: Spinner by bindView(R.id.in_sttb)
    val prestasi: Spinner by bindView(R.id.in_prestasi)
    val tanggalLahir: EditText by bindView(R.id.in_tanggal_lahir)
    val alamat: EditText by bindView(R.id.in_alamat)
    var disposable: Disposable? = null

    val indonesia by lazy {
        Indonesia.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getProvince()
        setSpinnerItem()

        tanggalLahir.setOnClickListener {

            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val dateOfBirthRaw = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID")).parse("$year-$monthOfYear-$dayOfMonth")
                val dateOfBirth = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))

                tanggalLahir.setText(dateOfBirth.format(dateOfBirthRaw))

            }, year, month, day)

            datePickerDialog.show()

        }

    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev != null && ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawY.toInt(), ev.rawX.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }

        return super.dispatchTouchEvent(ev)
    }

    private fun setSpinnerItem() {
        val adapterGolonganDarah = ArrayAdapter(this,
                R.layout.spinner_single_simple, resources.getStringArray(R.array.golongan_darah))
        adapterGolonganDarah.setDropDownViewResource(R.layout.spinner_dropdown_simple)
        golonganDarah.adapter = adapterGolonganDarah

        val adapterAktaKelahiran = ArrayAdapter(this,
                R.layout.spinner_single_simple, resources.getStringArray(R.array.ya_no))
        adapterAktaKelahiran.setDropDownViewResource(R.layout.spinner_dropdown_simple)
        aktaKelahiran.adapter = adapterAktaKelahiran

        val adapterSttb = ArrayAdapter(this,
                R.layout.spinner_single_simple, resources.getStringArray(R.array.ya_no))
        adapterSttb.setDropDownViewResource(R.layout.spinner_dropdown_simple)
        sttb.adapter = adapterSttb

        val adapterPrestasi = ArrayAdapter(this,
                R.layout.spinner_single_simple, resources.getStringArray(R.array.ya_no))
        adapterPrestasi.setDropDownViewResource(R.layout.spinner_dropdown_simple)
        prestasi.adapter = adapterPrestasi

        val adapterProvinsi = ArrayAdapter(this,
                R.layout.spinner_single_simple, resources.getStringArray(R.array.provinsi))
        adapterProvinsi.setDropDownViewResource(R.layout.spinner_dropdown_simple)
        provinsi.adapter = adapterProvinsi

        val adapterKota = ArrayAdapter(this,
                R.layout.spinner_single_simple, resources.getStringArray(R.array.kota))
        adapterKota.setDropDownViewResource(R.layout.spinner_dropdown_simple)
        city.adapter = adapterKota

        val adapterKecamatan = ArrayAdapter(this,
                R.layout.spinner_single_simple, resources.getStringArray(R.array.kecamatan))
        adapterKecamatan.setDropDownViewResource(R.layout.spinner_dropdown_simple)
        district.adapter = adapterKecamatan

        val adapterDesa = ArrayAdapter(this,
                R.layout.spinner_single_simple, resources.getStringArray(R.array.desa))
        adapterDesa.setDropDownViewResource(R.layout.spinner_dropdown_simple)
        village.adapter = adapterDesa



    }

    override fun onResume() {
        super.onResume()

    }

    private fun getProvince() {
        provinceLoad(true)
        disposable = indonesia.province()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    // Toast.makeText(this, "${result.message}", Toast.LENGTH_SHORT).show()

                    provinces.clear()
                    provincesId.clear()

                    provinces.add("Pilih Provinsi")
                    provincesId.add(0)

                    result.data.forEach {
                        provinces.add(it.name)
                        provincesId.add(it.id)
                    }

                    val adapterProvinsi = ArrayAdapter(this,
                            R.layout.spinner_single_simple, provinces)
                    adapterProvinsi.setDropDownViewResource(R.layout.spinner_dropdown_simple)
                    provinsi.adapter = adapterProvinsi

                    provinceLoad(false)

                },
                { error ->
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                    provinceLoad(false)
                }
            )

        provinsi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (provincesId.count() > 0 && position > 0) {
                    getCity(provincesId[position])
                }
            }
        }


    }

    private fun provinceLoad(status: Boolean) {
        progressAlamat.visibility = if (status) View.VISIBLE else View.GONE
        provinsi.isEnabled = !status
        city.isEnabled = false
        district.isEnabled = false
        village.isEnabled = false
        alamat.isEnabled = false
    }

    private fun getCity(id: Int) {
        cityLoad(true)
        disposable = indonesia.city(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    // Toast.makeText(this, "${result.message}", Toast.LENGTH_SHORT).show()

                    cities.clear()
                    citiesId.clear()

                    cities.add("Pilih Kota/Kab")
                    citiesId.add(0)

                    result.data.cities.forEach {
                        cities.add(it.name)
                        citiesId.add(it.id)
                    }

                    val adapterCity = ArrayAdapter(this,
                            R.layout.spinner_single_simple, cities)
                    adapterCity.setDropDownViewResource(R.layout.spinner_dropdown_simple)
                    city.adapter = adapterCity

                    cityLoad(false)

                },
                { error ->
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                    cityLoad(false)
                }
            )

        city.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (citiesId.count() > 0 && position > 0) {
                    getDistrict(citiesId[position])
                }
            }
        }
    }

    private fun cityLoad(status: Boolean) {
        progressAlamat.visibility = if (status) View.VISIBLE else View.GONE
        provinsi.isEnabled = !status
        city.isEnabled = !status
        district.isEnabled = false
        village.isEnabled = false
        alamat.isEnabled = false
    }

    private fun getDistrict(id: Int) {
        districtLoad(true)
        disposable = indonesia.district(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            // Toast.makeText(this, "${result.message}", Toast.LENGTH_SHORT).show()

                            districts.clear()
                            districtsId.clear()

                            districts.add("Pilih Kecamatan")
                            districtsId.add(0)

                            result.data.districts.forEach {
                                districts.add(it.name)
                                districtsId.add(it.id)
                            }

                            val adapterDistrict = ArrayAdapter(this,
                                    R.layout.spinner_single_simple, districts)
                            adapterDistrict.setDropDownViewResource(R.layout.spinner_dropdown_simple)
                            district.adapter = adapterDistrict

                            districtLoad(false)

                        },
                        { error ->
                            Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                            districtLoad(false)
                        }
                )



        district.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (districtsId.count() > 0 && position > 0) {
                    getVillage(districtsId[position])
                }
            }
        }
    }

    private fun districtLoad(status: Boolean) {
        progressAlamat.visibility = if (status) View.VISIBLE else View.GONE
        provinsi.isEnabled = !status
        city.isEnabled = !status
        district.isEnabled = !status
        village.isEnabled = false
        alamat.isEnabled = false
    }

    private fun getVillage(id: Long) {
        villageLoad(true)
        disposable = indonesia.village(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            // Toast.makeText(this, "${result.message}", Toast.LENGTH_SHORT).show()

                            villages.clear()
                            villagesId.clear()

                            villages.add("Pilih Kelurahan/Desa")
                            villagesId.add(0)

                            result.data.villages.forEach {
                                villages.add(it.name)
                                villagesId.add(it.id)
                            }

                            val adapterVillage = ArrayAdapter(this,
                                    R.layout.spinner_single_simple, villages)
                            adapterVillage.setDropDownViewResource(R.layout.spinner_dropdown_simple)
                            village.adapter = adapterVillage

                            villageLoad(false)

                        },
                        { error ->
                            Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                            villageLoad(false)
                        }
                )
    }

    private fun villageLoad(status: Boolean) {
        progressAlamat.visibility = if (status) View.VISIBLE else View.GONE
        provinsi.isEnabled = !status
        city.isEnabled = !status
        district.isEnabled = !status
        village.isEnabled = !status
        alamat.isEnabled = !status
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

}
