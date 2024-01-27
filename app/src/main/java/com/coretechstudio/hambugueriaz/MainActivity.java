package com.coretechstudio.hambugueriaz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView quantidadeTextView;
    private EditText nomeEditText;
    private CheckBox baconCheckBox;
    private CheckBox queijoCheckBox;
    private CheckBox onionRingsCheckBox;
    private int quantidade = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quantidadeTextView = findViewById(R.id.quantidadeView);
        nomeEditText = findViewById(R.id.nome);
        baconCheckBox = findViewById(R.id.bacon);
        queijoCheckBox = findViewById(R.id.queijo);
        onionRingsCheckBox = findViewById(R.id.onionRings);

        Button somarButton = findViewById(R.id.somarBotao);
        somarButton.setOnClickListener(v -> somar());

        Button subtrairButton = findViewById(R.id.subtrairBotao);
        subtrairButton.setOnClickListener(v -> subtrair());

        Button enviarPedidoButton = findViewById(R.id.fazerPedidoBotao);
        enviarPedidoButton.setOnClickListener(v -> enviarPedido());
    }

    private void somar() {
        if (quantidade < Integer.MAX_VALUE) {
            quantidade++;
            atualizarQuantidade();
        }
    }

    private void subtrair() {
        if (quantidade > 0) {
            quantidade--;
            atualizarQuantidade();
        }
    }

    private void atualizarQuantidade() {
        quantidadeTextView.setText(String.valueOf(quantidade));
    }

    private void enviarPedido() {
        String nomeCliente = nomeEditText.getText().toString().trim();

        if (nomeCliente.isEmpty()) {
            Toast.makeText(this, "Por favor, insira o nome do cliente.", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean temBacon = baconCheckBox.isChecked();
        boolean temQueijo = queijoCheckBox.isChecked();
        boolean temOnionRings = onionRingsCheckBox.isChecked();

        int precoBase = 20;
        int precoBacon = 2;
        int precoQueijo = 2;
        int precoOnionRings = 3;

        int precoAdicionais = (temBacon ? precoBacon : 0) + (temQueijo ? precoQueijo : 0) + (temOnionRings ? precoOnionRings : 0);
        int precoFinal = (precoBase + precoAdicionais) * quantidade;

        exibirResumoPedido(nomeCliente, temBacon, temQueijo, temOnionRings, quantidade, precoFinal);
    }

    private void exibirResumoPedido(String nomeCliente, boolean temBacon, boolean temQueijo, boolean temOnionRings, int quantidade, int precoFinal) {
        String mensagemResumo = "Nome do cliente: " + nomeCliente + "\n" +
                "Tem Bacon? " + (temBacon ? "Sim" : "Não") + "\n" +
                "Tem Queijo? " + (temQueijo ? "Sim" : "Não") + "\n" +
                "Tem Onion Rings? " + (temOnionRings ? "Sim" : "Não") + "\n" +
                "Quantidade: " + quantidade + "\n" +
                "Preço final: R$ " + precoFinal;

        TextView resumoPedidoTextView = findViewById(R.id.resumo);
        resumoPedidoTextView.setText(mensagemResumo);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Pedido de " + nomeCliente);
        intent.putExtra(Intent.EXTRA_TEXT, mensagemResumo);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Nenhum aplicativo de e-mail encontrado", Toast.LENGTH_SHORT).show();
        }
    }
}