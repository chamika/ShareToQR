package com.chamika.sharetoqr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chamika.sharetoqr.databinding.FragmentQrDisplayBinding
import com.google.zxing.WriterException

import android.os.Handler
import android.os.Looper
import android.util.Log

import androidmads.library.qrgenearator.QRGContents

import androidmads.library.qrgenearator.QRGEncoder
import java.util.concurrent.Executors


/**
 * Fragment for showing QR
 */
class QrDisplayFragment : Fragment() {

    private var _binding: FragmentQrDisplayBinding? = null

    //TODO use Kotlin Coroutines
    private val executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())

    private val binding get() = _binding!!

    companion object {
        private val TAG = QrDisplayFragment::class.java.simpleName
        public val EXTRA_TYPE = "type"
        public val EXTRA_VALUE = "value"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQrDisplayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val type = requireArguments().getInt(EXTRA_TYPE)
        val value = requireArguments().getString(EXTRA_VALUE)
        showQR(mapType(type), readValue(value))
    }

    private fun readValue(value: String?) =
        value ?: throw IllegalArgumentException("Value not found")

    private fun mapType(type: Int): String {
        return when (type) {
            1 -> QRGContents.Type.TEXT
            else -> throw IllegalArgumentException("Type $type is not supported")
        }
    }

    private fun showQR(type: String, value: String) {
        val qrgEncoder = QRGEncoder(value, type,800)
        executor.execute {
            try {
                val bitmap = qrgEncoder.bitmap
                handler.post {
                    binding.imageQr.setImageBitmap(bitmap)
                }
            } catch (e: WriterException) {
                Log.e(TAG, e.toString())
                handler.post {
                    binding.imageQr.setImageResource(R.drawable.qr_error)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}