package dam.inaki.u2p4conversor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "LogsConversor";

    // TODO: Activ 4.2 Claves para guardar el estado de los TextView
    private static final String KEY_RESULTADO = "resultado";
    private static final String KEY_ERROR = "error";
    private static final String KEY_ERROR_VISIBLE = "errorVisible";

    // TODO: Activ 6 Preferencias donde se guarda la valoración (0 = aún sin valorar)
    private static final String PREFS = "PrefsConversor";
    private static final String KEY_VALORACION = "valoracion";

    // TODO: Activ 5.1 Factor de cada conversión (mismo orden que R.array.conversiones)
    // TODO: Activ 1.1 Cada conversión tiene su sentido contrario (factor y 1/factor)
    private static final double[] FACTORES = {
            2.54, 1 / 2.54,             // pulgadas <-> cm
            2.20462, 1 / 2.20462,       // kg <-> libras
            0.621371, 1 / 0.621371      // km <-> millas
    };

    private Spinner spConversion;
    private EditText etValor;
    private TextView tvResultado;
    private TextView tvError;
    private TextView tvValoracion;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setUI();
        // TODO: Activ 3.1 Log del ciclo de vida
        Log.d(TAG, "onCreate");
    }

    private void setUI() {
        spConversion = findViewById(R.id.sp_Conversion);
        etValor = findViewById(R.id.et_Valor);
        tvResultado = findViewById(R.id.tv_Resultado);
        tvError = findViewById(R.id.tv_Error);
        tvValoracion = findViewById(R.id.tv_Valoracion);
        ratingBar = findViewById(R.id.rating_Bar);
        Button buttonConvertir = findViewById(R.id.button_Convertir);
        ImageButton ibAyuda = findViewById(R.id.ib_Ayuda);

        buttonConvertir.setOnClickListener(view -> {
            // TODO: Activ 3.2 Log al pulsar un botón indicando cuál
            Log.i(TAG, "Botón pulsado: Convertir");
            try {
                // TODO: Activ 1.1 Conversión en ambos sentidos según la opción elegida
                int posicion = spConversion.getSelectedItemPosition();
                String valor = convertir(etValor.getText().toString(), FACTORES[posicion]);
                String unidad = getResources().getStringArray(R.array.unidades_destino)[posicion];
                tvResultado.setText(getString(R.string.resultado_formato, valor, unidad));
                // TODO: Activ 2.2 Si todo va bien, el error se oculta
                tvError.setVisibility(View.GONE);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                // TODO: Activ 2.2 Mostrar el mensaje de error en el TextView (visible solo si hay error)
                tvResultado.setText("");
                tvError.setText(e.getMessage());
                tvError.setVisibility(View.VISIBLE);
            }
        });

        // TODO: Activ 5.2 Abre la Activity de ayuda con un Intent explícito
        ibAyuda.setOnClickListener(view -> {
            Log.i(TAG, "Botón pulsado: Ayuda");
            startActivity(new Intent(this, HelpActivity.class));
        });
    }

    // TODO: Activ 5.1 Un único método de conversión reutilizable (código limpio):
    //  recibe el factor de la conversión elegida y lanza las mismas excepciones de la Activ 2
    // TODO: Activ 2.1 convertir lanza excepciones que captura el try...catch de setUI()
    // TODO: Activ 1.2 Resultado formateado con dos decimales
    private String convertir(String texto, double factor) throws Exception {
        texto = texto.trim();
        if (texto.isEmpty()) {
            throw new Exception(getString(R.string.error_vacio));
        }
        // Evita casos como "." que no son un número (así no hace falta otro try...catch)
        if (!texto.matches("\\d+([.,]\\d*)?|[.,]\\d+")) {
            throw new Exception(getString(R.string.error_formato));
        }
        double valor = Double.parseDouble(texto.replace(',', '.'));
        if (valor < 1) {
            throw new Exception(getString(R.string.error_minimo));
        }
        return String.format(Locale.getDefault(), "%.2f", valor * factor);
    }

    // TODO: Activ 4.1 Al rotar, Android destruye y recrea la Activity (onDestroy -> onCreate).
    //  El layout se infla de nuevo y los TextView vuelven al valor del XML, así que el
    //  resultado y el error se pierden porque solo estaban guardados en las vistas.

    // TODO: Activ 4.2 Guardar el estado de los TextView antes de que la Activity se destruya
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_RESULTADO, tvResultado.getText().toString());
        outState.putString(KEY_ERROR, tvError.getText().toString());
        outState.putBoolean(KEY_ERROR_VISIBLE, tvError.getVisibility() == View.VISIBLE);
        Log.d(TAG, "onSaveInstanceState");
    }

    // TODO: Activ 4.2 Restaurar el estado de los TextView al recrear la Activity
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tvResultado.setText(savedInstanceState.getString(KEY_RESULTADO, ""));
        tvError.setText(savedInstanceState.getString(KEY_ERROR, ""));
        tvError.setVisibility(savedInstanceState.getBoolean(KEY_ERROR_VISIBLE, false)
                ? View.VISIBLE : View.GONE);
        Log.d(TAG, "onRestoreInstanceState");
    }

    // TODO: Activ 3.1 Estados del ciclo de vida de la Activity
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    // TODO: Activ 6 Al mostrarse la Activity: si ya hay valoración guardada, se muestra y se bloquea;
    //  si no, el usuario puede valorar (y cambiarla) mientras la Activity siga en primer plano.
    @Override
    protected void onResume() {
        super.onResume();
        float guardada = getSharedPreferences(PREFS, MODE_PRIVATE).getFloat(KEY_VALORACION, 0f);
        if (guardada > 0) {
            ratingBar.setRating(guardada);
            ratingBar.setIsIndicator(true); // solo lectura
            tvValoracion.setText(getString(R.string.valoracion_guardada, guardada));
        } else {
            ratingBar.setIsIndicator(false);
            tvValoracion.setText(R.string.valora_titulo);
        }
        Log.d(TAG, "onResume");
    }

    // TODO: Activ 6 Al perder el primer plano se guarda la valoración de forma persistente.
    //  onPause() es el único callback que se garantiza antes de que el proceso pueda ser eliminado.
    @Override
    protected void onPause() {
        super.onPause();
        if (!ratingBar.isIndicator() && ratingBar.getRating() > 0) {
            getSharedPreferences(PREFS, MODE_PRIVATE)
                    .edit()
                    .putFloat(KEY_VALORACION, ratingBar.getRating())
                    .apply();
            Log.i(TAG, "Valoración guardada: " + ratingBar.getRating());
        }
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}