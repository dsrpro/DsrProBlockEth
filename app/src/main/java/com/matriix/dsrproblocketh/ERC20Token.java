package com.matriix.dsrproblocketh;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;


public class ERC20Token extends Contract {
    private final String contractAddress;


    public ERC20Token(String contractAddress, Web3j web3j, Credentials transactionManager) {
        super("", contractAddress, web3j, transactionManager, new DefaultGasProvider());
        this.contractAddress = contractAddress;
    }
/*
        this.contractAddress = contractAddress;
    }*/

    public static ERC20Token load(String contractAddress, Web3j web3j, Credentials credentials) {
        //String contractAddress1 = new String();
        return new ERC20Token(contractAddress, web3j, credentials);
    }


    public RemoteCall<String> name() {
        Function function = new Function("name", Collections.emptyList(), Collections.singletonList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }


    public RemoteCall<BigInteger> balanceOf(String address) {
        Function function = new Function(
                "balanceOf",
                Arrays.asList(new org.web3j.abi.datatypes.Address(address)),
                Collections.singletonList(new TypeReference<Uint>() {})
        );

        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }
    // Ajoutez d'autres fonctions spécifiques à votre jeton ERC20 ici

    // Méthode utilitaire pour décoder les valeurs de retour des fonctions
/*
    public static <T extends Type> T decodeValue(String encodedValue, TypeReference<T> typeReference) {
        try {
            List<Type> decodedValues = FunctionReturnDecoder.decode(encodedValue, typeReference.returnType());
            if (decodedValues != null && !decodedValues.isEmpty()) {
                return (T) decodedValues.get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
*/
    // Exemple de méthode pour effectuer une transaction sur le contrat
    public RemoteCall<TransactionReceipt> transfer(String to, BigInteger amount) {
        Function function = new Function(
                "transfer",
                Arrays.asList(new Address(to), new Uint256(amount)),
                Collections.emptyList());

        return executeRemoteCallTransaction(function);
    }

}


