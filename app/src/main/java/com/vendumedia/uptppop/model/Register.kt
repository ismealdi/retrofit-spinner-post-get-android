package com.vendumedia.uptppop.model

import com.google.gson.annotations.SerializedName

/**
 * Created by aldimaulana on 11/7/17.
 */

object Register {
	data class Response(
            @SerializedName("message") val message: String = "", //Berhasil mendapatkan data provinsi!
            @SerializedName("status") val status: Int = 0, //200
            @SerializedName("data") val data: Data
	)

	data class Data(
			@SerializedName("alamat") val alamat: String = "",
			@SerializedName("alamat_club") val alamatClub: String = "",
			@SerializedName("anak_ke") val  anakKe: String = "",
			@SerializedName("asal_sekolah") val asalSekolah: String = "",
			@SerializedName("berat") val berat: String = "",
			@SerializedName("cabang_olahraga") val cabangOlahraga: String = "",
			@SerializedName("foto") val foto: String = "",
			@SerializedName("foto_akta") val fotoAkta: String = "",
			@SerializedName("foto_prestasi") val fotoPrestasi: String = "",
			@SerializedName("foto_sttb") val fotoSttb: String = "",
			@SerializedName("full_name") val fullName: String = "",
			@SerializedName("golongan_darah") val golonganDarah: String = "",
			@SerializedName("hobby") val hobby: String = "",
			@SerializedName("id_location") val idLocation: String = "",
			@SerializedName("jenis_kelamin") val jenisKelamin: String = "",
			@SerializedName("jumlah_saudara") val jumlahSaudara: String = "",
			@SerializedName("kode_pos") val kodePos: String = "",
			@SerializedName("nama_club") val namaClub: String = "",
			@SerializedName("nama_wali") val namaWali: String = "",
			@SerializedName("phone") val phone: String = "",
			@SerializedName("prestasi_terbaik") val prestasiTerbaik: String = "",
			@SerializedName("tanggal_lahir") val tanggalLahir: String = "",
			@SerializedName("tempat_lahir") val tempatLahir: String = "",
			@SerializedName("tinggi") val tinggi: String = ""
	)
}