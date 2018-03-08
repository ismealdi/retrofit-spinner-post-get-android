package com.vendumedia.uptppop.activity

import android.Manifest
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent

import com.vendumedia.uptppop.R
import kotterknife.bindView
import android.graphics.Rect
import android.support.v7.widget.AppCompatImageButton
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.vendumedia.uptppop.api.Indonesia
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import android.widget.Spinner
import android.widget.Toast
import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v7.widget.AppCompatButton
import android.util.Log
import com.vendumedia.uptppop.api.Pendaftar
import com.vendumedia.uptppop.model.Register
import io.vrinda.kotlinpermissions.PermissionCallBack
import io.vrinda.kotlinpermissions.PermissionsActivity
import java.io.FileNotFoundException
import java.io.InputStream

class MainActivity : PermissionsActivity() {

    val provinsi : Spinner by bindView(R.id.in_provinsi)
    var provinces : MutableList<String> = mutableListOf<String>()
    var provincesId : MutableList<Int> = mutableListOf<Int>()

    val city : Spinner by bindView(R.id.in_kota)
    var cities : MutableList<String> = mutableListOf<String>()
    var citiesId : MutableList<Int> = mutableListOf<Int>()

    val district : Spinner by bindView(R.id.in_kecamatan)
    var districts : MutableList<String> = mutableListOf<String>()
    var districtsId : MutableList<Long> = mutableListOf<Long>()

    val village : Spinner by bindView(R.id.in_desa)
    var villages : MutableList<String> = mutableListOf<String>()
    var villagesId : MutableList<Long> = mutableListOf<Long>()
    var idLocation : String? = ""

    val pickAkta : AppCompatImageButton by bindView(R.id.pick_akta_kelahiran)
    val pickSttb : AppCompatImageButton by bindView(R.id.pick_sttb)
    val pickPrestasi : AppCompatImageButton by bindView(R.id.pick_prestasi)
    val pickFoto : AppCompatImageButton by bindView(R.id.pick_foto)
    val buttonDone : AppCompatButton by bindView(R.id.submit_done)

    val progressAlamat : ProgressBar by bindView(R.id.progress_alamat)
    val golonganDarah : Spinner by bindView(R.id.in_golongan_darah)
    val aktaKelahiran : Spinner by bindView(R.id.in_akta_kelahiran)
    val sttb : Spinner by bindView(R.id.in_sttb)
    val prestasi : Spinner by bindView(R.id.in_prestasi)
    val tanggalLahir : EditText by bindView(R.id.in_tanggal_lahir)
    val alamat : EditText by bindView(R.id.in_alamat)
    val kodePos : EditText by bindView(R.id.in_kodepos)
    var disposable : Disposable? = null
    var imageAkta : String? = ""
    var imageSttb : String? = ""
    var imagePrestasi : String? = ""
    var imageFoto : String? = ""

    val nama : EditText by bindView(R.id.in_full_name)
    val tempatLahir : EditText by bindView(R.id.in_tempat_lahir)
    val asalSekolah : EditText by bindView(R.id.in_asal_sekolah)
    val tinggiBadan : EditText by bindView(R.id.in_tinggi)
    val beratBadan : EditText by bindView(R.id.in_berat)
    val phone : EditText by bindView(R.id.in_phone)
    val hobby : EditText by bindView(R.id.in_hobby)
    val namaWali : EditText by bindView(R.id.in_wali)
    val anakKe : EditText by bindView(R.id.in_anak_ke)
    val jumlahSaudara : EditText by bindView(R.id.in_jumlah_saudara)
    val cabangOlahraga : EditText by bindView(R.id.in_cabang_olahraga)
    val namaClub : EditText by bindView(R.id.in_nama_club)
    val alamatClub : EditText by bindView(R.id.in_alamat_club)
    val prestasiTerbaik : EditText by bindView(R.id.in_prestasi_terbaik)
    val gender : RadioGroup by bindView(R.id.in_gender)


    val indonesia by lazy {
        Indonesia.create()
    }

    val pendaftar by lazy {
        Pendaftar.create()
    }

    private val REQUEST_SELECT_IMAGE_IN_ALBUM_AKTA = 1
    private val REQUEST_SELECT_IMAGE_IN_ALBUM_STTB = 2
    private val REQUEST_SELECT_IMAGE_IN_ALBUM_PRESTASI = 3
    private val REQUEST_SELECT_IMAGE_IN_FOTO = 4

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


        pickAkta.setOnClickListener {
            selectImageInAlbum(REQUEST_SELECT_IMAGE_IN_ALBUM_AKTA)
        }

