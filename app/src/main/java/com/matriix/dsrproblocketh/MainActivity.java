package com.matriix.dsrproblocketh;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EthereumManager ethereumManager;
    private TextView tokenBalancesTextView;
    private TextView ethBalanceTextView;
    private static final String PRIVATE_KEY = "ca8476bc40031e4f1ba372440268232e0083b5373babcd187c38d4e79fbb70e1";
    private static final String INFURA_PROJECT_ID = "345123047cc74565857cc86473bedd51";
    private TextView textViewTokenBalances;
    private ListView listViewTokenBalances;
    private Button btnNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ethBalanceTextView = findViewById(R.id.textViewBalance);
        tokenBalancesTextView = findViewById(R.id.textViewTokenBalances);
        listViewTokenBalances = findViewById(R.id.listViewTokenBalances);
        btnNext = findViewById(R.id.btnNext);
        //ethereumManager = new EthereumManager("deaddeaddeaddeaddeaddeaddeaddeaddeaddeaddeaddeaddeaddeddeaddeadd", "https://mainnet.infura.io/v3/345123047cc74565857cc86473bedd51");
        //ethereumManager = new EthereumManager(PRIVATE_KEY, INFURA_PROJECT_ID);
        //deaddeaddeaddeaddeaddeaddeaddeaddeaddeaddeaddeaddeaddeddeaddeadd

        // Obtenir le solde Ethereum
        //ethereumManager.getEtherBalance();
        // Vérifier la connectivité Internet
        if (MainActivityNew.NetworkUtils.isNetworkAvailable(this)) {
            // Connexion Internet disponible, effectuer des opérations réseau ici
            Toast.makeText(this, "connexion Internet Disponible", Toast.LENGTH_SHORT).show();
            try {
                    //String etherBalance = ethereumManager.getEtherBalance();
/*                System.out.println("Ether Balance: " + etherBalance);
                    //ethBalanceTextView.setText(etherBalance + " ETH");

                ethBalanceTextView.setText(etherBalance);*/

                // Obtenir les soldes des jetons ERC20
                //ethereumManager.getAllTokenBalances();
/*                String tokenBalances = ethereumManager.getAllTokenBalances();
                System.out.println("Token Balance: " + tokenBalances);
                tokenBalancesTextView.setText(tokenBalances);*/
                // Créez une instance de Web3j en spécifiant l'URL du nœud Ethereum
                Web3j web3j = Web3j.build(new HttpService("https://mainnet.infura.io/v3/" + INFURA_PROJECT_ID));

                // Créez une instance de Credentials à partir de la clé privée
                Credentials credentials = Credentials.create(PRIVATE_KEY);
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

        // Exécutez une tâche asynchrone pour récupérer le solde du portefeuille Ethereum
        // Créez une instance de Web3j en spécifiant l'URL du nœud Ethereum
/*        Web3j web3j = Web3j.build(new HttpService("https://mainnet.infura.io/v3/" + INFURA_PROJECT_ID));

        // Créez une instance de Credentials à partir de la clé privée
        Credentials credentials = Credentials.create(PRIVATE_KEY);
        MainActivityNew.GetEthereumBalanceTask task = new MainActivityNew.GetEthereumBalanceTask(web3j, credentials);
        task.execute();*/


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Code à exécuter lors du clic sur le bouton
                    Intent intent = new Intent(MainActivity.this, EthBlock.class);
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

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
                // Code pour récupérer les soldes des jetons ERC20
                StringBuilder balancesBuilder = new StringBuilder();

                String[] tokenAddresses = {
                        "0xdac17f958d2ee523a2206206994597c13d831ec7",
                        "0x95ad61b0a150d79219dcf64e1e6cc01f0b64c4ce",
                        "0x6982508145454ce325ddbe47a25d4ec3d2311933",
                        "0x0f5d2fb29fb7d3cfee444a200298f468908cc942",
                        "0x68749665FF8D2d112Fa859AA293F07A622782F38"

                        // Ajoutez d'autres adresses de contrats ERC20 connus ici
                };
                // Récupérez l'adresse Ethereum à partir des informations d'identification
                String address = credentials.getAddress();

                // Effectuez une requête pour obtenir le solde de l'adresse Ethereum
                EthGetBalance balanceResponse = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();

                // Récupérez le solde en Wei
                BigInteger balanceInWei = balanceResponse.getBalance();

                // Convertissez le solde en Ether
                BigDecimal balanceInEther = Convert.fromWei(balanceInWei.toString(), Convert.Unit.ETHER);
                balancesBuilder.append("Solde ETH: ").append(balanceInEther).append("\n");
                for (String tokenAddress : tokenAddresses) {
                    ERC20Token token = ERC20Token.load(tokenAddress, web3j, credentials);
              try {

                        String name = token.name().send();
                        BigInteger balance = token.balanceOf(credentials.getAddress()).send();
                        balancesBuilder.append("Token Name: ").append(name).append("\n");
                        balancesBuilder.append("Token Balance: ").append(balance).append("\n");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                return balancesBuilder.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "Erreur lors de la récupération des soldes des jetons";
            }
        }

        @Override
        protected void onPostExecute(String balances) {
            try {
                List<String> balancesList = new ArrayList<>();
                balancesList.add(balances);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                        android.R.layout.simple_list_item_1, balancesList);

                listViewTokenBalances.setAdapter(adapter);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }


    }

/*    private void displayTokenBalances(String tokenBalances) {
        tokenBalancesTextView.setText(tokenBalances);
    }*/
/*    private void displayBalances(String ethBalance, String tokenBalances) {
        // Affichez les soldes ETH et des jetons dans votre interface utilisateur
        ethBalanceTextView.setText(ethBalance + " ETH");
        tokenBalancesTextView.setText(tokenBalances);*/
    }
/*    private class GetEthereumBalanceTask extends AsyncTask<Void, Void, String> {

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

    }*/

