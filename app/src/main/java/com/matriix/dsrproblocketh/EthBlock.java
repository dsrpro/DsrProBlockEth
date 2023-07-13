package com.matriix.dsrproblocketh;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class EthBlock extends AppCompatActivity {

    private EthereumManager ethereumManager;
    private TextView tokenBalancesTextView;
    private TextView ethBalanceTextView;
    private static final String PRIVATE_KEY = "ca8476bc40031e4f1ba372440268232e0083b5373babcd187c38d4e79fbb70e1";
    private static final String INFURA_PROJECT_ID = "345123047cc74565857cc86473bedd51";
    private TextView textViewTokenBalances;
    private ListView listViewTokenBalances;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eth_block);
        ethBalanceTextView = findViewById(R.id.textViewBalance);
        tokenBalancesTextView = findViewById(R.id.textViewTokenBalances);
        listViewTokenBalances = findViewById(R.id.listViewTokenBalances);
        //ethereumManager = new EthereumManager("deaddeaddeaddeaddeaddeaddeaddeaddeaddeaddeaddeaddeaddeddeaddeadd", "https://mainnet.infura.io/v3/345123047cc74565857cc86473bedd51");
        //ethereumManager = new EthereumManager(PRIVATE_KEY, INFURA_PROJECT_ID);

        if (MainActivityNew.NetworkUtils.isNetworkAvailable(this)) {
            // Connexion Internet disponible, effectuer des opérations réseau ici
            Toast.makeText(this, "connexion Internet Disponible", Toast.LENGTH_SHORT).show();
            try {
                LoadPrivateKeysTask task = new LoadPrivateKeysTask();
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

    private class LoadPrivateKeysTask extends AsyncTask<Void, Void, Void> {
        private Web3j web3j;
        private Credentials credentials;

        public LoadPrivateKeysTask(Web3j web3j, Credentials credentials) {
            this.web3j = web3j;
            this.credentials = credentials;
        }

        public LoadPrivateKeysTask() {
            super();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Lecture du fichier "config.txt" dans le répertoire "assets"
            try {
                InputStream inputStream = getAssets().open("config.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                List<String> privateKeys = new ArrayList<>();

                // Lire chaque ligne du fichier et ajouter la clé privée à la liste
                while ((line = reader.readLine()) != null) {
                    privateKeys.add(line);
                }

                reader.close();
                inputStream.close();
                // Code pour récupérer les soldes des jetons ERC20
                StringBuilder balancesBuilder = new StringBuilder();
                // Parcourir la liste des clés privées
                for (String privateKey : privateKeys) {
                    try {
                        // Créer une instance d'EthereumManager avec la clé privée et l'URL du réseau
                        String networkUrl = "https://mainnet.infura.io/v3/" + INFURA_PROJECT_ID;
                        EthereumManager ethereumManager = new EthereumManager(privateKey, networkUrl);
                        // Obtenir le solde ETH
                        ethereumManager.getEtherBalance();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    // Obtenir le solde des tokens
                    //ethereumManager.getAllTokenBalances();
                    // Récupérez l'adresse Ethereum à partir des informations d'identification
/*                    String address = credentials.getAddress();
                    // Effectuez une requête pour obtenir le solde de l'adresse Ethereum
                    EthGetBalance balanceResponse = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
                    // Récupérez le solde en Wei
                    BigInteger balanceInWei = balanceResponse.getBalance();
                    // Convertissez le solde en Ether
                    BigDecimal balanceInEther = Convert.fromWei(balanceInWei.toString(), Convert.Unit.ETHER);
                    balancesBuilder.append("Solde ETH: ").append(balanceInEther).append("\n");*/

                    ERC20Token token = ERC20Token.load(privateKey, web3j, credentials);
                    try {

                        String name = token.name().send();
                        BigInteger balance = token.balanceOf(credentials.getAddress()).send();
                        balancesBuilder.append("Token Name: ").append(name).append("\n");
                        balancesBuilder.append("Token Balance: ").append(balance).append("\n");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //return balancesBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Mettre à jour l'interface utilisateur (UI) ici
            // Afficher les résultats dans un ListView ou tout autre composant souhaité
            try {
                List<String> balancesList = new ArrayList<>();
                balancesList.add(String.valueOf(aVoid));

                ArrayAdapter<String> adapter = new ArrayAdapter<>(EthBlock.this,
                        android.R.layout.simple_list_item_1, balancesList);

                listViewTokenBalances.setAdapter(adapter);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }


}