        pickSttb.setOnClickListener {
            selectImageInAlbum(REQUEST_SELECT_IMAGE_IN_ALBUM_STTB)
        }

        pickPrestasi.setOnClickListener {
            selectImageInAlbum(REQUEST_SELECT_IMAGE_IN_ALBUM_PRESTASI)
        }

        pickFoto.setOnClickListener {
            selectImageInAlbum(REQUEST_SELECT_IMAGE_IN_FOTO)
        }

        buttonDone.setOnClickListener {
            submitData()
        }

    }

    private fun submitData() {
        val kelamin : RadioButton = findViewById(gender.checkedRadioButtonId);
        val registerData: Register.Data
        registerData = Register.Data(alamat.text.toString(), alamatClub.text.toString(), anakKe.text.toString(), asalSekolah.text.toString(), beratBadan.text.toString(), cabangOlahraga.text.toString(),
                imageFoto.toString(), imageAkta.toString(), imagePrestasi.toString(), imageSttb.toString(), nama.text.toString(), golonganDarah.selectedItem.toString(), hobby.text.toString(), idLocation.toString(),
                kelamin.text.toString(), jumlahSaudara.text.toString(), kodePos.text.toString(), namaClub.text.toString(), namaWali.text.toString(), phone.text.toString(), prestasiTerbaik.text.toString(), tanggalLahir.text.toString(), tempatLahir.text.toString(), tinggiBadan.text.toString());

        Log.i("aldieemaulana", "aldieemaulana: " + registerData.toString())
        Toast.makeText(this, "Loading ..", Toast.LENGTH_SHORT).show()
        disposable = pendaftar.store(registerData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            Log.i("aldieemaulana", "aldieemaulana: " + result.data.toString())

                             Toast.makeText(this, "${result.message}", Toast.LENGTH_SHORT).show()
                        },
                        { error ->
                            Log.i("aldieemaulana", "aldieemaulana: " + error.localizedMessage)
                            Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                        }
                )
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
        city.isEnabled = false

        val adapterKecamatan = ArrayAdapter(this,
                R.layout.spinner_single_simple, resources.getStringArray(R.array.kecamatan))
        adapterKecamatan.setDropDownViewResource(R.layout.spinner_dropdown_simple)
        district.adapter = adapterKecamatan
        district.isEnabled = false

        val adapterDesa = ArrayAdapter(this,
                R.layout.spinner_single_simple, resources.getStringArray(R.array.desa))
        adapterDesa.setDropDownViewResource(R.layout.spinner_dropdown_simple)
        village.adapter = adapterDesa
        village.isEnabled = false



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
        kodePos.isEnabled = false
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
        kodePos.isEnabled = false
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
        kodePos.isEnabled = false
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

        village.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (villagesId.count() > 0 && position > 0) {
                    idLocation = villagesId.get(position).toString()
                    alamat.isEnabled = true
                    kodePos.isEnabled = true
                }
            }
        }
    }

    private fun villageLoad(status: Boolean) {
        progressAlamat.visibility = if (status) View.VISIBLE else View.GONE
        provinsi.isEnabled = !status
        city.isEnabled = !status
        district.isEnabled = !status
        village.isEnabled = !status
        alamat.isEnabled = false
        kodePos.isEnabled = false
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    fun selectImageInAlbum(req: Int) {

        var status = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, object : PermissionCallBack {
                override fun permissionGranted() {
                    super.permissionGranted()
                }

                override fun permissionDenied() {
                    super.permissionDenied()
                    status = false
                }
            })
        }

        if(status){
            val intent = Intent()
            intent.type = "image/*"
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Pilih Dokumen"), req)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if(data?.data != null) {
                var imageStream: InputStream? = null
                try {
                    imageStream = this.contentResolver.openInputStream(data?.data)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }

                val selectedImage = BitmapFactory.decodeStream(imageStream)
                var imgBase64 = encodeToBase64(selectedImage)

                if (requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM_AKTA)
                    imageAkta = imgBase64
                else if (requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM_STTB)
                    imageSttb = imgBase64
                else if (requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM_PRESTASI)
                    imagePrestasi = imgBase64
                else if (requestCode == REQUEST_SELECT_IMAGE_IN_FOTO)
                    imageFoto = imgBase64
            }
        }

        return
    }

    private fun encodeToBase64(image: Bitmap): String {
        val byteArray = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 40, byteArray)
        val b = byteArray.toByteArray()

        return Base64.encodeToString(
                b,
                Base64.DEFAULT
        )
    }


}
