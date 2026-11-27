package dam.inaki.u2p4conversor;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    private static final double PULGADA_A_CM = 2.54;

    // TODO: Activ 4.2 Claves para guardar el estado de los TextView
    private static final String KEY_RESULTADO = "resultado";
    private static final String KEY_ERROR = "error";
    private static final String KEY_ERROR_VISIBLE = "errorVisible";

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
        EditText etValor = findViewById(R.id.et_Valor);
        tvResultado = findViewById(R.id.tv_Resultado);
        tvError = findViewById(R.id.tv_Error);
        Button buttonConvertir = findViewById(R.id.button_Convertir);
        RadioGroup rgSentido = findViewById(R.id.rg_Sentido);

        rgSentido.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rb = group.findViewById(checkedId);
            Log.i(TAG, "Botón pulsado: " + rb.getText());
            etValor.setHint(checkedId == R.id.rb_PulgCm
                    ? R.string.hintPulgadas : R.string.hintCentimetros);
        });

        buttonConvertir.setOnClickListener(view -> {
            Log.i(TAG, "Botón pulsado: Convertir");
            try {
                boolean pulgadasACm = rgSentido.getCheckedRadioButtonId() == R.id.rb_PulgCm;
                double factor = pulgadasACm ? PULGADA_A_CM : 1 / PULGADA_A_CM;
                tvResultado.setText(convertir(etValor.getText().toString(), factor));
                tvError.setVisibility(View.GONE);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                tvResultado.setText("");
                tvError.setText(e.getMessage());
                tvError.setVisibility(View.VISIBLE);
            }
        });
    }

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