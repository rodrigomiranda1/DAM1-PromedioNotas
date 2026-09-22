package com.cibertec.examenpractica2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.cibertec.examenpractica2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarOyentes()
    }

    private fun configurarOyentes(){
        binding.etNombre.doOnTextChanged { _, _, _, _ -> binding.tilNombre.error = null }
        binding.etPrimeraNota.doOnTextChanged { _, _, _, _ -> binding.tilPrimeraNota.error = null }
        binding.etSegundaNota.doOnTextChanged { _, _, _, _ -> binding.tilSegundaNota.error = null }

        binding.btnCalcular.setOnClickListener {
            validarYCalcular()
        }
    }

    private fun validarYCalcular(){
        val nombreTexto = binding.etNombre.text?.toString() ?: ""
        val nombreLimpio = nombreTexto.trim()

        var esValido = true


        if (nombreLimpio.isNullOrBlank()){
            binding.tilNombre.error = getString(R.string.error_nombre_vacio)
            esValido = false
        }

       val notaUno = parseDoubleonInput(binding.etPrimeraNota.text?.toString())
        if (notaUno == null || notaUno < 0.0 || notaUno > 20.0){
            binding.tilPrimeraNota.error = getString(R.string.error_numero_invalido)
            esValido = false
        }

        val notaDos = parseDoubleonInput(binding.etSegundaNota.text?.toString())
        if(notaDos == null || notaDos < 0.0 || notaDos > 20.0){
            binding.tilSegundaNota.error = getString(R.string.error_numero_invalido)
            esValido = false
        }

        if (!esValido) return

        val promedio = (notaUno!! + notaDos!!) / 2

        if(!promedio.isFinite()){
            binding.tilPrimeraNota.error = getString(R.string.error_numero_invalido)
            return
        }

        val clasificacion = when{
            promedio >= 0.0 && promedio < 11.0 -> getString(R.string.nota_desaprobado)
            promedio >= 11.0 && promedio < 16.0 -> getString(R.string.nota_aprobado)
            promedio >= 16.0 && promedio < 18.0 -> getString(R.string.nota_notable)
            promedio >= 18.0 && promedio <= 20.0 -> getString(R.string.nota_excelente)
            else -> getString(R.string.nota_desaprobado)
        }

        val intent = Intent(this, ResultadoActivity::class.java).apply {
            putExtra("EXTRA_NOMBRE", nombreLimpio)
            putExtra("EXTRA_RESULTADO", promedio)
            putExtra("EXTRA_CLASIFICACION", clasificacion)
        }
        startActivity(intent)
    }


    private fun parseDoubleonInput(input: String?): Double?{
        if (input.isNullOrBlank()) return null
        val normalizado = input.trim().replace(',', '.')
        val valor = normalizado.toDoubleOrNull() ?: return null
        return if(valor.isFinite()) valor else null
    }
}