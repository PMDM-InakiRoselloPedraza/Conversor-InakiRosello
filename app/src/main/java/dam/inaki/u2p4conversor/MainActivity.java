package dam.empar.u2p4conversor;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    }

    private void setUI() {
        EditText etValor = findViewById(R.id.et_Valor);
        TextView tvResultado = findViewById(R.id.tv_Resultado);
        TextView tvError = findViewById(R.id.tv_Error);
        Button buttonConvertir = findViewById(R.id.button_Convertir);
        RadioGroup rgSentido = findViewById(R.id.rg_Sentido);

        rgSentido.setOnCheckedChangeListener((group, checkedId) ->
                etValor.setHint(checkedId == R.id.rb_PulgCm
                        ? R.string.hintPulgadas : R.string.hintCentimetros));

        buttonConvertir.setOnClickListener(view -> {
            try {
                boolean pulgadasACm = rgSentido.getCheckedRadioButtonId() == R.id.rb_PulgCm;
                double factor = pulgadasACm ? PULGADA_A_CM : 1 / PULGADA_A_CM;
                tvResultado.setText(convertir(etValor.getText().toString(), factor));
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
    }

    // TODO: Activ 2.1 convertir lanza excepciones que captura el try...catch de setUI()
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
}