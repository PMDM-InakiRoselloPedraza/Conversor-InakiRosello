package dam.inaki.u2p4conversor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

    private static final String KEY_RESULTADO = "resultado";
    private static final String KEY_ERROR = "error";
    private static final String KEY_ERROR_VISIBLE = "errorVisible";

    // TODO: Activ 5.1 Factor de cada conversión (mismo orden que R.array.conversiones)
    private static final double[] FACTORES = {
            2.54, 1 / 2.54,             // pulgadas <-> cm
            2.20462, 1 / 2.20462,       // kg <-> libras
            0.621371, 1 / 0.621371      // km <-> millas
    };

    private Spinner spConversion;
    private EditText etValor;
    private TextView tvResultado;
    private TextView tvError;

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
        Log.d(TAG, "onCreate");
    }

    private void setUI() {
        spConversion = findViewById(R.id.sp_Conversion);
        etValor = findViewById(R.id.et_Valor);
        tvResultado = findViewById(R.id.tv_Resultado);
        tvError = findViewById(R.id.tv_Error);
        Button buttonConvertir = findViewById(R.id.button_Convertir);
        ImageButton ibAyuda = findViewById(R.id.ib_Ayuda);

        buttonConvertir.setOnClickListener(view -> {
            Log.i(TAG, "Botón pulsado: Convertir");
            try {
                int posicion = spConversion.getSelectedItemPosition();
                String valor = convertir(etValor.getText().toString(), FACTORES[posicion]);
                String unidad = getResources().getStringArray(R.array.unidades_destino)[posicion];
                tvResultado.setText(getString(R.string.resultado_formato, valor, unidad));
                tvError.setVisibility(View.GONE);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
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
    private String convertir(String texto, double factor) throws Exception {
        texto = texto.trim();
        if (texto.isEmpty()) {
            throw new Exception(getString(R.string.error_vacio));
        }
        if (!texto.matches("\\d+([.,]\\d*)?|[.,]\\d+")) {
            throw new Exception(getString(R.string.error_formato));
        }
        double valor = Double.parseDouble(texto.replace(',', '.'));
        if (valor < 1) {
            throw new Exception(getString(R.string.error_minimo));
        }
        return String.format(Locale.getDefault(), "%.2f", valor * factor);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_RESULTADO, tvResultado.getText().toString());
        outState.putString(KEY_ERROR, tvError.getText().toString());
        outState.putBoolean(KEY_ERROR_VISIBLE, tvError.getVisibility() == View.VISIBLE);
        Log.d(TAG, "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tvResultado.setText(savedInstanceState.getString(KEY_RESULTADO, ""));
        tvError.setText(savedInstanceState.getString(KEY_ERROR, ""));
        tvError.setVisibility(savedInstanceState.getBoolean(KEY_ERROR_VISIBLE, false)
                ? View.VISIBLE : View.GONE);
        Log.d(TAG, "onRestoreInstanceState");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
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