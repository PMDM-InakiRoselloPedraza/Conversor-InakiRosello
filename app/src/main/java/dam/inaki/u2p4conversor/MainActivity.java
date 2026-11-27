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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "LogsConversor";
    private static final double PULGADA_A_CM = 2.54;

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
        // Activ 3.1 Log del ciclo de vida
        Log.d(TAG, "onCreate");
    }

    private void setUI() {
        EditText etValor = findViewById(R.id.et_Valor);
        TextView tvResultado = findViewById(R.id.tv_Resultado);
        TextView tvError = findViewById(R.id.tv_Error);
        Button buttonConvertir = findViewById(R.id.button_Convertir);
        RadioGroup rgSentido = findViewById(R.id.rg_Sentido);

        rgSentido.setOnCheckedChangeListener((group, checkedId) -> {
            // Activ 3.2 Log al pulsar un botón (RadioButton) indicando cuál
            RadioButton rb = group.findViewById(checkedId);
            Log.i(TAG, "Botón pulsado: " + rb.getText());
            etValor.setHint(checkedId == R.id.rb_PulgCm
                    ? R.string.hintPulgadas : R.string.hintCentimetros);
        });

        buttonConvertir.setOnClickListener(view -> {
            // Activ 3.2 Log al pulsar un botón indicando cuál
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

    // Activ 3.1 Estados del ciclo de vida de la Activity
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