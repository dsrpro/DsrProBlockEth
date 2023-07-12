package com.matriix.dsrproblocketh;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public class EthereumManager {
    private Web3j web3j;
    private Credentials credentials;

    public EthereumManager(String privateKey, String networkUrl) {
        web3j = Web3j.build(new HttpService(networkUrl));
        credentials = Credentials.create(privateKey);
    }

    public String getEtherBalance() {
        try {
            EthGetBalance ethGetBalance = web3j
                    .ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();

            BigInteger weiBalance = ethGetBalance.getBalance();
            BigDecimal etherBalance = Convert.fromWei(weiBalance.toString(), Convert.Unit.ETHER);
            System.out.println("Ether Balance: " + etherBalance);
            //return etherBalance.toString();
            return etherBalance.toPlainString();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

/*    public void getAllTokenBalances() {
        String[] tokenAddresses = {
                "0xdac17f958d2ee523a2206206994597c13d831ec7",
                "0x95ad61b0a150d79219dcf64e1e6cc01f0b64c4ce"
                // Ajoutez d'autres adresses de contrats ERC20 connus ici
        };

        for (String tokenAddress : tokenAddresses) {
            ERC20Token token = ERC20Token.load(tokenAddress, web3j, getTransactionManager());
            try {
                String name = token.name().send();
                BigInteger balance = token.balanceOf(credentials.getAddress()).send();
                System.out.println("Token Name: " + name);
                System.out.println("Token Balance: " + balance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
  *//*      try{
            // Récupérer les soldes des jetons ERC20
            Map<String, String> tokenBalances = new HashMap<>();
            // ... (Logique pour récupérer les soldes des jetons ERC20)

            // Convertir les soldes en une chaîne de texte
            StringBuilder balancesText = new StringBuilder();
            for (Map.Entry<String, String> entry : tokenBalances.entrySet()) {
                String tokenName = entry.getKey();
                String balance = entry.getValue();
                balancesText.append(tokenName).append(": ").append(balance).append("\n");
            }

        }catch (Exception e) {
            e.printStackTrace();
        }*//*


*//*        // Appeler la méthode displayTokenBalances() de MainActivity pour afficher les résultats
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.displayTokenBalances(balancesText.toString());
        }*//*
    }*/

    public String getAllTokenBalances() {
        String[] tokenAddresses = {
                "0xdac17f958d2ee523a2206206994597c13d831ec7",
                "0x95ad61b0a150d79219dcf64e1e6cc01f0b64c4ce",
                "0xa2E3356610840701BDf5611a53974510Ae27E2e1",
                ""
                // Ajoutez d'autres adresses de contrats ERC20 connus ici
        };

        StringBuilder balancesBuilder = new StringBuilder();

        for (String tokenAddress : tokenAddresses) {
            ERC20Token token = ERC20Token.load(tokenAddress, web3j, credentials);
            try {
                String name = token.name().send();
                BigInteger balance = token.balanceOf(credentials.getAddress()).send();
                balancesBuilder.append("Token Name: ").append(name).append("\n");
                balancesBuilder.append("Token Balance: ").append(balance).append("\n\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return balancesBuilder.toString();
    }


    private TransactionManager getTransactionManager() {
        return new RawTransactionManager(web3j, credentials);
    }
}
