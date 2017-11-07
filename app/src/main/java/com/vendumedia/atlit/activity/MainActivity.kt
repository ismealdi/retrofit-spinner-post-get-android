package com.vendumedia.atlit.activity

import android.app.DatePickerDialog
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.ArrayAdapter
import android.widget.Spinner

import com.vendumedia.atlit.R
import kotterknife.bindView
import android.graphics.Rect
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.vendumedia.atlit.api.Indonesia
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    val golonganDarah: Spinner by bindView(R.id.in_golongan_darah)
    val aktaKelahiran: Spinner by bindView(R.id.in_akta_kelahiran)
    val sttb: Spinner by bindView(R.id.in_sttb)
    val prestasi: Spinner by bindView(R.id.in_prestasi)
    val tanggalLahir: EditText by bindView(R.id.in_tanggal_lahir)

    var disposable: Disposable? = null

    val indonesia by lazy {
        Indonesia.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

        getProvince()
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
    }

    override fun onResume() {
        super.onResume()

    }

    private fun getProvince() {
        disposable = indonesia.province()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    Toast.makeText(this, "${result.message}", Toast.LENGTH_SHORT).show()

                    result.data.forEach {
                        println(it.name)
                    }
                    
                },
                { error -> Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show() }
            )
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

}
