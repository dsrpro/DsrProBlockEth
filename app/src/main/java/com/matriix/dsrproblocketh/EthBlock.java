package com.matriix.dsrproblocketh;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EthBlock extends AppCompatActivity {

    private EthereumManager ethereumManager;
    private TextView tokenBalancesTextView;
    private TextView ethBalanceTextView;
    private static final String PRIVATE_KEY = "ca8476bc40031e4f1ba372440268232e0083b5373babcd187c38d4e79fbb70e1";
    private static final String INFURA_PROJECT_ID = "345123047cc74565857cc86473bedd51";
    private TextView textViewTokenBalances;
    private ListView listViewTokenBalances;
    public String KPrive = PRIVATE_KEY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eth_block);
        ethBalanceTextView = findViewById(R.id.textViewBalance);
        tokenBalancesTextView = findViewById(R.id.textViewTokenBalances);
        listViewTokenBalances = findViewById(R.id.listViewTokenBalances);
        //ethereumManager = new EthereumManager("deaddeaddeaddeaddeaddeaddeaddeaddeaddeaddeaddeaddeaddeddeaddeadd", "https://mainnet.infura.io/v3/345123047cc74565857cc86473bedd51");
        //ethereumManager = new EthereumManager(PRIVATE_KEY, INFURA_PROJECT_ID);
        // Créez le répertoire "application"
        File appDirectory = createAppDirectory();
        // Utilisez le répertoire pour stocker ou accéder aux données de l'application
        // ...
        File file = new File(appDirectory, "dataDsrProBlock.txt");
        try {
            file.createNewFile();
            // Écrivez ou lisez les données dans le fichier
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (MainActivityNew.NetworkUtils.isNetworkAvailable(this)) {
            // Connexion Internet disponible, effectuer des opérations réseau ici
            Toast.makeText(this, "connexion Internet Disponible", Toast.LENGTH_LONG).show();
            try {
                String[] privateKeys = readPrivateKeyFromFile();
                // Initialisez Web3j avec votre URL de réseau
                Web3j web3j = Web3j.build(new HttpService("https://mainnet.infura.io/v3/"+INFURA_PROJECT_ID));
                for (String privateKey : privateKeys) {
                    // Utilisez la clé privée ici
                    // Effectuez vos opérations avec la clé privée dans la boucle
                    KPrive = privateKey;
                    Credentials credentials = Credentials.create(KPrive);
                    LoadPrivateKeysTask task = new LoadPrivateKeysTask(web3j, credentials, EthBlock.this);
                    task.execute();
                }
            } catch (Exception e) {
                e.printStackTrace();
                //return "Erreur lors de la récupération du solde";
            }
        } else {
            // Pas de connexion Internet, afficher un message d'erreur ou prendre une autre action appropriée
            Toast.makeText(this, "Pas de connexion Internet", Toast.LENGTH_LONG).show();
        }
    }

    private String[] readPrivateKeyFromFile() {

        File file = new File
                (Environment.getExternalStorageDirectory(), "//applicationDsrPro//config.ini");
        if (file.exists()) {
            // Le fichier existe
            Toast.makeText(this, "Le fichier config.ini existe", Toast.LENGTH_LONG).show();
            if (file.canRead()) {
                // Le fichier est lisible
                Toast.makeText(this, "Le fichier config.ini est lisible", Toast.LENGTH_LONG).show();
                ArrayList<String> privateKeyList = new ArrayList<>();
                Log.d("TAG", "Répertoire lu avec succès");
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String line;
                    // Lire chaque ligne du fichier
                    while ((line = reader.readLine()) != null) {
                        String[] privateKeys = line.split(";");
                        for (String key : privateKeys) {
                            if (!key.trim().isEmpty()) {
                                privateKeyList.add(key.trim());
                                System.out.println("Résultat obtenu du config : " + privateKeyList);
                            }
                        }
                    }
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return new String[0]; // En cas d'erreur, retourne un tableau vide
                }

                // Convertir la liste en tableau de chaînes
                String[] privateKeyArray = new String[privateKeyList.size()];
                privateKeyArray = privateKeyList.toArray(privateKeyArray);

                return privateKeyArray;
            } else {
                // Le fichier n'est pas lisible
                Toast.makeText(this, "Le fichier config.ini n'est pas lisible", Toast.LENGTH_LONG).show();
            }
        } else {
            // Le fichier n'existe pas
            Toast.makeText(this, "Le fichier config.ini n'existe pas", Toast.LENGTH_LONG).show();
        }

         return new String[0];
    }

    private class LoadPrivateKeysTask extends AsyncTask<Void, Void, Void> {

        private Web3j web3j;
        private Credentials credentials;
        private Context context;

        public LoadPrivateKeysTask(Web3j web3j, Credentials credentials, Context context) {
            this.web3j = web3j;
            this.credentials = credentials;
            this.context = context;
        }
        public LoadPrivateKeysTask() {
            super();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Utiliser la clé privée pour obtenir les soldes ETH et tokens
            // Effectuer les opérations souhaitées avec les soldes
            // Parcourir la liste des clés privées
            StringBuilder balancesBuilder = new StringBuilder();

            try {
                // Créer une instance d'EthereumManager avec la clé privée et l'URL du réseau
                String networkUrl = "https://mainnet.infura.io/v3/" + INFURA_PROJECT_ID;
                EthereumManager ethereumManager = new EthereumManager(KPrive, networkUrl);
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

            ERC20Token token = ERC20Token.load(KPrive, web3j, credentials);
            try {

                String name = token.name().send();
                BigInteger balance = token.balanceOf(credentials.getAddress()).send();
                balancesBuilder.append("Token Name: ").append(name).append("\n");
                balancesBuilder.append("Token Balance: ").append(balance).append("\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
            //return balancesBuilder.toString();
            return null;
        }
        private void processPrivateKey(String privateKey) {

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Mettre à jour l'interface utilisateur (UI) ici
            // Afficher les résultats dans un ListView ou tout autre composant souhaité
            try {
                List<String> balancesList = new ArrayList<>();
                balancesList.add(String.valueOf(aVoid));
                // Afficher le résultat avant la capture de l'exception
                System.out.println("Résultat obtenu : " + aVoid);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(EthBlock.this,
                        android.R.layout.simple_list_item_1, balancesList);

                listViewTokenBalances.setAdapter(adapter);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    private File createAppDirectory() {
        // Obtenez le répertoire racine de l'espace de stockage externe
        File rootDir = getExternalFilesDir(null);

        // Créez un sous-répertoire nommé "applicationDsrPro" dans le répertoire racine
        File appDir = new File(rootDir, "applicationDsrPro");

        // Vérifiez si le répertoire existe déjà, sinon créez-le
        if (!appDir.exists()) {
            appDir.mkdirs();
            if (appDir.mkdirs()) {
                Log.d("TAG", "Répertoire créé avec succès");
            } else {
                Log.d("TAG", "Échec de la création du répertoire");
            }
        }

        return appDir;
    }

    private File createAppDirectory(String Directory) {
        // Obtenez le répertoire racine de l'espace de stockage externe
        File rootDir = getExternalFilesDir(null);

        // Créez un sous-répertoire nommé "applicationDsrPro" dans le répertoire racine
        File appDir = new File(rootDir, Directory);

        // Vérifiez si le répertoire existe déjà, sinon créez-le
        if (!appDir.exists()) {
            appDir.mkdirs();
            if (appDir.mkdirs()) {
                Log.d("TAG", "Répertoire créé avec succès");
            } else {
                Log.d("TAG", "Échec de la création du répertoire");
            }
        }

        return appDir;
    }


}
