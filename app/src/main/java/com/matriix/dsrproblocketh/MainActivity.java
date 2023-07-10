package com.matriix.dsrproblocketh;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
/*

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}*/
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String INFURA_PROJECT_ID = "345123047cc74565857cc86473bedd51";
    private static final String ETHEREUM_ADDRESS = "0xb732dFaa9Dbd47dd26277A45D0AFdF4A733c43E2";
    private static final String PRIVATE_KEY = "deaddeaddeaddeaddeaddeaddeaddeaddeaddeaddeaddeaddeaddeddeaddeadd";

    private TextView balanceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        balanceTextView = findViewById(R.id.textViewBalance);

        // Vérifier la connectivité Internet
        if (NetworkUtils.isNetworkAvailable(this)) {
            // Connexion Internet disponible, effectuer des opérations réseau ici
            Toast.makeText(this, "connexion Internet Disponible", Toast.LENGTH_SHORT).show();
            try {
                // Créez une instance de Web3j en spécifiant l'URL du nœud Ethereum
                Web3j web3j = Web3j.build(new HttpService("https://mainnet.infura.io/v3/" + INFURA_PROJECT_ID));

                // Créez une instance de Credentials à partir de la clé privée
                Credentials credentials = Credentials.create(PRIVATE_KEY);

                // Exécutez une tâche asynchrone pour récupérer le solde du portefeuille Ethereum
                GetEthereumBalanceTask task = new GetEthereumBalanceTask(web3j, credentials);
                task.execute();

            } catch (Exception e) {
                e.printStackTrace();
                //return "Erreur lors de la récupération du solde";
            }
        } else {
            // Pas de connexion Internet, afficher un message d'erreur ou prendre une autre action appropriée
            Toast.makeText(this, "Pas de connexion Internet", Toast.LENGTH_SHORT).show();
        }

    }

    private void displayBalance(String balance) {
        balanceTextView.setText(balance + " ETH");
    }

    public static class NetworkUtils {

        public static boolean isNetworkAvailable(Context context) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }
            return false;
        }
    }

    private class GetEthereumBalanceTask extends AsyncTask<Void, Void, String> {

        private Web3j web3j;
        private Credentials credentials;

        public GetEthereumBalanceTask(Web3j web3j, Credentials credentials) {
            this.web3j = web3j;
            this.credentials = credentials;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                // Récupérez l'adresse Ethereum à partir des informations d'identification
                String address = credentials.getAddress();

                // Effectuez une requête pour obtenir le solde de l'adresse Ethereum
                EthGetBalance balanceResponse = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();

                // Récupérez le solde en Wei
                BigInteger balanceInWei = balanceResponse.getBalance();

                // Convertissez le solde en Ether
                BigDecimal balanceInEther = Convert.fromWei(balanceInWei.toString(), Convert.Unit.ETHER);

                // Retournez le solde du portefeuille Ethereum sous forme de chaîne
                return balanceInEther.toPlainString();
            } catch (Exception e) {
                e.printStackTrace();
                return "Erreur lors de la récupération du solde";
            }
        }

        @Override
        protected void onPostExecute(String balance) {
            displayBalance(balance);
        }
    }
}
