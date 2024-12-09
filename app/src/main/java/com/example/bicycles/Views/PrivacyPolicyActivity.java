package com.example.bicycles.Views;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bicycles.R;

public class PrivacyPolicyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        // Referencia al TextView
        TextView textViewPrivacy = findViewById(R.id.tv_privacy_policy);

        // Establecer texto con las políticas de privacidad
        String privacyText = "Política de Privacidad\n\n" +
                "1. Recopilación de datos:\n" +
                "No recopilamos datos personales sin su consentimiento. Podemos solicitar permisos de acceso a ciertos datos del dispositivo (como ubicación o cámara) para funcionalidades específicas.\n\n" +
                "2. Uso de datos:\n" +
                "Los datos recopilados se utilizan exclusivamente para proporcionar una mejor experiencia en la aplicación. Nunca compartimos sus datos con terceros sin su autorización explícita.\n\n" +
                "3. Seguridad:\n" +
                "Implementamos medidas técnicas y organizativas para proteger su información contra accesos no autorizados, pérdidas o daños. Sin embargo, no podemos garantizar la seguridad completa debido a riesgos inherentes en el entorno digital.\n\n" +
                "4. Cookies y tecnologías similares:\n" +
                "Podemos usar cookies o tecnologías similares para mejorar su experiencia en la aplicación, analizar el tráfico y personalizar el contenido.\n\n" +
                "5. Derechos del usuario:\n" +
                "Usted tiene derecho a acceder, rectificar o eliminar sus datos personales en cualquier momento. Para ello, puede contactarnos a través de los canales proporcionados en la aplicación.\n\n" +
                "6. Cambios en la política:\n" +
                "Nos reservamos el derecho de actualizar estas políticas en cualquier momento. Por favor, revíselas periódicamente para mantenerse informado de cualquier cambio.\n\n" +
                "7. Contacto:\n" +
                "Si tiene preguntas o inquietudes sobre esta política de privacidad, puede contactarnos a través de soporte técnico.";
        textViewPrivacy.setText(privacyText);

        // Configurar el botón de regreso
        Button btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());
    }
}
