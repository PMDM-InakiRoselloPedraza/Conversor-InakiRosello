package dam.inaki.u2p4conversor;

import android.os.Bundle;
import android.util.Log;
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
        Button buttonConvertir = findViewById(R.id.button_Convertir);
        RadioGroup rgSentido = findViewById(R.id.rg_Sentido);

        // Activ 1.1 Cambia el hint según el sentido elegido
        rgSentido.setOnCheckedChangeListener((group, checkedId) ->
                etValor.setHint(checkedId == R.id.rb_PulgCm
                        ? R.string.hintPulgadas : R.string.hintCentimetros));

        buttonConvertir.setOnClickListener(view -> {
            try {
                // Activ 1.1 Two-way conversion inch <-> cm
                boolean pulgadasACm = rgSentido.getCheckedRadioButtonId() == R.id.rb_PulgCm;
                double factor = pulgadasACm ? PULGADA_A_CM : 1 / PULGADA_A_CM;
                tvResultado.setText(convertir(etValor.getText().toString(), factor));
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        });
    }

    // Activ 1.1 Metodo genérico: sirve para los dos sentidos usando el factor
    // Activ 1.2 Resultado formateado con dos decimales
    private String convertir(String texto, double factor) {
        double resultado = Double.parseDouble(texto.replace(',', '.')) * factor;
        return String.format(Locale.getDefault(), "%.2f", resultado);
    }
}